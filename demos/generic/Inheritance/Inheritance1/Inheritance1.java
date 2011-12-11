/*
 * This example demonstrates the Oracle 9i inheritance feature. It 
 * demonstrates the access of subtype objects by using Oracle
 * ORAData customized mapping.
 *
 * This example creates the object types hierarchy as follows --
 *
 *   Person_t  -+- Employee_t
 *              |
 *              +- Student_t  -- ParttimeStudent_t
 *  
 * and a object table of Person_t.
 *
 * This example inserts the Person_t, Employee_t, Student_t
 * and ParttimeStudent_t objects into the object table, and then 
 * select the objects and prints out their values.
 *
 * This example requires five dependency Java files (the ORAData
 * customized classes for the object types used in this example): 
 *
 *   - Person.java
 *   - Student.java
 *   - ParttimeStudent.java
 *   - Employee.java
 *   - PersonFactory.java
 *    
 * note: 1. You need to compile all the dependency classes before you
 *          run this example.
 *       2. You need to make sure COMPATIBLE parameter in tkinit.ora
 *          file is 9.0.0.0.0 or greater. If it is a lower version,
 *          shutdown and startup with a higher compatibility setting.
 *       3. Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

public class Inheritance1
{
  public static void main (String args []) throws Exception
  {
    String urlDefault = "jdbc:oracle:oci8:@";
    String url = null;
    try 
    {
      url = System.getProperty("JDBC_URL");
    } 
    catch (Exception e) 
    {
      // If there is any security exception, ignore it
      // and use the default
    }
    finally
    {
      if (url == null) url = urlDefault;
    }

    // Create a OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Connect to the database
    Connection conn = ods.getConnection ();

    // Create the object types and the database table
    createTables (conn);   

    // Insert the Person_t, Student_t, ParttimeStudent_t and Employee_t
    // objects into the database.
    insert (conn);

    // Select the objects previously inserted
    select (conn);

    // Remove the objects previously created in the database
    cleanup (conn);

    // Disconnect
    conn.close ();
  }

  /**
   * Create the database data structure used by this example.
   */ 
  public static void createTables (Connection conn) throws SQLException
  {
    // Remove database data created by this example
    cleanup (conn);

    // Create a Statement
    Statement stmt = conn.createStatement ();

    // Create the database object type "Person_t".
    stmt.execute ("CREATE TYPE Person_t AS OBJECT       "+
                  "(  ssn NUMBER,                       "+
                  "   name VARCHAR2(30),                "+
                  "   address VARCHAR2(100)             "+
                  ") NOT FINAL");

    // Create the database object type "Student_t" that inherits 
    // "Person_t".
    stmt.execute ("CREATE TYPE Student_t UNDER Person_t "+
                  "(  deptid NUMBER,                    "+
                  "   major VARCHAR2(30)                "+
                  ") NOT FINAL");

    // Create the database object type "Employee_t" that inherits
    // "Person_t".
    stmt.execute ("CREATE TYPE Employee_t UNDER Person_t"+
                  "(  empid NUMBER,                     "+
                  "   mgr VARCHAR2(30)                  "+
                  ")");

    // Create the database object type "PartTimeStudent_t" that
    // inherits "Student_t".
    stmt.execute ("CREATE TYPE PartTimeStudent_t UNDER Student_t"+
                  "(  numhours NUMBER                   "+
                  ")");

    // Create a database table
    stmt.execute ("CREATE TABLE PersonTab of Person_t");

    // Close the statement
    stmt.close ();
  }

  /**
   * Remove database data created by this example
   */
  public static void cleanup (Connection conn) throws SQLException
  {
    // Create a Statement
    Statement stmt = conn.createStatement ();

    try
    {
      stmt.execute ("drop table PersonTab");
    }
    catch (SQLException e)
    {
      // An exception could be raised here if the table did not exist already.
    }

    try { stmt.execute ("drop type Employee_t"); } catch (SQLException e) {}
    try { stmt.execute ("drop type ParttimeStudent_t"); } catch (SQLException e) {}
    try { stmt.execute ("drop type Student_t"); } catch (SQLException e) {}
    try { stmt.execute ("drop type Person_t"); } catch (SQLException e) {}
   
    // Close the statement
    stmt.close ();
  }
  
  /**
   * This method inserts Person_t, Student_t, ParttimeStudent_t
   * and Employee_t objects into the PersonTab table. This 
   * demonstrates the column substitutablity feature of the 
   * Oracle 9i database.
   */ 
  public static void insert (Connection conn) throws SQLException
  {

    // Prepare the insert statement
    PreparedStatement pstmt = 
      conn.prepareStatement ("insert into PersonTab values (?)");

    // A place holder for Person_t objects 
    Object person = null;

    // Create and insert a Person_t object into the database
    {
      // Create a Person_t object
      person = new Person (
                 new NUMBER (1001), 
                 new CHAR ("Scott", null), 
                 new CHAR ("SF", null));

      // Bind the Person_t object
      pstmt.setObject (1, person, OracleTypes.STRUCT);

      // Execute the insertion
      if (pstmt.executeUpdate () == 1)
        System.out.println ("Successfully inserted a Person_t object");
      else
        System.out.println ("Insertion failed");
    }

    // Create and insert a Student_t object into the database
    {
      // Create a Student_t object
      person = new Student (
                 new NUMBER (1002), 
                 new CHAR ("Peter", null), 
                 new CHAR ("NY", null),
                 new NUMBER (100), 
                 new CHAR ("EE", null));

      // Bind the Student_t object
      pstmt.setObject (1, person, OracleTypes.STRUCT);
      
      // Execute the insertion
      if (pstmt.executeUpdate () == 1)
        System.out.println ("Successfully inserted a Student_t object");
      else
        System.out.println ("Insertion failed");
    }

    // Create and insert a ParttimeStudent_t object into the database
    {
      // Create a ParttimeStudent_t object
      person = new ParttimeStudent (
                 new NUMBER (1003), 
                 new CHAR ("John", null), 
                 new CHAR ("LA", null),
                 new NUMBER (101), 
                 new CHAR ("CS", null), 
                 new NUMBER (20)); 

      // Bind the ParttimeStudent_t object
      pstmt.setObject (1, person, OracleTypes.STRUCT);

      // Execute the insertion and print the update count
      if (pstmt.executeUpdate () == 1)
        System.out.println ("Successfully inserted a ParttimeStudent_t object");
      else
        System.out.println ("Insertion failed");
    }

    // Create and insert a Employee_t object into the database
    {
      // Create a Employee_t object
      person = new Employee (
                 new NUMBER (1004), 
                 new CHAR ("David", null),
                 new CHAR ("SF", null), 
                 new NUMBER (1111), 
                 new CHAR ("SCOTT", null));

      // Bind the Employee_t object
      pstmt.setObject (1, person, OracleTypes.STRUCT);

      // Execute the insertion
      if (pstmt.executeUpdate () == 1)
        System.out.println ("Successfully inserted a Employee_t object");
      else
        System.out.println ("Insertion failed");
    }

    // Close the PreparedStatment
    pstmt.close ();
  }

  /**
   * This method queries the database PersonTab table and prints the
   * object values.
   */
  public static void select (Connection conn) throws SQLException
  {
    System.out.println ("List the Person_t objects : ");

    // Create a statement
    Statement stmt = conn.createStatement ();

    // Execute the query
    ResultSet rset = stmt.executeQuery ("select value(t) from PersonTab t");

    // Iterate the result set
    while (rset.next ())
    {
      // The object value can be a Person_t, Student_t, ParttimeStudent_t
      // or Employee_t.
      System.out.println (
        ((OracleResultSet) rset).getORAData (1, new PersonFactory ()));
    }

    // Close the result set
    rset.close ();

    // Close the statement
    stmt.close ();
  }
}

