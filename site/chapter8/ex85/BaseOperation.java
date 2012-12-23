package ex85;

public class BaseOperation implements Calculation{

  protected float value;
  public BaseOperation(){
  }
  public float getValue(){
    return value;
  }
  
  public float calculate() {
    return 0;
  }

}
