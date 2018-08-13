package cache;

public final class CacheUtility {

	/**
	 * Hashes the given string for map entry lookups. Names are case insensitive.
	 * 
	 * @param s
	 *            the string to hash
	 * @return the hash value
	 */
	public static int getNameHash(String s) {
		int count = 0;
		s = s.toLowerCase(); // Client forces all names to be lowercase.
		byte[] characters = s.getBytes();
		for (int i = 0; i < s.length(); i++) {
			count = (characters[i] & 0xff) + ((count << 5) - count);
		}
		return count;
	}
}
