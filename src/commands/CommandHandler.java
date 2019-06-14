package commands;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.Permission;

public class CommandHandler {
	
	private static final Pattern PARSE_QUOTES = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
	private final Map<String, CommandExecutor> commands;
	
	public CommandHandler() {
		this.commands = new HashMap<>();
	}
	
	public void registerCommandListener(CommandListener listener) {
		for(Method m : listener.getClass().getMethods()) {
			if(m.isAnnotationPresent(Command.class)) {
				Command command = m.getAnnotation(Command.class);
				MethodExecutor executor = new MethodExecutor(listener, m);
				for(String alias : command.aliases()) {
					commands.put(alias, executor);
				}
			}
		}
	}
	
	public void registerCommand(String commandName, BiConsumer<Object, String[]> consumer) {
		ConsumerExecutor executor = new ConsumerExecutor(consumer);
		commands.put(commandName, executor);
	}
	
	public void execute(Object player, String commandName, String[] args, Permission permission) {
		CommandExecutor executor = commands.get(commandName);
		if(executor.getCommand().permission().hasPermissions(permission)) {
			executor.execute(player, commandName, args);
		}
	}
	
	public void execute(Object player, String command, Permission permission) {
		Matcher m = PARSE_QUOTES.matcher(command);
        String commandName = "";
        List<String> arguments = new ArrayList<String>();
        if(m.find())
            commandName = m.group(1);
        while(m.find())
            arguments.add(m.group(1).replace("\"", ""));
        if(!commandName.isEmpty()) {
        	execute(player, commandName, arguments.toArray(String[]::new), permission);
        }
	}
	
	public HashSet<CommandExecutor> getCommands() {
		HashSet<CommandExecutor> set = new HashSet<>();
		set.addAll(commands.values());
		return set;
	}
}
