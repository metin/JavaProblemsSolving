/*
 * This sample shows how to trim BLOBs and CLOBs.
 * It needs jdk1.5 or later version and ojdbc5.jar or later
 * It drops, creates, and populates table jdbc_demo_lob_table
 * including columns of blob, clob data types in the database
 * The columns are then fetched and the length of each lob is
 * displayed. Then the lobs are truncated and the length
 * displayed again.
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Blob;
import java.sql.Clob;

public class TruncateLob
{
  public static void main (String args []) throws SQLException
  {
    Connection conn = DemoConnectionFactory.getHRConnection( args );
    LobExample.createSchemaObjects( conn );
    Statement stmt = conn.createStatement ();
    stmt.execute ("insert into jdbc_demo_lob_table values ('one', " +
                  "'010101010101010101010101010101', " +
                  "'onetwothreefour')");

    ResultSet rset = stmt.executeQuery
                          ("select * from jdbc_demo_lob_table for update");
    while (rset.next ())
    {
      Blob blob = rset.getBlob (2);
      Clob clob = rset.getClob (3);
      System.out.println ("blob.length()="+blob.length());
      System.out.println ("clob.length()="+clob.length());

      System.out.println ("Truncate the lob to legnth = 6");
      blob.truncate (6L);
      clob.truncate (6L);

      System.out.println ("new blob.length()="+blob.length());
      System.out.println ("new clob.length()="+clob.length());
    }
    rset.close ();
    stmt.close ();
    conn.close ();
  }
}
