package mta.ads.smid.app.rich.commands;

import java.io.File;

import mta.ads.smid.app.rich.Activator;
import mta.ads.smid.app.rich.model.IUFkForestsManager;
import mta.ads.smid.app.rich.model.IUFkForestsManager.UnionFileParseError;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;


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
    	try {
			manager.loadUnions(unionFile);
		} catch (UnionFileParseError e) {
    		MessageDialog.openInformation(window.getShell(), "Read File Error", e.getMessage());
		}
    }
}