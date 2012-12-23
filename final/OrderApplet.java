
import java.awt.*;

import ui.*;

public class OrderApplet extends java.applet.Applet
{

  // Create the User Interface
  public void init ()
  {
    this.setLayout (new BorderLayout ());
    Panel p = new ContentPanel(this);
    this.add(p, BorderLayout.CENTER);
  }

}
