package spaceinvaders;

import static org.junit.Assert.*;

import java.applet.Applet;

import org.junit.Test;

public class TestSounds {

  @Test
  public void testShotSounds() {
    Applet a = new Applet();
    Shot s = new Shot(a);
    assertEquals(s.hasDyingSound(), false);
    assertEquals(s.hasInitiationSound(), true);
  }

}
