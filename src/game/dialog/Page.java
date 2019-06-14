package game.dialog;

/**
 * The {@code Page} class represents a page within a dialog (written
 * conversation). This class is used by the {@link game.dialog.Dialog} class to
 * create a written conversation used by NPCs or Players within the game.
 * 
 * @author Albert Beaupre
 */
public class Page {

	/**
	 * This is the text displayed on this {@code Page}.
	 */
	public final String text;

	/**
	 * This is the action taken when this page is opened. The value is
	 * automatically set to null so no action will be taken.
	 */
	public Runnable action;

	/**
	 * This is the id of the npc that is talking. If an NPC is not talking and a
	 * Player is, then this value is set to -1.
	 * 
	 * <p>
	 * <b> This value is set to -1 by default.</b>
	 */
	public int npcId = -1;

	/**
	 * This is the value of the facial expression used by an NPC or Player. This
	 * value isn't used by the {@link game.dialog.OptionPage} class.
	 * 
	 * <p>
	 * <b>This value is set to {@link game.dialog.FacialExpressions#CALM_TALK}
	 * by default </b>
	 */
	public int expression = FacialExpressions.CALM_TALK;

	/**
	 * Constructs a new {@code Page} from the specified {@code text}. If the
	 * text is null or has no length to it, then the text value is set to "..."
	 * 
	 * @param text
	 *            the text displayed on this page
	 */
	public Page(String text) {
		if (text == null || text.isEmpty())
			text = "...";
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

}
