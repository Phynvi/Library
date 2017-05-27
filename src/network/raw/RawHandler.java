package network.raw;

import io.netty.buffer.ByteBuf;
import network.Connection;
import network.ConnectionHolder;
import network.packet.decoding.PacketProcessor;
import network.packet.encoding.EncodedPacket;
import network.packet.encoding.PacketSender;

/**
 * The {@code RawHandler} class is used to handle the raw connections between
 * this server and the client. The {@code RawHandler} handles creating a cache
 * information response for the client to receive as well as handling the login
 * information, which is used to begin a player connection and start game-play.
 * 
 * @author Albert Beaupre
 */
@SuppressWarnings("unchecked")
public abstract class RawHandler {

    private PacketProcessor<ConnectionHolder>[] processors;
    private PacketSender<ConnectionHolder>[] senders;

    /**
     * Constructs a new {@code RawHandler}.
     */
    public RawHandler() {
	this.processors = new PacketProcessor[256];
	this.senders = new PacketSender[256];
    }

    /**
     * Registers the specified {@code PacketSender} to this {@code RawHandler}
     * to handle for sending information to the client with the specified
     * {@code opcode} to correlate it to.
     * 
     * @throws NullPointerException
     *             if the sender is null
     * @throws UnsupportedOperationException
     *             if the opcode is less than 0
     * @param processor
     *            the {@code PacketSender} to register
     * @param opcode
     *            the opcode to correlate the {@code PacketSender} to
     */
    public <C extends ConnectionHolder> void registerSender(PacketSender<C> sender, int opcode) {
	if (sender == null)
	    throw new NullPointerException("A PacketProcessor cannot be registered as null");
	if (opcode < 0)
	    throw new UnsupportedOperationException("A PacketProcessor must have an opcode >= 0");
	senders[opcode] = (PacketSender<ConnectionHolder>) sender;
    }

    /**
     * Registers the specified {@code PacketProcessor} to this
     * {@code RawHandler} to handle for processing with the specified
     * {@code opcode} to correlate it to.
     * 
     * @throws NullPointerException
     *             if the processor is null
     * @throws UnsupportedOperationException
     *             if the opcode is less than 0
     * @param processor
     *            the {@code PacketProcessor} to register
     * @param opcode
     *            the opcode to correlate the {@code PacketProcessor} to
     */
    public <C extends ConnectionHolder> void registerProcessor(PacketProcessor<C> processor, int opcode) {
	if (processor == null)
	    throw new NullPointerException("A PacketProcessor cannot be registered as null");
	if (opcode < 0)
	    throw new UnsupportedOperationException("A PacketProcessor must have an opcode >= 0");
	processors[opcode] = (PacketProcessor<ConnectionHolder>) processor;
    }

    /**
     * Retrieves the {@code PacketProcessor} registered within this
     * {@code RawHandler} based on the specified {@code opcode}.
     * 
     * @param opcode
     *            the opcode of the correlating {@code PacketProcessor}
     * @return the {@code PacketProcessor} if existing; return null otherwise
     */
    public PacketProcessor<ConnectionHolder> getPacketProcessor(int opcode) {
	return processors[opcode];
    }

    /**
     * Retrieves the {@code PacketSender} registered within this
     * {@code RawHandler} based on the specified {@code opcode}.
     * 
     * @param opcode
     *            the opcode of the correlating {@code PacketSender}
     * @return the {@code PacketSender} if existing; return null otherwise
     */
    public PacketSender<ConnectionHolder> getPacketSender(int opcode) {
	return senders[opcode];
    }

    public <C extends ConnectionHolder> EncodedPacket getEncodedPacket(ConnectionHolder holder, int opcode, Object... args) {
	return senders[opcode].send(holder, args);
    }

    /**
     * This method is called before this {@code RawHandler} is used to load any
     * important variables for this {@code RawHandler} to use.
     */
    public abstract void loadRawHandler();

    /**
     * Creates a response to the cache request made by the client. The response
     * <b>must</b> contain the requested information, otherwise the client will
     * not respond correctly.
     * 
     * @param idx
     *            the index of the cache information to be sent
     * @param file
     *            the file containing the information to be sent
     * @param opcode
     *            the priority opcode
     * @return the cache response in bytes wrapped by a {@code ByteBuf}
     */
    public abstract ByteBuf createCacheResponse(int idx, int file, int opcode);

    /**
     * Creates a new {@code ConnectionHolder} object based on the specified
     * arguments. This method is used by the
     * {@link network.raw.LoginRequestDecoder} to create a new
     * {@link network.packet.decoding.IncomingPacketDecoder} based on the
     * returned {@code ConnectionHolder}.
     * 
     * @param connection
     *            the connection trying to log in
     * @param in
     *            the bytes of information to use for creating the
     *            {@code ConnectionHolder}
     * @param state
     *            the state of login
     * @return the new {@code ConnectionHolder} that was created based on the
     *         arguments
     */
    public abstract ConnectionHolder createConnectionHolder(Connection connection, ByteBuf in, int state);

    /**
     * The keys used by the handshake for decryption between the server and
     * client
     * 
     * @return the decryption keys for the handshake
     */
    public abstract int[] getKeys();

    /**
     * Returns the revision that this {@code RawHandler} will be handling.
     * 
     * @return the revision of this {@code RawHandler}
     */
    public abstract int getRevision();

}
