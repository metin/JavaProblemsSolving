package ex76;

public abstract class SortAlgorithm {

  abstract void sort(int a[]); 

  private AlgorithmAnimator animator; 

  protected SortAlgorithm(AlgorithmAnimator animator) {
    this.animator = animator; 
  }

  protected void pause() {
    if (animator != null) 
      animator.pause();
  }

  protected static void swap(int a[], int i, int j) {
    int T;
    T = a[i]; a[i] = a[j]; a[j] = T;
  }
}
