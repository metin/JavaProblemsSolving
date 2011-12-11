/*
 * This sample shows how to use the "define" extensions.
 * The define extensions allow the user to specify the types
 * under which to retrieve column data in a query. 
 *
 * This saves round-trips to the database (otherwise necessary to
 * gather information regarding the types in the select-list) and
 * conversions from native types to the types under which the user
 * will get the data.
 *
 * This can also be used to avoid streaming of long columns, by defining
 * them as CHAR or VARCHAR types.
 *
 * note: jdk1.2 or later version is recommended. 
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;

// You need to import oracle.jdbc.* in order to use the
// API extensions.
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

class DefineColumnType
{
  public static void main (String args [])
       throws SQLException
  {
    String url = "jdbc:oracle:oci8:@";
    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
    }

    // Create an OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Connect to the database
    Connection conn = ods.getConnection();

    Statement stmt = conn.createStatement ();

    // Call DefineColumnType to specify that the column will be
    // retrieved as a String to avoid conversion from NUMBER to String
    // on the client side.  This also avoids a round-trip to the
    // database to get the column type.
    //
    // There are 2 defineColumnType API.  We use the one with 3 arguments.
    // The 3rd argument allows us to specify the maximum length
    // of the String.  The values obtained for this column will
    // not exceed this length.

    ((OracleStatement)stmt).defineColumnType (1, Types.VARCHAR, 7);

    ResultSet rset = stmt.executeQuery ("select employee_id from employees");
    while (rset.next ())
    {
      System.out.println (rset.getString (1));
    }

    // Close the resultSet
    rset.close();

    // Close the statement
    stmt.close ();
 
    // Close the connection
    conn.close();
  }
}
