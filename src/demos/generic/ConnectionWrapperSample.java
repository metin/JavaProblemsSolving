/*
 * This sample shows how to list all the names from the EMP table
 * first using a normal connection and then a wrapped connectiion
 * and then a deeply nested wrapper
 */

import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

public class ConnectionWrapperSample
{
  public static void main (String args [])
       throws SQLException
  {
    // Create an OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL("jdbc:oracle:oci8:@");
    OracleConnection conn =
      (OracleConnection)(ods.getConnection ());

    printEmployees( conn );

    // Now wrap the connection
    OracleConnectionWrapper wrappedConnection = 
        new OracleConnectionWrapper( conn );
    printEmployees( wrappedConnection);

    // Wrap it in may layers
    for( int i=0; i<20; i++ )
       wrappedConnection = 
            new OracleConnectionWrapper( wrappedConnection );
    printEmployees( wrappedConnection);


    conn.close();
  }

public static void printEmployees( oracle.jdbc.OracleConnection conn ) throws SQLException
  {
    Statement stmt = conn.createStatement ();
    ResultSet rset = stmt.executeQuery ("select LAST_NAME from EMPLOYEES");
    while (rset.next ())
      System.out.print(rset.getString(1) + " ");
    rset.close();
    stmt.close();
    System.out.println();
    System.out.println();
  }
}
