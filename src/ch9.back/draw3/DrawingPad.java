

package draw3; 

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*; 
import draw2.*;
import scribble3.*;

public class DrawingPad extends draw2.DrawingPad { 

  public DrawingPad(String title) {
    super(title); 
    JMenu optionMenu = menuBar.getMenu(2); 
    addFontOptions(optionMenu); 
  }

  // factory method 
  protected ScribbleCanvas makeCanvas() {
    return (drawingCanvas = keyboardDrawingCanvas = new KeyboardDrawingCanvas()); 
  }

  protected void initTools() { 
    super.initTools();
    toolkit.addTool(new TextTool(canvas, "Text")); 
  }  

  protected void addFontOptions(JMenu optionMenu) { 
    String[] fontFamilyNames = {
      "Serif", 
      "Sans-serif",
      "Monospaced",
      "Dialog",
      "DialogInput"
    };

    int[] fontSizes = {
      8, 10, 12, 16, 20, 24, 28, 32, 40, 48, 64
    };

    String[] fontStyleNames = {
      "plain", 
      "bold", 
      "italic", 
      "bold italic" 
    };

    int i; 
    ActionListener fontFamilyAction = new ActionListener() { 
	public void actionPerformed(ActionEvent event) { 
	  Object source = event.getSource(); 
	  if (source instanceof JCheckBoxMenuItem) { 
	    JCheckBoxMenuItem mi = (JCheckBoxMenuItem) source; 
	    String name = mi.getText(); 
	    keyboardDrawingCanvas.setFontFamily(name); 	    
	  }
	}
      };
    JMenu fontFamilyMenu = new JMenu("Font family"); 
    ButtonGroup group = new ButtonGroup(); 
    for (i = 0; i < fontFamilyNames.length; i++) { 
      JCheckBoxMenuItem mi = new JCheckBoxMenuItem(fontFamilyNames[i]); 
      fontFamilyMenu.add(mi); 
      mi.addActionListener(fontFamilyAction);
      group.add(mi); 
    }
    optionMenu.add(fontFamilyMenu); 

    ActionListener fontSizeAction = new ActionListener() { 
	public void actionPerformed(ActionEvent event) { 
	  Object source = event.getSource(); 
	  if (source instanceof JCheckBoxMenuItem) { 
	    JCheckBoxMenuItem mi = (JCheckBoxMenuItem) source; 
	    String size = mi.getText(); 
	    try { 
	      keyboardDrawingCanvas.setFontSize(Integer.parseInt(size)); 	    
	    } catch (NumberFormatException e) {} 
	  }
	}
      };
    JMenu fontSizeMenu = new JMenu("Font size"); 
    group = new ButtonGroup(); 
    for (i = 0; i < fontSizes.length; i++) { 
      JCheckBoxMenuItem mi = new JCheckBoxMenuItem(Integer.toString(fontSizes[i])); 
      fontSizeMenu.add(mi); 
      mi.addActionListener(fontSizeAction);
      group.add(mi); 
    }
    optionMenu.add(fontSizeMenu); 
    
    ActionListener fontStyleAction = new ActionListener() { 
	public void actionPerformed(ActionEvent event) { 
	  Object source = event.getSource(); 
	  if (source instanceof JCheckBoxMenuItem) { 
	    JCheckBoxMenuItem mi = (JCheckBoxMenuItem) source; 
	    String styleName = mi.getText(); 
	    int style = Font.PLAIN;
	    if (styleName.equals("bold")) { 
	      style = Font.BOLD; 
	    } else if (styleName.equals("italic")) { 
	      style = Font.ITALIC;
	    } else if (styleName.equals("bold italic")) { 
	      style = Font.BOLD | Font.ITALIC; 
	    }
	    keyboardDrawingCanvas.setFontStyle(style); 	    
	  }
	}
      };
    JMenu fontStyleMenu = new JMenu("Font style"); 
    group = new ButtonGroup(); 
    for (i = 0; i < fontStyleNames.length; i++) { 
      JCheckBoxMenuItem mi = new JCheckBoxMenuItem(fontStyleNames[i]); 
      fontStyleMenu.add(mi); 
      mi.addActionListener(fontStyleAction);
      group.add(mi); 
    }
    optionMenu.add(fontStyleMenu); 
  }

  protected KeyboardDrawingCanvas keyboardDrawingCanvas; 

  public static void main(String[] args) {
    JFrame frame = new draw3.DrawingPad("Drawing Pad");
    frame.setSize(width, height);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(screenSize.width/2 - width/2,
		      screenSize.height/2 - height/2);
    frame.show();
  }

}
