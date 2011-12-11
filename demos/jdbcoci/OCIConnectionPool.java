/*
 * A simple OCI Connection Pool Sample
 *
 * It 1. creates an OracleOCIConnectionPool instance
 *       with the default configuration (CONNPOOL_MIN_LIMIT,
 *       CONNPOOL_MAX_LIMIT, CONNPOOL_INCREMENT)
 *    2. generates some logical connections conn1 and
 *       conn2 from above instance, and do some operations
 *       to the database.
 *    3. reconfigures the OracleOCIConnectionPool to let
 *       many users to get connected at the same time
 *
 * Please use jdk1.2 or later version
 */

import java.sql.*;
import javax.sql.*;
import java.util.Properties;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;
import oracle.jdbc.oci.*;

class OCIConnectionPool
{
  public static void main (String args [])
       throws SQLException
  {

    String url = "jdbc:oracle:oci:@";
    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    // Create an OracleOCIConnectionPool instance with
    // default configuration
    OracleOCIConnectionPool cpool = new OracleOCIConnectionPool
                                         ("hr", "hr", url, null);

    // Print out the default configuration for the
    // OracleOCIConnectionPool
    System.out.println
           ("-- The default configuration for the OracleOCIConnectionPool --");
    displayPoolConfig(cpool);

    // Get a connection from the pool
    OracleOCIConnection conn1 = (OracleOCIConnection)
                                  cpool.getConnection("hr", "hr");

    // Create a Statement
    Statement stmt = conn1.createStatement ();

    // Select the NAMEs column from the EMPLOYEES table
    ResultSet rset = stmt.executeQuery ("select FIRST_NAME, LAST_NAME " + 
                                        "from EMPLOYEES");

    // Iterate through the result and print the employee names
    System.out.println
           ("-- Use the connection from the OracleOCIConnectionPool --");
    while (rset.next ())
      System.out.println (rset.getString (1) + " " + rset.getString (2));

    System.out.println
           ("-- Use another connection from the OracleOCIConnectionPool --");

    // Get another connection from the pool
    // with different userID and password
    OracleOCIConnection conn2 = (OracleOCIConnection)
                                cpool.getConnection("system", "manager");

    // Create a Statement
    stmt = conn2.createStatement ();

    // Select the USER from DUAL to test the connection
    rset = stmt.executeQuery ("select USER from DUAL");

    // Iterate through the result and print it out 
    rset.next ();
    System.out.println (rset.getString (1));

    // Reconfigure the OracleOCIConnectionPool in case the performance
    // is too bad. This might happen when many users are trying to connect
    // at the same time. In this case, increase MAX_LIMIT to some larger
    // number, and also increase INCREMENT to a positive number.

    Properties p  = new Properties();
    p.put (OracleOCIConnectionPool.CONNPOOL_MIN_LIMIT,
           Integer.toString(cpool.getMinLimit()));
    p.put (OracleOCIConnectionPool.CONNPOOL_MAX_LIMIT,
           Integer.toString(cpool.getMaxLimit() * 2)) ;
    if (cpool.getConnectionIncrement() > 0)
        // Keep the old value
        p.put (OracleOCIConnectionPool.CONNPOOL_INCREMENT,
               Integer.toString(cpool.getConnectionIncrement()));
    else
        // Set it to a number larger than 0
        p.put (OracleOCIConnectionPool.CONNPOOL_INCREMENT, "1") ;
    
    // Enable the new configuration
    cpool.setPoolConfig(p);

    // Print out the current configuration for the
    // OracleOCIConnectionPool
    System.out.println
           ("-- The new configuration for the OracleOCIConnectionPool --");
    displayPoolConfig(cpool);

    // Close the RseultSet
    rset.close();
    rset = null;

    // Close the Statement
    stmt.close();
    stmt = null;

    // Close the connections
    conn1.close();
    conn2.close();
    conn1 = null;
    conn2 = null;

    // Close the OracleOCIConnectionPool
    cpool.close();
    cpool = null;
  }

  // Display the current status of the OracleOCIConnectionPool
  private static void displayPoolConfig (OracleOCIConnectionPool cpool)
      throws SQLException
  {
    System.out.println (" Min poolsize Limit: " + cpool.getMinLimit());
    System.out.println (" Max poolsize Limit: " + cpool.getMaxLimit());
    System.out.println (" Connection Increment: " +
                          cpool.getConnectionIncrement());
    System.out.println (" NoWait: " + cpool.getNoWait());
    System.out.println (" Timeout: " + cpool.getTimeout());
    System.out.println (" PoolSize: " + cpool.getPoolSize());
    System.out.println (" ActiveSize: " + cpool.getActiveSize());
  }
}

