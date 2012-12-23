/**
 * filename: SQLDataExample.java
 *
 * It shows 1. create a user-defined data type based on
 *             SQL type that is also user-defined.
 *          2. how to insert and retrieve those data
 *          3. how to insert user defined objects into
 *             database in 2 different ways. One is SQL
 *             facility, another is to create an object
 *             of a data type in the database 
 *
 * note: 1. Please use jdk1.2 or later version and classes12.jar
 *       2. It drops, creates, and populates table EMPLOYEE_TABLE
 *          of user defined data type -- EMPLOYEE
 **/

import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;
import oracle.sql.*;
import java.math.BigDecimal;


public class SQLDataExample
{

  public static void main(String args []) throws Exception
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
    OracleConnection conn = (OracleConnection)
      ods.getConnection ();

    java.util.Map map = conn.getTypeMap();
    map.put("EMPLOYEE", Class.forName("EmployeeObj"));

    // Create a Statement
    Statement stmt = conn.createStatement ();
    try 
    {
      stmt.execute ("drop table EMPLOYEE_TABLE");
      stmt.execute ("drop type EMPLOYEE");
    }
    catch (SQLException e) 
    {      
      // An error is raised if the table/type does not
      // exist. Just ignore it.
    }

    // Create and populate tables
    stmt.execute ("CREATE TYPE EMPLOYEE AS " +
                  "OBJECT(EmpName VARCHAR2(50),EmpNo INTEGER)"); 
    stmt.execute ("CREATE TABLE EMPLOYEE_TABLE (ATTR1 EMPLOYEE)");
    stmt.execute ("INSERT INTO EMPLOYEE_TABLE " + 
                  "VALUES (EMPLOYEE('Susan Smith', 123))");
    stmt.close();

    // Create a SQLData object
    EmployeeObj e = new EmployeeObj("HR.EMPLOYEE",
                                    "George Jones", 456);

    // Insert the SQLData object
    PreparedStatement pstmt
      = conn.prepareStatement ("insert into employee_table values (?)");

    pstmt.setObject(1, e, OracleTypes.STRUCT);
    pstmt.executeQuery();
    System.out.println("insert done");
    pstmt.close();

    // Select now
    Statement s = conn.createStatement();
    OracleResultSet rs = (OracleResultSet) 
      s.executeQuery("select * from employee_table");

    while(rs.next())
    {
       EmployeeObj ee = (EmployeeObj) rs.getObject(1);
       System.out.println("EmpName: " + ee.empName +
                          " EmpNo: " + ee.empNo);
    }
    rs.close();
    s.close();

    if (conn != null)
    {
      conn.close();
    }
  }
}


