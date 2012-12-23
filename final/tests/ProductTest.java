package tests;
import java.sql.SQLException;
import java.util.ArrayList;
import models.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ProductTest {

  @Test
  public void testProductFind() {
    Product p = null;
    try {
      p = new Product(1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    
    assertFalse(p==null);
    assertFalse(p.getImage() == null);
    assertFalse(p.getImage() == "");
    assertEquals(p.getPrice(), 0.99, 0.001);

  }

  @Test
  public void testProductFindAll() {
    ArrayList<Product> p = null;
    try {
      p = Product.findAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    
    assertFalse(p.size() == 0);
    assertFalse(p == null);
  }
}
