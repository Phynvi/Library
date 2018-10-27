package cache;

import java.io.IOException;
import java.nio.ByteBuffer;

import cache.compression.CompressionType;

/**
 * 
 * @author Albert Beaupre
 */
public class CacheFile {

	private CompressionType compression;
	private ByteBuffer payload;
	private int version;

	public static CacheFile decode(CacheStore store, int idx, int fileId, XTEAKey key) throws IOException {
		CacheFile f = new CacheFile();
		ByteBuffer payload = store.getFileData(idx, fileId);
		f.compression = CompressionType.forId(payload.get() & 0xFF);
		int length = payload.getInt(); /* Length of compressed data */
		int limit = payload.limit(); /* Preserve current limit */

		payload.limit(payload.position() + length + (f.compression == CompressionType.NONE ? 0 : 4)); /* Only read length bytes, the new limit is <= the old limit */

		try {
			f.payload = f.compression.decode(payload, key);
		} catch (IOException e) {
			f.payload = payload;
			return f;
		}

		payload.limit(limit);

		f.version = payload.remaining() >= 2 ? payload.getShort() : -1;

		if (f.payload.position() != 0)
			System.out.println("Failed to decode properly, position() != 0, compression " + f.compression + ", version: " + f.version + ", pos: " + f.payload.position() + " / lim: " + f.payload.limit());
		return f;
	}

	public ByteBuffer getData() {
		return payload.duplicate();
	}

	@Override
	public String toString() {
		return String.format("CacheFile[compression=%s, version=%s, payload=%s]", compression, version, payload);
	}
}