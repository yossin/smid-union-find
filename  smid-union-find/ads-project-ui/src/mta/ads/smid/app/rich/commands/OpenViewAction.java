package mta.ads.smid.app.rich.commands;

import mta.ads.smid.app.rich.Activator;
import mta.ads.smid.app.rich.views.View;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;


public class OpenViewAction extends Action {
	
	private final IWorkbenchWindow window;
	private int instanceNum = 0;
	private final String viewId;
	
	public OpenViewAction(IWorkbenchWindow window, String label, String viewId) {
		this.window = window;
		this.viewId = viewId;
        setText(label);
        // The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_OPEN);
        // Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(ICommandIds.CMD_OPEN);
		setImageDescriptor(Activator.getImageDescriptor("/icons/add-forest3.jpg"));
		
	}
	
	public void run() {
		if(window != null) {	
			try {

				
	            final Shell dialogShell = new Shell(window.getShell(), SWT.PRIMARY_MODAL | SWT.SHEET);
	            dialogShell.setLayout(new GridLayout());
	            
	    		Composite top = new Composite(dialogShell, SWT.LEFT|SWT.LEFT_TO_RIGHT);
	    		GridLayout layout = new GridLayout(2,false);
	    		layout.marginHeight = 0;
	    		layout.marginWidth = 0;
	    		top.setLayout(layout);
	            
	            
	    		// setup bold font
	    		Font boldFont = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);    
	    		
	    		Label l = new Label(top, SWT.WRAP);
	    		l.setText("Select Forest's 'k' value:");
	    		l.setFont(boldFont);

	    		final Text kText = new Text(top, SWT.SINGLE | SWT.WRAP);
	    		kText.setText("2");
	    		
	    		l = new Label(top, SWT.WRAP |SWT.CENTER);
	    		l.setText("(2 <= k <= 10)");
	    		

	            
	            Button closeButton = new Button(dialogShell, SWT.PUSH);
	            closeButton.setText("Create");
	            closeButton.addSelectionListener(new SelectionAdapter() {
	                @Override
	                public void widgetSelected(SelectionEvent e) {
	                	try {
	                		int k = Integer.parseInt(kText.getText());
	                		if (k>=2 && k<=10){
	                			dialogShell.dispose();
	                		}
	                	} catch (NumberFormatException ex){
	                	}
	                }
	            });
	            dialogShell.setDefaultButton(closeButton);
	            dialogShell.addDisposeListener(new DisposeListener() {
	                @Override
	                public void widgetDisposed(DisposeEvent e) {
						try {
		                	int k =Integer.parseInt(kText.getText());
	
		                	IViewPart part = window.getActivePage().showView(viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
		                	View view = (View) part;
	    					view.setK(k);
	        			} catch (PartInitException ex) {
	        				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + ex.getMessage());
	        			}
	                }
	            });
	            dialogShell.pack();
	            dialogShell.open();
				
			} catch (Exception e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}
}
