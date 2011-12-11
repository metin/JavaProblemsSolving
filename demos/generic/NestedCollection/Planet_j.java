/*
 * This is the ORAData customized class of "PLANET_T".
 * This is used in NestedCollection.java.
 * Please use jdk1.2 or later version and classes12.zip
 */

import java.sql.*;
import oracle.sql.*;

public class Planet_j implements ORAData, ORADataFactory
{
  static final Planet_j _planetFactory = new Planet_j ();

  String name;
  int mass;
  ARRAY satellites;

  public static ORADataFactory getFactory()
  {
    return _planetFactory;
  }

  public Planet_j () {}

  public Planet_j (String name, int mass, ARRAY satellites)
  {
    this.name = name;
    this.mass = mass;
    this.satellites = satellites;
  }
  
  public Planet_j (String name, int mass, Satellite_j[] satelliteElems, 
                   Connection conn) 
    throws SQLException
  {
    this.name = name;
    this.mass = mass;

    // Obtain the type descriptor of "NT_SAT_T"
    ArrayDescriptor desc = 
      ArrayDescriptor.createDescriptor ("NT_SAT_T", conn);

    // Create a oracle.sql.ARRAY that holds the Satellite elements
    this.satellites = new ARRAY (desc, conn, satelliteElems);
  }
  
  /**
   * Required by ORAData interface
   */
  public Datum toDatum(Connection c) 
    throws SQLException
  {
    StructDescriptor sd =
      StructDescriptor.createDescriptor("PLANET_T", c);
    
    Object[] attributes = { name, new NUMBER (mass), satellites };
    
    return new STRUCT(sd, c, attributes);
  }
    
  /**
   * Required by ORADataFactory interface 
   */
  public ORAData create(Datum d, int sqlType) throws SQLException
  {
    if (d == null) return null;
    
    Datum[] attributes = ((STRUCT) d).getOracleAttributes();
    
    return new Planet_j (attributes[0].stringValue (),
                         attributes[1].intValue (),
                         (ARRAY) attributes[2]);
  }

  /**
   * Return a string representation of the object.
   */
  public String toString () 
  {
    try
    {
      StringBuffer sbuf = new StringBuffer ();
      sbuf.append ("Planet: name="+name);
      sbuf.append (" mass="+mass);
      sbuf.append ("\n");

      // List each Satellite elements
      Object[] satelliteElems = (Object[]) satellites.getArray ();
      for (int i=0; i<satelliteElems.length; i++)
        sbuf.append ("  "+ (Satellite_j) satelliteElems [i]);

      return sbuf.toString ();
      
    } 
    catch (SQLException e) 
    {
      // ignore errors
    }

    return "Some error happened";
  }
}
