import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Average extends Applet implements ActionListener
{
	TextField txtNum;
	TextArea txtResult, txtNumbers;
	String numberSourceFile = "test.txt";
	StringBuffer numberStream;
    public void init() {
        GridBagLayout Layout = new GridBagLayout();
        setLayout (Layout);
        
        GridBagConstraints c = new GridBagConstraints();

        c = new GridBagConstraints();
        txtNumbers = new TextArea(6, 40);
        txtNumbers.setEditable(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;c.gridy = 0;
        add(txtNumbers, c);   

        Button btn = new Button("Calculate");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;c.gridy = 1;
        c.ipady = 20;
        add(btn, c);
        btn.addActionListener(this);
        
        txtResult = new TextArea(2, 40);
        txtResult.setEditable(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;c.gridy = 2;
        c.insets = new Insets(10,0,0,0); 
        add(txtResult, c);   
        String paramFile = getParameter("FILENAME");
        if (paramFile != null)
            numberSourceFile = paramFile;
        
        readFile();
    }

    public void actionPerformed(ActionEvent evt)
    {	
    	try{
    		txtResult.append("Reading file: " + numberSourceFile);
			txtResult.append("\n");
			txtResult.append("Number of integers: "+Integer.toString(numberOfInegers()));
			txtResult.append("\n");
			txtResult.append("Average: "+Double.toString(calculateAverage()));
			txtResult.append("\n");
    	}
    	catch (NumberFormatException nfe)
        {
    		txtResult.append(numberSourceFile+" contains invalid data.");
    		txtResult.append("\n");
            // txtResult.append(nfe.getMessage());
            // txtResult.append("\n");
            // StackTraceElement[] ste = nfe.getStackTrace();
            // for(int i=0; i < ste.length; i++){
            //     txtResult.append(ste[i].toString());        
            //     txtResult.append("\n");
            //}
        }
    }
    
    private double calculateAverage(){
    	return total()*1.0/numberOfInegers();
    }	
    
    private int numberOfInegers(){
		return numbers().size();
    }
    
    private int total(){
    	ArrayList<Integer> numbers = numbers();
    	int result = 0;
		for(int i=0; i<numbers.size(); i++)
			result += numbers.get(i);   
		return result;
    }
    
    private ArrayList<Integer> numbers(){
		String[] vals = txtNumbers.getText().split("\n");
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for(int i=0; i<vals.length; i++)
            if(!vals[i].isEmpty())
			 numbers.add(Integer.parseInt(vals[i]));
		return numbers; 
    }

    private void readFile(){
    	  String line;
    	  URL url = null;
    	  try{
    		  url = new URL(getCodeBase(), numberSourceFile);
    	  }
    	  catch(MalformedURLException e){}
    	  
    	  try{
	    	  InputStream in = url.openStream();
	    	  BufferedReader bf = new BufferedReader(new InputStreamReader(in));
	    	  numberStream = new StringBuffer();
	    	  while((line = bf.readLine()) != null){
	    		  numberStream.append(line + "\n");
	    	  }
	    	  txtNumbers.append(numberStream.toString());
    	  }
    	  catch(IOException e){
    		  txtResult.append("\nCould not read file " + numberSourceFile);
    	  }
    }
}
