/*
 * This example shows the access of nested collections. 
 *
 * This example contains two dependency Java files: Satellite_j.java and 
 * Planet_j.java. You need to compile both dependency classes before you
 * run this example.
 *
 * This example creates the following user defined datatypes --
 *
 *   SATELLITE_T : a object type that is customized mapped to 
 *                 "Satellite_j" Java class.
 *   NT_SAT_T    : a nested table type that contains SATELLITE_T 
 *                 elements. 
 *   PLANET_T    : a object type that contains NT_SAT_T. This type 
 *                 is customized mapped to "Planet_j" Java class.
 *   NT_PL_T     : a two layers nested table that contains PLANET_T
 *                 elements.
 *
 * The example will first create the above user defined datatypes, 
 * insert a NT_PL_T object into table stars in the database, and then
 * select the value from the database.
 */

import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;

public class NestedCollection
{
  public static void main (String args []) throws Exception
  {
    String urlDefault = "jdbc:oracle:oci8:@";
    String url = null;
    try 
    {
      url = System.getProperty("JDBC_URL");
    } 
    catch (Exception e) 
    {
      // If there is any security exception, ignore it
      // and use the default
    }
    finally
    {
      if (url == null) url = urlDefault;
    }

    // Create a OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Connect to the database
    Connection conn = ods.getConnection ();

    // It's faster when auto commit is off
    conn.setAutoCommit (false);

    // Create the nested table types and populate the database table
    createTables (conn);   

    // Populate the customized mapping entries
    java.util.Map map = conn.getTypeMap ();
    map.put ("HR.SATELLITE_T", Class.forName ("Satellite_j"));
    map.put ("HR.PLANET_T", Class.forName ("Planet_j"));

    // Insert a nested collection
    insert (conn);

    // Select the nested collection
    select (conn);

    // Remove the data previously created in the database
    cleanup (conn);

    // Disconnect
    conn.close ();
  }

  /**
   * Create the database data structure to be used by this example.
   */ 
  public static void createTables (Connection conn) throws SQLException
  {
    // Remove database data created by this example
    cleanup (conn);

    // Create a Statement
    Statement stmt = conn.createStatement ();

    // Create a database object type "satellite_t"
    stmt.execute ("CREATE TYPE satellite_t AS OBJECT"+
                  "(                                "+
                  "   name VARCHAR2(255),           "+
                  "   diameter NUMBER               "+
                  ")");

    // Create a nested table of "satellite_t"
    stmt.execute ("CREATE TYPE nt_sat_t AS TABLE OF satellite_t");

    // Create a database object type "planet_t".
    stmt.execute ("CREATE TYPE planet_t AS OBJECT   "+
                  "(                                "+
                  "  name VARCHAR2(255),            "+
                  "  mass NUMBER,                   "+
                  "  satellites nt_sat_t            "+
                  ")");

    // Create a multi level collection "nt_pl_t"
    stmt.execute ("CREATE TYPE nt_pl_t as TABLE OF planet_t");

    // Create a database table
    stmt.execute ("CREATE TABLE stars (             "+
                  "  name VARCHAR2(20),             "+
                  "  age NUMBER,                    "+
                  "  planets nt_pl_t)               "+
                  "NESTED TABLE planets STORE AS planets_tab "+
                  "  (NESTED TABLE satellites STORE AS satellites_tab)");

    // Commit the creations
    stmt.execute ("commit");

    // Close the statement
    stmt.close ();
  }

  /**
   * Remove database data created by this example
   */
  public static void cleanup (Connection conn) throws SQLException
  {
    // Create a Statement
    Statement stmt = conn.createStatement ();

    try
    {
      stmt.execute ("drop table stars");
    }
    catch (SQLException e)
    {
      // An exception could be raised here if the table did not exist already.
    }

    try { stmt.execute ("drop type nt_pl_t"); } catch (SQLException e) {}
    try { stmt.execute ("drop type planet_t"); } catch (SQLException e) {}
    try { stmt.execute ("drop type nt_sat_t"); } catch (SQLException e) {}
    try { stmt.execute ("drop type satellite_t"); } catch (SQLException e) {}
   
    // Close the statement
    stmt.close ();
  }

  /**
   * Insert a "NT_PL_T" into the "stars" table. "NT_PL_T" is a two layers
   * nested table.
   */ 
  public static void insert (Connection conn) throws SQLException
  {
    // Prepare the collection elements of "NT_SAT_T"
    Satellite_j[] neptuneSatellites =  
    { 
      new Satellite_j ("Proteus", 67), 
      new Satellite_j ("Triton", 82) 
    };

    // Prepare another collection elements of "NT_SAT_T"
    Satellite_j[] jupiterSatellites = 
    {
      new Satellite_j ("Callisto", 97),
      new Satellite_j ("Ganymede", 22)
    };

    // Prepare the collection elements of "NT_PL_T"
    Object[] sunPlanets = 
    {
      new Planet_j ("Neptune", 10, neptuneSatellites, conn),
      new Planet_j ("Jupiter", 189, jupiterSatellites, conn)
    };

    // Obtain the type descriptor of "NT_PL_T"
    ArrayDescriptor planetsType = 
      ArrayDescriptor.createDescriptor ("NT_PL_T", conn);

    // Create the planets (of type "NT_PL_T") of Sun
    ARRAY planets = new ARRAY (planetsType, conn, sunPlanets);

    // Prepare the insert statement
    PreparedStatement pstmt = 
      conn.prepareStatement ("insert into stars values ('Sun', 23, ?)");

    // Bind the array object
    pstmt.setObject (1, planets, OracleTypes.ARRAY);

    // Execute the insertion and print the update count
    System.out.println ("Insert "+pstmt.executeUpdate ()+" row");

    // Close the PreparedStatment
    pstmt.close ();
  }

  /**
   * Select the "NT_PL_T" inserted by "insert()", and list
   * the collection value.
   */
  public static void select (Connection conn) throws SQLException
  {
    // Create a statement
    Statement stmt = conn.createStatement ();

    // Execute the query
    ResultSet rset = stmt.executeQuery ("select * from stars");

    // Iterate the result set
    while (rset.next ())
    {
      System.out.println (rset.getString(1)+" Age="+rset.getFloat(2));
      System.out.println ("============================");

      // Get the two layers nested table "NT_PL_T"
      ARRAY planets = (ARRAY) rset.getObject (3);

      // Get the "NT_PL_T" elements
      Object[] planetsElems = (Object[]) planets.getArray ();

      // Print the "NT_PL_T" elements
      for (int i=0; i<planetsElems.length; i++)
      {
        // Planet_j.toString() contains the logic to print
        // the inner nested table ("NT_SAT_T") elements
        System.out.println ((Planet_j)planetsElems[i]);
      }
    }

    // Close the result set
    rset.close ();

    // Close the statement
    stmt.close ();
  }
}

