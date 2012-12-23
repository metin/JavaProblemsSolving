/* 
 * This sample demonstrates how to make PL/SQL calls
 * with index-by table parameters
 *
 * Please use jdk1.2 or later version
 */

// You need to import java.sql, oracle.sql and oracle.jdbc packages to use
import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;
import oracle.sql.*;

class PLSQLIndexTab
{
  public static void main (String args [])
       throws SQLException
  {

    String [] plSqlIndexArrayIn = {"string1","string2","string3"};
    int currentLen = plSqlIndexArrayIn.length;
    int maxLen = currentLen;
    int elementMaxLen = 20;

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

    // Create the procedures which use Index-by Table as IN/OUT parameters
    createProc_Func(conn);

    // Call a procedure with an IN parameter
    System.out.println ("Call a procedure with an IN parameter");
    OracleCallableStatement cs = 
        (OracleCallableStatement) conn.prepareCall
                                  ("begin proc_in (?, ?); end;");

    // Use setPlsqlIndexTable() to set the Index-by Table parameter
    cs.setPlsqlIndexTable (1, plSqlIndexArrayIn, maxLen, 
                               currentLen, OracleTypes.VARCHAR,
                               elementMaxLen);

    // Register OUT paramater
    cs.registerOutParameter (2, Types.CHAR);

    // Call the procedure
    cs.execute ();

    // Display the Status
    System.out.println ("Status = " + cs.getString (2));

    // Call a procedure with an OUT parameter
    System.out.println ("Call a procedure with an OUT parameter");
    cs = (OracleCallableStatement) conn.prepareCall
                                        ("begin proc_out (?); end;");

    // Use setPlsqlIndexTable() to set the Index-by Table parameter
    cs.registerIndexTableOutParameter (1, maxLen,
                                          OracleTypes.VARCHAR,
                                          elementMaxLen);

    // Call the procedure
    cs.execute ();

    // Display the OUT value
    Datum[] val = cs.getOraclePlsqlIndexTable (1);
    for (int i = 0; i < val.length; i++)
        System.out.println ("Value = " + val[i].stringValue());

    // Call a procedure with IN/OUT parameter
    System.out.println ("Call a procedure with IN/OUT parameter");

    cs = (OracleCallableStatement) conn.prepareCall
                                   ("begin proc_inout (?, ?); end;");

    // Use setPlsqlIndexTable() to set the Index-by Table parameter
    cs.setPlsqlIndexTable (1, plSqlIndexArrayIn, maxLen, 
                               currentLen, OracleTypes.VARCHAR,
                               elementMaxLen);

    // Register OUT paramater
    cs.registerIndexTableOutParameter (1, maxLen,
                                          OracleTypes.VARCHAR,
                                          elementMaxLen);
    cs.registerOutParameter (2, Types.CHAR);

    // Call the procedure
    cs.execute ();

    // Display the Status
    System.out.println ("Status = " + cs.getString (2));

    // Display the OUT value
    val = cs.getOraclePlsqlIndexTable (1);
    for (int i = 0; i < val.length; i++)
        System.out.println ("Value = " + val[i].stringValue());

    // Call the Function 
    System.out.println ("Call the function");

    cs = (OracleCallableStatement) conn.prepareCall
                                   ("begin ? := func (?); end;");

    // Use setPlsqlIndexTable() to set the Index-by Table parameter
    cs.setPlsqlIndexTable (2, plSqlIndexArrayIn, maxLen,
                               currentLen, OracleTypes.VARCHAR,
                               elementMaxLen);
  
    // Register OUT paramater
    cs.registerIndexTableOutParameter (1, maxLen, OracleTypes.VARCHAR,
                                          elementMaxLen);

    // Call the procedure
    cs.execute ();

    val = cs.getOraclePlsqlIndexTable (1);
    for (int i = 0; i < val.length; i++)
        System.out.println ("Value = " + val[i].stringValue());

    // Close the Callable Statement 
    cs.close(); 

    // Dump the contents of the demo_tab
    System.out.println ("Dump the demo_tab table");
    dumpTable(conn);

    // Clean up the schema
    cleanup(conn);

    // Close the connection
    conn.close();   
  }

  private static void createProc_Func (Connection conn)
      throws SQLException
  {
    // Cleanup the schema
    cleanup(conn);

    // Create a Statement
    Statement stmt = conn.createStatement ();

    // Create the Table
    stmt.execute("CREATE TABLE demo_tab (col1 VARCHAR2(20))");

    // Use PL/SQL to create the Package
    String plsql1 = "CREATE OR REPLACE PACKAGE pkg AS " +
                    "  TYPE indexByTab IS TABLE OF VARCHAR2(20) " +
                    "       INDEX BY BINARY_INTEGER; " +
                    "END;";

    stmt.execute(plsql1);

    // Create a procedure to use the Index-by Table as IN paramater
    String plsql2 = "CREATE OR REPLACE PROCEDURE proc_in " +
                    "       (p1 IN pkg.indexByTab, status OUT VARCHAR2) IS " +
                    "BEGIN " +
                    "  FOR i in 1..3 LOOP " +
                    "    INSERT INTO demo_tab VALUES (p1(i)); " +
                    "  END LOOP; " +
                    "  IF ((p1(1)='string1') AND (p1(2)='string2') " +
                    "                        AND (p1(3)='string3')) " +
                    "     THEN status := 'Values passed in correctly'; " +
                    "  ELSE " +
                    "      status := 'Values passed in are incorrect'; " +
                    "  END IF; " +
                    "END;";

    stmt.execute(plsql2);

    // Create a procedure to use the Index-by Table as OUT paramater
    String plsql3 = "CREATE OR REPLACE PROCEDURE proc_out " +
                    "          (p1 OUT pkg.indexByTab) IS " +
                    "BEGIN " +
                    "  p1(1) := 'string1'; " +
                    "  p1(2) := 'string2'; " +
                    "  p1(3) := 'string3'; " +
                    "END;";

    stmt.execute(plsql3);

    // Create a procedure to use the Index-by Table
    // as both IN and OUT paramater
    String plsql4 = "CREATE OR REPLACE PROCEDURE proc_inout " +
                    "       (p1 IN OUT pkg.indexByTab, status OUT VARCHAR2) IS " +
                    "BEGIN " +
                    "  FOR i in 1..3 LOOP " +
                    "    INSERT INTO demo_tab VALUES (p1(i)); " +
                    "  END LOOP; " +
                    "  IF ((p1(1)='string1') AND (p1(2)='string2') " +
                    "                        AND (p1(3)='string3')) " +
                    "     THEN status := 'Values passed in correctly'; " +
                    "  ELSE " +
                    "      status := 'Values passed in are incorrect'; " +
                    "  END IF; " +
                    "  p1(1) := 'string4'; " +
                    "  p1(2) := 'string5'; " +
                    "  p1(3) := 'string6'; " +
                    "END;";

    stmt.execute(plsql4);

    String plsql5 = "CREATE OR REPLACE FUNCTION func " +
                    "       (p1 pkg.indexByTab) RETURN pkg.indexByTab IS " +
                    "  n pkg.indexByTab; " +
                    "BEGIN " +
                    "  FOR i in 1..3 LOOP " +
                    "    INSERT INTO demo_tab VALUES (p1(i)); " +
                    "  END LOOP; " +
                    "  IF ((p1(1)='string1') AND (p1(2)='string2') " +
                    "                        AND (p1(3)='string3')) THEN " +
                    "     n(1) := 'p1(1) correct'; " +
                    "     n(2) := 'p1(2) correct'; " +
                    "     n(3) := 'p1(3) correct'; " +
                    "  ELSE " +
                    "     n(1) := 'p1(1) wrong'; " +
                    "     n(2) := 'p1(2) wrong'; " +
                    "     n(3) := 'p1(3) wring'; " +
                    "  END IF; " +
                    "  RETURN n; " +
                    "END;";

    stmt.execute(plsql5);

    // Close the statement
    stmt.close();
  }
 
  /**
   * Utility function to dump the contents of the "demo_tab" table
   */
  static void dumpTable (Connection conn) throws SQLException
  {
    Statement stmt = conn.createStatement ();
    ResultSet rset = stmt.executeQuery ("select col1 from demo_tab");
    while (rset.next ())
      System.out.println (rset.getString (1));
    rset.close();
    stmt.close();
  }

  /**
   * Cleanup data structures created in this example
   */
  static void cleanup (Connection conn) throws SQLException
  {
    Statement stmt = conn.createStatement ();

    try {
      stmt.execute ("DROP TABLE demo_tab");
    } catch (SQLException e) {}

    try {
      stmt.execute ("DROP PROCEDURE proc_in");
    } catch (SQLException e) {}

    try {
      stmt.execute ("DROP PROCEDURE proc_out");
    } catch (SQLException e) {}

    try {
      stmt.execute ("DROP PROCEDURE proc_inout");
    } catch (SQLException e) {}

    try {
      stmt.execute ("DROP PROCEDURE func");
    } catch (SQLException e) {}

    try {
      stmt.execute ("DROP PACKAGE pck");
    } catch (SQLException e) {}

    stmt.close ();
  }

}
