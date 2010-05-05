package org.anddev.andengine.entity.shape.modifier.util;

import org.anddev.andengine.entity.shape.IShapeModifier;

/**
 * @author Nicolas Gramlich
 * @since 13:04:17 - 05.05.2010
 */
public class ShapeModifierUtils {
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
	
	public static IShapeModifier getShapeModifierWithLongestDuration(final IShapeModifier[] pShapeModifiers){
		IShapeModifier out = null;
		float longestDuration = Float.MIN_VALUE;
		
		for(int i = pShapeModifiers.length - 1; i >= 0; i--) {
			final float duration = pShapeModifiers[i].getDuration();
			if(duration > longestDuration) {
				longestDuration = duration;
				out = pShapeModifiers[i];
			}
		}
		
		return out;
	}
	
	public static float getSequenceDurationOfShapeModifier(final IShapeModifier[] pShapeModifiers){
		float duration = Float.MIN_VALUE;
		
		for(int i = pShapeModifiers.length - 1; i >= 0; i--) {
			duration += pShapeModifiers[i].getDuration();
		}
		
		return duration;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
