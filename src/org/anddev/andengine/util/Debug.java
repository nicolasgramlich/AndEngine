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

	public static void v(final String pMessage){
		Log.v(DEBUGTAG, pMessage);
	}

	public static void d(final String pMessage){
		Log.d(DEBUGTAG, pMessage);
	}

	public static void i(final String pMessage){
		Log.i(DEBUGTAG, pMessage);
	}

	public static void w(final Throwable pThrowable){
		Debug.w("Warning", pThrowable);
	}

	public static void w(final String pMessage, final Throwable pThrowable){
		if(pThrowable == null){
			Log.w(DEBUGTAG, pMessage);
			(new Exception()).printStackTrace();
		}else{
			Log.w(DEBUGTAG, pMessage, pThrowable);
		}
	}

	public static void e(final Throwable pThrowable){
		Debug.e("Error", pThrowable);
	}

	public static void e(final String pMessage, final Throwable pThrowable){
		if(pThrowable == null){
			Log.e(DEBUGTAG, pMessage);
			(new Exception()).printStackTrace();
		}else{
			Log.e(DEBUGTAG, pMessage, pThrowable);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
