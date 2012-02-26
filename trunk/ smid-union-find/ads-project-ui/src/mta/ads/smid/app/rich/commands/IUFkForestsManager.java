package mta.ads.smid.app.rich.commands;

import java.util.Iterator;
import java.util.Set;

import mta.ads.smid.app.rich.views.UnionHistoryView;
import mta.ads.smid.app.ui.IUFkForestJPanel.IUFkForestUnionModel;
import mta.ads.smid.model.IUFkForestException;
import mta.ads.smid.patterns.Observable;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;

public class IUFkForestsManager extends Observable<IUFkForestUnionModel>{
	private static IUFkForestsManager instance = new IUFkForestsManager();
	int n=20;
	final MultiPickedState<Integer> pickedState = new MultiPickedState<Integer>();
	
	private IUFkForestsManager(){
		this.dispatcher=getEventDispatcher();
	}
	private final IUFkForestUnionModel dispatcher;
	public static IUFkForestsManager getInstance() {
		return instance;
	}
	
	public void union(int x, int y) throws IUFkForestException{
		dispatcher.union(x, y);
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n=n;
		
	}
	
	
	public MultiPickedState<Integer> getPickedState() {
		return pickedState;
	}
	

    
    private void notifyUnion(int x, int y){
    	UnionHistoryView unionsView= (UnionHistoryView) ActionUtil.getView(UnionHistoryView.ID);
    	unionsView.addUnion(x, y);
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
			union(x, y);
			notifyUnion(x,y);

		} catch (IUFkForestException e) {
    		throw new SelectionError("please pick 2 leafs!");
		}
	}
	
	
}
