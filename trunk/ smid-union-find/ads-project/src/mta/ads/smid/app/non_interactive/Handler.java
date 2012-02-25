package mta.ads.smid.app.non_interactive;

import java.util.List;

import mta.ads.smid.app.MultiIUFkForests;
import mta.ads.smid.app.util.OutputWriter;
import mta.ads.smid.app.util.UnionPair;
import mta.ads.smid.model.IUFkForestException;

class Handler extends MultiIUFkForests{
	final private OutputWriter writer;
	Handler(OutputWriter writer, int n, int maxK){
		super(n, 2, maxK);
		this.writer=writer;
	}
	
	private String trims(List<Integer> list){
		String str = list.toString();
		return str.substring(1,str.length()-1).replace(", ", ",");
	}
	private void write(UnionPair pair, List<Integer> heights){
		StringBuilder builder = new StringBuilder();
		builder.append(pair).append(",").append(trims(heights));
		writer.writeLine(builder.toString());
	}
	
	void union(UnionPair pair){
		try {
			baseUnion(pair.x, pair.y);
			List<Integer> heights = getMaxHeightList();
			write(pair, heights);
		} catch (IUFkForestException e) {
			System.err.println("unable perform union("+pair+"). "+e.getMessage());
		}
	}
	
	void printIUFkStatistics(){
		printStatistics();
	}
	
	
}