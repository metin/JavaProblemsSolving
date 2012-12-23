/*
 * This sample shows how to retrieve and list all the names
 * (FIRST_NAME, LAST_NAME) from the EMPLOYEES table
 *
 * Please use jdk1.2 or later version 
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

class SelectExample
{
  public static void main (String args [])
       throws SQLException
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

    // Create a Statement
    Statement stmt = conn.createStatement ();

    // Select first_name and last_name column from the employees table
    ResultSet rset = stmt.executeQuery ("select FIRST_NAME, "
                                        + "LAST_NAME from EMPLOYEES");

    // Iterate through the result and print the employee names
    while (rset.next ())
      System.out.println (rset.getString (1) + " " + rset.getString (2));

    // Close the RseultSet
    rset.close();

    // Close the Statement
    stmt.close();

    // Close the connection
    conn.close();   
  }
}
