package entity;

public enum Permission {
	
	PLAYER, DONATOR, MODERATOR, ADMINISTRATOR;
	
	public boolean hasPermissions(Permission mine) {
		return mine.ordinal() >= ordinal();
	}
}
