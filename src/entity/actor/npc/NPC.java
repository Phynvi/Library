package entity.actor.npc;

import entity.Entity;
import entity.actor.ActionQueue;
import entity.actor.Actor;
import entity.geometry.Location;

public abstract class NPC extends Entity implements Actor {

    @Override
    public Location getLocation() {
	return null;
    }

    @Override
    public ActionQueue<NPC> getActions() {
	return null;
    }

}
