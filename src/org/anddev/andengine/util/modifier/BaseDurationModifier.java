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

	private float mSecondsElapsed;
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

	@Override
	public float getSecondsElapsed() {
		return this.mSecondsElapsed;
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
	public final float onUpdate(final float pSecondsElapsed, final T pItem) {
		if(this.mFinished){
			return 0;
		} else {
			if(this.mSecondsElapsed == 0) {
				this.onManagedInitialize(pItem);
				if(this.mModifierListener != null) {
					this.mModifierListener.onModifierStarted(this, pItem);
				}
			}

			final float secondsElapsedUsed;
			if(this.mSecondsElapsed + pSecondsElapsed < this.mDuration) {
				secondsElapsedUsed = pSecondsElapsed;
			} else {
				secondsElapsedUsed = this.mDuration - this.mSecondsElapsed;
			}

			this.mSecondsElapsed += secondsElapsedUsed;
			this.onManagedUpdate(secondsElapsedUsed, pItem);

			if(this.mDuration != -1 && this.mSecondsElapsed >= this.mDuration) {
				this.mSecondsElapsed = this.mDuration;
				this.mFinished = true;
				if(this.mModifierListener != null) {
					this.mModifierListener.onModifierFinished(this, pItem);
				}
			}
			return secondsElapsedUsed;
		}
	}

	@Override
	public void reset() {
		this.mFinished = false;
		this.mSecondsElapsed = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
