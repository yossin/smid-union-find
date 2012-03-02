package mta.ads.smid.app.non_interactive;

import java.util.List;

import mta.ads.smid.app.MultiIUFkForests;
import mta.ads.smid.app.util.OutputWriter;
import mta.ads.smid.app.util.UnionPair;
import mta.ads.smid.model.IUFkForestException;

/**
 * non interactive handler
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
class Handler extends MultiIUFkForests{
	/**
	 * output writer for writing union result into an output file
	 */
	final private OutputWriter writer;
	/**
	 * @param writer file writer
	 * @param n number of elements
	 * @param maxK max k-tree size
	 */
	Handler(OutputWriter writer, int n, int maxK){
		super(n, 2, maxK);
		this.writer=writer;
	}
	
	/**
	 * trim spaces between elements
	 * @param list
	 * @return
	 */
	private String trims(List<Integer> list){
		String str = list.toString();
		return str.substring(1,str.length()-1).replace(", ", ",");
	}
	/**
	 * write union pair and a height list into the output file
	 * @param pair union pair
	 * @param heights height list
	 */
	private void write(UnionPair pair, List<Integer> heights){
		StringBuilder builder = new StringBuilder();
		builder.append(pair).append(",").append(trims(heights));
		writer.writeLine(builder.toString());
	}
	
	/**
	 * perform a union and write the result into the output file
	 * @param pair union pair
	 */
	void union(UnionPair pair){
		try {
			baseUnion(pair.x, pair.y);
			List<Integer> heights = getMaxHeightList();
			write(pair, heights);
		} catch (IUFkForestException e) {
			System.err.println("unable perform union("+pair+"). "+e.getMessage());
		}
	}
	
	/**
	 * print statistics into console
	 */
	void printIUFkStatistics(){
		printStatistics();
	}
	
	
}