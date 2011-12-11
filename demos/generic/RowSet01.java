/**
 *  This sample exhibits the scrollablity of OracleCachedRowSet.  
 *  @author Ashok.Shivarudraiah
 *
 *  Please use jdk1.2 or later version
 *
 *  useage: need to run a script "rowset.sql" to create a
 *          reservation table.
 */
import java.sql.SQLException;
import javax.sql.RowSet;
import oracle.jdbc.rowset.OracleCachedRowSet;
import oracle.jdbc.rowset.OracleRowSetListenerAdapter;

public class RowSet01 {
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

      System.out.println ("Displaying all the passengers in forward direction");
      System.out.println ("Seat no  Travel Date  Name   Class");
      System.out.println ("-------  ------ ----  ----   -----");
      while (crowset.next ())
      {
        printRow (crowset);
      }// end of while (crowset.next ())

      System.out.println ("");
      crowset.afterLast ();
      if (!crowset.isAfterLast ())
      {
        System.out.println ("ERROR: Could not go after the last row");
      }
      else
      {
        System.out.println (
         "Displaying all the passengers in reverse direction");
        System.out.println ("Seat no  Travel Date  Name   Class");
        System.out.println ("-------  ------ ----  ----   -----");
        while (crowset.previous ())
        {
          printRow (crowset);
        }// end of while (crowset.next ())
      }

      System.out.println ("");
      System.out.println ("Printing the 2rd row");
      if (crowset.absolute (2))
      {
        printRow (crowset);
      }

      System.out.println ("");
      System.out.println ("Printing the 2rd row from the present row");
      if (crowset.relative (2))
      {
        printRow (crowset);
      }
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

}// end of class RowSet01 

