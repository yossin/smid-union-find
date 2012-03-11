package mta.ads.smid.app.interactive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * UI
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
class UI {
	/**
	 * buffered reader that points into System.in
	 * @uml.property  name="reader"
	 */
	final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	/**
	 * read line from buffer
	 * @return read line
	 * @throws IOException
	 */
	String readCommand() throws IOException{
		return reader.readLine();
	}
	/**
	 * print a message
	 * @param message
	 */
	void message(String message){
		System.out.println(message);
		System.out.flush();
	}
	/**
	 * print an error message
	 * @param message
	 */
	void error(String message){
		System.err.println(message);
		System.err.flush();
	}
	/**
	 * print an exception
	 * @param e
	 */
	void error(Exception e){
		System.err.println(e.getMessage());
		System.err.flush();
	}
}