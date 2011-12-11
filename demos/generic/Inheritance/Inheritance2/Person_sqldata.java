/**
 * This is the SQLData customized class of SQL object type 
 * "Person_t".
 *
 * It is used in file Inheritance2.java
 * Please use jdk1.2 or later version and classes12.zip
 */
import java.sql.*;

public class Person_sqldata implements SQLData
{
  String sql_type;
  public int ssn;
  public String name;
  public String address;

  public Person_sqldata () {}

  public Person_sqldata (String sql_type, 
                         int ssn, String name, String address)
  {
    this.sql_type = sql_type;
    this.ssn = ssn;
    this.name = name;
    this.address = address;
  }

  public String getSQLTypeName() throws SQLException { return sql_type; }

  public void readSQL(SQLInput stream, String typeName) throws SQLException
  {
    sql_type = typeName;
    ssn = stream.readInt();
    name = stream.readString();
    address = stream.readString();
  }

  public void writeSQL(SQLOutput stream) throws SQLException
  {
    stream.writeInt (ssn);
    stream.writeString (name);
    stream.writeString (address);
  }

  public String toString ()
  {
    return sql_type+" = "+ssn+", "+name+", "+address;
  }
}
