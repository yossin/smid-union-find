package mta.ads.smid.app.interactive;

import mta.ads.smid.app.Application;

public class InteractiveApp implements Application{

	final UI ui;
	final SyntaxEngine syntaxEngine;
	public InteractiveApp(){
		ui = new UI();
		syntaxEngine=new SyntaxEngine(ui);
	}

	public void run() throws Exception {
		String line = null;
		while ((line=ui.readCommand())!=null){
			syntaxEngine.evaluate(line);
		}
	}
}