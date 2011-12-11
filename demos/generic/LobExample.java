/* 
 * This sample demonstrate basic Lob support using JDBC 3.0 Lob Apis
 * (insert, fetch rows of BLOB and CLOB)
 *
 * It 1. drops and creates jdbc_demo_lob_table
 *       with blob, clob data types in the database.
 *    2. Inserts rows using database functions empty_lob()
 *    3. and empty_blob() 
 *    4. Fetches lob locators and writes data using them.
 *       Note required "for update" clause in select statement.
 *       This locks the row and lobs and allows writing to
 *       the lobs.
 *    5. Fetches data again and displays updated data.
 *    6. Please use jdk1.5 or later and ojdbc5.jar or later
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Blob;
import java.sql.Clob;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.text.NumberFormat;

public class LobExample
{
  public static void main (String args []) throws Exception
  {
    Connection conn = DemoConnectionFactory.getHRConnection( args );
    createSchemaObjects(conn);
    Statement stmt = conn.createStatement ();
    stmt.execute ("insert into jdbc_demo_lob_table values ('two', " +
                  "empty_blob(), empty_clob() )");
    
    ResultSet rset = stmt.executeQuery
                          ("select * from jdbc_demo_lob_table for update");
    while (rset.next ())
    {
      Blob blob = rset.getBlob (2);
      Clob clob = rset.getClob (3);

      fill (clob, 100L);
      fill (blob, 100L);
    }

    conn.commit();

    System.out.println ("Dump lobs contents");

    rset = stmt.executeQuery
                ("select * from jdbc_demo_lob_table");
    while (rset.next ())
    {
      Blob blob = rset.getBlob (2);
      Clob clob = rset.getClob (3);

      dump (blob);
      dump (clob);
    }
    // Close all resources
    rset.close();
    stmt.close();
    conn.close(); 
  }

  static void dump (Clob clob) throws Exception
  {
    Reader instream = clob.getCharacterStream();
    char[] buffer = new char[10];
    int length = 0;

    while ((length = instream.read(buffer)) != -1)
    {
      System.out.print("Read " + length + " chars: ");

      for (int i=0; i<length; i++)
        System.out.print(buffer[i]);
      System.out.println();
    }
    instream.close();
  }

  static void dump (Blob blob) throws Exception
  {
    InputStream instream = blob.getBinaryStream();
    byte[] buffer = new byte[10];
    int length = 0;

    NumberFormat format = NumberFormat.getInstance();
    format.setMinimumIntegerDigits(3);
    format.setGroupingUsed( false );
    while ((length = instream.read(buffer)) != -1)
    {
      System.out.print("Read " + length + " bytes: ");

      for (int i=0; i<length; i++)
      {
        int b = (int)buffer[i]  & 0XFF;
        System.out.print(format.format( (long)b )+ " ");
      }
      System.out.println();
    }
    instream.close();
  }

  static void fill (Clob clob, long length) throws Exception
  {
    Writer outstream = clob.setCharacterStream(1L);

    long i = 0;
    int chunk = 10;
    NumberFormat format = NumberFormat.getInstance();
    format.setMinimumIntegerDigits( chunk );
    format.setGroupingUsed( false );
    while (i < length)
    {
      outstream.write(format.format( i ), 0, chunk);

      i += (long)chunk;
      if (length - i < chunk)
         chunk = (int) (length - i);
    }
    outstream.close();
  }

  static void fill (Blob blob, long length)
    throws Exception
  {
    OutputStream outstream =
                 blob.setBinaryStream(1L);

    long i = 0;
    int chunk = 10;
    byte [] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    while (i < length)
    {
      data [0] = (byte)i;
      outstream.write(data, 0, chunk);

      i += (long)chunk;
      if (length - i < chunk)
        chunk = (int) (length - i);
    }
    outstream.close();
  }

  public static void createSchemaObjects( Connection conn ) throws SQLException
  {
    Statement stmt = conn.createStatement ();
    try
    {
      stmt.execute ("drop table jdbc_demo_lob_table");
    }
    catch (SQLException e){}
    String sql = "create table jdbc_demo_lob_table (x varchar2 (30), b blob, c clob)";
    stmt.execute (sql);
    stmt.close();
  }
}
