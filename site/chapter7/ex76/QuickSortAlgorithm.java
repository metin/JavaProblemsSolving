package ex76;

public class QuickSortAlgorithm extends SortAlgorithm {
  public QuickSortAlgorithm(AlgorithmAnimator animator) {
    super(animator); 
  }

  protected void QSort(int a[], int lo0, int hi0) {
    int lo = lo0;
    int hi = hi0;
    int mid;

    pause();
    if (hi0 > lo0) {
      mid = a[ ( lo0 + hi0 ) / 2 ];
      while( lo <= hi ) {
	while( ( lo < hi0 ) && ( a[lo] < mid ) )
	  ++lo;
	while( ( hi > lo0 ) && ( a[hi] > mid ) )
	  --hi;
	if( lo <= hi ) {
	  swap(a, lo, hi);
	  pause();	  
	  ++lo;
	  --hi;
	}
      }
      if( lo0 < hi )
	QSort( a, lo0, hi );
      if( lo < hi0 )
	QSort( a, lo, hi0 );
    }
  }

  public void sort(int a[]) {
    QSort(a, 0, a.length - 1);
  }
}

