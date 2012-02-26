package tests;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class Test1 extends TestCase{

	
	static class Handler{
		public void find(String x){
			System.out.println("in find(x)");
		}
		public void union(String x, String y){
			System.out.println("in union(x,y)");
		}
		public void test(){
			System.out.println("in test()");
		}
		public void test(String ...x){
			System.out.println("in test(...x)");
		}
		public void test(String x, String y){
			System.out.println("in test(x,y)");
		}
	}
	
	static class Command{
		String method;
		String arguments[];
	}
	
	
	Pattern pattern = Pattern.compile("([^\\(]+)\\((.*)\\)");
	Command parse(String cmd) throws ParseException{
		Command command = null;
		Matcher matcher = pattern.matcher(cmd);
		if(matcher.matches()){
			command = new Command();
			command.method = matcher.group(1).trim();
			List<String> argumentList = new LinkedList<String>();
			String[] arguments = matcher.group(2).split(",");
			for (int i=0;i<arguments.length;i++){
				String argument = arguments[i].trim();
				if (argument.equals("")==false)
					argumentList.add(argument);
			}
			command.arguments = argumentList.toArray(new String[0]);
			return command;
		}
		throw new ParseException(cmd,0);
	}
	
	void handle(Object handler, Command command) throws Exception{
		try {
			Class[] argumentClasses = new Class[command.arguments.length];
			for (int i=0;i<argumentClasses.length;i++){
				argumentClasses[i]=String.class;
			}
			Method method = handler.getClass().getMethod(command.method, argumentClasses);
			method.invoke(handler, command.arguments);
		} catch (Exception e) {
			Method method = handler.getClass().getMethod(command.method, String[].class);
			method.invoke(handler, new Object[]{command.arguments});
		}
	}
	
	void genericTest(String cmd) throws Exception{
		Command command = parse(cmd);
		handle(new Handler(), command);
	}
	public void testParse1() throws Exception{
		genericTest("test()");
		genericTest("test(1)");
		genericTest("test(1,2)");
		genericTest("test(1,2,3)");
	}
}
