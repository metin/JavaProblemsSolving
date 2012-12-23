package ex87;

import java.io.*;
import java.util.zip.*;

public class ZipCompressor implements Compressor {
	static final int BUFFER = 2048;

	public ZipCompressor() {

	}

	public void compress(String source, String destination) throws IOException {
		BufferedInputStream origin = null;
		FileOutputStream dest = new FileOutputStream(destination);
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
		byte data[] = new byte[BUFFER];

		System.out.println("Compressing: " + source);
		FileInputStream fi = new FileInputStream(source);
		origin = new BufferedInputStream(fi, BUFFER);
		ZipEntry entry = new ZipEntry(source);
		out.putNextEntry(entry);
		int count;
		while ((count = origin.read(data, 0, BUFFER)) != -1) {
			out.write(data, 0, count);
		}
		origin.close();
		out.close();
	}

}
