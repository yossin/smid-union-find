package mta.ads.smid.model.nm;

/**
 * <i>Leaf</i> element for managing k-tree leaf data structure.
 * <br>a leaf element extends <i>NonRoot</i> element.
 * <br>it contains the name of the leaf. once the name is set it cannot be changed.
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class Leaf extends NonRoot{
	/**
	 * the name of the leaf
	 */
	final int name;
	/**
	 * @param name the name of the leaf
	 * @param parentId parent id
	 */
	Leaf(int name, int parentId) {
		super(parentId);
		this.name=name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("[Leaf: name=").append(name)
		.append("]");
		return out.toString();
	}
}