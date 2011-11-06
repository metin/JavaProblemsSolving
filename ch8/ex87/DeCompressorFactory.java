package ex87;

public class DeCompressorFactory {

	public DeCompressorFactory(){

	}
	
	public DeCompressor makeDeCompressor(String type){
		
		if(type.equals("zip")){
			System.out.println("Using ZIP decompression...");
			return new ZipDeCompressor();
		}
		else if(type.equals("gzip")){
			System.out.println("Using GZIP decompression...");
			return new GZipDeCompressor();
		}
		return new ZipDeCompressor();
	}
}
