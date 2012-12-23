package ex76;

public abstract class AlgorithmAnimator 
    extends DBAnimationApplet {

  abstract protected void algorithm(); 
  
  public void run() {
    algorithm(); 
  }

  final protected void pause() {
    if (Thread.currentThread() == animationThread) {
      try { 
        Thread.currentThread().sleep(delay); 
      } catch (InterruptedException e) {}
      repaint();
    }
  }
  
}
