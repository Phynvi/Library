package plugins;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.jar.JarFile;

public class PluginLoader {

	private HashSet<Plugin> plugins;
	private Path pluginDir;
	private final JarClassLoader classLoader;

	public PluginLoader(Path pluginDir) {
		if(!Files.notExists(pluginDir)) {
			try {
				Files.createDirectories(pluginDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.pluginDir = pluginDir;
		this.classLoader = new JarClassLoader();
		this.plugins = new HashSet<Plugin>();
	}

	public void load() throws IOException {
		Files.walk(pluginDir).filter(Files::isRegularFile).map(Path::toFile).forEach(f -> {
			classLoader.addJar(f);
			try(JarFile jar = new JarFile(f)) {
				jar.stream().forEach(e -> {
					Plugin plugin = isValidPlugin(e.getName());
					if(plugin != null) {
						plugin.init();
						plugins.add(plugin);
					}
				});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		plugins.forEach(Plugin::postInit);
	}

	private Plugin isValidPlugin(final String resourceName) {
		if (resourceName != null && resourceName.endsWith(".class") && !resourceName.contains("$")) {
			String className = resourceName.substring(0, resourceName.length() - 6).replace("/", ".");
			try {
				Class<?> clazz = Class.forName(className, false, classLoader);
				if (clazz != null && Plugin.class.isAssignableFrom(clazz) && clazz != Plugin.class) {
					return (Plugin) clazz.getConstructor().newInstance();
				}
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}