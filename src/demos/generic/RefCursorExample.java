/*
 * This sample shows how to call a PL/SQL function that opens
 * a cursor and get the cursor back as a Java ResultSet.
 *
 * sqlType CURSOR corresponds to "ref cursor". open the cursor
 * by specifying CURSOR type at register method. retrieve the
 * value by getObject method.
 *
 * note: jdk1.2 or later version is recommended. 
 */

import java.sql.*;
import java.io.*;

// Importing the Oracle Jdbc driver package makes the code more readable
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

class RefCursorExample
{
  public static void main (String args [])
       throws SQLException
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

    // Create the stored procedure
    init (conn);

    // Prepare a PL/SQL call
    CallableStatement call =
      conn.prepareCall ("{ ? = call java_refcursor.job_listing (?, ?)}");

    // Find out all the SALES person
    //  (marketing representative and marketing manager)
    call.registerOutParameter (1, OracleTypes.CURSOR);
    call.setString (2, "SA_REP");
    call.setString (3, "SA_MAN");
    call.execute ();
    ResultSet rset = (ResultSet)call.getObject (1);

    // Dump the cursor
    while (rset.next ())
      System.out.println (rset.getString ("FIRST_NAME") + "  "
                          + rset.getString ("LAST_NAME") + "  "
                          + rset.getString ("JOB_ID"));
      

    // Close all the resources
    rset.close();
    call.close();
    conn.close();

  }

  // Utility function to create the stored procedure
  static void init (Connection conn)
       throws SQLException
  {
    Statement stmt = conn.createStatement ();

    try
    {
      stmt.execute ("create or replace package java_refcursor as " +
       " type myrctype is ref cursor return EMPLOYEES%ROWTYPE; " +
       " function job_listing (j varchar2, k varchar2) return myrctype; " +
       "end java_refcursor;");

      stmt.execute ("create or replace package body java_refcursor as " +
       " function job_listing (j varchar2, k varchar2) return myrctype is " +
       "   rc myrctype; " +
       " begin " +
       "   open rc for select * from employees where job_id = j or job_id = k;" +
       "   return rc; " +
       "  end; " +
       "end java_refcursor;");
    } catch (Exception e)
    {
       e.printStackTrace();
    }

    stmt.close();
  }
}
