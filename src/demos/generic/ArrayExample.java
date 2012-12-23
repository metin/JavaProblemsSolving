/*
  This demonstrates how to make array of arrays 
  of different length.

  It 1. uses VARRAY to create an array type of different
        length.
     2. creates a table to store data of above array 
        data type
     3. shows how to create array objects and insert
        them into database table in 2 different ways.
        one is to use SQL facility, another is to use
        ArrayDescriptor 

  Please use jdk1.2 or later version, classes12.zip. 
  This will create and populate a varray_table of 
  user defined array data type into database.
*/

import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.oracore.Util;
import oracle.jdbc.*;
import oracle.jdbc.pool.OracleDataSource;
import java.math.BigDecimal;


public class ArrayExample
{
  public static void main (String args[])
    throws Exception
  {
    // Create an OracleDataSource instance explicitly
    OracleDataSource ods = new OracleDataSource();

    // The sample retrieves an varray of type "NUM_VARRAY",
    // materializes the object as an object of type ARRAY.
    // A new ARRAY is then inserted into the database.

    String url = "jdbc:oracle:oci8:@";
    try {
      String url1 = System.getProperty("JDBC_URL");
      if (url1 != null)
        url = url1;
    } catch (Exception e) {
      // If there is any security exception, ignore it
      // and use the default
    }

    // Set the user name, password, and the url
    ods.setUser("hr");
    ods.setPassword("hr");
    ods.setURL(url);

    // Connect to the database
    Connection conn = ods.getConnection();

    // It's faster when auto commit is off
    conn.setAutoCommit (false);

    // Create a Statement
    Statement stmt = conn.createStatement ();

    try
    {
      stmt.execute ("DROP TABLE varray_table");
      stmt.execute ("DROP TYPE num_varray");     
    }
    catch (SQLException e)
    {
      // the above drop statements will throw exceptions
      // if the types and tables did not exist before. Just ingore it.
    }
 
    stmt.execute ("CREATE TYPE num_varray AS VARRAY(10) OF NUMBER(12, 2)");
    stmt.execute ("CREATE TABLE varray_table (col1 num_varray)");
    stmt.execute ("INSERT INTO varray_table VALUES (num_varray(100, 200))");

    ResultSet rs = stmt.executeQuery("SELECT col1 FROM varray_table");
    showResultSet (rs);

    //now insert a new row

    // create a new ARRAY object    
    int elements[] = { 300, 400, 500, 600 };
    ArrayDescriptor desc = ArrayDescriptor.createDescriptor
                                           ("NUM_VARRAY", conn);
    ARRAY newArray = new ARRAY(desc, conn, elements);
    
    PreparedStatement ps = 
      conn.prepareStatement ("insert into varray_table values (?)");
    ((OraclePreparedStatement)ps).setARRAY (1, newArray);

    ps.execute ();

    rs = stmt.executeQuery("SELECT col1 FROM varray_table");
    showResultSet (rs);

    // Close all the resources
    rs.close();
    ps.close();
    stmt.close();
    conn.close();

  }   

  public static void showResultSet (ResultSet rs)
    throws SQLException
  {       
    int line = 0;
    while (rs.next())
    {
      line++;
      System.out.println("Row "+line+" : ");
      ARRAY array = ((OracleResultSet)rs).getARRAY (1);

      System.out.println ("Array is of type "+array.getSQLTypeName());
      System.out.println ("Array element is of type code "
                          + array.getBaseType()); 
      System.out.println ("Array is of length "+array.length());

      // get Array elements            
      BigDecimal[] values = (BigDecimal[]) array.getArray();

      for (int i=0; i<values.length; i++) 
      {
        BigDecimal value = (BigDecimal) values[i];
        System.out.println(">> index "+i+" = "+value.intValue());
      }
    }
  }     
}

