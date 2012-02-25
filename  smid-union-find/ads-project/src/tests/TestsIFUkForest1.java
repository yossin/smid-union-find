package tests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import mta.ads.smid.model.IUFkForest;
import mta.ads.smid.model.IUFkForestException;

public class TestsIFUkForest1 extends TestCase{
	int k=2;
	int n=4;
	int maxK=4;
	int unions[][]={{0,3},{0,1},{2,4}};

	@Override
	protected void setUp() throws Exception {
		if (k<2||maxK<2) throw new Exception("k must be grater than 1");
	}
	
	private void printGroups(IUFkForest forest){
		Map<Integer,Set<Integer>> groupMap = new HashMap<Integer, Set<Integer>>();
		for(int i=0;i<=n;i++){
			try {
				int root = forest.find(i);
				if (groupMap.containsKey(root)==false){
					Set<Integer> set = new HashSet<Integer>();
					groupMap.put(root, set);
				}
				groupMap.get(root).add(i); 
			} catch (IUFkForestException e) {
				e.printStackTrace();
			}
		}
		System.out.println(" Group Map for "+forest.getK()+"-Forest: "+groupMap);
	}
	
	private void unions(IUFkForest forests[] , int r, int s){
			int maxHeights[]=new int[forests.length];
			for (int i=0; i<forests.length; i++){
				try {
					forests[i].union(r, s);
				} catch (IUFkForestException e) {
					e.printStackTrace();
				}
				maxHeights[i]=forests[i].getStatistics().getMaxHeight();
			}
			System.out.print(r+","+s);
			for (int i=0;i<maxHeights.length;i++){
				System.out.print(","+maxHeights[i]);
			}
			System.out.println("");
			for (int i=0; i<forests.length; i++){
				printGroups(forests[i]);
			}
	}

	private void siquenceTest(IUFkForest forest, int[][] unions){
		IUFkForest forests[]={forest}; 
		multiSiquenceTest(forests, unions);
	}


	private void multiSiquenceTest(IUFkForest forests[], int[][] unions){
		for (int i=0;i<unions.length; i++){
			unions(forests, unions[i][0], unions[i][1]);
		}
	}
	
	private IUFkForest createForest(int k){
		return new IUFkForest(k, n+1);
	}

	//2...maxK
	private IUFkForest[] createForests(int maxK){
		IUFkForest forests[]=new IUFkForest[maxK-1];
		for (int i=0; i<maxK-1; i++){
			forests[i]=createForest(i+2);
		}
		return forests;
	}

	public void test_SingleTest() throws Exception{
		IUFkForest forest = createForest(k);
		siquenceTest(forest, unions);
	}
	
	public void xtest_MultiTest() throws Exception{
		IUFkForest forests[]= createForests(maxK);
		multiSiquenceTest(forests, unions);
	}
	
}
