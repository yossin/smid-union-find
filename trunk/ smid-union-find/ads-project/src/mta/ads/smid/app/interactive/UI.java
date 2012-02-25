package mta.ads.smid.app.interactive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class UI {
	final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	String readCommand() throws IOException{
		return reader.readLine();
	}
	void message(String message){
		System.out.println(message);
		System.out.flush();
	}
	void error(String message){
		System.err.println(message);
		System.err.flush();
	}
	void error(Exception e){
		System.err.println(e.getMessage());
		System.err.flush();
	}
}