package infrastructure.threads;

import infrastructure.CoreThread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import entity.actor.Actor;

/**
 * This Thread is used for the updating of any {@code Actor}.
 * 
 * @author Albert Beaupre
 */
public class ActorUpdateThread extends CoreThread {

    private HashSet<Actor> updating = new HashSet<>();
    private ArrayList<Actor> queued = new ArrayList<>();

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
		updating.addAll(queued);
		queued.clear();
	    }
	    if (updating.size() > 0) {
		Iterator<Actor> iterator = updating.iterator();
		update: while (iterator.hasNext()) {
		    Actor actor = iterator.next();
		    if (!actor.getModel().canBeUpdated()) {
			iterator.remove();
			continue update;
		    }
		    actor.getModel().update();
		}
		iterator = updating.iterator();
		while (iterator.hasNext()) {
		    Actor actor = iterator.next();
		    actor.getModel().reset();
		    actor.getModel().finishUpdate();
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Allows the specified {@code Actor} to be updating by this
     * {@code ActorUpdateThread}. It will be updating after it is removed from
     * the queue that the {@code Actor} is placed in.
     * 
     * @param actor
     *            the actor to set to update
     */
    public final void setForUpdating(Actor actor) {
	queued.add(Objects.requireNonNull(actor, "An Actor cannot be set for updating if it is null"));
    }

}
