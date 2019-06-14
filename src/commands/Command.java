package commands;

import entity.Permission;

public @interface Command {
	
	public String[] aliases();
	public String description();
	public Permission permission();
	
}
