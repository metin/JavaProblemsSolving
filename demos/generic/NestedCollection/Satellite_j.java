/*
 * This is the ORAData customized class of "SATELLITE_T".
 * This class is used in NestedCollection.java
 * Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;
import oracle.sql.*;

public class Satellite_j implements ORAData, ORADataFactory
{
  static final Satellite_j _satelliteFactory = new Satellite_j ();

  String name;
  double diameter;

  public static ORADataFactory getFactory()
  {
    return _satelliteFactory;
  }

  public Satellite_j () {}

  public Satellite_j (String name, double diameter)
  {
    this.name = name;
    this.diameter = diameter;
  }
  
  /**
   * Required by ORAData interface
   */
  public Datum toDatum(Connection c) 
    throws SQLException
  {
    StructDescriptor sd =
      StructDescriptor.createDescriptor("SATELLITE_T", c);
    
    Object[] attributes = { name, new NUMBER (diameter) };
    
    return new STRUCT(sd, c, attributes);
  }
    
  /**
   * Required by ORADataFactory interface 
   */
  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null;
    
    Datum[] attributes = ((STRUCT) d).getOracleAttributes();
    
    return new Satellite_j (attributes[0].stringValue (),
                            attributes[1].doubleValue ());
  }

  /**
   * Return a string representation of the object.
   */
  public String toString ()
  {
    StringBuffer sbuf = new StringBuffer ();

    sbuf.append ("Satellite: name="+name);
    sbuf.append (" diameter="+diameter);
    sbuf.append ("\n");

    return sbuf.toString ();
  }
}
