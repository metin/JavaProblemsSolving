package ex87;

import java.io.IOException;

public interface Compressor {
	public void compress(String source, String destination) throws IOException;
}
