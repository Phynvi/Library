package cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * 
 * @author Albert Beaupre
 */
public class CacheFile extends RandomAccessFile {

	private CompressionType compression;
	private ByteBuffer payload;
	private int version;

	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public CacheFile(File file) throws FileNotFoundException {
		super(file, "r");
	}

	/**
	 * 
	 * @param name
	 * @throws FileNotFoundException
	 */
	public CacheFile(String name) throws FileNotFoundException {
		super(name, "r");
	}

	public static CacheFile decode(File file, int idx, ByteBuffer payload, int fileId) throws IOException {
		CacheFile f = new CacheFile(file.getPath() + File.separator + "./cache/main_file_cache.idx" + idx);
		int type = payload.get() & 0xFF;
		f.compression = CompressionType.forId(type);
		int length = payload.getInt(1); /* Length of compressed data */
		int limit = payload.limit(); /* Preserve current limit */
		payload.limit(payload.position() + length + (f.compression == CompressionType.NONE ? 0 : 4)); /* Only read length bytes, the new limit is <= the old limit */

		try {
			f.payload = f.compression.decode(payload);
		} catch (IOException e) {
			throw e;
		}

		payload.limit(limit);

		/* The version is the last two bytes */
		if (payload.remaining() >= 2) {
			f.version = payload.getShort();
		} else {
			f.version = -1; /* No version attached */
		}

		if (f.payload.position() != 0)
			System.err.println("Failed to decode properly, position() != 0, compression " + f.compression + ", version: " + f.version + ", pos: " + f.payload.position() + " / lim: " + f.payload.limit());
		return f;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public int readMedium() throws IOException {
		return (this.readByte() << 16) | (this.readByte() << 8) | this.readByte();
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public int readUnsignedMedium() throws IOException {
		return (this.readUnsignedByte() << 16) | (this.readUnsignedByte() << 8) | this.readUnsignedByte();
	}

	public ByteBuffer getData() {
		return payload;
	}

}