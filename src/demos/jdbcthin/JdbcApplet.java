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

public class JdbcApplet extends java.applet.Applet
                        implements ActionListener
{

  // The connect string 
  static final String connect_string = 
                  "jdbc:oracle:thin:hr/hr@//localhost:1521/orcl.oracle.com";

  // This is the kind of string you would use if going through the 
  // Oracle 8 connection manager which lets you run the database on a 
  // different host than the Web Server.  See the on-line documentation
  // for more information.
  // static final String connect_string = "jdbc:oracle:thin:hr/hr@(description=(address_list=(address=(protocol=tcp)(host=localhost)(port=1610))(address=(protocol=tcp)(host=localhost)(port=1521)))(source_route=yes)(connect_data=(service_name=orcl.oracle.com)))";

  // The query we will execute
  static final String query = "select 'Hello JDBC: ' || sysdate from dual";
  

  // The button to push for executing the query
  Button execute_button;

  // The place where to dump the query result
  TextArea output;

  // The connection to the database
  Connection conn;

  // Create the User Interface
  public void init ()
  {
    this.setLayout (new BorderLayout ());
    Panel p = new Panel ();
    p.setLayout (new FlowLayout (FlowLayout.LEFT));
    execute_button = new Button ("Hello JDBC");
    p.add (execute_button);
    execute_button.addActionListener (this);
    this.add ("North", p);
    output = new TextArea (10, 60);
    this.add ("Center", output);
  }

  // Do the work
  public void actionPerformed (ActionEvent ev)
  {
    if (ev.getSource() == execute_button)
    {
      try
      {

        // See if we need to open the connection to the database
        if (conn == null)
        {
          // Create a OracleDataSource instance and set URL
          OracleDataSource ods = new OracleDataSource();
          ods.setURL(connect_string); 

          // Connect to the databse
          output.append ("Connecting to " + connect_string + "\n");
          conn = ods.getConnection ();
          output.append ("Connected\n");
        }

        // Create a statement
        Statement stmt = conn.createStatement ();

        // Execute the query
        output.append ("Executing query " + query + "\n");
        ResultSet rset = stmt.executeQuery (query);

        // Dump the result
        while (rset.next ())
          output.append (rset.getString (1) + "\n");

        // We're done
        output.append ("done.\n");
      }
      catch (Exception e)
      {
        e.printStackTrace();
        // Oops
        output.append (e.getMessage () + "\n");
      }
    }
  }
}
