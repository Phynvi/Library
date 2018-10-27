package cache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import cache.openrs.Archive;
import infrastructure.Core;

/**
 * @author Albert Beaupre
 */
public enum IDX {
	SKELETONS(0),
	SKINS(1),
	GRAPHIC_ACCESSORIES(2),
	INTERFACES(3),
	SOUND_EFFECTS(4),
	LANDSCAPES(5),
	MUSIC(6),
	MODELS(7),
	SPRITES(8),
	TEXTURES(9),
	HUFFMAN(10),
	MUSIC_2(11),
	INTERFACE_SCRIPTS(12),
	FONTS(13),
	SOUND_EFFECTS_2(14),
	SOUND_EFFECTS_3(15),
	OBJECTS(16),
	CLIENTSCRIPT_SETTINGS(17),
	NPCS(18),
	ITEMS(19),
	ANIMATIONS(20),
	GRAPHICS(21),
	CONFIGURATION(22),
	WORLD_MAP(23),
	THEORA(35),
	VORBIS(36);

	private final int archiveIndex;

	private IDX(int archiveIndex) {
		this.archiveIndex = archiveIndex;
	}

	public int getArchiveIndex() {
		return this.archiveIndex;
	}

	/**
	 * Fetches the ID of the file in the given IDX with the given name hash
	 * 
	 * @param identifier
	 *            the name of the file (case insensitive), this is hashed
	 * @return the file id
	 * @throws FileNotFoundException
	 *             if the file was not found.
	 */
	public int getFileId(String identifier) {
		try {
			return Core.getCache().getFileId(archiveIndex, identifier);
		} catch (Exception e) {
			System.err.printf("Cache FileID not found for Identifier [%s]: %s\n", identifier, e.getMessage());
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Fetches the requested CacheFile. This file is cached so that it does not have to be parsed
	 * multiple times.
	 * 
	 * @param identifier
	 *            the name of the file (case insensitive), this is hashed
	 * @return the cache file, not null.
	 * @throws IOException
	 *             if an IO error occurs
	 * @throws FileNotFoundException
	 *             if the file was not found.
	 */
	public CacheFile getFile(String identifier, XTEAKey key) {
		try {
			return Core.getCache().getFile(archiveIndex, getFileId(identifier), key);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.printf("Cache File [%s] not found: %s\n", getFileId(identifier), e.getMessage());
		}
		return null;
	}

	/**
	 * Fetches the requested CacheFile. This file is cached so that it does not have to be parsed
	 * multiple times.
	 * 
	 * @param fileId
	 *            the fileId to fetch
	 * @return the cache file, not null.
	 * @throws IOException
	 *             if an IO error occurs
	 * @throws FileNotFoundException
	 *             if the file was not found.
	 */
	public CacheFile getFile(int fileId, XTEAKey key) {
		try {
			return Core.getCache().getFile(archiveIndex, fileId, key);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.printf("Cache File [%s] not found: %s\n", fileId, e.getMessage());
		}
		return null;
	}

	/**
	 * Fetches the archive by a given IDX and fileId value. This method caches the archives, which are
	 * immutable.
	 * 
	 * @param fileId
	 *            the file id
	 * @return the archive
	 * @throws IOException
	 *             if the archive could not be retrieved. Not thrown if the archive is already cached.
	 */
	public Archive getArchive(int fileId) {
		try {
			return Core.getCache().getArchive(archiveIndex, fileId);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.printf("Cache Archive [%s] not found: %s\n", fileId, e.getMessage());
		}
		return null;
	}

	/**
	 * Gets the subfile by ID of the Archive for the IDX.
	 * 
	 * @param id
	 *            the index of the archive
	 * @param subFileId
	 *            the id of the sub file for the archive file
	 * @return the subfile of the archive
	 */
	public ByteBuffer getMember(int id, int subFileId) {
		return getArchive(id).get(subFileId);
	}

}