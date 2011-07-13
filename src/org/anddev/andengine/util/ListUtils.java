package org.anddev.andengine.util;

import java.util.ArrayList;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:43:39 - 11.03.2010
 */
public class ListUtils {
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

	public static <T> ArrayList<? extends T> toList(final T pItem) {
		final ArrayList<T> out = new ArrayList<T>();
		out.add(pItem);
		return out;
	}

	public static <T> ArrayList<? extends T> toList(final T ... pItems) {
		final ArrayList<T> out = new ArrayList<T>();
		final int itemCount = pItems.length;
		for(int i = 0; i < itemCount; i++) {
			out.add(pItems[i]);
		}
		return out;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
