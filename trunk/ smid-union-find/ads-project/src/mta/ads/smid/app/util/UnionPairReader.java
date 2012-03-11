package mta.ads.smid.app.util;

import java.io.File;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Read Union Pair File implementing a UnionPair Iterator 
 * <br> the reader ignores unparsable lines, though the first line must be a number.
 * <br> first line: the number of elements. for example: 3 is for {0,1,2} elements
 * <br> second+ lines: union operations. for example: 0,1 
 * <br><u>input file example</u>
 * <br>4
 * <br>0,1
 * <br>2,3
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class UnionPairReader implements Iterable<UnionPair>{
	/**
	 * file reader
	 * @uml.property  name="reader"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final private FileReader reader;
	/**
	 * pair pattern - \\s*(\\d+)\\s*,\\s*(\\d+)\\s*
	 */
	final static private Pattern pairPattern = Pattern.compile("\\s*(\\d+)\\s*,\\s*(\\d+)\\s*");
	/**
	 * number pattern - \\s*(\\d+)\\s*
	 */
	final static private Pattern numberPattern = Pattern.compile("\\s*(\\d+)\\s*");

	/**
	 * @param file input file
	 */
	public UnionPairReader(File file){
		reader = new FileReader(file);
	}
	
	/**
	 * non static Iterator class, to be used by union pair reader only
	 * @author  Yossi Naor & Yosi Zilberberg
	 */
	public class UnionPairIterator implements Iterator<UnionPair>{
		/**
		 * last parsed pair
		 * @uml.property  name="pair"
		 * @uml.associationEnd  
		 */
		private UnionPair pair;
		/**
		 * private constructor, for union pair reader
		 */
		UnionPairIterator(){
			readNext();
		}
		/**
		 * Parse a line. If line matches pair pattern, return a pair, else return null. 
		 * @param line line to be parsed
		 * @return
		 */
		private UnionPair parseLine(String line){
			if (line != null) {
				Matcher matcher = pairPattern.matcher(line);
				if (matcher.matches()){
					int x = Integer.parseInt(matcher.group(1));
					int y = Integer.parseInt(matcher.group(2));
					return new UnionPair(x, y);
				}
			}
			return null;
		}
		/**
		 * read next line until parsing a pair, or until EOF
		 */
		private void readNext(){
			String line;
			pair=null;
			do {
				line=reader.readLine();
				pair=parseLine(line);
			}while(pair==null && line!=null);
			
			if (line==null){
				reader.close();
			}
		}
		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return (pair != null);
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public UnionPair next() {
			UnionPair ret = pair;
			readNext();
			return ret;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new RuntimeException("unsuported");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<UnionPair> iterator() {
		return new UnionPairIterator();
	}
	
	/**
	 * @return element number
	 */
	public Integer readElementNumber(){
		Integer number=null;
		String line = reader.readLine();
		if (line != null) {
			Matcher matcher = numberPattern.matcher(line);
			if (matcher.matches()){
				number = Integer.parseInt(matcher.group(1));
			}
		}
		return number;
	}

	
}