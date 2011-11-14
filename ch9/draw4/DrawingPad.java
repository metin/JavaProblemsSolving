package draw4;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import draw1.LineShape;
import draw1.OvalShape;
import draw1.RectangleShape;
import draw1.ToolKit;
import draw2.*;
import draw3.KeyboardDrawingCanvas;
import scribble3.*;

public class DrawingPad extends draw3.DrawingPad {

  public DrawingPad() {
    super();
    JMenu optionMenu = menuBar.getMenu(2);
    addLineOptions(optionMenu);
  }

  protected void initTools() {
    super.initTools();
    toolkit.addTool(new TwoEndsShapeTool(canvas, "Eraser", new Eraser()));
  }


  protected void addLineOptions(JMenu optionMenu) {
    String[] lineThicknesses = { "0.1", "2", "8", "15", "30" };

    ActionListener lineThicknessAction = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof JCheckBoxMenuItem) {
          JCheckBoxMenuItem mi = (JCheckBoxMenuItem) source;
          String name = mi.getText();
          drawingCanvas.setCurThickness(Float.parseFloat(name));
        }
      }
    };
    JMenu lineStyleMenu = new JMenu("Line Thicknes");
    ButtonGroup group = new ButtonGroup();
    for (int i = 0; i < lineThicknesses.length; i++) {
      JCheckBoxMenuItem mi = new JCheckBoxMenuItem(lineThicknesses[i]);
      lineStyleMenu.add(mi);
      mi.addActionListener(lineThicknessAction);
      group.add(mi);
    }
    optionMenu.add(lineStyleMenu);

  }


}
