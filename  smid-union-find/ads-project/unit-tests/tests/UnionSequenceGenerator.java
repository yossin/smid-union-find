package tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Queue;

import junit.framework.TestCase;
import mta.ads.smid.Constants;

public class UnionSequenceGenerator extends TestCase{
	
	/**
	 * @uml.property  name="dir"
	 */
	String dir="D:/Users/Yos/Workspaces/mta/ads-project/tests/";
	
	private void uGroup(int k, int n, BufferedWriter writer) throws IOException{
		k=k-1;
		Queue<Integer> q1 = new LinkedList<Integer>();
		for (int i=0;i<n;i++){
			q1.add(i);
		}
		
		do {
			Queue<Integer> q2 = new LinkedList<Integer>();
			
			while (q1.isEmpty() == false){
				int x=q1.poll();
				q2.add(x);
				int size=(k<q1.size()?k:q1.size());
				for (int i=0;i<size;i++){
					int y=q1.poll();
					writer.write(x+","+y+Constants.NEW_LINE);
				}
			}
			q1=q2;
		} while(q1.size()>1);

	}
	private void generate(int n, int k, String suffix) throws IOException{
		File file = new File(dir, k+"-balanced-"+suffix+".txt");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		try {
			writer.write(n+Constants.NEW_LINE);
			uGroup(k,n,writer);
		} finally {
			writer.close();
		}
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
