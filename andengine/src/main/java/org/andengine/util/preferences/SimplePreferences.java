package org.andengine.util.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:55:12 - 02.08.2010
 */
public class SimplePreferences {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static SharedPreferences INSTANCE;
	private static Editor EDITORINSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	public static SharedPreferences getInstance(final Context pContext) {
		if(SimplePreferences.INSTANCE == null) {
			SimplePreferences.INSTANCE = PreferenceManager.getDefaultSharedPreferences(pContext);
		}
		return SimplePreferences.INSTANCE;
	}

	public static Editor getEditorInstance(final Context pContext) {
		if(SimplePreferences.EDITORINSTANCE == null) {
			SimplePreferences.EDITORINSTANCE = SimplePreferences.getInstance(pContext).edit();
		}
		return SimplePreferences.EDITORINSTANCE;
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

	public static int incrementAccessCount(final Context pContext, final String pKey) {
		return SimplePreferences.incrementAccessCount(pContext, pKey, 1);
	}

	public static int incrementAccessCount(final Context pContext, final String pKey, final int pIncrement) {
		final SharedPreferences prefs = SimplePreferences.getInstance(pContext);
		final int accessCount = prefs.getInt(pKey, 0);

		final int newAccessCount = accessCount + pIncrement;
		prefs.edit().putInt(pKey, newAccessCount).commit();

		return newAccessCount;
	}

	public static int getAccessCount(final Context pCtx, final String pKey) {
		return SimplePreferences.getInstance(pCtx).getInt(pKey, 0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
