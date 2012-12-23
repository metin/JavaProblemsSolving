/*
 * This example demonstrates the Oracle 9i SQLJ Object Types
 * feature (using the SQLData representation). 
 *
 * This example creates the SQLJ Object type "Person_t"
 * to represent the Java type "Person", and a SQLJ Object
 * type "Address_t" to represent Java type "Address". Both 
 * SQLJ Object types use the "SQLDATA" representation. The
 * "Person" class implements the SQLData interface, and the "Address"
 * class implements the ORAData interface.
 * 
 * This example creates the SQLJ Object types and a 
 * database table to store SQLJ Objects, inserts a "Person"
 * instance into the table, and then selects the "Person" instance and
 * prints out its value.
 *
 * This example requires two dependency Java files (the Java classes
 * whose instances will be stored in the database): 
 *
 *   - Person.java
 *   - Address.java
 *    
 * note: 1. You need to compile all the dependency classes before
 *          you run this example.
 *       2. Please use jdk1.2 or later version and classes12.zip
 *       3. It drops, creates, and populates table PersonTab including
 *          rows of user defined data types in the database
 */

import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

public class JavaObject1
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
    Connection conn = ods.getConnection(); 

    // Create SQLJ Object types and create a 
    // database table
    createTables (conn);   

    // Insert a "Person" instance into the database table
    insert (conn);

    // Select the object previously inserted
    select (conn);

    // Remove the object previously created in the database
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

    // Create a database object type "Address_t" that maps to Java
    // type "Address"
    stmt.execute ("CREATE TYPE Address_t AS OBJECT       "+
                  "EXTERNAL NAME 'Address' "+
                  "LANGUAGE JAVA USING SQLDATA         "+
                  "(                                   "+
                  "  street_attr VARCHAR2(250) EXTERNAL NAME 'street',"+
                  "  city_attr VARCHAR2(50) EXTERNAL NAME 'city',"+
                  "  state_attr VARCHAR2(50) EXTERNAL NAME 'state',"+
                  "  zip_code_attr NUMBER(5) EXTERNAL NAME 'zipCode' "+
                  ")");
    
    // Create a database object type "Person_t" that maps to Java
    // type "Person"
    stmt.execute ("CREATE TYPE person_t AS OBJECT  "+
                  "EXTERNAL NAME 'Person'          "+
                  "LANGUAGE JAVA USING SQLDATA     "+
                  "( ss_no NUMBER(9) EXTERNAL NAME 'ssn',  "+
                  "  name VARCHAR2(100) EXTERNAL NAME 'name', "+ 
                  "  address address_t EXTERNAL NAME 'address' "+
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

    try { stmt.execute ("drop type Person_t"); } catch (SQLException e) {}
    try { stmt.execute ("drop type Address_t"); } catch (SQLException e) {}

    // Close the statement
    stmt.close ();
  }
  
  /**
   * This method inserts a Person object. The object will be stored
   * as type "Person_t" in the database.
   */ 
  public static void insert (Connection conn) throws SQLException
  {
    // Prepare the insert statement
    PreparedStatement pstmt = 
      conn.prepareStatement ("insert into PersonTab values (?)");

    // Create a Address object to be referenced by the "Person" object
    Address address = new Address ("500 Oracle Parkway",
                                   "Redwood Shores",
                                   "CA",
                                   94065);

    // Create a Person object
    Person person = new Person (1234, "Scott", address);

    // Bind the Person object
    pstmt.setObject (1, person, OracleTypes.JAVA_STRUCT);

    // Execute the insertion
    if (pstmt.executeUpdate () == 1)
      System.out.println ("Successfully inserted a Person object");
    else
      System.out.println ("Insertion failed");

    // Close the PreparedStatment
    pstmt.close ();
  }

  /**
   * This method queries the database PersonTab table and print the
   * object table values.
   */
  public static void select (Connection conn) throws SQLException
  {
    System.out.println ("List the Person objects : ");

    // Create a statement
    Statement stmt = conn.createStatement ();

    // Execute the query
    ResultSet rset = stmt.executeQuery ("select value(t) from PersonTab t");

    // Iterate the result set
    while (rset.next ())
    {
      // The object is of type "Person_t" in the database, and
      // is restored to "Person" Java object.
      System.out.println (rset.getObject(1));
    }

    // Close the result set
    rset.close ();

    // Close the statement
    stmt.close ();
  }
}

