/*
 * This sample shows how to use the Oracle performance extensions
 * for row-prefetching. This allows the driver to fetch multiple
 * rows in one round-trip, saving unecessary round-trips to the database.
 *
 * This example shows how to set the rowPrefetch for the connection object,
 * which will be used for all statements created from this connection.
 * Please see RowPrefetch_statement.java for examples of how to set
 * the rowPrefetch for statements individually.
 *
 * note: jdk1.2 or later version is recommended. 
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;

// You need to import oacle.jdbc.driver in order to use the oracle extensions.
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

class RowPrefetch_connection
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

    // set the RowPrefetch value from the Connection object
    // This sets the rowPrefetch for *all* statements belonging
    // to this connection.
    // The rowPrefetch value can be overriden for specific statements by
    // using the setRowPrefetch API on the statement object. Please look
    // at RowPrefetch_statement.java for an example.

    
    // Please note that any statements created *before* the connection
    // rowPrefetch was set, will use the default rowPrefetch.

    ((OracleConnection)conn).setDefaultRowPrefetch (30);
    
    Statement stmt = conn.createStatement ();
    
    // Check to verify statement rowPrefetch value is 30.
    int row_prefetch = ((OracleStatement)stmt).getRowPrefetch ();
    System.out.println ("The RowPrefetch for the statement is:  "
                        + row_prefetch + "\n");

    ResultSet rset = stmt.executeQuery ("select first_name, last_name from employees");
    
    while(rset.next ())
    {
      System.out.println (rset.getString (1) + "  " + rset.getString (2));
    }
    rset.close ();
    stmt.close ();
    conn.close (); 
  }
}
