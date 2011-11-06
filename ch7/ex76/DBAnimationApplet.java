package ex76;

import java.awt.*;

public abstract class DBAnimationApplet 
    extends AnimationApplet { 

  abstract protected void paintFrame(Graphics g); 

  final public void update(Graphics g) {
    if (doubleBuffered) {
      if (im == null) {
        im = createImage(d.width, d.height);
        offscreen = im.getGraphics();
      }
      paintFrame(offscreen); 
      g.drawImage(im, 0, 0, this);
    } else { 
      super.update(g); 
    }
  }
  
  final public void paint(Graphics g) {
    paintFrame(g); 
  }
  final public void init() {
    d = getSize();
    initAnimator(); 
  }
  
  protected void initAnimator() {} 

  protected DBAnimationApplet(boolean doubleBuffered) {
    this.doubleBuffered = doubleBuffered;
  }
  
  protected DBAnimationApplet() {
    this.doubleBuffered = true; 
  }
  
  protected boolean doubleBuffered;
  protected Image im;
  protected Graphics offscreen;  
  protected Dimension d; 
}
