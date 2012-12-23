package ex85;

import static org.junit.Assert.*;

import org.junit.Test;

public class CalculatorTest {

  @Test
  public void testAddition() {
    Calculation calc = new BaseOperation();
    calc = new Add(calc, 10);
    calc = new Add(calc, 12);
    calc = new Add(calc, 5);
    assertTrue(calc.calculate() == 27);	
  }
  
  @Test
  public void testSubtraction() {
    Calculation calc = new BaseOperation();
    calc = new Add(calc, 10);
    calc = new Subtract(calc, 3);
    assertTrue(calc.calculate() == 7);	
  }
  
  @Test
  public void testMultiplication() {
    Calculation calc = new BaseOperation();
    calc = new Add(calc, 10);
    calc = new Add(calc, 5);
    calc = new Multiply(calc, 2);
    assertTrue(calc.calculate() == 30);	
  }

  @Test
  public void testDivision() {
    Calculation calc = new BaseOperation();
    calc = new Add(calc, 10);
    calc = new Add(calc, 5);
    calc = new Divide(calc, 3);
    assertTrue(calc.calculate() == 5);	
  }
  
}
