package org.anddev.andengine.util;

import org.anddev.andengine.util.constants.Constants;

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

	private static String sDebugTag = DEBUGTAG;
	private static DebugLevel sDebugLevel = DebugLevel.VERBOSE;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public static String getDebugTag() {
		return Debug.sDebugTag;
	}

	public static void setDebugTag(final String pDebugTag) {
		Debug.sDebugTag = pDebugTag;
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

	public static void v(final String pMessage) {
		Debug.v(pMessage, null);
	}

	public static void v(final String pMessage, final Throwable pThrowable) {
		if(sDebugLevel.isSameOrLessThan(DebugLevel.VERBOSE)) {
			Log.v(sDebugTag, pMessage, pThrowable);
		}
	}

	public static void d(final String pMessage) {
		Debug.d(pMessage, null);
	}

	public static void d(final String pMessage, final Throwable pThrowable) {
		if(sDebugLevel.isSameOrLessThan(DebugLevel.DEBUG)) {
			Log.d(sDebugTag, pMessage, pThrowable);
		}
	}

	public static void i(final String pMessage) {
		Debug.i(pMessage, null);
	}

	public static void i(final String pMessage, final Throwable pThrowable) {
		if(sDebugLevel.isSameOrLessThan(DebugLevel.INFO)) {
			Log.i(sDebugTag, pMessage, pThrowable);
		}
	}

	public static void w(final String pMessage) {
		Debug.w(pMessage, null);
	}

	public static void w(final Throwable pThrowable) {
		Debug.w("", pThrowable);
	}

	public static void w(final String pMessage, final Throwable pThrowable) {
		if(sDebugLevel.isSameOrLessThan(DebugLevel.WARNING)) {
			if(pThrowable == null) {
				Log.w(sDebugTag, pMessage, new Exception());
			} else {
				Log.w(sDebugTag, pMessage, pThrowable);
			}
		}
	}

	public static void e(final String pMessage) {
		Debug.e(pMessage, null);
	}

	public static void e(final Throwable pThrowable) {
		Debug.e(sDebugTag, pThrowable);
	}

	public static void e(final String pMessage, final Throwable pThrowable) {
		if(sDebugLevel.isSameOrLessThan(DebugLevel.ERROR)) {
			if(pThrowable == null) {
				Log.e(sDebugTag, pMessage, new Exception());
			} else {
				Log.e(sDebugTag, pMessage, pThrowable);
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

		private boolean isSameOrLessThan(final DebugLevel pDebugLevel) {
			return this.compareTo(pDebugLevel) >= 0;
		}
	}
}
