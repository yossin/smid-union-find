package mta.ads.smid.app.util;

public class UnionPair implements Comparable<UnionPair>{
	public final int x;
	public final int y;
	public UnionPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append(x).append(",").append(y);
		return builder.toString();
	}
	@Override
	public int compareTo(UnionPair unionPair) {
		return createOrdered().toString().compareTo(unionPair.createOrdered().toString());
	}
	
	
	
	private UnionPair createOrdered(){
		return new UnionPair(x<y?x:y, y>x?y:x);
	}
	
	@Override
	public int hashCode() {
		return createOrdered().toString().hashCode();
	}
	
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