package org.anddev.andengine.util.modifier;

import java.util.Comparator;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:17:50 - 19.03.2010
 */
public interface IModifier<T> extends Cloneable {
	// ===========================================================
	// Final Fields
	// ===========================================================

	public static final Comparator<IModifier<?>> MODIFIER_COMPARATOR_DURATION_DESCENDING = new Comparator<IModifier<?>>() {
		@Override
		public int compare(final IModifier<?> pModifierA, final IModifier<?> pModifierB) {
			return (int)Math.signum(pModifierA.getDuration() - pModifierB.getDuration());
		}
	};

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset();

	public boolean isFinished();
	public boolean isRemoveWhenFinished();
	public void setRemoveWhenFinished(final boolean pRemoveWhenFinished);

	public IModifier<T> clone() throws CloneNotSupportedException;
	//	public IModifier<T> clone(final IModifierListener<T> pModifierListener) throws CloneNotSupportedException; TODO

	public float getSecondsElapsed();
	public float getDuration();

	public float onUpdate(final float pSecondsElapsed, final T pItem);

	public void addModifierListener(final IModifierListener<T> pModifierListener);
	public boolean removeModifierListener(final IModifierListener<T> pModifierListener);

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
