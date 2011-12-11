/**
 *  This sample exhibits the insertion, updation, deletion of a row in the
 *  rowset and also displays the call for Synchronizing the database with the
 *  rowset.
 *
 *  Please use jdk1.2 or later version
 *
 *  useage: need to run a script "rowset.sql" to create a
 *          reservation table.
 *
 *  @author Ashok.Shivarudraiah
 */
import java.sql.SQLException;
import java.sql.Date;
import javax.sql.RowSet;
import oracle.jdbc.rowset.OracleCachedRowSet;

public class RowSet02 {
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

      System.out.println ("Displaying all the rows before modification");
      System.out.println ("Seat no  Travel Date  Name   Class");
      System.out.println ("-------  ------ ----  ----   -----");
      while (crowset.next ())
      {
        printRow (crowset);
      }// end of while (crowset.next ())

      System.out.println ("Write enabling the rowset");
      crowset.setReadOnly (false);

      System.out.println ("");
      crowset.beforeFirst ();
      if (crowset.absolute (2))
      {
        System.out.println ("Updating passenger's class at 102 from Economy to" 
          +" Business");
        crowset.updateString (4, "Business");
        crowset.updateRow ();
        System.out.println ("Row updated");
      }

      System.out.println ("");
      crowset.beforeFirst ();
      System.out.println ("Inserting a new passenger with seat no 107");
      crowset.moveToInsertRow ();
      crowset.updateInt (1, 107);
      crowset.updateDate (2, new Date (975915381774L));
      crowset.updateString (3, "Pluto");
      crowset.updateString (4, "Business");
      crowset.insertRow ();
      System.out.println ("New row inserted");

      System.out.println ("");
      crowset.beforeFirst ();
      if (crowset.absolute (6))
      {
        System.out.println ("Deleting the passenger's entry at seatno: " 
          +crowset.getInt (1));
        crowset.deleteRow ();
        System.out.println ("Row deleted");
      }

      System.out.println ("");
      System.out.println ("Displaying all the rows after modification");
      System.out.println ("Seat no  Travel Date  Name   Class");
      System.out.println ("-------  ------ ----  ----   -----");
      crowset.beforeFirst ();
      while (crowset.next ())
      {
        printRow (crowset);
      }// end of while (crowset.next ())

      System.out.println ("");
      System.out.println ("Synchronizing the rowset with the database");
      crowset.acceptChanges ();
      crowset.close ();

      System.out.println ("Closing the old rowset and re-creating the rowset");
      crowset = new OracleCachedRowSet ();
      crowset.setUrl ("java:oracle:oci8:@");
      crowset.setUsername ("hr");
      crowset.setPassword ("hr");
      crowset.setCommand ("SELECT seatno, tdate, name, class FROM reservation");
      crowset.execute ();

      System.out.println ("Displaying all the rows from the re-read rowset");
      System.out.println ("Seat no  Travel Date  Name   Class");
      System.out.println ("-------  ------ ----  ----   -----");
      while (crowset.next ())
      {
        printRow (crowset);
      }// end of while (crowset.next ())

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

}// end of class RowSet02 

