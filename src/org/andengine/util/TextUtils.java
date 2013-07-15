package org.andengine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.andengine.util.adt.array.ArrayUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 19:01:08 - 03.04.2010
 */
public final class TextUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final Pattern SPLITPATTERN_SPACE = Pattern.compile(" ");
	public static final Pattern SPLITPATTERN_SPACES = Pattern.compile(" +");
	public static final Pattern SPLITPATTERN_COMMA = Pattern.compile(",");

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private TextUtils() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static final CharSequence pad(final CharSequence pText, final char pPadChar, final int pLength) {
		final int padCount = pLength - pText.length();
		if (padCount <= 0) {
			return pText;
		} else {
			final StringBuilder sb = new StringBuilder();

			sb.append(pText);
			for (int i = padCount - 1; i >= 0; i--) {
				sb.append(pPadChar);
			}

			return sb.toString();
		}
	}

	public static final CharSequence padFront(final CharSequence pText, final char pPadChar, final int pLength) {
		final int padCount = pLength - pText.length();
		if (padCount <= 0) {
			return pText;
		} else {
			final StringBuilder sb = new StringBuilder();

			for (int i = padCount - 1; i >= 0; i--) {
				sb.append(pPadChar);
			}
			sb.append(pText);

			return sb.toString();
		}
	}

	public static final int countOccurrences(final CharSequence pText, final char pCharacter) {
		int count = 0;

		int lastOccurrence = android.text.TextUtils.indexOf(pText, pCharacter, 0);

		while (lastOccurrence != -1) {
			count++;
			lastOccurrence = android.text.TextUtils.indexOf(pText, pCharacter, lastOccurrence + 1);
		}

		return count;
	}

	/**
	 * Split a {@link CharSequence} by a Character, i.e. Split lines by using '\n'.<br/>
	 * Same behavior as <code>String.split("" + pCharacter);</code> .
	 */
	public static final ArrayList<CharSequence> split(final CharSequence pText, final char pCharacter) {
		return TextUtils.split(pText, pCharacter, new ArrayList<CharSequence>());
	}

	/**
	 * Split a {@link CharSequence} by a Character, i.e. Split lines by using '\n'.<br/>
	 * Same behavior as <code>String.split("" + pCharacter);</code> .
	 */
	public static final <L extends List<CharSequence>> L split(final CharSequence pText, final char pCharacter, final L pResult) {
		final int partCount = TextUtils.countOccurrences(pText, pCharacter) + 1;

		if (partCount == 0) {
			pResult.add(pText);
		} else {
			int from = 0;
			int to;

			for (int i = 0; i < (partCount - 1); i++) {
				to = android.text.TextUtils.indexOf(pText, pCharacter, from);
				pResult.add(pText.subSequence(from, to));
				from = to + 1;
			}

			pResult.add(pText.subSequence(from, pText.length()));
		}

		return pResult;
	}

	public static final String formatStackTrace(final StackTraceElement pStackTraceElement) {
		return new StringBuilder()
			.append(pStackTraceElement.getClassName())
			.append('.')
			.append(pStackTraceElement.getMethodName())
			.append('(')
			.append(pStackTraceElement.getFileName())
			.append(':')
			.append(pStackTraceElement.getLineNumber())
			.append(')')
			.toString();
	}

	public static final String formatStackTrace(final StackTraceElement[] pStackTraceElements) {
		final StringBuilder sb = new StringBuilder();
		final int stackTraceElementCount = pStackTraceElements.length;
		for (int i = 0; i < stackTraceElementCount; i++) {
			sb.append(pStackTraceElements[i]);
			if (i < (stackTraceElementCount - 1)) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	public static int countCharacters(final List<CharSequence> pTexts) {
		return TextUtils.countCharacters(pTexts, false);
	}

	public static int countCharacters(final List<CharSequence> pTexts, final boolean pIgnoreWhitespaces) {
		int characters = 0;
		if (pIgnoreWhitespaces) {
			for (int i = pTexts.size() - 1; i >= 0; i--) {
				final CharSequence text = pTexts.get(i);
				for (int j = text.length() - 1; j >= 0; j--) {
					final char character = text.charAt(j);
					if (!Character.isWhitespace(character)) {
						characters++;
					}
				}
			}
		} else {
			for (int i = pTexts.size() - 1; i >= 0; i--) {
				final CharSequence text = pTexts.get(i);
				characters += text.length();
			}
		}
		return characters;
	}

	/**
	 * As opposed to {@link ArrayUtils#isEmpty(Object[])}, this method performs a {@link android.text.TextUtils#isEmpty(String)} check on all elements of the array.
	 */
	public static boolean isEmpty(final CharSequence[] pTexts) {
		if (pTexts == null) {
			return true;
		} else {
			for (int i = pTexts.length - 1; i >= 0; i--) {
				if (!android.text.TextUtils.isEmpty(pTexts[i])) {
					return false;
				}
			}
			return true;
		}
	}

	public static final String join(final char pSeparator, final Object ... pStrings) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int count = pStrings.length;
		for (int i = 0; i < count; i++) {
			stringBuilder.append(pStrings[i]);
			if (i < count - 1) {
				stringBuilder.append(pSeparator);
			}
		}

		return stringBuilder.toString();
	}

	public static final String join(final String pSeparator, final Object ... pStrings) {
		final StringBuilder stringBuilder = new StringBuilder();

		final int count = pStrings.length;
		for (int i = 0; i < count; i++) {
			stringBuilder.append(pStrings[i]);
			if (i < count - 1) {
				stringBuilder.append(pSeparator);
			}
		}

		return stringBuilder.toString();
	}

	public static final String ellipsize(final String pText, final String pEllipsis, final int pMaxLength) throws IllegalArgumentException {
		final int textLength = pText.length();
		if (textLength > pMaxLength) {
			final int ellipsisLength = pEllipsis.length();
			if (ellipsisLength > pMaxLength) {
				throw new IllegalArgumentException("pMaxLength: '" + pMaxLength + "' isn't long enough for pEllipsis: '" + pEllipsis + "'.");
			} else {
				final StringBuilder result = new StringBuilder();
				result.append(pText.substring(0, pMaxLength - ellipsisLength));
				result.append(pEllipsis);
				return result.toString();
			}
		} else {
			return pText;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
