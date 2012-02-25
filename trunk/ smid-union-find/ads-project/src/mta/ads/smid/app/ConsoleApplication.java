package mta.ads.smid.app;

import java.io.File;

import mta.ads.smid.app.interactive.InteractiveApp;
import mta.ads.smid.app.non_interactive.NonInteractiveApp;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class ConsoleApplication {
	static class ConsoleApplicationArgs{
		@Option(name="-maxK", usage="max IUF-k")
		int maxK=5;
		@Option(name="-i", usage="interactive mode")
		boolean interactive=false;
		@Option(name="-in", usage="union input file")
		File inputFile;
		@Option(name="-out", usage="union result output file")
		File outputFile;
	}
	
	private static Application createApplication(ConsoleApplicationArgs args){
		if (args.interactive){
			return new InteractiveApp();
		} else {
			return new NonInteractiveApp(args.maxK, args.inputFile, args.outputFile);
		}
	}
	
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
