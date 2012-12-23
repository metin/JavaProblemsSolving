package ex87;

import java.io.*;
import java.util.zip.*;

public class GZipDeCompressor implements DeCompressor {
	static final int BUFFER = 2048;

	public void decompress(String source, String destination) throws IOException {
		 GZIPInputStream gzipInputStream = null;

		  gzipInputStream = new GZIPInputStream(new FileInputStream(source));

		  String outFilename = source +".txt";
		  OutputStream out = new FileOutputStream(outFilename);

		  byte[] buf = new byte[1024];  

		  int len;
		  while ((len = gzipInputStream.read(buf)) > 0) {
		  out.write(buf, 0, len);
		  }
		  
		  gzipInputStream.close();
		  out.close();
	}
}
