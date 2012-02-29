package mta.ads.smid.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * File Reader Wrapper
 * <br>the reader is used for parsing 'union-operation' files
 * <br>this is a private class for that package.
 * 
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
class FileReader{
	/**
	 * buffered reader for reading lines
	 */
	final BufferedReader reader;
	/**
	 * @param file input file
	 */
	FileReader(File file){
		try {
			this.reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * @return line, or null when reaching EOL
	 */
	String readLine(){
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * close the file
	 */
	void close(){
		try {
			reader.close();
		} catch (IOException e) {
		}
	}
}