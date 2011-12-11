package models;

import java.sql.SQLException;

public class Product extends ProductData implements Pricing{
  private Pricing pricing = new BasePrice();
  
  public Product(){

  }

  public Product(int id) throws SQLException{
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
  public Float getPrice() {
    return price;
  }
  public void setPrice(Float price) {
    this.price = price;
  }
  public String getImage() {
    return image;
  }
  public void setImage(String image) {
    this.image = image;
  }

  public Pricing getPricing() {
    return pricing;
  }

  public void setPricing(Pricing pricing) {
    this.pricing = pricing;
  }
  
  public float calculate() {
    System.out.println("Product calc");
    return pricing.calculate() + getPrice();
  }

  public String description() {
    return getName();
  }
}
