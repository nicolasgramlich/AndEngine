package org.andengine.util.modifier.util;

import org.andengine.util.modifier.IModifier;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:16:36 - 03.09.2010
 */
public final class ModifierUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private ModifierUtils() {

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

	public static float getSequenceDurationOfModifier(final IModifier<?>[] pModifiers) {
		float duration = Float.MIN_VALUE;

		for (int i = pModifiers.length - 1; i >= 0; i--) {
			duration += pModifiers[i].getDuration();
		}

		return duration;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
