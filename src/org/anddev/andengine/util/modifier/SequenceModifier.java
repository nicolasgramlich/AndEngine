package org.anddev.andengine.util.modifier;

import org.anddev.andengine.util.modifier.util.ModifierUtils;


/**
 * @author Nicolas Gramlich
 * @since 19:39:25 - 19.03.2010
 */
public class SequenceModifier<T> extends BaseModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private ISubSequenceModifierListener<T> mSubSequenceModifierListener;

	private final IModifier<T>[] mSubSequenceModifiers;
	private int mCurrentSubSequenceModifierIndex;

	private float mSecondsElapsed;
	private final float mDuration;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SequenceModifier(final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		this(null, null, pModifiers);
	}

	public SequenceModifier(final ISubSequenceModifierListener<T> pSubSequenceModifierListener, final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		this(pSubSequenceModifierListener, null, pModifiers);
	}

	public SequenceModifier(final IModifierListener<T> pModifierListener, final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		this(null, pModifierListener, pModifiers);
	}

	public SequenceModifier(final ISubSequenceModifierListener<T> pSubSequenceModifierListener, final IModifierListener<T> pModifierListener, final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		super(pModifierListener);
		if (pModifiers.length == 0) {
			throw new IllegalArgumentException("pModifiers must not be empty!");
		}

		this.mSubSequenceModifierListener = pSubSequenceModifierListener;
		this.mSubSequenceModifiers = pModifiers;

		this.mDuration = ModifierUtils.getSequenceDurationOfModifier(pModifiers);

		pModifiers[0].setModifierListener(new InternalModifierListener());
	}

	@SuppressWarnings("unchecked")
	protected SequenceModifier(final SequenceModifier<T> pSequenceModifier) throws CloneNotSupportedException {
		super(pSequenceModifier.mModifierListener);
		this.mSubSequenceModifierListener = pSequenceModifier.mSubSequenceModifierListener;

		this.mDuration = pSequenceModifier.mDuration;

		final IModifier<T>[] otherModifiers = pSequenceModifier.mSubSequenceModifiers;
		this.mSubSequenceModifiers = new IModifier[otherModifiers.length];

		final IModifier<T>[] shapeModifiers = this.mSubSequenceModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i] = otherModifiers[i].clone();
		}

		shapeModifiers[0].setModifierListener(new InternalModifierListener());
	}

	@Override
	public SequenceModifier<T> clone() throws CloneNotSupportedException{
		return new SequenceModifier<T>(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ISubSequenceModifierListener<T> getSubSequenceModifierListener() {
		return this.mSubSequenceModifierListener;
	}

	public void setSubSequenceModifierListener(final ISubSequenceModifierListener<T> pSubSequenceModifierListener) {
		this.mSubSequenceModifierListener = pSubSequenceModifierListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getSecondsElapsed() {
		return this.mSecondsElapsed;
	}

	@Override
	public float getDuration() {
		return this.mDuration;
	}

	@Override
	public float onUpdate(final float pSecondsElapsed, final T pItem) {
		if(this.mFinished){
			return 0;
		} else {
			float secondsElapsedRemaining = pSecondsElapsed;
			while(secondsElapsedRemaining > 0 && !this.mFinished) {
				secondsElapsedRemaining -= this.mSubSequenceModifiers[this.mCurrentSubSequenceModifierIndex].onUpdate(secondsElapsedRemaining, pItem);
			}

			final float secondsElapsedUsed = pSecondsElapsed - secondsElapsedRemaining;
			this.mSecondsElapsed += secondsElapsedUsed;
			return secondsElapsedUsed;
		}
	}

	@Override
	public void reset() {
		this.mCurrentSubSequenceModifierIndex = 0;
		this.mFinished = false;
		this.mSecondsElapsed = 0;

		final IModifier<T>[] shapeModifiers = this.mSubSequenceModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i].reset();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleModifierStarted(final InternalModifierListener pInternalModifierListener, final IModifier<T> pModifier, final T pItem) {
		if(this.mCurrentSubSequenceModifierIndex == 0) {
			if(this.mModifierListener != null) {
				this.mModifierListener.onModifierStarted(this, pItem);
			}
		}

		if(this.mSubSequenceModifierListener != null) {
			this.mSubSequenceModifierListener.onSubSequenceStarted(pModifier, pItem, this.mCurrentSubSequenceModifierIndex);
		}
	}

	private void onHandleModifierFinished(final InternalModifierListener pInternalModifierListener, final IModifier<T> pModifier, final T pItem) {
		if(this.mSubSequenceModifierListener != null) {
			this.mSubSequenceModifierListener.onSubSequenceFinished(pModifier, pItem, this.mCurrentSubSequenceModifierIndex);
		}

		this.mCurrentSubSequenceModifierIndex++;

		if(this.mCurrentSubSequenceModifierIndex < this.mSubSequenceModifiers.length) {
			final IModifier<T> nextSubSequenceModifier = this.mSubSequenceModifiers[this.mCurrentSubSequenceModifierIndex];
			nextSubSequenceModifier.setModifierListener(pInternalModifierListener);
		} else {
			this.mFinished = true;

			if(this.mModifierListener != null) {
				this.mModifierListener.onModifierFinished(this, pItem);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface ISubSequenceModifierListener<T> {
		public void onSubSequenceStarted(final IModifier<T> pModifier, final T pItem, final int pIndex);
		public void onSubSequenceFinished(final IModifier<T> pModifier, final T pItem, final int pIndex);
	}

	private class InternalModifierListener implements IModifierListener<T>  {
		@Override
		public void onModifierStarted(final IModifier<T> pModifier, final T pItem) {
			SequenceModifier.this.onHandleModifierStarted(this, pModifier, pItem);
		}

		@Override
		public void onModifierFinished(final IModifier<T> pModifier, final T pItem) {
			SequenceModifier.this.onHandleModifierFinished(this, pModifier, pItem);
		}
	}
}
