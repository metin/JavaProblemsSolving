package ex85;

public class Add extends BaseOperation{

  protected Calculation calc;
  public Add(Calculation calc, float val){
	this.value = val;
    this.calc = calc;
  }
  
  public float calculate() {
    return calc.calculate() + this.getValue();
  }
}
