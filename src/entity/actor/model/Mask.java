package entity.actor.model;

import network.packet.encoding.EncodedPacket;

/**
 * @author Albert Beaupre
 */
public abstract class Mask implements Comparable<Mask> {

    /**
     * 
     * @return
     */
    public abstract int data();

    /**
     * 
     * @return
     */
    public abstract byte ordinal();

    /**
     * 
     * @return
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
