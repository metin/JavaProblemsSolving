package tests;
import java.sql.SQLException;
import java.util.ArrayList;

import models.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrderTest {

  @Test
  public void testOrder() {
    Order order = new Order();

    Product p = null;
    try {
      p = new Product(1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
 
    Ingredient ing = null;
    try {
      ing = new Ingredient(1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    
    assertEquals(p.getPrice(), 0.99, 0.001);

    order.decorate(p);
    order.decorate(ing);

    assertEquals(order.total(), 1.19, 0.001);

  }

}
