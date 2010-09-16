package org.anddev.andengine.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author Nicolas Gramlich
 * @since 20:55:35 - 08.09.2009
 */
public class ViewUtils {
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

	public static View inflate(final Context pContext, final int pLayoutID){
		final LayoutInflater inflater = LayoutInflater.from(pContext);
		return inflater.inflate(pLayoutID, null);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
