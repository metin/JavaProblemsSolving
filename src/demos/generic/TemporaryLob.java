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
 * It needs jdk1.5 or later version and ojdbc5.jar
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

class TemporaryLob
{
  public static void main (String args [])
       throws Exception
  {
    Connection conn = DemoConnectionFactory.getHRConnection( args );
    LobExample.createSchemaObjects( conn );
    BLOB tempBlob = BLOB.createTemporary (conn, 
                                          false, 
                                          BLOB.DURATION_SESSION);
    CLOB tempClob = CLOB.createTemporary (conn, 
                                          false, 
                                          CLOB.DURATION_SESSION);
    System.out.println ("tempBlob.isTemporary()="+
                        tempBlob.isTemporary());
    System.out.println ("tempClob.isTemporary()="+
                        tempClob.isTemporary());
    LobExample.fill(tempBlob, 100L);
    LobExample.fill(tempClob, 100L);

    String insertSql = "insert into jdbc_demo_lob_table values ( ?, ?, ? )";
    PreparedStatement pstmt = conn.prepareStatement( insertSql );
    pstmt.setString( 1, "one" );
    pstmt.setBlob( 2, tempBlob );
    pstmt.setClob( 3, tempClob );
    pstmt.execute();
    pstmt.close();

    tempBlob.freeTemporary ();
    tempClob.freeTemporary ();

    conn.commit();

    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery( "select b, c from jdbc_demo_lob_table" );
    while( rs.next() )
    {
      Blob permanentBlob = rs.getBlob(1);
      Clob permanentClob = rs.getClob(2);
      System.out.println ("permanentBlob.isTemporary()="+
                          ((BLOB)permanentBlob).isTemporary());
      System.out.println ("permanentClob.isTemporary()="+
                          ((CLOB)permanentClob).isTemporary());
      LobExample.dump(permanentBlob);
      LobExample.dump(permanentClob);
    }
    rs.close();
    stmt.close();

    conn.close();
  }
}
