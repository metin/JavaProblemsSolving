/*
 * A Ntier Authentication Sample
 *
 * It 1. create new users proxy and client
 *       grant corresponding privileges to them
 *    2. create new roles role1, role2, and
 *       grant some operation privileges to those roles
 *    3. create and populate table proxy.account
 *    4. shows how to use OCIConnectionPool to get
 *       the proxy connection with "client" as user
 *       and role1 as role 
 *    5. note user client is granted with role1(select)
 *       but not role2 (insert), so the select-op
 *       succeeds but insert-op should fail
 *
 * Please use jdk1.2 or later version
 */

import java.sql.*;
import javax.sql.*;
import java.util.Properties;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;
import oracle.jdbc.oci.*;

class NtierAuth
{
  public static void main (String args [])
       throws SQLException
  {
    // Step 1: Connect as system/manager to create the users,
    //         setup roles and proxies.

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
    ods.setUser("system");
    ods.setPassword("manager");
    ods.setURL(url);
 
    // Connect to the database as system/manager
    Connection sysConn = ods.getConnection();

    // Do some cleanup
    trySQL (sysConn, "drop role role1");
    trySQL (sysConn, "drop role role2");
    trySQL (sysConn, "drop user client cascade");
    trySQL (sysConn, "drop user proxy cascade");

    // Create a Statement
    Statement sysStmt = sysConn.createStatement ();

    // Create client and proxy
    sysStmt.execute("create user proxy identified by mehul");
    sysStmt.execute("create user client identified by ding");

    // Grant privilages to client and proxy
    sysStmt.execute("grant create session, connect, " +
                    "resource to proxy");
    sysStmt.execute("grant create session, connect, " +
                    "resource to client");

    // Create and setup roles with system connection
    sysStmt.execute("create role role1");
    sysStmt.execute("create role role2");

    // Connect to the database as proxy
    ods.setUser("proxy");
    ods.setPassword("mehul");
    Connection proxyConn = ods.getConnection();

    // Create a table with proxy connection
    Statement proxyStmt = proxyConn.createStatement ();
    proxyStmt.execute("create table account (purchase number)");
    proxyStmt.execute("insert into account values (6)");

    // Grant privilages to role1, role2
    proxyStmt.execute("grant select on account to role1");
    proxyStmt.execute("grant insert on account to role2");

    // Close the proxy statement and connection
    proxyStmt.close();
    proxyConn.close();

    // Grant role1, role2 to client
    sysStmt.execute("grant role1, role2 to client");

    // Grant proxy privilage to connect as client
    sysStmt.execute("alter user client grant connect " +
                    "through proxy with role role1");

    // Step 2: Use OCIConnectionPool to get the proxy connection

    // Create an OracleOCIConnectionPool instance with default
    // configuration using proxy/mehul
    OracleOCIConnectionPool cpool = new OracleOCIConnectionPool
                                        ("proxy", "mehul", url, null);

    Properties prop = new Properties();
    String[] roles = {"role1"};
    prop.put(OracleOCIConnectionPool.PROXY_USER_NAME,"client" );
    prop.put(OracleOCIConnectionPool.PROXY_ROLES, roles);

    // Get the proxy connection
    OracleOCIConnection conn = (OracleOCIConnection) 
         cpool.getProxyConnection
               (OracleOCIConnectionPool.PROXYTYPE_USER_NAME, prop);
   
    // Create a Statement
    Statement stmt = conn.createStatement ();
    
    ResultSet rset = stmt.executeQuery ("select purchase from proxy.account");

    // Iterate through the result and print the purchase number
    System.out.println ("-- Do a Select from the proxy connection --");
    while (rset.next ())
      System.out.println (rset.getString (1));

    // Close the RseultSet
    rset.close();
    rset = null;

    // Now, try to do an Insert. This shouldn't be authorized
    System.out.println
           ("-- Now, try to do an Insert with the proxy connection --");
    try {
      stmt.execute("insert into proxy.account values (9)");
    } catch (SQLException e) {
      System.out.println ("Expected exception thrown: " + e.getMessage());
    }
    finally {
        if (stmt != null)
           stmt.close();
    }

    // Close the connection
    conn.close();
    conn = null;

    // Close the OracleOCIConnectionPool
    cpool.close();
    cpool = null;

    // Make the cleanup
    trySQL (sysConn, "drop role role1");
    trySQL (sysConn, "drop role role2");
    trySQL (sysConn, "drop user client cascade");
    trySQL (sysConn, "drop user proxy cascade");

    // Close the system statement and connection
    sysStmt.close();
    sysConn.close();

  }

  // Used for Cleaning up the database
  private static void trySQL (Connection conn, String sqlString)
      throws SQLException
  {
    // Create a Statement
    Statement stmt = conn.createStatement ();

    try {
        stmt.execute(sqlString);
        stmt.close();
    } catch (SQLException e) {
        // In case the user or role hasn't been created, ignore it.
    }
    finally {
        if (stmt != null)
           stmt.close();
    }
  }

}

