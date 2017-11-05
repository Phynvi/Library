package modules;

/**
 * Represents a type of modular program, which would be connected to a main project but separated in
 * its own project to allow for easy adding and removing of features because it is isolated to its
 * own files, which could just be added or deleted without re-writing code to implement it.
 * 
 * @author Albert Beaupre
 */
public abstract class Module {

	/**
	 * Loads this {@code Module}, which is meant to take first-step preparation for the
	 * {@code Module}, such as registering {@code EventListeners} or {@code Areas}, or any kind of
	 * beginning for systems that are going to be within this {@code Module}.
	 * 
	 * @throws Exception
	 *            if a problem occurs whilst loading this {@code Module}
	 */
	public abstract void load() throws Exception;

	/**
	 * Unloads this {@code Module}, which is meant to unregister or to null objects for better memory
	 * optimization, such as unregistering {@code EventListeners} or {@code Areas}, or any kind of
	 * beginning for systems that had been within this {@code Module}.
	 * 
	 * @throws Exception
	 *            if a problem occurs whilst unloading this {@code Module}
	 */
	public abstract void unload() throws Exception;

}
