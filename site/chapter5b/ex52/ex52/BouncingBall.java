package ex52;
import java.awt.*;

/** 
 * BouncingBall animated applet.
 * This is BouncingBall example applet from Xiaoping Jia's Object-Oriented 
 * Software Development Using Java. 
 * Code slightly modified, javadoc commets added.
 * 
 * Bouncing ball demonstrates how to avoid flickering when working with 
 * graphics. It uses a technique called off-screen drawing or double 
 * buffering. Instead of painting each frame directly to screen, it first
 * paints it in a temporary buffer in the memory then copies that 
 * frame to the screen.
 * 
 * @author    Xiaoping Jia
 * @author    Metin Yorulmaz
 * @version   1.0.0
 * @since     2011-10-06
 */
public class BouncingBall extends java.applet.Applet implements Runnable{
	
	/** Color of ball */
	protected Color color = Color.green;
	
	/** Radius of ball */
	protected int radius = 20;
	
	/**Refresh rate of bouncing ball. 
	 * Default value is 100 mills, but can 
	 * be set by applet parameter 'delay'*/
	protected int delay = 100;
	
	/** X coordinate of center of the ball*/
	protected int x;
	
	/** Y coordinate of center of the ball*/
	protected int y;
	
	/** Variable that determines pixel amount of 
	 * horizontal movement of the ball per delay time */
	protected int dx=-1;

	/** Variable that determines pixel amount of 
	 * vertical movement of the ball per delay time */
	protected int dy=-5;
	
	/** Image to show actual ball.*/
	protected Image image;
	
	/** Graphics object used to implement off-screen drawing.*/
	protected Graphics offscreen;
	
	/**Variable used to hold applet's dimensions.*/
	protected Dimension d;
	
	/**Thread that refreshes screen every delay seconds.*/
	protected Thread bouncingThread;
	
	/**
	 * Initial method called after applet initialized.
	 * Dimension of the applet screen is determined. 
	 * 'delay' parameter from applet parameters is read.
	 * @see java.applet.Applet
	 */
	public void init(){
		String att = getParameter("delay");
		if(att != null){
			delay = Integer.parseInt(att);
		}
		d = getSize();
		x = d.width * 2 / 3;
		y = d.height - radius;
	}
	
	/**
	 * Calculate the new location of the ball.
	 * Draws the off-screen Image in the memory first. 
	 * Then draws that image on the applet.
	 * Use {@link #createImage(int, int)} to generate off-screen image.	 
	 * @param  g Graphics object that rendering the off-screen image.
	 * @see java.applet.Applet 
	 * @see Graphics
	 * @see Image
	 */
	public void update(Graphics g){
		if(image == null){
			image = createImage(d.width, d.height);
			offscreen = image.getGraphics();
		}
		offscreen.setColor(Color.white);
		offscreen.fillRect(0, 0, d.width, d.height);
		if(x < radius || x > d.width - radius){
			dx = -dx;
		}
		
		if(y < radius || y > d.height - radius){
			dy = -dy;
		}
		x += dx;
		y += dy;
		
		offscreen.setColor(color);
		offscreen.fillOval(x-radius, y - radius, radius * 2, radius * 2);
		g.drawImage(image, 0, 0, this);
	}
	
	/**
	 * This method is called implicitly by the thread that updates 
	 * screen to create animation. paint method is invoked when thread repaint 
	 * method is called.
	 * Use {@link #update(Graphics)} to animate.	 
	 * @param  g Graphics object to update 
	 * @see java.applet.Applet 
	 * @see Graphics
	 */
	public void paint(Graphics g){
		update(g);
	}
		
	/**
	 * Initializes animation by initializing bouncingThread and activating it.
	 * Called implicitly in lifecycle of applet.
	 * @see java.applet.Applet
	 */
	public void start(){
		bouncingThread = new Thread(this);
		bouncingThread.start();
	}
	
	/**
	 * Stop animation. Called implicitly in lifecycle of applet.
	 * Assign null to bouncingThread to stop it. It gets collected 
	 * by garbage collector. 
	 * @see java.applet.Applet
	 */	
	public void stop(){
		bouncingThread = null;
	}
	
	/**
	 * Runs an infinite loop. Wakes the thread up every delay milliseconds,
	 * animate screen (by calling repaint()) then sleep the worker thread.
	 * Called implicitly when thread's start method is called. 
	 * @see Thread
	 */
	public void run() {
		while(Thread.currentThread() == bouncingThread){
			try{
				Thread.sleep(delay);				
			}catch(InterruptedException e){}
			repaint();
		}
		
	}
	
}
