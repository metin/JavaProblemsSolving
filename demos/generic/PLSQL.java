/*
 * This sample shows how to call PL/SQL blocks from JDBC.
 *
 * It 1. creates stored procedure / function first, then
 *    2. makes calls to procedures with / without parameter,
 *    3. makes calls to functions with / without parameter.
 *    4. shows the correspondence of IN / OUT parameter
 *       with get / set /register methods.
 *    5. shows CallableStatement.setXXX() will take place of 
 *       set-value statement in PL/SQL blocks, please look at
 *       the arguments and set-value statements of procinout
 *
 * note: jdk1.2 or later version is recommended. 
 */

import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

class PLSQL
{
  public static void main (String args [])
       throws SQLException, ClassNotFoundException
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

    // Create an OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Connect to the database
    Connection conn = ods.getConnection();

    // Create the stored procedures
    init (conn);

    // Call a procedure with no parameters
    {
      CallableStatement procnone = conn.prepareCall ("begin procnone; end;");
      procnone.execute ();
      System.out.println("\ndump table regions after calling to procnone--"
                         + "insert 101 Africa\n");
      dumpTable (conn);
      procnone.close();
    }

    // Call a procedure with an IN parameter
    {
      CallableStatement procin = conn.prepareCall ("begin procin (?, ?); end;");
      procin.setInt (1, 303);
      procin.setString (2, "Australia");
      procin.execute ();
      System.out.println("\ndump table regions after calling to procin--"
                         + "insert 303 Australia\n");
      dumpTable (conn);
      procin.close();
    }

    // Call a procedure with an OUT parameter
    {
      CallableStatement procout = conn.prepareCall ("begin procout (?, ?); end;");
      procout.registerOutParameter (1, Types.INTEGER);
      procout.registerOutParameter (2, Types.CHAR);
      procout.execute ();
      System.out.println ("\nOut argument to procout in PL/SQL block is:\n\t" +
                           procout.getInt (1) + " " + procout.getString (2));
      procout.close();
    }
    
    // Call a procedure with an IN/OUT prameter
    // This will insert "404, North Pole" instead of "303, Mars" that
    // are in PL/SQL block of procedure procinout into table regions
    {
      CallableStatement procinout = conn.prepareCall ("begin procinout (?, ?); end;");
      procinout.registerOutParameter (1, Types.INTEGER);
      procinout.registerOutParameter (2, Types.VARCHAR);
      procinout.setInt (1, 404);
      procinout.setString (2, "North Pole");
      procinout.execute ();
      System.out.println ("\ndump table regions after calling to procinout--"
                          + "insert 404 North Pole\n");
      dumpTable (conn);
      System.out.println ("Out argument in PL/SQL block definition of procinout is:\n\t" +
                          procinout.getInt (1) + " " + procinout.getString (2));
      procinout.close();
    }

    // Call a function with no parameters
    {
      CallableStatement funcnone = conn.prepareCall ("begin ? := funcnone; end;");
      funcnone.registerOutParameter (1, Types.CHAR);
      funcnone.execute ();
      System.out.println ("\nReturn value of funcnone is: " + funcnone.getString (1));
      funcnone.close();
    }

    // Call a function with an IN parameter
    {
      CallableStatement funcin = conn.prepareCall ("begin ? := funcin (?); end;");
      funcin.registerOutParameter (1, Types.CHAR);
      funcin.setString (2, "testing");
      funcin.execute ();
      System.out.println ("\nReturn value of funcin is: " + funcin.getString (1));
      funcin.close();
    }

    // Call a function with an OUT parameter
    {
      CallableStatement funcout = conn.prepareCall ("begin ? := funcout (?); end;");
      funcout.registerOutParameter (1, Types.CHAR);
      funcout.registerOutParameter (2, Types.CHAR);
      funcout.execute ();
      System.out.println ("\nReturn value of funcout is: " + funcout.getString (1));
      System.out.println ("Out argument is: " + funcout.getString (2));
      funcout.close();
    }

    // Close the connection
    conn.close();
  }

  // Utility function to dump the contents of the REGIONS table and
  // delete newly added rows 
  static void dumpTable (Connection conn)
    throws SQLException
  {
    Statement stmt = conn.createStatement ();
    ResultSet rset = stmt.executeQuery ("select region_id, region_name from regions order by region_id");
    while (rset.next ())
      System.out.println (rset.getInt(1) + " " + rset.getString (2));
    stmt.execute ("delete from regions where region_id > 100");
    rset.close();
    stmt.close();
  }

  // Utility function to create the stored procedures
  static void init (Connection conn)
    throws SQLException
  {
    Statement stmt = conn.createStatement ();
    try { stmt.execute ("delete from regions where region_id > 100"); } catch (SQLException e) { } 
    stmt.execute ("create or replace procedure procnone is " +
                  "begin insert into regions values (101, 'Africa'); end;");
    stmt.execute ("create or replace procedure procin (x Int, y char) is " +
                  "begin insert into regions values (x, y); end;");
    stmt.execute ("create or replace procedure procout (x out Int, y out char) is " +
                  "begin x := 202; y := 'Moon'; end;");
    stmt.execute ("create or replace procedure procinout (x in out Int, y in out varchar) "
                  + "is begin insert into regions values (x, y); "
                  + "x := 303; y := 'Mars'; end;");

    stmt.execute ("create or replace function funcnone return char is " +
                  "begin return 'tested'; end;");
    stmt.execute ("create or replace function funcin (y char) return char "
                  + "is begin return y || y; end;");
    stmt.execute ("create or replace function funcout (y out char) " +
                  "return char is begin y := 'tested'; " +
                  "return 'returned'; end;");
    stmt.close();
  }
}
