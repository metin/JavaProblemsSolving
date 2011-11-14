package scribble3;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Scribble extends Applet {

  public Scribble() {

    // calling factory method
    canvas = makeCanvas();
    setLayout(new BorderLayout());
    menuBar = createMenuBar();
    add(menuBar, BorderLayout.NORTH);
    add(canvas, BorderLayout.CENTER);

//    AccessController.doPrivileged(new PrivilegedAction<Object>() {
//      @Override
//      public Object run() {
//        chooser = new JFileChooser();
//        return null;
//      }     
//    });
    // chooser = new JFileChooser();
  }

  protected JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu;
    JMenuItem mi;

    // File menu
    menu = new JMenu("File");
    menuBar.add(menu);

    mi = new JMenuItem("New");
    menu.add(mi);
    mi.addActionListener(new NewFileListener());

    mi = new JMenuItem("Open");
    menu.add(mi);
    mi.addActionListener(new OpenFileListener());

    mi = new JMenuItem("Save");
    menu.add(mi);
    mi.addActionListener(new SaveFileListener());

    mi = new JMenuItem("Save As");
    menu.add(mi);
    mi.addActionListener(new SaveAsFileListener());

    menu.add(new JSeparator());

    exitAction = new ExitListener();
    mi = new JMenuItem("Exit");
    menu.add(mi);
    mi.addActionListener(exitAction);

    // option menu
    menu = new JMenu("Option");
    menuBar.add(menu);

    mi = new JMenuItem("Color");
    menu.add(mi);
    mi.addActionListener(new ColorListener());

    // horizontal space
    menuBar.add(Box.createHorizontalGlue());

    // Help menu
    menu = new JMenu("Help");
    menuBar.add(menu);

    mi = new JMenuItem("About");
    menu.add(mi);
    mi.addActionListener(new AboutListener());

    return menuBar;
  }

  // factory method
  protected ScribbleCanvas makeCanvas() {
    return new ScribbleCanvas();
  }

  protected void newFile() {
    currentFilename = null;
    canvas.newFile();

  }

  protected void openFile(String filename) {
    currentFilename = filename;
    canvas.openFile(filename);

  }

  protected void saveFile() {
    if (currentFilename == null) {
      currentFilename = "Untitled";
    }
    canvas.saveFile(currentFilename);

  }

  protected void saveFileAs(String filename) {
    currentFilename = filename;
    canvas.saveFile(filename);

  }

  class NewFileListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      newFile();
    }

  }

  class OpenFileListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      int retval = chooser.showDialog(Scribble.this, "Open");
      if (retval == JFileChooser.APPROVE_OPTION) {
        File theFile = chooser.getSelectedFile();
        if (theFile != null) {
          if (theFile.isFile()) {
            String filename = chooser.getSelectedFile().getAbsolutePath();
            openFile(filename);
          }
        }
      }
    }

  }

  class SaveFileListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      saveFile();
    }

  }

  class SaveAsFileListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      int retval = chooser.showDialog(Scribble.this, "Save As");
      if (retval == JFileChooser.APPROVE_OPTION) {
        File theFile = chooser.getSelectedFile();
        if (theFile != null) {
          if (!theFile.isDirectory()) {
            String filename = chooser.getSelectedFile().getAbsolutePath();
            saveFileAs(filename);
          }
        }
      }
    }

  }

  class ExitListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      int result = JOptionPane.showConfirmDialog(null,
          "Do you want to exit Scribble Pad?", "Exit Scribble Pad?",
          JOptionPane.YES_NO_OPTION);
      if (result == JOptionPane.YES_OPTION) {
        saveFile();
        System.exit(0);
      }
    }

  }

  class ColorListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {

      Color newColor = JColorChooser.showDialog(null,
          "Choose Background Color", canvas.getCurColor());
      canvas.setCurColor(newColor);
    }

  }

  class AboutListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(null,
          "DrawingPad version 1.0\nCopyright (c) Xiaoping Jia 2002", "About",
          JOptionPane.INFORMATION_MESSAGE);
    }

  }

  protected ScribbleCanvas canvas;
  protected JMenuBar menuBar;
  protected JColorChooser tcc;

  protected String currentFilename = null;
  protected ActionListener exitAction;
  protected JFileChooser chooser;

  protected static int width = 600;
  protected static int height = 400;

}
