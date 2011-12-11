/* 
 * This sample demonstrate basic Lob support
 *
 * It shows how to use PL/SQL package DBMS_LOB 
 * to do operations on BLOB and CLOB data type.
 *
 * The examples here duplicate function that could be done
 * more effectively by using the client interfaces. The point
 * is to see how to call PL/SQL.
 * 
 * note: 1. It needs jdk1.5 or later version and ojdbc5.jar or later
 *       2. It drops, creates, and populates table
 *          jdbc_demo_lob_table in the database.
 *       3. The data is dumped and modified using
 *          calls to DBMS_LOB package functions invoked
 *          via CallableStatements.
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Types;

import java.text.NumberFormat;

public class PLSQL_LobExample
{
  public static void main (String args [])
    throws Exception
  {
    Connection conn = DemoConnectionFactory.getHRConnection( args );
    LobExample.createSchemaObjects(conn);
    Statement stmt = conn.createStatement ();
    stmt.execute ("insert into jdbc_demo_lob_table values ('one', empty_blob(), empty_clob() )" );
    ResultSet rset = stmt.executeQuery ("select * from jdbc_demo_lob_table for update");
    while (rset.next ())
    {
      Blob blob = rset.getBlob (2);
      Clob clob = rset.getClob (3);

      fill (conn, clob, 100L);
      fill (conn, blob, 100L);
    }

    System.out.println ("Dump lobs after data update");
    rset = stmt.executeQuery ("select * from jdbc_demo_lob_table");
    while (rset.next ())
    {
      Blob blob = rset.getBlob (2);
      Clob clob = rset.getClob (3);
      dump (conn, blob);
      dump (conn, clob);
    }
    rset.close();
    stmt.close();
    conn.close();
  }

  static void dump (Connection conn, Clob clob)
    throws Exception
  {
    CallableStatement cstmt1 =  conn.prepareCall ("begin ? := dbms_lob.getLength (?); end;");
    CallableStatement cstmt2 = conn.prepareCall ("begin dbms_lob.read (?, ?, ?, ?); end;");

    cstmt1.registerOutParameter (1, Types.NUMERIC);
    cstmt1.setClob (2, clob);
    cstmt1.execute ();

    long length = cstmt1.getLong (1);
    long i = 0;
    int chunk = 10;

    while (i < length)
    {
      cstmt2.setClob (1, clob);
      cstmt2.setLong (2, chunk);
      cstmt2.registerOutParameter (2, Types.NUMERIC);
      cstmt2.setLong (3, i + 1);
      cstmt2.registerOutParameter (4, Types.VARCHAR);
      cstmt2.execute ();

      long read_this_time = cstmt2.getLong (2);
      String string_this_time = cstmt2.getString (4);

      System.out.print ("Read " + read_this_time + " chars: ");
      System.out.println (string_this_time);
      i += read_this_time;
    }
    cstmt1.close ();
    cstmt2.close ();
  }

  static void dump (Connection conn, Blob blob)
    throws Exception
  {
    NumberFormat format = NumberFormat.getInstance();
    format.setMinimumIntegerDigits(3);
    format.setGroupingUsed( false );
    CallableStatement cstmt1 = conn.prepareCall ("begin ? := dbms_lob.getLength (?); end;");
    CallableStatement cstmt2 = conn.prepareCall ("begin dbms_lob.read (?, ?, ?, ?); end;");

    cstmt1.registerOutParameter (1, Types.NUMERIC);
    cstmt1.setBlob (2, blob);
    cstmt1.execute ();

    long length = cstmt1.getLong (1);
    long i = 0;
    int chunk = 10;

    while (i < length)
    {
      cstmt2.setBlob (1, blob);
      cstmt2.setLong (2, chunk);
      cstmt2.registerOutParameter (2, Types.NUMERIC);
      cstmt2.setLong (3, i + 1);
      cstmt2.registerOutParameter (4, Types.VARBINARY);
      cstmt2.execute ();

      long read_this_time = cstmt2.getLong (2);
      byte [] bytes_this_time = cstmt2.getBytes (4);

      System.out.print ("Read " + read_this_time + " bytes: ");

      int j;
      for (j = 0; j < read_this_time; j++)
        System.out.print (format.format(bytes_this_time [j]) + " ");
      System.out.println ();

      i += read_this_time;
    }

    cstmt1.close ();
    cstmt2.close ();
  }

  static void fill (Connection conn, Clob clob, long length)
    throws Exception
  {
    CallableStatement cstmt = conn.prepareCall ("begin dbms_lob.write (?, ?, ?, ?); end;");

    long i = 0;
    long chunk = 10;
    NumberFormat format = NumberFormat.getInstance();
    format.setMinimumIntegerDigits( (int)chunk );
    format.setGroupingUsed( false );

    while (i < length)
    {
      cstmt.setClob (1, clob);
      cstmt.setLong (2, chunk);
      cstmt.setLong (3, i + 1);
      cstmt.setString (4, format.format( i ));
      cstmt.execute ();

      i += chunk;
      if (length - i < chunk)
        chunk = length - i;
    }

    cstmt.close ();
  }

  static void fill(Connection conn, Blob blob, long length)
    throws Exception
  {
    CallableStatement cstmt = conn.prepareCall ("begin dbms_lob.write (?, ?, ?, ?); end;");

    long i = 0;
    long chunk = 10;

    byte [] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    while (i < length)
    {
      cstmt.setBlob (1, blob);
      cstmt.setLong (2, chunk);
      cstmt.setLong (3, i + 1);
      data [0] = (byte)i;
      cstmt.setBytes (4, data);
      cstmt.execute ();

      i += chunk;
      if (length - i < chunk)
        chunk = length - i;
    }
    cstmt.close ();
  }
}
