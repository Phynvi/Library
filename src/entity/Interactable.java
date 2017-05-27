package entity;

public interface Interactable {

    public Option<?>[] options();

    public default void addOption(Option<?> option) {
	options()[option.getIndex()] = option;
    }

    public String getName();

}
