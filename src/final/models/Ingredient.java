package models;

import java.sql.SQLException;

public class Ingredient extends IngredientData implements Pricing{
  
  private Pricing pricing;

  public Ingredient(){
  }

  public Ingredient(int id) throws SQLException{
    super(id);
    find();
  }

  public void setName(String name){
    this.name = name;
  }

  public String getName(){
    return this.name;
  }
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  public String getColor() {
    return color;
  }
  public void setColot(String color) {
    this.color = color;
  }

  public Float getPrice() {
    return price;
  }
  public void setPrice(Float price) {
    this.price = price;
  }

  public Pricing getPricing() {
    return pricing;
  }

  public void setPricing(Pricing pricing) {
    this.pricing = pricing;
  }
  
  public float calculate() {
    System.out.println("Ingredient calc");
    return pricing.calculate() + getPrice();
  }

  public String description() {
    return  pricing.description() + " with " + getName();
  }
}
