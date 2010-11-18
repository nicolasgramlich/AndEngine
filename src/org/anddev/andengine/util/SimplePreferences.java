package org.anddev.andengine.util;

import org.anddev.andengine.util.constants.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Nicolas Gramlich
 * @since 18:55:12 - 02.08.2010
 */
public class SimplePreferences implements Constants {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String PREFERENCES_NAME = null;

	// ===========================================================
	// Fields
	// ===========================================================

	private static SharedPreferences INSTANCE;
	private static Editor EDITORINSTANCE;

	// ===========================================================
	// Constructors
	// ===========================================================

	public static SharedPreferences getInstance(final Context ctx) {
		if(SimplePreferences.INSTANCE == null) {
			SimplePreferences.INSTANCE = ctx.getSharedPreferences(SimplePreferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
		}
		return SimplePreferences.INSTANCE;
	}

	public static Editor getEditorInstance(final Context ctx) {
		if(SimplePreferences.EDITORINSTANCE == null) {
			SimplePreferences.EDITORINSTANCE = SimplePreferences.getInstance(ctx).edit();
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

	public static int incrementAccessCount(final Context pCtx, final String pKey) {
		return SimplePreferences.incrementAccessCount(pCtx, pKey, 1);
	}

	public static int incrementAccessCount(final Context pCtx, final String pKey, final int pIncrement) {
		final SharedPreferences prefs = SimplePreferences.getInstance(pCtx);
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
