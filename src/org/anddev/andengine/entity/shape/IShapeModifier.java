package org.anddev.andengine.entity.shape;



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

	public IShapeModifier clone();

	public float getDuration();

	public void onUpdateShape(final float pSecondsElapsed, final Shape pShape);

	public IModifierListener getModiferListener();
	public void setModiferListener(final IModifierListener pModiferListener);
}
