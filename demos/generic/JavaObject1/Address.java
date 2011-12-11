/*
 * This shows how to use ORAData interface to encapsulate
 * SQL types into java types of user's choice. 
 *
 * 1. It uses StructDescriptor and STRUCT to create a
 *    customized Java data type based on an user defined
 *    SQL type ADDRESS_T.
 * 2. This class is used in file JavaObject1.java
 * 3. Please use jdk1.2 or later version and classes12.zip
 */

 
import java.sql.*;
import oracle.sql.*;

public class Address implements ORAData, ORADataFactory
{
  static final Address _factory = new Address ();

  String sql_type = "HR.ADDRESS_T";
  public String street;
  public String city;
  public String state;
  public int zipCode;

  public static ORADataFactory getFactory()
  {
    return _factory;
  }

  public Address () {}

  public Address (String street, String city, String state,
                              int zipCode)
  {
    this.street = street;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
  }

  public Datum toDatum(Connection c) 
    throws SQLException
  {
    StructDescriptor sd = 
      StructDescriptor.createDescriptor(sql_type, c);
    Object [] attributes = { street, city, state, new Integer(zipCode) };
    return new STRUCT(sd, c, attributes);
  }

  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null;
    Datum[] attributes = ((JAVA_STRUCT) d).getOracleAttributes();
    return new Address (attributes[0].stringValue (),
                        attributes[1].stringValue (),
                        attributes[2].stringValue (),
                        attributes[3].intValue ());
  }

  public String toString ()
  {
    return sql_type+" : "+street+", "+city+", "+state+", "+zipCode;
  }
}
