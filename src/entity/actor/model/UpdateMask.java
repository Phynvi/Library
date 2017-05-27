package entity.actor.model;

import network.packet.encoding.EncodedPacket;

/**
 * @author Albert Beaupre
 */
public abstract class UpdateMask implements Comparable<UpdateMask> {

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
    public int compareTo(UpdateMask o) {
	return Integer.compare(ordinal(), o.ordinal());
    }

}
