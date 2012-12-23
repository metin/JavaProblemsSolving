package ex76;

class BubbleSortAlgorithm extends SortAlgorithm {
  void sort(int a[]) {
    for (int i = a.length; --i>=0; )
      for (int j = 0; j<i; j++) {
        if (a[j] > a[j+1])
          swap(a, j, j+1);
        pause();
      }
  }

  public BubbleSortAlgorithm(AlgorithmAnimator animator) {
    super(animator); 
  }
}
