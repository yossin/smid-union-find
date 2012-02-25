package mta.ads.smid.app.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import mta.ads.smid.app.Constants;

public class OutputWriter {
	final private BufferedWriter writer;
	public OutputWriter(File file){
		try {
			this.writer=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public void writeLine(String line){
		try {
			writer.write(line+Constants.NEW_LINE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close(){
		try {
			writer.close();
		} catch (IOException e) {
		}
	}

}