package plugins;

public abstract class Plugin {
	
	/**
	 * This method is called after the server itself has initialized, so use this for most of your changes.
	 */
	public abstract void init();
	
	/**
	 * Post Initialization. This happens after every other plugin has initialized. If you are checking for other plugins, this is the ideal place to do so.
	 */
	public abstract void postInit();
	
	public final PluginInfo getPluginInfo() {
		PluginInfo info = getClass().getAnnotation(PluginInfo.class);
		if(info == null) {
			System.err.print(String.format("%s doesn't contain the @%s annotation", getClass().getName(), PluginInfo.class.getSimpleName()));
		}
		return info;
	}
	
	@Override
	public final String toString() {
		PluginInfo info = getPluginInfo();
		return String.format("[name=%s, author=%s, version=%s]", info.name(), info.author(), info.version());
	}
}
