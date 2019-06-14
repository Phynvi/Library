package entity.actor.model;

import network.packet.encoding.EncodedPacket;

/**
 * Represents a type of mask, such as movement, facing, graphics, animations, etc placed on an
 * {@code Actor} for updating.
 * 
 * @author Albert Beaupre
 */
public abstract class Mask implements Comparable<Mask> {

	/**
	 * Returns the data value of this {@code Mask}, usually in hexadecimal form.
	 * 
	 * @return the data value of this {@code Mask}
	 */
	public abstract short data();

	/**
	 * Returns the ordinal (index) which this {@code Mask} is specifically read by any {@code Model}.
	 * 
	 * @return the ordinate (index) of this {@code Mask} to be read
	 */
	public abstract byte ordinal();

	/**
	 * Writes information based on this {@code Mask} to the specified {@code packet} argument.
	 */
	public abstract void write(EncodedPacket packet);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Mask o) {
		return Integer.compare(ordinal(), o.ordinal());
	}

}
