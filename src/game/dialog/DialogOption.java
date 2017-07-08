package game.dialog;

import java.util.Objects;

/**
 * The {@code DialogOption} class represents an option to be chosen within a
 * {@link game.dialog.OptionPage}. An action must be set to this
 * {@code DialogOption} so it may be executed upon this {@code DialogOption}
 * being selected.
 * 
 * @author Albert Beaupre
 */
public class DialogOption {

    /**
     * This is the name displayed for the option name
     */
    public final String name;

    /**
     * This is the action taken when the option is selected, which cannot be
     * null. It must either end or continue the dialog in some way.
     */
    public final Runnable action;

    /**
     * Constructs a new {@code DialogOption} with the option name as the
     * specified {@code name} and uses the specified {@code action} to execute
     * when the option is selected.
     * 
     * @param name
     *            the option name
     * @param action
     *            the action taken when the option is selected
     */
    public DialogOption(String name, Runnable action) {
	this.name = name;
	this.action = Objects.requireNonNull(action, "The action of the option cannot equal null");
    }

}
