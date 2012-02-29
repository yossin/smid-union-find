package mta.ads.smid.app.util;

/**
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class UnionPair implements Comparable<UnionPair>{
	/**
	 * 
	 */
	public final int x;
	/**
	 * 
	 */
	public final int y;
	/**
	 * @param x
	 * @param y
	 */
	public UnionPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append(x).append(",").append(y);
		return builder.toString();
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(UnionPair unionPair) {
		return createOrdered().toString().compareTo(unionPair.createOrdered().toString());
	}
	
	
	
	/**
	 * @return
	 */
	private UnionPair createOrdered(){
		return new UnionPair(x<y?x:y, y>x?y:x);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return createOrdered().toString().hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		try {
			UnionPair unionPair=(UnionPair)obj;
			return this.compareTo(unionPair)==0;
		} catch (ClassCastException e){
			return super.equals(obj);
		}
	}
}