package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.IShapeModifier;
import org.anddev.andengine.entity.shape.Shape;

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
	private float mTotalSecondsElapsed;
	protected final float mDuration;
	private IModifierListener mModiferListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseModifier() {
		this(-1, null);
	}

	public BaseModifier(final float pDuration) {
		this(pDuration, null);
	}

	public BaseModifier(final float pDuration, final IModifierListener pModiferListener) {
		this.mDuration = pDuration;
		this.mModiferListener = pModiferListener;
	}

	BaseModifier(final BaseModifier pBaseModifier) {
		this(pBaseModifier.mDuration, pBaseModifier.mModiferListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public boolean isFinished() {
		return this.mFinished;
	}

	public void setFinished(final boolean pFinished) {
		this.mFinished = pFinished;
	}

	protected float getTotalSecondsElapsed() {
		return this.mTotalSecondsElapsed;
	}

	public IModifierListener getModiferListener() {
		return this.mModiferListener;
	}

	public void setModiferListener(final IModifierListener pModiferListener) {
		this.mModiferListener = pModiferListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public abstract IShapeModifier clone();

	protected abstract void onManagedUpdateShape(final float pSecondsElapsed, final Shape pShape);

	protected abstract void onManagedInitializeShape(final Shape pShape);

	@Override
	public final void onUpdateShape(final float pSecondsElapsed, final Shape pShape) {
		if(!this.isFinished()){
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
				this.setFinished(true);
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
