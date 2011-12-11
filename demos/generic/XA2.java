/*
    A simple XA demo with suspend and resume. Opens 2 global
    transactions each of one branch. Does some DML on the first one
    and suspends it and does some DML on the 2nd one and resumes the
    first one and commits. Basically, to illustrate interleaving
    of global transactions.
    Need a java enabled 8.1.6 database to run this demo.

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

class XA2
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
          stmt.execute ("delete from regions where region_id > 100");
        }
        catch (SQLException e)
        {
          // Ignore an error here
        }


        // Create a XADataSource instance
        OracleXADataSource oxds = new OracleXADataSource();
        oxds.setURL(url);
        oxds.setUser("hr");
        oxds.setPassword("hr");
    
        // get a XA connection
        XAConnection pc  = oxds.getXAConnection();
        // Get a logical connection
        Connection conn1 = pc.getConnection();
       
        // Get XA resource handle
        XAResource oxar = pc.getXAResource();
        Xid xid1 = createXid(111,111);
    
        // Start a transaction branch
        oxar.start (xid1, XAResource.TMNOFLAGS);
    
        // Create a Statement
        Statement stmt1 = conn1.createStatement ();
    
        // Do some DML
        stmt1.executeUpdate ("insert into regions values (101, 'Moon')");
    
        // Suspend the first global transaction
        // ((OracleXAResource)oxar).suspend (xid1); or
        oxar.end (xid1, XAResource.TMSUSPEND);

        Xid xid2 = createXid(222,222);
        oxar.start (xid2, XAResource.TMNOFLAGS);
        Statement stmt2 = conn1.createStatement ();
        stmt2.executeUpdate ("insert into regions values (202, 'Mars')");
        oxar.commit (xid2, true);
        stmt2.close();
        stmt2 = null;

        // Close the Statement
        stmt1.close();
        stmt1 = null;

        // Resume the first global transaction
        // ((OracleXAResource)oxar).resume (xid1); or
        oxar.start (xid1, XAResource.TMRESUME);

        // End the branch
        oxar.end(xid1, XAResource.TMSUCCESS);
    
        // Do a 1 phase commit
        oxar.commit (xid1, true);

        // Close the connection
        conn1.close();   
        conn1 = null;

        // close the XA connection
        pc.close();
        pc = null;

        ResultSet rset = stmt.executeQuery ("select region_id, region_name from regions");
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
      if (xae instanceof OracleXAException)
      {
        System.out.println("XA error is " + ((OracleXAException)xae).getXAError());
        System.out.println("SQL error is " + ((OracleXAException)xae).getOracleError());
      }
      xae.printStackTrace();
    }
  }

  static Xid createXid(int gd, int bd)
    throws XAException
  {
    byte[] gid = new byte[1]; gid[0]= (byte) gd;
    byte[] bid = new byte[1]; bid[0]= (byte) bd;
    byte[] gtrid = new byte[64];
    byte[] bqual = new byte[64];
    System.arraycopy (gid, 0, gtrid, 0, 1);
    System.arraycopy (bid, 0, bqual, 0, 1);
    Xid xid = new OracleXid(0x1234, gtrid, bqual);
    return xid;
  }
}
