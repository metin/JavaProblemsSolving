/**
 * javadoc -version -author -d docs ex52/*.java
 * javadoc -tag pre:cm:"Precondition:"  -tag post:cm:"Postcondition:"  *.java
 */
package ex63;


/**
 * @author Metin Yorulmaz
 *
 */
public interface IStack {
	
	/**
	 * Adds a new object onto top of the stack. 
	 * 
	 * @pre item != null
	 * @post size() == size()@pre + 1
	 * @post item@pre == top()
	 * @param item Item to be pushed to the top of the stack. 
	 */
	public void push(Object item);
	
	
	/**
	 * Removes the object at the top of the stack and returns the object. 
	 * 
	 * @pre size() > 0
	 * @post @result == top()@pre
	 * @post size() == size()@pre - 1
	 * @return the object at the top of the stack, null if 
	 * 		   this stack contains no elements
	 */
	public Object pop();
	

	/**
	 * Returns the object at the top of the stack without removing it from 
	 * the stack
	 * 
	 * @pre size() > 0
	 * @post @nochange
	 * @return the object at the top of the stack, null if 
	 * 		   this stack contains no elements
	 */
	public Object top();
	
	
	/**
	 * Returns the number of objects currently in the stack.
	 * 
	 * @pre true
	 * @post @nochange
	 * @return number of items in the stack
	 */
	public int size();
	
	
	/**
     * Returns true if this collection contains no elements.
	 * This implementation returns size() == 0.
	 * 
	 * @pre true
	 * @post @nochange
	 * @return true if this stack contains no elements
	 */
	public boolean isEmpty();
}
