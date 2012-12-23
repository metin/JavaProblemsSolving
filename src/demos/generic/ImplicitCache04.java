/**
 *  Oracle connection cache meets the JDBC 3.0 Connection Cache
 *  requirements. Oracle connection cache supports
 *    transparent access to JDBC connection cache
 *    authentication with multiple users
 *    additional cache attributes other than user/password
 *
 *  This is to demo following attributes: AttributeWeights,
 *  ClosestConnectionMatch, and ConnectionWaitTimeout
 *
 *  Connection attributes, defauls, and their demos :
 *    user/password          defaults to DataSources' ImplicitCache02
 *    MinLimit               defaults to 0            ImplicitCache03
 *    MaxLimit               default  is unbound      ImplicitCache03
 *    InitialLimit           defaults to 0            ImplicitCache03
 *    MaxStatementsLimit     defaults to 0            ImplicitCache03
 *    AttributeWeights       defaults to null         ImplicitCache04
 *    LowerThresholdLimit    default is 20% of the MaxLimit
 *    PropertyCheckInterval  default is 15 minutes   ImplicitCache03
 *    ValidateConnection     default is false
 *    ClosestConnectionMatch default is false        ImplicitCache04
 *    ConnectionWaitTimeout  defaults to 0           ImplicitCache04
 *    Inactivitytimeout      defaults to -1          ImplicitCache03
 *    TimeToLiveTimeout      defaults to -1          ImplicitCache03
 *    AbandonedConnectionTimeout defaults to -1      ImplicitCache03
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

class ImplicitCache04 extends Thread
{
  Context ctx = null;
  String  url = "jdbc:oracle:oci8:@";

  // this connAttrA is shared by multiple methods
  java.util.Properties connAttrA = new java.util.Properties ();

  public static void main (String args [])
       throws SQLException
  { 
    ImplicitCache04 cc04 = new ImplicitCache04();
    cc04.start();
  }

  public void run ()
  {
    try
    {
      setup ();
      ConnectionWaitTimeout();   // demo wait timeout and other properties
      demoNoAttributeWeights();  // demo retrieving connections without
                                 // weight attribute set for the comparison
      demoAttributeWeights();    // demo AttributeWeight property
                                 // and ClosestConnectionMatch with weight set
    } catch (Exception e)
    {
       e.printStackTrace();
       System.exit(1);
    }
  }


  /**
   * This demos how to set a clear response time for connection
   * retrieving to avoid indefinite waiting. 
   **/
  private void ConnectionWaitTimeout() throws Exception
  {
    Connection conns[] = new OracleConnection[11];
    Statement  stmts[] = new Statement[11];
    int i = 0;

    // create a DataSource
    OracleDataSource ds = new OracleDataSource();

    // set DataSource properties
    ds.setURL(url);
    ds.setUser("hr");
    ds.setPassword("hr");
    ds.setConnectionCachingEnabled(true);
    ds.setConnectionCacheName("ImplicitCache04");

    java.util.Properties prop = new java.util.Properties ();
    prop.setProperty("MaxLimit", "10"); // at most 10 PooledConnections in cache

    // set ConnectionWaitTimeout attribute to give user a
    // clear reponse time on connection retrieval
    prop.setProperty("ConnectionWaitTimeout", "8"); // wait for 8 seconds

    // set PropertyCheckInterval to 5 second for testing
    prop.setProperty("PropertyCheckInterval", "5");
    ds.setConnectionCacheProperties(prop);

    System.out.println("*** demo ConnectionWaitTimeout ***");
    System.out.println("\nThe MaxLimit for the cache is 10");
    System.out.println("The connectionWaitTime set to 8 seconds");
    System.out.println("Let 10 logical connections occupy all the cache");
    for ( i = 0; i < 10; ++i )
    {
       conns[i] = ds.getConnection();
       stmts[i] = conns[i].createStatement();
       ResultSet rset = stmts[i].executeQuery("select user from dual"); 
    }

    // we should get a null return
    conns[10] = ds.getConnection();
    if ( conns[10] == null )
       System.out.println("we get the expected null return for "
                          + "additional connection retrieval");
    else
       System.err.println("Something funny happened");

    // close a connection to let others get connection
    if ( conns[0] != null )
    {
       conns[0].close();
       conns[0] = null;
    }

    // retrieving additional connections becomes available
    conns[10] = ds.getConnection();
    if ( conns[10] == null )
       System.err.println("Something funny happened");

    else
       System.out.println("After closing some connections, " +
                          "additional connections can be retrieved.");

    // close the connections, and clean up the cache
    for ( i = 0; i < 11; ++i )
    {
       if ( stmts[i] != null )
       {
          stmts[i].close();
          stmts[i] = null;
       }
       if ( conns[i] != null )
       {
          conns[i].close();
          conns[i] = null;
       }
    }
    ds.close();
    ds = null;
  } // end of ConnectionWaitTimeout()

  // This is for the comparison with demoAttributeWeights()
  private void demoNoAttributeWeights () throws Exception
  {
    createDataSource1();
    System.out.println("\n*** demo retrieving connections "
       + "without weight attributes set ***\n");
    createConnections("DS_CACHENOWEIGHT");
  }

 
  /**
   * Weights are assigned to each Key, and stored in a
   * java.util.Properties object. Each weight is an integer value
   * that defines the expensiveness of the Key. Once the weights
   * are specified on the cache, connection requests are made on
   * the DataSource calling getConnection(connectionAttributes).
   * These connectionAttributes are Keys and their associated values.
   *
   * The connection retrieval from the cache invloves searching for
   * - Key/value match
   * - Maximum total weight of all the keys of the connectionAttributes.
   **/
  private void demoAttributeWeights () throws Exception
  {
    createDataSource2();
    System.out.println("\n*** demo retrieving connections with ***");
    System.out.println("*** weight attributes set and ClosestConnectionMatch set ***\n");
    createConnections("DS_CACHEWITHWEIGHT");
  }


  /**
   * Create 2 initial connections in the cache. The 2 connections
   * carry different attributes sets: connAttrA, connAttrB
   **/
  private void createInitialConnections (OracleDataSource ods,
                                         OracleConnectionCacheManager occm)
  throws Exception
  {
    // make a connection to create the cache
    Connection connA = ods.getConnection();
    // apply connAttrA connection attributes
    ((OracleConnection)connA).applyConnectionAttributes (connAttrA);
    connA.close();

    // retrieve a connection with attributes set B
    java.util.Properties connAttrB = new java.util.Properties ();
    connAttrB.setProperty("CONNECTION_TAG", "JOE'S_CONNECTION");
    connAttrB.setProperty("SecurityGroup", "1");
    connAttrB.setProperty("Application", "HR");

    java.util.Properties cacheProp = occm.getCacheProperties ("ImplicitCache04");

    // print out the AttributeWeights
    System.out.println("\nProperty AttributeWeights contains:");
    System.out.println(cacheProp.get("AttributeWeights"));

    // make another connection and apply the attribute set
    Connection connB = ods.getConnection(connAttrB);
    // apply connAttrB connection attributes
    ((OracleConnection)connB).close(connAttrB);

    System.out.println("\nAfter creating and closing 2 connections, "
       + "cache size=" + checkNoSessions(ods));
    System.out.println("There are "
       + occm.getNumberOfActiveConnections("ImplicitCache04")
       + " connections checked out and active");
    System.out.println("There are "
       + occm.getNumberOfAvailableConnections("ImplicitCache04")
       + " connections are available in the cache");
  } // end of createInitialConnections()


  /**
   * This is to demo a exact attributes match case.
   * There is an available connection in the cache carry connAttrA.
   * Create a conn1 using getConnection(connAttrA).
   * The retrieved conn1 should contains connAttrA
   **/ 
  private void demoExactMatch (OracleDataSource ods,
                               OracleConnectionCacheManager occm)
  throws Exception
  {
    Connection conn1 = ods.getConnection(connAttrA);

    // List out the returned connection's attributes.
    java.util.Properties connAttr =
       ((OracleConnection)conn1).getConnectionAttributes();
    System.out.println("\nconn1 asks for attributes of");
    System.out.println("{NLSLANG=ISO-LATIN-1, " +
       "TRANSACTION_ISOLATION=SERIALIZABLE, READONLY=TRUE}");
    System.out.println("\nThe attributes of the returned conn1 are:");
    listProperties(connAttr);

    conn1.close(); // return this connection to the cache

    System.out.println("After closing conn1, cache size=" +
       checkNoSessions(ods));
    System.out.println("There are "
       + occm.getNumberOfActiveConnections("ImplicitCache04")
       + " connections checked out and active");
    System.out.println("There are "
       + occm.getNumberOfAvailableConnections("ImplicitCache04")
       + " connections are available in the cache");
  } // end of demoExactMatch()


  /**
   * This is to demo a closest match case, the case of there is
   * no such connection available in the cache that exactly
   * matches the connection's desired attributes set.
   *
   * If the WeightAttributes and ClosestConnectionMatch is set,
   * the returned connection is the one that has the maximum
   * combined weight. Please compare the with and without
   * WeightAttributes set cases. 
   **/
  private void demoClosestMatch (OracleDataSource ods,
                                 OracleConnectionCacheManager occm)
  throws Exception
  {
    // This connAttr2 neither match connAttrA, nor connAttrB.
    // connAttrA is closer to it since NLSLANG matches connAttrA
    // and weight 10; CONNECTION_TAG matches connAttrB and
    // weight 7. 
    java.util.Properties connAttr2 = new java.util.Properties();
    connAttr2.setProperty("NLSLANG", "ISO-LATIN-1");
    connAttr2.setProperty("CONNECTION_TAG", "JOE'S_CONNECTION");
    connAttr2.setProperty("CONNECTION_TIME", "OCT_16_2003");

    Connection conn2 = ods.getConnection(connAttr2);

    // List out the retrieved connection's attributes
    java.util.Properties returnedConnAttr =
       ((OracleConnection)conn2).getConnectionAttributes();
    System.out.println("\nconn2 asks for attributes of");
    System.out.println("{NLSLANG=ISO-LATIN-1, CONNECTION_TAG=JOE'S_CONNECTION,"
       + " CONNECTION_TIME=OCT_16_2003}");

    if ( returnedConnAttr != null )
    {
       System.out.println("\nThe retrieved conn2 carries following attributes:");
       listProperties (returnedConnAttr);
    }

    // check the unmatched attributes
    returnedConnAttr = ((OracleConnection)conn2).getUnMatchedConnectionAttributes();
    if ( returnedConnAttr != null )
    {
       System.out.println("\nconn2's unmatched attributes are:");
       listProperties (returnedConnAttr);
    }

    conn2.close(); // return the connection into the cache

    // check the cache status before retrieving conn3
    System.out.println("\nAfter creating and closing conn2, cache size="
       + checkNoSessions(ods));
    System.out.println("There are "
       + occm.getNumberOfActiveConnections("ImplicitCache04")
       + " connections checked out and active");
    System.out.println("There are "
       + occm.getNumberOfAvailableConnections("ImplicitCache04")
       + " connections are available in the cache");

    // There is no available connections in the cache that 
    // matche the connAttr3 exactly. connAttrB is the closest
    // since NLSLANG matches connAttrA weights 10; SecurityGroup
    // and CONNECTION_TAG matche the ones in connAttrB and
    // weight 15 together.
    java.util.Properties connAttr3 = new java.util.Properties ();
    connAttr3.setProperty("NLSLANG", "ISO-LATIN-1");
    connAttr3.setProperty("SecurityGroup", "1");
    connAttr3.setProperty("CONNECTION_TAG", "JOE'S_CONNECTION");

    Connection conn3 = ods.getConnection(connAttr3);
    returnedConnAttr = ((OracleConnection)conn3).getConnectionAttributes();
    System.out.println("\nconn3 asks for {NLSLANG=ISO-LATIN-1, " +
       "SecurityGroup=1, CONNECTION_TAG=JOE'S_CONNECTION} attributes");

    // List out the attributes of the retrieved connection
    if ( returnedConnAttr != null )
    {
       System.out.println("\nThe retrieved conn3 carries following attributes:");
       listProperties (returnedConnAttr );
    }

    // check the unmatched attributes
    returnedConnAttr = ((OracleConnection)conn3).getUnMatchedConnectionAttributes();
    if ( returnedConnAttr != null )
    {
       System.out.println("Following attributes are not matched for conn3:");
       listProperties (returnedConnAttr );
    }
    conn3.close();  // return the connection into the cache
  } // end of demoClosestMatche ()


  private void createConnections(String dsName) throws Exception
  {
    // look up the datasource object
    OracleDataSource ods = (OracleDataSource)ctx.lookup(dsName);

    // create a OracleCacheManager for cache status verification
    OracleConnectionCacheManager occm = 
       OracleConnectionCacheManager.getConnectionCacheManagerInstance();
    createInitialConnections(ods, occm);
    demoExactMatch (ods, occm);
    demoClosestMatch (ods, occm);

    ods.close();
  } // end of createConnections()


  /**
   * DataSource1 has no AttributeWeights property settings
   * Used for the comparison with DataSource2.
   **/
  private void createDataSource1() throws Exception
  {
    OracleDataSource ods = new OracleDataSource();

    // set cache properties
    java.util.Properties prop = new java.util.Properties();
    prop.setProperty("MinLimit", "3");
    prop.setProperty("MaxLimit", "5");
    prop.setProperty("InitialLimit", "3");
    prop.setProperty("MaxStatementsLimit", "2");

    ods.setURL(url);
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setConnectionCachingEnabled(true);
    ods.setConnectionCacheName("ImplicitCache04");
    ods.setConnectionCacheProperties(prop);

    // Bind DataSource object to JNDI
    try 
    {
       ctx.bind("DS_CACHENOWEIGHT", ods);
    } catch (Exception e) 
    {
       // ignore the exception of already binded
    }
    ods.close();
  }  // end of createDataSource1()


  /**
   * DataSource2 has the AttributeWeights set
   **/
  private void createDataSource2() throws Exception
  {
    OracleDataSource ods = new OracleDataSource();

    // set cache properties
    java.util.Properties prop = new java.util.Properties();
    prop.setProperty("MinLimit", "3");
    prop.setProperty("MaxLimit", "5");
    prop.setProperty("InitialLimit", "3");
    prop.setProperty("MaxStatementsLimit", "2");

    // set CacheAttributeWeights
    java.util.Properties weightProp = new java.util.Properties();
    weightProp.setProperty("NLSLANG", "10");
    weightProp.setProperty("TRANSACTION_ISOLATION", "8");
    weightProp.setProperty("READONLY", "4");
    weightProp.setProperty("SecurityGroup", "8");
    weightProp.setProperty("Application", "4");
    weightProp.setProperty("CONNECTION_TAG", "7");

    prop.put("AttributeWeights", weightProp);
    prop.setProperty("ClosestConnectionMatch", "true");

    ods.setURL(url);
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setConnectionCachingEnabled(true);
    ods.setConnectionCacheName("ImplicitCache04");
    ods.setConnectionCacheProperties(prop);

    // Bind DataSource object to JNDI
    try 
    {
       ctx.bind("DS_CACHEWITHWEIGHT", ods);
    } catch (Exception e) 
    {
       // ignore the exception of already binded
    }
    ods.close();
  }  // end of createDataSource2()


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

    connAttrA.setProperty("NLSLANG", "ISO-LATIN-1");
    connAttrA.setProperty("TRANSACTION_ISOLATION", "SERIALIZABLE");
    connAttrA.setProperty("READONLY", "TRUE");
  } // end of setup()
}
