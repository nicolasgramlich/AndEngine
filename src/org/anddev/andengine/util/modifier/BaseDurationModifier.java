package org.anddev.andengine.util.modifier;


/**
 * @author Nicolas Gramlich
 * @since 10:48:13 - 03.09.2010
 * @param <T>
 */
public abstract class BaseDurationModifier<T> extends BaseModifier<T> {
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

	public BaseDurationModifier() {
		this(-1, null);
	}

	public BaseDurationModifier(final float pDuration) {
		this(pDuration, null);
	}

	public BaseDurationModifier(final float pDuration, final IModifierListener<T> pModifierListener) {
		super(pModifierListener);
		this.mDuration = pDuration;
	}

	protected BaseDurationModifier(final BaseDurationModifier<T> pBaseModifier) {
		this(pBaseModifier.mDuration, pBaseModifier.mModifierListener);
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

	@Override
	public float getDuration() {
		return this.mDuration;
	}

	protected abstract void onManagedUpdate(final float pSecondsElapsed, final T pItem);

	protected abstract void onManagedInitialize(final T pItem);

	@Override
	public final void onUpdate(final float pSecondsElapsed, final T pItem) {
		if(!this.mFinished){
			if(this.mTotalSecondsElapsed == 0) {
				this.onManagedInitialize(pItem);
			}

			final float secondsToElapse;
			if(this.mTotalSecondsElapsed + pSecondsElapsed < this.mDuration) {
				secondsToElapse = pSecondsElapsed;
			} else {
				secondsToElapse = this.mDuration - this.mTotalSecondsElapsed;
			}

			this.mTotalSecondsElapsed += secondsToElapse;
			this.onManagedUpdate(secondsToElapse, pItem);

			if(this.mDuration != -1 && this.mTotalSecondsElapsed >= this.mDuration) {
				this.mTotalSecondsElapsed = this.mDuration;
				this.mFinished = true;
				if(this.mModifierListener != null) {
					this.mModifierListener.onModifierFinished(this, pItem);
				}
			}
		}
	}

	@Override
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
