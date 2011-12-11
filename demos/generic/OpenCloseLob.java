/*
 * This sample shows how to open, close BLOB and CLOB
 * and test whether they are open or not.
 *
 * note: 1. It needs jdk1.5 or later version and ojdbc5.jar
 *       2. It drops, creates, and populates a table 
 *          including types of BLOB and CLOB
 *       3. The lobs are fetched and the open/close APIs are
 *          exercised.
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

// This example uses some of the remaining proprietary extensions
// so it must import the Oracle implementation classes.
import oracle.sql.BLOB;
import oracle.sql.CLOB;

import oracle.jdbc.pool.OracleDataSource;

class OpenCloseLob
{
  public static void main (String args [])
    throws SQLException
  {
    Connection conn = DemoConnectionFactory.getHRConnection( args );
    LobExample.createSchemaObjects( conn );
    Statement stmt = conn.createStatement ();

    // Populate the table -- using server side conversion from
    // char data to BLOB and CLOB
    stmt.execute ("insert into jdbc_demo_lob_table values ('one', " +
                  "'010101010101010101010101010101', 'onetwothreefour')");

    ResultSet rset = stmt.executeQuery ("select * from jdbc_demo_lob_table");
    while (rset.next ())
    {
      BLOB blob = (BLOB) rset.getObject (2);
      CLOB clob = (CLOB) rset.getObject (3);

      System.out.println ("Open the lobs");
      blob.open (BLOB.MODE_READWRITE);
      clob.open (CLOB.MODE_READWRITE);

      System.out.println ("blob.isOpen()="+blob.isOpen());
      System.out.println ("clob.isOpen()="+clob.isOpen());

      System.out.println ("Close the lobs");
      blob.close ();
      clob.close ();

      System.out.println ("blob.isOpen()="+blob.isOpen());
      System.out.println ("clob.isOpen()="+clob.isOpen());    
    }

    rset.close ();
    stmt.close ();
    conn.close ();
  }
}
