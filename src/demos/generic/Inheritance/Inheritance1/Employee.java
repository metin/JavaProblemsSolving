/*
 * This shows how to use StructDescriptor and STRUCT
 * to create user defined data type that maps to a
 * user defined SQL type Employee_t in the database.
 *
 * This class is used by Inheritance1.java
 * Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;
import oracle.sql.*;

public class Employee extends Person
{
  static final Employee _factory = 
  new Employee ();

  public NUMBER empid;
  public CHAR mgr;

  public static ORADataFactory getFactory()
  {
    return _factory;
  }

  public Employee () {}

  public Employee (NUMBER ssn, CHAR name, 
                               CHAR address,
                               NUMBER empid, CHAR mgr)
  {
    super (ssn, name, address);
    this.empid = empid;
    this.mgr = mgr;
  }
  
  public Datum toDatum(Connection c) 
    throws SQLException
  {
    StructDescriptor sd =
      StructDescriptor.createDescriptor("HR.EMPLOYEE_T", c);
    Object [] attributes = { ssn, name, address, empid, mgr };
    return new STRUCT(sd, c, attributes);
  }

  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null;
    Object [] attributes = ((STRUCT) d).getOracleAttributes();
        return new Employee((NUMBER) attributes[0],
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
        " empid="+empid.intValue()+
        " mgr="+mgr.stringValue();
    }
    catch (SQLException e) 
    {
      // ignore the error
    }
    return "some error happened";
  }
}
