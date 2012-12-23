import ex63.*;
import ex65.*;

public class TestStack {

	/**
	 * @param args
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws CloneNotSupportedException {

		Stack s = new Stack();
		Integer num1 = new Integer(1);
		s.push(num1);
		Stack clone = s.clone();
		System.out.println(s.equals(clone));

	}

}
