/*
 * A simple Pooled Connection Sample
 * Please compare to PooledConnection2.java
 * Please use jdk1.2 or later version
 */

import java.sql.*;
import javax.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;

class PooledConnection1
{
  public static void main (String args [])
       throws SQLException
  {

    // Create a OracleConnectionPoolDataSource instance
    OracleConnectionPoolDataSource ocpds =
                               new OracleConnectionPoolDataSource();

    String url = "jdbc:oracle:oci8:@";
    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    // Set connection parameters
    ocpds.setURL(url);
    ocpds.setUser("hr");
    ocpds.setPassword("hr");

    // Create a pooled connection
    PooledConnection pc  = ocpds.getPooledConnection();

    // Get a Logical connection
    Connection conn = pc.getConnection();

    // Create a Statement
    Statement stmt = conn.createStatement ();

    // Select the NAME columns from the EMPLOYEES table
    ResultSet rset = stmt.executeQuery
                         ("select FIRST_NAME, LAST_NAME from EMPLOYEES");

    // Iterate through the result and print the employee names
    while (rset.next ())
      System.out.println (rset.getString (1) + "  " + rset.getString (2));

    // Close the RseultSet
    rset.close();
    rset = null;

    // Close the Statement
    stmt.close();
    stmt = null;

    // Close the logical connection
    conn.close();
    conn = null;

    // Close the pooled connection
    pc.close();
    pc = null;
  }
}

