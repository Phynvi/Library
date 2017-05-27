package network.packet.decoding;

import network.ConnectionHolder;

/**
 * The {@code PacketProcessor} is used to process a {@code DecodedPacket} and
 * take entity.actor.action based on what is decoded.
 * 
 * @author Albert Beaupre
 * 
 * @param <C>
 *            The {@code ConnectionHolder} type
 */
public interface PacketProcessor<C extends ConnectionHolder> {

    /**
     * Processes the specified {@code DecodedPacket} using the specified
     * {@code holder}.
     * 
     * @param holder
     *            the {@code ConnectionHolder} to process the packet
     * @param packet
     *            the {@code DecodedPacket} to process
     */
    public void process(C holder, DecodedPacket packet);

}
