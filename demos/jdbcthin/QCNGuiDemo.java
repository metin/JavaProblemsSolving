/* Copyright (c) 2006, Oracle. All rights reserved.  */

/*
  This class is a demonstration of performance of Database Change Notification (DCN),
  a new feature of the JDBC thin driver in 11gR1. It starts from the simpler
  BasicQCNDemo.java file and includes code adapted from the Slider demo in the 
  SwingSet demo.
  
  The program does the following:
  1. Create a GUI.
  2. Connect to the database, create a new empty DCN registration and register
     an event listener..
  3. Associate a "select" query to this registration.
  4. Close the connection.
  5. Open a new connection, when the slider is moved and some DML operation
     is performed which will affect the registered query.
  6  The registered event listener looks in the event for what happened and
     executes a query to get the updated data.
  7. The window close event cleans up.
 
  Please note that multiple copies of this program that connect to the same 
  database will each be updated whenever any of the upper sliders are moved.
 
  It is a bug that the inital positions of the sliders are not obtained from
  the database.
  
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

  Authors: Ed Shirk (edward.shirk@oracle.com) and Jean de Lavarene (jean.de.lavarene@oracle.com)
  Creation date: September 29th 2006
 */

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.dcn.*;


public class QCNGuiDemo {

  JPanel mainPanel = null;
  SliderListenerThread sliderThread;
  JProgressBar progressBarToUpdate = null;
  public static Dimension VGAP5 = new Dimension(1,5);
  public static Dimension VGAP10 = new Dimension(1,10);
  private int PREFERRED_WIDTH = 680;
  private int PREFERRED_HEIGHT = 200;
  static final String USERNAME= "scott";
  static final String PASSWORD= "tiger";

  static String URL = null;
  OracleConnection getConn = null;
  PreparedStatement getPstmt = null;
  DatabaseChangeRegistration dcr = null;

  public static void main(String[] argv)
  {
    if(argv.length < 1)
    {
      System.out.println("Error: You need to provide the URL in the first argument.");
      System.out.println("  For example: > java -classpath .:ojdbc5.jar QCNGuiDemo \"jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=myhost.mydomain.com)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=myoracleservicename)))\"");

      System.exit(1);
    }
    URL = argv[0];
    QCNGuiDemo demo = new QCNGuiDemo();
    demo.constructGUIObjects();
    demo.registerChangeNotification();
  }
  
  public void constructGUIObjects() {
    UIManager.put("swing.boldMetal", Boolean.FALSE);
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    
    JLabel tf = new JLabel("value");
    mainPanel.add(tf, BorderLayout.SOUTH);
  
    JPanel tp = new JPanel();
    tp.setLayout(new BoxLayout(tp, BoxLayout.Y_AXIS));
    mainPanel.add(tp, BorderLayout.CENTER);
    
    // Horizontal Slider 1
    JPanel horizontalPanel1 = new JPanel();
    {
      horizontalPanel1.setLayout(new BoxLayout(horizontalPanel1, BoxLayout.Y_AXIS));
      horizontalPanel1.setBorder(new TitledBorder("sender"));

      JSlider s = new JSlider(-10, 100, 20);
      s.getAccessibleContext().setAccessibleName("sender");
      s.getAccessibleContext().setAccessibleDescription("sender");
      sliderThread = new SliderListenerThread(tf);
      sliderThread.start();
      ChangeListener listener = new SliderListener(sliderThread);
      s.addChangeListener(listener);
    
      horizontalPanel1.add(Box.createRigidArea(VGAP5));
      horizontalPanel1.add(s);
      horizontalPanel1.add(Box.createRigidArea(VGAP5));
    }
    tp.add(horizontalPanel1);
    tp.add(Box.createRigidArea(VGAP10));

    // Horizontal Slider 2
    JPanel horizontalPanel2 = new JPanel();
    {
      horizontalPanel2.setLayout(new BoxLayout(horizontalPanel2, BoxLayout.Y_AXIS));
      horizontalPanel2.setBorder(new TitledBorder("receiver"));
      progressBarToUpdate = new JProgressBar(JProgressBar.HORIZONTAL,-10,100);
      
      progressBarToUpdate.setStringPainted(true);
      progressBarToUpdate.setString("");
      progressBarToUpdate.setValue(20);
      progressBarToUpdate.getAccessibleContext().setAccessibleName("receiver");
      progressBarToUpdate.getAccessibleContext().setAccessibleDescription("receiver");

      horizontalPanel2.add(Box.createRigidArea(VGAP5));
      horizontalPanel2.add(progressBarToUpdate);
      horizontalPanel2.add(Box.createRigidArea(VGAP5));
    }
    tp.add(horizontalPanel2);
    tp.add(Box.createRigidArea(VGAP10));

    GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    JFrame frame = new JFrame("Query Change Notification Demo",gc);
    Rectangle bounds = gc.getBounds();
    frame.setLocation(15 + bounds.x, 15 + bounds.y); 

    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          cleanup();
        }
      });
    mainPanel.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Utility method: creates a new JDBC connection to the database.
   */
  static OracleConnection connect()
  {
    OracleConnection conn = null;
    try{
      OracleDriver dr = new OracleDriver();
      Properties prop = new Properties();
      prop.setProperty("user",QCNGuiDemo.USERNAME);
      prop.setProperty("password",QCNGuiDemo.PASSWORD);
      conn = (OracleConnection)dr.connect(QCNGuiDemo.URL,prop);
      conn.setAutoCommit(false);
    } catch( SQLException ex ){ex.printStackTrace(); }
    return conn;
  }

  /**
   * Creates a new registration in the database, associates the SQL query 
   * "select sal from emp where empno=7369" and add a listener.
   */
  void registerChangeNotification()
  {
    OracleConnection conn = null;
    conn = connect();
    if( conn == null ) return;
    Properties prop = new Properties();
    prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS,"true");
    prop.setProperty(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION,"true");
    
    try
    {
      dcr = conn.registerDatabaseChangeNotification(prop);
      QCNGuiDemoDatabaseChangeListener list = new QCNGuiDemoDatabaseChangeListener(this);
      dcr.addListener(list);
      Statement stmt = conn.createStatement();
      String query = "select sal from emp where empno=7369";
      ((OracleStatement)stmt).setDatabaseChangeRegistration(dcr);
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next())
      {}
      rs.close();
      stmt.close();
    }
    catch(SQLException ex)
    {
      // if an exception occurs, we need to close the registration in order
      // to interrupt the thread otherwise it will be hanging around.
      if(conn != null && dcr != null)
      {
        try{ conn.unregisterDatabaseChangeNotification(dcr);}
        catch(SQLException ex2){ex2.printStackTrace();}
      }
      ex.printStackTrace();
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
  }

  /**
   * Connects to the database and closes the registration.
   */
  void unregisterChangeNotification()
  {
    OracleConnection conn = null;
    try { 
      conn = connect();
      conn.unregisterDatabaseChangeNotification(dcr);
    }catch( Throwable t ) { t.printStackTrace();}
    finally{try{ if( conn != null ) conn.close();}catch(SQLException e){} }

  }

  /**
   * Retrieves the salary given the rowid and updates the progress bar.
   */
  void getUpdateForROWID( oracle.sql.ROWID rowid )
  {
    int newValue = Integer.MIN_VALUE;
    if( getConn == null ) getConn = connect();
    String sql = "select sal from emp  where rowid = ?";
    try{
      if( getPstmt == null ) getPstmt = getConn.prepareStatement( sql );
      ((OraclePreparedStatement)getPstmt).setROWID( 1, rowid );
      ResultSet rst = getPstmt.executeQuery();
      if( rst.next() )
        newValue = rst.getInt(1);
      rst.close();
    } catch( SQLException ex ) { ex.printStackTrace(); }

    if( (progressBarToUpdate != null) &&  (newValue != Integer.MIN_VALUE) )
      progressBarToUpdate.setValue( newValue );
  }
  
  void cleanup()
  {
    if (dcr != null) unregisterChangeNotification();
    if( getPstmt != null){ try{ getPstmt.close();}catch(SQLException ex){}}
    if( getConn != null){ try{ getConn.close();}catch(SQLException ex){}}
    sliderThread.interrupt();
    System.exit(0);
  }


  class SliderListener implements ChangeListener {
    SliderListenerThread thread;
    public SliderListener(SliderListenerThread threa) {
      thread = threa;
    }
    public void stateChanged(ChangeEvent e) {
      JSlider s1 = (JSlider)e.getSource();
      thread.addTask(s1.getValue());
    }
  }
}

/**
 * The onDatabaseChangeNotification method of this listener will be called
 * by the JDBC driver whenever a new database change event occurs. In this
 * demo, we get the rowid from the event and update the progress bar.
 */
class QCNGuiDemoDatabaseChangeListener implements DatabaseChangeListener
{
  QCNGuiDemo demo = null;
  QCNGuiDemoDatabaseChangeListener( QCNGuiDemo demo )
  {
    super();
    this.demo = demo;
  }

  public void onDatabaseChangeNotification(DatabaseChangeEvent e)
  {
    QueryChangeDescription [] changes = e.getQueryChangeDescription();
    QueryChangeDescription change = changes[0];
    TableChangeDescription [] tableChanges = change.getTableChangeDescription();
    TableChangeDescription tableChange = tableChanges[0];
    RowChangeDescription[] rowChanges = tableChange.getRowChangeDescription();
    RowChangeDescription rowChange = rowChanges[0];
    oracle.sql.ROWID rowid = rowChange.getRowid();
    demo.getUpdateForROWID( rowid );
  }
}  

/**
 * This thread is responsible for updating the database when the user moves
 * the slider. It uses a one element queue to avoid any latency.
 */
class SliderListenerThread extends Thread
{
  int newValue;
  JLabel tf;
  OracleConnection upConn = null;
  PreparedStatement upPstmt = null;

  SliderListenerThread(JLabel f)
  {
    tf = f;
    newValue = -1;
  }
  void addTask(int value)
  {
    if(newValue != value)
    {
      newValue = value;
      synchronized(this)
      { this.notify(); }
    }
  }
  public void run()
  {
    int myNewValue = -1;
    int myLastValue = -1;
    try
    {
      while(true)
      {
        myNewValue = newValue; // atomic assignment is thread safe
        if(myLastValue == myNewValue)
          synchronized(this)
          { this.wait(); }

        myNewValue = newValue; // atomic assignment is thread safe
        updateEmpTable(myNewValue);
        tf.setText("value: " + myNewValue);
        myLastValue = myNewValue;
      }
    }
    catch(InterruptedException iex)
    {
      if( upPstmt != null){ try{ upPstmt.close();}catch(SQLException ex){}}
      if( upConn != null){ try{ upConn.close(); }catch(SQLException ex){}}
    }
  }
  void updateEmpTable( int newValue )
  {
    if( upConn == null ) 
    {
      upConn = QCNGuiDemo.connect();
      try
      {
        upConn.setAutoCommit(true);
      }catch(SQLException ex)
      { ex.printStackTrace(); }
    }
    String sql = "update emp set sal =? where empno = 7369";
    try{
      if( upPstmt == null ) upPstmt = upConn.prepareStatement( sql );
      upPstmt.setInt( 1, newValue );
      upPstmt.executeUpdate();
    } catch( SQLException ex ) { ex.printStackTrace(); }
  }
}
