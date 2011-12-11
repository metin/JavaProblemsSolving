/**
 *  Oracle connection cache meets the JDBC 3.0 requirements
 *  Oracle connection cache supports
 *    transparent access to JDBC connection cache
 *    authentication with multiple users
 *    additional cache attributes other than user/password
 *
 *  This demo shows the basic steps involved in accessing
 *  the connection cache. 
 *   
 *  Connection cache using OracleConnectionCacheImpl is deprecated.
 *
 *  note: 1. Please rm ./JNDI/.bindings in advance
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

class ImplicitCache01 extends Thread
{
  Context ctx = null;

  public static void main (String args [])
       throws SQLException
  { 
    ImplicitCache01 cc01 = new ImplicitCache01();
    cc01.start();
  }

  public void run ()
  {
    try
    {
      setods ();
      accessConnectionCache();
    } catch (Exception e)
    {
       e.printStackTrace();
       System.exit(1);
    }
  }

  private void accessConnectionCache() throws Exception
  {
    String user = "hr";
    String pass = "hr";

    // this step supports the cache status verification
    // not neccessary in connection retrieving from cache
    OracleConnectionCacheManager  occm = 
         OracleConnectionCacheManager.getConnectionCacheManagerInstance();

    /**
     **  STEP 1 create an OracleDataSource object
     **/

    // look up the datasource object from the Context
    OracleDataSource ds = (OracleDataSource) ctx.lookup("DS_CACHE01");


    /**
     **  STEP 2  create an initial connection to establish the cache
     **/

    // We need to create a connection to create the cache 
    Connection conn = ds.getConnection(user, pass);
    System.out.println("\nRetrieve an initial connection to create the cache succeed\n");

    // List out the cache properties first
    listProperties (occm);

    // verify whether this connection cache is established
    // the existsCache("ImplicitCache01") should return true
    System.out.println("\nVerify the cache status after retrieing the first connection");
    verifyCacheStatus( occm );
    conn.close(); 

    // the existsCache("ImplicitCache01") should still return true
    // but active/available connections change
    System.out.println("\nVerify the cache status after closing the first connection");
    verifyCacheStatus( occm );


    /**
     **  STEP 3 retrieve connections from the cache
     **         and do your work
     **/    
    // Now is the steps to retrieve connections from the cache
    Connection conn1 = ds.getConnection(user, pass);
    Statement  stmt  = conn1.createStatement();
    ResultSet  rset  = stmt.executeQuery("select user from dual");
    while ( rset.next() )
    {
       String s = rset.getString(1);
    }
    System.out.println("\nWe succeed in retrieving connections from the "
                       + "cache and do some work.");

    // verify the cache status
    System.out.println("\nVerify the cache status when a connection is retrieved");
    verifyCacheStatus( occm );
    conn1.close(); 

    ds.close();
  }

  private void listProperties (OracleConnectionCacheManager occm) throws Exception
  {
     // cache name "ImplicitCache01" is set at setods()

     java.util.Properties cacheProp = occm.getCacheProperties("ImplicitCache01");
     java.util.Enumeration propertyNames = cacheProp.propertyNames();
     String key = null;
     System.out.println("The cache's properties are:");
     while ( propertyNames.hasMoreElements() )
     {
        key = (String)propertyNames.nextElement();
        System.out.print(key + ": ");
        System.out.println(cacheProp.getProperty(key));
     }
     System.out.println("\n");
  }

  private void verifyCacheStatus ( OracleConnectionCacheManager occm) throws Exception
  {
     // cache name "ImplicitCache01" is set at setods()

     // true means the specified cache is established
     // false means there is no such named cache 
     System.out.println("Cache exist status is: " + occm.existsCache("ImplicitCache01"));

     // active connection means the connection is checked out,
     System.out.println("The number of Active connections is: " +
                         occm.getNumberOfActiveConnections("ImplicitCache01"));

     // available connection means the connection instance is in
     // the cache and available to be checked out
     System.out.println("The number of available connections is: " +
                         occm.getNumberOfAvailableConnections("ImplicitCache01"));
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

    // set cache properties
    java.util.Properties prop = new java.util.Properties();
    prop.setProperty("MinLimit", "2");
    prop.setProperty("MaxLimit", "10");

    // set DataSource properties
    ods.setURL(url);
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setConnectionCachingEnabled(true); // be sure set to true
    ods.setConnectionCacheProperties (prop);
    ods.setConnectionCacheName("ImplicitCache01"); // this cache's name

    // bind the DataSource object to JNDI
    try {
       ctx.bind("DS_CACHE01", ods);
    } catch (Exception e) { }
  } // end of setods()

}
