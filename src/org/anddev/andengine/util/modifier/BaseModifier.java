package org.anddev.andengine.util.modifier;


/**
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
	protected IModifierListener<T> mModifierListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseModifier() {
		this((IModifierListener<T>)null);
	}

	public BaseModifier(final IModifierListener<T> pModifierListener) {
		this.mModifierListener = pModifierListener;
	}

	protected BaseModifier(final BaseModifier<T> pBaseModifier) {
		this(pBaseModifier.mModifierListener);
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
	public IModifierListener<T> getModifierListener() {
		return this.mModifierListener;
	}

	@Override
	public void setModifierListener(final IModifierListener<T> pModifierListener) {
		this.mModifierListener = pModifierListener;
	}

	@Override
	public abstract IModifier<T> clone();

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
