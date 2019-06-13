package event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the method can be used to listen for any relevant event calls.
 * 
 * @author Albert Beaupre
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventMethod {
	public EventPriority priority() default EventPriority.NORMAL;
}
