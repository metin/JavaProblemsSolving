package ex85;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;


public class Calculator extends Applet
{
	JTextField display = new JTextField();
	public Calculator() {
		BorderLayout layout = new BorderLayout();
        setLayout(layout);
        
        JPanel displayPanel = new JPanel(new BorderLayout(50, 50));
        displayPanel.setBorder(BorderFactory.createEtchedBorder());
        displayPanel.add(display, BorderLayout.CENTER);

        Font newTextFieldFont = new Font(display.getFont().getName(), display.getFont().getStyle(), 20);  
        
        //Set JTextField font using new created font  
        display.setFont(newTextFieldFont);
        
        JPanel clearButtonPanel = new JPanel(new BorderLayout());

        JButton btn = new JButton("C");
        btn.setActionCommand("C");
        clearButtonPanel.add(btn, BorderLayout.EAST);

        btn = new JButton("AC");
        btn.setActionCommand("AC");
        clearButtonPanel.add(btn, BorderLayout.WEST);
        displayPanel.add(clearButtonPanel, BorderLayout.EAST);
        
        add(displayPanel, BorderLayout.NORTH);
        
        JPanel numpad = new JPanel(new GridLayout(1, 4));

        JPanel numpadCol1 = new JPanel(new GridLayout(4, 1));
        btn = new JButton("7");
        btn.setActionCommand("7");
        numpadCol1.add(btn);
        btn = new JButton("4");
        btn.setActionCommand("4");
        numpadCol1.add(btn);
        btn = new JButton("1");
        btn.setActionCommand("1");
        numpadCol1.add(btn);
        btn = new JButton("0");
        btn.setActionCommand("0");
        numpadCol1.add(btn);
        numpad.add(numpadCol1);
        
        JPanel numpadCol2 = new JPanel(new GridLayout(4, 1));
        btn = new JButton("8");
        btn.setActionCommand("8");
        numpadCol2.add(btn);
        btn = new JButton("5");
        btn.setActionCommand("5");
        numpadCol2.add(btn);
        btn = new JButton("2");
        btn.setActionCommand("2");
        numpadCol2.add(btn);
        btn = new JButton(".");
        btn.setActionCommand(".");
        numpadCol2.add(btn);

        numpad.add(numpadCol2);
        JPanel numpadCol3 = new JPanel(new GridLayout(4, 1));
        btn = new JButton("9");
        btn.setActionCommand("9");
        numpadCol3.add(btn);
        btn = new JButton("6");
        btn.setActionCommand("6");
        numpadCol3.add(btn);
        btn = new JButton("3");
        btn.setActionCommand("3");
        numpadCol3.add(btn);
        btn = new JButton("=");
        btn.setActionCommand("=");
        numpadCol3.add(btn);
        numpad.add(numpadCol3);
        
        JPanel numpadCol4 = new JPanel(new GridLayout(4, 1));
        btn = new JButton("/");
        btn.setActionCommand("d");
        numpadCol4.add(btn);
        btn = new JButton("*");
        btn.setActionCommand("m");
        numpadCol4.add(btn);
        btn = new JButton("-");
        btn.setActionCommand("s");
        numpadCol4.add(btn);
        btn = new JButton("+");
        btn.setActionCommand("a");
        numpadCol4.add(btn);
        numpad.add(numpadCol4);
        add(numpad, BorderLayout.CENTER);
        ButtonEventHandler bhandler = new ButtonEventHandler(); 
        bhandler.beActionListener(this);
	}
	
    class ButtonEventHandler implements ActionListener {
    	String nums = "1234567890.";
    	String ops = "asdm";
    	Calculation calc = null;
    	Boolean reset = false;
    	String lastOperation = "+";
    	Boolean executed = false;
	    public void actionPerformed(ActionEvent event) {
	      if (event!=null)
	        System.out.println("ActionPerformed: " + event); 
	      JButton source = (JButton) event.getSource(); 
	      if (source != null){
	    	  String cmd = source.getActionCommand();
	    	  if(nums.indexOf(cmd) > -1){
	    		  if(reset){
	    			  reset = false;
	    			  display.setText("");
	    		  }
	    		  display.setText(display.getText() + cmd); 
	    	  }else if(ops.indexOf(cmd) > -1){
	    		  if(calc == null)
	    			  calc = new Add(new BaseOperation(), Float.parseFloat(display.getText()));
	    		  else if(!executed){
		    		  doOperation(cmd.trim());
		    		  display.setText( String.valueOf(calc.calculate()));
	    		  }
	    		  lastOperation = cmd;
	    		  reset = true;
	    		  executed = false;
	    	  }else if(cmd == "="){
	    		  doOperation(lastOperation);
	    		  display.setText( String.valueOf(calc.calculate()));
	    		  executed = true;
	    		  reset = true;
	    	  }else if(cmd == "C"){
	    		  display.setText("");
	    	  }else if(cmd == "AC"){
	    		  display.setText("");
	    		  calc = null;
	    	  }
	       }
	    }
	    
	    protected void doOperation(String operator){
  		  if(operator == "a")
			  calc = new Add(calc, Float.parseFloat(display.getText()));
		  else if (operator.equals("s"))
			  calc = new Subtract(calc, Float.parseFloat(display.getText()));
		  else if (operator=="d")
			  calc = new Divide(calc, Float.parseFloat(display.getText()));
		  else if (operator=="m")
			  calc = new Multiply(calc, Float.parseFloat(display.getText()));
  		}
	    
	    protected void beActionListener(Component comp) {
	      if (comp != null) {
	        if (comp instanceof JButton) {
	          JButton button = (JButton) comp;
	          button.addActionListener(this);  
	        } else if (comp instanceof Container) {
	          Container container = (Container) comp; 
	          int n = container.getComponentCount();
	          for (int i = 0; i < n; i++)  
	            beActionListener(container.getComponent(i));
	        }        
	      }
	    }    
    }

}
