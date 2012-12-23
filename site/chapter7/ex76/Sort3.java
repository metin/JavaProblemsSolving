package ex76;

import java.awt.*; 

public class Sort3 extends Sort2 {
  public void initAnimator() {
    String att = getParameter("dis");
    displayFactory = new StaticSortDisplayFactory(); 
    theDisplay = displayFactory.makeSortDisplay(att); 
    super.initAnimator(); 
  }
  protected void scramble() {
    int n = theDisplay.getArraySize(getSize()); 
    arr = new int[n];
    for (int i = arr.length; --i >= 0;)
      arr[i] = i; 
    for (int i = arr.length; --i >= 0;) {
      int j = (int)(i * Math.random());
      SortAlgorithm.swap(arr, i, j); 
    }
  }
  protected void paintFrame(Graphics g) {
    theDisplay.display(arr, g, getSize()); 
  }
  
  SortDisplay          theDisplay; 
  SortDisplayFactory   displayFactory; 
  
}
