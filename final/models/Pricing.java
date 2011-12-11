package models;

public interface Pricing {

  public float calculate();
  public String description();
  public Pricing getPricing();
  public void setPricing(Pricing p);
}
