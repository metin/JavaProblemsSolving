package models;

import java.sql.SQLException;

public class Order extends OrderData{
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
  
  public void save() throws SQLException
  {
    if(order==null) return;
    this.price = order.calculate();
    this.description = order.description();
    super.save();
  }
}
