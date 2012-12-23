package ex76;

public class StaticAlgoFactory 
    implements SortAlgorithmFactory {
  public SortAlgorithm makeSortAlgorithm(String algName) {
    if ("BubbleSort".equals(algName))
      return new BubbleSortAlgorithm(animator);       
    else if ("QuickSort".equals(algName)) 
      return new QuickSortAlgorithm(animator); 
    else if ("InsertionSort".equals(algName)) 
        return new InsertionSortAlgorithm(animator); 
    else if ("SelectionSort".equals(algName))
    	return new SelectionSortAlgorithm(animator);
    else
      return new BubbleSortAlgorithm(animator); 
  }

  protected AlgorithmAnimator animator; 
  public StaticAlgoFactory(AlgorithmAnimator animator) {
    this.animator = animator; 
  }
}
