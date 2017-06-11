package infrastructure.threads;

import infrastructure.CoreThread;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import entity.actor.Actor;

/**
 * This Thread is used for the updating of any {@code Actor}.
 * 
 * @author Albert Beaupre
 */
public class ActorUpdateThread extends CoreThread {

    private HashSet<Actor> updated = new HashSet<>();
    private CopyOnWriteArrayList<Actor> queued = new CopyOnWriteArrayList<>();

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
    public final void run() {
	try {
	    if (queued.size() > 0) {
		updated.addAll(queued);
		queued.clear();
	    }
	    if (updated.size() > 0) {
		Iterator<Actor> iterator = updated.iterator();
		while (iterator.hasNext()) {
		    Actor actor = iterator.next();
		    actor.getModel().update();
		}
		iterator = updated.iterator();
		while (iterator.hasNext()) {
		    Actor actor = iterator.next();
		    actor.getModel().reset();
		    iterator.remove();
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Allows the specified {@code Actor} to be updated by this
     * {@code ActorUpdateThread}. It will be updated after it is removed from
     * the queue that the {@code Actor} is placed in.
     * 
     * @param actor
     *            the actor to set to update
     */
    public final void setForUpdating(Actor actor) {
	queued.add(Objects.requireNonNull(actor, "An Actor cannot be set for updating if it is null"));
    }

}
