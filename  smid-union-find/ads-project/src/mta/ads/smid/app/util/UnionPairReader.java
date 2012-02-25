package mta.ads.smid.app.util;

import java.io.File;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UnionPairReader implements Iterable<UnionPair>{
	final private FileReader reader;
	final static private Pattern pairPattern = Pattern.compile("\\s*(\\d+)\\s*,\\s*(\\d+)\\s*");
	final static private Pattern numberPattern = Pattern.compile("\\s*(\\d+)\\s*");

	public UnionPairReader(File file){
		reader = new FileReader(file);
	}
	
	public class UnionPairIterator implements Iterator<UnionPair>{
		private UnionPair pair;
		UnionPairIterator(){
			readNext();
		}
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
		@Override
		public boolean hasNext() {
			return (pair != null);
		}

		@Override
		public UnionPair next() {
			UnionPair ret = pair;
			readNext();
			return ret;
		}

		@Override
		public void remove() {
			throw new RuntimeException("unsuported");
		}
		
	}
	
	@Override
	public Iterator<UnionPair> iterator() {
		return new UnionPairIterator();
	}
	
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