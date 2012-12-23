/* 
 * This sample demonstrate basic Ref support in the oci8 driver
 *
 * 1. It drops, creates, and populates an object table 
 *    student_table including user defined data types in the database.
 * 2. It shows how to use REF and STRUCT to retrieve each
 *    attributes of the object from the table in the database
 * 3. It needs jdk1.2 or later version and classes12.zip
 */

import java.sql.*;
import java.io.*;
import java.util.*;
import java.math.BigDecimal;

// this import is needed for Object/Ref  Support
import oracle.sql.*;

// Importing the Oracle Jdbc driver package makes the code
// more readable
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

public class PersonRef
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
      stmt.execute ("drop table student_table");
      stmt.execute ("drop type STUDENT");   
    }
    catch (SQLException e)
    {      
      // the above drop and create statements will throw exceptions
      // if the types and tables did not exist before
    }

    stmt.execute ("create type STUDENT as object " +
                  "(name VARCHAR (30), age NUMBER)");
    stmt.execute ("create table student_table of STUDENT");
    stmt.execute ("insert into student_table values ('John', 20)");

    ResultSet rs = stmt.executeQuery
                        ("select ref (s) from student_table s");
    rs.next ();

    // retrieve the ref object
    REF ref = (REF) rs.getObject (1);

    //retrieve the object value that the ref points to in the
    // object table

    STRUCT student = (STRUCT) ref.getValue ();
    Object attributes[] = student.getAttributes();

    System.out.println ("student name: " + (String) attributes[0]);
    System.out.println ("student age:  " +
                        ((BigDecimal) attributes[1]).intValue());

    rs.close();
    stmt.close();
    conn.close();
  }
}
    

    

