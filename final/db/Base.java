package db;

import java.sql.*;

public class Base {
  
  private String url = "jdbc:oracle:thin:@prophet.njit.edu:1521:course";
  private String user = "my67";
  private String password = "u3CwmgIn";

  protected Connection connection;
  
  public Base(){
    
  }
  
  protected void connect() throws SQLException{
    // Load the Oracle JDBC driver
    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

    // Connect to the database
    connection = DriverManager.getConnection (url, user, password);
  }
  
  public Statement getStatement() throws SQLException{
    if(connection == null)
      connect();
    return connection.createStatement ();
  }


  public String test() throws SQLException{

    Statement stmt = getStatement();
    ResultSet rset = stmt.executeQuery ("select SYSDATE from dual");
    
    String result = "";
    // Print the result
    while (rset.next ())
      result = rset.getString (1);

    // Close the RseultSet
    rset.close();

    // Close the Statement
    stmt.close();

    close();
    return result;
  }
  
  private void close() throws SQLException{
    connection.close();
  }
  
}
