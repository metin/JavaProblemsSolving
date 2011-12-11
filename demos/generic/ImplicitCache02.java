
/**
 *  Oracle connection cache meets the JDBC 3.0 Connection Cache
 *  requirements. Oracle connection cache supports
 *    transparent access to JDBC connection cache
 *    authentication with multiple users
 *    additional cache attributes other than user/password
 *
 *  This sample demos various getConnection APIs and
 *  authentication with multiple users.
 *
 *  Connection cache using OracleConnectionCacheImpl is deprecated.
 *
 *  note: 1. JNDI sub directory must exists in this directory
 *        2. Please use jdk1.3.1 or later version
 *        3. ImplicitCache01 ~ ImplicitCache05 demonstrate implicit connection cache features
 */

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.naming.*;
import javax.naming.spi.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;

class ImplicitCache02 extends Thread
{
  Context ctx = null;

  public static void main (String args [])
       throws SQLException
  { 
    ImplicitCache02 cc02 = new ImplicitCache02();
    cc02.start();
  }

  public void run ()
  {
    try
    {
      setods ();            // create an OracleDataSource
      retrieveConnection(); // retrieve a connection
    } catch (Exception e)
    {
       e.printStackTrace();
       System.exit(1);
    }
  }

  private void retrieveConnection() throws Exception
  {
    // look up the datasource object from the Context
    OracleDataSource ds = (OracleDataSource) ctx.lookup("DS_CACHE02");

    OracleConnectionCacheManager  occm =
         OracleConnectionCacheManager.getConnectionCacheManagerInstance();
 

    /** use getConnection() API 
     *  retrieve a connection from cache by matching the DataSource
     *  properties user/password. use the Datasource user/password
     *
     *  this initial connection creating establishes the cache
     **/
    Connection conn1 = ds.getConnection();
    System.out.println("\n*** demo getConnection() ***");
    System.out.println("getConnection() as " +
                       ((OracleConnection)conn1).getUserName() + " succeed");
    System.out.println("The user on the DataSource is " + ds.getUser() );


    // Verify whether this conneciton cache is establised 
    // the exixtsCache("ImplicitCache02") should return true
    System.out.println("\nAfter retrieing the first connection, the cache status:");
    verifyCacheStatus( occm );
    System.out.println("Closing the first connection");
    conn1.close();
    conn1 = null;

    // list out the connection cache properties when the cache
    // is established
    System.out.println("\nThe cache's properties are:");
    java.util.Properties cacheProp = occm.getCacheProperties("ImplicitCache02");
    listProperties (cacheProp);

    /** use getConnection(user,password) API
     *  The user/password on the DataSource is ignored. use different 
     *  user authentication create a table under this user. 
     **/
    System.out.println("\n*** demo getConnection(user, password) ***");
    Connection conn2 = ds.getConnection("scott", "tiger");
    doSomeWork(conn2);
    System.out.println("\ngetConnection() as " +
                       ((OracleConnection)conn2).getUserName() + " succeed");

    // list out the unmatched properties that user scott desired
    java.util.Properties unmatchedProp =
       ((OracleConnection)conn2).getUnMatchedConnectionAttributes();
    if ( unmatchedProp == null )
       System.out.println("conn2 desired properties are all matched");
    else
    {
       System.out.println("The unmatched properties for conn2 as user scott are:");
       listProperties (unmatchedProp);
    }
 
    Connection conn3 = ds.getConnection("adams", "wood");
    System.out.println("\ngetConnection() as " +
                       ((OracleConnection)conn3).getUserName() + " succeed");

    // verify the cache status
    // the existsCache("ImplicitCache02") should return true
    System.out.println("After retrieing 2 connections with different "
      + "user authentication, the cache status:");
    verifyCacheStatus( occm );

    // use system/manager to grant user adams necessary privileges
    Connection connsys = ds.getConnection("system", "manager");
    Statement  stmtsys = connsys.createStatement();
    stmtsys.execute("grant connect, resource, create any table, "
                    + "drop any table to adams");
    stmtsys.close();
    connsys.close();
    stmtsys = null;
    connsys = null;

    Statement stmt = conn3.createStatement();
    try
    {
       stmt.execute("drop table adams_tab");
    } catch (SQLException e)
    {
       // ignore the table not exists error 
    }

    // under user adams, create table adams_tab, and do some insertion
    stmt.executeUpdate("create table adams_tab(col1 NUMBER, col2 VARCHAR2 (20))");
    stmt.executeUpdate("insert into adams_tab values(1, 'adams')");
    stmt.executeUpdate("insert into adams_tab values(2, 'scott')");

    // verify table adams_tab
    ResultSet rs = stmt.executeQuery("select * from adams_tab");
    System.out.println("\nwith different user authenticated connection, " +
                       "we can modify the database as well"); 
    System.out.println("New table adams_tab is created and contains contents:");
    while ( rs.next() )
    {
        System.out.println(rs.getInt(1) + " " + rs.getString(2));
    }

    stmt.executeUpdate("drop table adams_tab");
    stmt.close();
    stmt = null; 

    /** use getConnection(connectionAttributes) API
     *  additional attributes 
     **/
    System.out.println("\n*** demo getConnection(connectionAttributes) ***");
    java.util.Properties connAttr = new java.util.Properties();
    connAttr.setProperty("NLS_LANG", "ISO-LATIN-1");

    // retrieve connection that matches NLS_LANG
    Connection conn4 = ds.getConnection(connAttr); 
    doSomeWork(conn4);
    System.out.println("getConnection(connAttr) succeed");

    // list out the attributes of conn4
    System.out.println("conn4 desired NLS_LANG=ISO-LATIN-1 property.");
    System.out.println("The actual properties carried by conn4 "
       + "retrieved from the cache are:");
    java.util.Properties connProp =
             ((OracleConnection)conn4).getConnectionAttributes();
    listProperties (connProp);
 
    // list out the unmatched properties for conn4 but desired
    unmatchedProp = ((OracleConnection)conn4).getUnMatchedConnectionAttributes();
    if ( unmatchedProp == null )
       System.out.println("conn4 desired properties are all matched");
    else
    {
       System.out.println("The unmatched properties for conn4 are:");
       listProperties (unmatchedProp);
    }

    // apply NLS_LANG attributes to the connection
    System.out.println("Apply NLS_LANG attribute to conn4");
    ((OracleConnection)conn4).applyConnectionAttributes(connAttr);  

    // verify whether conn4 contain NLS_LANG property fter apply attributes
    connProp = ((OracleConnection)conn4).getConnectionAttributes(); 
    System.out.println("Now, conn4 should contain NLS_LANG property");
    System.out.println("conn4's property list:");
    listProperties (connProp);

    // verify the cache status after getConnection(connectionAttributes)
    System.out.println("\nAfter the 4th connection retrieving, the cache status:");
    verifyCacheStatus( occm );

 
    /** show another way to apply attributes to the connection
     *  step1: connA.close(Attr);
     *  step2: connB = ds.getConnection(Attr);
     **/
    System.out.println("\n*** demo another way to apply attributes ***"); 
    System.out.println("The verification has 2 steps.");
    System.out.println("Step1 is to use close(Attr) to apply the attributes");
    System.out.println("Step2 is to use getConnection(Attr) to retrieve that connection\n");

    java.util.Properties connAttr2 = new java.util.Properties();
    connAttr2.setProperty("TRANSACTION_ISOLATION", "SERIALIZABLE");
    connAttr2.setProperty("CONNECTION_TAG", "JOE'S_CONNECTION");
    Connection conn5 = ds.getConnection(connAttr2);
    doSomeWork(conn5);
    System.out.println("\nRetrieving 5th connection with specified attributes succeed");
    connProp = ((OracleConnection)conn5).getConnectionAttributes();
    if ( connProp == null )
       System.out.println("The desired attributes by conn5 is not set");
    else
    {
       System.out.println("conn5 carry following attributes:");
       listProperties (connProp);
    }

    ((OracleConnection)conn5).close(connAttr2); // apply attributes
    System.out.println("Closing conn5 using close(connAttr)");

    // verify the cache status after retrieving and closing
    // a connection with a specified attributes set
    System.out.println("\nAfter the 5th connection retrieving and"
       + " closing, the cache status:");
    verifyCacheStatus( occm );

    /** retrieve a specific connection from the cache using a 
     *  specified Connection attributes. Verify whether the retrieved 
     *  conn6 contains conn5's properties.
     **/
    Connection conn6 = ds.getConnection(connAttr2);
    doSomeWork(conn6);
    connProp = ((OracleConnection)conn6).getConnectionAttributes();
    System.out.println("\nAfter closing a connection using conn.close(connAttr),");
    System.out.println("the call of getConnection(connAttr) should return a "
       +"connection that carry connAttr's attributes");
    System.out.println("conn6's properties are:");
    listProperties (connProp);

    // verify the cache status
    System.out.println("After retrieving 6th connection, "
       + "the cache status:");
    verifyCacheStatus( occm );


    /** getConnection(user,password,connAttr) API
     *  to retrieve a connection with the specified user, properties
     *  List out the attributes got from the retrieved
     *  connection and unmatched attributes originally desired.
     *  This is usefull in deciding whether to reinitialize some
     *  of the session state or not.
     **/
    // retrieve the connection use specified attributes
    java.util.Properties connAttr7 = new java.util.Properties();
    connAttr7.setProperty("NLSLANG", "ISO-LATIN-1");
    connAttr7.setProperty("TRANSACTION_ISOLATION", "SERIALIZABLE");
    connAttr7.setProperty("Application", "HR");   

    Connection conn7 = ds.getConnection("scott", "tiger", connAttr7);
    System.out.println("\n*** demo getConnection(user,password,connAttr7) ***");
    doSomeWork(conn7);
    System.out.println("connection7 succeed"); 

    // List out the matched properties
    connProp = ((OracleConnection)conn7).getConnectionAttributes();
    System.out.println("\nThe properties of the connection are:");
    listProperties (connProp);

    // List out the unmatched properties
    connProp = ((OracleConnection)conn7).getUnMatchedConnectionAttributes ();
    System.out.println("Unmatched properties are:");
    listProperties (connProp);

    // verify the cache status after retriveing the 7th connection
    System.out.println("\nAfter retrieving the 7th connection, "
      + "the cache status:");
    verifyCacheStatus( occm );


    if ( conn1 != null )
      conn1.close();
    if ( conn2 != null )
      conn2.close();
    if ( conn3 != null )
      conn3.close();
    if ( conn4 != null )
      conn4.close();
    if ( conn5 != null )
      conn5.close();
    if ( conn6 != null )
      conn6.close();
    if ( conn7 != null )
      conn7.close();
    ds.close();
  }

  private void doSomeWork(Connection conn) throws Exception
  {
     Statement stmt = conn.createStatement();
     ResultSet  rset  = stmt.executeQuery("select user from dual");
     while ( rset.next() )
     {
        String s = rset.getString(1);
     }
  }

  private void listProperties(java.util.Properties prop)
  {
    java.util.Enumeration propertyNames = prop.propertyNames();
    String key = null;
    while ( propertyNames.hasMoreElements() )
    {
        key = (String)propertyNames.nextElement ();
        System.out.print(key + ": ");
        System.out.println (prop.getProperty(key));
    }
    System.out.print("\n");
  } // end of listProperties()

  private void verifyCacheStatus ( OracleConnectionCacheManager occm) throws Exception
  {
     // cache name "ImplicitCache02" is set at setods()

     // true means the specified cache is established
     // false means there is no such named cache
     System.out.println("Cache exist status is: " + occm.existsCache("ImplicitCache02"));

     // active connection means the connection is checked out,
     System.out.println("The number of Active connections is: " +
                         occm.getNumberOfActiveConnections("ImplicitCache02"));

     // available connection means the connection instance is in
     // the cache and available to be checked out
     System.out.println("The number of available connections is: " +
                         occm.getNumberOfAvailableConnections("ImplicitCache02"));
  }

  private void setods() throws Exception
  {
    // create a context for holding name-to-object bindings
    Hashtable env = new Hashtable (5);
    env.put (Context.INITIAL_CONTEXT_FACTORY,
           "com.sun.jndi.fscontext.RefFSContextFactory");
    env.put (Context.PROVIDER_URL, "file:./JNDI" );
    ctx = new InitialContext (env);

    // use the url supplied in command line or the default
    String url = "jdbc:oracle:oci8:@";
    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    // create a DataSource
    OracleDataSource ods = new OracleDataSource();

    // set DataSource properties
    ods.setURL(url);
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setConnectionCachingEnabled(true);
    ods.setConnectionCacheName("ImplicitCache02");

    // set DataSource object to JNDI
    try {
       ctx.bind("DS_CACHE02", ods);
    } catch (Exception e) { }
  } // end of setods()

}
