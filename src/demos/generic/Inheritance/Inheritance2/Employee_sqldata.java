/*
 * This is a sub class of Person_sqldata, and maps
 * to a user defined SQL data type -- Person_t
 * This class is used in Inheritance2.java
 *
 * Please use jdk1.2 or later version and classes12.zip
 */ 

import java.sql.*;

public class Employee_sqldata extends Person_sqldata
{
  public int empid;
  public String mgr;
  
  public Employee_sqldata () { super(); }

  public Employee_sqldata (String sql_type,
                          int ssn, String name, String address,
                          int empid, String mgr)
  {
    super (sql_type, ssn, name, address);
    this.empid = empid;
    this.mgr = mgr;
  }  
  
  public void readSQL(SQLInput stream, String typeName) throws SQLException
  {
    sql_type = typeName;
    super.readSQL (stream, typeName);    // read supertype attributes
    empid = stream.readInt ();
    mgr = stream.readString ();
  }

  public void writeSQL(SQLOutput stream) throws SQLException
  {
    super.writeSQL (stream);             // write supertype attributes
    stream.writeInt (empid);
    stream.writeString (mgr);
  }

  public String toString ()
  {
    return sql_type+" = "+ssn+", "+name+", "+address+", "+empid+", "+mgr;
  }

}
