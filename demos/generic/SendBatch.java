/*
 * This sample shows how to use the batching extensions.
 * In this example, we demonstrate the use of the "sendBatch" API.
 * This allows the user to actually execute a set of batched
 * execute commands.
 *
 * Please use jdk1.2 or later version 
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;

// You need to import oracle.jdbc.* in order to use the
// API extensions.
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

class SendBatch
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

    // Default batch value set to 50 for all prepared statements
    // belonging to this connection.
    ((OracleConnection)conn).setDefaultExecuteBatch (50);

    PreparedStatement ps =
      conn.prepareStatement ("insert into departments values (?, ?, ?, ?)");
    
    ps.setInt (1, 2010);
    ps.setString (2, "Import");
    ps.setInt (3, 114);
    ps.setInt (4, 1700);

    // this execute does not actually happen at this point
    System.out.println (ps.executeUpdate ());     

    ps.setInt (1, 2020);
    ps.setString (2, "Export");
    ps.setInt (3, 145);
    ps.setInt (4, 2500);

    // this execute does not actually happen at this point
    int rows = ps.executeUpdate ();  
    
    System.out.println ("Number of rows updated before calling sendBatch: "
                        + rows);

    // Execution of both previously batched executes will happen
    // at this point. The number of rows updated will be
    // returned by sendBatch.
    rows = ((OraclePreparedStatement)ps).sendBatch ();

    System.out.println ("Number of rows updated by calling sendBatch: "
                        + rows);
  
    ps.close ();
    conn.close ();
  }
}
