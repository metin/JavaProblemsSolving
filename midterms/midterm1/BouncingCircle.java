package midterm1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class BouncingCircle implements BouncingObject {
	
	public BouncingCircle(){
		x = 3*radius;
		y = 3*radius;
	}

	public BouncingCircle(AnimationApplet animator){
		this.animator = animator;
		Dimension d = animator.getSize();
	    x = d.width * 2 / 3 ;
	    y = d.height - radius;
	}
	
    public void bounce(Graphics g){
    	Dimension d = animator.getSize();
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
    protected AnimationApplet animator;
}
