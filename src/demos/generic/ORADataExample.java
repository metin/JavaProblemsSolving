/**
 * This demonstrates how to use interface ORAData and 
 * ORADataFactory for customized types.
 * 
 * 1. It shows how to create, store, retrieve objects of
 *    user-defined types.
 * 2. It needs jdk1.2 or later version and classes12.zip
 * 3. It drops, creates, and populates table EMPLOYEE_TABLE
 *    of user defined data type -- EMPLOYEE
 **/


import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;
import oracle.sql.*;
import java.math.BigDecimal;


public class ORADataExample
{

  /* Example invocation:
     java ORADataTest "jdbc:oracle:oci8:@" HR HR \
                          "oracle.jdbc.OracleDriver"
  */

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

    // Create an OracleDataSouce instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Connect to the database
    OracleConnection conn = (OracleConnection)
      ods.getConnection ();

    // Create a Statement
    Statement stmt = conn.createStatement ();
    try 
    {
      stmt.execute ("drop table EMPLOYEE_TABLE");
      stmt.execute ("drop type EMPLOYEE");
    }
    catch (SQLException e) 
    {      
      // An error is raised if the table/type does not exist.
      // Just ignore it.
    }

    // Create and populate tables
    stmt.execute ("CREATE TYPE EMPLOYEE AS " +
                  "OBJECT(EmpName VARCHAR2(50),EmpNo INTEGER)"); 
    stmt.execute ("CREATE TABLE EMPLOYEE_TABLE (ATTR1 EMPLOYEE)");
    stmt.execute ("INSERT INTO EMPLOYEE_TABLE VALUES " +
                  "(EMPLOYEE('Susan Smith', 123))");
    stmt.close();

    // Create a ORAData object
    Employee e = new Employee("George Jones", 
                               new BigDecimal("456"));

    // Insert the ORAData object
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
       Employee ee = (Employee) rs.getORAData(1,
                                    Employee.getFactory());
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


