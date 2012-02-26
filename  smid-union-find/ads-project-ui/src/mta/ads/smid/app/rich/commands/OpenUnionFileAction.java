package mta.ads.smid.app.rich.commands;

import java.io.File;
import java.util.LinkedList;

import mta.ads.smid.app.rich.Activator;
import mta.ads.smid.app.rich.views.UnionHistoryView;
import mta.ads.smid.app.rich.views.View;
import mta.ads.smid.app.util.UnionPair;
import mta.ads.smid.app.util.UnionPairReader;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


public class OpenUnionFileAction extends Action {

    private final IUFkForestsManager manager = IUFkForestsManager.getInstance();
    private final IWorkbenchWindow window;
    
    public OpenUnionFileAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        setId(ICommandIds.CMD_OPEN_UNION_FILE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_OPEN_UNION_FILE);
        setImageDescriptor(Activator.getImageDescriptor("/icons/open-union-file.jpg"));
    }
    

    private File getUnionFile(){
    	FileDialog fd = new FileDialog(window.getShell(), SWT.OPEN);
    	String[] extensions = {"*.*"};
		fd.setFilterExtensions(extensions);
		fd.setText("Select union file");
		
		String fileName = fd.open();
		if (fileName != null){
			File file = new File(fileName);
			if (file.exists()){
				return file;
			}
		}
		return null;
    }

    public void run() {
    	File unionFile = getUnionFile();
    	if (unionFile == null){
    		return;
    	}
    	UnionPairReader reader = new UnionPairReader(unionFile);
    	Integer elementsNumber = reader.readElementNumber();
    	if (elementsNumber == null){
    		MessageDialog.openInformation(window.getShell(), "Read File Error", "Wrong format for union file. \r\nUnable to read find elements number.");
    	}
    	manager.setN(elementsNumber);
    	LinkedList<UnionPair> unionList = new LinkedList<UnionPair>();
    	for (UnionPair unionPair : reader) {
			unionList.add(unionPair);
		}
    	
    	IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
    	getActivePage().getViewReferences();
    	for (IViewReference ref :views){
    		if (ref.getId().equals(UnionHistoryView.ID)){
    			UnionHistoryView unionsView = (UnionHistoryView) ref.getView(true);
    	    	unionsView.setUnionList(unionList);
    		} else if (ref.getId().equals(View.ID)){
    			View view = (View) ref.getView(true);
    			view.initializeForest();
    		}
    	}
    }
}