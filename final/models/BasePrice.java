package models;

public class BasePrice implements Pricing{
  
  Pricing pricing;
  
  public BasePrice(){
  }

  public float calculate() {
    return 0;
  }

  public String description() {
    return "";
  }

  public Pricing getPricing() {
    return pricing;
  }

  public void setPricing(Pricing p) {
    pricing = p;
  }
  
  
}
