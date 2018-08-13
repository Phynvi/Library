package cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

import cache.reference.Reference;

public class Archive {
	/**
	 * Decodes the given cache file into an archive.
	 * 
	 * @param ref
	 *            the reference for the file
	 * @param file
	 *            the file to decode
	 * @return the archive
	 * @throws IOException
	 */
	public static Archive decode(Reference ref, CacheFile file) throws IOException {
		Archive a = new Archive(ref.getChildCount());

		try {
			int size = ref.getChildCount();

			ByteBuffer bb = file.getData();

			bb.position(bb.limit() - 1);
			int chunks = bb.get() & 0xFF;

			// read the sizes of the child entries and individual chunks
			int[][] chunkSizes = new int[chunks][size];
			int[] sizes = new int[size];
			bb.position(bb.limit() - 1 - chunks * size * 4);
			for (int chunk = 0; chunk < chunks; chunk++) {
				int chunkSize = 0;
				for (int id = 0; id < size; id++) {
					// read the delta-encoded chunk length
					int delta = bb.getInt();
					chunkSize += delta;

					chunkSizes[chunk][id] = chunkSize; // store the size of this chunk
					sizes[id] += chunkSize; // and add it to the size of the whole file
				}
			}

			ByteBuffer[] entries = new ByteBuffer[size];

			// allocate the buffers for the child entries
			for (int id = 0; id < size; id++) {
				if (sizes[id] > 1024 * 1024 * 20) {
					// Size > 20mb
					throw new IOException("Illegal archive subfile size. Archive ID: " + ref.getId() + ", ChildId: " + id + ", Requested Size: " + sizes[id]);
				}
				entries[id] = ByteBuffer.allocate(sizes[id]);
			}

			// read the data into the buffers
			bb.position(0);
			for (int chunk = 0; chunk < chunks; chunk++) {
				for (int id = 0; id < size; id++) {
					// get the length of this chunk
					int chunkSize = chunkSizes[chunk][id];

					// copy this chunk into a temporary buffer
					byte[] temp = new byte[chunkSize];
					bb.get(temp);

					// copy the temporary buffer into the file buffer
					entries[id].put(temp);
				}
			}

			// flip all of the buffers
			for (int id = 0; id < size; id++) {
				entries[id].flip();
			}

			// correctly disperse the ids
			for (int i = 0; i < size; i++) {
				int index = i;
				if (ref.getChild(i) != null) {
					index = ref.getChild(i).getId();
				}
				// a.entries.put(i, entries[i]);
				a.entries.put(index, entries[i]);
			}
		} catch (Exception e) {
			throw new IOException(e.getMessage(), e);
		}

		return a;
	}

	// private ByteBuffer[] entries;
	private HashMap<Integer, ByteBuffer> entries;

	/**
	 * Constructs a new, blank archive of the given size
	 * 
	 * @param size
	 *            the size of the archive.
	 */
	public Archive(int size) {
		this.entries = new HashMap<Integer, ByteBuffer>(size);
	}

	/**
	 * Gets the subfile by ID
	 * 
	 * @param id
	 *            the index
	 * @return the subfile
	 */
	public ByteBuffer get(int id) {
		ByteBuffer bb = entries.get(id);
		return bb;
	}

	/**
	 * Fetches all bytebuffers from this archive. These are made to be a read-only copy of the array.
	 * There may be null indexes.
	 * 
	 * @return a read only copy of all subfiles
	 */
	public HashMap<Integer, ByteBuffer> getAll() {
		HashMap<Integer, ByteBuffer> buffers = new HashMap<Integer, ByteBuffer>(this.entries.size());
		for (Entry<Integer, ByteBuffer> e : entries.entrySet()) {
			ByteBuffer bb = e.getValue();
			if (bb != null)
				buffers.put(e.getKey(), bb.asReadOnlyBuffer());
		}
		return buffers;
	}

	/**
	 * The number of subfiles
	 * 
	 * @return the number of subfiles
	 */
	public int size() {
		return entries.size();
	}
}