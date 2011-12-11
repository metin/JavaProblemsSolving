/**
 * A simple sample to demonstrate previous(), absolute() and relative().
 * Please compare to ResultSet1.java ~ ResultSet6.java
 *
 * Please use jdk1.2 or later version
 */

import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

public class ResultSet2
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
    ResultSet rset = stmt.executeQuery ("select FIRST_NAME, LAST_NAME from EMPLOYEES");

    // iterate through the result using next()
    show_resultset_by_next(rset);

    // iterate through the result using previous()
    show_resultset_by_previous(rset);

    // iterate through the result using absolute()
    show_resultset_by_absolute(rset);

    // iterate through the result using relative()
    show_resultset_by_relative(rset);

    // Close the RseultSet
    rset.close();

    // Close the Statement
    stmt.close();

    // Close the connection
    conn.close();   
  }

  /**
   * Iterate through the result using next().
   *
   * @param rset a result set object
   */ 
  public static void show_resultset_by_next(ResultSet rset) 
    throws SQLException
  {
    System.out.println ("List the employee names using ResultSet.next():");

    // Make sure the cursor is placed right before the first row
    if (!rset.isBeforeFirst())
    {
      // Place the cursor right before the first row
      rset.beforeFirst ();
    }
 
    // Iterate through the rows using next()
    while (rset.next())
      System.out.println (rset.getString (1) + "  " + rset.getString (2));

    System.out.println ();
  }

  /**
   * Iterate through the result using previous().
   *
   * @param rset a result set object
   */ 
  public static void show_resultset_by_previous(ResultSet rset) 
    throws SQLException
  {
    System.out.println ("List the employee names using ResultSet.previous():");

    // Make sure the cursor is placed after the last row
    if (!rset.isAfterLast())
    {
      // Place the cursor after the last row
      rset.afterLast ();
    }
 
    // Iterate through the rows using previous()
    while (rset.previous())
      System.out.println (rset.getString (1) + "  " + rset.getString (2));

    System.out.println ();
  }

  /**
   * Iterate through the result using absolute().
   *
   * @param rset a result set object
   */ 
  public static void show_resultset_by_absolute (ResultSet rset) 
    throws SQLException
  {
    System.out.println ("List the employee names using ResultSet.absolute():");

    // The begin index for ResultSet.absolute (idx)
    int idx = 1;

    // Loop through the result set until absolute() returns false.
    while (rset.absolute(idx))
    {
      System.out.println (rset.getString (1) + "  " + rset.getString (2));
      idx ++;
    }
    System.out.println (); 
  }
     
  /**
   * Iterate through the result using relative().
   *
   * @param rset a result set object
   */ 
  public static void show_resultset_by_relative (ResultSet rset) 
    throws SQLException
  {
    System.out.println ("List the employee names using ResultSet.relative():");

    // getRow() returns 0 if there is no current row
    if (rset.getRow () == 0 || !rset.isLast())
    {
      // place the cursor on the last row
      rset.last ();
    }

    // Calling relative(-1) is similar to previous(), but the cursor 
    // has to be on a valid row before calling relative().
    do
    {
      System.out.println (rset.getString (1) + "  " + rset.getString (2));
    }
    while (rset.relative (-1));

    System.out.println (); 
  }
}
