package ex85;

public class Multiply extends BaseOperation{

  protected Calculation calc;
  public Multiply(Calculation calc, float val){
	this.value = val;
    this.calc = calc;
  }

  public float calculate() {
    return calc.calculate() * this.getValue();
  }


}
