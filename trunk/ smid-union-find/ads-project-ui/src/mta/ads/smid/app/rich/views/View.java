package mta.ads.smid.app.rich.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Rectangle;
import java.util.Set;

import javax.swing.JPanel;

import mta.ads.smid.app.rich.commands.IUFkForestsManager;
import mta.ads.smid.app.ui.IUFkForestJPanel;
import mta.ads.smid.app.ui.IUFkForestJPanel.IUFkForestUnionModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.uci.ics.jung.visualization.picking.MultiPickedState;

public class View extends ViewPart {

	public static final String ID = "ads-project-ui.view";
	private final IUFkForestsManager model = IUFkForestsManager.getInstance();
	private IUFkForestUnionModel unionModel;
	private IUFkForestJPanel panel;
	private int k=5;
	
	@Override
	public void dispose() {
		super.dispose();
		if (unionModel != null){
			model.removeObserver(unionModel);
		}
	}
	
	private MultiPickedState<Integer> getMultiPickedState(){
    	/*
		IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
    	getActivePage().getViewReferences();
    	
    	for (IViewReference ref :views){
    		if (ref.getId().equals(View.ID)){
    			View view = (View) ref.getView(true);
    			return view.panel.getMultiPickedState();
    		}
    	}
    	return new MultiPickedState<Integer>();
    	*/
		return model.getPickedState();
	}
	
	public void createPartControl(Composite parent) {
		
		Composite swtAwtComponent = new Composite(parent, SWT.EMBEDDED | SWT.LEFT_TO_RIGHT);
		Frame frame = SWT_AWT.new_Frame( swtAwtComponent );
		panel = new IUFkForestJPanel(getMultiPickedState(),k,model.getN());

		frame.add(panel);

		unionModel = panel.getUnionModel();
		model.addObserver(unionModel);
		
	}

	public void setFocus() {
	}
	
	public Set<Integer> getPicked(){
		return panel.getPiked();
	}
	
	public void initializeForest(){
		panel.initializeForest(k,model.getN());
	}

	public void setK(int k) {
		this.k=k;
		initializeForest();
		setPartName("IUF-"+k+" Forest");
	}

	
}
