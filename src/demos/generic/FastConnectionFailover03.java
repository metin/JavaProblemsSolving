/**
 *  Oracle supports Fast Connection Failover on a Cache
 *  enabled OracleDataSource. It deals with following
 *  events: 
 *    Service down
 *    Service up
 *    node down
 *
 *  This demos the fast connection failover feature
 *  in the case of node down.
 *
 *  In case of nodedown, the corresponding connections
 *  are closed by JDBC.
 *  In case of node up, there will be more than one
 *  events generated, nodeup event and service up events
 *
 *  note: 1. rm ./JNDI/.bindings in advance
 *        2. use jdk1.3.1 or later version
 *        3. ONS must be started in advance. refer
 *           to JDBC doc for ONS setup and configuration.
 *        4. replace host/port/service_name/database
 *           with your settings.
 */

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.naming.*;
import javax.naming.spi.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;

class FastConnectionFailover03 extends Thread
{
  Context ctx = null;
  OracleConnectionCacheManager occm = null;
  String query = "select sys_context('userenv', 'instance_name')," +
                       " sys_context('userenv', 'server_host')," +
                       " sys_context('userenv', 'service_name')" +
                       " from dual";
  // Please replace following with your settings
  String host = "myhost";
  String host2 = host;
  String port = "1521";
  String port2 = "1555";
  String db1 = "mydb1";
  String db2 = "mydb2";
  String instance = "";
  String service1 = "";
  String service2 = "";
  String url1 = null; // corresponds to crm service
  String url2 = null; // corresponds to gl service

  // the properties of event body
  String type="database/event/node/";
  String type2="database/event/service/";
  String comp="";
  String node="";
  String body="";
  String body1="";
  String body2="";
 
  public static void main (String args [])
       throws SQLException
  { 
    FastConnectionFailover03 fcf03 = new FastConnectionFailover03();
    fcf03.start();
  }

  public void run ()
  {
    try
    { 
      getURLs();
      setods (url1, url2);
      nodeDownEvent();
    } catch (Exception e)
    {
       e.printStackTrace();
       System.exit(1);
    }
  }

  private void getConnections(OracleDataSource ds1,
                              OracleDataSource ds2,
                              Connection []    conns,
                              int              num)
  throws Exception
  {
    for (int i = 0; i < num; ++i)
    {
      if ( (i%2) == 0 )
         conns[i] = ds1.getConnection();
      else
         conns[i] = ds2.getConnection();
      Statement stmt = conns[i].createStatement();
      ResultSet rs = stmt.executeQuery(query);
      rs.next();
      instance = rs.getString(1);
      if ( ((i%2) == 0) && ( (service1 == null) || (service1 == "")) ) 
         service1 = rs.getString(3);
      else if ( ((i%2) == 1) && ( (service2 == null) || (service2 == "")) )
         service2 = rs.getString(3);

      System.out.println("conns[" + i +
        "] from Instacne: " + rs.getString(1) +
        "; Host: " + rs.getString(2) +
        "; Service: " + rs.getString(3));
    }
  }

  private void nodeDownEvent() throws Exception
  {
    OracleConnection conns[] = new OracleConnection[10];
    OracleConnection conns2[] = new OracleConnection[10];

    // lookup for a DataSource
    OracleDataSource ds1 = (OracleDataSource)ctx.lookup("DS_FCF03A");
    OracleDataSource ds2 = (OracleDataSource)ctx.lookup("DS_FCF03B");
    printFailoverSettings(ds1, ds2);

    // set the cache properties
    java.util.Properties prop = new java.util.Properties();
    prop.setProperty("MaxLimit", "30");
    prop.setProperty("InitialLimit", "3");
    occm.createCache("FCFCache03A", ds1, prop);
    occm.createCache("FCFCache03B", ds2, prop);

    // retrieve 10 connections from the DataSource
    System.out.println("\nGet 10 connections from both caches");
    getConnections(ds1, ds2, conns, 10);
    verifyCache();

    handleEventDown();
    verifyCache();

    handleEventUp();
    verifyCache();

    // check out 10 connections from the cache
    getConnections(ds1, ds2, conns2, 10);
    verifyCache();

    cleanUp(conns, conns2, 10, ds1, ds2);
  } // end of nodeDownEvent()


  private void printFailoverSettings(OracleDataSource ods1,
                                     OracleDataSource ods2)
  throws Exception
  {
    System.out.println(
      "on DataSource1, ConnectionFailoverEnabled: " +
      ods1.getFastConnectionFailoverEnabled());
    System.out.println(
      "on DataSource2, ConnectionFailoverEnabled: " +
      ods2.getFastConnectionFailoverEnabled());
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


  private void verifyCache() throws Exception
  {
    System.out.println("\nIn Cache FCFCache03A:");
    System.out.println("NumberOfAvaibleConnections: " +
      occm.getNumberOfAvailableConnections("FCFCache03A"));
    System.out.println("NumberOfActiveConnections: " +
      occm.getNumberOfActiveConnections("FCFCache03A"));
    System.out.println("In Cache FCFCache03B:");
    System.out.println("NumberOfAvaibleConnections: " +
      occm.getNumberOfAvailableConnections("FCFCache03B"));
    System.out.println("NumberOfActiveConnections: " +
      occm.getNumberOfActiveConnections("FCFCache03B"));
  }


  // simulate a node down event
  private void handleEventDown() throws Exception
  {
    System.out.println("\n*** the node DOWN ***");
    type = "database/event/host";
    body = "VERSION=1.0" +
           " host=" + host +
           " status=nodedown";
    if (sendEvent(type, comp, node, body))
       System.out.println("node DOWN event is sent out");
  }


  // simulate node up event, service up event 
  private void handleEventUp() throws Exception
  {
    System.out.println("\n*** the node UP ***");
    body = "VERSION=1.0" +
           " host=" + host +
           " status=nodeup";
    if (sendEvent(type, comp, node, body))
       System.out.println("node UP event is sent out");

    // Along with node up event, there will be corresponding
    // service up events sent out
    body1 = "VERSION=1.0" +
            " service=" + service1 +
            " instance=" + instance +
            " database=" + db1 +
            " host=" + host +
            " status=up";
    body2 = "VERSION=1.0" +
            " service=" + service2 +
            " instance=" + instance +
            " database=" + db1 +
            " host=" + host +
            " status=up";

    type2 = "database/event/service";

    if (sendEvent(type2, comp, node, body1))
    {
       System.out.println("service " + service1 +
         " UP event is sent");
    }
    if (sendEvent(type2, comp, node, body2))
    {
       System.out.println("service " + service2 +
         " UP event is sent");
    }
  } // end of handleEventUp()


  private void getURLs() throws Exception
  {
    String url = System.getProperty("JDBC_URL");
    if ((url != null) && (url.indexOf("thin") != -1))
    {
      url1 = "jdbc:oracle:thin:@(DESCRIPTION=" +
      "(LOAD_BALANCE=on)" +
      "(FAILOVER=on)" +
      "(ADDRESS=(PROTOCOL=tcp)(HOST=" + host + ")(PORT=" + port + "))" +
      "(ADDRESS=(PROTOCOL=tcp)(HOST=" + host2 + ")(PORT=" + port2 + "))" +
      "(CONNECT_DATA=(SERVICE_NAME=crm.oracle.com)))";

      url2 = "jdbc:oracle:thin:@(DESCRIPTION=" +
      "(LOAD_BALANCE=on)" +
      "(FAILOVER=on)" +
      "(ADDRESS=(PROTOCOL=tcp)(HOST=" + host + ")(PORT=" + port + "))" +
      "(ADDRESS=(PROTOCOL=tcp)(HOST=" + host2 + ")(PORT=" + port2 + "))" +
      "(CONNECT_DATA=(SERVICE_NAME=gl.oracle.com)))";
    }
    else
    {
      url1 = "jdbc:oracle:oci8:@(DESCRIPTION=" +
      "(LOAD_BALANCE=on)" +
      "(FAILOVER=on)" +
      "(ADDRESS=(PROTOCOL=tcp)(HOST=" + host + ")(PORT=" + port + "))" +
      "(ADDRESS=(PROTOCOL=tcp)(HOST=" + host2 + ")(PORT=" + port2 + "))" +
      "(CONNECT_DATA=(SERVICE_NAME=crm.oracle.com)))";

      url2 = "jdbc:oracle:oci8:@(DESCRIPTION=" +
      "(LOAD_BALANCE=on)" +
      "(FAILOVER=on)" +
      "(ADDRESS=(PROTOCOL=tcp)(HOST=" + host + ")(PORT=" + port + "))" +
      "(ADDRESS=(PROTOCOL=tcp)(HOST=" + host2 + ")(PORT=" + port2 + "))" +
      "(CONNECT_DATA=(SERVICE_NAME=gl.oracle.com)))";
    }
  }

 
  private void setods(String url1, String url2)
  throws Exception
  {
    // create a context for holding name-to-object bindings
    Hashtable env = new Hashtable (5);
    env.put (Context.INITIAL_CONTEXT_FACTORY,
           "com.sun.jndi.fscontext.RefFSContextFactory");
    env.put (Context.PROVIDER_URL, "file:./JNDI" );
    ctx = new InitialContext (env);

    // create a cache manager
    occm =
     OracleConnectionCacheManager.getConnectionCacheManagerInstance();

    // create two DataSources
    OracleDataSource ods1 = new OracleDataSource(); // for service crm
    OracleDataSource ods2 = new OracleDataSource(); // for service gl

    // set DataSource properties
    ods1.setURL(url1);
    ods1.setUser("hr");
    ods1.setPassword("hr");
    ods1.setConnectionCachingEnabled(true); // be sure set to true
    ods1.setFastConnectionFailoverEnabled(true); // be sure set to true

    ods2.setURL(url2);
    ods2.setUser("hr");
    ods2.setPassword("hr");
    ods2.setConnectionCachingEnabled(true); // be sure set to true
    ods2.setFastConnectionFailoverEnabled(true); // be sure set to true

    // bind the DataSource objects to JNDI
    try {
       ctx.bind("DS_FCF03A", ods1);
       ctx.bind("DS_FCF03B", ods2);
    } catch (Exception e) { }
    ods1.close();
    ods2.close();
  } // end of setods()


  private void cleanUp(Connection[]     conns1,
                       Connection[]     conns2,
                       int              num,
                       OracleDataSource ods1,
                       OracleDataSource ods2)
  throws Exception
  {
    int i = 0;
    for (i = 0; i < num; ++i)
    {
      if ( conns1[i] != null )
      {
         conns1[i].close();
         conns1[i] = null;
      }
      if (  conns2[i] != null )
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
}
