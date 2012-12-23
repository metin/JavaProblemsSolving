/**
 * 
 */
package ex65;

import ex63.IStack;

/**
 * @author Metin Yorulmaz
 *
 * @invariant _wellformed()
 */
public class Stack implements IStack, Cloneable {

	/**
	 * Creates a stack with size of 200
	 */
	public Stack(){
		stackArray = new Object[200];
		assert _wellformed();
	}
	
	/**
	 * Creates a stack with size of  @param stackSize
	 * 
	 * @param stackSize Size of stack 
	 */	
	public Stack(int stackSize){
		stackArray = new Object[stackSize];
		assert _wellformed();
	}
	

	
	/**
	 * Adds a new object onto top of the stack. 
	 * 
	 * @pre item != null
	 * @post size() == size()@pre + 1
	 * @post item@pre == top()
	 * @param item Item to be pushed to the top of the stack. 
	 */
	public void push(Object item) {
		assert _wellformed();
		assert item!=null;
		int preSize = size();
		stackArray[size()] = item;
		size++;
		assert size() == preSize + 1;
		assert item.equals(top());
	}

	
	/**
	 * Removes the object at the top of the stack and returns the object. 
	 * 
	 * @pre size() > 0
	 * @post @result == top()@pre
	 * @post size() == size()@pre - 1
	 * @return the object at the top of the stack, null if 
	 * 		   this stack contains no elements
	 */
	public Object pop() {
		assert _wellformed();
		assert size() > 0;
		int preSize = size();
		Object top = top();
		Object item = null;
		if(!isEmpty()){
			item = stackArray[size()-1];
			stackArray[size()-1] = null;
			size--;
			assert item.equals(top);
			assert size() == preSize - 1;
		}
		return item;
	}

	/**
	 * Returns the object at the top of the stack without removing it from 
	 * the stack
	 * 
	 * @pre size() > 0
	 * @post @nochange
	 * @return the object at the top of the stack, null if 
	 * 		   this stack contains no elements
	 */
	public Object top() {
		assert _wellformed();
		assert size() > 0;
		int preSize = size();
		Object item = null;
		if(!isEmpty())
			item = stackArray[size()-1];
		assert preSize == size();
		return item;
	}	

	/**
	 * Returns the number of objects currently in the stack.
	 * 
	 * @pre true
	 * @post @nochange
	 * @return number of items in the stack
	 */
	public int size() {
		assert _wellformed();
		return size;
	}

	/**
     * Returns true if this collection contains no elements.
	 * This implementation returns size() == 0.
	 * 
	 * @pre true
	 * @post @nochange
	 * @return true if this stack contains no elements
	 */	
	public boolean isEmpty() {
		return size() == 0;
	}	

	/**
	 * Compares this stack to the specified object. 
	 * The result is true if and only if the argument 
	 * is not null and is a stack object that 
	 * represents the same sequence of objects.
	 * @param other object to compare
	 * @return true it other stack is equal to this stack
	 */
	public boolean equals(Object other){
		if (this == other) return true;
		if(other instanceof Stack){
			Stack otherStack = (Stack)other;
			if(otherStack.size() == this.size()){
				//Compare items at the same index,
				//Make sure they are equal
				for(int i=0; i < size(); i++){
					if(stackArray[i] != otherStack.stackArray[i])
						return false;
				}
				return true;
			}
		}	
		return false;
	}

	
	/**
	 * Returns a clone of this stack. The copy will contain a 
	 * reference to a clone of the internal array, 
	 * not a reference to the original array object.
	 * @return a clone of this stack.
	 */
	public Stack clone() throws CloneNotSupportedException{
		Stack clone = (Stack)super.clone();
		clone.stackArray = new Object[stackArray.length];
		for(int i=0; i < size(); i++){
			clone.stackArray[i] = stackArray[i];
		}
		clone.size = this.size;
		return clone;		
	}

	/**
	 * Invariants of the stack implementation
	 * @return true if stack is well formed
	 */
	protected boolean _wellformed(){
		if (stackArray == null) return false;		
		return true;
	}			
	
	//Internal array to hold stack items
	private Object[] stackArray;
	private int size = 0;
	
}
