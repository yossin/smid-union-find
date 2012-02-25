package mta.ads.smid.app.interactive;

import java.util.List;

import mta.ads.smid.app.MultiIUFkForests;
import mta.ads.smid.model.IUFkForestException;

public class Handler extends MultiIUFkForests{
	
	final UI ui;
	public Handler(UI ui){
		super(10, 2, 2);
		this.ui=ui;
		initK1(10, 2);
	}
	private void init1(int n, int minK, int maxK){
		baseInit(n, minK, maxK);
		ui.message("init "+n+" elements (0,..,"+(n-1)+")");
		ui.message("init "+((minK==maxK)?" IUF-"+minK:" IUF-"+minK+",...,IUF-"+maxK));
	}

	private void print (String resultProperty, List<Integer> result){
		int minK=getMinK();
		int maxK=getMaxK();
		ui.message(resultProperty+(minK==maxK?"":"s")+" for IUF-"+minK+(minK==maxK?" is:":",...,IUF-"+maxK+" are:"));
		ui.message("   "+result);
	}
	public Handler find (int x) {
		try {
			List<Integer> result = baseFind(x);
			print ("root", result);
		} catch (IUFkForestException e) {
			ui.error(e);
		}
		return this;
	}
	public Handler union (int x, int y){
		try {
			baseUnion(x, y);
			height();
		} catch (IUFkForestException e) {
			ui.error(e);
		}
		return this;
	}
	public Handler height (){
		List<Integer> result = getMaxHeightList();
		print("tree height", result);
		return this;
	}
	public Handler initK1(int n, int k){
		init1(n,k,k);
		return this;
	}
	public Handler initKs(int n, int maxK){
		init1(n,2,maxK);
		return this;
	}
	
	public Handler exit(){
		 System.exit(0);
		 return this;
	}
	public Handler help(){
		ui.message("Avialable Commands:");
		ui.message("-------------------");
		ui.message("find(x)    - search for x's root");
		ui.message("union(x,y) - union x & y. Note: x,y allowed to be leaves");
		ui.message("help()     - print this H E L P");
		ui.message("exit()     - terminate this program");
		ui.message("initK1(n,k):"); 
		ui.message("             initialize a DS with n elements: 0,1,...,n-1");
		ui.message("             each root element is IUF-k size");
		ui.message("initKs(n,maxK):"); 
		ui.message("             initialize several DSs with n elements: 0,1,...,n-1");
		ui.message("             DSs root element are: IUF-2,...,IUF-maxK size");
		ui.message("             ** this is the default mode for n=10, maxK=n");
		return this;
	}
	
	public Handler f(int x){
		return find(x);
	}
	public Handler u(int x, int y){
		return union(x, y);
	}
	public Handler h(){
		return height();
	}
	public Handler iK1(int n, int k){
		return initK1(n, k);
	}
	public Handler iKs(int n, int maxK){
		return initKs(n, maxK);
	}
	
}