/*
 * This sample shows how to create
 * a temporary BLOB and CLOB, write
 * some data to them and then insert
 * them into a table. This makes a 
 * permanent copy in the table. The 
 * temp lobs are still available for
 * further use if desired until the
 * transaction is committed.
 * When fetched from the table, the
 * lobs are no longer temporary.
 *
 * This version uses the new 
 * JDBC 4.0 factory methods in 
 * java.sql.Connection and the
 * free methods in java.sql.Blob and Clob
 *
 * Testing for temporary status still 
 * requires Oracle specfiic APIs in 
 * oracle.sql.BLOB and oracle.sql.CLOB.
 *
 * It needs jdk6 or later version and ojdbc6.jar
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Blob;
import java.sql.Clob;


class TemporaryLobJDBC40
{
  public static void main (String args [])
    throws Exception
  {
    Connection conn = DemoConnectionFactory.getHRConnection( args );
    LobExample.createSchemaObjects( conn );
    Blob tempBlob = conn.createBlob();
    Clob tempClob = conn.createClob();

    System.out.println ("tempBlob.isTemporary()="+
                        ((oracle.sql.BLOB)tempBlob).isTemporary());
    System.out.println ("tempClob.isTemporary()="+
                        ((oracle.sql.CLOB)tempClob).isTemporary());
    LobExample.fill(tempBlob, 100L);
    LobExample.fill(tempClob, 100L);

    String insertSql = "insert into jdbc_demo_lob_table values ( ?, ?, ? )";
    PreparedStatement pstmt = conn.prepareStatement( insertSql );
    pstmt.setString( 1, "one" );
    pstmt.setBlob( 2, tempBlob );
    pstmt.setClob( 3, tempClob );
    pstmt.execute();
    pstmt.close();
    tempBlob.free();
    tempClob.free();
    conn.commit();

    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery( "select b, c from jdbc_demo_lob_table" );
    while( rs.next() )
    {
      Blob permanentBlob = rs.getBlob(1);
      Clob permanentClob = rs.getClob(2);
      System.out.println ("permanentBlob.isTemporary()="+
                          ((oracle.sql.BLOB)permanentBlob).isTemporary());
      System.out.println ("permanentClob.isTemporary()="+
                          ((oracle.sql.CLOB)permanentClob).isTemporary());
      LobExample.dump(permanentBlob);
      LobExample.dump(permanentClob);
    }
    rs.close();
    stmt.close();
    conn.close();
  }
}
