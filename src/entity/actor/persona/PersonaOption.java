package entity.actor.persona;

import entity.interactable.Option;

/**
 * The {@code PersonaOption} interface represents an
 * {@link entity.interactable.Option} that is used on a
 * {@link entity.actor.persona.Persona}.
 * 
 * @author Albert
 * 
 * @see entity.interactable.Option
 * @see entity.actor.persona.Persona
 */
public interface PersonaOption extends Option {

    /**
     * Returns {@code true} if this {@code PersonaOption} is placed on top of
     * other options; return {@code false} otherwise.
     * 
     * @return true if on top of all other options; return false otherwise
     */
    public boolean onTop();
}
