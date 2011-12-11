/**
 * A simple sampe to demonstrate scroll sensitive result set.
 * ResultSet type is determined by the statement that created 
 * the result set. The types include: TYPE_FORWARD_ONLY,
 * TYPE_SCROLL_INSENSITIVE, TYPE_SCROLL_SENSITIVE
 *
 * Please compare to ResultSet1.java ~ ResultSet6.java
 * Please use jdk1.2 or later version
 */

import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

public class ResultSet5
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
    Statement stmt = conn.createStatement
                          (ResultSet.TYPE_SCROLL_SENSITIVE, 
                           ResultSet.CONCUR_UPDATABLE);

    // Set the statement fetch size to 1
    stmt.setFetchSize (1);

    // Query the EMPLOYEES table
    ResultSet rset = stmt.executeQuery
                          ("select EMPLOYEE_ID, FIRST_NAME, LAST_NAME, SALARY " +
                           "from EMPLOYEES");
 
    // List the result set's type, concurrency type, ..., etc
    showProperty (rset);
 
    // List the query result 
    System.out.println
               ("List EMPLOYEE_ID, FIRST_NAME, LAST_NAME and SALARY " +
                "from the EMPLOYEES table: ");
    while (rset.next())
    {
      System.out.println (rset.getInt(1)+" "+rset.getString(2)+" "+
                          rset.getString(3) + " " + rset.getInt(4));
    }
    System.out.println ();

    // Do some changes outside the result set
    doSomeChanges (conn);
    
    // Place the cursor right before the first row
    rset.beforeFirst ();

    // List the employee information again
    System.out.println
               ("List EMPLOYEE_ID, FIRST_NAME, LAST_NAME and SALARY again: ");
    while (rset.next())
    {
      // We expect to see the changes made in "doSomeChanges()"
      System.out.println (rset.getInt(1)+" "+rset.getString(2)+" "+
                          rset.getString(3) + " " + rset.getString(4));
    }

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
   * Update the EMP table.
   */ 
  public static void doSomeChanges (Connection conn)
    throws SQLException
  {
    System.out.println
              ("Update the employee salary outside the result set\n");
   
    Statement otherStmt = conn.createStatement ();
    otherStmt.execute ("update employees set salary = salary + 500");
    otherStmt.execute ("commit");
    otherStmt.close ();
  }

  /**
   * Show the result set properties like type, concurrency type, fetch 
   * size,..., etc.
   */
  public static void showProperty (ResultSet rset) throws SQLException
  {
    // Verify the result set type
    switch (rset.getType())
    {
      case ResultSet.TYPE_FORWARD_ONLY:
        System.out.println ("Result set type: TYPE_FORWARD_ONLY");
        break;
      case ResultSet.TYPE_SCROLL_INSENSITIVE:
        System.out.println ("Result set type: TYPE_SCROLL_INSENSITIVE");
        break;
      case ResultSet.TYPE_SCROLL_SENSITIVE:
        System.out.println ("Result set type: TYPE_SCROLL_SENSITIVE");
        break;
      default: 
        System.out.println ("Invalid type");
        break;
    }

    // Verify the result set concurrency
    switch (rset.getConcurrency())
    {
      case ResultSet.CONCUR_UPDATABLE:
        System.out.println
                   ("Result set concurrency: ResultSet.CONCUR_UPDATABLE");
        break;
      case ResultSet.CONCUR_READ_ONLY:
        System.out.println
                   ("Result set concurrency: ResultSet.CONCUR_READ_ONLY");
        break;
      default: 
        System.out.println ("Invalid type");
        break;
    }

    // Verify the fetch size
    System.out.println ("fetch size: "+rset.getFetchSize ());

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
