package midterm1;

import java.awt.*;

public abstract class DBAnimationApplet
    extends AnimationApplet {

  final public void update(Graphics g) {
    if (doubleBuffered) {
      paintFrame(offscreen);
      g.drawImage(im, 0, 0, this);
    } else {
      // super.update();
    }
  }

  final public void paint(Graphics g) {
    paintFrame(g);
  }

  final public void init() {
    d = getSize();
    im = createImage(d.width, d.height);
    offscreen = im.getGraphics();
    initAnimator();
  }

  protected void initAnimator() {}

  abstract protected void paintFrame(Graphics g);

  protected DBAnimationApplet(boolean doubleBuffered) {
    this.doubleBuffered = doubleBuffered;
  }

  protected DBAnimationApplet() {
    this.doubleBuffered = true;
  }

  protected boolean doubleBuffered;
  protected Dimension d;
  protected Image im;
  protected Graphics offscreen;

}
