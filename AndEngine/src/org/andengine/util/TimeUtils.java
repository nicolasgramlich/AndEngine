package org.andengine.util;

import org.andengine.util.time.TimeConstants;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:48:49 - 04.04.2011
 */
public final class TimeUtils implements TimeConstants {
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

	public static final String formatSeconds(final int pSecondsTotal) {
		return formatSeconds(pSecondsTotal, new StringBuilder());
	}
	
	public static final String formatSeconds(final int pSecondsTotal, final StringBuilder pStringBuilder) {
		final int minutes = pSecondsTotal / SECONDS_PER_MINUTE;
		final int seconds = pSecondsTotal % SECONDS_PER_MINUTE;
		
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
