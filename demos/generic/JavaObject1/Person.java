/*
 * This shows how to use SQLData to create a customized
 * java data type that maps to user defined SQL type
 * PERSON_T
 *
 * It is used in file JavaObject1.java
 * Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;

public class Person implements SQLData
{
  private String sql_type = "HR.PERSON_T";
  public int ssn;
  public String name;
  public Address address;

  public Person () {}

  public Person (int ssn, String name, Address address)
  {
    this.ssn = ssn;
    this.name = name;
    this.address = address;
  }

  public String getSQLTypeName() throws SQLException
  { 
    return sql_type; 
  } 
 
  public void readSQL(SQLInput stream, String typeName)
    throws SQLException
  {
    sql_type = typeName;
    ssn = stream.readInt();
    name = stream.readString();
    address = (Address) stream.readObject();
  }
 
  public void writeSQL(SQLOutput stream) throws SQLException
  {     
    stream.writeInt (ssn);
    stream.writeString (name);
    ((oracle.sql.OracleSQLOutput)stream).writeObject (address);
  }

  public String toString ()
  {
    return sql_type+" : "+ssn+", "+name+", ["+address+"]";
  }
}


