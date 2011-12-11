/**
 * This is the SQLData customized class of SQL object type 
 * "ParttimeStudent_t".
 * This class is used in Inheritance2.java
 *
 * Please use jdk1.2 or later version and classes12.zip
 */
import java.sql.*;

public class ParttimeStudent_sqldata extends Student_sqldata
{
  public int numHours;
  
  public ParttimeStudent_sqldata () { super(); }

  public ParttimeStudent_sqldata (String sql_type,
                                  int ssn, String name, String address,
                                  int deptid, String major,
                                  int numHours)
  {
    super (sql_type, ssn, name, address, deptid, major);
    this.numHours = numHours;
  }  
  
  public void readSQL(SQLInput stream, String typeName) throws SQLException
  {
    sql_type = typeName;
    super.readSQL (stream, typeName);    // read supertype attributes
    numHours = stream.readInt();
  }

  public void writeSQL(SQLOutput stream) throws SQLException
  {
    super.writeSQL (stream);             // write supertype attributes
    stream.writeInt (numHours);
  }

  public String toString ()
  {
    return sql_type+" = "+ssn+", "+name+", "+address+", "+deptid+
      ", "+major+" "+numHours;
  }
}
