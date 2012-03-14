package mta.ads.smid.app.interactive;

import java.util.List;

import mta.ads.smid.app.MultiIUFkForests;
import mta.ads.smid.model.IUFkForestException;

/**
 * command handler for interactive mode
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class Handler extends MultiIUFkForests{
	
	/**
	 * our UI (the console)
	 * @uml.property  name="ui"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final UI ui;
	/**
	 * handler initializes a 2-forest with 10 elements
	 * @param ui
	 */
	public Handler(UI ui){
		super(10, 2, 2);
		this.ui=ui;
		help();
		initK1(10, 2);
	}
	
	/**
	 * general forest initialization
	 * @param n number of elements
	 * @param minK minimum k-tree size
	 * @param maxK maximum k-tree size
	 */
	private void init1(int n, int minK, int maxK){
		baseInit(n, minK, maxK);
		ui.message("init "+n+" elements (0,..,"+(n-1)+")");
		ui.message("init "+((minK==maxK)?" IUF-"+minK:" IUF-"+minK+",...,IUF-"+maxK));
	}

	/**
	 * print messages into UI
	 * @param resultProperty
	 * @param result
	 */
	private void print (String resultProperty, List<Integer> result){
		int minK=getMinK();
		int maxK=getMaxK();
		ui.message(resultProperty+(minK==maxK?"":"s")+" for IUF-"+minK+(minK==maxK?" is:":",...,IUF-"+maxK+" are:"));
		ui.message("   "+result);
	}
	/**
	 * perform find and prints the root elements for every forest
	 * @param x a leaf
	 * @return the handler for assembling operations
	 */
	public Handler find (int x) {
		try {
			List<Integer> result = baseFind(x);
			print ("root", result);
		} catch (IUFkForestException e) {
			ui.error(e);
		}
		return this;
	}
	/**
	 * perform a union and print the forests height
	 * @param x first leaf
	 * @param y second leaf
	 * @return the handler for assembling operations
	 */
	public Handler union (int x, int y){
		try {
			baseUnion(x, y);
			height();
		} catch (IUFkForestException e) {
			ui.error(e);
		}
		return this;
	}
	/**
	 * print height for every forest
	 * @return the handler for assembling operations
	 */
	public Handler height (){
		List<Integer> result = getMaxHeightList();
		print("tree height", result);
		return this;
	}
	/**
	 * print statistics
	 * @return the handler for assembling operations
	 */
	public Handler statistics(){
		printStatistics();
		return this;
	}
	/**
	 * @param n number for elements
	 * @param k k-tree size (set minK=maxK=k)
	 * @return the handler for assembling operations
	 */
	public Handler initK1(int n, int k){
		init1(n,k,k);
		return this;
	}
	/**
	 * @param n number of elements
	 * @param maxK maximal k-tree size
	 * @return the handler for assembling operations
	 */
	public Handler initKs(int n, int maxK){
		init1(n,2,maxK);
		return this;
	}
	
	/**
	 * exit
	 * @return the handler for assembling operations
	 */
	public Handler exit(){
		 System.exit(0);
		 return this;
	}
	/**
	 * print help
	 * @return the handler for assembling operations
	 */
	public Handler help(){
		ui.message("-------------------");
		ui.message("Avialable Commands:");
		ui.message("-------------------");
		ui.message("find(x)       - search for x's root");
		ui.message("union(x,y)    - union x & y. Note: x,y allowed to be leaves");
		ui.message("height()      - print the maximal height for each DS");
		ui.message("statistics()  - print the maximal height for each DS");
		ui.message("help()        - print this H E L P");
		ui.message("exit()        - terminate this program");
		ui.message("initK1(n,k):"); 
		ui.message("             initialize a DS with n elements: 0,1,...,n-1");
		ui.message("             each root element is IUF-k size");
		ui.message("initKs(n,maxK):"); 
		ui.message("             initialize several DSs with n elements: 0,1,...,n-1");
		ui.message("             DSs root element are: IUF-2,...,IUF-maxK size");
		ui.message("             ** this is the default mode for n=10, maxK=n");
		ui.message("-----------------");
		ui.message("Avialable Aliases:");
		ui.message("-----------------");
		ui.message("f(x)          - same as find(x)");
		ui.message("u(x,y)        - same as union(x,y)");
		ui.message("h()           - same as height()");
		ui.message("s()           - same as statistics()");
		ui.message("iK1(n,k)      - same as initK1(n,k)");
		ui.message("iKs(n,maxK)   - same as initKs(n,maxK)");
		ui.message("----------------------------------------------------------------");
		return this;
	}
	
	/**
	 * find alias
	 * @param x leaf
	 * @return the handler for assembling operations
	 */
	public Handler f(int x){
		return find(x);
	}
	/**
	 * union alias
	 * @param x
	 * @param y
	 * @return the handler for assembling operations
	 */
	public Handler u(int x, int y){
		return union(x, y);
	}
	/**
	 * height alias
	 * @return the handler for assembling operations
	 */
	public Handler h(){
		return height();
	}
	/**
	 * statistics alias
	 * @return the handler for assembling operations
	 */
	public Handler s(){
		return statistics();
	}
	/**
	 * initK1 alias
	 * @param n
	 * @param k
	 * @return the handler for assembling operations
	 */
	public Handler iK1(int n, int k){
		return initK1(n, k);
	}
	/**
	 * initKs alias
	 * @param n
	 * @param maxK
	 * @return the handler for assembling operations
	 */
	public Handler iKs(int n, int maxK){
		return initKs(n, maxK);
	}
	
}