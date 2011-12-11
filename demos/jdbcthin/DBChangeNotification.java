/* Copyright (c) 2007, Oracle. All rights reserved.  */

/*
  This class is a demonstration of Database Change Notification (DCN), a new feature
  of the JDBC thin driver in 11gR1.
  
  The program does the following:
  1. Connect to the database, create a new empty DCN registration and register
     an event listener that simply prints out the event details.
  2. Associate a "select" query to this registration.
  3. Close the connection.
  4. Open a new connection, execute some DML operations on the tables
     that are registered and close the connection.
  5. Wait... The DCN event will be printed... and close the registration.
  
  Table registration as opposed to query registration 
  ====================================================
  In other words: "Database Change Notification" as opposed to "Query Change Notification".
  This demo will work against a 10.2 database: it uses "table registration"
  which means that when you register a select query, what you register is the name of the tables
  involved not the query itself. In other words you might select one single row of a table
  and if another row is updated you will be notified although the result of your query hasn't
  changed. When using a 11g database, you have the possibility to use a different
  option: the "query registration" with finer granularity (see QueryChangeNotification.java).
  
  A setup in the db is required: you need to grant the following privilege to scott:
     grant change notification to scott;
  
  The output of the program is:
  *****************************************************************************
  SCOTT.DEPT is part of the registration.
  inserted one row with ROWID=AAANAUAABAAALRzAAE
  inserted one row with ROWID=AAANAUAABAAALRzAAF
  DCNDemoListener: got an event (DCNDemoListener@1c running on thread Thread[Thread-1,5,main])
  Connection information  : local=clienthost.domain.com/10.10.10.101:47632, remote=serverhost.domain.com/10.12.12.101:16564
  Registration ID         : 15
  Notification version    : 1
  Event type              : OBJCHANGE
  Database name           : dbj11
  Table Change Description (length=1)
     operation=[INSERT], tableName=SCOTT.DEPT, objectNumber=53268
     Row Change Description (length=2):
      ROW:  operation=INSERT, ROWID=AAANAUAABAAALRzAAE
      ROW:  operation=INSERT, ROWID=AAANAUAABAAALRzAAF
  *****************************************************************************
  
  A variant of this demo: leave the registration open instead of closing it
  at the end. The DCN thread continues to run. Then from sqlplus, execute a DML
  query that changes the SCOTT.DEPT table and commit. The java program should print
  the notification.

  Author: Jean de Lavarene (jean.de.lavarene@oracle.com)
  Creation date: January 4th 2007
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;

public class DBChangeNotification
{
  static final String USERNAME= "scott";
  static final String PASSWORD= "tiger";
  static String URL;
  
  public static void main(String[] argv)
  {
    if(argv.length < 1)
    {
      System.out.println("Error: You need to provide the URL in the first argument.");
      System.out.println("  For example: > java -classpath .:ojdbc5.jar DBChangeNotification \"jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=yourhost.yourdomain.com)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=yourservicename)))\"");

      System.exit(1);
    }
    URL = argv[0];
    DBChangeNotification demo = new DBChangeNotification();
    try
    {
      demo.run();
    }
    catch(SQLException mainSQLException )
    {
      mainSQLException.printStackTrace();
    }
  }

  void run() throws SQLException
  {
    OracleConnection conn = connect();
      
    // first step: create a registration on the server:
    Properties prop = new Properties();
    
    // if connected through the VPN, you need to provide the TCP address of the client.
    // For example:
    // prop.setProperty(OracleConnection.NTF_LOCAL_HOST,"14.14.13.12");

    // Ask the server to send the ROWIDs as part of the DCN events (small performance
    // cost):
    prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS,"true");

    // The following operation does a roundtrip to the database to create a new
    // registration for DCN. It sends the client address (ip address and port) that
    // the server will use to connect to the client and send the notification
    // when necessary. Note that for now the registration is empty (we haven't registered
    // any table). This also opens a new thread in the drivers. This thread will be
    // dedicated to DCN (accept connection to the server and dispatch the events to 
    // the listeners).
    DatabaseChangeRegistration dcr = conn.registerDatabaseChangeNotification(prop);

    try
    {
      // add the listenerr:
      DCNDemoListener list = new DCNDemoListener(this);
      dcr.addListener(list);
       
      // second step: add objects in the registration:
      Statement stmt = conn.createStatement();
      // associate the statement with the registration:
      ((OracleStatement)stmt).setDatabaseChangeRegistration(dcr);
      ResultSet rs = stmt.executeQuery("select * from dept where deptno='45'");
      while (rs.next())
      {}
      String[] tableNames = dcr.getTables();
      for(int i=0;i<tableNames.length;i++)
        System.out.println(tableNames[i]+" is part of the registration.");
      rs.close();
      stmt.close();
    }
    catch(SQLException ex)
    {
      // if an exception occurs, we need to close the registration in order
      // to interrupt the thread otherwise it will be hanging around.
      if(conn != null)
        conn.unregisterDatabaseChangeNotification(dcr);
      throw ex;
    }
    finally
    {
      try
      {
        // Note that we close the connection!
        conn.close();
      }
      catch(Exception innerex){ innerex.printStackTrace(); }
    }
    
    synchronized( this ) 
    {
      // The following code modifies the dept table and commits:
      try
      {
        OracleConnection conn2 = connect();
        conn2.setAutoCommit(false);
        Statement stmt2 = conn2.createStatement();
        stmt2.executeUpdate("insert into dept (deptno,dname) values ('45','cool dept')",Statement.RETURN_GENERATED_KEYS);
        ResultSet autoGeneratedKey = stmt2.getGeneratedKeys();
        if(autoGeneratedKey.next())
          System.out.println("inserted one row with ROWID="+autoGeneratedKey.getString(1));      
        stmt2.executeUpdate("insert into dept (deptno,dname) values ('50','fun dept')",Statement.RETURN_GENERATED_KEYS);
        autoGeneratedKey = stmt2.getGeneratedKeys();
        if(autoGeneratedKey.next())
          System.out.println("inserted one row with ROWID="+autoGeneratedKey.getString(1));
        stmt2.close();
        conn2.commit();
        conn2.close();
      }
      catch(SQLException ex) { ex.printStackTrace(); }

      // wait until we get the event
      try{ this.wait();} catch( InterruptedException ie ) {}
    }
    
    // At the end: close the registration (comment out these 3 lines in order
    // to leave the registration open).
    OracleConnection conn3 = connect();
    conn3.unregisterDatabaseChangeNotification(dcr);
    conn3.close();
  }
  
  /**
   * Creates a connection the database.
   */
  OracleConnection connect() throws SQLException
  {
    OracleDriver dr = new OracleDriver();
    Properties prop = new Properties();
    prop.setProperty("user",DBChangeNotification.USERNAME);
    prop.setProperty("password",DBChangeNotification.PASSWORD);
    return (OracleConnection)dr.connect(DBChangeNotification.URL,prop);
  }
}
/**
 * DCN listener: it prints out the event details in stdout.
 */
class DCNDemoListener implements DatabaseChangeListener
{
  DBChangeNotification demo;
  DCNDemoListener(DBChangeNotification dem)
  {
    demo = dem;
  }
  public void onDatabaseChangeNotification(DatabaseChangeEvent e)
  {
    Thread t = Thread.currentThread();
    System.out.println("DCNDemoListener: got an event ("+this+" running on thread "+t+")");
    System.out.println(e.toString());
    synchronized( demo ){ demo.notify();}
  }
}

