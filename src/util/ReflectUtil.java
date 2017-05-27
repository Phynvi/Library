package util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * This class holds various methods to help with <b>Java Reflection</b>
 * 
 * @author Albert Beaupre
 */
public class ReflectUtil {

    /**
     * Returns the {@code Class} that called the {@code method} you placed this
     * {@code method} under.
     * 
     * @param level
     *            The level of the caller class. For example: If you are calling
     *            this class inside a method and you want to get the caller
     *            class of that method, you would use level 2. If you want the
     *            caller of that class, you would use level 3. Usually level 2
     *            is the one you want.
     * 
     * @return the class that called the method
     * @throws ClassNotFoundException
     *             When the caller class was not found as a java class
     */
    public static Class<?> getCallerClass(int level) throws ClassNotFoundException {
	StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
	String rawFQN = stElements[level + 1].toString().split("\\(")[0];
	return Class.forName(rawFQN.substring(0, rawFQN.lastIndexOf('.')));
    }

    /**
     * Returns a {@code String} describing every accessible field within the
     * specified {@code Object}.
     * 
     * @param o
     *            the object to get the description of
     * @return the description of the object's fields
     */
    public static String describe(Object o) {
	StringBuilder result = new StringBuilder();
	String newLine = System.getProperty("line.separator");

	result.append(o.getClass().getName());
	result.append(" {");
	result.append(newLine);

	//determine fields declared in this class only (no fields of superclass)
	Field[] fields = o.getClass().getDeclaredFields();

	//print field names paired with their values
	for (Field field : fields) {
	    if ((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC)
		continue; //Field is static

	    boolean access = field.isAccessible();
	    if (!access)
		field.setAccessible(true);

	    result.append("  ");
	    try {
		result.append(field.getName());
		result.append(": ");
		//requires access to private field: 
		Object v = field.get(o);

		if (v instanceof Object[]) {
		    result.append(Arrays.toString((Object[]) v));
		} else if (v instanceof boolean[]) {
		    result.append(Arrays.toString((boolean[]) v));
		} else if (v instanceof char[]) {
		    result.append(Arrays.toString((char[]) v));
		} else if (v instanceof byte[]) {
		    result.append(Arrays.toString((byte[]) v));
		} else if (v instanceof short[]) {
		    result.append(Arrays.toString((short[]) v));
		} else if (v instanceof int[]) {
		    result.append(Arrays.toString((int[]) v));
		} else if (v instanceof long[]) {
		    result.append(Arrays.toString((long[]) v));
		} else if (v instanceof double[]) {
		    result.append(Arrays.toString((double[]) v));
		} else if (v instanceof float[]) {
		    result.append(Arrays.toString((float[]) v));
		} else {
		    result.append(field.get(o));
		}
	    } catch (IllegalAccessException ex) {
		System.out.println(ex);
	    }
	    result.append(newLine);
	    if (!access)
		field.setAccessible(false);
	}
	result.append("}");

	return result.toString();
    }
}