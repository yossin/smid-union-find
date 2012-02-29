package mta.ads.smid.app.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import mta.ads.smid.Constants;

/**
 * File Writer Wrapper
 * <br>the writer is used for writing 'union-operation' ant their result into an output file.
 * 
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class OutputWriter {
	/**
	 * buffered writer for writing lines into a file
	 */
	final private BufferedWriter writer;
	/**
	 * @param file output file
	 */
	public OutputWriter(File file){
		try {
			this.writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * write a line into the output file
	 * @param line output line
	 */
	public void writeLine(String line){
		try {
			writer.write(line+Constants.NEW_LINE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * close the file
	 */
	public void close(){
		try {
			writer.close();
		} catch (IOException e) {
		}
	}

}