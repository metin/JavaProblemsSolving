/*
 * A sample to check how many physical connections
 * a pool connection will open. 1 physical connection
 * can corresponds to multiple logical connections.
 * Only one logical connection get control of the physical
 * connection and is valid. As a result, the previous 
 * logical connection should thrown an exception if used,
 * and the latest logical connection is valid.
 *
 * Please compare to PooledConnection1.java
 * Please use jdk1.2 or later version
 */

import java.sql.*;
import javax.sql.*;
import oracle.jdbc.*;
import java.util.Properties;
import oracle.jdbc.pool.*;

class PooledConnection2
{
  static String url = "jdbc:oracle:oci8:@";

  public static void main (String args [])
       throws SQLException, java.io.IOException
  {

    OracleConnectionPoolDataSource ocpds = 
                               new OracleConnectionPoolDataSource();

    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    ocpds.setURL(url);
    ocpds.setUser("hr");
    ocpds.setPassword("hr");
 
    // Open sysconn and sysstmt
    open_sys_conn();

    System.out.println("No of Sessions before opening a PooledConnection is " +
                        count_sessions());

    PooledConnection pc  = ocpds.getPooledConnection(); // physical connection

    System.out.println("No of Sessions before opening first LogicalConnection is " +
                        count_sessions());

    Connection conn1 = pc.getConnection(); // logical connection

    System.out.println("No of Sessions before opening Second LogicalConnection is " +
                        count_sessions());

    Connection conn2 = pc.getConnection(); // logical connection

    System.out.println("No of Sessions after opening Second LogicalConnection is " +
                        count_sessions());

    // Create a Statement
    Statement stmt = conn2.createStatement ();

    // Select the NAME columns from the EMPLOYEES table
    ResultSet rset = stmt.executeQuery
                         ("select FIRST_NAME, LAST_NAME from EMPLOYEES");

    // Iterate through the result and print the employee names
    while (rset.next ())
      System.out.println (rset.getString (1) + "  " + rset.getString (2));

    // Close the RseultSet
    rset.close();

    // Close the Statement
    stmt.close();

    try {
  
      // This should throw an exception as conn1 is invalid
      // as control is stolen by conn2

      stmt = conn1.createStatement ();
    } catch (SQLException se)
    {
      System.out.println("Exception expected : " + se.getMessage());
    }

    // Close the connection
    conn1.close();   

    // Close the connection
    conn2.close();   

    // CLose the Pooled Connection
    pc.close();

    // Close sysconn and sysstmt
    close_sys_conn();
  }


  static Connection sysconn = null;
  static Statement sysstmt = null;

  private static void open_sys_conn ()
   throws SQLException
  {
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("system");
    ods.setPassword("manager");
    ods.setURL(url);
    sysconn = ods.getConnection();

    sysstmt = sysconn.createStatement();
    ods.close();
    ods = null;
  }

  private static int count_sessions ()
   throws SQLException
  {
    ResultSet sysrs = sysstmt.executeQuery
                              ("select count(*) from V$SESSION");
    sysrs.next();
    int cnt = sysrs.getInt(1);

    sysrs.close();
    sysrs = null;

    return cnt;
  }

  private static void close_sys_conn ()
   throws SQLException
  {

    sysstmt.close();
    sysstmt = null;
   
    sysconn.close();
    sysconn = null;
  }
}
