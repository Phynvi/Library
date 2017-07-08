package game.dialog;

import java.util.ArrayDeque;

/**
 * The {@code Dialog} class is used to create a conversation between any player
 * or NPC using the {@link game.dialog.Page} to create pages as a written
 * conversation.
 * 
 * @author Albert Beaupre
 */
public class Dialog implements FacialExpressions {

    /**
     * A queue containing any upcoming {@code Page}.
     */
    protected final ArrayDeque<Page> pages = new ArrayDeque<>();

    /**
     * The transactor that will converse within this dialog.
     */
    protected final DialogTransactor transactor;

    /**
     * Constructs a new {@code Dialog} based on the {@code DialogTransactor}.
     * 
     * @param transactor
     *            the transactor
     */
    public Dialog(DialogTransactor transactor) {
	this.transactor = transactor;
    }

    /**
     * 
     * @param optionName
     * @param i
     */
    public Dialog options(String optionName, Runnable i) {
	DialogOption option1 = new DialogOption(optionName, i);
	pages.add(new OptionPage("Choose an option", option1));
	return this;
    }

    /**
     * 
     * @param optionName
     * @param i
     * @param optionName2
     * @param i2
     */
    public Dialog options(String optionName, Runnable i, String optionName2, Runnable i2) {
	DialogOption option1 = new DialogOption(optionName, i);
	DialogOption option2 = new DialogOption(optionName2, i2);
	pages.add(new OptionPage("Choose an option", option1, option2));
	return this;
    }

    /**
     * 
     * @param optionName
     * @param i
     * @param optionName2
     * @param i2
     * @param optionName3
     * @param i3
     */
    public Dialog options(String optionName, Runnable i, String optionName2, Runnable i2, String optionName3, Runnable i3) {
	DialogOption option1 = new DialogOption(optionName, i);
	DialogOption option2 = new DialogOption(optionName2, i2);
	DialogOption option3 = new DialogOption(optionName3, i3);
	pages.add(new OptionPage("Choose an option", option1, option2, option3));
	return this;
    }

    /**
     * 
     * @param optionName
     * @param i
     * @param optionName2
     * @param i2
     * @param optionName3
     * @param i3
     * @param optionName4
     * @param i4
     */
    public Dialog options(String optionName, Runnable i, String optionName2, Runnable i2, String optionName3, Runnable i3, String optionName4, Runnable i4) {
	DialogOption option1 = new DialogOption(optionName, i);
	DialogOption option2 = new DialogOption(optionName2, i2);
	DialogOption option3 = new DialogOption(optionName3, i3);
	DialogOption option4 = new DialogOption(optionName4, i4);
	pages.add(new OptionPage("Choose an option", option1, option2, option3, option4));
	return this;
    }

    /**
     * 
     * @param optionName
     * @param i
     * @param optionName2
     * @param i2
     * @param optionName3
     * @param i3
     * @param optionName4
     * @param i4
     * @param optionName5
     * @param i5
     */
    public Dialog options(String optionName, Runnable i, String optionName2, Runnable i2, String optionName3, Runnable i3, String optionName4, Runnable i4, String optionName5, Runnable i5) {
	DialogOption option1 = new DialogOption(optionName, i);
	DialogOption option2 = new DialogOption(optionName2, i2);
	DialogOption option3 = new DialogOption(optionName3, i3);
	DialogOption option4 = new DialogOption(optionName4, i4);
	DialogOption option5 = new DialogOption(optionName5, i5);
	pages.add(new OptionPage("Choose an option", option1, option2, option3, option4, option5));
	return this;
    }

    /**
     * Displays an {@code InformationPage} with the specified {@code text}.
     * 
     * @param the
     *            text to display on the page
     */
    public Dialog info(String text) {
	pages.add(new InformationPage(text));
	return this;
    }

    /**
     * Writes multiple actions to the last {@code Page} written to this
     * {@code Dialog}.
     * 
     * @param runnables
     *            the actions to execute when the {@code Page} is opened.
     */
    public Dialog then(Runnable... runnables) {
	pages.peekLast().action = () -> {
	    for (Runnable r : runnables)
		r.run();
	};
	return this;
    }

    /**
     * Writes a {@code Page} to this {@code Dialog} that is meant to be written
     * for a player to speak.
     * 
     * <p>
     * This method is effectively equivalent to:
     * 
     * <pre>
     * player(CALM_TALK, name);
     * </pre>
     * 
     * @param name
     *            the name that is spoken
     */
    public Dialog player(String text) {
	return this.player(CALM_TALK, text);
    }

    /**
     * Writes a {@code Page} to this {@code Dialog} that is meant to be written
     * for a player to speak.
     * 
     * @param expression
     *            the expression id value of the player speaking
     * @param name
     *            the name that is spoken
     */
    public Dialog player(int expression, String text) {
	Page page = new Page(text);
	page.npcId = -1;
	page.expression = expression;
	pages.add(page);
	return this;
    }

    /**
     * Continues the dialog to the next page.
     * 
     * <p>
     * The {@code Page} returned is the next page within this {@code Dialog}, if
     * existing, If there is not another page, the
     * {@link DialogTransactor#exitDialog()} method is called and the dialog is
     * stopped and null is returned.
     * 
     * @return the next page; return null otherwise
     */
    public Page continueDialog() {
	if (pages.isEmpty()) {
	    transactor.exitDialog();
	    return null;
	}
	Page page = pages.poll();
	if (page instanceof InformationPage) {
	    transactor.displayInformation((InformationPage) page);
	} else if (page instanceof OptionPage) {
	    transactor.displayOptions((OptionPage) page);
	    pages.clear();
	} else {
	    transactor.displayDialogPage(page);
	}
	if (page.action != null)
	    page.action.run();
	return page;
    }

    /**
     * Selects the option from an {@code OptionPage} and executes the action
     * within the option.
     * 
     * @param index
     *            the index of the option to select
     * 
     * @throws UnsupportedOperationException
     *             if an {@code OptionPage} is not the current page or if the
     *             specified index of the option is not available
     */
    public Dialog selectOption(int index) {
	Page last = pages.peekLast();
	if (!(last instanceof OptionPage))
	    throw new UnsupportedOperationException("The page selected is not an OptionPage");
	OptionPage page = (OptionPage) last;
	if (index >= page.options.length)
	    throw new UnsupportedOperationException("The option on the page does not exist");
	page.options[index].action.run();
	if (pages.isEmpty()) {
	    transactor.exitDialog();
	    return null;
	}
	return this;
    }

}
