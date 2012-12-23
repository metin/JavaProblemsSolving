/**
 * A simple sample to demonstrate ResultSet.udpateRow().
 * ResultSet.udpateRow() flushes the changes in ResultSet
 * into database.
 *
 * Please compare to ResultSet1.java ~ ResultSet6.java
 * Please use jdk1.2 or later version
 */

import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

public class ResultSet4
{
  public static void main(String[] args) throws SQLException
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
    Statement stmt = conn.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE, 
                                           ResultSet.CONCUR_UPDATABLE);

    // Query the EMPLOYEES table
    ResultSet rset = stmt.executeQuery
                     ("select EMPLOYEE_ID, FIRST_NAME, LAST_NAME, SALARY" +
                      " from EMPLOYEES");

    // Give everybody a $500 raise
    adjustSalary (rset, 500);

    // Verify the sarlary changes
    System.out.println ("Verify the changes with a new query: ");
    rset = stmt.executeQuery
                ("select EMPLOYEE_ID, FIRST_NAME, LAST_NAME, SALARY " +
                 "from EMPLOYEES"); 
    while (rset.next())
    {
      System.out.println (rset.getInt(1)+" "+rset.getString(2)+" "+
                          rset.getString(3) + " " + rset.getInt(4));
    }
    System.out.println ();

    // Close the RseultSet
    rset.close();

    // Close the Statement
    stmt.close();

    // Cleanup
    cleanup(conn);

    // Close the connection
    conn.close();   
  }

  /**
   * Update the ResultSet content using updateRow().
   */
  public static void adjustSalary (ResultSet rset, int raise) 
    throws SQLException
  {
    System.out.println ("Give everybody in the EMPLOYEES " +
                        "table a $500 raise\n");

    int salary = 0;

    while (rset.next ()) 
    {
      // save the old value
      salary = rset.getInt (4);
      // update the row 
      rset.updateInt (4, salary + raise);
      // flush the changes to database
      rset.updateRow ();
      // show the changes
      System.out.println (rset.getInt(1)+" "+rset.getString(2)+" "+
                          rset.getString(3) + " " +
                          salary+" -> "+rset.getInt(4));
    }
    System.out.println ();
  }

  /**
   * Generic cleanup.
   */
  public static void cleanup (Connection conn) throws SQLException
  {
    Statement stmt = conn.createStatement ();
    stmt.execute ("UPDATE EMPLOYEES SET SALARY = SALARY - 500");
    stmt.execute ("COMMIT");
    stmt.close ();
  }
}
