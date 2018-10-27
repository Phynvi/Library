package cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.logging.Logger;

import cache.openrs.Archive;
import cache.openrs.ByteBufferUtils;
import cache.openrs.ChecksumTable;
import cache.openrs.reference.Reference;
import cache.openrs.reference.ReferenceTable;
import cache.openrs.util.crypto.Whirlpool;

/**
 * @author Albert Beaupre
 */
public class Cache {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private final HashMap<Integer, Archive> archives;
	private final HashMap<Integer, CacheFile> file_table;
	private final CacheStore store;

	private ReferenceTable[] reference_tables;
	private ChecksumTable checksum;

	private int revision;

	/**
	 * Constructs a new {@code Cache}, and initializes the {@code CacheStore} of this {@code Cache}
	 * while being constructed.
	 * 
	 * @param cacheFolder
	 *            the folder the cache files are contained in
	 * @throws IOException
	 *             if there is a problem initializing the {@code CacheStore}.
	 */
	public Cache(File cacheFolder, int revision) throws IOException {
		this.store = new CacheStore(cacheFolder);
		this.archives = new HashMap<>();
		this.file_table = new HashMap<>();
		if (revision <= 0)
			throw new IllegalArgumentException("The cache revision must be > 0 before loading");
		this.revision = revision;
		this.reference_tables = new ReferenceTable[store.getIndexSize()];
	}

	/**
	 * Loads this {@code Cache} by the given {@code revision}.
	 * 
	 * @throws IOException
	 *             when an error occurs loading the cache
	 */
	public void load() throws IOException {
		LOGGER.info("Loading cache...");

		for (int index = 0; index < store.getIndexSize(); index++) {
			ByteBuffer data = store.getFileData(255, index);
			if (data != null && data.remaining() > 0) {
				CacheFile file = CacheFile.decode(store, 255, index, null);
				ReferenceTable reference = ReferenceTable.decode(index, file);

				this.reference_tables[index] = reference;
			}
		}

		this.rebuildChecksum();
		LOGGER.info("Finished loading cache.");
	}

	/**
	 * Rebuilds the checksum for this {@code Cache}.
	 * 
	 * @throws IOException
	 *             When rebuilding fails
	 */
	public void rebuildChecksum() throws IOException {
		this.checksum = new ChecksumTable(this.reference_tables.length);
		for (int i = 0; i < this.reference_tables.length; i++) {
			int crc = 0;
			int version = 0;
			byte[] whirlpool = new byte[64];

			try {
				ByteBuffer raw = store.getFileData(255, i);
				if (raw.limit() <= 0)
					throw new FileNotFoundException();

				ReferenceTable r = this.reference_tables[i];

				crc = ByteBufferUtils.getCrcChecksum(raw);
				version = r.getVersion();
				raw.position(0);
				whirlpool = ByteBufferUtils.getWhirlpoolDigest(raw);
			} catch (FileNotFoundException e) {
				whirlpool = Whirlpool.whirlpool(new byte[0], 0, 0);
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				whirlpool = Whirlpool.whirlpool(new byte[0], 0, 0);
				continue;
			} finally {
				checksum.setEntry(i, new ChecksumTable.Entry(crc, version, whirlpool));
			}
		}
	}

	/**
	 * Gets the {@code Archive} for the given {@code idx} and {@code fileId}.
	 * 
	 * @param idx
	 *            the index of the archive
	 * @param fileId
	 *            the file id of the archive
	 * @return the archive found
	 * @throws IOException
	 *             if the archive could not be found
	 */
	public Archive getArchive(int idx, int fileId) throws IOException {
		int uid = (idx << 24) | (fileId);
		Archive a = archives.get(uid);
		if (a != null)
			return a;

		CacheFile file = getFile(idx, fileId, null);
		Reference reference = reference_tables[idx].getReference(fileId);

		a = Archive.decode(reference, file);
		archives.put(uid, a);
		return a;
	}

	/**
	 * Gets the {@code CacheFile} for the given {@code idx} and {@code fileId}.
	 * 
	 * @param idx
	 *            the index of the file
	 * @param fileId
	 *            the file id of the file
	 * @return the file found
	 * @throws IOException
	 *             If the file could not be found
	 */
	public CacheFile getFile(int idx, int fileId, XTEAKey key) throws IOException {
		int uid = (idx << 24) | (fileId);
		CacheFile file = file_table.get(uid);
		if (file != null)
			return file;
		file = CacheFile.decode(store, idx, fileId, key);
		file_table.put(uid, file);
		return file;
	}

	/**
	 * Returns the id of the file correlating to the {@code idx} and {@code identifier}.
	 * 
	 * @param idx
	 *            the index of the file
	 * @param identifier
	 *            the identifier
	 * @return the file id correlating
	 * @throws FileNotFoundException
	 *             if the file id could not be found
	 */
	public int getFileId(int idx, int identifier) throws FileNotFoundException {
		if (idx >= reference_tables.length || idx < 0)
			throw new FileNotFoundException("IDX invalid " + idx);

		ReferenceTable t = reference_tables[idx];
		for (Reference r : t.getReferences())
			if (r.getIdentifier() == identifier)
				return r.getId();
		throw new FileNotFoundException("IDX " + idx + ", identifier " + identifier);
	}

	/**
	 * Fetches the ID of the file in the given IDX with the given name hash
	 * 
	 * @param idx
	 *            the IDX value, 0-37
	 * @param name
	 *            the name of the file (case insensitive), this is hashed
	 * @return the file id
	 * @throws FileNotFoundException
	 *             if the file was not found.
	 */
	public int getFileId(int idx, String name) throws FileNotFoundException {
		int hash = ByteBufferUtils.getNameHash(name);
		return getFileId(idx, hash);
	}

	/**
	 * Creates a response, which goes to the client.
	 * 
	 * @param idx
	 *            the index type
	 * @param fileId
	 *            the file id
	 * @param opcode
	 *            the opcode
	 * @return the response
	 */
	public ByteBuffer createResponse(int idx, int fileId, int opcode) {
		try {
			ByteBuffer out, raw;
			int length, compression;

			if (idx == 255 && fileId == 255) {
				if (checksum == null)
					rebuildChecksum();
				raw = checksum.encode(true);
				compression = 0;
				length = raw.remaining();
			} else {
				raw = store.getFileData(idx, fileId);
				compression = raw.get() & 0xFF;
				length = raw.getInt();
			}

			out = ByteBuffer.allocate(raw.remaining() + 8 + ((raw.remaining() + 8) / 512) + 4); // Why +4?
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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getRevision() {
		return revision;
	}
}