/*
 * This sample is a  multi-threaded JDBC program.
 * The number of threads defaults to 10, it also
 * can be set at commandline. It demonstrates shared
 * connection and non-shared connection two modes.
 * All threads share the same connection in shared mode,
 * create their own connection in non-shared mode, and
 * start running at the same time.
 *
 * Each thread access table hr.employees and print out
 * some of its contents also with thread-id.
 *
 * command line example:
 *         java JdbcMTSample
 *         java JdbcMTSample 20 share 
 *
 * note: jdk1.2 or later version is recommended. 
 */

import java.sql.*;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.pool.OracleDataSource;

public class JdbcMTSample extends Thread
{
  // Default no of threads to 10
  private static int NUM_OF_THREADS = 10;

  int m_myId;

  static     int c_nextId = 1;
  static  Connection s_conn = null;
  static  boolean   share_connection = false;
  static  String url = "jdbc:oracle:oci8:@";

  synchronized static int getNextId()
  {
      return c_nextId++;
  }

  public static void main (String args [])
  {
    try  
    {  
      // If NoOfThreads is specified, then read it
      if ((args.length > 2)  || 
           ((args.length > 1) && !(args[1].equals("share"))))
      {
         System.out.println("Error: Invalid Syntax. ");
         System.out.println("java JdbcMTSample [NoOfThreads] [share]");
         System.exit(0);
      }

      try {
        String url1 = System.getProperty("JDBC_URL");
        if (url1 != null)
          url = url1;
      } catch (Exception e) {
        // If there is any security exception, ignore it
        // and use the default
      }

      OracleDataSource ods = new OracleDataSource();
      ods.setUser("hr");
      ods.setPassword("hr");
      ods.setURL(url);

      if (args.length > 1) 
      {
         share_connection = true;
         System.out.println("All Connections will be sharing the same connection");
      }
  
      // get the no of threads if given
      if (args.length > 0)
         NUM_OF_THREADS = Integer.parseInt (args[0]);
  
      // get a shares connection
      if (share_connection)
          s_conn = ods.getConnection();
  
      // Create the threads
      Thread[] threadList = new Thread[NUM_OF_THREADS];

      // spawn threads
      for (int i = 0; i < NUM_OF_THREADS; i++)
      {
          threadList[i] = new JdbcMTSample();
          threadList[i].start();
      }
    
      // Start everyone at the same time
      setGreenLight ();

      // wait for all threads to end
      for (int i = 0; i < NUM_OF_THREADS; i++)
      {
          threadList[i].join();
      }

      if (share_connection)
      {
          s_conn.close();
          s_conn = null;
      }
          
    }
    catch (Exception e)
    {
       e.printStackTrace();
    }
  
  }  

  public JdbcMTSample()
  {
     super();
     // Assign an Id to the thread
     m_myId = getNextId();
  }

  public void run()
  {
    Connection conn = null;
    ResultSet     rs   = null;
    Statement  stmt = null;

    try
    {    
      // Get the connection

      if (share_connection)
        stmt = s_conn.createStatement (); // Create a Statement
      else
      {
        // Create an OracleDataSource and set properties
        OracleDataSource ods2 = new OracleDataSource();
        ods2.setUser("hr");
        ods2.setPassword("hr");
        ods2.setURL(url);

        // get the connection
        conn = ods2.getConnection();
        stmt = conn.createStatement (); // Create a Statement

        // Closing of DataSource won't affect previously
        // created connection or statement
        ods2.close();
        ods2 = null;
      }

      while (!getGreenLight())
        yield();
          
      // Execute the Query
      rs = stmt.executeQuery ("select * from EMPLOYEES");
          
      // Loop through the results
      while (rs.next())
      {
        System.out.println("Thread " + m_myId + 
                           " Employee Id : " + rs.getInt(1) + 
                           " Name : " + rs.getString(2) + " "
                           + rs.getString(3));
        yield();  // Yield To other threads
      }
          
      // Close all the resources
      rs.close();
      rs = null;
  
      // Close the statement
      stmt.close();
      stmt = null;
  
      // Close the local connection
      if ((!share_connection) && (conn != null))
      {
         conn.close();
         conn = null;
      }
      System.out.println("Thread " + m_myId +  " is finished. ");
    }
    catch (Exception e)
    {
      System.out.println("Thread " + m_myId + " got Exception: " + e);
      e.printStackTrace();
      return;
    }
  }

  static boolean greenLight = false;
  static synchronized void setGreenLight () { greenLight = true; }
  synchronized boolean getGreenLight () { return greenLight; }

}




