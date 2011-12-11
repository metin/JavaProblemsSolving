/*
 * This sample demonstrates using REF over
 * two different Sessions.
 *
 * It 1. gets a ref to a SQL structured type value 
 *       through session1.
 *    2. retrieve the ref's value through session2
 *
 * note: 1. This needs jdk1.2 or later version and classes12.zip
 *       2. Run RefClient.sql to setup the table
 *       3. RefClient.sql drops, creates, and populates table
 *          student_table of an user defined type STUDENT
 */
 
import java.util.Hashtable;
import java.math.BigDecimal;

import oracle.sql.*;
import java.sql.*;

import oracle.jdbc.pool.OracleDataSource;

public class RefClient {

  public static void main (String [] args) throws Exception {

    String user = "hr";
    String password = "hr";
    String jdbcURL1 = "jdbc:oracle:oci8:@";
    String jdbcURL2 = "jdbc:oracle:oci8:@";

    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        jdbcURL1 = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    try {
      String url2 = System.getProperty("JDBC_URL_2");
      if (url2 != null)
        jdbcURL2 = url2;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    // Create an OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser(user);
    ods.setPassword(password);
    ods.setURL(jdbcURL1);

    // get Connection to Server 1
    Connection conn = ods.getConnection();

    // Create a statment and execute the query
    Statement stmt = conn.createStatement ();
    ResultSet rs = stmt.executeQuery
                        ("select ref (s) from student_table s");
    rs.next ();

    // get the REF
    REF sess1Ref = (REF) rs.getObject (1);

    // Materialize into GenREF
    GenREF result = new GenREF (sess1Ref);
   
    // Close all the resources
    rs.close ();
    stmt.close ();
    conn.close ();
    ods.close();

    // get Connection to Server 2
    OracleDataSource ods2 = new OracleDataSource();
    ods2.setUser(user);
    ods2.setPassword(password);
    ods2.setURL(jdbcURL2);
    conn = ods2.getConnection ();

    // De-materialize REF from GenREF
    REF sess2Ref = result.getREF (conn);

    // Get the REF Values
    STRUCT student = (STRUCT) sess2Ref.getValue ();
    Object attributes[] = student.getAttributes();

    System.out.println ("name: " + (String) attributes[0]);
    System.out.println ("age:  " + 
                        ((BigDecimal) attributes[1]).intValue());
  }
}



