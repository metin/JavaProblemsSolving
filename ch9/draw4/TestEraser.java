package draw4;

import static org.junit.Assert.*;
import org.junit.Test;
import draw2.*;
import scribble3.*;

public class TestEraser {

  @Test
  public void test() {
    ScribbleCanvas canvas = new ScribbleCanvas();
    TwoEndsShapeTool eraserTool = new TwoEndsShapeTool(canvas, "Eraser", new Eraser());
    assertTrue(eraserTool.getCurrentShape() instanceof Eraser);
  }

}
