/*
    A simple 2 phase XA demo. Both the branches talk to the same RM
    Need a java enabled 8.1.6 database to run this demo.
      -> start-1
      -> start-2
      -> Do some DML on 1
      -> Do some DML on 2
      -> end 1
      -> end 2
      -> prepare-1
      -> prepare-2
      -> commit-2

   Please use jdk1.2 or later version
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;
import javax.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;
import oracle.jdbc.xa.OracleXid;
import oracle.jdbc.xa.OracleXAException;
import oracle.jdbc.xa.client.*;
import javax.transaction.xa.*;

class XA3
{
  public static void main (String args [])
       throws SQLException
  {

    try
    {
        String url = "jdbc:oracle:oci8:@";
        try {
          String url1 = System.getProperty("JDBC_URL");
          if (url1 != null)
            url = url1;
        } catch (Exception e) {
          // If there is any security exception, ignore it
          // and use the default
        }

        // Create a OracleDataSource instance and set properties
        OracleDataSource ods = new OracleDataSource();
        ods.setUser("hr");
        ods.setPassword("hr");
        ods.setURL(url);

        // Connect to the database
        Connection conn = ods.getConnection();

        Statement stmt = conn.createStatement ();

        try
        {
          stmt.execute ("delete from jobs where job_id = 'SC_STUFF'");
        }
        catch (SQLException e)
        {
          // Ignore an error here
        }

        try
        {
          stmt.execute ("delete from regions where region_id > 100");
        }
        catch (SQLException e)
        {
          // Ignore an error here
        }

        try
        {   
          stmt.execute ("create table my_tab (col1 char(30))");
        }
        catch (SQLException e)
        {
          // Ignore an error here too
        }

        // Create a XADataSource instance
        OracleXADataSource oxds = new OracleXADataSource();
        oxds.setURL(url);
        oxds.setUser("hr");
        oxds.setPassword("hr");
    
        // Get a XA connection to the underlying data source
        XAConnection pc1  = oxds.getXAConnection();

        // We can use the same data source 
        XAConnection pc2  = oxds.getXAConnection();

        // Get the Physical Connections
        Connection conn1 = pc1.getConnection();
        Connection conn2 = pc2.getConnection();

        // Get the XA Resources
        XAResource oxar1 = pc1.getXAResource();
        XAResource oxar2 = pc2.getXAResource();

        // Create the Xids With the Same Global Ids
        Xid xid1 = createXid(1);
        Xid xid2 = createXid(2);

        // Start the Resources
        oxar1.start (xid1, XAResource.TMNOFLAGS);
        oxar2.start (xid2, XAResource.TMNOFLAGS);

        // Do  something with conn1 and conn2
        DoSomeWork1 (conn1);
        DoSomeWork2 (conn2);

        // END both the branches -- THIS IS MUST
        oxar1.end(xid1, XAResource.TMSUCCESS);
        oxar2.end(xid2, XAResource.TMSUCCESS);

        // Prepare the RMs
        int prp1 =  oxar1.prepare (xid1);
        int prp2 =  oxar2.prepare (xid2);

        System.out.println("Return value of prepare 1 is " + prp1);
        System.out.println("Return value of prepare 2 is " + prp2);

        boolean do_commit = true;

        /*
         * If both the branches are to the same RM, oracle does some
         * optimization. All but one branch return XA_RDONLY. Only
         * one branch will return XA_OK or failure. Commit or Rollback
         * has to be called on this branch only accordingly.
         */

        if (!((prp1 == XAResource.XA_OK) || (prp1 == XAResource.XA_RDONLY)))
           do_commit = false;

        if (!((prp2 == XAResource.XA_OK) || (prp2 == XAResource.XA_RDONLY)))
           do_commit = false;

        System.out.println("do_commit is " + do_commit);

        System.out.println("Is oxar1 same as oxar2 ? " + oxar1.isSameRM(oxar2));
        if (prp1 == XAResource.XA_OK)
          if (do_commit)
             oxar1.commit (xid1, false);
          else
             oxar1.rollback (xid1);

        if (prp2 == XAResource.XA_OK)
          if (do_commit)
             oxar2.commit (xid2, false);
          else
             oxar2.rollback (xid2);

        // Close connections
        conn1.close();
        conn1 = null;
        conn2.close();
        conn2 = null;

        pc1.close();
        pc1 = null;
        pc2.close();
        pc2 = null;

        ResultSet rset = stmt.executeQuery ("select job_id, job_title from jobs");
        System.out.println("\ncontents of table jobs:\n");
        while (rset.next())
          System.out.println(rset.getString(1) + " " + rset.getString(2));
  
        rset.close();
        rset = null;

        rset = stmt.executeQuery ("select region_id, region_name from regions order by region_id");
        System.out.println("\ncontents of table regions:\n");
        while (rset.next())
          System.out.println(rset.getInt(1) + " " + rset.getString(2));
  
        rset.close();
        rset = null;

        stmt.close();
        stmt = null;

        conn.close();
        conn = null;

    } catch (SQLException sqe)
    {
      sqe.printStackTrace();
    } catch (XAException xae)
    {
      if (xae instanceof OracleXAException) {
        System.out.println("XA Error is " +
                      ((OracleXAException)xae).getXAError());
        System.out.println("SQL Error is " +
                      ((OracleXAException)xae).getOracleError());
      }
    }
  }

  static Xid createXid(int bids)
    throws XAException
  {
    byte[] gid = new byte[1]; gid[0]= (byte) 9;
    byte[] bid = new byte[1]; bid[0]= (byte) bids;
    byte[] gtrid = new byte[64];
    byte[] bqual = new byte[64];
    System.arraycopy (gid, 0, gtrid, 0, 1);
    System.arraycopy (bid, 0, bqual, 0, 1);
    Xid xid = new OracleXid(0x1234, gtrid, bqual);
    return xid;
  }

  private static void DoSomeWork1 (Connection conn)
   throws SQLException
  {
    // Create a Statement
    Statement stmt = conn.createStatement ();

    int cnt = stmt.executeUpdate ("insert into jobs values ('SC_STUFF', 'Security Stuff', null, null)");

    System.out.println("No of rows Affected " + cnt);

    stmt.close();
    stmt = null;
  }

  private static void DoSomeWork2 (Connection conn)
    throws SQLException
  {
    // Create a Statement
    Statement stmt = conn.createStatement ();

    int cnt = stmt.executeUpdate ("insert into regions values (101, 'Africa')");

    System.out.println("No of rows Affected " + cnt);

    stmt.close();
    stmt = null;
  }


}
