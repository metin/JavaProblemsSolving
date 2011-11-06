package ex76;

class InsertionSortAlgorithm extends SortAlgorithm {
  void sort(int a[]) {
    for (int i = 0; i < a.length; i++ )
      for (int j = i; j > 0; j--) {
        if (a[j-1] > a[j])
          swap(a, j-1, j);
        pause();
      }
  }

  public InsertionSortAlgorithm(AlgorithmAnimator animator) {
    super(animator); 
  }
}


