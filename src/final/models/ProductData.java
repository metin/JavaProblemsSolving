package models;

import java.sql.*;
import java.util.*;

import db.*;

public class ProductData extends db.Base {
  protected Integer id;
  protected String name;
  protected Float price;
  protected String image;
  

  public ProductData(){
    
  }
  public ProductData(Integer id){
    this.id = id;
  }  
  public void find() throws SQLException{
    this.connect();
    PreparedStatement pstmt = connection.prepareStatement ("SELECT * FROM PRODUCTS WHERE PRODUCT_ID = ? ");
    pstmt.setInt (1, this.id);
    ResultSet rs = pstmt.executeQuery();
    while (rs.next()) {
      this.name = rs.getString("PRODUCT_NAME");
      this.price  = rs.getFloat("PRICE");
      this.image  = rs.getString("IMAGE_FILE");
    }
    rs.close();
    pstmt.close();
    connection.close();
  }

  public static ArrayList<Product> findAll() throws SQLException{
    ArrayList<Product> all = new ArrayList<Product>();
    Product base = new Product();
    base.connect();
    PreparedStatement pstmt = base.connection.prepareStatement("SELECT * FROM PRODUCTS");
    ResultSet rs = pstmt.executeQuery();
    while (rs.next()) {
      Product p = new Product();
      p.id = rs.getInt("PRODUCT_ID");
      p.name = rs.getString("PRODUCT_NAME");
      p.price  = rs.getFloat("PRICE");
      p.image  = rs.getString("IMAGE_FILE");
      all.add(p);
    }
    rs.close();
    pstmt.close();
    base.connection.close();
    return all;
  }

}
