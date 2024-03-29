package mta.ads.smid.app.rich;

import mta.ads.smid.app.rich.commands.OpenUnionFileAction;
import mta.ads.smid.app.rich.commands.OpenViewAction;
import mta.ads.smid.app.rich.commands.UnionAction;
import mta.ads.smid.app.rich.commands.UnionSequenceAction;
import mta.ads.smid.app.rich.views.View;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 *
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    private IWorkbenchAction exitAction;
    private IWorkbenchAction aboutAction;
    private IWorkbenchAction newWindowAction;
    private OpenViewAction addIUFkForestAction;
    private Action unionAction;
    private Action unionSequenceAction;
    private Action openUnionFile;
    

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        
        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
        register(newWindowAction);
        
        addIUFkForestAction = new OpenViewAction(window, "Add IUF-k Forest", View.ID);
        register(addIUFkForestAction);
        
        unionAction = new UnionAction("Union", window);
        register(unionAction);

        unionSequenceAction = new UnionSequenceAction("Union Sequence", window);
        register(unionSequenceAction);
        
        openUnionFile = new OpenUnionFileAction("Open Union File", window);
        register(openUnionFile);
        
    }
    
    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        
        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);
        
        // File
        fileMenu.add(newWindowAction);
        fileMenu.add(new Separator());
        fileMenu.add(unionAction);
        fileMenu.add(addIUFkForestAction);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        
        // Help
        helpMenu.add(aboutAction);
    }
    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
        toolbar.add(addIUFkForestAction);
        toolbar.add(unionAction);
        toolbar.add(unionSequenceAction);
        toolbar.add(openUnionFile);
    }
}
