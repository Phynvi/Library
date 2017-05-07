package modules;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import yaml.ConfigSection;

/**
 * A utility class that helps to load {@code Module} types in different ways.
 * 
 * @author Albert Beaupre
 */
public class ModuleLoader {

    /**
     * Retrieves a list of jar files from the specified {@code folder} and
     * checks if they can be loaded as a {@code Module}. If so, they are placed
     * within a {@code Collection}. A {@code Collection} of the modules loaded
     * are then returned once the folder has been searched.
     * 
     * @param folder
     *            the folder to search for modules
     * @return a {@code Collection} of {@code Module} loaded from the
     *         {@code folder}
     */
    public Collection<Module> getModulesFromFolder(File folder) {
	List<Module> modules = new ArrayList<>();
	if (!folder.exists()) {
	    if (!folder.mkdirs()) {
		System.err.println("Error creating extension folder.");
		return Collections.emptyList();
	    }
	}
	File[] extensionFileList = folder.listFiles((f, name) -> name.endsWith(".jar"));
	for (File extensionFile : extensionFileList) {
	    try {
		modules.add(getModuleFromFile(extensionFile));
		System.out.println("Successfully loaded extension from file: " + extensionFile.getName() + ".");
	    } catch (Exception e) {
		System.err.println("Failed to load extension: " + extensionFile.getName() + ".");
		e.printStackTrace();
		return Collections.emptyList();
	    }
	}
	return modules;
    }

    /**
     * Checks the information within the specified {@code file} to see if it can
     * be loaded as a {@code Module}, and if it can't, then {@link IOException}
     * will be thrown. If the {@code file} can be loaded as a {@code Module},
     * then it will parse the file and return the parsed {@code Module}.
     * 
     * @param file
     *            the file to load the {@code Module} from
     * @return the {@code Module} loaded, if successful; otherwise returns null
     * @throws Exception
     *             if an error occurs at any state of loading the module
     */
    public Module getModuleFromFile(File file) throws Exception {
	URLClassLoader classLoader = new URLClassLoader(new URL[] { file.toURI().toURL() }, ClassLoader.getSystemClassLoader());
	JarFile extensionJar = new JarFile(file);
	ZipEntry zipEntry = extensionJar.getEntry("extension.yml");
	if (zipEntry == null) {
	    classLoader.close();
	    extensionJar.close();
	    throw new IOException(file.getName() + " does not contain a extension.yml configuration file in the root directory!");
	}

	ConfigSection extensionConfig = new ConfigSection(extensionJar.getInputStream(zipEntry));
	String name = extensionConfig.getString("name");
	if (name == null) {
	    classLoader.close();
	    extensionJar.close();
	    throw new IOException("extension.yml configuration file must contain a 'name' field to load...");
	}
	String className = extensionConfig.getString("class");
	if (className == null) {
	    classLoader.close();
	    extensionJar.close();
	    throw new IOException("extension.yml configuration file must contain a 'class' field to load...");
	}

	Class<?> clazz = classLoader.loadClass(className);
	Class<?> parent = clazz.getSuperclass();
	while (parent != null && parent != Module.class)
	    parent = parent.getSuperclass();

	if (parent != Module.class) {
	    classLoader.close();
	    extensionJar.close();
	    throw new IOException("'class' field specified in extension.yml configuration file is not an ExtensionModule");
	}
	Module module;
	try {
	    module = (Module) clazz.newInstance();
	} catch (Exception e) {
	    classLoader.close();
	    extensionJar.close();
	    throw new IOException("The ExtensionModule class must not have any arguments in its constructor.");
	}
	classLoader.close();
	extensionJar.close();
	return module;
    }

}
