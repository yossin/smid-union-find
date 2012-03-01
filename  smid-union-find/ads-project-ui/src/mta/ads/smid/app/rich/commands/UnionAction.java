package mta.ads.smid.app.rich.commands;

import mta.ads.smid.app.rich.model.IUFkForestsManager;
import mta.ads.smid.app.rich.model.IUFkForestsManager.SelectionError;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;


/**
 * Union command
 * 
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class UnionAction extends Action {

    /**
     * workbench window
     */
    private final IWorkbenchWindow window;

    /**
     * forest manager, to delegate action
     */
    private final IUFkForestsManager manager = IUFkForestsManager.getInstance();

    /**
     * @param text
     * @param window
     */
    public UnionAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_UNION);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_UNION);
        setImageDescriptor(mta.ads.smid.app.rich.Activator.getImageDescriptor("/icons/union.jpg"));
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
    	try {
			manager.union();
		} catch (SelectionError e) {
    		MessageDialog.openInformation(window.getShell(), "Union Error", e.getMessage());
		}
    }
}