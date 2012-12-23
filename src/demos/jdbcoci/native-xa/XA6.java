/*
    A simple single-phase XA demo that uses the hetero-RM XA feature.
    The backend could be either post-8.1.6 versions, or pre-8.1.6
    versions (like 8.0.5, 8.1.5, etc.).
    This demo requires one command-line parameter that is the connect
    string to the database.  This connect string should already be set
    up in your tnsnames.ora prior to executing this program.

    Please use jdk1.2 or later version
    usage: java XA6 <service_name>
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;
import javax.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;
import oracle.jdbc.xa.OracleXid;
import oracle.jdbc.xa.client.*;
import javax.transaction.xa.*;

class XA6
{
  public static void main (String args [])
       throws SQLException
  {
    if ( args.length != 1 )
    {
         System.out.println("usage: java XA6 <service_name>");
         System.exit (0);
    }

    String service_name = args[0];

    try
    {
        DriverManager.registerDriver(new OracleDriver());

        String url = "jdbc:oracle:oci8:@" + service_name;
        try {
          String url1 = System.getProperty("JDBC_URL");
          if (url1 != null)
            url = url1;
        } catch (Exception e) {
          // If there is any security exception, ignore it
          // and use the default
        }

        // Open a connection to the database for checking results
        Connection conn =
          DriverManager.getConnection (url, "hr", "hr");

        Statement stmt = conn.createStatement ();

        try
        {
          stmt.execute ("delete from jobs where job_id = 'SC_STUFF'");
        }
        catch (SQLException e)
        {
          // Ignore an error here
        }

        // Create a XADataSource instance
        OracleXADataSource oxds = new OracleXADataSource();
        oxds.setURL(url);

        // Must set the nativeXA flag to true to use the hetero-RM XA feature
        oxds.setNativeXA(true);

        // Must set the tnsEntryName (i.e., connect string) to use hetero-RM
        oxds.setTNSEntryName(service_name);

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
        stmt1.executeUpdate ("insert into jobs(job_id, job_title) values ('SC_STUFF', 'Security Stuff')");
    
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

        // check the result
        ResultSet rset = stmt.executeQuery ("select job_id, job_title from jobs");
        System.out.println("\ncontents of table jobs:\n");
        while (rset.next())
          System.out.println(rset.getString(1) + " " + rset.getString(2));
  
        rset.close();
        rset = null;

        stmt.close();
        stmt = null;

        conn.close();
        conn = null;

    } catch (SQLException sqe)
    {
      sqe.printStackTrace();
    } catch (XAException xae)   // Use XAException here, not OracleXAException
    {
      xae.printStackTrace();
    }
  }

  static Xid createXid()
    throws XAException
  {
    byte[] gid = new byte[1]; gid[0]= (byte) 6;
    byte[] bid = new byte[1]; bid[0]= (byte) 1;
    byte[] gtrid = new byte[64];
    byte[] bqual = new byte[64];
    System.arraycopy (gid, 0, gtrid, 0, 1);
    System.arraycopy (bid, 0, bqual, 0, 1);
    Xid xid = new OracleXid(0x1234, gtrid, bqual);
    return xid;
  }
}

