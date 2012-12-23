/**
 *  Oracle supports Fast Connection Failover on a Cache
 *  enabled OracleDataSource. It deals with following
 *  events: 
 *    Service down
 *    Service up
 *    node down
 *
 *  This demos the fast connection failover feature
 *  in the case of one instance with multi services. In 
 *  this case, one service member's event won't affect
 *  other service members activities.
 *   
 *  note: 1. rm ./JNDI/.bindings in advance
 *        2. use jdk1.3.1 or later version
 *        3. ONS must be started in advance. refer
 *           to JDBC doc for ONS setup and configuration
 *        4. replace host/port/service_name with
 *           yours. 
 */

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.naming.*;
import javax.naming.spi.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;

class FastConnectionFailover02 extends Thread
{
  Context ctx = null;
  OracleConnectionCacheManager occm = null;
  String query = "select sys_context('userenv', 'instance_name')," +
                       " sys_context('userenv', 'server_host')," +
                       " sys_context('userenv', 'service_name')" +
                       " from dual";
  String instance = null;
  String service = null;
  String type = null;
  String body = null;
  String comp = null;
  String node = null;
  String host = null;
  String urlgl = null;  // corresponds to service gl
  String urlcrm = null; // corresponds to service crm

  public static void main (String args [])
       throws SQLException
  { 
    FastConnectionFailover02 fcf02 =
       new FastConnectionFailover02();
    fcf02.start();
  }

  public void run ()
  {
    try
    {
      // one service down/up,
      // another service keeps running.
      //
      // on DOWN event, connections retrieved from
      // the down-service are cleaned, no affect on
      // the other connectons.
      oneServiceDownOneServiceUp();
    } catch (Exception e)
    {
       e.printStackTrace();
       System.exit(1);
    }
  }


  private void oneServiceDownOneServiceUp() throws Exception
  {
    occm =
     OracleConnectionCacheManager.getConnectionCacheManagerInstance();
    Connection conns1[] = new OracleConnection[20];
    Connection conns2[] = new OracleConnection[20];
    getURLs();

    // create DataSources for service gl and crm
    OracleDataSource dsgl = createDataSource("FCFCacheGL", urlgl); 
    OracleDataSource dscrm = createDataSource("FCFCacheCRM", urlcrm);
    // show the settings for ConnectionFailoverEnabled
    printFailoverSettings(dsgl, dscrm);

    // retrieve connections from both datasources
    System.out.println("\nGet 5 connections from service gl");
    getConnections(dsgl, conns1, 5);
    System.out.println("\nGet 5 connections from service crm");
    getConnections(dscrm, conns2, 5);
    verifyCache();

    // service gl down event
    handleDownEvent();
    verifyCache();

    // service gl up event 
    handleUpEvent();
    verifyCache();

    // check out 5 more connections from cache FCFCacheGL
    System.out.println("\nNow we can retrieve connections from "
       + "cache FCFCacheGL again.");
    getConnections(dsgl, conns1, 5);
    verifyCache();

    cleanUp(conns1, conns2, 20, dsgl, dscrm);
  } // end of oneServieDownOneServiceUp() 


  // Please replace localhost/port/service_name with yours
  private void getURLs() throws Exception
  {
    String url = System.getProperty("JDBC_URL");
    if ((url != null) && (url.indexOf("thin") != -1))
    {
      urlgl = "jdbc:oracle:thin:@(DESCRIPTION=" +
            "(FAILOVER=on)" +
            "(ADDRESS=(PROTOCOL=tcp)(HOST=localhost)(PORT=1251))" +
            "(CONNECT_DATA=(SERVICE_NAME=gl.oracle.com)))";
      urlcrm = "jdbc:oracle:thin:@(DESCRIPTION=" +
             "(FAILOVER=on)" +
             "(ADDRESS=(PROTOCOL=tcp)(HOST=localhost)(PORT=1255))" +
             "(CONNECT_DATA=(SERVICE_NAME=crm.oracle.com)))";
    }
    else
    { 
      urlgl = "jdbc:oracle:oci8:@(DESCRIPTION=" +
            "(FAILOVER=on)" +
            "(ADDRESS=(PROTOCOL=tcp)(HOST=localhost)(PORT=1251))" +
            "(CONNECT_DATA=(SERVICE_NAME=gl.oracle.com)))";
      urlcrm = "jdbc:oracle:oci8:@(DESCRIPTION=" +
             "(FAILOVER=on)" +
             "(ADDRESS=(PROTOCOL=tcp)(HOST=localhost)(PORT=1255))" +
             "(CONNECT_DATA=(SERVICE_NAME=crm.oracle.com)))";
    }
  } // end of getURLs()


  private OracleDataSource createDataSource(String cacheName,
                                            String url)
  throws Exception
  {
    OracleDataSource ods = new OracleDataSource();
    ods.setURL(url);
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setConnectionCachingEnabled(true); // be sure set to true
    ods.setFastConnectionFailoverEnabled(true); // be sure set to true
    ods.setConnectionCacheName(cacheName);

    java.util.Properties prop = new java.util.Properties();
    prop.setProperty("MinLimit", "5");
    prop.setProperty("InitialLimit", "5");
    ods.setConnectionCacheProperties(prop);

    // create an initial connection to trigger
    // the cache creation
    Connection conn = ods.getConnection();
    conn.close();
    conn = null;

    return ods;
  } // end of createDataSource()

  
  private void cleanUp(Connection[]     conns1,
                       Connection[]     conns2,
                       int              num,
                       OracleDataSource ods1,
                       OracleDataSource ods2)
  throws Exception
  {
    int i = 0;
    for (i= 0; i < num; ++i)
    {
      if ( conns1[i] != null )
      {
         conns1[i].close();
         conns1[i] = null;
      }
      if ( conns2[i] != null )
      {
         conns2[i].close();
         conns2[i] = null;
      }
    }
    ods1.close();
    ods1 = null;
    ods2.close();
    ods2 = null;
  } // end of cleanUp()


  private void printFailoverSettings(OracleDataSource dsgl,
                                     OracleDataSource dscrm)
  throws Exception
  {
    System.out.println("DataSource for service gl " +
      "ConnectionFailoverEnabled: " +
      dsgl.getFastConnectionFailoverEnabled());
    System.out.println("DataSource for service crm " +
      "ConnectionFailoverEnabled: " +
      dscrm.getFastConnectionFailoverEnabled());
  }

  
  private void getConnections(OracleDataSource ds,
                              Connection [] conns,
                              int num)
  throws Exception
  {
    for (int i = 0; i < num; ++i)
    {
      conns[i] = ds.getConnection();

      Statement stmt = conns[i].createStatement();
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      System.out.println("conns[" + i + "] from Instacne: " +
        rs.getString(1) + "; Host: " +
        rs.getString(2) + "; Service: " +
        rs.getString(3));
      rs.close();
      stmt.close();
    }
  }


  private void verifyCache() throws Exception
  {
    System.out.println(
      "NumberOfAvaibleConnections in cache FCFCacheGL: " +
      occm.getNumberOfAvailableConnections("FCFCacheGL"));
    System.out.println(
      "NumberOfActiveConnections in cache FCFCacheGL: " +
      occm.getNumberOfActiveConnections("FCFCacheGL"));
    System.out.println(
      "NumberOfAvaibleConnections in cache FCFCacheCRM: " +
      occm.getNumberOfAvailableConnections("FCFCacheCRM"));
    System.out.println(
      "NumberOfActiveConnections in cache FCFCacheCRM: " +
      occm.getNumberOfActiveConnections("FCFCacheCRM"));
  }


  // Please replave instance/database/host with yours
  // create service down event and sent it out
  private void handleDownEvent() throws Exception
  {
    System.out.println("\n*** service gl DOWN ***");
    type = "database/event/service";
    body = "VERSION=1.0" +
           " service=gl.oracle.com" +
           " instance=myinstance" +
           " database=myinstance" +
           " host=myhost" +
           " status=DOWN";
    if (sendEvent(type, comp, node, body))
       System.out.println("Service DOWN event is sent out");
  }


  // create service up event and send it out
  private void handleUpEvent() throws Exception
  {
    System.out.println("\n*** the service UP ***");
    type = "database/event/service";
    body = "VERSION=1.0" +
           " service=gl.oracle.com" +
           " instance=myinstance" +
           " database=myinstance" +
           " host=myhost" +
           " status=up";
    if (sendEvent(type, comp, node, body))
       System.out.println("Service UP event is sent out");

  }


  private boolean sendEvent(String type,
                            String comp,
                            String node,
                            String body)
  throws Exception
  {
     try
     {
       System.out.println("Send out Event: type=" + type +
          " comp=" + comp + " node=" + node + " body=" + body);

       // create a Publisher object for pulishing notifiactions
       oracle.ons.Publisher p = new oracle.ons.Publisher("generator");

       // create the notification body
       oracle.ons.Notification anotification = new oracle.ons.Notification(
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
}
