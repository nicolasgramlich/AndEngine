package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 16:10:42 - 19.03.2010
 */
public abstract class BaseShapeDurationModifier extends BaseShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mTotalSecondsElapsed;
	protected final float mDuration;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseShapeDurationModifier() {
		this(-1, null);
	}

	public BaseShapeDurationModifier(final float pDuration) {
		this(pDuration, null);
	}

	public BaseShapeDurationModifier(final float pDuration, final IShapeModifierListener pShapeModiferListener) {
		super(pShapeModiferListener);
		this.mDuration = pDuration;
	}

	protected BaseShapeDurationModifier(final BaseShapeDurationModifier pBaseModifier) {
		this(pBaseModifier.mDuration, pBaseModifier.mShapeModifierListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	protected float getTotalSecondsElapsed() {
		return this.mTotalSecondsElapsed;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public float getDuration() {
		return this.mDuration;
	}

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
				if(this.mShapeModifierListener != null) {
					this.mShapeModifierListener.onModifierFinished(this, pShape);
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
