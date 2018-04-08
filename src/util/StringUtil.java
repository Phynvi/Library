package util;

import java.text.NumberFormat;

/**
 * 
 * @author Albert Beaupre
 */
public class StringUtil {

	/**
	 * Formats a number to add any commas needed. For example, 1000000 would be converted to 1,000,000.
	 * 
	 * @param number
	 *            the number to format with commas
	 * @return the formatted number with commas
	 */
	public static String formatNumber(double number) {
		return NumberFormat.getInstance().format(number);
	}

	/**
	 * Wraps
	 * 
	 * @param lineWidth
	 * @param wrapWords
	 * @param strings
	 * @return
	 */
	public static String[] wrap(int lineWidth, boolean wrapWords, String... strings) {
		if (wrapWords) {
			StringBuilder builder = new StringBuilder();
			String[] words = String.join(" ", strings).split(" ");
			String currentLine = "";
			for (int i = 0; i < words.length; i++) {
				String w = i < words.length - 1 ? words[i] + " " : words[i];
				builder.append(" " + words[i]);
				if (currentLine.length() + w.length() > lineWidth) {
					builder.append('\n');
					currentLine = "";
				} else
					currentLine += w;
			}
			return builder.toString().trim().split("\n");
		} else {
			String compact = String.join(" ", strings);
			String[] lines = new String[(compact.length() + lineWidth - 1) / lineWidth];
			for (int i = 0, index = 0; i < compact.length(); i += lineWidth)
				lines[index++] = compact.substring(i, Math.min(compact.length(), i + lineWidth));
			return lines;
		}
	}

}
