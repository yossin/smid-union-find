package tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

import junit.framework.TestCase;
import mta.ads.smid.Constants;

public class UnionRandomGenerator extends TestCase{
	
	String dir="D:/Users/Yos/Workspaces/mta/ads-project/tests/";
	
	
	private static int randPoll(LinkedList<Integer> q){
		int size = q.size();
		if (size==0){
			throw new ArrayIndexOutOfBoundsException();
		}
		int position = (int)(Math.random()*(double)(size-1));
		return q.remove(position);
	}
	
	private void uGroup(int n, BufferedWriter writer) throws IOException{
		LinkedList<Integer> q1 = new LinkedList<Integer>();
		for (int i=0;i<n;i++){
			q1.add(i);
		}
		
		do {
			
			int x=randPoll(q1);
			int y=randPoll(q1);
			writer.write(x+","+y+Constants.NEW_LINE);

			q1.add(x);
		} while(q1.size()>1);

	}
	private void generate(int n, String suffix) throws IOException{
		File file = new File(dir, "random-union-"+suffix+".txt");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		try {
			writer.write(n+Constants.NEW_LINE);
			uGroup(n,writer);
		} finally {
			writer.close();
		}
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
