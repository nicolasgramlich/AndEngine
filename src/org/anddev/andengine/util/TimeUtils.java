package org.anddev.andengine.util;

import org.anddev.andengine.util.constants.TimeConstants;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:48:49 - 04.04.2011
 */
public class TimeUtils implements TimeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

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

	public static String formatSeconds(final int pSecondsTotal) {
		return formatSeconds(pSecondsTotal, new StringBuilder());
	}
	
	public static String formatSeconds(final int pSecondsTotal, final StringBuilder pStringBuilder) {
		final int minutes = pSecondsTotal / SECONDSPERMINUTE;
		final int seconds = pSecondsTotal % SECONDSPERMINUTE;
		
		pStringBuilder.append(minutes);
		pStringBuilder.append(':');
		
		if(seconds < 10) {
			pStringBuilder.append('0');
		}
		pStringBuilder.append(seconds);

		return pStringBuilder.toString();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
