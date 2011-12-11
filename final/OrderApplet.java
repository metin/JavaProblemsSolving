/*
 * This sample applet just selects 'Hello World' and the date from the database
 *
 * One way to get this work using appletviewer or a web browser with the Java plugin
 * for jre 5 or jre 6
 *  
 * 1. compile this file using jdk5 and use a jre 5 plugin and ojdbc5.jar
 *    or using jdk 6 and a jre 6 plugin and ojdbc6.jar
 * 2. put ojdbc5.jar or ojdbc6.jar at the same directory as the codebase 
 *    indicated in JdbcApplet.htm file
 * 3. modify JdbcApplet.htm file to let codebase point to the directory
 *    where JdbcApplet.class and ojdbc5.jar or ojdbc6.jar exist.
 *    using syntax of "file:/path_of_JdbcApplet/"
 * 4. use policytool to grant AllPermission of byte-codes at the codebase 
 *    indicated in .htm file to the applet.
 *    the URL syntax for codebase in file ~/.java.policy is
 *    "file:/path_of_JdbcApplet/*". This is to get around of java security.
 *    You could choose other permissions instead of AllPermission.
 */

// Import the JDBC classes
import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

// Import the java classes used in applets
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import db.*;
import ui.*;

public class OrderApplet extends java.applet.Applet implements ActionListener
{

  // The button to push for executing the query
  Button execute_button;

  // The place where to dump the query result
  TextArea output;


  // Create the User Interface
  public void init ()
  {
    this.setLayout (new BorderLayout ());
    Panel p = new ContentPanel(this);
    this.add(p, BorderLayout.CENTER);
  }

  // Do the work
  public void actionPerformed (ActionEvent ev)
  {
    if (ev.getSource() == execute_button)
    {
      try
      {
        db.Base b = new db.Base();
        output.append (b.test() + " done.\n");
      }
      catch (Exception e)
      {
        e.printStackTrace();
        output.append (e.getMessage () + "\n");
      }
    }
  }
}
