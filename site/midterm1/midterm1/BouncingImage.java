package midterm1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

	
public class BouncingImage implements BouncingObject {
	
	public BouncingImage(){
		x = imgWidth;
		y = imgHeight;
	}

	public BouncingImage(AnimationApplet animator){
		this.animator = animator;
		Dimension d = animator.getSize();
	    x = d.width * 2 / 3 ;
	    y = d.height - imgHeight/2;
	    try{
	        String imageName = animator.getParameter("image");
	        image  = animator.getImage(animator.getDocumentBase(), imageName);
	    }catch(Exception e){}
	}
	
    public void bounce(Graphics g){
    	Dimension d = animator.getSize();
        g.setColor(Color.white);
        g.fillRect(0,0,d.width,d.height);
        d = animator.getSize();
        if (x < imgWidth/2 || x > d.width - imgWidth/2) {
          dx = -dx;
        }
        
        if (y < imgHeight/2 || y > d.height - imgHeight/2) {
          dy  =  -dy;
        }
        x += dx; y += dy;

        g.drawImage(image, x - imgWidth/2, y - imgHeight/2, imgHeight, imgWidth, animator);
    }

    protected int x, y;
    protected int dx = -2, dy = -2;
    protected int imgHeight = 50;
    protected int imgWidth = 50;
    protected AnimationApplet animator;
    protected Image image;

}
