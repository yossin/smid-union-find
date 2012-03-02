package mta.ads.smid.app.interactive;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.JexlException;
import org.apache.commons.jexl2.MapContext;

/**
 * Syntax engine for parsing CLI expressions.
 * <br>every expression will be interpreted into java code evaluation invoking handler methods
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
/**
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
class SyntaxEngine{
	/**
	 * holds the key context for the handler (at the expression map of jexl2)
	 */
	final static String HANDLER_PREFIX="iuf";
	/**
	 * engine
	 */
	final JexlEngine engine;
	/**
	 * context map
	 */
	final JexlContext context;
	/**
	 * handler
	 */
	final Handler handler;
	/**
	 * initializes: handler, engine. set the handler into the jexl2 context map
	 * @param ui
	 */
	SyntaxEngine(UI ui){
		handler = new Handler(ui);
		engine = new JexlEngine();
		engine.setSilent(true);
		context = new MapContext();
		context.set(HANDLER_PREFIX, handler );
	}
	
	/**
	 * evaluate command
	 * @param command
	 */
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