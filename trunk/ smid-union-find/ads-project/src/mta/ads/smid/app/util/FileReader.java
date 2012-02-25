package mta.ads.smid.app.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

class FileReader{
	final BufferedReader reader;
	FileReader(File file){
		try {
			this.reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	String readLine(){
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	void close(){
		try {
			reader.close();
		} catch (IOException e) {
		}
	}
}