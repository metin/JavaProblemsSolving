package draw4;

import static org.junit.Assert.*;
import java.awt.BasicStroke;
import org.junit.Test;
import scribble3.ScribbleCanvas;

public class TestThickness {

  @Test
  public void test() {
    ScribbleCanvas canvas = new ScribbleCanvas();
    canvas.setCurThickness(new Float(5));
    assertTrue(canvas.getCurThickness().equals(new BasicStroke(5)));
  }

}
