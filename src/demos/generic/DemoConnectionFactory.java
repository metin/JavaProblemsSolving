/*
 * Utility class to get create connections for demo
 * programs. Write code once.
 *
 * This is Oracle specific code since it uses an
 * Oracle implementation of javax.sql.DataSource
 */
import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.pool.OracleDataSource;
public class DemoConnectionFactory
{
  public static Connection getHRConnection( String [] args )
    throws SQLException
  {
    return getConnection( args, "hr", "hr" );
  }

  public static Connection getConnection( String [] args, 
                                          String user, String password) 
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

    // Create an OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser(user);
    ods.setPassword(password);
    ods.setURL(url);

    Connection conn = ods.getConnection();
    conn.setAutoCommit (false);
    return conn;
  }
}
