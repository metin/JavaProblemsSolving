/*
 * This is a complement of class Person
 * This class is used by Inheritance1.java
 * Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;
import oracle.sql.*;

public class PersonFactory implements ORADataFactory
{
  static final PersonFactory _factory = new PersonFactory ();
  
  public static ORADataFactory getFactory()
  {
    return _factory;
  }

  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    STRUCT s = (STRUCT) d;
    if (s.getSQLTypeName ().equals ("HR.PERSON_T"))
      return Person.getFactory ().create (d, sqlType);
    else if (s.getSQLTypeName ().equals ("HR.STUDENT_T"))
      return Student.getFactory ().create(d, sqlType);
    else if (s.getSQLTypeName ().equals ("HR.PARTTIMESTUDENT_T"))
      return ParttimeStudent.getFactory ().create(d, sqlType);
    else if (s.getSQLTypeName ().equals ("HR.EMPLOYEE_T"))
      return Employee.getFactory ().create(d, sqlType);
    else
      return null;
  }
}
