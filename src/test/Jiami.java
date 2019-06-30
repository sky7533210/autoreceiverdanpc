package test;

public class Jiami {
	private static byte[] my= new byte[] {0x11,0x1c,0x3a,0x48,0x56,0x11,0x26,0x58,0x2a,0x45};
	public static byte[] jiami(byte[] yw){		
		for(int i=0;i<yw.length;++i){
			yw[i]=(byte) (yw[i]^my[i%my.length]);
		}		
		return yw;
	}
}
