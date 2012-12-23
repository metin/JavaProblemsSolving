package test;

import java.text.DecimalFormat;

public class AddressGen {

  public static final void main(String[] args) {
    for (int i = 0; i < 20; i++) {
      int n1 = (int) (Math.random() * 1000);
      int n2 = (int) (Math.random() * 100000);
      while (n2 < 10000) {
        n2 = (int) (Math.random() * 100000);
      }
      int n3 = (int) (Math.random() * 1000);
      while (n3 < 100) {
        n3 = (int) (Math.random() * 1000);
      }
      int n4 = (int) (Math.random() * 1000);
      while (n4 < 100) {
        n4 = (int) (Math.random() * 1000);
      }
      int n5 = (int) (Math.random() * 10000);
      DecimalFormat df1 = new DecimalFormat("0000");
      String s5 = df1.format(n5);

      int n6 = ((int) (Math.random() * 50)) * 4;

      double gpa = (Math.random() * 4) + 0.01;
      DecimalFormat df2 = new DecimalFormat("0.00");
      String s7 = df2.format(gpa);

      System.out.println(n1 + "\t" + n2 + "\t" + n3 + "-" + n4 + "-" + s5
          + "\t" + n6 + "\t" + s7);
    }
  }

}
