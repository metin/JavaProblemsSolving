/**
 *  Oracle supports Fast Connection Failover on a Cache
 *  enabled OracleDataSource. It deals with following
 *  events: 
 *    Service down
 *    Service up
 *    node down
 *
 *  This demos the fast connection failover feature
 *  in the case of one instance and one service. In this
 *  case, if the relevant service is down, then all the
 *  connections from this service will be cleaned up, and
 *  the corresponding cache will be empty. Further
 *  connection retrieval will become unavailable. 
 *   
 *  note: 1. rm ./JNDI/.bindings in advance
 *        2. use jdk1.3.1 or later version
 *        3. ONS must be started in advance. refer
 *           to JDBC doc for ONS setup and configuration.
 */

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.naming.*;
import javax.naming.spi.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;

class FastConnectionFailover01 extends Thread
{
  Context ctx = null;
  OracleConnectionCacheManager occm = null;
  String query = "select sys_context('userenv', 'instance_name')," +
                       " sys_context('userenv', 'server_host')," +
                       " sys_context('userenv', 'service_name')" +
                       " from dual";
  String database = null;
  String instance = null;
  String service = null;
  String type = null;
  String body = null;
  String comp = null;
  String node = null;
  String host = null;

  public static void main (String args [])
       throws SQLException
  { 
    FastConnectionFailover01 fcf01 = new FastConnectionFailover01();
    fcf01.start();
  }

  public void run ()
  {
    try
    {
      setods ();

      // case of only one service invloved.
      // all connections are cleaned up at
      // DOWN event, and re-initialized at
      // UP event
      singleServiceDownUp();
    } catch (Exception e)
    {
       e.printStackTrace();
       System.exit(1);
    }
  }

  private void singleServiceDownUp() throws Exception
  {
    OracleConnection conns[] = new OracleConnection[10];
    OracleConnection conns2[] = new OracleConnection[10];

    // lookup for a DataSource
    OracleDataSource ds = (OracleDataSource)ctx.lookup("DS_FCF01");

    // set cache properties
    java.util.Properties prop = new java.util.Properties();
    prop.setProperty("MaxLimit", "20");
    prop.setProperty("MinLimit", "10");
    ds.setConnectionCacheProperties (prop);

    // Get 10 connections from the cache
    System.out.println("Get 10 connections from the cache");
    getConnections(ds, conns, 10);
    // show the settings for ConnectionFailoverEnabled
    System.out.println("ConnectionFailoverEnabled: " +
      ds.getFastConnectionFailoverEnabled());
    verifyCache();

    // Simulate a service down event
    handleDownEvent();
    verifyCache();

    // Simulate a service up event
    handleUpEvent(); 
    verifyCache();

    // check out 10 connections from the cache
    getConnections(ds, conns2, 10);
    verifyCache();

    // clean up the datasource
    for (int i = 0; i < 10; ++i)
    {
      if ( conns[i] != null )
      {
         conns[i].close();
         conns[i] = null;
      }
      if ( conns2[i] != null )
      {
         conns2[i].close();
         conns2[i] = null;
      }
    }
    ds.close();
    ds = null;
  } // end of singleServiceDownUp()

  // create service down event and sent it out
  private void handleDownEvent() throws Exception
  {
    System.out.println("\n*** the service DOWN ***");
    type = "database/event/service";
    body = "VERSION=1.0" +
           " service=" + service +
           " instance=" + instance +
           " database=" + database +
           " host=" + host +
           " status=DOWN";
    if (sendEvent(type, comp, node, body))
       System.out.println("Service DOWN event is sent out");
  }

  // create service up event and send it out
  private void handleUpEvent() throws Exception
  {
    System.out.println("\n*** the service UP ***");
    body = "VERSION=1.0" +
           " service=" + service +
           " instance=" + instance +
           " database=" + database +
           " host=" + host +
           " status=up";
    if (sendEvent(type, comp, node, body))
       System.out.println("Service UP event is sent out");

  }

  private void getConnections(OracleDataSource   ds,
                              OracleConnection[] conns,
                              int                num)
  throws Exception
  {
    String instances[] = new String[num];
    String hosts[] = new String[num];
    String services[] = new String[num];
    Statement stmt = null;
    ResultSet rs = null;
    int i = 0;

    for (i = 0; i < num; ++i)
    {
      conns[i] = (OracleConnection)ds.getConnection();
      stmt = conns[i].createStatement();
      rs = stmt.executeQuery(query);
      rs.next();
      instances[i] = rs.getString(1);
      hosts[i] = rs.getString(2);
      services[i] = rs.getString(3);

      System.out.println("Conn[" + i + "] from Instance: " +
        instances[i] + "; Host: " + hosts[i] + "; Service: " +
        services[i]);
      rs.close();
      stmt.close();
    }

    // record the instance name and service name
    // in order to simulate a service down event.
    instance = instances[0];
    database = instance;
    service = services[0];
    host = hosts[0];
  } // end of getConnections()

  private void verifyCache() throws Exception
  {
    System.out.println("NumberOfAvaibleConnections: " +
      occm.getNumberOfAvailableConnections("FCFCache01"));
    System.out.println("NumberOfActiveConnections: " +
      occm.getNumberOfActiveConnections("FCFCache01"));
  }

  private boolean sendEvent(String type,
                            String comp,
                            String node,
                            String body)
  {
     try
     {
       System.out.println("Send out Event: type=" + type +
          " comp=" + comp + " node=" + node + " body=" + body);

       // create a Publisher object for pulishing notifiactions
       oracle.ons.Publisher p = new oracle.ons.Publisher("generator");

       // create the notification body
       oracle.ons.Notification anotification =
          new oracle.ons.Notification(
          type, comp, node, body.getBytes());

       // publish the notification
       p.publish(anotification);

       // wait for 3 second for triggering the event
       java.lang.Thread.currentThread().sleep(3000);

       return true;
     } catch (Exception e)
     {
       System.out.println("Sending event failed");
       e.printStackTrace();
       return false;
     }
   } // end of sendEvent()

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

    // create a cache manager
    occm =
     OracleConnectionCacheManager.getConnectionCacheManagerInstance();

    // create a DataSources
    OracleDataSource ods = new OracleDataSource(); 

    // set DataSource properties
    ods.setURL(url);
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setConnectionCachingEnabled(true); // be sure set to true
    ods.setFastConnectionFailoverEnabled(true); // be sure set to true
    ods.setConnectionCacheName("FCFCache01"); // one cache's name

    // bind the DataSource objects to JNDI
    try {
       ctx.bind("DS_FCF01", ods);
    } catch (Exception e) { }
    ods.close();
  } // end of setods()
}
