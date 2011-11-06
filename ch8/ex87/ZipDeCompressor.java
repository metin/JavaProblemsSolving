package ex87;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

public class ZipDeCompressor implements DeCompressor {
	static final int BUFFER = 2048;

	public ZipDeCompressor() {

	}

	public void decompress(String source, String destination) throws IOException {
		BufferedOutputStream dest = null;
		BufferedInputStream is = null;
		ZipEntry entry;
		ZipFile zipfile = new ZipFile(source);
		Enumeration e = zipfile.entries();
		while (e.hasMoreElements()) {
			entry = (ZipEntry) e.nextElement();
			System.out.println("Extracting: " + entry);
			is = new BufferedInputStream(zipfile.getInputStream(entry));
			int count;
			byte data[] = new byte[BUFFER];
			FileOutputStream fos = new FileOutputStream(destination + entry.getName());
			dest = new BufferedOutputStream(fos, BUFFER);
			while ((count = is.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
			is.close();
		}
	}

}
