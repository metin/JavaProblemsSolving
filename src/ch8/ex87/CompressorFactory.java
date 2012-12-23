package ex87;

public class CompressorFactory {
	protected Zipper zipper;
	public CompressorFactory(Zipper zipper){
		this.zipper = zipper;
	}
	
	public Compressor makeCompressor(String type){
		
		if(type.equals("zip")){
			System.out.println("Using ZIP compression..");
			return new ZipCompressor();
		}
		else if(type.equals("gzip")){
			System.out.println("Using GZIP compression..");
			return new GZipCompressor();
		}
		return new ZipCompressor();
	}
}
