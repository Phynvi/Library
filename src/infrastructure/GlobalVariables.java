package infrastructure;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import entity.actor.model.ModelUpdater;
import event.EventManager;
import infrastructure.threads.TickThread;
import io.netty.util.AttributeKey;
import network.World;

/**
 * The {@code GlobalVariables} class is meant to hold methods that will be used throughout the
 * library.
 * 
 * @author Albert Beaupre
 */
public class GlobalVariables {

	public static final AttributeKey<World> WORLD_KEY = AttributeKey.valueOf("world");
	public static final AttributeKey<int[]> ISAAC_KEYS_IN = AttributeKey.valueOf("isaac_keys_in");
	public static final AttributeKey<int[]> ISAAC_KEYS_OUT = AttributeKey.valueOf("isaac_keys_out");

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final HashMap<String, Object> CONSTANTS = new HashMap<>();

	private static TickThread TICKER;
	private static EventManager EVENT_MANAGER;
	private static ModelUpdater MODEL_UPDATER;

	private static boolean DEBUGGING = true;

	private static boolean isaacEnabled;

	/**
	 * Sets the specified {@code ticker} to this {@code GlobalVariables} class.
	 * 
	 * @param tickThread
	 *            the ticker to set
	 */
	public static void setTicker(TickThread tickThread) {
		if (GlobalVariables.TICKER != null) {
			LOGGER.warning("A TickThread has already been set");
			return;
		}
		GlobalVariables.TICKER = Objects.requireNonNull(tickThread, "The TickThread cannot be set as NULL");
		Core.scheduleFixedTask(TICKER, 0, 1, TimeUnit.MILLISECONDS);
		LOGGER.info("A TickThread has successfully been set");
	}

	/**
	 * Returns the {@code TickThread} that has been set to this {@code GlobalVariables} class that will
	 * globally handle Ticks.
	 * 
	 * @throws NullPointerException
	 *             if the {@code TickThread} of this class is {@code null}
	 * 
	 * @return the {@code TickThread} that has been set
	 */
	public static TickThread getTicker() {
		if (GlobalVariables.TICKER == null)
			throw new NullPointerException("There is not a TickThread set");
		return GlobalVariables.TICKER;
	}

	/**
	 * Sets the specified {@code eventManager} to this {@code GlobalVariables} class so it may globally
	 * handle any {@code Event}.
	 * 
	 * @param eventManager
	 *            the {@code EventMaanger} to set
	 */
	public static void setEventManager(EventManager eventManager) {
		if (GlobalVariables.EVENT_MANAGER != null) {
			LOGGER.warning("An EventManager has already been set");
			return;
		}
		GlobalVariables.EVENT_MANAGER = Objects.requireNonNull(eventManager, "The EventManager cannot be set as NULL");
		LOGGER.info("An EventManager has successfully been set");
	}

	/**
	 * Returns the {@code EventManager} that has been set to this {@code GlobalVariables} class that
	 * will globally handle any {@code Event}.
	 * 
	 * @throws NullPointerException
	 *             if the {@code EventManager} of this class is {@code null}
	 * 
	 * @return the {@code EventManager} that has been set
	 */
	public static EventManager getEventManager() {
		if (GlobalVariables.EVENT_MANAGER == null)
			throw new NullPointerException("There is not an EventManager set");
		return GlobalVariables.EVENT_MANAGER;
	}

	/**
	 * Sets the specified {@code updater} to this {@code GlobalVariables} class so it may globally
	 * handle any {@code Model} for updating.
	 * 
	 * @param updater
	 *            the {@code ModelUpdater} to set
	 */
	public static void setModelUpdater(ModelUpdater updater) {
		if (GlobalVariables.MODEL_UPDATER != null) {
			LOGGER.warning("A ModelUpdater has already been set");
			return;
		}
		GlobalVariables.MODEL_UPDATER = Objects.requireNonNull(updater, "The ModelUpdater cannot be set as NULL");
		LOGGER.info("A ModelUpdater has successfully been set");

		updater.queue(30);
	}

	/**
	 * Returns the {@code ModelUpdater} that has been set to this {@code GlobalVariables} class that
	 * will globally handle any {@code Model} that is being updated.
	 * 
	 * @throws NullPointerException
	 *             if the {@code ModelUpdater} of this class is {@code null}
	 * 
	 * @return the {@code ModelUpdater} that has been set
	 */
	public static ModelUpdater getModelUpdater() {
		if (GlobalVariables.MODEL_UPDATER == null)
			throw new NullPointerException("There is not a ModelUpdater set");
		return GlobalVariables.MODEL_UPDATER;
	}

	/**
	 * Sets the debugging flag for the server to the given {@code debugging} paramter.
	 * 
	 * @param debugging
	 *            the flag to set
	 */
	public static void setDebuggingEnabled(boolean debugging) {
		GlobalVariables.DEBUGGING = debugging;
	}

	/**
	 * Returns true if debugging is enabled for the server; return false otherwise
	 * 
	 * @return true if debugging is enabled
	 */
	public static boolean isDebugEnabled() {
		return DEBUGGING;
	}

	public static boolean isISAACEnabled() {
		return isaacEnabled;
	}

	public static void setISAACEnabled(boolean isaacEnabled) {
		GlobalVariables.isaacEnabled = isaacEnabled;
	}

	public static void storeConstant(String name, Object value) {
		CONSTANTS.put(name, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getConstant(String name, Class<T> cast) {
		return (T) CONSTANTS.get(name);
	}
}
