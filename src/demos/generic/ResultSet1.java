/**
 * A simple sample to check the availability of scrollable result sets.
 * Please compare to ResultSet2.java ~ ResultSet6.java
 *
 * Please use jdk1.2 or later version
 */

import java.sql.*;
import oracle.jdbc.pool.OracleDataSource;

public class ResultSet1
{
  public static void main(String[] args) throws SQLException
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

    // Get the metadata regarding this connection's database
    DatabaseMetaData dbmd = conn.getMetaData();

    // List all the possible result set types
    int resultset_types[] = 
    {
      ResultSet.TYPE_FORWARD_ONLY,
      ResultSet.TYPE_SCROLL_INSENSITIVE,
      ResultSet.TYPE_SCROLL_SENSITIVE 
    };

    // List all the possible result set concurrency types
    int concurrency_types[] = 
    { 
      ResultSet.CONCUR_READ_ONLY,
      ResultSet.CONCUR_UPDATABLE 
    };

    // List the result set type names
    String resultset_types_msg [] = 
    {
      "Forward only", 
      "Scroll insensitive", 
      "Scroll sensitive"
    };

    // List the concurrency type names
    String concurrency_types_msg[] = 
    { 
      "Read only", 
      "Updatable" 
    };

    // Check the availability of the result type and concurrency type
    for (int i=0; i<resultset_types.length; i++)
    {
      for (int j=0; j<concurrency_types.length; j++)
      {
        int type = resultset_types[i];
        int concurrency = concurrency_types[j];

        System.out.println ("Type: "+resultset_types_msg[i]+"    "+
                            "Concurrency: "+concurrency_types_msg[j]);
        System.out.println
                  ("----------------------------------------------------");

        // Return true if the result set type is supported
        System.out.println ("supportResultSetType: "+
                            dbmd.supportsResultSetType(type));

        // Return true if the result set type and concurrency type is supported
        System.out.println ("supportsResultSetConcurrency: "+
                        dbmd.supportsResultSetConcurrency(type, concurrency));

        // Return true if the result set's updates are visible
        System.out.println ("ownUpdatesAreVisible: "+
                            dbmd.ownUpdatesAreVisible(type));

        // Return true if the result set's deletions are visible
        System.out.println ("ownDeletesAreVisible: "+
                            dbmd.ownDeletesAreVisible(type));

        // Return true if the result set's insertions are visible
        System.out.println ("ownInsertAreVisible: "+
                            dbmd.ownInsertsAreVisible(type));

        // Return true if other's changes are visible
        System.out.println ("othersUpdatesAreVisible: "+
                            dbmd.othersUpdatesAreVisible(type));

        // Return true if other's deletions are visible
        System.out.println ("othersDeletesAreVisible: "+
                            dbmd.othersDeletesAreVisible(type));

        // Return true if other's insertions are visible
        System.out.println ("othersInsertsAreVisible: "+
                            dbmd.othersInsertsAreVisible(type));

        // Return true if ResultSet.rowUpdated() is supported
        System.out.println ("updatesAreDetected: "+
                            dbmd.updatesAreDetected(type));

        // Return true if ResultSet.rowDeleted() is supported
        System.out.println ("deletesAreDetected: "+
                            dbmd.deletesAreDetected(type));

        // Return true if ResultSet.rowInserted() is supported
        System.out.println ("insertsAreDetected: "+
                            dbmd.insertsAreDetected(type));

        System.out.println ();
      }
    }

    // Close the connection
    conn.close();   
  }
}
