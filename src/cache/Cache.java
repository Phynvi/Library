package cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.apache.commons.collections4.map.MultiKeyMap;

import cache.openrs.util.ByteBufferUtils;
import cache.openrs.util.crypto.Whirlpool;
import cache.reference.Reference;
import cache.reference.ReferenceTable;

/**
 * @author Albert Beaupre
 */
public class Cache {

	private final CacheStore store;
	private final File folder;

	private final HashMap<Integer, Archive> archives;
	private ChecksumTable checksum;

	private ReferenceTable[] reference_tables;
	private CacheFile[] indicies;

	private MultiKeyMap<Integer, CacheFile> file_table;

	/**
	 * Constructs a new {@code Cache}, and initializes the {@code CacheStore} of this {@code Cache}
	 * while being constructed.
	 * 
	 * @param cacheFolder
	 *            the folder the cache files are contained in
	 * @throws IOException
	 *             if there is a problem initializing the {@code CacheStore}.
	 */
	public Cache(File cacheFolder) throws IOException {
		this.folder = cacheFolder;
		this.store = new CacheStore(cacheFolder);
		this.archives = new HashMap<>(store.getBlockSize());
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException {
		int blockSize = store.getBlockSize();

		this.file_table = new MultiKeyMap<>();
		this.indicies = new CacheFile[blockSize];
		this.reference_tables = new ReferenceTable[blockSize];

		for (int index = 0; index < blockSize; index++) {
			ByteBuffer data = store.getFileData(255, index);
			if (data != null && data.remaining() > 0) {
				CacheFile file = CacheFile.decode(folder, 255, data, index);
				ReferenceTable reference = ReferenceTable.decode(index, file);

				this.indicies[index] = file;
				this.reference_tables[index] = reference;
			}
		}

		this.rebuildChecksum();
	}

	/**
	 * 
	 * @throws IOException
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
				System.out.println("Error parsing IDX " + i + " index.");
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
	 * @param fileId
	 * @return
	 * @throws IOException
	 */
	public Archive getArchive(int idx, int fileId) throws IOException {
		int uid = (idx << 24) | (fileId);
		Archive a = archives.get(uid);
		if (a != null)
			return a;

		CacheFile file = getFile(idx, fileId);
		Reference reference = reference_tables[idx].getReference(fileId);

		try {
			a = Archive.decode(reference, file);
		} catch (IOException e) {
			throw new IOException("Failed to decode archive, IDX: " + idx + ", File: " + fileId + (e.getMessage() == null ? "" : ": " + e.getMessage()), e);
		}
		archives.put(uid, a);
		return a;
	}

	/**
	 * 
	 * @param idx
	 * @param fileId
	 * @return
	 * @throws IOException
	 */
	public CacheFile getFile(int idx, int fileId) throws IOException {
		CacheFile file = file_table.get(idx, fileId);
		if (file != null)
			return file;
		file = CacheFile.decode(folder, idx, store.getFileData(idx, fileId), fileId);
		file_table.put(idx, fileId, file);
		return file;
	}

	/**
	 * 
	 * @param idx
	 * @param identifier
	 * @return
	 * @throws FileNotFoundException
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
	 * 
	 * @param idx
	 * @param fileId
	 * @param opcode
	 * @return
	 * @throws IOException
	 */
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
	}
}