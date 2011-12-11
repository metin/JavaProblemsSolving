/* 
 * This sample demonstrates the registration and operation of
 * JDBC OCI application failover callbacks
 * 
 * Note: Before you run this sample, set up the following
 *       service in tnsnames.ora: 
 *       inst_primary=(DESCRIPTION=
 *             (ADDRESS=(PROTOCOL=tcp)(Host=hostname)(Port=1521))
 *             (CONNECT_DATA=(SERVICE_NAME=ORCL)
 *                           (FAILOVER_MODE=(TYPE=SELECT)(METHOD=BASIC))
 *             )
 *           )
 *       Please see Net8 Administrator's Guide for more detail about 
 *       failover_mode
 *
 * To demonstrate the the functionality, first compile and start up the sample,
 *    then log into sqlplus and connect /as sysdba. While the sample is still 
 *    running, shutdown the database with "shutdown abort;". At this moment, 
 *    the failover callback functions should be invoked. Now, the database can
 *    be restarted, and the interupted query will be continued.
 *
 * Please use jdk1.2 or later version
 */

// You need to import java.sql and oracle.jdbc packages to use
// JDBC OCI failover callback 

import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.*;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleOCIFailover;
import oracle.jdbc.pool.OracleDataSource;

public class OCIFailOver {

  static final String user = "hr";
  static final String password = "hr";

  static final String URL = "jdbc:oracle:oci8:@inst_primary"; 


  public static void main (String[] args) throws Exception {

    Connection conn = null;
    CallBack   fcbk= new CallBack();
    String     msg = null;
    Statement  stmt = null;
    ResultSet rset = null; 
 
    // Create a OracleDataSource instance and set properties
    OracleDataSource ods = new OracleDataSource();
    ods.setUser(user);
    ods.setPassword(password);
    ods.setURL(URL);

    // Connect to the database
    conn = ods.getConnection();

    // register TAF callback function
    ((OracleConnection) conn).registerTAFCallback(fcbk, msg);

    // Create a Statement
    stmt = conn.createStatement ();

    for (int i=0; i<30; i++) {
      // Select the NAMEs column from the EMPLOYEES table
      rset = stmt.executeQuery ("select FIRST_NAME, LAST_NAME from EMPLOYEES");

      // Iterate through the result and print the employee names
      while (rset.next ()) 
        System.out.println (rset.getString (1) + " " +
                            rset.getString (2));

      // Sleep one second to make it possible to shutdown the DB.
      Thread.sleep(1000);
    } // End for
 
    // Close the RseultSet
    rset.close();

    // Close the Statement
    stmt.close();

    // Close the connection
    conn.close();


  } // End Main()

} // End class jdemofo 


/*
 * Define class CallBack
 */
class CallBack implements OracleOCIFailover {
   
   // TAF callback function 
   public int callbackFn (Connection conn, Object ctxt, int type, int event) {

     /*********************************************************************
      * There are 7 possible failover event
      *   FO_BEGIN = 1   indicates that failover has detected a 
      *                  lost conenction and faiover is starting.
      *   FO_END = 2     indicates successful completion of failover.
      *   FO_ABORt = 3   indicates that failover was unsuccessful, 
      *                  and there is no option of retrying.
      *   FO_REAUTH = 4  indicates that a user handle has been re-
      *                  authenticated. 
      *   FO_ERROR = 5   indicates that failover was temporarily un-
      *                  successful, but it gives the apps the opp-
      *                  ortunity to handle the error and retry failover.
      *                  The usual method of error handling is to issue 
      *                  sleep() and retry by returning the value FO_RETRY
      *   FO_RETRY = 6
      *   FO_EVENT_UNKNOWN = 7  It is a bad failover event
      *********************************************************************/
     String failover_type = null;

     switch (type) {
         case FO_SESSION: 
                failover_type = "SESSION";
                break;
         case FO_SELECT:
                failover_type = "SELECT";
                break;
         default:
                failover_type = "NONE";
     }

     switch (event) {
      
       case FO_BEGIN:
            System.out.println(ctxt + ": "+ failover_type + " failing over...");
            break;
       case FO_END:
            System.out.println(ctxt + ": failover ended");
            break;
       case FO_ABORT:
            System.out.println(ctxt + ": failover aborted.");
            break;
       case FO_REAUTH:
            System.out.println(ctxt + ": failover.");
            break;
       case FO_ERROR:
            System.out.println(ctxt + ": failover error gotten. Sleeping...");
            // Sleep for a while 
            try {
              Thread.sleep(100);
            }
            catch (InterruptedException e) {
               System.out.println("Thread.sleep has problem: " + e.toString());
            }
            return FO_RETRY;
       default:
            System.out.println(ctxt + ": bad failover event.");
            break;
       
     }  

     return 0;

   }
}
