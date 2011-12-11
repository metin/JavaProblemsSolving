/*
 * This shows how to use ORAData and ORADataFactory to
 * create user defined Java data type that maps to a
 * user defined SQL data type Person_t in the database 
 *
 * This class is used by Employee.java and Inheritance1.java
 * Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;
import oracle.sql.*;

public class Person implements ORAData, ORADataFactory
{
  static final Person _factory = new Person ();

  public NUMBER ssn;
  public CHAR name;
  public CHAR address;

  public static ORADataFactory getFactory()
  {
    return _factory;
  }

  public Person () {}

  public Person (NUMBER ssn, CHAR name, CHAR address)
  {
    this.ssn = ssn;
    this.name = name;
    this.address = address;
  }

  public Datum toDatum(Connection c) 
    throws SQLException
  {
    StructDescriptor sd = 
      StructDescriptor.createDescriptor("HR.PERSON_T", c);
    Object [] attributes = { ssn, name, address };
    return new STRUCT(sd, c, attributes);
  }

  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null;
    Object [] attributes = ((STRUCT) d).getOracleAttributes();
    return new Person ((NUMBER) attributes[0],
                                   (CHAR) attributes[1],
                                   (CHAR) attributes[2]);
  }

  public String toString () 
  {
    try
    {
      return "HR.PERSON_T: ssn="+ssn.intValue()+
        " name="+name.stringValue()+
        " address="+address.stringValue();
    }
    catch (SQLException e) 
    {
      // ignore the error
    }
    return "some error happened";
  }
}
