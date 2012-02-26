package mta.ads.smid.app.rich.views;

import java.awt.Frame;

import mta.ads.smid.app.rich.model.IUFkForestsManager;
import mta.ads.smid.app.ui.IUFkForestJPanel;
import mta.ads.smid.app.ui.IUFkForestJPanel.IUFkForestUnionModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart {

	public static final String ID = "ads-project-ui.view";
	private final IUFkForestsManager model = IUFkForestsManager.getInstance();
	private IUFkForestUnionModel unionModel;
	private IUFkForestJPanel panel;
	private int k=2;
	
	@Override
	public void dispose() {
		super.dispose();
		if (unionModel != null){
			model.removeObserver(unionModel);
		}
	}
	
	public void createPartControl(Composite parent) {
		
		Composite swtAwtComponent = new Composite(parent, SWT.EMBEDDED | SWT.LEFT_TO_RIGHT);
		Frame frame = SWT_AWT.new_Frame( swtAwtComponent );
		panel = new IUFkForestJPanel(model.getPickedState(),k,model.getN());

		frame.add(panel);

		unionModel = panel.getUnionModel();
		model.addObserver(unionModel);
		
	}

	public void setFocus() {
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
