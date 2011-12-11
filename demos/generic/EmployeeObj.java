/**
 * filename: EmployeeObj.java
 *
 * This class is used in SQLDataExample.java
 * Please use jdk1.2 or later version and classes12.zip
 **/

import java.sql.*;

public class EmployeeObj implements SQLData
{
  private String sql_type;

  public String empName;
  public int empNo;

  public EmployeeObj()
  {
  }

  public EmployeeObj (String sql_type, String empName, int empNo)
  {
    this.sql_type = sql_type;
    this.empName = empName;
    this.empNo = empNo;
  }  

  ////// implements SQLData //////
 
  public String getSQLTypeName() throws SQLException
  { 
    return sql_type; 
  } 
 
  public void readSQL(SQLInput stream, String typeName)
    throws SQLException
  {
    sql_type = typeName;
 
    empName = stream.readString();
    empNo = stream.readInt();
  }
 
  public void writeSQL(SQLOutput stream)
    throws SQLException
  { 
    stream.writeString(empName);
    stream.writeInt(empNo);
  }
}
