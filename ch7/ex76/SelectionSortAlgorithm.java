package ex76;

class SelectionSortAlgorithm extends SortAlgorithm {
  void sort(int a[]) {
    for (int i = 0; i < a.length-1; i++ ){

    	for (int j = i+1; j < a.length; j++) {
    		if (a[j] < a[i]){
    			swap(a, j, i);
    		pause();
    		}
        }
    }
    
  }

  public SelectionSortAlgorithm(AlgorithmAnimator animator) {
    super(animator); 
  }
}


