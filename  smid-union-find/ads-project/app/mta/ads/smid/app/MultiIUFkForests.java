package mta.ads.smid.app;

import java.util.LinkedList;
import java.util.List;

import mta.ads.smid.model.IUFkForest;
import mta.ads.smid.model.IUFkForestException;

/**
 * manage several forests
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public abstract class MultiIUFkForests{
	
	/**
	 * forest list
	 * @uml.property  name="forests"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="mta.ads.smid.model.IUFkForest"
	 */
	final List<IUFkForest> forests = new LinkedList<IUFkForest>();
	/**
	 * minimal k is set into 2
	 * @uml.property  name="minK"
	 */
	private int minK=2;
	
	/**
	 * maximal k is set into 10
	 * @uml.property  name="maxK"
	 */
	private int maxK=10;
	/**
	 * @param n number of elements
	 * @param minK minimum k-tree size
	 * @param maxK maximum k-tree size
	 */
	public MultiIUFkForests(int n, int minK, int maxK){
		baseInit(n, minK, maxK);
	}
	/**
	 * private init method
	 * @param n number of elements
	 * @param minK minimum k-tree size
	 * @param maxK maximum k-tree size
	 */
	protected void baseInit(int n, int minK, int maxK){
		this.minK=minK;
		this.maxK=maxK;
		forests.clear();
		for (int i=minK;i<=maxK;i++){
			forests.add(new IUFkForest(i, n));
		}
	}

	/**
	 * perform union in all forests
	 * @param x first leaf
	 * @param y second leaf
	 * @throws IUFkForestException
	 */
	protected void baseUnion (int x, int y) throws IUFkForestException{
		for (IUFkForest forest : forests){
			forest.union(x,y);
		}
	}

	/**
	 * perform find
	 * @param x
	 * @return the <i>Root</i> element
	 * @throws IUFkForestException
	 */
	protected List<Integer> baseFind (int x) throws IUFkForestException{
		List<Integer> result = new LinkedList<Integer>();
		for (IUFkForest forest : forests){
			int root = forest.find(x);
			result.add(root);
		}
		return result;
	}
	
	/**
	 * @return the maximal height exist in every forest minK,...,maxK
	 */
	protected List<Integer> getMaxHeightList (){
		List<Integer> result = new LinkedList<Integer>();
		for (IUFkForest forest : forests){
			result.add(forest.getStatistics().getMaxHeight());
		}
		return result;
	}
	/**
	 * @return  maxK
	 * @uml.property  name="maxK"
	 */
	protected int getMaxK() {
		return maxK;
	}
	/**
	 * @return  minK
	 * @uml.property  name="minK"
	 */
	protected int getMinK() {
		return minK;
	}
	
	/**
	 * print statistics for every forest
	 */
	protected void printStatistics(){
		for (IUFkForest forest : forests){
			System.out.println(forest.getStatistics());
		}
	}

}