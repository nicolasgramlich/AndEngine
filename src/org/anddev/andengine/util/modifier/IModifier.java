package org.anddev.andengine.util.modifier;


/**
 * @author Nicolas Gramlich
 * @since 11:17:50 - 19.03.2010
 */
public interface IModifier<T> extends Cloneable {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset();

	public boolean isFinished();
	public boolean isRemoveWhenFinished();
	public void setRemoveWhenFinished(final boolean pRemoveWhenFinished);

	public IModifier<T> clone();

	public float getDuration();

	public void onUpdate(final float pSecondsElapsed, final T pItem);

	public IModifierListener<T> getModifierListener();
	public void setModifierListener(final IModifierListener<T> pModiferListener);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IModifierListener<T> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onModifierFinished(final IModifier<T> pModifier, final T pItem);
	}
}
