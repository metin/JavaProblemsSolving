/*
 * This shows how to create a user defined java data
 * type structure that maps to user defined SQL data
 * type structure with inheritance inside 
 *
 * This class is used by Inheritance1.java
 * Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;
import oracle.sql.*;

public class ParttimeStudent extends Student
{
  static final ParttimeStudent _factory = 
  new ParttimeStudent ();

  public NUMBER numHours;

  public static ORADataFactory getFactory()
  {
    return _factory;
  }

  public ParttimeStudent () {}

  public ParttimeStudent (NUMBER ssn, CHAR name, 
                                      CHAR address,
                                      NUMBER deptid, CHAR major,
                                      NUMBER numHours)
  {
    super (ssn, name, address, deptid, major);
    this.numHours = numHours;    
  }
  
  public Datum toDatum(Connection c) 
    throws SQLException
  {
    StructDescriptor sd =
      StructDescriptor.createDescriptor("HR.PARTTIMESTUDENT_T", c);
    Object [] attributes = { ssn, name, address, deptid, major, numHours};
    return new STRUCT(sd, c, attributes);
  }

  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null;
    Object [] attributes = ((STRUCT) d).getOracleAttributes();
        return new ParttimeStudent ((NUMBER) attributes[0],
                                                (CHAR) attributes[1],
                                                (CHAR) attributes[2],
                                                (NUMBER) attributes[3],
                                                (CHAR) attributes[4],
                                                (NUMBER) attributes[5]);
  }

  public String toString ()
  {
    try
    {
      return "HR.PARTTIMESTUDENT_T: ssn="+ssn.intValue()+
        " name="+name.stringValue()+
        " address="+address.stringValue()+
        " deptid="+deptid.intValue()+
        " major="+major.stringValue()+
        " numHours="+numHours.intValue();
    }
    catch (SQLException e) 
    {
      // ignore the error
    }
    return "some error happened";
  }
}
