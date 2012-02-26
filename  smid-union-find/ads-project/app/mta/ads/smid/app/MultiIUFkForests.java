package mta.ads.smid.app;

import java.util.LinkedList;
import java.util.List;

import mta.ads.smid.model.IUFkForest;
import mta.ads.smid.model.IUFkForestException;

public abstract class MultiIUFkForests{
	
	final List<IUFkForest> forests = new LinkedList<IUFkForest>();
	private int minK=2;
	private int maxK=10;
	public MultiIUFkForests(int n, int minK, int maxK){
		baseInit(n, minK, maxK);
	}
	protected void baseInit(int n, int minK, int maxK){
		this.minK=minK;
		this.maxK=maxK;
		forests.clear();
		for (int i=minK;i<=maxK;i++){
			forests.add(new IUFkForest(i, n));
		}
	}

	protected void baseUnion (int x, int y) throws IUFkForestException{
		for (IUFkForest forest : forests){
			forest.union(x,y);
		}
	}

	protected List<Integer> baseFind (int x) throws IUFkForestException{
		List<Integer> result = new LinkedList<Integer>();
		for (IUFkForest forest : forests){
			int root = forest.find(x);
			result.add(root);
		}
		return result;
	}
	
	protected List<Integer> getMaxHeightList (){
		List<Integer> result = new LinkedList<Integer>();
		for (IUFkForest forest : forests){
			result.add(forest.getStatistics().getMaxHeight());
		}
		return result;
	}
	protected int getMaxK() {
		return maxK;
	}
	protected int getMinK() {
		return minK;
	}
	
	protected void printStatistics(){
		for (IUFkForest forest : forests){
			System.out.println(forest.getStatistics());
		}
	}

}