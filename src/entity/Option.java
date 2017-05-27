package entity;

public interface Option<E extends Interactable> {

    public void select(E interactor, Interactable selected);

    public String name();

    public int getIndex();

}
