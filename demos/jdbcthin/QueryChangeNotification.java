/* Copyright (c) 2007, Oracle. All rights reserved.  */

/*
  This class is a demonstration of Database Change Notification (DCN), a new feature
  of the JDBC thin driver in 11gR1. It specifically shows how the "query registration"
  or Query Change Notification (QCN) works as opposed to the "table registration"
  (see DBChangeNotification.java). Note that QCN is a subset of DCN.
  
  The program does the following:
  1. Connect to the database, create a new empty DCN registration and register
     an event listener that simply prints out the event details.
  2. Associate a "select" query to this registration.
  3. Close the connection.
  4. Open a new connection, execute some DML operations on the tables
     that are registered and close the connection.
  5. Wait... The DCN event will be printed... and close the registration.
  
  Table registration as opposed to query registration:
  ====================================================
  This demo requires a 11.1 database because it uses "query registration"
  which means that when you register a select query, what you register is the actual
  result of the query with finer granularity than when you register the tables.
  In this example, we insert two rows and we get only one row in the event because
  the second row doesn't affect the result of the query.
  
  A setup in the db is required: you need to grant the following privilege to scott:
     grant change notification to scott;
     
  You also need to set the COMPATIBLE flag in t_init1.ora and restart the db:
     compatible=11.0.0.0.0
  
  The output of the program is:
  *****************************************************************************
  Register the following query: select deptno,dname from dept where deptno='45'
  SCOTT.DEPT is part of the registration.
  inserted one row (45, cool dept) with ROWID=AAAMAgAABAAAKVcAAS
  inserted one row (50, fun dept) with ROWID=AAAMAgAABAAAKVcAAT
  QCNDemoListener: got an event (QCNDemoListener@1c running on thread Thread[Thread-1,5,main])
  Connection information  : local=clienthost.domain.com/10.10.10.101:47632, remote=serverhost.domain.com/10.12.12.101:17060
  Registration ID         : 11
  Notification version    : 1
  Event type              : QUERYCHANGE
  Database name           : dbjh
  Query Change Description (length=1)
    queryId=7, query change event type=QUERYCHANGE
    Table Change Description (length=1):    operationCode=[INSERT], tableName=SCOTT.DEPT, objectNumber=49184
      Row Change Description (length=1):
        ROW:  operationCode=INSERT, ROWID=AAAMAgAABAAAKVcAAS
  *****************************************************************************
  
  A variant of this demo: leave the registration open instead of closing it
  at then end. The DCN thread continues to run. Then from sqlplus, execute a DML
  query that changes the result of the query and commit. The java program should print
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

public class QueryChangeNotification
{
  static final String USERNAME= "scott";
  static final String PASSWORD= "tiger";
  static String URL = null;
  
  public static void main(String[] argv)
  {
    if(argv.length < 1)
    {
      System.out.println("Error: You need to provide the URL in the first argument.");
      System.out.println("  For example: > java -classpath .:ojdbc5.jar QueryChangeNotification \"jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=yourhost.yourdomain.com)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=yourservicename)))\"");

      System.exit(1);
    }
    URL = argv[0];
    QueryChangeNotification demo = new QueryChangeNotification();
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
    // prop.setProperty(OracleConnection.NTF_LOCAL_HOST,"10.10.10.101");

    // Ask the server to send the ROWIDs as part of the DCN events (small performance
    // cost):
    prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS,"true");
    
    // Activate "query" change notification as opposed to the "table" change notification:
    prop.setProperty(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION,"true");

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
      // add the listener:
      QCNDemoListener list = new QCNDemoListener(this);
      dcr.addListener(list);
       
      // second step: add objects in the registration:
      Statement stmt = conn.createStatement();
      // associate the statement with the registration:
      String query = "select deptno,dname from dept where deptno='45'";
      System.out.println("Register the following query: "+query);
      ((OracleStatement)stmt).setDatabaseChangeRegistration(dcr);
      ResultSet rs = stmt.executeQuery(query);
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
          System.out.println("inserted one row (45, cool dept) with ROWID="+autoGeneratedKey.getString(1));      
        stmt2.executeUpdate("insert into dept (deptno,dname) values ('50','fun dept')",Statement.RETURN_GENERATED_KEYS);
        autoGeneratedKey = stmt2.getGeneratedKeys();
        if(autoGeneratedKey.next())
          System.out.println("inserted one row (50, fun dept) with ROWID="+autoGeneratedKey.getString(1));
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
    prop.setProperty("user",QueryChangeNotification.USERNAME);
    prop.setProperty("password",QueryChangeNotification.PASSWORD);
    return (OracleConnection)dr.connect(QueryChangeNotification.URL,prop);
  }
}
/**
 * Databace change listener: it prints out the event details in stdout.
 */
class QCNDemoListener implements DatabaseChangeListener
{
  QueryChangeNotification demo;
  QCNDemoListener(QueryChangeNotification dem)
  {
    demo = dem;
  }
  public void onDatabaseChangeNotification(DatabaseChangeEvent e)
  {
    Thread t = Thread.currentThread();
    System.out.println("QCNDemoListener: got an event ("+this+" running on thread "+t+")");
    System.out.println(e.toString());
    synchronized( demo ){ demo.notify();}
  }
}
