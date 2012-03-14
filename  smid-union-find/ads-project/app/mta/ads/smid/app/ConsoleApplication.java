package mta.ads.smid.app;

import java.io.File;

import mta.ads.smid.app.interactive.InteractiveApp;
import mta.ads.smid.app.non_interactive.NonInteractiveApp;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Console Application that can be executed in two modes:
 * <ul>
 * <li><b>interactive</b> mode - console application in which union/find operations can be executed using a CLI interface</li>
 * <li><b>non-interactive</b> mode - batch application, that loads a union instructions file an input and generates a union file output with execution statistics</li>
 * </ul>
 * <br>this application is based on the following packages:
 * <ul>
 * <li><b>args4j</b> for parsing command line arguments
 * <li><b>jexl2</b> for parsing command line expressions
 * </ul>
 *  
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class ConsoleApplication {
	/**
	 * arguments for the console application
	 * @author Yossi Naor & Yosi Zilberberg
	 *
	 */
	static class ConsoleApplicationArgs{
		/**
		 * the maximal k-tree size. operations will be executed for k=2,...,maxK
		 * for non interactive mode
		 * <br> the default value is 5
		 */
		@Option(name="-maxK", usage="the maximal k-tree size")
		int maxK=5;
		/**
		 * interactive execution mode
		 * <br> the default value is false
		 */
		@Option(name="-i", usage="interactive mode")
		boolean interactive=false;
		@Option(name="-in", usage="union input file")
		/**
		 * union instructions file as an input
		 * for non interactive mode
		 */
		File inputFile;
		@Option(name="-out", usage="union result output file")
		/**
		 * output file
		 * for non interactive mode
		 * <br>the default is set into inputFile.csv
		 */
		File outputFile;
	}
	
	/**
	 * create application according to the execution arguments. there are two options:
	 * <ul>
	 * <li><b>interactive</b> application mode
	 * <li><b>non-interactive</b> application mode
	 * </ul>
	 * @param args execution arguments
	 * @return
	 */
	private static Application createApplication(ConsoleApplicationArgs args){
		if (args.interactive){
			return new InteractiveApp();
		} else {
			return new NonInteractiveApp(args.maxK, args.inputFile, args.outputFile);
		}
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ConsoleApplicationArgs appArgs = new ConsoleApplicationArgs(); 
		CmdLineParser parser = new CmdLineParser(appArgs);
		try {
			parser.parseArgument(args);
			Application application = createApplication(appArgs);
			application.run();
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			parser.printUsage(System.out);
		}
	}

}
