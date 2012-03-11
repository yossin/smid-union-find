package mta.ads.smid.app.non_interactive;

import java.io.File;

import mta.ads.smid.app.Application;
import mta.ads.smid.app.util.OutputWriter;
import mta.ads.smid.app.util.UnionPair;
import mta.ads.smid.app.util.UnionPairReader;


/**
 * Non Interactove application
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class NonInteractiveApp implements Application{
	/**
	 * maximal k-tree size
	 * @uml.property  name="maxK"
	 */
	private final int maxK;
	/**
	 * input file
	 * @uml.property  name="in"
	 */
	private final File in;
	/**
	 * output file
	 * @uml.property  name="out"
	 */
	private final File out;
	public NonInteractiveApp(int maxK, File in, File out){
		this.maxK=maxK;
		this.in=in;
		if (out==null){
			out=new File(in.getParent(), in.getName()+".csv");
		}
		this.out=out;
	}
	/**
	 * generate output file header
	 * @param n number of elements
	 * @return output header string
	 */
	private String generateHeader(int n){
		StringBuilder builder = new StringBuilder();
		builder.append(n).append("(x),")
		.append(n).append("(y)");
		for (int i=2; i<=maxK;i++){
			builder.append(",IUF-").append(i);
		}
		return builder.toString();
	}
	/* (non-Javadoc)
	 * @see mta.ads.smid.app.Application#run()
	 */
	public void run(){
		OutputWriter writer = new OutputWriter(out);
		try {
			// 1. resolve n
			UnionPairReader pairReader = new UnionPairReader(in);
			int n = pairReader.readElementNumber();
			// 2. write header 
			writer.writeLine(generateHeader(n));

			// 2. for each x,y write: 
			//    x,y,IUF-2.maxHeight,..,IUF-k.maxHeight
			Handler handler = new Handler(writer, n, maxK);
			for (UnionPair pair: pairReader) {
				handler.union(pair);
			}
			
			// 3. print statistics
			handler.printIUFkStatistics();
		}finally {
			writer.close();
		}
	}
}
