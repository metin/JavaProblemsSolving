package tests;
import java.sql.SQLException;
import java.util.ArrayList;

import models.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class IngredientTest {

  @Test
  public void testIngredientFind() {
    Ingredient p = null;
    try {
      p = new Ingredient(1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    
    assertFalse(p==null);
    assertFalse(p.getColor() == null);
    assertFalse(p.getColor() == "");

  }

  @Test
  public void testIngredientFindAll() {
    ArrayList<Ingredient> p = null;
    try {
      p = Ingredient.findAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    
    assertFalse(p.size() == 0);
    assertFalse(p == null);
  }
}
