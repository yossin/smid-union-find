package tests;

import java.applet.Applet;

import edu.uci.ics.jung.visualization.picking.MultiPickedState;

import mta.ads.smid.app.ui.IUFkForestJPanel;

public class ViewerApplet extends Applet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		
		IUFkForestJPanel panel = new IUFkForestJPanel(new MultiPickedState<Integer>(),5,33);
		add(panel);
	}

}
