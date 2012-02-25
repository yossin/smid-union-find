package mta.ads.smid.app.util;

public class UnionPair{
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
}