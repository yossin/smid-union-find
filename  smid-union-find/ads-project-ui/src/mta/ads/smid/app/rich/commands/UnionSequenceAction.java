package mta.ads.smid.app.rich.commands;

import java.util.LinkedList;
import java.util.List;

import mta.ads.smid.app.rich.Activator;
import mta.ads.smid.app.rich.model.IUFkForestsManager;
import mta.ads.smid.app.rich.views.UnionHistoryView;
import mta.ads.smid.app.rich.views.View;
import mta.ads.smid.app.util.UnionPair;
import mta.ads.smid.model.IUFkForestException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


public class UnionSequenceAction extends Action {

    private final IWorkbenchWindow window;
    
    private final IUFkForestsManager manager = IUFkForestsManager.getInstance();

    public UnionSequenceAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_UNION_SEQUENCE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_UNION_SEQUENCE);
        setImageDescriptor(Activator.getImageDescriptor("/icons/union-sequence.jpg"));
    }
    

    private void union(final UnionPair pair){
		try {
			manager.union(pair.x, pair.y);
		} catch (IUFkForestException e) {
    		MessageDialog.openInformation(window.getShell(), "Union Error", e.getMessage());
		}
    }

    public void run() {
    	IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
    	getActivePage().getViewReferences();
    	UnionHistoryView unionsView = null;
    	for (IViewReference ref :views){
    		if (ref.getId().equals(UnionHistoryView.ID)){
    			unionsView = (UnionHistoryView) ref.getView(true);
    		} else if (ref.getId().equals(View.ID)){
    			View view = (View) ref.getView(true);
    			view.initializeForest();
    		}
    	}
		List<UnionPair> unions = unionsView.getUnionPairSequence();
		for (UnionPair pair : unions) {
			union(pair);
		}
    }
}