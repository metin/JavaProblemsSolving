package spaceinvaders;

import java.applet.*;
import java.net.*;

public class AudioUtility {

  public static final AudioClip getAudioClip(String filename) {
    if (filename != null) {
      try {
        return Applet.newAudioClip(new URL("http://harp.njit.edu/~my67/cs602/chapter10/" + filename));
      } catch (MalformedURLException e) {
        System.err.println(e.getMessage());
      }
    }
    return null;
  }

}
