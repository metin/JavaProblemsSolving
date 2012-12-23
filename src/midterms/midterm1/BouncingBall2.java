package midterm1;

import java.awt.*;

public class BouncingBall2 extends DBAnimationApplet {

  public BouncingBall2() {
    super(true);  // double buffering
  }

  protected void initAnimator() {
    String att = getParameter("delay");
    if (att != null)
      setDelay(Integer.parseInt(att));
    x = d.width * 2 / 3 ;
    y = d.height - radius;
  }


  protected void paintFrame(Graphics g) {
    g.setColor(Color.white);
    g.fillRect(0,0,d.width,d.height);
    if (x < radius || x > d.width - radius) {
      dx = -dx;
    }

    if (y < radius || y > d.height - radius) {
      dy  =  -dy;
    }
    x += dx; y += dy;
    g.setColor(color);
    g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
  }

  protected int x, y;
  protected int dx = -2, dy = -4;
  protected int radius = 20;
  protected Color color = Color.green;
}
