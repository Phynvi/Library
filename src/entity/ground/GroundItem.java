package entity.ground;

import infrastructure.Tick;
import container.Item;
import entity.Entity;
import entity.geometry.Location;
import entity.interactable.Option;

/**
 * 
 * @author Albert Beaupre
 */
public class GroundItem extends Entity {

    private Item parent;
    private Tick updateTick;

    public GroundItem(Item parent, Location location) {
	this.setLocation(location);
	this.parent = parent;
    }

    @Override
    public Option[] options() {
	return null;
    }

    @Override
    public String getName() {
	return null;
    }

    @Override
    public void create() {
	this.updateTick = new Tick() {
	    @Override
	    public void tick() {

	    }
	};
	this.updateTick.queue(600);
    }

    @Override
    public void destroy() {
	this.updateTick.cancel();
	this.updateTick = null;
	this.parent = null;
    }

}
