/**
 * A sample with JNDI using OCI Connection Pool as Datasource.
 * This is tested using File System based reference 
 * implementation of JNDI SPI driver from JavaSoft.
 * You need to download fscontext1_2beta2.zip from
 * JavaSoft site.
 * Include providerutil.jar & fscontext.jar extracted
 * from the above ZIP in the classpath. 
 * Create a directory ./JNDI/jdbc
 *
 * Please use jdk1.2 or later version
 * usage: rm ./JNDI/jdbc/.bindings first, then
 *        java DataSourceJNDI <tmp_dir>
 */

import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import javax.naming.spi.*;
import java.util.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;
import oracle.jdbc.oci.*;

public class OCIConnPoolJNDI
{
  public static void main(String args[]) 
    throws SQLException, NamingException
  {
    if ( args.length != 1 )
    {
       System.out.println("usage: java DataSourceJNDI <tmp_dir>");
       System.exit(0);
    }

    // Initialize the Context
    Context ctx = null;
    try {
      Hashtable env = new Hashtable (5);
      env.put (Context.INITIAL_CONTEXT_FACTORY,
             "com.sun.jndi.fscontext.RefFSContextFactory");
      env.put (Context.PROVIDER_URL, "file:" + args[0]);
      ctx = new InitialContext(env);
    }
    catch (NamingException ne) {
      ne.printStackTrace();
    }

    do_bind(ctx, "jdbc/connpool");
    do_lookup(ctx, "jdbc/connpool");

  }

  static void do_bind (Context ctx, String ln)
    throws SQLException, NamingException
  {
    // Set the properties for the OCI Connection Pool
    Properties poolconfig = new Properties( ) ;
    poolconfig.put(OracleOCIConnectionPool.CONNPOOL_MIN_LIMIT, "1") ;
    poolconfig.put(OracleOCIConnectionPool.CONNPOOL_MAX_LIMIT, "10") ;
    poolconfig.put(OracleOCIConnectionPool.CONNPOOL_INCREMENT, "2") ;

    String url = "jdbc:oracle:oci8:@";
    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    // Create a OracleOCIConnectionPool instance
    OracleOCIConnectionPool cpool = new OracleOCIConnectionPool
        ("hr", "hr", url, poolconfig);

    // Bind it 
    System.out.println ("Doing a bind with the logical name : " + ln);
    ctx.bind (ln, cpool);

    // Close the OracleOCIConnectionPool
    cpool.close();
    cpool = null;
  }

  static void do_lookup (Context ctx, String ln)
    throws SQLException, NamingException
  {
    System.out.println ("Doing a lookup with the logical name : " + ln);

    OracleOCIConnectionPool cpool = (OracleOCIConnectionPool) ctx.lookup (ln);

    // Print the current settings of the OCI Connection Pool
    printSettings(cpool);

    // Retrieve some connections from the Pool and test them
    testIt(cpool);

    // Close the connection
    cpool.close();
    cpool = null;
  }

  static void printSettings (OracleOCIConnectionPool cpool) 
        throws SQLException
  {
    System.out.println ("User = " + cpool.getUser());
    System.out.println ("URL = " + cpool.getURL());
    System.out.println ("Min poolsize Limit = " + cpool.getMinLimit());
    System.out.println ("Max poolsize Limit = " + cpool.getMaxLimit());
    System.out.println ("Connection Increment = " + cpool.getConnectionIncrement());
  }

  static void testIt (OracleOCIConnectionPool cpool) 
        throws SQLException
  {
    //get two connections from the pool
    OracleOCIConnection conn1 = (OracleOCIConnection) cpool.getConnection();
    OracleOCIConnection conn2 = (OracleOCIConnection) cpool.getConnection();

    // Create a Statement
    Statement stmt = conn1.createStatement ();

    // Select the LAST_NAME column from the EMPLOYEES table
    ResultSet rset = stmt.executeQuery ("select LAST_NAME from EMPLOYEES");

    // Iterate through the result and print the employee names
    System.out.println ("-- Use connection 1 from the OCI Connection Pool --");
    while (rset.next ())
      System.out.println (rset.getString (1));

    System.out.println ("-- Use connection 2 from the OCI Connection Pool --");

    // Create a Statement from conn2
    stmt = conn2.createStatement ();

    // Select the USER from DUAL to test the connection
    rset = stmt.executeQuery ("select USER from DUAL");

    // Print out the USER name
    rset.next ();
    System.out.println (rset.getString (1));

    conn1.close();
    conn2.close();
  }

}

