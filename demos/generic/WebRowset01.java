/* Copyright (c) 2004, Oracle. All rights reserved.  */

/*
 This demos how a WebRowSet read and write a RowSet object in a XML format.
 1. check the output file WebRowset01_out.xml for writeXml.
 2. look at the display for checking the readXml contents
 */

import java.sql.*;
import java.io.*;
import oracle.jdbc.*;
import oracle.jdbc.pool.*;
import oracle.jdbc.rowset.*;

public class WebRowset01
{
   private static String filename = "WebRowset01_out.xml";
   public static void main (String[] args) throws Exception
   {
     String url = getUrl(); 

     OracleDataSource ods = new OracleDataSource();
     ods.setUser("hr");
     ods.setPassword("hr");
     ods.setURL(url);
     Connection conn = ods.getConnection();
     cleanTable(conn); // drop table wrs01_tab
     createTable(conn); // create and populate table wrs01_tab
     conn.setAutoCommit(false);

     testWrite(conn);
     testRead();
     conn.close();
     conn = null;
     ods.close();
     ods = null;
  }

  private static void testWrite(Connection conn) throws Exception
  {
     System.setProperty("javax.xml.parsers.SAXParserFactory",
                        "oracle.xml.jaxp.JXSAXParserFactory");
     System.setProperty("http.proxyHost", "www-proxy.us.oracle.com");
     System.setProperty("http.proxyPort", "80");

     Statement stmt = conn.createStatement();
     ResultSet rset = stmt.executeQuery("select * from wrs01_tab");
     OracleWebRowSet owrs01 = new OracleWebRowSet();
     owrs01.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
     owrs01.populate(rset);

     try
     {
        FileWriter out1 = new FileWriter(filename);
        owrs01.writeXml(out1);
        out1.close();
     } catch (IOException e)
     {
        System.err.println("Couldn't construct a FileWriter");
        throw e;
     } catch (SQLException e)
     {
        System.err.println("ErrorCode: " + e.getErrorCode() + "SQLException:");
        e.printStackTrace();
        throw e;
     }
  }

  private static void testRead() throws Exception
  {
     OracleWebRowSet wset = new OracleWebRowSet();
     try
     {
        FileReader fr = new FileReader(filename);
        wset.readXml(fr);
        fr.close();
     } catch (IOException e)
     {
        System.err.println("IOException:");
        e.printStackTrace();
        throw e;
     } catch (SQLException e)
     {
        System.err.println("ErrorCode: " + e.getErrorCode() + " SQLException:");
        e.printStackTrace();
        throw e;
     }

     // check the contents of wset
     try
     {
        wset.beforeFirst();
        System.out.println("The data stored in OracleWebRowSet:");
        while (wset.next())
        {
            System.out.println(" empno: " + wset.getInt(1) +
                               " empname: " + wset.getString(2) +
                               " job: " + wset.getString(3) +
                               " mgr:" + wset.getInt(4) +
                               " hiredate: " + wset.getDate(5) +
                               " sales: " + wset.getFloat(6) +
                               " comm: " + wset.getFloat(7) +
                               " deptno: " + wset.getInt(8));
        }
     } catch (Exception e)
     {
       e.printStackTrace();
     }
  }

  private static String getUrl()
  {
     String url = "jdbc:oracle:oci:@";
     try
     {
        String url1 = System.getProperty("JDBC_URL");
        if ( url1 != null )
            url = url1;
     } catch (Exception e)
     {
        // ignore security exception
     }
     return url;
  }

  private static void cleanTable(Connection conn) throws SQLException
  {
     Statement stmt = conn.createStatement();
     try
     {
        stmt.execute("drop table wrs01_tab");
     } catch (SQLException e)
     {
        // ingore no table error
     }
     stmt.close();
     stmt = null;
  }

  private static void createTable(Connection conn) throws SQLException
  {
     Statement stmt = conn.createStatement();
     stmt.execute("create table wrs01_tab (empno NUMBER(4) not null, ename VARCHAR2(10), job VARCHAR2(9), mgr NUMBER(4), hiredate DATE, sal NUMBER(7,2), comm NUMBER(7,2), deptno NUMBER(2))");
     stmt.execute("insert into wrs01_tab values(7369, 'smith','clerk',7902,TO_DATE('17-12-1980','DD-MM-YYYY'),800,NULL,20)");
     stmt.execute("insert into wrs01_tab values(7499, 'allen','salesman',7698,TO_DATE('20-2-1981','DD-MM-YYYY'),1600,300,30)");
     stmt.execute("insert into wrs01_tab values(7521, 'ward','salesman',7698,TO_DATE('22-2-1981','DD-MM-YYYY'),1250,500,30)");
     stmt.execute("insert into wrs01_tab values(7566, 'jones','manager',7839,TO_DATE('2-4-1981','DD-MM-YYYY'),2975,NULL,20)");
     stmt.execute("insert into wrs01_tab values(7654, 'martin','salesman',7698,TO_DATE('28-9-1981','DD-MM-YYYY'),1250,1400,30)");
     stmt.execute("insert into wrs01_tab values(7698, 'blake','manager',7839,TO_DATE('1-5-1981','DD-MM-YYYY'),2850,NULL,30)");
     stmt.close();
     stmt = null;
  }
}
