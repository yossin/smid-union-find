package mta.ads.smid.app.interactive;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.JexlException;
import org.apache.commons.jexl2.MapContext;

class SyntaxEngine{
	final static String HANDLER_PREFIX="iuf";
	final JexlEngine engine;
	final JexlContext context;
	final Handler handler;
	SyntaxEngine(UI ui){
		handler = new Handler(ui);
		engine = new JexlEngine();
		engine.setSilent(true);
		context = new MapContext();
		context.set(HANDLER_PREFIX, handler );
	}
	
	void evaluate(String command){
		try{
			Expression expression = engine.createExpression(SyntaxEngine.HANDLER_PREFIX+"."+command);
			Object result = expression.evaluate(context);
			if (handler.equals(result) == false){
				System.err.println("unable to process your request!");
			}
		} catch (JexlException e){
			System.err.println("invalid command!");
		}
		
	}
	
}