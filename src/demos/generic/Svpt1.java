/*
 * A simple example showing how to use some of the Savepoint APIs
 * in JDBC 3.0. The program uses the HR schema in the Oracle seed
 * database.  Please refer to the "Sample Schemas" documentation
 * for schema details.
 *
 * Oracle JDBC also provides Oracle-specific APIs that are
 * equivalent to the standard Savepoint APIs in JDBC 3.0.
 * These start with "oracle" prefix.  Please refer to the JDBC
 * document for details.
 *
 * The program uses a single table "regions" with the following
 * main operations:
 *   -> insert row 1
 *   -> set Savepoint 1
 *   -> insert row 2
 *   -> set Savepoint 2
 *   -> set Savepoint 3
 *   -> rollback to Savepoint 1
 *   -> commit
 * It also checks for Savepoint support from the DatabaseMetaData,
 * and obtains the names or ids of the established Savepoints.
 * With normal execution, the program should report that there
 * is only one newly inserted row (i.e., <10, 'newregion1'>),
 * and leave the database state unchanged.
 */

import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

public class Svpt1
{
  public static void main(String args[])
  {
    Connection conn = null;
    Statement  stmt = null;
    ResultSet  rset = null;
    int        rows = 0;

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

      // Create a Statement
      stmt = conn.createStatement();

      // Cleanup table to original state
      stmt.execute("DELETE FROM regions WHERE region_id >= 10");

      // Turn off the auto-commit mode
      conn.setAutoCommit(false);

      DatabaseMetaData dbmd = conn.getMetaData();

      // Check whether Savepoint is supported
      show("Checking savepoint support ...");
      if (dbmd.supportsSavepoints())
        show("Savepoint is supported");
      else
        show("Savepoint is not supported");

      // Insert a new record into the "regions" table
      show("Insert record(10, 'newregion1') ...");
      rows =
        stmt.executeUpdate("insert into regions values (10, 'newregion1')");

      // Establish the first savepoint (named)
      show("Establish savepoint 1 ...");
      Savepoint svpt1 = conn.setSavepoint("svpt_1");

      // Insert a second record into the "regions" table
      show("Insert record(11, 'newregion2') ...");
      rows =
        stmt.executeUpdate("insert into regions values (11, 'newregion2')");

      // Establish the second savepoint (named)
      show("Establish savepoint 2 ...");
      Savepoint svpt2 = conn.setSavepoint("svpt_2");

      // Establish the third savepoint (unnamed)
      show("Establish savepoint 3 ...");
      Savepoint svpt3 = conn.setSavepoint();

      // Insert a third record into the "regions" table
      show("Insert record(12, 'newregion3') ...");
      rows =
        stmt.executeUpdate("insert into regions values (12, 'newregion3')");

      // Check names and ids of established Savepoints
      show("The name of txn savepoint 1 is: " + svpt1.getSavepointName());
      show("The name of txn savepoint 2 is: " + svpt2.getSavepointName());
      show("The id of txn savepoint 3 is: " + svpt3.getSavepointId());

      // Rollback to the first savepoint
      show("Rollback to savepoint 1 ...");
      conn.rollback(svpt1);

      // Commit the transaction
      show("Commit the transaction ...");
      conn.commit();

      // Dump the content of the table --
      //   Only updates before the first savepoint should be reflected;
      //   all updates after the first savepoint should be rolled back.
      show("Checking changes in database ...");
      rset = stmt.executeQuery ("select * from regions where region_id >= 10");

      // Iterate through and print the result
      while (rset.next ())
      {
        show ("Region_id: " + rset.getString (1));
        show ("Region_name: " + rset.getString (2));
      }

      // Close the ResultSet
      rset.close();

      // Cleanup table to original state
      stmt.execute("DELETE FROM regions WHERE region_id >= 10");

      // Close the Statement
      stmt.close();

      // Close the Connection
      conn.close();
    }
    catch(SQLException sqlexc)
    {
      show("Unexpected SQL Exception " + sqlexc.getMessage());
      sqlexc.printStackTrace();
    }
    catch(Exception exc)
    {
      show("Unexpected Exception " + exc.getMessage());
      exc.printStackTrace();
    }
  }

  static void show(String mesg)
  {
    System.out.println(mesg);
  }
}

