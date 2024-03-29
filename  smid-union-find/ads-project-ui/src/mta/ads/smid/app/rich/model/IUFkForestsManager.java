package mta.ads.smid.app.rich.model;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mta.ads.smid.app.ui.IUFkForestJPanel.IUFkForestUnionModel;
import mta.ads.smid.app.util.UnionPair;
import mta.ads.smid.app.util.UnionPairReader;
import mta.ads.smid.model.IUFkForestException;
import mta.ads.smid.patterns.Observable;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;

/**
 * union-find k-forest manager for UI
 * 
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
/**
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class IUFkForestsManager extends Observable<IUFkForestUnionModel>{
	/**
	 * the number of elements (default = 20)
	 */
	private int n=20;
	/**
	 * multi picked state for sharing among views
	 */
	private final MultiPickedState<Integer> pickedState = new MultiPickedState<Integer>();
	/**
	 * loaded union list
	 */
	private List<UnionPair> unionList;
	/**
	 * union history (performed unions)
	 */
	private Set<UnionPair> unionHistory = new HashSet<UnionPair>();
	/**
	 * selected pair (from union list)
	 */
	private UnionPair selectedPair;
	/**
	 * dispatcher that delegates the commands into the UI 
	 */
	private final IUFkForestUnionModel dispatcher;
	/**
	 * singleton instance
	 */
	private static IUFkForestsManager instance = new IUFkForestsManager();
	
	/**
	 * private constructor
	 */
	private IUFkForestsManager(){
		this.dispatcher=getEventDispatcher();
	}
	/**
	 * @return the singleton instance
	 */
	public static IUFkForestsManager getInstance() {
		return instance;
	}
	
	/**
	 * @return the number of elements
	 */
	public int getN() {
		return n;
	}

	/**
	 * set the number of elements
	 * @param n
	 */
	void setN(int n) {
		this.n=n;
		
	}
	/**
	 * set selected pair form union history
	 * @param selectedPair
	 */
	public void setSelectedPair(UnionPair selectedPair) {
		this.selectedPair = selectedPair;
	}
	
	
	/**
	 * @return picked state
	 */
	public MultiPickedState<Integer> getPickedState() {
		return pickedState;
	}
	
        
    /**
     * selection error, for instance: when selecting wrong number of leaves for union action
     * @author Yossi Naor & Yosi Zilberberg
     *
     */
    public static class SelectionError extends Exception{
		private static final long serialVersionUID = 1L;
		SelectionError(String message){
    		super(message);
    	}
    }
    
	/**
	 * perform union according to the selected leaves
	 * @throws SelectionError
	 */
	public void union() throws SelectionError{
		Set<Integer> picked = pickedState.getPicked();
    	if (picked.size()!=2){
    		throw new SelectionError("please pick 2 items in order to perform a union");
    	}
    	
		Iterator<Integer> iterator = picked.iterator();
		int x = iterator.next();
		int y = iterator.next();
		try {
			UnionPair pair = new UnionPair(x, y);
			if (unionHistory.contains(pair)){
				return;
			}
			
			dispatcher.union(x, y);
			if (unionHistory.containsAll(unionList) == false){
				unionList.clear();
				unionList.addAll(unionHistory);
			}
			
			unionHistory.add(pair);
	    	unionList.add(pair);

		} catch (IUFkForestException e) {
    		throw new SelectionError("please pick 2 leafs!");
		}
	}
	
	/**
	 * perform union from union list, until the selected pair
	 * @throws SelectionError
	 */
	public void unionSequence() throws SelectionError{
		if (selectedPair==null){
			return;
		}
		UnionPair lastIterated = selectedPair;
		unionHistory = new HashSet<UnionPair>();
		dispatcher.initialize(n);
		for (UnionPair pair : unionList){
			try {
				dispatcher.union(pair.x, pair.y);
				unionHistory.add(pair);
			} catch (IUFkForestException e) {
	    		throw new SelectionError(e.getMessage());
			}
			if (pair.equals(lastIterated)){
				return;
			}
		}
	}
	
	/**
	 * set a new union list (will be set only once, when union history view is created)
	 * @param unionList
	 */
	public void setUnionList (List<UnionPair> unionList){
		this.unionList=unionList;
	}
	
    /**
     * union parse error
     * @author Yossi Naor & Yosi Zilberberg
     *
     */
    public static class UnionFileParseError extends Exception{
		private static final long serialVersionUID = 1L;
		UnionFileParseError(String message){
    		super(message);
    	}
    }
	/**
	 * load unions from a file
	 * @param unionFile a file that contains a union sequence
	 * @throws UnionFileParseError
	 */
	public void loadUnions(File unionFile) throws UnionFileParseError{
    	UnionPairReader reader = new UnionPairReader(unionFile);
    	Integer elementsNumber = reader.readElementNumber();
    	if (elementsNumber == null){
    		throw new UnionFileParseError("Wrong format for union file. \r\nUnable to read find elements number.");
    	}
    	setN(elementsNumber);
    	unionList.clear();
    	unionHistory.clear();
    	for (UnionPair unionPair : reader) {
			unionList.add(selectedPair=unionPair);
		}
    	pickedState.clear();
    	dispatcher.initialize(n);
	}
	
}
