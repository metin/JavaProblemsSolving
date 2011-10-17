package ex65;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class StackApplet extends Applet implements ActionListener
{
	JPanel stackContent;
	Stack stack = new Stack();

	DefaultListModel listModel = new DefaultListModel();  
	JList stackList = new JList(listModel);
	JTextField val = new JTextField(5);
	JLabel topDisplay = new JLabel("Top:");
	JLabel sizeDisplay = new JLabel("Size:");
	public void init() {

        GridBagLayout layout = new GridBagLayout();
        
        setLayout(layout);
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
       
        add(val, c);
        
        c = new GridBagConstraints();
        JButton btn = new JButton("Push");
        c.gridx = 0;c.gridy = 1;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        add(btn, c);
        btn.setActionCommand("PUSH");
        btn.addActionListener(this);
 
        JButton btnClear = new JButton("Clear Screen");
        c.gridx = 0;c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        add(btnClear, c);
        btnClear.setActionCommand("CLEAR_SCR");
        btnClear.addActionListener(this);
 
        c = new GridBagConstraints();        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;c.gridy = 0;
        add(new JLabel("Stack:"));
        
        c = new GridBagConstraints();     
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;c.gridy = 1;
        c.gridheight = 3;
        
        stackList.setLayoutOrientation(JList.VERTICAL);
        stackList.setVisibleRowCount(500);
        
        JScrollPane listScroller = new JScrollPane(stackList);
        listScroller.setPreferredSize(new Dimension(250, 350));

        add(listScroller, c);   

        c = new GridBagConstraints();     
        JButton btnPop = new JButton("Pop");
        c.gridx = 2;c.gridy = 0;
        add(btnPop, c);
        btnPop.setActionCommand("POP");
        btnPop.addActionListener(this);

        c = new GridBagConstraints();     
        c.gridx = 2;c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        add(topDisplay, c);

        c = new GridBagConstraints();     
        c.gridx = 2;c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        add(sizeDisplay, c);
    }

    public void actionPerformed(ActionEvent evt)
    {	
    	if(evt.getActionCommand() == "PUSH")
    		push();
    	else if(evt.getActionCommand() == "CLEAR_SCR")
    		listModel.clear();
    	else
    		pop();	
    }
    
    private void push(){
    	if(!val.getText().isEmpty()){
			stack.push(val.getText());
			updateDisplay(stack.top() + " was pushed");
			val.setText("");
    	}
    }

    private void pop(){
    	if(!stack.isEmpty()){
			String s = (String)stack.pop();
			updateDisplay(s + " was popped.");
    	}
    }
    
    private void updateDisplay(String msg){
    	listModel.addElement(msg);
    	sizeDisplay.setText("Size: " + stack.size());
    	topDisplay.setText("Top:" + stack.top());
    }
    
}
