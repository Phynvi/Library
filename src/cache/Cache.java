package cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import cache.openrs.ByteBufferUtils;
import cache.openrs.ChecksumTable;
import cache.openrs.crypto.Whirlpool;

/**
 * @author Albert Beaupre
 */
public class Cache {

    private final CacheStore store;

    private final HashMap<Integer, byte[]> archives;
    private ChecksumTable checksum;

    private int[] revisions = new int[37];

    /**
     * Constructs a new {@code Cache}, and initializes the {@code CacheStore} of
     * this {@code Cache} while being constructed.
     * 
     * @param cacheFolder
     *            the folder the cache files are contained in
     * @throws IOException
     *             if there is a problem initializing the {@code CacheStore}.
     */
    public Cache(File cacheFolder) throws IOException {
	this.store = new CacheStore(cacheFolder);
	this.archives = new HashMap<>(37);
    }

    /**
     * 
     * @throws IOException
     */
    public void load() throws IOException {
	int blockSize = store.getBlockSize();
	for (int index = 0; index < blockSize; index++) {
	    ByteBuffer data = store.getArchiveData(255, index);
	    if (data != null) {
		System.out.println("GOOD: " + index);
		archives.put((255 << 24) | index, data.array());
		if (data.remaining() > 0) {
		    int id = data.get();
		    CompressionType compression = CompressionType.forId(id);
		    ByteBuffer decompressed = compression.decode(data);

		    int version = decompressed.get();
		    if (version < 6) {
			revisions[index] = 0;
		    } else {
			revisions[index] = decompressed.getInt();
		    }
		}
	    }
	}
    }

    public void rebuildChecksum() throws IOException {
	this.checksum = new ChecksumTable(store.getBlockSize());
	for (int i = 0; i < store.getBlockSize(); i++) {
	    int crc = 0;
	    int version = 0;
	    byte[] whirlpool = new byte[64];
	    try {
		ByteBuffer raw = getArchiveData(255, i);
		if (raw.limit() <= 0)
		    throw new FileNotFoundException();
		crc = ByteBufferUtils.getCrcChecksum(raw);
		version = revisions[i];
		raw.position(0);
		whirlpool = ByteBufferUtils.getWhirlpoolDigest(raw);
	    } catch (Exception e) {
		whirlpool = Whirlpool.whirlpool(new byte[0], 0, 0);
		continue;
	    } finally {
		checksum.setEntry(i, new ChecksumTable.Entry(crc, version, whirlpool));
	    }
	}
    }

    /**
     * 
     * @param idx
     * @param id
     * @return
     */
    public ByteBuffer getArchiveData(int idx, int id) throws IOException {
	byte[] savedData = archives.get((idx << 24) | id);
	if (savedData != null) {
	    ByteBuffer buffer = ByteBuffer.allocate(savedData.length);
	    buffer.put(savedData);
	    buffer.flip();
	    return buffer;
	}
	return store.getArchiveData(idx, id);
    }

    public ByteBuffer createResponse(int idx, int fileId, int opcode) throws IOException {
	ByteBuffer out, raw;
	int length, compression;

	if (idx == 255 && fileId == 255) {
	    if (checksum == null)
		rebuildChecksum();
	    raw = checksum.encode(true);
	    compression = 0;
	    length = raw.remaining();
	} else {
	    raw = getArchiveData(idx, fileId);
	    compression = raw.get() & 0xFF;
	    length = raw.getInt();
	}
	out = ByteBuffer.allocate(raw.remaining() + 8 + ((raw.remaining() + 8) / 512) + 4);

	int attribs = compression;
	if (opcode == 0)
	    attribs |= 0x80;

	out.put((byte) idx);
	out.putShort((short) fileId);
	out.put((byte) attribs);
	out.putInt(length);

	raw.limit(raw.position() + length + (compression == 0 ? 0 : 4));
	while (raw.remaining() > 0) {
	    if (out.position() % 512 == 0)
		out.put((byte) 0xFF);
	    out.put(raw.get());
	}
	out.flip();
	return out;
    }

    public static void main(String[] args) throws Exception {
	Cache cache = new Cache(new File("./cache/"));
	cache.load();
    }
}
