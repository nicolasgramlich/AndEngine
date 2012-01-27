package org.andengine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:01:08 - 03.04.2010
 */
public final class StringUtils {
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

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static final String padFront(final String pString, final char pPadChar, final int pLength) {
		final int padCount = pLength - pString.length();
		if(padCount <= 0) {
			return pString;
		} else {
			final StringBuilder sb = new StringBuilder();

			for(int i = padCount - 1; i >= 0; i--) {
				sb.append(pPadChar);
			}
			sb.append(pString);

			return sb.toString();
		}
	}

	public static final int countOccurrences(final String pString, final char pCharacter) {
		int count = 0;

		int lastOccurrence = pString.indexOf(pCharacter, 0);

		while (lastOccurrence != -1) {
			count++;
			lastOccurrence = pString.indexOf(pCharacter, lastOccurrence + 1);
		}

		return count;
	}

	/**
	 * Split a String by a Character, i.e. Split lines by using '\n'.<br/>
	 * Same behavior as <code>String.split("" + pCharacter);</code> .
	 */
	public static final ArrayList<String> split(final String pString, final char pCharacter) {
		return StringUtils.split(pString, pCharacter, new ArrayList<String>());
	}

	/**
	 * Split a String by a Character, i.e. Split lines by using '\n'.<br/>
	 * Same behavior as <code>String.split("" + pCharacter);</code> .
	 */
	public static final <L extends List<String>> L split(final String pString, final char pCharacter, final L pResult) {
		final int partCount = StringUtils.countOccurrences(pString, pCharacter) + 1;

		if(partCount == 0) {
			pResult.add(pString);
		} else {
			int from = 0;
			int to;

			for (int i = 0; i < partCount - 1; i++) {
				to = pString.indexOf(pCharacter, from);
				pResult.add(pString.substring(from, to));
				from = to + 1;
			}

			pResult.add(pString.substring(from, pString.length()));
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
		for(int i = 0; i < stackTraceElementCount; i++) {
			sb.append(pStackTraceElements[i]);
			if(i < stackTraceElementCount - 1) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	public static int countCharacters(final List<String> pStrings) {
		int characters = 0;
		for(int i = pStrings.size() - 1; i >= 0; i--) {
			characters += pStrings.get(i).length();
		}
		return characters;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
