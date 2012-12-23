package spaceinvaders;

import java.applet.Applet;
import java.applet.AudioClip;

import javax.swing.ImageIcon;


public class Shot extends Sprite {

    private String shot = "spacepix/shot.png";
    private final int H_SPACE = 6;
    private final int V_SPACE = 1;
    
    
    public Shot(Applet a) {
      this.container = a;
      newSound = AudioUtility.getAudioClip("audio/adapt-or-die.au");

    }

    public Shot(int x, int y, Applet a) {
        this.container = a;
        newSound = AudioUtility.getAudioClip("audio/adapt-or-die.au");

        //ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
        ImageIcon ii = getImageByName(shot);

        setImage(ii.getImage());
        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }
    

}
