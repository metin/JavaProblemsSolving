/**
 *  Oracle connection cache meets the JDBC 3.0 Connection Cache
 *  requirements. Oracle connection cache supports
 *    transparent access to JDBC connection cache
 *    authentication with multiple users
 *    additional cache attributes other than user/password
 *
 *  This sample demos Connection Cache Manager APIs
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

class ImplicitCache05 extends Thread
{
  Context ctx = null;
  OracleConnectionCacheManager occm = null;
  String  url = "jdbc:oracle:oci8:@";

  public static void main (String args [])
       throws SQLException
  { 
    ImplicitCache05 cc05 = new ImplicitCache05();
    cc05.start();
  }

  public void run ()
  {
    try
    {
      setup ();
      setods1();
      setods2();
      demoConnectionCacheManager(); 
    } catch (Exception e)
    {
       e.printStackTrace();
       System.exit(1);
    }
  }


  /**
   * List out following cache status:
   * cache exist status,              number of active connections
   * number of available connections, cache properties
   **/
  private void checkCacheStatus (OracleConnectionCacheManager occm,
                                 String cacheName,
                                 OracleDataSource ds)
  throws Exception
  {
    // check if the specific connection cache exists among the list
    // of caches that Connection Cache Manager handles
    System.out.println("\n*** check cache status ***");
    System.out.println(cacheName + " exist status is: " +
                       occm.existsCache(cacheName));
    System.out.println("DS_CCACHE21_Cache exist status is: " +
                       occm.existsCache("DS_CCACHE21_Cache"));

    // number of available connections in the specified cache
    System.out.println(occm.getNumberOfAvailableConnections(cacheName)
        + " connections are available in cache " + cacheName);

    // number of active connections in the specified cache
    System.out.println(occm.getNumberOfActiveConnections(cacheName)
        + " connections are active");

    // list the cache's properties
    System.out.println("\n*** The cache properties ***");
    java.util.Properties cacheProp = occm.getCacheProperties(cacheName);
    java.util.Enumeration propertyNames = cacheProp.propertyNames();
    System.out.println("The properties of Cache " + cacheName + ":");
    String key = null;
    while ( propertyNames.hasMoreElements() )
    {
       key = (String)propertyNames.nextElement();
       System.out.print(key + " ");
       System.out.println(cacheProp.getProperty(key));
    }
  } // end of checkCacheStatus()


  private void demoDisableCache (OracleConnectionCacheManager occm,
                                 String cacheName,
                                 OracleDataSource ds,
                                 Connection conn)
  throws Exception
  {
    occm.disableCache(cacheName);
    System.out.println("\n\nAfter disable the cache " + cacheName);
    try
    {
       Statement stmt = conn.createStatement();
       ResultSet rs = stmt.executeQuery("select user from dual");
       rs.close();
       stmt.close();
       System.out.println("unclosed conn1 is still working. expected."); // expected
    } catch (SQLException e)
    {
       System.err.print("conn1 is not working! unexpected."); // error
    }

    try
    {
       Connection conn3 = ds.getConnection();
       System.err.println("We still retrieve connection from the cache "
          + cacheName + ". unexpected."); // error
    } catch (SQLException e)
    {
       // this is expected
       System.out.println("Unable to retrieve any more connection "
          + "from the cache ImplicitCache05. expected.");
    }
  } // end of demoDisableCache()


  private void demoReinitializeCache (OracleConnectionCacheManager occm,
                                      String cacheName,
                                      OracleDataSource ds)
  throws Exception
  {
    java.util.Properties newProp = new java.util.Properties();
    newProp.setProperty("MinLimit", "4");
    newProp.setProperty("MaxLimit", "6");
    newProp.setProperty("InitialLimit", "4");
    occm.reinitializeCache(cacheName, newProp);

    // list out the new properties
    java.util.Properties newcacheProp = occm.getCacheProperties(cacheName);
    java.util.Enumeration newenum = newcacheProp.propertyNames();
    System.out.println("Connection Cache Properties for " + cacheName + ":");
    String key = null;
    while ( newenum.hasMoreElements() )
    {
       key = (String)newenum.nextElement();
       System.out.print(key + " ");
       System.out.println(newcacheProp.getProperty(key));
    }

    System.out.println("Session Number is:" + checkNoSessions(ds));
  } // end of demoReinitializeCache ()


  private void demoConnectionCacheManager() throws Exception
  {
    // Create 2 DataSource objects for 2 caches 
    OracleDataSource ds = (OracleDataSource) ctx.lookup("DS_CACHE05");
    OracleDataSource ds2 = (OracleDataSource) ctx.lookup("DS_CACHE05_2");

    // set properties for ds
    java.util.Properties prop = new java.util.Properties();
    prop.setProperty("MinLimit", "1");
    prop.setProperty("MaxLimit", "8");
    prop.setProperty("InitialLimit", "3");

    // set properties for ds2
    java.util.Properties prop2 = new java.util.Properties();
    prop2.setProperty("MinLimit", "3");
    prop2.setProperty("MaxLimit", "10");
    prop2.setProperty("ConnectionWaitTimeout", "5");

    // create a cache named as ImplicitCache05 based on ds and prop
    occm.createCache("ImplicitCache05", ds, prop);

    // creat a second cache named as anotherCache based on ds2 and prop2
    occm.createCache("anotherCache", ds2, prop2);

    System.out.println("\n*** show cache names controled by the cache manager ***");
    String names[] = occm.getCacheNameList();
    for ( int i = 0; i < names.length; ++i )
       System.out.println("one cache name is: " + names[i]);

    // create intial connections 
    Connection conn1 = ds.getConnection(); // keep the conn1 alive

    Connection conn2 = ds2.getConnection();
    conn2.close();

    Thread.sleep (3000); // wait 3 seconds for the warming up

    // check the caches' status
    System.out.println("\n\n*** Cache status after getting the initial connection ***");
    System.out.println("\nCache status for cache ImplicitCache05:");
    checkCacheStatus (occm, "ImplicitCache05",ds);
    System.out.println("\nCache status for cache anotherCache:");
    checkCacheStatus (occm, "anotherCache", ds2);

    // diable cache  ImplicitCache05
    // check its conn1 whether still accessible
    demoDisableCache(occm, "ImplicitCache05", ds, conn1);

    // check whether anotherCache works fine
    conn2 = ds2.getConnection();
    Statement stmt = conn2.createStatement();
    stmt.executeQuery("select user from dual");
    stmt.close();
    conn2.close();
    System.out.println("The anotherCache works fine. expected");
    
    // enable cache ImplicitCache05
    // check its functionality
    System.out.println("\nEnable the cache ImplicitCache05");
    occm.enableCache("ImplicitCache05");
    Connection conn3 = ds.getConnection();
    System.out.println("We are able to retrieve connection from cache "
       + "ImplicitCache05 again");
    System.out.println("Session Number is: " + checkNoSessions(ds));

    if ( conn1 != null ) 
       conn1.close();
    if ( conn3 != null )
       conn3.close();

    // refresh cache ImplicitCache05
    occm.refreshCache("ImplicitCache05",
       OracleConnectionCacheManager.REFRESH_ALL_CONNECTIONS);
    System.out.println("\nAfter close all the connections and refresh"
       + " cache ImplicitCache05,");
    System.out.println("Session Number becomes: " + checkNoSessions(ds)); 

    // re-initialize cache ImplicitCache05 and check
    System.out.println("\n*** After reinitialize cache ImplicitCache05 ***");
    demoReinitializeCache(occm, "ImplicitCache05", ds); 

    // remove cache ImplicitCache05
    occm.removeCache("ImplicitCache05", 0);
    Thread.sleep (1000); // wait for 1 second
  
    names = occm.getCacheNameList();
    System.out.println("\nAfter remove ImplicitCache05 cache, " +
        "ImplicitCache05 would disappear from the ConnectionCacheManager");
    for ( int i = 0; i < names.length; ++i )
        System.out.println("one cache's name: " + names[i]);
 
  } // end of demoConnetionCacheManager()

  private void setup() throws Exception
  {
    // create a context for holding name-to-object bindings
    Hashtable env = new Hashtable (5);
    env.put (Context.INITIAL_CONTEXT_FACTORY,
           "com.sun.jndi.fscontext.RefFSContextFactory");
    env.put (Context.PROVIDER_URL, "file:./JNDI" );
    ctx = new InitialContext (env);

    // use the url supplied in command line or the default
    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    // create the cache manager
    occm = OracleConnectionCacheManager.getConnectionCacheManagerInstance();
  } // end of setup ()

  private void setods1() throws Exception
  {
    // create a DataSource
    OracleDataSource ods = new OracleDataSource();

    // set DataSource properties
    ods.setURL(url);
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setConnectionCachingEnabled(true);

    // set DataSource object to JNDI
    try {
       ctx.bind("DS_CACHE05", ods);
       //ctx.bind("DS_CACHE05_2", ods2);
    } catch (Exception e) { }
    ods.close();
  } // end of setods1()

  private void setods2() throws Exception
  {
    OracleDataSource ods = new OracleDataSource();

    // set properties for ods2
    ods.setURL(url);
    ods.setUser("scott");
    ods.setPassword("tiger");
    ods.setConnectionCachingEnabled(true);
   
    try
    {
       ctx.bind("DS_CACHE05_2", ods);
    } catch (Exception e)
    {
       // ignore the already binded error
    }
    ods.close();
  } // end of setods2()


  private int checkNoSessions ( OracleDataSource ods)
  {
     Connection conn = null;
     PreparedStatement pstmt = null;
     ResultSet rs = null;
     int sessions = 0;

     try
     {
         conn = ods.getConnection("system", "manager");
         pstmt = conn.prepareStatement (
                      "SELECT COUNT(username) " +
                      "FROM v$session " +
                      "WHERE type != \'BACKGROUND\'");
         rs = pstmt.executeQuery ();
         rs.next ();
         sessions = rs.getInt (1);
         // subtracting the entry of system/manager from the actual
         // entry
         --sessions;

         rs.close(); 
         rs = null;
         pstmt.close();
         pstmt = null;
         conn.close ();
         conn = null;
     } catch (SQLException e)
     {
         e.printStackTrace();
     }

     return sessions;
  } // end of private int checkNoSessions ()

}
