package tests;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import mta.ads.smid.app.util.UnionSequenceGenerator;

public class UnionRandomGeneratorTestCase extends TestCase{
	
	/**
	 * @uml.property  name="dir"
	 */
	String dir="D:/Users/Yos/Workspaces/mta/ads-project/tests/";
	
	
	private void generate(int n, String suffix){
		File file = new File(dir, "random-union-"+suffix+".txt");
		UnionSequenceGenerator.generateRandomFile(n, file);
	}
//	private int p(int x, int y){
//		return (int)Math.pow(x,y);
//	}

	public void testGenerate_RandomUnion100() throws IOException{
		generate(100,"100");
	}
	public void testGenerate_RandomUnion10K() throws IOException{
		generate(10000,"10K");
	}
}
