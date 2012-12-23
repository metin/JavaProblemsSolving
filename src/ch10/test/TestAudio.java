
package test;

import java.applet.*; 
import java.net.*;

public class TestAudio { 

  //static JApplet applet = new JApplet(); 
  
  public static final void main(String[] args) { 
    try { 
      AudioClip clip = Applet.newAudioClip(new URL("file:" + args[0]));
      clip.play();
    } catch (MalformedURLException e) { 
      e.printStackTrace();
    }
  }

}
