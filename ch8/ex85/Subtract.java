package ex85;

public class Subtract extends BaseOperation{

  protected Calculation calc;
  public Subtract(Calculation calc, float val){
	this.value = val;
    this.calc = calc;
  }

  public float calculate() {
    return calc.calculate() - this.getValue();
  }
}
