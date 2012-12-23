/*
 * This example shows how to stream data from the database
 *
 * When a very large ASCII value is input to a LONGVARCHAR
 * parameter, it may be more practical to send it via a 
 * java.io.InputStream.
 * The setXXX methods for setting IN parameter values must
 * specify types that are compatible with the defined SQL type
 * of the input parameter.
 *
 * This example is to
 *      1. create table "streamexample"
 *      2. put the source code of this very test into table
 *         "streamexample".
 *      3. retrieve the contents of table "streamexample"
 *         and write it into file example.out.
 *
 * note: jdk1.2 is recommended. jdk1.1 will also work
 */

import java.sql.*;
import java.io.*;
import oracle.jdbc.pool.OracleDataSource;

class StreamExample
{
  public static void main (String args [])
       throws SQLException, IOException
  {
    String url = "jdbc:oracle:oci8:@";
    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    // Create a OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Connect to the database
    Connection conn = ods.getConnection();

    // It's faster when you don't commit automatically
    conn.setAutoCommit (false);

    // Create a Statement
    Statement stmt = conn.createStatement ();

    // Create the example table
    try
    {
      stmt.execute ("drop table streamexample");
    }
    catch (SQLException e)
    {
      // An exception would be raised if the table did not exist
      // We just ignore it
    }

    // Create the table
    stmt.execute ("create table streamexample (NAME varchar2 (256), DATA long)");

    // Let's insert some data into it.  We'll put the source code
    // for this very test in the database.
    File file = new File ("StreamExample.java");
    InputStream is = new FileInputStream ("StreamExample.java");
    PreparedStatement pstmt = 
      conn.prepareStatement ("insert into streamexample (data, name) values (?, ?)");
    pstmt.setAsciiStream (1, is, (int)file.length ());
    pstmt.setString (2, "StreamExample");
    pstmt.execute ();

    // Do a query to get the row with NAME 'StreamExample'
    ResultSet rset = 
      stmt.executeQuery ("select DATA from streamexample where NAME='StreamExample'");
    
    // Get the first row
    if (rset.next ())
    {
      // Get the data as a Stream from Oracle to the client
      InputStream gif_data = rset.getAsciiStream (1);

      // Open a file to store the gif data
      FileOutputStream os = new FileOutputStream ("example.out");
      
      // Loop, reading from the gif stream and writing to the file
      int c;
      while ((c = gif_data.read ()) != -1)
        os.write (c);

      // Close the file
      os.close ();
    }
  
    // Close all the resources
    if (rset != null)
      rset.close();
    
    if (stmt != null)
      stmt.close();
    
    if (pstmt != null)
      pstmt.close();

    if (conn != null)
      conn.close();
  }
}
