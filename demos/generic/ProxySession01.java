/*
 * This sample shows Oracle proxy feature on property 
 * OracleConnection.PROXYTYPE_USER_NAME.
 *
 * This property allows a user to get a connection without
 * password input. To do this, step1 is to "alter user client
 * grant connect through proxy with role roleX" without the
 * authentication phrase. step2 is to use PROXYTYPE_USER_NAME
 * property when open a proxy session. This feature release
 * the necesity that a mid-tier has to know the passwords of
 * all the database users.
 *
 * This feature is supported starting Oracle JDBC 10.2.
 *
 * Note: execute ProxySession.sql in advance
 */

import java.util.*;
import java.sql.*;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

class ProxySession01
{
  public static void main (String args [])
       throws SQLException
  {
    String url = getURL();
    OracleDataSource ods = new OracleDataSource();

    // grant user client's connect through user proxy
    grantConnect(url, ods);

    // retrieve a connection of user proxy
    OracleConnection proxyConn = getConnection("proxy", "proxy", url, ods);
 
    // isProxySession is false before open a proxy session
    System.out.println("Before a proxy session is open, isProxySession: "
        + proxyConn.isProxySession());
    // the user is proxy
    checkUser(proxyConn);

    // open a proxy session with corresponding roles.
    // this session represents user client and 
    // no need to authenticate user client
    startProxySession(proxyConn);

    // isProxySession is true after open a proxy session
    System.out.println("After the proxy session is open, isProxySession: "
        + proxyConn.isProxySession());

    // the proxy session can access user client's table
    // just as user client himself
    proxyAccessClientTables(proxyConn);

    // the proxy session can play user client's role to
    // access system's table as well
    proxyPlayClientRoles(proxyConn);

    proxyConn.close();
    proxyConn = null;
    ods.close();
    ods = null;
  }

  private static void startProxySession(OracleConnection conn)
  throws SQLException
  {
    Properties prop = new Properties();
    prop.put(OracleConnection.PROXY_USER_NAME, "client");
    // corresponds to the alter sql statment (select, insert roles)
    String[] roles = {"role1", "role2"};
    prop.put(OracleConnection.PROXY_ROLES, roles);
    conn.openProxySession(OracleConnection.PROXYTYPE_USER_NAME, prop);
  }

  private static void proxyAccessClientTables(Connection conn)
  throws SQLException
  {
    checkUser(conn);
    Statement stmt = conn.createStatement();

    // insert into user client's table, go through
    stmt.execute("insert into client_account values(100)");
    // select user client's table cliant_account, go through
    System.out.println("total 3 rows after inserting into client_account:");
    showResultSet(stmt, "client_account");

    // delete from user client's table, go through
    stmt.execute("delete from client_account where balance = 100");
    System.out.println("after delete call, 2 rows left:");
    showResultSet(stmt, "client_account");

    stmt.close();
    stmt = null;
  }

  private static void proxyPlayClientRoles(Connection conn)
  throws SQLException
  {
    Statement stmt = conn.createStatement();

    // play role2 insert into system.account, go through
    System.out.println("\ninsert into system.account, allowed");
    stmt.execute("insert into system.account values (1)");
    // play role1 select from system.account, go through
    System.out.println("select * from system.account, allowed");
    showResultSet(stmt, "system.account");
    // play role3 delete from system.account, SQLException
    try
    {
       stmt.execute("delete from system.account where purchase=1");
    } catch (SQLException e)
    {
       System.out.println("expected exception on delete from system.account");
    }
  }
 
  private static OracleConnection getConnection(String user,
                                                String password,
                                                String url,
                                                OracleDataSource ods)
  throws SQLException
  {
    ods.setUser(user);
    ods.setPassword(password);
    ods.setURL(url);
    return ((OracleConnection)ods.getConnection());
  }

  private static String getURL()
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
    return url;
  }

  private static void checkUser(Connection conn) throws SQLException
  {
     Statement stmt = conn.createStatement();
     ResultSet rset = stmt.executeQuery("select user from dual");
     while (rset.next())
     {
         System.out.println("User is: " + rset.getString(1));
     }
     rset.close();
     rset = null;
     stmt.close();
     stmt = null;
  } 
 
  private static void showResultSet(Statement stmt, String table)
  throws SQLException
  {
    ResultSet rset = stmt.executeQuery("select * from " + table);
    while (rset.next())
    {
       System.out.println(rset.getString(1));
    }
    rset.close();
    rset = null;
  }
 
  private static void grantConnect(String url, OracleDataSource ods)
  throws SQLException
  {
    // create system manger's connection to grant the permissions
    Connection sysConn = getConnection("system", "manager", url, ods);
    Statement  sysStmt = sysConn.createStatement();
    sysStmt.execute(
       "alter user client grant connect through proxy with role role1, role2");

    sysStmt.close();
    sysStmt = null;
    sysConn.close();
    sysConn = null;
  } 
}
