package midterm1;

import java.awt.*;

public class BouncingBall3 extends BouncingBall2 {

  public BouncingBall3() {
    super(); 
  }

  protected void initAnimator() {
    super.initAnimator();
    String attrShape = getParameter("shape");
    BouncingObjectFactory objectFactory = new BouncingObjectFactory(this);
    bouncingObject = objectFactory.makeBouncingObject(attrShape);
  }

  protected void paintFrame(Graphics g) {
    bouncingObject.bounce(g);
  }

  protected BouncingObject bouncingObject;
  
}
