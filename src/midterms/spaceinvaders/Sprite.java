package spaceinvaders;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Sprite {

        private boolean visible;
        private Image image;
        protected int x;
        protected int y;
        protected boolean dying;
        protected int dx;
        protected Applet container;
        protected AudioClip dyingSound = null;
        protected AudioClip newSound = null;
        
        public Sprite() {
            visible = true;
        }
        
        public Sprite(Applet applet){
          this.container = applet;
          visible = true;
        }
        public void die() {
            visible = false;
        }

        public boolean isVisible() {
            return visible;
        }

        protected void setVisible(boolean visible) {
            this.visible = visible;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Image getImage() {
            return image;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }
        public int getY() {
            return y;
        }

        public int getX() {
            return x;
        }

        public void setDying(boolean dying) {
            this.dying = dying;
            if(dyingSound != null && dying)
              dyingSound.play();
        }

        public boolean isDying() {
            return this.dying;
        }
        public ImageIcon getImageByName(String img){
          return new ImageIcon(this.container.getImage(this.container.getCodeBase(), img));
        }
        
        public boolean hasDyingSound(){
          return dyingSound != null;
        }
        
        public boolean hasInitiationSound(){
          return newSound != null;
        }
                
        public void afterCreated(){
          if(newSound != null)
            newSound.play();
        }
}