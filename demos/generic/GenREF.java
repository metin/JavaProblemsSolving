/*
 * It demonstrates how to use REF 
 *
 * This class is used in RefClient.java
 * Please use jdk1.2 or later version and classes12.zip
 */

import oracle.sql.REF;
import oracle.sql.StructDescriptor;
import java.sql.Connection;
import java.sql.SQLException;

public class GenREF 
{
  String typeName;
  byte[] bytes;

  public GenREF (oracle.sql.REF ref) throws SQLException {
    this.typeName = ref.getBaseTypeName ();
    this.bytes = ref.getBytes ();
  }

  public REF getREF (Connection conn) throws SQLException {
    return new REF (new StructDescriptor (typeName, conn),
                    conn,
                    bytes);
  }
}
