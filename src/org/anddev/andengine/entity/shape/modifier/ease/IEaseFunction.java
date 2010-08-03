package org.anddev.andengine.entity.shape.modifier.ease;

/**
 * @author Gil, Nicolas Gramlich
 * @since 17:13:17 - 26.07.2010
 */
public interface IEaseFunction {
	// ===========================================================
	// Final Fields
	// ===========================================================

	public static final IEaseFunction DEFAULT = EaseLinear.getInstance();

	// ===========================================================
	// Methods
	// ===========================================================

	public abstract float getPercentageDone(final float pSecondsElapsed, final float pDuration, final float pMinValue, final float pMaxValue);
}
