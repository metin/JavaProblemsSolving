/*
 * This is a sub class of Person. It creates
 * a user defined Java data type Student that
 * maps to a user defined SQL data type Student_t
 * in the database. 
 *
 * This class is used by Inheritance1.java
 * Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;
import oracle.sql.*;

public class Student extends Person
{
  static final Student _factory = 
  new Student ();

  public NUMBER deptid;
  public CHAR major;

  public static ORADataFactory getFactory()
  {
    return _factory;
  }

  public Student () {}

  public Student (NUMBER ssn, CHAR name, 
                              CHAR address,
                              NUMBER deptid, CHAR major)
  {
    super (ssn, name, address);
    this.deptid = deptid;
    this.major = major;
  }
  
  public Datum toDatum(Connection c) 
    throws SQLException
  {
    StructDescriptor sd =
      StructDescriptor.createDescriptor("HR.STUDENT_T", c);
    Object [] attributes = { ssn, name, address, deptid, major };
    return new STRUCT(sd, c, attributes);
  }

  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null;
    Object [] attributes = ((STRUCT) d).getOracleAttributes();
        return new Student((NUMBER) attributes[0],
                                       (CHAR) attributes[1],
                                       (CHAR) attributes[2],
                                       (NUMBER) attributes[3],
                                       (CHAR) attributes[4]);
  }

  public String toString ()
  {
    try
    {
      return "HR.STUDENT_T: ssn="+ssn.intValue()+
        " name="+name.stringValue()+
        " address="+address.stringValue()+
        " deptid="+deptid.intValue()+
        " major="+major.stringValue();
    }
    catch (SQLException e) 
    {
      // ignore the error
    }
    return "some error happened";
  }
}
