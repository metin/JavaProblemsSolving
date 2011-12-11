package spaceinvaders;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;


public class SpaceInvadersApplet extends Applet implements Commons {

    public SpaceInvadersApplet()
    {
      setLayout(new BorderLayout());
    }
    
    public void init(){
      Board b = new Board(this);
      Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
      b.setSize(d);
      setFocusable(true);
      add(b, BorderLayout.CENTER);
      
    }

}