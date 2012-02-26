package mta.ads.smid.app.rich.commands;

/**
 * Interface defining the application's command IDs.
 * Key bindings can be defined for specific commands.
 * To associate an action with a command, use IAction.setActionDefinitionId(commandId).
 *
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds {

    public static final String CMD_OPEN = "ads-project-ui.open";
    public static final String CMD_UNION = "ads-project-ui.union";
    public static final String CMD_UNION_SEQUENCE = "ads-project-ui.unionSequence";
	public static final String CMD_OPEN_UNION_FILE = "ads-project-ui.openUnionFile";
    
}
