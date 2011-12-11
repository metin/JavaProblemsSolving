/*
 * This sample shows how to use the batching extensions.
 * In this example, we set the defaultBatch value from the
 * connection object. This affects all statements created from
 * this connection. 
 * It is possible to set the batch value individually for each 
 * statement. The API to use on the statement object is setExecuteBatch.
 *
 * Please use jdk1.2 or later version 
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;

// You need to import oracle.jdbc.* in order to use the
// API extensions.
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

class SetExecuteBatch
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

    Statement stmt = conn.createStatement ();
    stmt.execute ("delete from departments where department_id > 2000");

    // Default batch value set to 2 for all prepared statements belonging
    // to this connection.
    ((OracleConnection)conn).setDefaultExecuteBatch (2);

    PreparedStatement ps =
      conn.prepareStatement ("insert into departments values (?, ?, ?, ?)");
    
    ps.setInt (1, 2030);
    ps.setString (2, "IMS"); 
    ps.setInt (3, 103);
    ps.setInt (4, 1400);

    // No data is sent to the database by this call to executeUpdate
    System.out.println ("Number of rows updated so far: "
                        + ps.executeUpdate ());     

    ps.setInt (1, 2040);
    ps.setString (2, "Conference Center");
    ps.setInt (3, 200);
    ps.setInt (4, 1700);

    // The number of batch calls to executeUpdate is now equal to the
    // batch value of 2.  The data is now sent to the database and
    // both rows are inserted in a single roundtrip.
    int rows = ps.executeUpdate ();
    System.out.println ("Number of rows updated now: " + rows);      
  
    ps.close ();
    conn.close();
  }
}
