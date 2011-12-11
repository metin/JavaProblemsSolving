/**
 * A Simple DataSource sample with JNDI.
 *
 * It shows how to bind a logical name to an OracleDataSource
 * object, and then how to retrieve the named object of
 * OracleDataSource type
 *
 * This is tested using File System based reference 
 * implementation of JNDI SPI driver from JavaSoft.
 * You need to download fscontext1_2beta3.zip from
 * JavaSoft site.
 * Include providerutil.jar & fscontext.jar extracted
 * from the above ZIP in the classpath. 
 * Create a directory with JNDI, such as ./JNDI/jdbc
 * to hold the logical name
 *
 * note: 1. Please compare to DataSource.java and
 *          rm ./JNDI/jdbc/.bindings
 *       2. Please use jdk1.2 or later version
 *
 * usage: java DataSourceJNDI <tmp_dir>
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;
import javax.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;
import javax.naming.*;
import javax.naming.spi.*;
import java.util.Hashtable;

public class DataSourceJNDI
{
  public static void main (String args [])
    throws SQLException, NamingException
  {
    if ( args.length != 1 )
    {
       System.out.println("usage: java DataSourceJNDI <tmp_dir>");
       System.exit(0);
    }

    // Initialize the Context
    Context ctx = null;
    try {
      Hashtable env = new Hashtable (5);
      env.put (Context.INITIAL_CONTEXT_FACTORY,
             "com.sun.jndi.fscontext.RefFSContextFactory");
      env.put (Context.PROVIDER_URL, "file:" + args[0]);
      ctx = new InitialContext(env);
    } catch (NamingException ne)
    {
      ne.printStackTrace();
    }

    do_bind(ctx, "jdbc/sampledb");
    do_lookup(ctx, "jdbc/sampledb");

  }

  static void do_bind (Context ctx, String ln)
    throws SQLException, NamingException
  {
    // Create a OracleDataSource instance explicitly
    OracleDataSource ods = new OracleDataSource();

    // Set the user name, password, driver type and network protocol
    String url = "jdbc:oracle:oci8:@";
    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Bind it 
    System.out.println ("Doing a bind with the logical name : " + ln);
    ctx.bind (ln,ods);
  }

  static void do_lookup (Context ctx, String ln)
    throws SQLException, NamingException
  {

    System.out.println ("Doing a lookup with the logical name : " + ln);
    OracleDataSource ods = (OracleDataSource) ctx.lookup (ln);

    // Retrieve a connection
    Connection conn = ods.getConnection();
    getUserName(conn);
    // Close the connection
    conn.close();
    conn = null;
  }

  static void getUserName(Connection conn)
       throws SQLException
  {
    // Create a Statement
    Statement stmt = conn.createStatement ();

    // Select the USER column from the dual table
    ResultSet rset = stmt.executeQuery ("select USER from dual");

    // Iterate through the result and print the employee names
    while (rset.next ())
      System.out.println ("User name is " + rset.getString (1));

    // Close the RseultSet
    rset.close();
    rset =  null;

    // Close the Statement
    stmt.close();
    stmt = null;
  }
}



