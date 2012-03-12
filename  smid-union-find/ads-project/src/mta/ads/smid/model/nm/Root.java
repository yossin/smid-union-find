package mta.ads.smid.model.nm;

/**
 * <i>Root</i> element for managing a k-tree root data structure. <br>a <i>Root</i> element implements <i>Node</i> interface.
 * @author  Yossi Naor & Yosi Zilberberg
 */
public class Root implements Node{
	/**
	 * once set it cannot be changed
	 * @uml.property  name="id"
	 */
	public final int id;
	/**
	 * once set it cannot be changed
	 */
	public final int height;
	/**
	 * once set it cannot be changed
	 */
	public final int name;
	/**
	 * an array of sons. the size of the array cannot be changed. 
	 * <br> the sons can be manipulated only by Root / NodeManaged
	 */
	final private int sonsId[];
	/**
	 * the number of sons in sonsId array. initialized with 0
	 * @uml.property  name="numberOfSons"
	 */
	private int numberOfSons=0;

	/**
	 * sonsId array is initialized with k elements (the default value is 0)
	 * @param id the root id
	 * @param name the name of the root
	 * @param height the height of the root
	 * @param k the k-size of the tree
	 */
	Root(int id, int name, int height, int k){
		this.id=id;
		this.name=name;
		this.height=height;
		this.sonsId=new int[k];
	}
	/**
	 * @param sonId son id to be added into sonId array
	 */
	public void addSon(int sonId){
		sonsId[numberOfSons++]=sonId;
	}
	/**
	 * Get a son by index (from sonsId array) 
	 * @param index in the sonsId array for the requested son 
	 * @return sonId that matches the index in sonsId array
	 */
	public int getSonId(int index) {
		return sonsId[index];
	}
	/**
	 * @return  the number of sons stored in the array
	 * @uml.property  name="numberOfSons"
	 */
	public int getNumberOfSons() {
		return numberOfSons;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("[Root: id=").append(id)
			.append(", name=").append(name)
			.append(", height=").append(height)
			.append(", numberOfSons=").append(numberOfSons)
			.append("]");
		return out.toString();
	}
}