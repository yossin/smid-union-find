package mta.ads.smid.model;

import mta.ads.smid.Constants;

/**
 * Save Union-Find Statistics
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class Statistics{
	/**
	 * union general counter
	 */
	private int unionCouner=0;
	/**
	 * union case 1 counter
	 */
	private int unionCase1Counter=0;
	/**
	 * union case 2 counter
	 */
	private int unionCase2Counter=0;
	/**
	 * union case  counter
	 */
	private int unionCase3Counter=0;
	
	/**
	 * find general counter
	 */
	private int findCouner=0;
	/**
	 * direct find counter, using root cache - <b>O(1)</b>
	 */
	private int findDirectlyCounter=0;
	/**
	 * recursive find counter - <b>O(log/k(n))</b>
	 */
	private int findRecursivleyCounter=0;
	/**
	 * maximal tree height
	 */
	private int maxHeight=1;
	/**
	 * <i>Root</i> element counter
	 */
	private int rootCounter;
	/**
	 * <i>NonRoot</i> element counter
	 */
	private int nonRootCounter;
	/**
	 * element number
	 */
	private int n;
	/**
	 * k-tree size
	 */
	private int k;
	
	
	/**
	 * @param rootCounter initial <i>Root</i> number
	 * @param nonRootCounter initial <i>NonRoot</i> number
	 * @param n elements number
	 * @param k k-tree size
	 */
	Statistics(int rootCounter, int nonRootCounter, int n, int k){
		this.rootCounter=rootCounter;
		this.nonRootCounter=nonRootCounter;
		this.n=n;
		this.k=k;
	}
	/**
	 * Increase Recursive Find
	 */
	void increaseRecursiveFind(){
		findCouner++;
		findRecursivleyCounter++;
	}
	/**
	 * Increase Direct Find
	 */
	void increaseDirectFind(){
		findCouner++;
		findDirectlyCounter++;
	}
	/**
	 * Increase Union Case 1
	 */
	void increaseUnionCase1(){
		unionCase1Counter++;
		unionCouner++;
		rootCounter--;
	}
	/**
	 * Increase Union Case 2
	 */
	void increaseUnionCase2(int height){
		unionCase2Counter++;
		unionCouner++;
		nonRootCounter+=2;
		rootCounter--;
		if (height>maxHeight)
			maxHeight=height;
	}
	/**
	 * Increase Union Case 3
	 */
	void increaseUnionCase3(){
		unionCase3Counter++;
		unionCouner++;
		rootCounter--;
	}
	/**
	 * @return maximal height
	 */
	public int getMaxHeight() {
		return maxHeight;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		int total = nonRootCounter+rootCounter;
		StringBuilder builder = new StringBuilder();
		builder.append("IUF-").append(k).append(Constants.NEW_LINE)
			.append(" #Leafs: ").append(n).append(Constants.NEW_LINE)
			.append(" #Roots: ").append(rootCounter).append(Constants.NEW_LINE)
			.append(" #NonRoots: ").append(nonRootCounter).append(" (including leafs)").append(Constants.NEW_LINE)
			.append(" #Total: ").append(total).append(" (Roots + NonRoots)").append(Constants.NEW_LINE)
			.append(" #Find: ").append(findCouner)
			.append(" (direct:").append(findDirectlyCounter)
			.append(", recursive:").append(findRecursivleyCounter).append(")").append(Constants.NEW_LINE)
			.append(" #Union: ").append(unionCouner)
			.append(" (case1:").append(unionCase1Counter)
			.append(", case2:").append(unionCase2Counter)
			.append(", case3:").append(unionCase3Counter).append(")").append(Constants.NEW_LINE)
			.append(" #Max Height: ").append(maxHeight);
		int expectedTotal=2*n -unionCase1Counter +unionCase2Counter -unionCase3Counter;
		if (total != expectedTotal){
			builder.append("**** total nodes are not equal to the expected value ("+expectedTotal+") ****").append(Constants.NEW_LINE);
		}
		return builder.toString();
	}

}