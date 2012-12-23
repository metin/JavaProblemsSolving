/*
 * This example demonstrates the Oracle 9i inheritance feature. It 
 * demonstrates the access of subtype objects by using JDBC default
 * mapping.
 *
 * This example creates the object types hierarchy as follows --
 *
 *   Person_t  -+- Employee_t
 *              |
 *              +- Student_t  -- ParttimeStudent_t
 *  
 * and a object table of Person_t.
 *
 * This example inserts the Person_t, Employee_t, Student_t and
 * ParttimeStudent_t objects into the object table, and selects 
 * the objects and prints out their values.
 *
 * Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

public class Inheritance3
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

    // Insert Person_t, Student_t, ParttimeStudent_t and Employee_t
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
   * Create the database data structure to be used by this example.
   */ 
  public static void createTables (Connection conn) throws SQLException
  {
    // Remove database data created by this example
    cleanup (conn);

    // Create a Statement
    Statement stmt = conn.createStatement ();

    // Create a database object type "Person_t".
    stmt.execute ("CREATE TYPE Person_t AS OBJECT       "+
                  "(  ssn NUMBER,                       "+
                  "   name VARCHAR2(30),                "+
                  "   address VARCHAR2(100)             "+
                  ") NOT FINAL");

    // Create a database object type "Student_t" that inherits 
    // "Person_t".
    stmt.execute ("CREATE TYPE Student_t UNDER Person_t "+
                  "(  deptid NUMBER,                    "+
                  "   major VARCHAR2(30)                "+
                  ") NOT FINAL");

    // Create a database object type "Employee_t" that inherits
    // "Person_t".
    stmt.execute ("CREATE TYPE Employee_t UNDER Person_t"+
                  "(  empid NUMBER,                     "+
                  "   mgr VARCHAR2(30)                  "+
                  ")");

    // Create a database object type "PartTimeStudent_t" that
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
    STRUCT person = null;

    // Create and insert a Person_t object into the database
    {
      // Obtain the Person_t type descriptor
      StructDescriptor personDesc = 
        StructDescriptor.createDescriptor ("HR.PERSON_T", conn);

      // Prepare the Person_t attributes
      Datum[] personAttrs = 
      {
        new NUMBER (1001), 
        new CHAR ("Scott", null), 
        new CHAR ("SF", null)
      };

      // Create a Person_t object
      person = new STRUCT (personDesc, conn, personAttrs);

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
      // Obtain the Student_t type descriptor
      StructDescriptor studentDesc = 
        StructDescriptor.createDescriptor ("HR.STUDENT_T", conn);

      // Prepare the Student_t attributes
      Datum[] studentAttrs = 
      {
        new NUMBER (1002), 
        new CHAR ("Peter", null), 
        new CHAR ("NY", null),
        new NUMBER (100), 
        new CHAR ("EE", null)
      };

      // Create a Student_t object
      person = new STRUCT (studentDesc, conn, studentAttrs);

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
      // Obtain the ParttimeStudent_t type descriptor
      StructDescriptor parttimestudentDesc = 
        StructDescriptor.createDescriptor ("HR.PARTTIMESTUDENT_T", conn);

      // Prepare the ParttimeStudent_t attributes
      Datum[] parttimestudentAttrs = 
      {
        new NUMBER (1003), 
        new CHAR ("John", null), 
        new CHAR ("LA", null),
        new NUMBER (101), 
        new CHAR ("CS", null), 
        new NUMBER (20)
      };

      // Create ParttimeStudent_t object
      person = new STRUCT (parttimestudentDesc, conn, parttimestudentAttrs);

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
      // Obtain the Employee_t type descriptor
      StructDescriptor employeeDesc = 
        StructDescriptor.createDescriptor ("HR.EMPLOYEE_T", conn);

      // Prepare the Employee_t attributes
      Datum[] employeeAttrs = 
      {
        new NUMBER (1004), 
        new CHAR ("David", null),
        new CHAR ("SF", null), 
        new NUMBER (1111), 
        new CHAR ("SCOTT", null) 
      };

      // Create a Employee_t object
      person = new STRUCT (employeeDesc, conn, employeeAttrs);

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
   * This method queries the database PersonTab table and print the
   * object table values.
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
      STRUCT person = (STRUCT) rset.getObject (1);

      // Obtain the type descriptor 
      StructDescriptor desc = person.getDescriptor ();

      // Obtain the type metadata to display the attribute names
      ResultSetMetaData md = desc.getMetaData ();

      // Create a StringBuffer to save the attribute information
      StringBuffer sbuf = new StringBuffer ();

      // Write the SQL type name to hte StringBuffer
      sbuf.append (person.getSQLTypeName()+": ");

      // Access the Person_t attributes
      Datum[] attrs = person.getOracleAttributes ();

      // Iterate the attributes
      for (int i=0; i<attrs.length; i++)
      {      
        // For each attribute, we first write the attribute name 
        // the string buffer
        sbuf.append (md.getColumnName(i+1)+"=");

        // Write the attribute value to the string buffer
        sbuf.append (attrs[i].stringValue()+" ");
      }
      // Print the stringBuffer value
      System.out.println (sbuf.toString());
    }

    // Close the result set
    rset.close ();

    // Close the statement
    stmt.close ();
  }
}

