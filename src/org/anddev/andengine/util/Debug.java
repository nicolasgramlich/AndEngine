package org.anddev.andengine.util;

import org.anddev.andengine.util.constants.Constants;

import android.util.Log;

/**
 * @author Nicolas Gramlich
 * @since 13:29:16 - 08.03.2010
 */
public class Debug implements Constants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static DebugLevel DEBUGLEVEL = DebugLevel.VERBOSE;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public static void setDebugLevel(final DebugLevel pDebugLevel) {
		if(pDebugLevel == null) {
			throw new IllegalArgumentException("pDebugLevel must not be null!");
		}
		Debug.DEBUGLEVEL = pDebugLevel;
	}

	public static DebugLevel getDebugLevel() {
		return Debug.DEBUGLEVEL;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static void v(final String pMessage) {
		Debug.v(pMessage, null);
	}

	public static void v(final String pMessage, final Throwable pThrowable) {
		if(DEBUGLEVEL.isSameOrLessThan(DebugLevel.VERBOSE)) {
			Log.v(DEBUGTAG, pMessage, pThrowable);
		}
	}

	public static void d(final String pMessage) {
		Debug.d(pMessage, null);
	}

	public static void d(final String pMessage, final Throwable pThrowable) {
		if(DEBUGLEVEL.isSameOrLessThan(DebugLevel.DEBUG)) {
			Log.d(DEBUGTAG, pMessage, pThrowable);
		}
	}

	public static void i(final String pMessage) {
		Debug.i(pMessage, null);
	}

	public static void i(final String pMessage, final Throwable pThrowable) {
		if(DEBUGLEVEL.isSameOrLessThan(DebugLevel.INFO)) {
			Log.i(DEBUGTAG, pMessage, pThrowable);
		}
	}

	public static void w(final String pMessage) {
		Debug.w(pMessage, null);
	}

	public static void w(final Throwable pThrowable) {
		Debug.w(DEBUGTAG, pThrowable);
	}

	public static void w(final String pMessage, final Throwable pThrowable) {
		if(DEBUGLEVEL.isSameOrLessThan(DebugLevel.WARNING)) {
			if(pThrowable == null) {
				Log.w(DEBUGTAG, pMessage, new Exception());
			} else {
				Log.w(DEBUGTAG, pMessage, pThrowable);
			}
		}
	}

	public static void e(final String pMessage) {
		Debug.e(pMessage, null);
	}

	public static void e(final Throwable pThrowable) {
		Debug.e(DEBUGTAG, pThrowable);
	}

	public static void e(final String pMessage, final Throwable pThrowable) {
		if(DEBUGLEVEL.isSameOrLessThan(DebugLevel.ERROR)) {
			if(pThrowable == null) {
				Log.e(DEBUGTAG, pMessage, new Exception());
			} else {
				Log.e(DEBUGTAG, pMessage, pThrowable);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum DebugLevel implements Comparable<DebugLevel> {
		NONE, ERROR, WARNING, INFO, DEBUG, VERBOSE;
		
		public static DebugLevel ALL = DebugLevel.VERBOSE; 

		private boolean isSameOrLessThan(final DebugLevel pDebugLevel) {
			return this.compareTo(pDebugLevel) >= 0;
		}
	}
}
