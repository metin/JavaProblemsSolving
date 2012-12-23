package midterm1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

  
public class BouncingSquare implements BouncingObject {
  
  public BouncingSquare(){
    x = edgeLength;
    y = edgeLength;
  }

  public BouncingSquare(AnimationApplet animator){
	  this.animator = animator;
	  Dimension d = animator.getSize();
      x = d.width * 2 / 3 ;
      y = d.height - edgeLength/2;
  }
  
  public void bounce(Graphics g){
      Dimension d = animator.getSize();
      g.setColor(Color.white);
      g.fillRect(0,0,d.width,d.height);
      if (x < edgeLength/2 || x > d.width - edgeLength/2) {
        dx = -dx;
      }
      if (y < edgeLength/2 || y > d.height - edgeLength/2) {
        dy  =  -dy;
      }
      x += dx; y += dy;
      g.setColor(color);
      g.fillRect(x - edgeLength/2, y - edgeLength/2, edgeLength, edgeLength);
  }
  
  protected int x, y;
  protected int dx = -2, dy = -2;
  protected int edgeLength = 40;
  protected Color color = Color.red;
  protected AnimationApplet animator;

}
