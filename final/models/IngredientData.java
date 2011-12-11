package models;

import java.sql.*;
import java.util.*;

import db.*;

public class IngredientData extends db.Base {
  protected Integer id;
  protected String name;
  protected Float price;
  protected String color;
  

  public IngredientData(){
    
  }
  public IngredientData(Integer id){
    this.id = id;
  }  
  public void find() throws SQLException{
    this.connect();
    PreparedStatement pstmt = connection.prepareStatement ("SELECT * FROM INGREDIENTS WHERE INGREDIENT_ID = ? ");
    pstmt.setInt (1, this.id);
    ResultSet rs = pstmt.executeQuery();
    while (rs.next()) {
      this.name = rs.getString("INGREDIENT_NAME");
      this.price  = rs.getFloat("PRICE");
      this.color  = rs.getString("COLOR");
    }
    rs.close();
    pstmt.close();
    connection.close();
  }

  public static ArrayList<Ingredient> findAll() throws SQLException{
    ArrayList<Ingredient> all = new ArrayList<Ingredient>();
    Ingredient base = new Ingredient();
    base.connect();
    PreparedStatement pstmt = base.connection.prepareStatement("SELECT * FROM INGREDIENTS");
    ResultSet rs = pstmt.executeQuery();
    while (rs.next()) {
      Ingredient p = new Ingredient();
      p.id = rs.getInt("INGREDIENT_ID");
      p.name = rs.getString("INGREDIENT_NAME");
      p.price  = rs.getFloat("PRICE");
      p.color  = rs.getString("COLOR");
      all.add(p);
    }
    rs.close();
    pstmt.close();
    base.connection.close();
    return all;
  }

}
