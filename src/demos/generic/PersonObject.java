/* 
 * This sample demonstrate basic Object support in the oci8 driver
 *
 * It shows 
 * 1. how to use StructDescriptor and STRUCT to create objects 
 *    of user-defined compounded type structure 
 * 2. how to add the object into database.
 * 3. how to retrieve those objects from database.
 *
 * note: 1. It needs jdk1.2 or later version and classes12.zip
 *       2. It drops, creates, and populates table
 *          people of user including user defined data type
 *          PERSON, ADDRESS in the database 
 */

import java.sql.*;
import java.io.*;
import java.util.*;
import java.math.BigDecimal;

// this import is needed for Object Support
import oracle.sql.*;

// Importing the Oracle Jdbc driver package makes the code
// more readable
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

public class PersonObject
{
  public static void main (String args [])
       throws Exception
  {
    // The sample retrieves an object of type "person",
    // materializes the object as an object of type ADT.
    // The Object is then modified and inserted back into the database.

    String url = "jdbc:oracle:oci8:@";
    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    // Create an OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Connect to the database
    Connection conn = ods.getConnection();

    // It's faster when auto commit is off
    conn.setAutoCommit (false);

    // Create a Statement
    Statement stmt = conn.createStatement ();

    try
    {
      stmt.execute ("drop table people");
      stmt.execute ("drop type PERSON FORCE");
      stmt.execute ("drop type ADDRESS FORCE");
    }
    catch (SQLException e)
    {
      // the above drop and create statements will throw exceptions
      // if the types and tables did not exist before
    }

    stmt.execute ("create type ADDRESS as object (street VARCHAR (30), " +
                  "num NUMBER)");
    stmt.execute ("create type PERSON as object (name VARCHAR (30), " +
                  "home ADDRESS)");
    stmt.execute ("create table people (empno NUMBER, empid PERSON)");

    stmt.execute ("insert into people values (101, PERSON ('Greg', " +
                  "ADDRESS ('Van Ness', 345)))");
    stmt.execute ("insert into people values (102, PERSON ('John', " +
                  "ADDRESS ('Geary', 229)))");

    ResultSet rs = stmt.executeQuery ("select * from people");
    showResultSet (rs);
    rs.close();

    //now insert a new row

    // create a new STRUCT object with a new name and address
    // create the embedded object for the address
    Object [] address_attributes = new Object [2];
    address_attributes [0] = "Mission";
    address_attributes [1] = new BigDecimal (346);

    StructDescriptor addressDesc = 
      StructDescriptor.createDescriptor ("ADDRESS", conn);
    STRUCT address = new STRUCT (addressDesc, conn, address_attributes);

    Object [] person_attributes = new Object [2];
    person_attributes [0] = "Gary";
    person_attributes [1] = address;
    
    StructDescriptor personDesc = 
      StructDescriptor.createDescriptor("PERSON", conn);
    STRUCT new_person = new STRUCT (personDesc, conn, person_attributes);

    PreparedStatement ps = 
      conn.prepareStatement ("insert into people values (?,?)");
    ps.setInt (1, 102);
    ps.setObject (2, new_person);

    ps.execute ();
    ps.close();

    rs = stmt.executeQuery ("select * from people");
    System.out.println ();
    System.out.println (" a new row has been added to the people table");
    System.out.println ();
    showResultSet (rs);

    rs.close();
    stmt.close();
    conn.close();    
  }

  public static void showResultSet (ResultSet rs)
    throws SQLException
  {
    while (rs.next ())
    {
      int empno = rs.getInt (1);
      // retrieve the STRUCT 
      STRUCT person_struct = (STRUCT)rs.getObject (2);
      Object person_attrs[] = person_struct.getAttributes();

      System.out.println ("person name:  " + 
                          (String) person_attrs[0]);

      STRUCT address = (STRUCT) person_attrs[1];

      System.out.println ("person address: ");

      Object address_attrs[] = address.getAttributes();

      System.out.println ("street:  " + 
                          (String) address_attrs[0]);
      System.out.println ("number:  " + 
                          ((BigDecimal) address_attrs[1]).intValue());
      System.out.println ();
    }
  }
}
    

    

