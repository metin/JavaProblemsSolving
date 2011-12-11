/*
 * This sample demonstrates the jdbc driver by printing the system date from dual
 * after connectng to the course database on prophet.njit.edu.
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;

class jdbcTest
{
  public static void main (String args [])
       throws SQLException
  {
    // Load the Oracle JDBC driver
    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

    String url = "jdbc:oracle:thin:@prophet.njit.edu:1521:course";

    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    // Connect to the database
    Connection conn =
      DriverManager.getConnection (url, "my67", "u3CwmgIn");

    // Create a Statement
    Statement stmt = conn.createStatement ();

    // Select the SYSDATE column from the dual table
    ResultSet rset = stmt.executeQuery ("select SYSDATE from dual");

    // Print the result
    while (rset.next ())
    System.out.println (rset.getString (1));

    // Close the RseultSet
    rset.close();

    // Close the Statement
    stmt.close();

    // Close the connection
    conn.close();
  }
}