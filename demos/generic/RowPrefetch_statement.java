/*
 * This sample shows how to use the Oracle performance extensions
 * for row-prefetching. This allows the driver to fetch multiple
 * rows in one round-trip, saving unecessary round-trips to the database.
 *
 * This example shows how to set the rowPrefetch for individual
 * statements.
 *
 * note: jdk1.2 is recommended. jdk1.1 will also work
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;

// You need to import oracle.jdbc in order to use the
// Oracle extensions
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

class RowPrefetch_statement
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
      // and use the default
    }

    // Create a OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Connect to the database
    Connection conn = ods.getConnection();

    // get the value of the default row prefetch from the connection object

    int default_row_prefetch =
      ((OracleConnection)conn).getDefaultRowPrefetch ();
    System.out.println ("The Default RowPrefetch for the connection is: "
                        + default_row_prefetch);

    Statement stmt = conn.createStatement ();

    // set the RowPrefetch value from the statement object
    // This sets the rowPrefetch only for this particular statement.
    // All other statements will use the default RowPrefetch from the
    // connection.

    ((OracleStatement)stmt).setRowPrefetch (30);
    
    // Check to verify statement rowPrefetch value is 30.
    int row_prefetch = ((OracleStatement)stmt).getRowPrefetch ();
    System.out.println ("The RowPrefetch for the statement is: "
                        + row_prefetch + "\n");

    ResultSet rset = stmt.executeQuery ("select first_name, last_name from employees");
    
    while(rset.next ())
    {
      System.out.println (rset.getString (1) + "  " + rset.getString (2));
    }
    rset.close ();
    stmt.close ();
    stmt.close ();
  }
}
