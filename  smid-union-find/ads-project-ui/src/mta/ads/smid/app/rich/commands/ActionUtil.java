package mta.ads.smid.app.rich.commands;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

public class ActionUtil {
	public static IViewPart getView(String id){
    	IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
    	getActivePage().getViewReferences();
    	for (IViewReference ref :views){
    		if (ref.getId().equals(id)){
    			return ref.getView(true);
    		}
    	}
    	return null;
	}
}
