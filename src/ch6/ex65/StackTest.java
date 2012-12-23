
package ex65;

import ex63.IStack;
import junit.framework.*;
import java.util.*;

public class StackTest extends TestCase {

	public StackTest(String testName){
		super(testName);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testPush(){
		IStack s = new Stack();
		Integer num1 = new Integer(1);
		s.push(num1);
		assertTrue(s.size() == 1);
		assertTrue(s.top() == num1);	
	}
	
	public void testPop(){
		Stack s = new Stack();
		Integer num1 = new Integer(1);
		Integer num2 = new Integer(2);
		Integer num3 = new Integer(3);
		Integer num4 = new Integer(4);
		s.push(num1);
		s.push(num2);
		s.push(num3);
		s.push(num4);
		
		assertTrue(s.size() == 4);
		assertTrue(s.top() == num4);
		Integer top = (Integer)s.pop();
		assertTrue(top == num4);
		
		assertTrue(s.size() == 3);
		assertTrue(s.top() == num3);
		top = (Integer)s.pop();
		assertTrue(top == num3);
		
		assertTrue(s.size() == 2);
		assertTrue(s.top() == num2);
		top = (Integer)s.pop();
		assertTrue(top == num2);
	
	}

	public void testEmptyStack(){
		IStack s = new Stack();
		assertTrue(s.size() == 0);
        assertTrue(s.top() == null);
        assertTrue(s.pop() == null);
    }

	public void testCloneEquals() throws CloneNotSupportedException{
		Stack s = new Stack();
		Integer num1 = new Integer(1);
		s.push(num1);
		Integer num2 = new Integer(2);
		s.push(num2);
		Stack clone = s.clone();
		assertTrue(s.size() == clone.size());
		assertTrue(s.equals(clone));

    }
	
	public static Test suite(){
		return new TestSuite(StackTest.class);
	}
	
}
