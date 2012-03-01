package mta.ads.smid.app.rich.commands;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 * @author Yossi Naor & Yosi Zilberberg
 *
 */
public interface ICommandIds {

    /**
     * open view action
     */
    public static final String CMD_OPEN = "ads-project-ui.open";
    /**
     * union action
     */
    public static final String CMD_UNION = "ads-project-ui.union";
    /**
     * union sequence action
     */
    public static final String CMD_UNION_SEQUENCE = "ads-project-ui.unionSequence";
	/**
	 * open union file action
	 */
	public static final String CMD_OPEN_UNION_FILE = "ads-project-ui.openUnionFile";
    
}
