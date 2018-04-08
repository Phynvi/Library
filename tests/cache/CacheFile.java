package cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 
 * @author Albert Beaupre
 */
public class CacheFile extends RandomAccessFile {

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
}