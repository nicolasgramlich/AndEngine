package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 11:17:50 - 19.03.2010
 */
public interface IShapeModifier {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset();

	public boolean isFinished();
	public boolean isRemoveWhenFinished();

	public IShapeModifier clone();

	public float getDuration();

	public void onUpdateShape(final float pSecondsElapsed, final IShape pShape);

	public IShapeModifierListener getModiferListener();
	public void setModiferListener(final IShapeModifierListener pShapeModiferListener);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static interface IShapeModifierListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onModifierFinished(final IShapeModifier pShapeModifier, final IShape pShape);
	}
}
