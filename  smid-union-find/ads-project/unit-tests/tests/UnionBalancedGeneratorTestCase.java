package tests;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import mta.ads.smid.app.util.UnionSequenceGenerator;

public class UnionBalancedGeneratorTestCase extends TestCase{
	
	/**
	 * @uml.property  name="dir"
	 */
	String dir="D:/Users/Yos/Workspaces/mta/ads-project/tests/";
	
	private void generate(int n, int k, String suffix) throws IOException{
		File file = new File(dir, k+"-balanced-"+suffix+".txt");
		UnionSequenceGenerator.generateBalancedFile(k, n, file);
	}
	private int p(int x, int y){
		return (int)Math.pow(x,y);
	}
	public void testGenerate_2Balanced2P10() throws IOException{
		generate(p(2, 10),2,"2P10");
	}
	public void testGenerate_3Balanced5K() throws IOException{
		generate(5000,3,"5K");
	}
	public void testGenerate_6Balanced50K() throws IOException{
		generate(50000,6,"50K");
	}
	public void testGenerate_2Balanced2P20() throws IOException{
		generate(p(2, 20),2,"2P20");
	}
	public void testGenerate_3Balanced2P20() throws IOException{
		generate(p(2, 20),3,"2P20");
	}
	public void testGenerate_2Balanced10() throws IOException{
		generate(10,2,"10");
	}
	public void testGenerate_2Balanced100() throws IOException{
		generate(100,2,"100");
	}
	public void testGenerate_3Balanced10K() throws IOException{
		generate(10000,3,"10K");
	}
}
