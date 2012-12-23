package ex87;

import java.io.IOException;

public class UnZipper {
	static final int BUFFER = 2048;

	protected String source;
	protected String destination;
	public DeCompressor decompressor;

	public UnZipper() {

	}

	public UnZipper(String src, String dest, String format) {
		this.source = src;
		this.destination = dest;
		decompressor = new DeCompressorFactory().makeDeCompressor(format);
	}

	public UnZipper(String src) {
		this.source = src;
		this.destination = "";

	}

	public void decompress() throws IOException {
		decompressor.decompress(source, destination);
	}
}
