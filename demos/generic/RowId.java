/*
 * This sample demonstrates Oracle feature ROWID. Each row
 * in an Oracle table is assigned a ROWID that corresponds
 * to the physical address of a row. ROWID 
 * 1. enables direct access to a row, so gain performance benefits.
 * 2. performs as an unique ID for a row when there is no primary key
 *
 * note: execute book.sql in advance
 */

// You need to import the java.sql package to use JDBC
import java.sql.*;
import java.util.*;

// You need to import oracle.jdbc.* and ROWID in order to use the
// Oracle extensions.
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;
import oracle.sql.ROWID;


class RowId
{
  public static void main (String args [])
       throws SQLException
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

    // prepare table with newer entries
    setupTable(conn);
 
    Statement stmt = conn.createStatement ();
    ResultSet rset = stmt.executeQuery(
      "select rowid, name, isbn, category, premier_day, sold_amount, instock, purchase_plan"
      + " from books");

    ROWID [] ids = new ROWID[20]; 
    int      index = 0;
    Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    cal.set(Calendar.YEAR, (cal.get(Calendar.YEAR) - 1) );
    java.util.Date one_year_before = cal.getTime();

    ROWID    book_rowid = null;
    String   book_name = null;
    int      book_isbn = 0;
    String   book_category = null;
    java.util.Date     book_date = null;
    int      book_sold = 0;
    int      book_stock = 0;
    int      book_purchase = 0;

    System.out.println("Following books are well-sold new books:");
    System.out.println("book name\t\t\tISBN\tcategory\n");

    while ( rset.next() )
    {
       book_rowid = ((OracleResultSet)rset).getROWID(1);
       book_name = rset.getString (2);
       book_isbn = rset.getInt (3);
       book_category = rset.getString(4);
       book_date = rset.getDate(5);
       book_sold = rset.getInt (6);
       book_stock = rset.getInt (7);
       book_purchase = rset.getInt (8);
      
 
       // find well-selling new books, save their rowid into ids
       if ( (book_date.after (one_year_before)) &&
            (book_purchase == 0) && (book_sold >= 7*book_stock) )
       {
          if (  ( (book_category.equals("liguistics")) &&
                  (book_stock <= 100) ) 
                ||
                ( ((book_category.equals("romance") ||
                    book_category.equals("health")) &&
                    (book_stock <= 200) ) )
                ||
                (  book_category.equals("education") &&
                   (book_stock <= 100) )
                ||
                ( book_category.equals("suspense") &&
                  (book_stock <= 300) ) ) 
          {
             System.out.println(book_name + "\t\t" +
                                book_isbn + "\t" +
                                book_category);
             ids[index] = book_rowid;
             ++index;
          }
       }
    }

    // plan to purchase 500 more for those well-selling books.
    // use rowid do update.
    PreparedStatement pstmt = conn.prepareStatement(
      "update books set purchase_plan = ? where rowid = ?");
    ((OraclePreparedStatement)pstmt).setExecuteBatch(20);

    --index;
    while ( index >= 0 )
    {
       pstmt.setInt (1, 500);
       ((OraclePreparedStatement)pstmt).setROWID (2, ids[index]);
       pstmt.executeUpdate();
       --index;
    }
    ((OraclePreparedStatement)pstmt).sendBatch();
 
    rset.close();
    stmt.close();
    pstmt.close();
    conn.close();
  }

  private static void setupTable(Connection con) throws SQLException
  {
    PreparedStatement ps = con.prepareStatement(
     "insert into books values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

    java.util.Date utildate = new java.util.Date();
    java.sql.Date sqldate = new java.sql.Date (utildate.getTime());

    // add an entry of Dan Brown's "The Da Vinci Code"
    ps.setString(1, "The Da Vinci Code");
    ps.setString(2, "Dan Brown");
    ps.setInt(3, 385504209);      // ISBN
    ps.setString(4, "suspense");   // category
    ps.setInt(5, 1);               // edition
    ps.setDate(6, sqldate);
    ps.setInt(7, 10000);            // copies sold 
    ps.setInt(8, 200);             // in stock
    ps.setInt(9, 0);               // purchase plan
    ps.executeUpdate();

    // add an entry of Bart King's "The Big Book of Boy Stuff"
    ps.setString(1, "The Big Book of Boy Stuff");
    ps.setString(2, "Bark King, Chris Sabatino");  
    ps.setInt(3, 1586853333);      // ISBN
    ps.setString(4, "education");   // category
    ps.setInt(5, 1);               // edition
    ps.setDate(6, sqldate);
    ps.setInt(7, 3000);            // copies sold 
    ps.setInt(8, 100);             // in stock
    ps.setInt(9, 0);               // purchase plan
    ps.executeUpdate();
    ps.close();
    ps = null;
  }
}
