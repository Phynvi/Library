package game.dialog;

/**
 * The {@code DialogTransactor} is meant to carry out conversations and will have methods to display
 * the conversations within the game.
 * 
 * @author Albert Beaupre
 */
public interface DialogTransactor {

	/**
	 * 
	 * @param dialog
	 */
	public abstract void initializeDialog(Dialog dialog);

	/**
	 * Displays the given {@code OptionPage} to the screen of the DialogTransactor.
	 * 
	 * @param page
	 *            the page to display
	 */
	public abstract void displayOptions(OptionPage page);

	/**
	 * Displays the given {@code Page} to the screen of the DialogTransactor.
	 * 
	 * @param page
	 *            the page to display
	 */
	public abstract void displayDialogPage(Page page);

	/**
	 * Displays the given {@code InformationPage} to the screen of the DialogTransactor.
	 * 
	 * @param page
	 *            the page to display
	 */
	public abstract void displayInformation(InformationPage page);

	/**
	 * Executes any actions to exit the dialog, if any, that this {@code DialogTransactor} is in.
	 */
	public abstract void exitDialog();

}
