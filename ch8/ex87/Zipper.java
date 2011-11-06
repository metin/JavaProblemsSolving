package ex87;

import java.io.*;

public class Zipper {

	protected String source;
	protected String destination;
	protected Compressor compressionMethod;

	public Zipper() {

	}

	public Zipper(String src, String dest, String format) {
		this.source = src;
		this.destination = dest;
		compressionMethod = new CompressorFactory(this).makeCompressor(format);
	}

	public void compress() throws IOException {
		compressionMethod.compress(this.source, this.destination);

	}

}
