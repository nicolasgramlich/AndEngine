package org.andengine.util.modifier;

import org.andengine.util.adt.list.SmartList;


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
	private boolean mAutoUnregisterWhenFinished = true;
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
	public final boolean isAutoUnregisterWhenFinished() {
		return this.mAutoUnregisterWhenFinished;
	}

	@Override
	public final void setAutoUnregisterWhenFinished(final boolean pAutoUnregisterWhenFinished) {
		this.mAutoUnregisterWhenFinished = pAutoUnregisterWhenFinished;
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
	public abstract IModifier<T> deepCopy() throws DeepCopyNotSupportedException;

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

	protected static final <T> void assertNoNullModifier(final IModifier<T> pModifier) {
		if(pModifier == null) {
			throw new IllegalArgumentException("Illegal 'null' " + IModifier.class.getSimpleName() + " detected!");
		}
	}

	protected static final <T> void assertNoNullModifier(final IModifier<T> ... pModifiers) {
		final int modifierCount = pModifiers.length;
		for(int i = 0; i < modifierCount; i++) {
			if(pModifiers[i] == null) {
				throw new IllegalArgumentException("Illegal 'null' " + IModifier.class.getSimpleName() + " detected at position: '" + i + "'!");
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
