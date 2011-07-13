package org.anddev.andengine.util.modifier;

import org.anddev.andengine.util.SmartList;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:47:23 - 03.09.2010
 * @param <T>
 */
public abstract class BaseModifier<T> implements IModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected boolean mFinished;
	private boolean mRemoveWhenFinished = true;
	private final SmartList<IModifierListener<T>> mModifierListeners = new SmartList<IModifierListener<T>>(2);

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseModifier() {

	}

	public BaseModifier(final IModifierListener<T> pModifierListener) {
		this.addModifierListener(pModifierListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isFinished() {
		return this.mFinished;
	}

	@Override
	public final boolean isRemoveWhenFinished() {
		return this.mRemoveWhenFinished;
	}

	@Override
	public final void setRemoveWhenFinished(final boolean pRemoveWhenFinished) {
		this.mRemoveWhenFinished = pRemoveWhenFinished;
	}

	@Override
	public void addModifierListener(final IModifierListener<T> pModifierListener) {
		if(pModifierListener != null) {
			this.mModifierListeners.add(pModifierListener);
		}
	}

	@Override
	public boolean removeModifierListener(final IModifierListener<T> pModifierListener) {
		if(pModifierListener == null) {
			return false;
		} else {
			return this.mModifierListeners.remove(pModifierListener);
		}
	}

	@Override
	public abstract IModifier<T> clone() throws CloneNotSupportedException;

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onModifierStarted(final T pItem) {
		final SmartList<IModifierListener<T>> modifierListeners = this.mModifierListeners;
		final int modifierListenerCount = modifierListeners.size();
		for(int i = modifierListenerCount - 1; i >= 0; i--) {
			modifierListeners.get(i).onModifierStarted(this, pItem);
		}
	}

	protected void onModifierFinished(final T pItem) {
		final SmartList<IModifierListener<T>> modifierListeners = this.mModifierListeners;
		final int modifierListenerCount = modifierListeners.size();
		for(int i = modifierListenerCount - 1; i >= 0; i--) {
			modifierListeners.get(i).onModifierFinished(this, pItem);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
