package cache;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 
 * @author Albert Beaupre
 */
public class CacheStore {

	public static final int INDEX_IDENTIFIER = 6;
	public static final int BLOCK_LENGTH = 520;

	private final int HEADER_LENGTH;
	private final int BLOCK_DATA_LENGTH;

	private final CacheFile metaFile;
	private final CacheFile[] indexFiles;
	private final CacheFile dataFile;

	/**
	 * Constructs a new {@code CacheStore} around the index correlating to the specified {@code index}
	 * within the {@code cache}.
	 * 
	 * @param cache
	 *            the cache to retrieve any information from
	 * @param index
	 *            the index id of the file
	 * @throws IOException
	 *             if there is a problem reading the data or index file within the path
	 */
	public CacheStore(File path) throws IOException {
		dataFile = new CacheFile(path.getPath() + File.separator + "main_file_cache.dat2");
		metaFile = new CacheFile(path.getPath() + File.separator + "main_file_cache.idx255");

		this.HEADER_LENGTH = dataFile.readUnsignedShort() >= 65535 ? 10 : 8;
		this.BLOCK_DATA_LENGTH = BLOCK_LENGTH - HEADER_LENGTH;

		int size = getIndexSize();
		indexFiles = new CacheFile[size];
		for (int index = 0; index < size; index++)
			indexFiles[index] = new CacheFile(path.getPath() + File.separator + "main_file_cache.idx" + index);
	}

	/**
	 * Returns an {@code IoBuffer} object with the information contained in the archive with the
	 * specified {@code id}. If it does not exist, then an {@code IoBuffer} with an initial capacity of
	 * 0 is returned instead.
	 * 
	 * @param idx
	 *            the id of the archive to retrieve the data from
	 * @return an {@code IoBuffer} with the archive data; returns an empty {@code IoBuffer} is non
	 *         existent
	 * @throws IOException
	 */
	public synchronized ByteBuffer getFileData(int idx, int id) throws IOException {
		@SuppressWarnings("resource")
		CacheFile indexFile = idx == 255 ? metaFile : indexFiles[idx];

		if (id * INDEX_IDENTIFIER + INDEX_IDENTIFIER > indexFile.length())
			throw new NullPointerException();

		ByteBuffer temp = ByteBuffer.allocateDirect(BLOCK_LENGTH);
		temp.position(0).limit(INDEX_IDENTIFIER);
		indexFile.getChannel().read(temp, id * INDEX_IDENTIFIER);
		temp.flip();

		int size = ((temp.get() & 0xff) << 16) | ((temp.get() & 0xff) << 8) | (temp.get() & 0xff);
		int block = ((temp.get() & 0xff) << 16) | ((temp.get() & 0xff) << 8) | (temp.get() & 0xff);

		if (size < 0 || size > (idx == 255 ? 500000 : 1000000))
			throw new NullPointerException("Size: " + size);
		if (block < 0 || block > dataFile.getChannel().size() / BLOCK_LENGTH)
			throw new NullPointerException("Block: " + block);
		ByteBuffer data = ByteBuffer.allocate(size);
		int remaining = size, chunk = 0;
		while (remaining > 0) {
			if (block < 1)
				throw new NullPointerException("Block: " + block);

			int availableLength = remaining > BLOCK_DATA_LENGTH ? BLOCK_DATA_LENGTH : remaining;
			ByteBuffer temporary = ByteBuffer.allocate(availableLength + HEADER_LENGTH);
			dataFile.getChannel().read(temporary, block * BLOCK_LENGTH);
			temporary.flip();

			int archiveId = HEADER_LENGTH == 10 ? temporary.getInt() & 0xFFFF : temporary.getShort() & 0xFFFF;
			int currentChunk = temporary.getShort() & 0xFFFF;
			int nextBlock = ((temporary.get() & 0xFF) << 16) | ((temporary.get() & 0xFF) << 8) | (temporary.get() & 0xFF);
			int relativeIndex = temporary.get() & 0xFF;

			if (id != archiveId || chunk != currentChunk || idx != relativeIndex)
				throw new NullPointerException(String.format("IDX=%s, FID=%s, AID=%s, C=%s, B=%s, RI=%s", idx, id, archiveId, currentChunk, nextBlock, relativeIndex));
			if (nextBlock < 0 || nextBlock > dataFile.getChannel().size() / BLOCK_LENGTH)
				throw new NullPointerException(String.format("Next Block: " + nextBlock));

			data.put(temporary);
			remaining -= availableLength;
			block = nextBlock;
			chunk++;
		}
		data.flip();
		return data;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public int getIndexSize() throws IOException {
		return (int) (metaFile.length() / INDEX_IDENTIFIER);
	}

	public int getHeaderLength() {
		return HEADER_LENGTH;
	}

	public int getBlockDataLength() {
		return BLOCK_DATA_LENGTH;
	}
}