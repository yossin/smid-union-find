package mta.ads.smid.app.rich.views;

import java.awt.Frame;

import mta.ads.smid.app.rich.model.IUFkForestsManager;
import mta.ads.smid.app.ui.IUFkForestJPanel;
import mta.ads.smid.app.ui.IUFkForestJPanel.IUFkForestUnionModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * view for displaying IUF-k Forests
 * 
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class View extends ViewPart {

	/**
	 * view id
	 */
	public static final String ID = "ads-project-ui.view";
	/**
     * forest manager, to delegate action
	 */
	private final IUFkForestsManager manager = IUFkForestsManager.getInstance();
	/**
	 * union model for handling events
	 */
	private IUFkForestUnionModel unionModel;
	/**
	 * forest panel for displaying - forest
	 */
	private IUFkForestJPanel panel;
	/**
	 * k-tree size 
	 */
	private int k=2;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (unionModel != null){
			manager.removeObserver(unionModel);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		Composite swtAwtComponent = new Composite(parent, SWT.EMBEDDED | SWT.LEFT_TO_RIGHT);
		Frame frame = SWT_AWT.new_Frame( swtAwtComponent );
		panel = new IUFkForestJPanel(manager.getPickedState(),k,manager.getN());

		frame.add(panel);

		unionModel = panel.getUnionModel();
		manager.addObserver(unionModel);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
	}
	
	
	/**
	 * initialize the forest using:
	 * <ul>
	 * <li>k-tree size - set as a member of this class (by <i>OpenViewAction</i>)</li>
	 * </li>n elements - from manager</li>
	 * </ul>
	 */
	public void initializeForest(){
		panel.initializeForest(k,manager.getN());
	}

	/**
	 * set k-tree size.
	 * <br>after setting k the following happens:
	 * <ul>
	 * <li>set this part name (title) with the new value of k</li> 
	 * <li>initialize forest with the new value of k</li> 
	 * </ul>
	 * @param k
	 */
	public void setK(int k) {
		this.k=k;
		initializeForest();
		setPartName("IUF-"+k+" Forest");
	}
	
}
