package org.andengine.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 20:55:35 - 08.09.2009
 */
public final class ViewUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private ViewUtils() {

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

	public static final View inflate(final Context pContext, final int pLayoutID) {
		return LayoutInflater.from(pContext).inflate(pLayoutID, null);
	}

	public static final View inflate(final Context pContext, final int pLayoutID, final ViewGroup pViewGroup) {
		return LayoutInflater.from(pContext).inflate(pLayoutID, pViewGroup, true);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
