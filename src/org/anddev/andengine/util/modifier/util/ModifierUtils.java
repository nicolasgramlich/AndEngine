package org.anddev.andengine.util.modifier.util;

import org.anddev.andengine.util.modifier.IModifier;

/**
 * @author Nicolas Gramlich
 * @since 11:16:36 - 03.09.2010
 */
public class ModifierUtils {
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

	public static <T> IModifier<T> getModifierWithLongestDuration(final IModifier<T>[] pModifiers){
		IModifier<T> out = null;
		float longestDuration = Float.MIN_VALUE;

		for(int i = pModifiers.length - 1; i >= 0; i--) {
			final float duration = pModifiers[i].getDuration();
			if(duration > longestDuration) {
				longestDuration = duration;
				out = pModifiers[i];
			}
		}

		return out;
	}

	public static float getSequenceDurationOfModifier(final IModifier<?>[] pModifiers){
		float duration = Float.MIN_VALUE;

		for(int i = pModifiers.length - 1; i >= 0; i--) {
			duration += pModifiers[i].getDuration();
		}

		return duration;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
