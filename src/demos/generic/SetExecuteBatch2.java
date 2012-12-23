/*
 * This sample demonstrates Oracle style batch. In Oracle
 * style batch, batched operations get flushed when
 * 1. batch_size is reached and execute*() is called, or
 * 2. sendBatch() is called.
 * 3. partial operations get flushed even exception happens. 
 *
 * note: 1. jdk1.2 or newer version is recommended. 
 *       2. please do not mix Oracle style batch and Java 
 *          standard batch
 *       3. Oracle style batch is introduced at Oracle8.1.5
 *          standard batch is introduced at Oracle8.1.6
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;

// You need to import oracle.jdbc.* in order to use the
// API extensions.
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

class SetExecuteBatch2
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

    setup (conn);
 
    PreparedStatement pstmt = conn.prepareStatement (
                                  "INSERT INTO departments VALUES(?, ?, 200, 1700)");
    ((OraclePreparedStatement)pstmt).setExecuteBatch (30);

    // demonstrates implicite batch flush
    // executeUpdate won't do real updates if batched operations not reached to batch size 30 
    for ( int i = 300; i < 320; ++i )
    {
       pstmt.setInt(1, i);
       pstmt.setString(2, ("department_" + i));
       pstmt.executeUpdate();
    }
    // we should get 0 here
    System.out.println("number of rows updated=" + 
                        pstmt.getUpdateCount());


    // demonstrates sendBatch(). It flushes the batch even batch size 30 not reached yet.
    // we should get 20 here
    System.out.println("number of rows updated=" +
                        ((OraclePreparedStatement)pstmt).sendBatch());

    // demonstrates implicite batch flush
    // executeUpdate will flush the batch when batch size 10 is reached
    ((OraclePreparedStatement)pstmt).setExecuteBatch (10);
    setup (conn);
    for ( int j = 300; j < 310; ++j )
    {
       pstmt.setInt(1, j);
       pstmt.setString(2, ("department_" + j));
       pstmt.executeUpdate();
    }
    // we should get 10 here
    System.out.println("number of rows updated=" +
                        pstmt.getUpdateCount());
    // we should get 0 here since no more batched operations
    System.out.println("number of rows updated=" +
                        ((OraclePreparedStatement)pstmt).sendBatch());

    // demostrates implicite batch flush
    // on exception, the batched operations will partially flushed
    setup (conn);
    try
    {
       for ( int k = 300; k < 310; ++k )
       {
          if ( (k == 305) || (k == 306) )
              pstmt.setInt (1, 305);
          else
              pstmt.setInt (1, k);
          pstmt.setString (2, ("department_" + k));
          pstmt.executeUpdate();
       }
    } catch (SQLException e)
    {
       if ( e.getErrorCode() != 1 ) // not key confliction exception
          throw e;
    }
    // we should get 6 here
    System.out.println("number of rows updated=" +
                        pstmt.getUpdateCount());

    setup (conn);
    pstmt.close ();
    conn.close();
  }

  // delete certain rows to avoid key confliction
  static void setup (Connection conn) throws SQLException
  {
    Statement stmt = conn.createStatement ();
    try
    {
      stmt.execute("delete from departments where department_id >= 300");
    } catch (SQLException e) {}
  }
}
