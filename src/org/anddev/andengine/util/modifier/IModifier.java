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

	public IModifier<T> clone() throws CloneNotSupportedException;

	public float getSecondsElapsed();
	public float getDuration();

	public float onUpdate(final float pSecondsElapsed, final T pItem);

	public IModifierListener<T> getModifierListener();
	public void setModifierListener(final IModifierListener<T> pModifierListener);

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

		public void onModifierStarted(final IModifier<T> pModifier, final T pItem);
		public void onModifierFinished(final IModifier<T> pModifier, final T pItem);
	}
	
	public static class CloneNotSupportedException extends RuntimeException {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = -5838035434002587320L;

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

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
