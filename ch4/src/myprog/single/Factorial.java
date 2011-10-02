package myprog.single;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Factorial extends Applet implements ActionListener
{
	TextField txtNum;
	Label lblResult;
    public void init() {
        GridBagLayout Layout = new GridBagLayout();
        setLayout (Layout);
        
        GridBagConstraints c = new GridBagConstraints();
        txtNum = new TextField();
        txtNum.setSize(400, 30);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 30;
        c.weightx = 2;
        add(txtNum, c);
        
        c = new GridBagConstraints();
        Button btn = new Button("Calculate");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        add(btn, c);
        btn.addActionListener(this);

        c = new GridBagConstraints();
        lblResult = new Label("Result:");
        lblResult.setSize(200, 30);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(10,0,0,0); 
        add(lblResult, c);   
    }

    public void actionPerformed(ActionEvent evt)
    {	
    	try{
	    	if(isValidInput()){
	    		int val = calculate();
		    	lblResult.setText("Result:"+Integer.toString(val));
	    	}else{
	    		lblResult.setText("Error:Not valit input");
	    	}
    	}
    	catch (NumberFormatException nfe)
        {
    		lblResult.setText("Error:Colud not calculate.");
        }
    }
    

    private int calculate()
    {	
    	int result = 1;
    	int val = Integer.parseInt(txtNum.getText());
    	while(val > 0){
    		result *= val--;
    	}
    	return result;
    }
    
    private boolean isValidInput()
    {
    	String val = txtNum.getText();
    	if(val == null || val.isEmpty())
    		return false;
    	return true;
    }
}
