package commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodExecutor implements CommandExecutor {

	private final CommandListener listener;
	private final Method method;
	
	public MethodExecutor(CommandListener listener, Method method) {
		this.listener = listener;
		this.method = method;
	}
	
	@Override
	public void execute(Object player, String commandName, String[] args) {
		try {
			method.invoke(listener, commandName, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
