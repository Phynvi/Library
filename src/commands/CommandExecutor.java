package commands;

public interface CommandExecutor {
	
	public void execute(Object player, String commandName, String[] args);
	
	public default Command getCommand() {
		return getClass().getAnnotation(Command.class);
	}
}