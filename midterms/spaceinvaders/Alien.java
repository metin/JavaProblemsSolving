package spaceinvaders;

import java.applet.Applet;

import javax.swing.ImageIcon;


public class Alien extends Sprite {

    private Bomb bomb;
    private final String shot = "spacepix/alien.png";

    public Alien(int x, int y, Applet applet) {
        this.x = x;
        this.y = y;
        this.container = applet;
        bomb = new Bomb(x, y, applet);
        ImageIcon ii = getImageByName(shot);
        setImage(ii.getImage());
        dyingSound = AudioUtility.getAudioClip("audio/laser.wav");

    }

    public void act(int direction) {
        this.x += direction;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public class Bomb extends Sprite {

        private final String bomb = "spacepix/bomb.png";
        private boolean destroyed;

        public Bomb(int x, int y, Applet applet) {
            this.container = applet;
            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon ii = getImageByName(bomb);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }
}