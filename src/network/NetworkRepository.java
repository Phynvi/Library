package network;

import java.util.HashMap;
import network.raw.RawHandler;

/**
 * This class holds important, static information for the Network of this Library. It is important
 * to register a {@code RawHandler} that will handle the revision type of a server.
 * 
 * @author Albert Beaupre
 * 
 * @see network.raw.RawHandler
 */
public final class NetworkRepository {

	/**
	 * Inaccessible.
	 */
	private NetworkRepository() {

	}

	private static final HashMap<Integer, RawHandler> rawHandlers = new HashMap<>();

	/**
	 * Registers the specified {@code RawHandler} to this {@code NetworkRepository} for static access to
	 * the specified {@code handler}.
	 * 
	 * @throws NullPointerException
	 *             if the {@code RawHandler} is null
	 * @param handler
	 *            the handler to register
	 */
	public static void registerRawHandler(RawHandler handler) {
		if (handler == null)
			throw new NullPointerException("A RawHandler cannot be registered as null");
		NetworkRepository.rawHandlers.put(handler.getRevision(), handler);
	}

	/**
	 * Returns the static {@code RawHandler} registered within this {@code NetworkRepository} based on
	 * the specified {@code revision}, or null if it does not exist.
	 * 
	 * @param revision
	 *            the revision of the {@code RawHandler}
	 * @return the raw handler based on the revision; or null if it doesn't exist
	 */
	public static RawHandler getRawHandler(int revision) {
		return rawHandlers.get(revision);
	}
}
