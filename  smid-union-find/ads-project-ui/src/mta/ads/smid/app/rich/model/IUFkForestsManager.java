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

public class IUFkForestsManager extends Observable<IUFkForestUnionModel>{
	private int n=20;
	private final MultiPickedState<Integer> pickedState = new MultiPickedState<Integer>();
	private List<UnionPair> unionList;
	private Set<UnionPair> unionHistory = new HashSet<UnionPair>();
	private UnionPair selectedPair;
	private final IUFkForestUnionModel dispatcher;
	private static IUFkForestsManager instance = new IUFkForestsManager();
	
	private IUFkForestsManager(){
		this.dispatcher=getEventDispatcher();
	}
	public static IUFkForestsManager getInstance() {
		return instance;
	}
	
	public int getN() {
		return n;
	}

	void setN(int n) {
		this.n=n;
		
	}
	public void setSelectedPair(UnionPair selectedPair) {
		this.selectedPair = selectedPair;
	}
	
	
	public MultiPickedState<Integer> getPickedState() {
		return pickedState;
	}
	
        
    public static class SelectionError extends Exception{
		private static final long serialVersionUID = 1L;
		SelectionError(String message){
    		super(message);
    	}
    }
    
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
	
	public void setUnionList (List<UnionPair> unionList){
		this.unionList=unionList;
	}
	
    public static class UnionFileParseError extends Exception{
		private static final long serialVersionUID = 1L;
		UnionFileParseError(String message){
    		super(message);
    	}
    }
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
