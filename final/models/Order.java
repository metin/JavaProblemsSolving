package models;

public class Order {
  Pricing order;
  
  public Order(){
    
  }
  
  public void decorate(Pricing ingredient){
    if(order != null)
      ingredient.setPricing(order);
    order = ingredient;
  }
  
  public String description(){
    return order.description();
  }
  
  public Float total(){
    return order.calculate();
  }
}
