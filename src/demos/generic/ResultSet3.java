/**
 * A simple sample to to demonstrate ResultSet.insertRow() and 
 * ResultSet.deleteRow().
 * Please compare to ResultSet1.java ~ ResultSet6.java
 *
 * Please use jdk1.2 or later version
 */

import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

public class ResultSet3
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
                     ("select EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL," +
                      " PHONE_NUMBER, HIRE_DATE, JOB_ID, SALARY, " +
                      " COMMISSION_PCT, MANAGER_ID, DEPARTMENT_ID from EMPLOYEES");
    // Add three new employees using ResultSet.insertRow()
    addEmployee (rset, 1001, "PETER", "Medley", "PMEDLEY", null,
                 (new Date(96,3,8)), "SA_REP", 10000, 0, 145, 80);
    addEmployee (rset, 1002, "MARY", "Lang", "MLANG", null,
                 (new Date(96,3,8)), "SA_REP", 10000, 0, 145, 80);
    addEmployee (rset, 1003, "DAVID", "Fisher", "DFISHER", null,
                 (new Date(96,3,8)), "SA_REP", 10000, 0, 145, 80);

    // Close the result set
    rset.close ();

    // Verify the insertion
    System.out.println
               ("\nList EMPLOYEE_ID and FIRST_NAME, LAST_NAME in the EMPLOYEES table: ");

    rset = stmt.executeQuery
               ("select EMPLOYEE_ID, FIRST_NAME, LAST_NAME from EMPLOYEES");
    while (rset.next())
    {
      // We expect to see the three new employees
      System.out.println (rset.getInt(1)+" "+rset.getString(2) +
                          " " + rset.getString(3));
    }
    System.out.println ();

    // Delete the new employee 'PETER' using ResultSet.deleteRow()
    removeEmployee (rset, 1001);
    rset.close ();

    // Verify the deletion
    System.out.println
           ("\nList EMPLOYEE_ID and FIRST_NAME, LAST_NAME in the EMPLOYEES table: ");
    rset = stmt.executeQuery
           ("select EMPLOYEE_ID, FIRST_NAME, LAST_NAME from EMPLOYEES");
    while (rset.next())
    {
      // We expect "PETER" is removed
      System.out.println (rset.getInt(1)+" "+rset.getString(2) +
                          " " + rset.getString(3));
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
   * Add a new employee to EMPLOYEES table.
   */
  public static void addEmployee (ResultSet rset, 
                                  int employeeId,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  String phone,
                                  Date date,
                                  String jobid,
                                  int    sal,
                                  int    commi,
                                  int    mgrid,
                                  int    deptid) 
    throws SQLException
  {
    System.out.println ("Adding new employee: "+employeeId+" "+
                        firstName + " " + lastName);

    // Place the cursor on the insert row
    rset.moveToInsertRow();

    // Assign the new values
    rset.updateInt (1, employeeId);
    rset.updateString (2, firstName);
    rset.updateString (3, lastName);
    rset.updateString (4, email);
    rset.updateString (5, phone);
    rset.updateDate (6, date);
    rset.updateString (7, jobid);
    rset.updateInt (8, sal);
    rset.updateInt (9, commi);
    rset.updateInt (10, mgrid);
    rset.updateInt (11, deptid);

    // Insert the new row to database
    rset.insertRow();
  }

  /**
   * Remove the employee from EMPLOYEES table.
   */
  public static void removeEmployee (ResultSet rset, 
                                     int employeeId)
    throws SQLException
  {
    System.out.println ("Removing the employee: id="+employeeId);

    // Place the cursor right before the first row if it doesn't
    if (!rset.isBeforeFirst())
    {
      rset.beforeFirst();
    }

    // Iterate the result set
    while (rset.next())
    {
      // Place the cursor the row with matched employee id
      if (rset.getInt(1) == employeeId)
      {
        // Delete the current row
        rset.deleteRow();
        break;
      }
    }
  }

  /**
   * Generic cleanup.
   */
  public static void cleanup (Connection conn) 
    throws SQLException
  {
    Statement stmt = conn.createStatement ();
    stmt.execute ("DELETE FROM HR.EMPLOYEES " +
                  "WHERE EMPLOYEE_ID=1001 OR EMPLOYEE_ID=1002 OR EMPLOYEE_ID=1003");
    //stmt.execute ("DELETE FROM EMPLOYEES WHERE EMPLOYEE_ID=1001");
    stmt.execute ("COMMIT");
    stmt.close ();
  }
}
