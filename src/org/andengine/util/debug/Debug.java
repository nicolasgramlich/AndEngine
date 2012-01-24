package org.andengine.util.debug;

import org.andengine.util.constants.Constants;

import android.util.Log;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
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

	private static String sTag = Constants.DEBUGTAG;
	private static DebugLevel sDebugLevel = DebugLevel.VERBOSE;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public static String getTag() {
		return Debug.sTag;
	}

	public static void setTag(final String pTag) {
		Debug.sTag = pTag;
	}

	public static DebugLevel getDebugLevel() {
		return Debug.sDebugLevel;
	}

	public static void setDebugLevel(final DebugLevel pDebugLevel) {
		if(pDebugLevel == null) {
			throw new IllegalArgumentException("pDebugLevel must not be null!");
		}
		Debug.sDebugLevel = pDebugLevel;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static void log(final DebugLevel pDebugLevel, final String pMessage) {
		switch(pDebugLevel) {
			case NONE:
				return;
			case VERBOSE:
				Debug.v(pMessage);
				return;
			case INFO:
				Debug.i(pMessage);
				return;
			case DEBUG:
				Debug.d(pMessage);
				return;
			case WARNING:
				Debug.w(pMessage);
				return;
			case ERROR:
				Debug.e(pMessage);
				return;
		}
	}

	public static void log(final DebugLevel pDebugLevel, final String pMessage, final Throwable pThrowable) {
		switch(pDebugLevel) {
			case NONE:
				return;
			case VERBOSE:
				Debug.v(pMessage, pThrowable);
				return;
			case INFO:
				Debug.i(pMessage, pThrowable);
				return;
			case DEBUG:
				Debug.d(pMessage, pThrowable);
				return;
			case WARNING:
				Debug.w(pMessage, pThrowable);
				return;
			case ERROR:
				Debug.e(pMessage, pThrowable);
				return;
		}
	}

	public static void log(final DebugLevel pDebugLevel, final String pTag, final String pMessage) {
		switch(pDebugLevel) {
			case NONE:
				return;
			case VERBOSE:
				Debug.v(pTag, pMessage);
				return;
			case INFO:
				Debug.i(pTag, pMessage);
				return;
			case DEBUG:
				Debug.d(pTag, pMessage);
				return;
			case WARNING:
				Debug.w(pTag, pMessage);
				return;
			case ERROR:
				Debug.e(pTag, pMessage);
				return;
		}
	}

	public static void log(final DebugLevel pDebugLevel, final String pTag, final String pMessage, final Throwable pThrowable) {
		switch(pDebugLevel) {
			case NONE:
				return;
			case VERBOSE:
				Debug.v(pTag, pMessage, pThrowable);
				return;
			case INFO:
				Debug.i(pTag, pMessage, pThrowable);
				return;
			case DEBUG:
				Debug.d(pTag, pMessage, pThrowable);
				return;
			case WARNING:
				Debug.w(pTag, pMessage, pThrowable);
				return;
			case ERROR:
				Debug.e(pTag, pMessage, pThrowable);
				return;
		}
	}

	public static void v(final String pMessage) {
		Debug.v(Debug.sTag, pMessage, null);
	}

	public static void v(final String pMessage, final Throwable pThrowable) {
		Debug.v(Debug.sTag, pMessage, pThrowable);
	}

	public static void v(final String pTag, final String pMessage) {
		Debug.v(pTag, pMessage, null);
	}

	public static void v(final String pTag, final String pMessage, final Throwable pThrowable) {
		if(Debug.sDebugLevel.isSameOrLessThan(DebugLevel.VERBOSE)) {
			if(pThrowable == null) {
				Log.v(pTag, pMessage);
			} else {
				Log.v(pTag, pMessage, pThrowable);
			}
		}
	}

	public static void d(final String pMessage) {
		Debug.d(Debug.sTag, pMessage, null);
	}

	public static void d(final String pMessage, final Throwable pThrowable) {
		Debug.d(Debug.sTag, pMessage, pThrowable);
	}

	public static void d(final String pTag, final String pMessage) {
		Debug.d(pTag, pMessage, null);
	}

	public static void d(final String pTag, final String pMessage, final Throwable pThrowable) {
		if(Debug.sDebugLevel.isSameOrLessThan(DebugLevel.DEBUG)) {
			if(pThrowable == null) {
				Log.d(pTag, pMessage);
			} else {
				Log.d(pTag, pMessage, pThrowable);
			}
		}
	}

	public static void i(final String pMessage) {
		Debug.i(Debug.sTag, pMessage, null);
	}

	public static void i(final String pTag, final String pMessage) {
		Debug.i(pTag, pMessage, null);
	}

	public static void i(final String pMessage, final Throwable pThrowable) {
		Debug.i(Debug.sTag, pMessage, pThrowable);
	}

	public static void i(final String pTag, final String pMessage, final Throwable pThrowable) {
		if(Debug.sDebugLevel.isSameOrLessThan(DebugLevel.INFO)) {
			if(pThrowable == null) {
				Log.i(pTag, pMessage);
			} else {
				Log.i(pTag, pMessage, pThrowable);
			}
		}
	}

	public static void w(final String pMessage) {
		Debug.w(Debug.sTag, pMessage, null);
	}

	public static void w(final Throwable pThrowable) {
		Debug.w("", pThrowable);
	}

	public static void w(final String pTag, final String pMessage) {
		Debug.w(pTag, pMessage, null);
	}

	public static void w(final String pMessage, final Throwable pThrowable) {
		Debug.w(Debug.sTag, pMessage, pThrowable);
	}

	public static void w(final String pTag, final String pMessage, final Throwable pThrowable) {
		if(Debug.sDebugLevel.isSameOrLessThan(DebugLevel.WARNING)) {
			if(pThrowable == null) {
				Log.w(pTag, pMessage);
			} else {
				Log.w(pTag, pMessage, pThrowable);
			}
		}
	}

	public static void e(final String pMessage) {
		Debug.e(Debug.sTag, pMessage, null);
	}

	public static void e(final Throwable pThrowable) {
		Debug.e(Debug.sTag, pThrowable);
	}

	public static void e(final String pTag, final String pMessage) {
		Debug.e(pTag, pMessage, null);
	}

	public static void e(final String pMessage, final Throwable pThrowable) {
		Debug.e(Debug.sTag, pMessage, pThrowable);
	}

	public static void e(final String pTag, final String pMessage, final Throwable pThrowable) {
		if(Debug.sDebugLevel.isSameOrLessThan(DebugLevel.ERROR)) {
			if(pThrowable == null) {
				Log.e(pTag, pMessage);
			} else {
				Log.e(pTag, pMessage, pThrowable);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum DebugLevel implements Comparable<DebugLevel> {
		NONE,
		ERROR,
		WARNING,
		INFO,
		DEBUG,
		VERBOSE;

		public static DebugLevel ALL = DebugLevel.VERBOSE;

		public boolean isSameOrLessThan(final DebugLevel pDebugLevel) {
			return this.compareTo(pDebugLevel) >= 0;
		}
	}
}
