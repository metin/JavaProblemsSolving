
package adapter;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;
import javax.swing.*; 

public class Main {

  static Student[] students = {
    new Student("1001", "Bill", "Gates", 
		"1 Microsoft Way", "WA", "Redmond", "USA", "65432", 
		"555-123-4567", 3.9f, 32),
    new Student("1002", "Steve", "Jobs", 
		"100 Next Drive", "CA", "Orchidville", "USA", "79910", 
		"321-654-4567", 3.7f, 24),
    new Student("1003", "Scott", "McNealy", 
		"123 Main Street", "CA", "Sunnyville", "USA", "90715", 
		"590-298-4262", 3.5f, 48),
    new Student("1004", "Larry", "Ellison", 
		"321 North Blvd.", "CA", "Sea Side", "USA", "23456", 
		"808-750-8955", 3.2f, 88),
    new Student("1005", "Paul", "Allen", 
		"51 Garden Street", "OR", "Protland", "USA", "36845", 
		"455-757-7311", 3.9f, 144),
    new Student("1006", "Thomas", "Jackson", 
		"543 Lake Ave.", "IL", "Plainville", "USA", "80108", 
		"103-367-4105", 2.1f, 72),
    new Student("1007", "Jim", "Barksdale", 
		"789 Bay Street", "CA", "Any Town", "USA", "34191", 
		"156-303-8166", 2.5f, 84),
    new Student("1008", "Marc", "Andreesen", 
		"333 Westgate Ave.", "IL", "Old Town", "USA", "33081", 
		"430-488-0931", 3.7f, 24),
    new Student("1009", "David", "Boise", 
		"433 K Street", "DC", "Washington", "USA", "90324", 
		"981-981-8493", 3.5f, 32),
    new Student("1010", "James", "Gosling", 
		"1 Oak Street", "CA", "Java Island", "USA", "98650", 
		"516-192-9406", 4.0f, 64),
    new Student("1011", "Chris", "Galvin", 
		"768 My Street", "IL", "Northfield", "USA", "37857", 
		"272-666-5555", 2.9f, 32),
    new Student("1012", "Linus", "Torvalds", 
		"53884 Norht Sea Drive", "CA", "Bay Side", "USA", "98260", 
		"815-150-3179", 3.9f, 20),
    new Student("1013", "Gordon", "Moore", 
		"654 Moore Street", "FL", "Any Town", "USA", "71333", 
		"880-310-0516", 3.8f, 12),
    new Student("1014", "Jerry", "Young", 
		"748 Hillside Blvd.", "CA", "Yahooville", "USA", "91578", 
		"397-716-6169", 3.5f, 104),
    new Student("1015", "Eric", "Gamma", 
		"897 Central Street", "NM", "Any Town", "USA", "27351", 
		"431-878-7706", 3.6f, 136),
    new Student("1016", "Richard", "Helm", 
		"567 Long Blvd.", "NY", "My Town", "USA", "27150", 
		"640-597-8608", 3.3f, 56),
    new Student("1017", "Ralph", "Johnson", 
		"446 Main Street", "IL", "Middle Town", "USA", "93686", 
		"252-438-9179", 3.8f, 64),
    new Student("1018", "John", "Valissides", 
		"775 North Blvd.", "NY", "My City", "USA", "33595", 
		"864-969-7578", 2.7f, 28),
    new Student("1019", "James", "Coplien", 
		"387 South Street", "IL", "Any Town", "USA", "49432", 
		"741-968-7355", 3.9f, 100),
    new Student("1020", "Mitchell", "Kapor", 
		"4328 Central Blvd.", "MA", "Sea Side", "USA", "71126", 
		"230-525-1849", 3.1f, 44),
    new Student("1021", "Donald", "Knuth", 
		"723 Long Blvd.", "CA", "See City", "USA", "74646", 
		"719-915-6393", 4.0f, 172),
  };

  public static final int INITIAL_FRAME_WIDTH = 800;
  public static final int INITIAL_FRAME_HEIGHT = 400;

  public static void main(String[] args) { 
    boolean useDelegation = false; 
    if (args.length > 0 &&
	"Delegation".equals(args[0])) { 
      useDelegation = true;
    }

    List entries = new ArrayList(students.length); 
    for (int i = 0; i < students.length; i++) { 
      if (useDelegation) {
	entries.add(new StudentEntry2(students[i])); 
      } else { 
	entries.add(new StudentEntry(students[i])); 
      }
    }

    Table table = new Table(entries); 

    JFrame frame = new JFrame("Students");
    frame.setContentPane(new JScrollPane(table)); 
    frame.setSize(INITIAL_FRAME_WIDTH, INITIAL_FRAME_HEIGHT);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(screenSize.width / 2 - INITIAL_FRAME_WIDTH / 2, 
		      screenSize.height / 2 - INITIAL_FRAME_HEIGHT / 2);    
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
    frame.setVisible(true); 
  }

}
