package infrastructure;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import cache.data.ItemData;
import event.EventManager;
import infrastructure.threads.ActorUpdateThread;
import infrastructure.threads.TickThread;
import io.netty.util.AttributeKey;

/**
 * The {@code GlobalVariables} class is meant to hold methods that will be used throughout the
 * library.
 * 
 * @author Albert Beaupre
 */
public class GlobalVariables {

	public static AttributeKey<World> WORLD_KEY = AttributeKey.valueOf("world");

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); // This is used for any sort of console logging

	private static TickThread TICKER;
	private static EventManager EVENT_MANAGER;
	private static ActorUpdateThread UPDATE_THREAD;
	private static ItemData ITEM_DATA;

	/**
	 * setes the specified {@code ActorUpdateThread} to this {@code sets} class.
	 * 
	 * @param updateThread
	 *            the {@code ActorUpdateThread} to set
	 */
	public static void setActorUpdator(ActorUpdateThread updateThread) {
		if (GlobalVariables.UPDATE_THREAD != null) {
			LOGGER.warning("An ActorUpdateThread has already been seted");
			return;
		}
		GlobalVariables.UPDATE_THREAD = Objects.requireNonNull(updateThread, "The ActorUpdateThread cannot be seted as NULL");
		Core.scheduleFixedTask(UPDATE_THREAD, 0, 30, TimeUnit.MILLISECONDS);
		LOGGER.info("An ActorUpdateThread has successfully been seted");
	}

	/**
	 * Returns the {@code ActorUpdateThread} that has been seted to this {@code sets} class so it may
	 * globally handle any updating of an {@code Actor}.
	 * 
	 * @return the {@code ActorUpdateThread} seted to this class
	 */
	public static ActorUpdateThread getActorUpdator() {
		if (GlobalVariables.UPDATE_THREAD == null)
			throw new NullPointerException("There is not an ActorUpdateThread seted");
		return GlobalVariables.UPDATE_THREAD;
	}

	/**
	 * setes the specified {@code ticker} to this {@code sets} class.
	 * 
	 * @param tickThread
	 *            the ticker to set
	 */
	public static void setTicker(TickThread tickThread) {
		if (GlobalVariables.TICKER != null) {
			LOGGER.warning("A TickThread has already been seted");
			return;
		}
		GlobalVariables.TICKER = Objects.requireNonNull(tickThread, "The TickThread cannot be seted as NULL");
		Core.scheduleFixedTask(TICKER, 0, 1, TimeUnit.MILLISECONDS);
		LOGGER.info("A TickThread has successfully been seted");
	}

	/**
	 * Returns the {@code TickThread} that has been seted to this {@code sets} class that will globally
	 * handle Ticks.
	 * 
	 * @throws NullPointerException
	 *             if the {@code TickThread} of this class is {@code null}
	 * 
	 * @return the {@code TickThread} that has been seted
	 */
	public static TickThread getTicker() {
		if (GlobalVariables.TICKER == null)
			throw new NullPointerException("There is not a TickThread seted");
		return GlobalVariables.TICKER;
	}

	/**
	 * setes the specified {@code eventManager} to this {@code sets} class so it may globally handle any
	 * {@code Event}.
	 * 
	 * @param eventManager
	 *            the {@code EventMaanger} to set
	 */
	public static void setEventManager(EventManager eventManager) {
		if (GlobalVariables.EVENT_MANAGER != null) {
			LOGGER.warning("An EventManager has already been seted");
			return;
		}
		GlobalVariables.EVENT_MANAGER = Objects.requireNonNull(eventManager, "The EventManager cannot be seted as NULL");
		LOGGER.info("An EventManager has successfully been seted");
	}

	/**
	 * Returns the {@code EventManager} that has been seted to this {@code sets} class that will
	 * globally handle any {@code Event}.
	 * 
	 * @throws NullPointerException
	 *             if the {@code EventManager} of this class is {@code null}
	 * 
	 * @return the {@code EventManager} that has been seted
	 */
	public static EventManager getEventManager() {
		if (GlobalVariables.EVENT_MANAGER == null)
			throw new NullPointerException("There is not an EventManager seted");
		return GlobalVariables.EVENT_MANAGER;
	}

	/**
	 * setes the specified {@code data} to this {@code sets} class to keep a global field of item
	 * information to be used around the server.
	 * 
	 * @param data
	 *            the information to set
	 */
	public static void setItemData(ItemData data) {
		if (GlobalVariables.ITEM_DATA != null) {
			LOGGER.warning("Item information has already been seted");
			return;
		}
		GlobalVariables.ITEM_DATA = Objects.requireNonNull(data, "The Item information cannot equal NULL");
		LOGGER.info("Item information has been successfully seted");
	}

	/**
	 * Returns the {@code ItemData} seted to this {@code sets} class.
	 * 
	 * @throws NullPointerException
	 *             if the {@code ItemData} has not been seted
	 * 
	 * @return the {@code ItemData} that has been seted
	 */
	public static ItemData getItemData() {
		if (GlobalVariables.ITEM_DATA == null)
			throw new NullPointerException("There is not any Item information seted");
		return GlobalVariables.ITEM_DATA;
	}
}
