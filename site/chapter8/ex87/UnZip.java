package ex87;

public class UnZip {

	public static void main(String argv[]) {
		String source = argv[0];
		String destination = argv[1];
		String format = argv[2];

		try {
			UnZipper uz = new UnZipper(source, destination, format);
			uz.decompress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
