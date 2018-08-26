package network;

import entity.EntityList;
import entity.actor.persona.Persona;

/**
 * 
 * @author Albert Beaupre
 */
public class World {

	public static final int COUNTRY_AUSTRALIA = 16;
	public static final int COUNTRY_BELGIUM = 22;
	public static final int COUNTRY_BRAZIL = 31;
	public static final int COUNTRY_CANADA = 38;
	public static final int COUNTRY_DENMARK = 58;
	public static final int COUNTRY_FINLAND = 69;
	public static final int COUNTRY_IRELAND = 101;
	public static final int COUNTRY_MEXICO = 152;
	public static final int COUNTRY_NETHERLANDS = 161;
	public static final int COUNTRY_NORWAY = 162;
	public static final int COUNTRY_SWEDEN = 191;
	public static final int COUNTRY_UK = 77;
	public static final int COUNTRY_USA = 225;

	public static final int HIGHLIGHT = 16;
	public static final int LOOTSHARE = 8;
	public static final int MEMBERS = 1;
	public static final int NON_MEMBERS = 0;
	public static final int PVP = 4;

	private final int id;
	private final int location;
	private final int country;
	private final int flags;
	private final String name;
	private final String host;
	private final String activity;

	private final EntityList<Persona> players = new EntityList<>(2048);

	public World(int id, String name, String host, String activity, int location, int country, int flags) {
		this.id = id;
		this.location = location;
		this.activity = activity;
		this.flags = flags;
		this.name = name;
		this.country = country;
		this.host = host;
	}

	public int getCountry() {
		return country;
	}

	public int getFlags() {
		return flags;
	}

	public String getName() {
		return name;
	}

	public String getHost() {
		return host;
	}

	public String getActivity() {
		return activity;
	}

	public int getId() {
		return id;
	}

	public int getLocation() {
		return location;
	}

	public EntityList<Persona> getPlayers() {
		return players;
	}

}
