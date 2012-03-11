package mta.ads.smid.app.interactive;

import mta.ads.smid.app.Application;

/**
 * interactive application implementation
 * 
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class InteractiveApp implements Application{

	/**
	 * ui
	 * @uml.property  name="ui"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final UI ui;
	/**
	 * syntax engine (based in jexl2)
	 * @uml.property  name="syntaxEngine"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final SyntaxEngine syntaxEngine;
	/**
	 * initialize ui and syntax engine
	 */
	public InteractiveApp(){
		ui = new UI();
		syntaxEngine=new SyntaxEngine(ui);
	}

	/* (non-Javadoc)
	 * @see mta.ads.smid.app.Application#run()
	 */
	public void run() throws Exception {
		String line = null;
		while ((line=ui.readCommand())!=null){
			syntaxEngine.evaluate(line);
		}
	}
}