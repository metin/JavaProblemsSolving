package ex85;

public class Divide extends BaseOperation{

  protected Calculation calc;
  public Divide(Calculation calc, float val){
	this.value = val;
    this.calc = calc;
  }

  public float calculate() {
    return calc.calculate() / this.getValue();
  }
}
