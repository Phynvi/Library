package game.dialog;

import java.util.ArrayDeque;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The {@code Dialog} class is used to create a conversation between any player or NPC using the
 * {@link game.dialog.Page} to create pages as a written conversation.
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

	private boolean breakHere;
	private Page currentPage;

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
	 * Displays 1 option with 1 action.
	 */
	public Dialog options(String optionName, Runnable i) {
		DialogOption option1 = new DialogOption(optionName, i);
		pages.add(new OptionPage("Choose an Option", option1));
		return this;
	}

	/**
	 * 
	 * Displays 2 options with 2 actions.
	 */
	public Dialog options(String optionName, Runnable i, String optionName2, Runnable i2) {
		DialogOption option1 = new DialogOption(optionName, i);
		DialogOption option2 = new DialogOption(optionName2, i2);
		pages.add(new OptionPage("Choose an Option", option1, option2));
		return this;
	}

	/**
	 * 
	 * Displays 3 options with 3 actions.
	 */
	public Dialog options(String optionName, Runnable i, String optionName2, Runnable i2, String optionName3, Runnable i3) {
		DialogOption option1 = new DialogOption(optionName, i);
		DialogOption option2 = new DialogOption(optionName2, i2);
		DialogOption option3 = new DialogOption(optionName3, i3);
		pages.add(new OptionPage("Choose an Option", option1, option2, option3));
		return this;
	}

	/**
	 * 
	 * Displays 4 options with 4 actions.
	 */
	public Dialog options(String optionName, Runnable i, String optionName2, Runnable i2, String optionName3, Runnable i3, String optionName4, Runnable i4) {
		DialogOption option1 = new DialogOption(optionName, i);
		DialogOption option2 = new DialogOption(optionName2, i2);
		DialogOption option3 = new DialogOption(optionName3, i3);
		DialogOption option4 = new DialogOption(optionName4, i4);
		pages.add(new OptionPage("Choose an Option", option1, option2, option3, option4));
		return this;
	}

	/**
	 * Displays 5 options with 5 actions.
	 */
	public Dialog options(String optionName, Runnable i, String optionName2, Runnable i2, String optionName3, Runnable i3, String optionName4, Runnable i4, String optionName5, Runnable i5) {
		DialogOption option1 = new DialogOption(optionName, i);
		DialogOption option2 = new DialogOption(optionName2, i2);
		DialogOption option3 = new DialogOption(optionName3, i3);
		DialogOption option4 = new DialogOption(optionName4, i4);
		DialogOption option5 = new DialogOption(optionName5, i5);
		pages.add(new OptionPage("Choose an Option", option1, option2, option3, option4, option5));
		return this;
	}

	/**
	 * Displays an {@code InformationPage} with the specified {@code text}.
	 * 
	 * @param the
	 *            text to display on the page
	 * @return a chain of this instance
	 */
	public Dialog info(String text) {
		pages.add(new InformationPage(text));
		return this;
	}

	/**
	 * Removes the last {@code Page} queued to this {@code Dialog} if the specified {@code predicate}
	 * does not return true in its {@link Predicate#test(Object)} function.
	 * 
	 * @param predicate
	 *            the predicate to test
	 * @return a chain of this instance
	 */
	public Dialog when(Predicate<DialogTransactor> predicate) {
		if (!predicate.test(transactor)) {
			breakHere = !breakHere;
		}
		return this;
	}

	public Dialog or(Predicate<DialogTransactor> predicate) {
		if (!predicate.test(transactor)) {
			pages.pollLast();
			breakHere = false;
		}
		return this;
	}

	/**
	 * Writes an action to the last {@code Page} written to this {@code Dialog}.
	 * 
	 * @param consumer
	 *            the action to execute when the {@code Page} is opened.
	 * @return a chain of this instance
	 */
	public Dialog then(Consumer<DialogTransactor> consumer) {
		if (!breakHere) {
			pages.peekLast().action = () -> consumer.accept(transactor);
		} else breakHere = false;
		return this;
	}

	/**
	 * Writes multiple actions to the last {@code Page} written to this {@code Dialog}.
	 * 
	 * @param runnables
	 *            the actions to execute when the {@code Page} is opened.
	 * @return a chain of this instance
	 */
	public Dialog then(Runnable... runnables) {
		if (!breakHere) {
			pages.peekLast().action = () -> {
				for (Runnable r : runnables)
					r.run();
			};
		} else breakHere = false;
		return this;
	}

	/**
	 * Writes a {@code Page} to this {@code Dialog} that is meant to be written for a player to speak.
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
	 * @return a chain of this instance
	 */
	public Dialog player(String text) {
		return this.player(CALM_TALK, text);
	}

	/**
	 * Writes a {@code Page} to this {@code Dialog} that is meant to be written for a player to speak.
	 * 
	 * @param expression
	 *            the expression id value of the player speaking
	 * @param name
	 *            the name that is spoken
	 * @return a chain of this instance
	 */
	public Dialog player(int expression, String text) {
		Page page = new Page(text);
		page.npcId = -1;
		page.expression = expression;
		pages.add(page);
		return this;
	}

	/**
	 * Selects the entityOption from an {@code OptionPage} and executes the action within the
	 * entityOption.
	 * 
	 * @param index
	 *            the index of the entityOption to select
	 * 
	 * @throws UnsupportedOperationException
	 *             if an {@code OptionPage} is not the current page or if the specified index of the
	 *             entityOption is not available
	 * @return a chain of this instance
	 */
	public Dialog selectOption(int index) {
		Page last = pages.poll();
		if (!(last instanceof OptionPage))
			throw new UnsupportedOperationException("The page selected is not an OptionPage");
		OptionPage page = (OptionPage) last;
		if (index >= page.options.length)
			throw new UnsupportedOperationException("The Option on the page does not exist: " + index);
		page.options[index].action.run();
		if (pages.isEmpty()) {
			finish();
			transactor.exitDialog();
			return null;
		}
		return this;
	}

	/**
	 * Starts this {@code Dialog}.
	 */
	public void start() {
		transactor.initializeDialog(this);
	}

	/**
	 * Continues the dialog to the next page.
	 * 
	 * <p>
	 * The {@code Page} returned is the next page within this {@code Dialog}, if existing, If there is
	 * not another page, the {@link DialogTransactor#exitDialog()} method is called and the dialog is
	 * stopped and null is returned.
	 * 
	 * @return the next page; return null otherwise
	 */
	public Page continueDialog() {
		if (pages.isEmpty()) {
			finish();
			transactor.exitDialog();
			return null;
		}
		Page page = pages.poll();
		this.currentPage = page;
		if (page instanceof InformationPage) {
			transactor.displayInformation((InformationPage) page);
		} else if (page instanceof OptionPage) {
			transactor.displayOptions((OptionPage) page);
		} else {
			transactor.displayDialogPage(page);
		}
		if (page.action != null)
			page.action.run();
		return page;
	}

	/**
	 * Finalizes anything with this {@code Dialog}.
	 */
	public final void finish() {
		this.pages.clear();
	}

	public Page getCurrentPage() {
		return this.currentPage;
	}

}
