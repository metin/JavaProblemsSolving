/*
 * This is a sub class of Person_sqldata, and maps to
 * a user defined SQL data type -- Student_t.
 *
 * This class is used in class Inheritance2
 * Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;

public class Student_sqldata extends Person_sqldata
{
  public int deptid;
  public String major;
  
  public Student_sqldata () { super(); }

  public Student_sqldata (String sql_type,
                          int ssn, String name, String address,
                          int deptid, String major)
  {
    super (sql_type, ssn, name, address);
    this.deptid = deptid;
    this.major = major;
  }
                            
  public void readSQL(SQLInput stream, String typeName) throws SQLException
  {
    sql_type = typeName;
    super.readSQL (stream, typeName);    // read supertype attributes
    deptid = stream.readInt();
    major = stream.readString();
  }

  public void writeSQL(SQLOutput stream) throws SQLException
  {
    super.writeSQL (stream);             // write supertype attributes
    stream.writeInt (deptid);
    stream.writeString (major);
  }

  public String toString ()
  {
    return sql_type+" = "+ssn+", "+name+", "+address+" "+deptid+" "+major;
  }
}
