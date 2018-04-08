package network.raw.handshake;

/**
 * Represents a type of request used by the {@link network.raw.handshake.HandshakeDecoder} to decide
 * what entity.actor.action to take next within decoding the beginning of the server.
 * 
 * @author Albert Beaupre
 */
public enum HandshakeRequest {

	/**
	 * This type of request is sent when a holder is trying to log into the client.
	 */
	LOGIN(14),

	/**
	 * This type of request requires the server to send update information to the client.
	 */
	UPDATE(15);

	private final int requestId;

	/**
	 * Constructs a new {@code HandshakeRequest} from the specified {@code requestId}.
	 * 
	 * @param requestId
	 *            the id of this request
	 */
	private HandshakeRequest(int requestId) {
		this.requestId = requestId;
	}

	/**
	 * Returns the id given to this {@code HandshakeRequest}.
	 * 
	 * @return the id of this {@code HandshakeRequest}
	 */
	public int getRequestId() {
		return requestId;
	}

	/**
	 * Finds the specific {@code HandshakeRequest} for the specified {@code id}, and will return
	 * {@code null} if it does not exist.
	 * 
	 * @param id
	 *            the id of the request to retrieve
	 * @return the retrieved {@code HandshakeRequest} if existing; return {@code null} otherwise
	 */
	public static final HandshakeRequest forId(int id) {
		for (HandshakeRequest r : values())
			if (r.getRequestId() == id)
				return r;
		return null;
	}
}
