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
 * The {@code Attachments} class is meant to hold a various amount of attachments that help the
 * overall Library.
 * 
 * @author Albert Beaupre
 */
public class GlobalAttachments {

	public static AttributeKey<World> WORLD_KEY = AttributeKey.valueOf("world");

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); // This is used for any sort of console logging

	private static TickThread TICKER;
	private static EventManager EVENT_MANAGER;
	private static ActorUpdateThread UPDATE_THREAD;
	private static ItemData ITEM_DATA;

	/**
	 * Attaches the specified {@code ActorUpdateThread} to this {@code Attachments} class.
	 * 
	 * @param updateThread
	 *           the {@code ActorUpdateThread} to attach
	 */
	public static void attachActorUpdator(ActorUpdateThread updateThread) {
		if (GlobalAttachments.UPDATE_THREAD != null) {
			LOGGER.warning("An ActorUpdateThread has already been attached");
			return;
		}
		GlobalAttachments.UPDATE_THREAD = Objects.requireNonNull(updateThread, "The ActorUpdateThread cannot be attached as NULL");
		Core.scheduleFixedTask(UPDATE_THREAD, 0, 30, TimeUnit.MILLISECONDS);
		LOGGER.info("An ActorUpdateThread has successfully been attached");
	}

	/**
	 * Returns the {@code ActorUpdateThread} that has been attached to this {@code Attachments} class
	 * so it may globally handle any updating of an {@code Actor}.
	 * 
	 * @return the {@code ActorUpdateThread} attached to this class
	 */
	public static ActorUpdateThread getActorUpdator() {
		if (GlobalAttachments.UPDATE_THREAD == null)
			throw new NullPointerException("There is not an ActorUpdateThread attached");
		return GlobalAttachments.UPDATE_THREAD;
	}

	/**
	 * Attaches the specified {@code ticker} to this {@code Attachments} class.
	 * 
	 * @param tickThread
	 *           the ticker to attach
	 */
	public static void attachTicker(TickThread tickThread) {
		if (GlobalAttachments.TICKER != null) {
			LOGGER.warning("A TickThread has already been attached");
			return;
		}
		GlobalAttachments.TICKER = Objects.requireNonNull(tickThread, "The TickThread cannot be attached as NULL");
		Core.scheduleFixedTask(TICKER, 0, 1, TimeUnit.MILLISECONDS);
		LOGGER.info("A TickThread has successfully been attached");
	}

	/**
	 * Returns the {@code TickThread} that has been attached to this {@code Attachments} class that
	 * will globally handle Ticks.
	 * 
	 * @throws NullPointerException
	 *            if the {@code TickThread} of this class is {@code null}
	 * 
	 * @return the {@code TickThread} that has been attached
	 */
	public static TickThread getTicker() {
		if (GlobalAttachments.TICKER == null)
			throw new NullPointerException("There is not a TickThread attached");
		return GlobalAttachments.TICKER;
	}

	/**
	 * Attaches the specified {@code eventManager} to this {@code Attachments} class so it may
	 * globally handle any {@code Event}.
	 * 
	 * @param eventManager
	 *           the {@code EventMaanger} to attach
	 */
	public static void attachEventManager(EventManager eventManager) {
		if (GlobalAttachments.EVENT_MANAGER != null) {
			LOGGER.warning("An EventManager has already been attached");
			return;
		}
		GlobalAttachments.EVENT_MANAGER = Objects.requireNonNull(eventManager, "The EventManager cannot be attached as NULL");
		LOGGER.info("An EventManager has successfully been attached");
	}

	/**
	 * Returns the {@code EventManager} that has been attached to this {@code Attachments} class that
	 * will globally handle any {@code Event}.
	 * 
	 * @throws NullPointerException
	 *            if the {@code EventManager} of this class is {@code null}
	 * 
	 * @return the {@code EventManager} that has been attached
	 */
	public static EventManager getEventManager() {
		if (GlobalAttachments.EVENT_MANAGER == null)
			throw new NullPointerException("There is not an EventManager attached");
		return GlobalAttachments.EVENT_MANAGER;
	}

	/**
	 * Attaches the specified {@code data} to this {@code Attachments} class to keep a global field
	 * of item information to be used around the server.
	 * 
	 * @param data
	 *           the information to attach
	 */
	public static void attachItemData(ItemData data) {
		if (GlobalAttachments.ITEM_DATA != null) {
			LOGGER.warning("Item information has already been attached");
			return;
		}
		GlobalAttachments.ITEM_DATA = Objects.requireNonNull(data, "The Item information cannot equal NULL");
		LOGGER.info("Item information has been successfully attached");
	}

	/**
	 * Returns the {@code ItemData} attached to this {@code Attachments} class.
	 * 
	 * @throws NullPointerException
	 *            if the {@code ItemData} has not been attached
	 * 
	 * @return the {@code ItemData} that has been attached
	 */
	public static ItemData getItemData() {
		if (GlobalAttachments.ITEM_DATA == null)
			throw new NullPointerException("There is not any Item information attached");
		return GlobalAttachments.ITEM_DATA;
	}
}
