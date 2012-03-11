package mta.ads.smid.model.nm;

/**
 * <i>NonRoot</i> element for managing k-tree non root data structure.  <br>a <i>NonRoot</i> element implement <i>Node</i> interface and contains a parent. <br>the parent can be replaced only by NodeManager
 * @author  Yossi Naor & Yosi Zilberberg
 */
public class NonRoot implements Node {
	/**
	 * parent id
	 * @uml.property  name="parentId"
	 */
	int parentId;
	/**
	 * @param parentId parent id
	 */
	NonRoot(int parentId) {
		this.parentId = parentId;
	}
	/**
	 * @return  parent id
	 * @uml.property  name="parentId"
	 */
	public int getParentId() {
		return parentId;
	}
}