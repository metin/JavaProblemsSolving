/**
 * A simple sample to demonstrate Batch Updates.
 * Please use jdk1.2 or later version
 */

import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

public class BatchUpdates
{
  public static void main(String[] args)
  {
    Connection          conn = null;
    Statement           stmt = null;
    PreparedStatement   pstmt = null;
    ResultSet           rset = null;
    int                 i = 0;

    try
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
      conn = ods.getConnection ();

      stmt = conn.createStatement();
      try { stmt.execute(
            "create table mytest_table (col1 number, col2 varchar2(20))");
      } catch (Exception e1) {}

      //
      // Insert in a batch.
      //
      pstmt = conn.prepareStatement("insert into mytest_table values (?, ?)");

      pstmt.setInt(1, 1);
      pstmt.setString(2, "row 1");
      pstmt.addBatch();

      pstmt.setInt(1, 2);
      pstmt.setString(2, "row 2");
      pstmt.addBatch();

      pstmt.executeBatch();

      //
      // Select and print results.
      //
      rset = stmt.executeQuery("select * from mytest_table");
      while (rset.next())
      {
        System.out.println(rset.getInt(1) + ", " + rset.getString(2));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (stmt != null)
      {
        try { stmt.execute("drop table mytest_table"); } catch (Exception e) {}
        try { stmt.close(); } catch (Exception e) {}
      }
      if (pstmt != null)
      {
        try { pstmt.close(); } catch (Exception e) {}
      }
      if (conn != null)
      {
        try { conn.close(); } catch (Exception e) {}
      }
    }
  }
}
