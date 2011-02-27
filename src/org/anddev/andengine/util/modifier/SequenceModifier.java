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
	private int mCurrentSubSequenceModifier;

	private final float mDuration;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SequenceModifier(final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		this(null, pModifiers);
	}

	public SequenceModifier(final IModifierListener<T> pModifierListener, final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		this(pModifierListener, null, pModifiers);
	}

	public SequenceModifier(final IModifierListener<T> pModifierListener, final ISubSequenceModifierListener<T> pSubSequenceModifierListener, final IModifier<T> ... pModifiers) throws IllegalArgumentException {
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
	protected SequenceModifier(final SequenceModifier<T> pSequenceModifier) {
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
	public SequenceModifier<T> clone(){
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
	public float getDuration() {
		return this.mDuration;
	}

	@Override
	public void onUpdate(final float pSecondsElapsed, final T pItem) {
		if(!this.mFinished) {
			this.mSubSequenceModifiers[this.mCurrentSubSequenceModifier].onUpdate(pSecondsElapsed, pItem);
		}
	}

	@Override
	public void reset() {
		this.mCurrentSubSequenceModifier = 0;
		this.mFinished = false;

		final IModifier<T>[] shapeModifiers = this.mSubSequenceModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i].reset();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void onHandleModifierFinished(final InternalModifierListener pInternalModifierListener, final IModifier<T> pModifier, final T pItem) {
		this.mCurrentSubSequenceModifier++;

		if(this.mCurrentSubSequenceModifier < this.mSubSequenceModifiers.length) {
			final IModifier<T> nextSubSequenceModifier = this.mSubSequenceModifiers[this.mCurrentSubSequenceModifier];
			nextSubSequenceModifier.setModifierListener(pInternalModifierListener);

			if(this.mSubSequenceModifierListener != null) {
				this.mSubSequenceModifierListener.onSubSequenceFinished(pModifier, pItem, this.mCurrentSubSequenceModifier);
			}
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
		public void onSubSequenceFinished(final IModifier<T> pModifier, final T pItem, final int pIndex);
	}

	private class InternalModifierListener implements IModifierListener<T>  {
		@Override
		public void onModifierFinished(final IModifier<T> pModifier, final T pItem) {
			SequenceModifier.this.onHandleModifierFinished(this, pModifier, pItem);
		}
	}
}
