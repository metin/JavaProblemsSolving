import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.Context;
import javax.naming.InitialContext;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;

/**
 *  Oracle connection cache meets the JDBC 3.0 Connection Cache
 *  requirements. Oracle connection cache supports
 *    transparent access to JDBC connection cache
 *    authentication with multiple users
 *    additional cache attributes other than user/password
 *
 *  This sample mainly demos following connection attributes:
 *  MinLimit, MaxLimit, InitialLimit, PropertyCheckInterval,
 *  TimeToLiveTimeout, Inactivitytimeout, MaxStatementsLimit,
 *  and AbandonedConnectionTimeout.
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
 *    ValidateConnection     default is set to false
 *    ClosestConnectionMatch default is set to false ImplicitCache04
 *    ConnectionWaitTimeout  defaults to 0           ImplicitCache04
 *    Inactivitytimeout      defaults to -1          ImplicitCache03
 *    TimeToLiveTimeout      defaults to -1          ImplicitCache03
 *    AbandonedConnectionTimeout defaults to -1      ImplicitCache03
 *
 *  Inactivitytimeout
 *  default to -1, means this feature is not in effect.
 *  It sets the maximum time a PooledConnection can remain idle
 *  in a connection cache. An idle connection is one that is
 *  not active and does not have a logical handle associated with
 *  it. When this timout expires, the underlying pooledConnection
 *  is closed. 
 *
 *  TimeToLiveTimeout
 *  default to -1, means this feature is not enabled
 *  It sets the maximum time slice, in seconds, a logical connection
 *  is given. When this tiemout expires, the logical connection is
 *  unconditionally closed, the relevant statement handles canceled,
 *  and the underlying PooledConnection is returned to the cache for
 *  reuse.
 * 
 *  AbandonedConnectionTimeout
 *  default to -1, means this feature is not in effect.
 *  This is similar to Inactivitytimeout, but on a logical connection.
 *  When set, JDBC monitors SQL database activity on this logical
 *  connection. If a connection has been inactive for the specified
 *  amount of time, the underlying PooledConnection is reclaimed
 *  and returned to the cache for reuse.
 *
 *  MinLimit
 *  The default is 0
 *  This sets the minimum number of PooledConnections the cache
 *  cache maintains. This gurantees the cache won't shrink below
 *  this limit. note: this property does not initialize the cache 
 *  with minium number of connections.
 *
 *  InitialLimit
 *  The default is 0
 *  This sets the size of the connection cache when the cache is
 *  initially created or reinitialized.
 *
 *  Connection cache using OracleConnectionCacheImpl is deprecated.
 *
 *  note: 1. JNDI sub directory must exists in this directory
 *        2. Please use jdk1.3.1 or later version
 *        3. ImplicitCache01 ~ ImplicitCache05 demonstrate implicit connection cache features
 */


class ImplicitCache03 extends Thread
{
  Context ctx = null;

  public static void main (String args [])
       throws Exception
  { 
    ImplicitCache03 cc03 = new ImplicitCache03();
    cc03.start();    
  }

  public void run ()
  {
    try
    {
      setods ();
      InactivityTimeout();
      TimeToLiveTimeout();
      AbandonedConnectionTimeout();
      MaxStatementsLimit();
    } catch (Exception e)
    {
       e.printStackTrace();
       System.exit(1);
    }
  }

  private void InactivityTimeout() throws Exception
  {
    Connection conns[] = new OracleConnection[8];
    int i = 0;
   
    // look up the datasource object from the Context
    OracleDataSource ds = (OracleDataSource) ctx.lookup("DS_CACHE03");
    
    // set cache properties
    java.util.Properties prop = new java.util.Properties ();
    prop.setProperty("MinLimit", "2");     // the cache size is 2 at least 
    prop.setProperty("InitialLimit", "4"); // create 4 connections at the cache creation
    prop.setProperty("InactivityTimeout", "10");    // 10 seconds
    prop.setProperty("PropertyCheckInterval", "5"); // 5 seconds
    ds.setConnectionCacheProperties (prop);

    // Call getConnection to create the Cache
    Connection conn = ds.getConnection();
    conn.close();

    System.out.println("\n*** Show Inactivity timeout ***");

    Thread.sleep(2000); // wait 2 second for the warming up
    // check the number of sessions after initialization
    System.out.println("The initial connections in the cache: "
                        + checkNoSessions(ds)); // expect 4 as the InitialLimit

    // retrieve 8 connections, then check the number of
    // sessions.
    for ( i = 0; i < 8; ++i )
    {
       conns[i] = ds.getConnection();
    }
    System.out.println("active session number expect 8, we get " +
                       checkNoSessions(ds)); // 8 is the number of connections

    // close all the connections, then check the number of
    // sessions to see whether it go below the MinLimit
    for (i=0; i<8; ++i )
    {
       conns[i].close();
    }

    System.out.println("Sleep 15 seconds to enable the Inactivity Timeout");
    Thread.sleep(15000);
    System.out.println("active session number at least should be 1, we get " +
                       checkNoSessions(ds)); // 1 = MinLimit - System's session

    for ( i = 0; i < 8; ++i )
    {
       if ( conns[i] != null )
       {
          conns[i].close();
          conns[i] = null;
       }
    } 

    ds.close();// close the DataSource and clean the cache
  } // end of InactivityTimeout()

  private void TimeToLiveTimeout () throws Exception
  {
    Connection conns[] = new Connection[15];
    Statement stmts[] = new Statement[15];
    int  i = 0;

    // look up the datasource object
    OracleDataSource ds = (OracleDataSource)ctx.lookup("DS_CACHE03");    
    
    // set cache properties
    java.util.Properties prop = new java.util.Properties();
    prop.setProperty("TimeToLiveTimeout", "10");     // 10 seconds
    prop.setProperty("PropertyCheckInterval", "5");  // 5 seconds
    ds.setConnectionCacheProperties(prop);

    System.out.println("\n*** Show TimeToLive timeout ***");

    // create 15 connections and 15 statements
    System.out.println("Get 15 connections and statements from the cache");
    for ( i = 0; i < 15; ++i )
    {
       conns[i] = ds.getConnection();
       if ( conns[i] == null )
          System.err.println("conns[" + i + "] is bad");
       else
          stmts[i] = conns[i].createStatement();
    }

    // sleep 15 seconds, which is longer than TimeToLive 
    // property 10 seconds, to enable the TimeToLive timeout
    System.out.println("Sleep 15 seconds to enable the TimeToLive timeout");
    Thread.sleep (15000); 

    // check all the statements and connections. they should be claimed
    for ( i = 0; i < 15; ++i )
    {
       // check whether all those 15 stmts get canceled out
       try
       {
          stmts[i].executeQuery("select USER from DUAL");
          System.err.println("statement " + i + " still alive");
       } catch (Exception e)
       {
          System.out.print("Statement " + i + " is successfully canceled. ");
       }
    
       // check whether all those 15 conns get canceled out
       try 
       {
          stmts[i] = conns[i].createStatement();
          System.err.println("connection " + i + "is still alive");
       } catch (Exception e)
       {
          System.out.println("connection " + i + " is successfully canceled");
       }
    }
    ds.close(); // close the datasource and clean up the cache 
  } // end of TimeToLiveTimeout()

  private void AbandonedConnectionTimeout() throws Exception
  {
    Connection conns[] = new Connection[6];
    Statement  stmts[] = new Statement[6];
    int        i       = 0;
    
    Timer tm=new Timer(true);
    
    OracleConnectionCacheManager conCacheMgr = 
      OracleConnectionCacheManager.getConnectionCacheManagerInstance();

    // look up for the DataSource object
    OracleDataSource ds = (OracleDataSource) ctx.lookup("DS_CACHE03");    
    
    // set cache properties
    java.util.Properties prop = new java.util.Properties();
    //create 6 connections at the cache creation
    prop.setProperty("InitialLimit", "6"); 
    prop.setProperty("AbandonedConnectionTimeout", "10");  // 10 seconds
    prop.setProperty("PropertyCheckInterval", "5");        //  5 seconds
    ds.setConnectionCacheProperties(prop);    
    
    System.out.println("\n*** Show AbandonedConnectionTimeout ***");

    // create 6 connections and 6 statements 
    System.out.println("Get 6 connections and statements from the cache\n");
    
    for ( i = 0; i < 6; ++i )
    {
       conns[i] = ds.getConnection();
       if ( conns[i] == null )
          System.err.println("conns[" + i + "] is null");
       else
          stmts[i] = conns[i].createStatement();
    }
    
    System.out.println("\nNumber of Available Connections in the pool " +
    "before AbandonedConnectionTimeout " + 
    conCacheMgr.getNumberOfAvailableConnections("ImplicitCache03"));
                       
    System.out.println("\nNumber of Borrowed Connections from the the pool" +
    " before AbandonedConnectionTimeout " +
    conCacheMgr.getNumberOfActiveConnections("ImplicitCache03"));

    // Keep the first 3 connections active and let others inactive
    // and sleep 15 seconds to enable the AbandonedConnection timeout
    System.out.println("\nLet conns[0] ~ conns[2] be active, " +
                       "and others be inactive");
    for ( i = 0; i < 6; ++i )
    {
      if(i < 3)
       {          
          System.out.println("\nDoing some work on Connection "+ i);           
          tm.schedule(new 
              TestAbandonedConnectionTimeoutTimer(conns[i]),1000,1000);
       }                
       Thread.sleep(2500); //sleep for 2.5 secs on each thread
    }    
        
    //sleep 15 seconds
    System.out.println("\nSleep 15 seconds to enable the " +
    "AbandonedConnectionTimeout");    
    
    try
    {
      Thread.sleep (15000);
    }
    catch(InterruptedException e)
    {}    
    
    // check which connections are closed, which statements
    // are canceled, and which are still alive
    System.out.println("15 seconds later, conns[0] ~ conns[2] " +
                       "should still there");
    System.out.println("inactive conns[3] ~ conns[5] and their " +
                       "stmts should be canceled");
    
    System.out.println("\nNumber of Available Connections in the pool " +
    "after AbandonedConnectionTimeout " + 
    conCacheMgr.getNumberOfAvailableConnections("ImplicitCache03"));
               
    System.out.println("\nNumber of Borrowed Connections from the the pool" +
    " after AbandonedConnectionTimeout " +
    conCacheMgr.getNumberOfActiveConnections("ImplicitCache03")+"\n");    
    
           
    for ( i = 0; i < 3; ++i )
    {
       try
       {
          stmts[i].executeQuery("select USER from DUAL");
          stmts[i].close();
       } catch (Exception e)
       {
          System.err.print("Statement handle " + i + " is canceled. ");
       }

       try
       {
          stmts[i] = conns[i].createStatement();
          System.out.println("conns[" + i + "] and its statement handle is good");
       } catch (Exception e)
       {
          System.err.println("conns[" + i + "] is canceled");
       }
    }

    for ( i = 3; i < 6; ++i )
    {
       try
       {
          stmts[i].executeQuery("select USER from DUAL");
          System.err.println("Statement handle " + i + " is not canceled");
       } catch (Exception e)
       {
          System.out.print("statement handle " + i + " is successfully canceled. ");
       }

       try
       {
          stmts[i] = conns[i].createStatement();
          System.err.println("conns[" + i + "] is not canceled");
       } catch (Exception e)
       { 
          System.out.println("conns[" + i + "] is successfully canceled");
       }
    }

    for ( i = 0; i < 6; ++i )
    {
       if ( conns[i] != null )
       {
          conns[i].close();
          conns[i] = null;
       }
    }
    ds.close(); // close the DataSource and clean up the cache
    tm.cancel();    
  } // end of AbandonedConnectionTimeout()
  
  private void MaxStatementsLimit () throws Exception
  {
    Connection conns[] = new Connection[4];
    Statement  stmts[] = new Statement[8];
    int  i = 0;

    OracleDataSource ds = (OracleDataSource)ctx.lookup("DS_CACHE03");    

    // set the Datasource with maxium cache size as 4,
    // maximum statement size per connection as 1
    java.util.Properties cacheProp = new java.util.Properties();
    cacheProp.setProperty("MaxLimit", "4");
    cacheProp.setProperty("MaxStatementsLimit", "1");
    ds.setConnectionCacheProperties(cacheProp);

    System.out.println("\n*** demo MaxStatementsLimit ***");
    for ( i = 0; i < 4; ++i )
    {
       conns[i] = ds.getConnection();
       if ( conns[i] == null )
          System.err.println("conns[" + i + "] is bad");
       else
       {
          System.out.print("conns[" + i +"].getStatementCacheSize(): "
             + ((OracleConnection)conns[i]).getStatementCacheSize());
          stmts[2*i] = conns[i].prepareStatement("select user from dual");
          ((PreparedStatement)stmts[2*i]).execute();
          stmts[2*i].close();

          stmts[2*i+1] = conns[i].prepareStatement("select user from dual");
          ((PreparedStatement)stmts[2*i+1]).execute();

          if ( stmts[2*i+1].equals(stmts[2*i]) )
             System.out.println(". We get the cached statement for additional request");
          else
             System.err.print("we get a different statement. ");
       }
    } // end of for loop

    for ( i = 0; i < 4; ++i )
    {
       if ( conns[i] != null )
       {
          conns[i].close();
          conns[i] = null;
       }
    }
    ds.close();
    ds = null;

  } // end of MaxStatementsLimit()


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
    ods.setConnectionCacheName("ImplicitCache03");

    // set DataSource object to JNDI
    try {
       ctx.bind("DS_CACHE03", ods);
    } catch (Exception e) { }
  } // end of setods()

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

class TestAbandonedConnectionTimeoutTimer extends TimerTask
{
  Connection conn=null;
  
  public TestAbandonedConnectionTimeoutTimer(Connection con)   
  {
    conn=con;
  }
  public void run()
  {
    try
    {
      conn.createStatement().execute("select * from dual");      
    }
    catch (Exception ucpException)
    {
      
    }
  }
}
