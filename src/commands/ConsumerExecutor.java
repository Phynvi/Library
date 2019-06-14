package commands;

import java.util.function.BiConsumer;

public class ConsumerExecutor implements CommandExecutor {

	public BiConsumer<Object, String[]> consumer;
	
	public ConsumerExecutor(BiConsumer<Object, String[]> consumer) {
		this.consumer = consumer;
	}
	
	@Override
	public void execute(Object player, String commandName, String[] args) {
		this.consumer.accept(player, args);
	}
}
