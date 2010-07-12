package org.anddev.andengine.entity.shape.modifier;


/**
 * @author Nicolas Gramlich
 * @since 16:10:42 - 19.03.2010
 */
public abstract class BaseShapeModifier implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected boolean mFinished;
	private boolean mRemoveWhenFinished = true;
	protected IShapeModifierListener mShapeModifierListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseShapeModifier() {
		this((IShapeModifierListener)null);
	}

	public BaseShapeModifier(final IShapeModifierListener pShapeModiferListener) {
		this.mShapeModifierListener = pShapeModiferListener;
	}

	protected BaseShapeModifier(final BaseShapeModifier pBaseModifier) {
		this(pBaseModifier.mShapeModifierListener);
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

	public final void setRemoveWhenFinished(final boolean pRemoveWhenFinished) {
		this.mRemoveWhenFinished = pRemoveWhenFinished;
	}

	public IShapeModifierListener getShapeModifierListener() {
		return this.mShapeModifierListener;
	}

	public void setShapeModifierListener(final IShapeModifierListener pShapeModifierListener) {
		this.mShapeModifierListener = pShapeModifierListener;
	}

	@Override
	public abstract IShapeModifier clone();

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
