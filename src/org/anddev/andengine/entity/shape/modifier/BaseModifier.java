package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 16:10:42 - 19.03.2010
 */
public abstract class BaseModifier implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mFinished;
	private boolean mRemoveWhenFinished = true;
	private float mTotalSecondsElapsed;
	protected final float mDuration;
	private IShapeModifierListener mModiferListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseModifier() {
		this(-1, null);
	}

	public BaseModifier(final float pDuration) {
		this(pDuration, null);
	}

	public BaseModifier(final float pDuration, final IShapeModifierListener pShapeModiferListener) {
		this.mDuration = pDuration;
		this.mModiferListener = pShapeModiferListener;
	}

	BaseModifier(final BaseModifier pBaseModifier) {
		this(pBaseModifier.mDuration, pBaseModifier.mModiferListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	protected float getTotalSecondsElapsed() {
		return this.mTotalSecondsElapsed;
	}

	public final void setRemoveWhenFinished(final boolean pRemoveWhenFinished) {
		this.mRemoveWhenFinished = pRemoveWhenFinished;
	}

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

	public IShapeModifierListener getModiferListener() {
		return this.mModiferListener;
	}

	public void setModiferListener(final IShapeModifierListener pShapeModiferListener) {
		this.mModiferListener = pShapeModiferListener;
	}

	public float getDuration() {
		return this.mDuration;
	}

	@Override
	public abstract IShapeModifier clone();

	protected abstract void onManagedUpdateShape(final float pSecondsElapsed, final IShape pShape);

	protected abstract void onManagedInitializeShape(final IShape pShape);

	@Override
	public final void onUpdateShape(final float pSecondsElapsed, final IShape pShape) {
		if(!this.mFinished){
			if(this.mTotalSecondsElapsed == 0) {
				this.onManagedInitializeShape(pShape);
			}

			final float secondsToElapse;
			if(this.mTotalSecondsElapsed + pSecondsElapsed < this.mDuration) {
				secondsToElapse = pSecondsElapsed;
			} else {
				secondsToElapse = this.mDuration - this.mTotalSecondsElapsed;
			}

			this.mTotalSecondsElapsed += secondsToElapse;
			this.onManagedUpdateShape(secondsToElapse, pShape);

			if(this.mDuration != -1 && this.mTotalSecondsElapsed >= this.mDuration) {
				this.mTotalSecondsElapsed = this.mDuration;
				this.mFinished = true;
				if(this.mModiferListener != null) {
					this.mModiferListener.onModifierFinished(this, pShape);
				}
			}
		}
	}

	public void reset() {
		this.mFinished = false;
		this.mTotalSecondsElapsed = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
