package ex87;

public class Zip {
   static final int BUFFER = 2048;
   public static void main (String argv[]) {
	  String source = argv[0];
	  String destination = argv[1];
	  String format = argv[2];
	  
      try {
    	  Zipper z = new Zipper(source, destination, format);
    	  z.compress();
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
}
