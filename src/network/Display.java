package network;

import util.yaml.ConfigSection;
import util.yaml.YMLSerializable;

/**
 * @author Albert Beaupre
 */
public class Display implements YMLSerializable {

    private int mode;
    private int width;
    private int height;

    /**
     * Constructs a new {@code Display} from the specified arguments.
     * 
     * @param mode
     *            the display mode
     * @param length
     *            the length of the display
     * @param width
     *            the width of the display
     */
    public Display(int mode, int width, int height) {
	this.mode = mode;
	this.width = width;
	this.height = height;
    }

    /*
     * (non-Javadoc)
     * 
     * @see util.yaml.YMLSerializable#serialize()
     */
    public ConfigSection serialize() {
	ConfigSection config = new ConfigSection();
	config.put("mode", mode);
	config.put("length", width);
	config.put("width", height);
	return config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see util.yaml.YMLSerializable#deserialize(util.yaml.ConfigSection)
     */
    public void deserialize(ConfigSection section) {
	if (section.isEmpty())
	    return;
	this.mode = section.getInt("mode");
	this.width = section.getInt("length");
	this.height = section.getInt("width");
    }

    /**
     * @return the mode
     */
    public int getMode() {
	return mode;
    }

    /**
     * @param mode
     *            the mode to set
     */
    public void setMode(int mode) {
	this.mode = mode;
    }

    /**
     * @return the length
     */
    public int getWidth() {
	return width;
    }

    /**
     * @param length
     *            the length to set
     */
    public void setWidth(int width) {
	this.width = width;
    }

    /**
     * @return the width
     */
    public int getHeight() {
	return height;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setHeight(int height) {
	this.height = height;
    }

}
