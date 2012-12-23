/*
 * This sample shows how to insert data into a table using
 * PreparedStatement. It also shows that the same
 * PreparedStatement can be re-used. 
 *
 * Require: a pre-existing hr.employees table and a 
 *          schema hr with password hr.
 *
 * note: jdk1.2 or later version is recommended. 
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

class InsertExample
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
    }

    // Create an OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Connect to the database
    Connection conn = ods.getConnection();

    // Prepare a statement to cleanup the employees table
    Statement stmt = conn.createStatement ();
    try
    {
      stmt.execute ("delete from EMPLOYEES where EMPLOYEE_ID = 150");
    }
    catch (SQLException e)
    {
      // Ignore an error here
    }

    try
    {
      stmt.execute ("delete from EMPLOYEES where EMPLOYEE_ID = 180");
    }
    catch (SQLException e)
    {
      // Ignore an error here too
    }

    // Close the statement
    stmt.close();

    // Prepare to insert new names in the EMPLOYEES table
    PreparedStatement pstmt = 
      conn.prepareStatement ("insert into EMPLOYEES "
                            + "(EMPLOYEE_ID, FIRST_NAME, LAST_NAME,"
                            + "EMAIL, PHONE_NUMBER, HIRE_DATE, JOB_ID, SALARY, "
                            + "COMMISSION_PCT, MANAGER_ID, DEPARTMENT_ID) "
                            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

    // Add Peter Tucker as employee number 150
    pstmt.setInt (1, 150);          // The first ? is for EMPLOYEE_ID
    pstmt.setString (2, "Peter");   // The second ? is for FIRST_NAME
    pstmt.setString (3, "Tucker");  // The third ? is for LAST_NAME
    pstmt.setString (4, "PTUCKER"); // The 4th ? is for EMAIL
    pstmt.setString (5, "011.44.1344.129268");
                                    // The 5th ? is for PHONE_NUMBER
    pstmt.setDate (6, new Date(97,1,30));
                                    // The 6th ? is for HIRE_DATE
    pstmt.setString (7, "SA_REP");  // The 7th ? is for JOB_ID
    pstmt.setInt (8, 10000);        // The 8th ? is for SALARY
    pstmt.setDouble (9, .3);        // The 9th ? is for COMMISSION_PCT
    pstmt.setInt (10, 145);         // The 10th ? is for MANAGER_ID
    pstmt.setInt (11, 80);          // The 11th ? is for DEPARTMENT_ID

    // Do the insertion, check number of rows updated
    pstmt.execute ();
    System.out.println(pstmt.getUpdateCount() + " rows updated");

    // Add Winston Taylor as employee number 180

    pstmt.setInt (1, 180);          // The first ? is for EMPLOYEE_ID
    pstmt.setString (2, "Winston"); // The second ? is for FIRST_NAME
    pstmt.setString (3, "Taylor");  // The third ? is for LAST_NAME
    pstmt.setString (4, "WTAYLOR"); // The 4th ? is for EMAIL
    pstmt.setString (5, "650.507.9876");
                                    // The 5th ? is for PHONE_NUMBER
    pstmt.setDate (6, new Date(98,1,24));
                                    // The 6th ? is for HIRE_DATE
    pstmt.setString (7, "SH_CLERK");// The 7th ? is for JOB_ID
    pstmt.setInt (8, 3200);         // The 8th ? is for SALARY
    pstmt.setString (9, null);      // The 9th ? is for COMMISSION_PCT
    pstmt.setInt (10, 120);         // The 10th ? is for MANAGER_ID
    pstmt.setInt (11, 50);          // The 11th ? is for DEPARTMENT_ID

    // Do the insertion, check number of rows updated
    pstmt.execute ();
    pstmt.getGeneratedKeys();
    System.out.println(pstmt.getUpdateCount() + " rows updated");

    // Close the statement
    pstmt.close();

    // Close the connecion
    conn.close();

  }
}
