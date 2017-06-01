package infrastructure.threads;

import infrastructure.CoreThread;

/**
 * This Thread is used for the updating of any {@code Actor}.
 * 
 * @author Albert Beaupre
 */
public class ActorUpdateThread extends CoreThread {

    /**
     * Constructs a new {@code ActorUpdateThread}.
     */
    public ActorUpdateThread() {
	super("Actor Update Thread", Thread.MAX_PRIORITY, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see infrastructure.CoreThread#run()
     */
    public void run() {
	
    }

}
