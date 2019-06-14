package plugins;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarClassLoader extends URLClassLoader {

	public JarClassLoader() {
		super(new URL[0]);
	}
	
	public void addJar(File jarFile) {
		try {
			if(!jarFile.getName().toLowerCase().endsWith(".jar")) {
				throw new UnsupportedOperationException("file is not a jar");
			}
			super.addURL(jarFile.toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
