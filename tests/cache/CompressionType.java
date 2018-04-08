package cache;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import cache.apache.bzip2.CBZip2InputStream;
import cache.apache.bzip2.GZIPDecompressor;

/**
 * Represents a type of compression used for blocks of data within the cache.
 * 
 * @author Albert Beaupre
 */
public enum CompressionType {
	/**
	 * This compression type is used when the first byte of the block is equal to 1.
	 */
	NONE(0) {
		@Override
		public ByteBuffer decode(ByteBuffer bytes) throws IOException {
			int length = bytes.getInt();
			byte[] data = new byte[length];
			System.arraycopy(bytes.array(), 5, data, 0, data.length);
			return ByteBuffer.wrap(data);
		}

		@Override
		public ByteBuffer encode(ByteBuffer bytes) throws IOException {
			return null;
		}
	},

	/**
	 * The {@code BZIP} compression type is used when the first byte of the block is equal to 1.
	 * 
	 * <p>
	 * This compression type will encode and decode using the BZip2 compressing/decompressing.
	 */
	BZIP(1) {
		@Override
		public ByteBuffer decode(ByteBuffer bytes) throws IOException {
			int length = bytes.getInt();
			byte[] data = new byte[bytes.getInt()];
			CBZip2InputStream zipStream = new CBZip2InputStream(new ByteArrayInputStream(bytes.array(), 9, length), 0);
			DataInputStream stream = new DataInputStream(zipStream);
			stream.readFully(data);
			stream.close();
			return ByteBuffer.wrap(data);
		}

		@Override
		public ByteBuffer encode(ByteBuffer bytes) throws IOException {
			return null;
		}
	},

	/**
	 * The {@code GZIP} compression type is used when the first byte of the block is equal to 2.
	 * 
	 * <p>
	 * This compression type will encode and decode using the GZip compressing/decompressing.
	 */
	GZIP(2) {
		@Override
		public ByteBuffer decode(ByteBuffer bytes) throws IOException {
			int length = bytes.getInt();
			byte[] data = new byte[bytes.getInt()];
			GZIPDecompressor.decompress(length, 9, bytes.array(), data);
			return ByteBuffer.wrap(data);
		}

		@Override
		public ByteBuffer encode(ByteBuffer bytes) throws IOException {
			return null;
		}
	};

	private final int id;

	/**
	 * Constructs a new {@code CompressionType} with a corresponding {@code id} which is the correlating
	 * compression id.
	 * 
	 * @param id
	 *            the correlating compression id
	 */
	private CompressionType(int id) {
		this.id = id;
	}

	/**
	 * Retrieves the {@code CompressionType} with the specified {@code id}; returns null if
	 * non-existent.
	 * 
	 * @param id
	 *            the id of the corresponding {@code CompressionType}
	 * @return the {@code CompressionType} with the corresponding id
	 */
	public static CompressionType forId(int id) {
		for (CompressionType type : values())
			if (type.id == id)
				return type;
		return null;
	}

	/**
	 * Decodes the given {@code bytes} argument using its compression algorithm (if any) and returns the
	 * decoded bytes in a {@code ByteBuffer}.
	 * 
	 * @param bytes
	 *            the bytes to decode
	 * @return the decoded bytes
	 * @throws IOException
	 *             if there is a problem decoding the bytes
	 */
	public abstract ByteBuffer decode(ByteBuffer bytes) throws IOException;

	/**
	 * Encodes the given {@code bytes} argument using its compression algorithm (if any) and returns the
	 * encoded bytes in a {@code ByteBuffer}.
	 * 
	 * @param bytes
	 *            the bytes to encode
	 * @return the encoded bytes
	 * @throws IOException
	 *             if there is a problem encoding the bytes
	 */
	public abstract ByteBuffer encode(ByteBuffer bytes) throws IOException;

}