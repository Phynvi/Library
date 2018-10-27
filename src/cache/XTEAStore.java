package cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

/**
 * A manage class which allows XTEA keys to be stored for any file in a container from the cache.
 *
 * @author netherfoam
 */
public class XTEAStore {

	/**
	 * This will load any xtea .txt file from the specified {@code folder} and store it into the given
	 * {@code toStore} file.
	 * 
	 * @param folder
	 *            the folder to read the unpacked xtea files
	 * @param toStore
	 *            the file to store the xtea data into
	 * @throws IOException
	 *             if an error occurs reading or storing the data
	 */
	public static XTEAStore loadFormatUnpacked(File folder, File toStore) throws IOException {
		XTEAStore store = new XTEAStore(toStore);
		for (File f : folder.listFiles()) {
			if (!f.getName().endsWith(".txt"))
				continue; //Only pack .txt files

			int regionId = Integer.parseInt(f.getName().substring(0, f.getName().indexOf(".")));

			//This may be the other way around.
			int rx = regionId >> 8;
			int ry = regionId & 0xFF;
			int fileId = IDX.LANDSCAPES.getFileId("l" + rx + "_" + ry);
			Scanner sc = new Scanner(f);

			int[] keys = new int[4];
			for (int i = 0; i < keys.length; i++)
				keys[i] = sc.nextInt();
			sc.close();

			store.setKey(fileId, new XTEAKey(keys));
		}
		store.save();
		return store;
	}

	public static XTEAStore loadPackedFile(File toStore) throws IOException {
		XTEAStore store = new XTEAStore(toStore);
		RandomAccessFile raf = new RandomAccessFile(toStore, "rw");
		ByteBuffer buffer = raf.getChannel().map(MapMode.READ_ONLY, 0, raf.length());
		while (buffer.remaining() > 0) {
			int id = buffer.getShort() & 0xFFFF;
			int[] key = new int[4];
			for (int i2 = 0; i2 < 4; i2++)
				key[i2] = buffer.getInt();
			store.setKey(id, new XTEAKey(key));
		}
		raf.close();
		return store;
	}

	/**
	 * The size of an XTEA int[] array. This may not vary.
	 */
	public static final int XTEA_KEY_LENGTH = 4;

	/**
	 * A hashmap of (IDX << 24) | (fileId) to XTEA Key for that file
	 */
	private HashMap<Integer, XTEAKey> keys = new HashMap<>(1024);

	/**
	 * The file this was loaded from
	 */
	private final File file;

	/**
	 * Constructs a new {@code XTEAStore} with the given {@code file} to handle the data from.
	 * 
	 * @param file
	 *            the file containing the data
	 */
	public XTEAStore(File file) {
		this.file = file;
	}

	/**
	 * Loads this XTEA Store from the given file. This does not overwrite existing values. specified in
	 * the given file.
	 *
	 * @param f
	 *            the file to load from
	 * @throws IOException
	 *             if there was an IO error
	 * @throws FileNotFoundException
	 *             if the file could not be found
	 */
	public void load() throws IOException {
		// Throws FileNotFound
		DataInputStream in = new DataInputStream(new FileInputStream(file));

		while (in.available() > 0) {
			in.readByte();
			int fileId = ((in.readByte() & 0xFF) << 16) | ((in.readByte() & 0xFF) << 8) | (in.readByte() & 0xFF);

			int[] v = new int[XTEA_KEY_LENGTH];
			for (int i = 0; i < v.length; i++)
				v[i] = in.readInt();

			this.setKey(fileId, new XTEAKey(v));
		}
		in.close();
	}

	/**
	 * Saves all loaded XTEAs in this store to the given file. If the file doesn't exist, this method
	 * creates any relevant directories and then creates the file.
	 *
	 * @param f
	 *            the file to save to
	 * @throws IOException
	 *             if there was an IO error
	 */
	public void save() throws IOException {
		if (!file.exists()) {
			if (file.getParentFile() != null)
				file.getParentFile().mkdirs();
			file.createNewFile();
		}

		DataOutputStream out = new DataOutputStream(new FileOutputStream(file));

		for (Entry<Integer, XTEAKey> e : keys.entrySet()) {
			out.writeByte(e.getKey() >> 24); //IDX
			out.writeByte(e.getKey() >> 16); //FileId
			out.writeByte(e.getKey() >> 8); //FileId
			out.writeByte(e.getKey()); //FileId

			XTEAKey value = e.getValue();
			int[] data = value.getKeys();

			//The next 4 integers are the key.
			for (int i = 0; i < XTEA_KEY_LENGTH; i++)
				out.writeInt(data[i]);
		}
		out.close();
	}

	/**
	 * Returns the number of keys in this store
	 *
	 * @return the number of keys in this store
	 */
	public int size() {
		return keys.size();
	}

	/**
	 * Fetches the xtea value for the given hash value
	 *
	 * @param idx
	 *            the type
	 * @param fileId
	 *            the file number
	 * @return the key.
	 */
	public XTEAKey getKey(int fileId) {
		return keys.get(fileId);
	}

	public Set<Entry<Integer, XTEAKey>> getKeys() {
		return this.keys.entrySet();
	}

	/**
	 * Sets the key for the given idx and container to the given value.
	 *
	 * @param idx
	 *            the idx for the section
	 * @param fileId
	 *            the file id
	 * @param key
	 *            the keys to set, possibly null
	 * @throws IllegalArgumentException
	 *             if the keys length is not 4
	 */
	public void setKey(int fileId, XTEAKey key) {
		if (fileId < 0)
			throw new IllegalArgumentException();

		if (key == null) {
			keys.remove(fileId);
		} else {
			//No need to validate key, XTEAKey class does this for us.
			keys.put(fileId, key);
		}
	}
}