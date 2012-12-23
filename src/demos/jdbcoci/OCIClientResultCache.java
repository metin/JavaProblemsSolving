/* Demo of OCI Client Result Cache. 

See the Oracle Call Interface Programmer's Guide
"Client Result Cache" for details. 

Note that the only things needed to use this feature
are some database setup and specifying the result_cache
hint in the query. See the OCI manual for other ways
to control this. There is no special code in the Oracle
JDBC driver needed to support this feature.

Most of the code below is there to measure the effects of
adding the result_cache hint. The actual operation is easy.

See also Oracle Database Reference "About Dynamic Performance Views."

There are some database setup requirements for this demo. Please
request your database administrator to do the following:

1) CLIENT_RESULT_CACHE_SIZE must be set in database initialization
file and the database restarted. 100,000 is a good value for this demo.

2) hr schema must be installed. See the Oracle Database Sample Schemas Manual.

3) hr must have select rights on certain performance views:
    grant select on v_$system_parameter to hr;
    grant select on v_$mystat to hr;
    grant select on v_$statname to hr;
    grant select on v_$sqlarea to hr;

Note: v_$xxxx are views, the names v$xxxx below are public synonyms.
*/

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import java.util.Random;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class OCIClientResultCache
{
  static Random rand = new Random();
  static NumberFormat format = new DecimalFormat();
  static {format.setGroupingUsed( true ); }
  public static void main( String [] args ) throws Exception
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
   
    OracleDataSource ods = new OracleDataSource();
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    printSystemParameterValue( ods, "client_result_cache_size"  );

    String queryFragment =
      " d.department_name, l.city, c.country_name"
      +" from regions r, countries c, locations l, departments d"
      +" where r.region_id = c.region_id"
      +" and c.country_id = l.country_id"
      +" and l.location_id = d.location_id"
      +" and  r.region_name = 'Americas'";

    String plainQuery = "select " + queryFragment;
    String queryWithResultCacheHint =   "select /*+ result_cache */ " + queryFragment; 

    // As with any cache there are warm-up issues. Run multiple times.  
    runQueryAndPrintStatistics( plainQuery, ods);
    runQueryAndPrintStatistics( queryWithResultCacheHint, ods);
    runQueryAndPrintStatistics( plainQuery, ods);
    runQueryAndPrintStatistics( queryWithResultCacheHint, ods);
  }

  static void runQueryAndPrintStatistics(String queryBody, DataSource ds) throws Exception
  {
    System.out.println("Query: " + queryBody);

    Connection conn = ds.getConnection();

    String preambleCommentIdentifiesQueryInStats = "/*==" + rand.nextInt() + "==*/ ";
    String query  = preambleCommentIdentifiesQueryInStats + queryBody;

    int beginRoundTrips = getRoundTrips(conn);
    int endRoundTrips = getRoundTrips(conn);
    int roundTripsToGetRoundTrips = endRoundTrips - beginRoundTrips;
    beginRoundTrips = getRoundTrips(conn);
    int totalRows = 0;
    int totalChars = 0;
    long start = System.currentTimeMillis();
    ((OracleConnection)conn).setImplicitCachingEnabled(true);
    ((oracle.jdbc.OracleConnection)conn).setStatementCacheSize(10);

    for (int j=0; j<50; j++) {
      PreparedStatement  pstmt = conn.prepareStatement (query);
      ResultSet  rs = pstmt.executeQuery();
      while (rs.next()) {
        totalRows++;
        totalChars += (rs.getString(1)).length();
        totalChars += (rs.getString(2)).length();
        totalChars += (rs.getString(3)).length();
      }
      rs.close ();
      pstmt.close();
    }

    long stop = System.currentTimeMillis();
    long elapsed = stop - start;
    long rate = (elapsed > 0L ) ? ((long)(totalChars) * 1000L / elapsed) : 0L;
    endRoundTrips = getRoundTrips(conn);

    System.out.println("Count of Round Trips: " 
         + (endRoundTrips - beginRoundTrips - roundTripsToGetRoundTrips));
    printParseAndExecuteStats( preambleCommentIdentifiesQueryInStats, conn);
    System.out.println("Total rows: " + totalRows + " total return length: " + totalChars);
    System.out.println( "Time to run: " + (stop - start) + " milliseconds, rate: " 
       + format.format(rate) + " chars per seocnd" );
    conn.close();
    System.out.println();
  }

  static int getRoundTrips(Connection conn) throws SQLException
  {
    Statement stmt =  null;
    stmt = conn.createStatement();
    String sql = "select t1.value from v$mystat t1, v$statname t2 "
      + "where t1.statistic# = t2.statistic# and "
      + "t2.name = 'SQL*Net roundtrips to/from client' ";
    ResultSet rset = stmt.executeQuery (sql);
    rset.next();
    int ret = rset.getInt (1);
    stmt.close();
    return ret;
  }

  static void printParseAndExecuteStats (String preambleCommentIdentifiesQueryInStats, 
     Connection conn) throws SQLException
  {
    Statement stmt = conn.createStatement ();
    String sql = "select parse_calls, EXECUTIONS, fetches from v$sqlarea"
      +" WHERE sql_text like '" + preambleCommentIdentifiesQueryInStats +"%'";
    ResultSet rs = stmt.executeQuery (sql);
    if( rs.next () )
      System.out.println("Parse: " +rs.getInt (1) +", Execute: " + rs.getInt (2));
    else 
      System.out.println( "Query not found." );
    stmt.close();
  }

  static void printSystemParameterValue(DataSource ds, String name) throws SQLException
  {
    Connection conn = ds.getConnection();
    Statement stmt =  null;
    stmt = conn.createStatement();
    String sql = "select value from v$system_parameter where name  = '" + name + "'";
    ResultSet rset = stmt.executeQuery (sql);
    rset.next();
    System.out.println( name + ": " +  rset.getString(1) );
    stmt.close();
    conn.close();
  }
}
