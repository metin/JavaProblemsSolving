/*
    A simple single phase XA demo.
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

class XA1
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
          stmt.execute ("delete from regions where region_id = 101");
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
    
        Xid xid = createXid();
    
        // Start a transaction branch
        oxar.start (xid, XAResource.TMNOFLAGS);
    
        // Create a Statement
        Statement stmt1 = conn1.createStatement ();
    
        // Do some DML
        stmt1.executeUpdate ("insert into REGIONS values (101, 'Africa')");
    
        // Close the Statement
        stmt1.close();
        stmt1 = null;

        // End the branch
        oxar.end(xid, XAResource.TMSUCCESS);
    
        // Do a 1 phase commit
        oxar.commit (xid, true);

        // Close the connection
        conn1.close();   
        conn1 = null;

        // close the XA connection
        pc.close();
        pc = null;

        ResultSet rset = stmt.executeQuery ("select REGION_NAME from regions");
        while (rset.next())
          System.out.println("REGION_NAME is " + rset.getString(1));
  
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
      xae.printStackTrace();
    }
  }

  static Xid createXid()
    throws XAException
  {
    byte[] gid = new byte[1]; gid[0]= (byte) 1;
    byte[] bid = new byte[1]; bid[0]= (byte) 1;
    byte[] gtrid = new byte[64];
    byte[] bqual = new byte[64];
    System.arraycopy (gid, 0, gtrid, 0, 1);
    System.arraycopy (bid, 0, bqual, 0, 1);
    Xid xid = new OracleXid(0x1234, gtrid, bqual);
    return xid;
  }
}
