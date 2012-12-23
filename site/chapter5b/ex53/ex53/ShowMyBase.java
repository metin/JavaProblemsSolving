package ex53;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class ShowMyBase extends Applet implements ActionListener
{
	JLabel txtCodeBase, txtDocBase;
    public void init() {

//    	try {
//		     // Set cross-platform Java L&F (also called "Metal")
//    		UIManager.setLookAndFeel(
//    				UIManager.getCrossPlatformLookAndFeelClassName());
//		}
//		catch (Exception e) {}

    	 
        GridBagLayout Layout = new GridBagLayout();
        setLayout (Layout);
        
        GridBagConstraints c = new GridBagConstraints();
        JButton btn = new JButton("Show Bases");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;c.gridy = 0;
        c.ipady = 5;      
        c.gridwidth = 2;
        add(btn, c);
        btn.addActionListener(this);
 
        c = new GridBagConstraints();        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 5; 
        c.gridx = 0;c.gridy = 1;
        add(new JLabel("Code base:"), c);
        
        c = new GridBagConstraints();     
        c.ipady = 5; 
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;c.gridy = 1;
        txtCodeBase = new JLabel();
        txtCodeBase.setBackground(Color.yellow);

        add(txtCodeBase, c);   

        c = new GridBagConstraints();     
        c.ipady = 5; 
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;c.gridy = 2;
        add(new JLabel("Doc base:"), c);
        
        c = new GridBagConstraints();    
        c.ipady = 5; 
        txtDocBase = new JLabel();
        txtDocBase.setBackground(Color.white);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;c.gridy = 2;
        add(txtDocBase, c);  
        
    }

    public void actionPerformed(ActionEvent evt)
    {	
    	displayCodeBase();
    	displayDocumentBase();    
    }
    
    private void displayCodeBase(){
    	//URL url = new URL(, );
		txtCodeBase.setText(getCodeBase().toString());    	  
    }

    private void displayDocumentBase(){
		txtDocBase.setText(getDocumentBase().toString());     	  
    }
}






