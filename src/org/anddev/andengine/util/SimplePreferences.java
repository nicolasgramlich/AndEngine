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

	public static SharedPreferences getInstance(final Context ctx){
		if(INSTANCE == null) {
			INSTANCE = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		}
		return INSTANCE;
	}

	public static Editor getEditorInstance(final Context ctx){
		if(EDITORINSTANCE == null) {
			EDITORINSTANCE = SimplePreferences.getInstance(ctx).edit();
		}
		return EDITORINSTANCE;
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

	public static boolean isFirstAccess(final Context pCtx, final String pKey){
		return SimplePreferences.isFirstAccess(pCtx, pKey, true);
	}

	public static boolean isFirstAccess(final Context pCtx, final String pKey, final boolean pIncrement){
		return SimplePreferences.isXthAccess(pCtx, pKey, 0);
	}

	public static boolean isXthAccess(final Context pCtx, final String pKey, final int pXthAccess){
		return isXthAccess(pCtx, pKey, pXthAccess, true);
	}

	public static boolean isXthAccess(final Context pCtx, final String pKey, final int pXthAccess, final boolean pIncrement){
		final SharedPreferences prefs = SimplePreferences.getInstance(pCtx);
		final int xthTime = prefs.getInt(pKey, 0);

		if(pIncrement) {
			prefs.edit().putInt(pKey, xthTime + 1).commit();
		}

		return xthTime == pXthAccess;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
