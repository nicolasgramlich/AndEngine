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

	public abstract float calc(float t, float b, float c, float d);
}
