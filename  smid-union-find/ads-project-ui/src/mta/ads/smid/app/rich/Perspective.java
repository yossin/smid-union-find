package mta.ads.smid.app.rich;

import mta.ads.smid.app.rich.views.UnionHistoryView;
import mta.ads.smid.app.rich.views.View;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class Perspective implements IPerspectiveFactory {

	/**
	 * The ID of the perspective as specified in the extension.
	 */
	public static final String ID = "ads-project-ui.perspective";

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(UnionHistoryView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
		IFolderLayout folder = layout.createFolder("Forests", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(View.ID + ":*");
		
		//folder.addView(View.ID);
		
		layout.getViewLayout(UnionHistoryView.ID).setCloseable(false);
	}
}
