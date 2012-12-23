/* 
 * This sample demonstrate basic File support
 *
 * It shows
 * 1. how to use DIRECTORY object
 * 2. how to use PL/SQL package -- dbms_lob to
 *    open, access, retrieve information from,
 *    close object of BFILE which is stored at DIRECTORY.
 *
 * usage: java PLSQL_FileExample <test_dir>
 *        <test_dir> is a location where binary files
 *        file1.bin and file2.bin are stored
 *
 * note: 1. It needs jdk1.2 or later version and classes12.zip
 *       2. It drops, creates, and populates table
 *          test_dir_table in the database
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.io.InputStream;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.BFILE;


import oracle.sql.BFILE;

public class PLSQL_FileExample
{
  public static void main (String args [])
       throws Exception
  {
    if ( args.length != 2 )
    {
         System.out.println("usage: java PLSQL_FileExample <test_dr> <system password>");
         System.exit(0);
    }

    FileExample.createDirectory( args );

    Connection conn = DemoConnectionFactory.getHRConnection( args );
    Statement stmt = conn.createStatement ();

    try
    {
      stmt.execute ("drop table test_dir_table");
    }
    catch (SQLException e)
    {
      // An error is raised if the table does not exist.  Just ignore it.
    }

    // Create and populate a table with files
    // The files file1.bin and file2.bin must exist in the directory
    // TEST_DIR created above as symbolic name for args[0]
    stmt.execute ("create table test_dir_table " +
                  "(x varchar2 (30), b bfile)");
    stmt.execute ("insert into test_dir_table values ('one', " +
                  "bfilename ('TEST_DIR', 'file1.bin'))");
    stmt.execute ("insert into test_dir_table values ('two', " +
                  "bfilename ('TEST_DIR', 'file2.bin'))");

    // Select the file from the table
    ResultSet rset = stmt.executeQuery ("select * from test_dir_table");
    while (rset.next ())
    {
      String x = rset.getString (1);
      BFILE bfile = ((OracleResultSet)rset).getBFILE (2);
      System.out.println (x + " " + bfile);

      // Dump the file contents
      dumpBfile (conn, bfile);
    }

    // Close all the open resources
    rset.close();
    stmt.close();
    conn.close();
  }

  // Utility function to dump the contents of a Bfile
  static void dumpBfile (Connection conn, BFILE bfile)
    throws Exception
  {
    OracleCallableStatement read =
      (OracleCallableStatement)
        conn.prepareCall ("begin dbms_lob.read (?, ?, ?, ?); end;");

    System.out.println ("Dumping file " + filegetname (conn, bfile));
    System.out.println ("File exists: " + fileexists (conn, bfile));
    System.out.println ("File open: " + fileisopen (conn, bfile));

    System.out.println ("Opening File: ");

    bfile = fileopen (conn, bfile);

    System.out.println ("File open: " + fileisopen (conn, bfile));

    long length = getLength (conn, bfile);
    System.out.println ("File length: " + length);

    long i = 0;
    int chunk = 10;

    while (i < length)
    {
      read.setBFILE (1, bfile);
      read.setLong (2, chunk);
      read.registerOutParameter (2, Types.NUMERIC);
      read.setLong (3, i + 1);
      read.registerOutParameter (4, Types.VARBINARY);
      read.execute ();

      long read_this_time = read.getLong (2);
      byte [] bytes_this_time = read.getBytes (4);

      System.out.print ("Read " + read_this_time + " bytes: ");

      int j;
      for (j = 0; j < read_this_time; j++)
        System.out.print (bytes_this_time [j] + " ");
      System.out.println ();

      i += read_this_time;
    }

    fileclose (conn, bfile);
    fileisopen (conn, bfile);

    read.close ();
  }

  // Utility function to get the length of a Bfile
  static long getLength (Connection conn, BFILE bfile)
    throws SQLException
  {
    OracleCallableStatement cstmt =
      (OracleCallableStatement)
        conn.prepareCall ("begin ? := dbms_lob.getLength (?); end;");
    try
    {
      cstmt.registerOutParameter (1, Types.NUMERIC);
      cstmt.setBFILE (2, bfile);
      cstmt.execute ();
      return cstmt.getLong (1);
    }
    finally
    {
      cstmt.close ();
    }
  }

  // Utility function to test if a Bfile exists
  static boolean fileexists (Connection conn, BFILE bfile)
    throws SQLException
  {
    OracleCallableStatement cstmt =
      (OracleCallableStatement)
        conn.prepareCall ("begin ? := dbms_lob.fileexists (?); end;");

    try
    {
      cstmt.registerOutParameter (1, Types.NUMERIC);
      cstmt.setBFILE (2, bfile);
      cstmt.execute ();
      return cstmt.getBoolean (1);
    }
    finally
    {
      cstmt.close ();
    }
  }

  // Utility function to return the filename of a Bfile
  static String filegetname (Connection conn, BFILE bfile)
    throws SQLException
  {
    OracleCallableStatement cstmt =
      (OracleCallableStatement)
        conn.prepareCall ("begin dbms_lob.filegetname (?, ?, ?); end;");

    try
    {
      cstmt.setBFILE (1, bfile);
      cstmt.registerOutParameter (2, Types.VARCHAR);
      cstmt.registerOutParameter (3, Types.VARCHAR);
      cstmt.execute ();
      
      return cstmt.getString (3);
    }
    finally
    {
      cstmt.close ();
    }
  }

  // Utility function to open a Bfile.
  // Note that an open Bfile is a different object from the
  // closed one.  The fileopen entrypoint returns the
  // open file object which is the one you have to use to
  // read the file contents.
  static BFILE fileopen (Connection conn, BFILE bfile)
    throws SQLException
  {
    OracleCallableStatement cstmt =
      (OracleCallableStatement)
        conn.prepareCall ("begin dbms_lob.fileopen (?, 0); end;");

    try
    {
      cstmt.setBFILE (1, bfile);
      cstmt.registerOutParameter (1, OracleTypes.BFILE);
      cstmt.execute ();
      return cstmt.getBFILE (1);
    }
    finally
    {
      cstmt.close ();
    }
  }

  // Utility function to test if a Bfile is open
  static boolean fileisopen (Connection conn, BFILE bfile)
    throws SQLException
  {
    OracleCallableStatement cstmt =
      (OracleCallableStatement)
        conn.prepareCall ("begin ? := dbms_lob.fileisopen (?); end;");

    try
    {
      cstmt.registerOutParameter (1, Types.NUMERIC);
      cstmt.setBFILE (2, bfile);
      cstmt.execute ();
      return cstmt.getBoolean (1);
    }
    finally
    {
      cstmt.close ();
    }
  }

  // Utility function to close a Bfile
  static void fileclose (Connection conn, BFILE bfile)
    throws SQLException
  {
    OracleCallableStatement cstmt =
      (OracleCallableStatement)
        conn.prepareCall ("begin dbms_lob.fileclose (?); end;");

    System.out.println ("Closing bfile.");
    cstmt.setBFILE (1, bfile);
    cstmt.execute ();
    cstmt.close ();
  }
}
