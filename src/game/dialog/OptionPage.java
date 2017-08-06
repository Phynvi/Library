package game.dialog;

/**
 * The {@code OptionPage} will hold the {@link game.dialog.DialogOption} options
 * in a page and handle the execution of the actions of them.
 * 
 * @author Albert Beaupre
 */
public class OptionPage extends Page {

    /**
     * The options within this {@code OptionPage}.
     */
    public final DialogOption[] options;

    /**
     * Constructs a new {@code OptionPage}.
     * 
     * @param title
     *            the title of the page
     * @param options
     *            the options
     */
    public OptionPage(String title, DialogOption... options) {
	super(title);
	this.options = options;
    }

    /**
     * Executes the entityOption, if any
     * 
     * @param entityOption
     */
    public final void execute(int option) {
	if (options.length <= option)
	    throw new ArrayIndexOutOfBoundsException("The DialogOption selected does not exist in the page");
	options[option].action.run();
    }

}
