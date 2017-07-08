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

    private static final Option[] default_options = new Option[3];

    private Item parent;
    private Tick updateTick;

    public GroundItem(Item parent, Location location) {
	this.setLocation(location);
	this.parent = parent;
    }

    @Override
    public Option[] options() {
	return default_options;
    }

    @Override
    public String getName() {
	return parent.toString();
    }

    @Override
    public void create() {
	this.updateTick = new Tick() {
	    @Override
	    public void tick() {}
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
