package mta.ads.smid.model;

import mta.ads.smid.Constants;

public class Statistics{
	private int unionCouner=0;
	private int unionCase1Counter=0;
	private int unionCase2Counter=0;
	private int unionCase3Counter=0;
	
	private int findCouner=0;
	private int findDirectlyCounter=0;
	private int findRecursivleyCounter=0;
	private int maxHeight=1;
	private int rootCounter;
	private int nonRootCounter;
	private int n;
	private int k;
	Statistics(int rootCounter, int nonRootCounter, int n, int k){
		this.rootCounter=rootCounter;
		this.nonRootCounter=nonRootCounter;
		this.n=n;
		this.k=k;
	}
	void increaseRecursiveFind(){
		findCouner++;
		findRecursivleyCounter++;
	}
	void increaseDirectFind(){
		findCouner++;
		findDirectlyCounter++;
	}
	void performUnionCase1(){
		unionCase1Counter++;
		unionCouner++;
		rootCounter--;
	}
	void performUnionCase2(int height){
		unionCase2Counter++;
		unionCouner++;
		nonRootCounter+=2;
		rootCounter--;
		if (height>maxHeight)
			maxHeight=height;
	}
	void performUnionCase3(){
		unionCase3Counter++;
		unionCouner++;
		rootCounter--;
	}
	public int getMaxHeight() {
		return maxHeight;
	}
	
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
	
	//TODO: split find cases: O(1), O(log/k(n))

}