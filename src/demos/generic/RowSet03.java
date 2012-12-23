/**
 *  This sample exhibits the various duplication properties of 
 *  OracleCachedRowSet viz., 
 *    - cloning the rowset.
 *    - copying the rowset.
 *    - creating a shared copy of the rowset. (Only data in the cache is 
 *      shared) 
 *
 * Please use jdk1.2 or later version
 *
 *  useage: need to run a script "rowset.sql" to create a
 *          reservation table.
 *
 *  @author Ashok.Shivarudraiah
 */
import java.sql.SQLException;
import javax.sql.RowSet;
import oracle.jdbc.rowset.OracleCachedRowSet;

public class RowSet03 {
  public static void main (String []args)
  {
    String url = "java:oracle:oci8:@";
    try
    {
      OracleCachedRowSet crowset = new OracleCachedRowSet ();
      try
      {
        String url1 = System.getProperty ("JDBC_URL");
        if (url1 != null)
          url = url1;
      }catch (Exception ea) { }
      crowset.setUrl (url);
      crowset.setUsername ("hr");
      crowset.setPassword ("hr");
      crowset.setCommand ("SELECT seatno, tdate, name, class FROM reservation");
      crowset.execute ();

      System.out.println ("Displaying all the rows before cloning");
      System.out.println ("Seat no  Travel Date  Name   Class");
      System.out.println ("-------  ------ ----  ----   -----");
      while (crowset.next ())
      {
        printRow (crowset);
      }// end of while (crowset.next ())
      crowset.beforeFirst ();

      /*
       * Create a clone of rowset.  Updation of the copy does not affect 
       * the original copy.
       */
      try
      {
        System.out.println ("");
        System.out.println ("Cloning the rowset");
        OracleCachedRowSet cloneRowSet = (OracleCachedRowSet) crowset.clone ();
        System.out.println ("Displaying all the rows from the clone");
        System.out.println ("Seat no  Travel Date  Name   Class");
        System.out.println ("-------  ------ ----  ----   -----");
        while (cloneRowSet.next ())
        {
          printRow (cloneRowSet);
        }// end of while (cloneRowSet.next ())
        cloneRowSet.close ();
      }catch (CloneNotSupportedException ea)
      {
        System.out.println ("ERROR: While cloning");
        ea.printStackTrace ();
      }

      /*
       * Create a copy of rowset.  This is equivalent to a cloned object.
       * Updation of the copy does not affect the original copy.
       */
      System.out.println ("");
      System.out.println ("Copying the rowset");
      OracleCachedRowSet copyRowSet = crowset.createCopy ();
      System.out.println ( "Displaying all the rows from the copy of "
        +"the rowset");
      System.out.println ("Seat no  Travel Date  Name   Class");
      System.out.println ("-------  ------ ----  ----   -----");
      while (copyRowSet.next ())
      {
        printRow (copyRowSet);
      }// end of while (copyRowSet.next ())
      copyRowSet.close ();

      /*
       * Create a shared rowset.  Updation of the shared rowset updates the 
       * original copy also.  Where as the local properties like cursor position
       * is not shared between the copies.
       */
      System.out.println ("");
      System.out.println ("Creating a shared copy of rowset");
      OracleCachedRowSet sharedRowSet = 
         (OracleCachedRowSet)crowset.createShared ();
      System.out.println (
        "Displaying all the rows from the shared copy of the rowset");
      System.out.println ("Seat no  Travel Date  Name   Class");
      System.out.println ("-------  ------ ----  ----   -----");
      while (sharedRowSet.next ())
      {
        printRow (sharedRowSet);
      }// end of while (sharedRowSet.next ())
      sharedRowSet.close ();

      crowset.close ();
    }catch (SQLException ea)
    {
      ea.printStackTrace ();
    }
  }// end of main (String[])

  public static void printRow (RowSet rowset)
    throws SQLException
  {
    System.out.println (rowset.getInt (1) +"      " +rowset.getDate (2)
      +"   " +rowset.getString (3) +"  " +rowset.getString (4));
  }// end of printRow (RowSet)

}// end of class RowSet03 

