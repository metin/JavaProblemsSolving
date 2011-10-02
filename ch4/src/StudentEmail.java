import java.io.*;
import java.util.*;

public class StudentEmail
{
	public static void main(String[] args) {
		if(args.length != 2)
		{
			System.out.println("Usage:");
			System.out.println("java StudentEmail students.txt studentemail.txt");
			System.exit(1);
		}
		
		for (String arg : args) {
			System.out.println(arg);
		}
		convertFile(args[0], args[1]);
	}
	
	public static void convertFile(String srcFile, String destFile)
	{
		ArrayList<Student> students = loadStudents(srcFile);
		createEmailsFile(destFile, students);
	}
	
	public static ArrayList<Student> loadStudents(String srcFile){
		 ArrayList<Student> students = new ArrayList<Student>();

		try{
			  BufferedReader in = new BufferedReader(new BufferedReader(new FileReader(srcFile)));
			  String strLine;
			  while ((strLine = in.readLine()) != null)   {
				  System.out.println (strLine);
				  String[] vals = strLine.split(":");
				  students.add(new Student(vals[0], vals[1], vals[2]));
			  }
			  in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		 return students;
	}

	public static void createEmailsFile(String destFile,  ArrayList<Student> students){
		 
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(destFile)));
			for (Student s : students) {
				out.println(s.generateEmail());
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


class Student{
	
	public String firstName, lastName, ssn; 
	public Student(){
		
	}
	public Student(String fName, String lName, String socialn){
		firstName = fName;
		lastName = lName; 
		ssn = socialn; 
	}
	
	public String generateEmail(){
		String email = firstName.substring(0, 1).toLowerCase();
		email += lastName.substring(0, 1).toLowerCase();
		email += ssn.substring(ssn.length()-4);
		email += "@se.depaul.edu";
		return email;
	}
}



