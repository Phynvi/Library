package infrastructure;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import timing.Ticker;

/**
 * The {@code Attachments} class is meant to hold a various amount of
 * attachments that help the overall Library.
 * 
 * @author Albert Beaupre
 */
public class Attachments {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static Ticker TICKER; // This is an attached Ticker that will globally handle Ticks

    /**
     * Attaches the specified {@code ticker} to this {@code Attachments} class
     * scheduled to the specified {@code service} to run the {@code ticker}.
     * 
     * @param service
     *            the service to run the ticker
     * @param ticker
     *            the ticker to attach
     */
    public static void attachTicker(ScheduledExecutorService service, Ticker ticker) {
	if (Attachments.TICKER != null) {
	    LOGGER.warning("A Ticker has already been attached");
	    return;
	}
	service.scheduleAtFixedRate(Attachments.TICKER = ticker, 1, 1, TimeUnit.MILLISECONDS);
	LOGGER.info("A Ticker has successfully been attached");
    }

    /**
     * Returns the {@code Ticker} that has been attached to this
     * {@code Attachments} class that will globally handle Ticks.
     * 
     * @throws NullPointerException
     *             if the {@code Ticker} of this class is {@code null}
     * 
     * @return the {@code Ticker} of this {@code Attachments} class
     */
    public static Ticker getTicker() {
	if (TICKER == null)
	    throw new NullPointerException("There is not a ticker attached");
	return TICKER;
    }
}
