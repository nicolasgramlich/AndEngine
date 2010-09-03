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

	private ISubSequenceModifierListener<T> mSubSequenceModiferListener;

	private final IModifier<T>[] mSubSequenceModifiers;
	private int mCurrentSubSequenceModifier;

	private final float mDuration;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SequenceModifier(final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		this(null, pModifiers);
	}

	public SequenceModifier(final IModifierListener<T> pModiferListener, final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		this(pModiferListener, null, pModifiers);
	}

	public SequenceModifier(final IModifierListener<T> pModiferListener, final ISubSequenceModifierListener<T> pSubSequenceModifierListener, final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		super(pModiferListener);
		if (pModifiers.length == 0) {
			throw new IllegalArgumentException("pModifiers must not be empty!");
		}

		this.mSubSequenceModiferListener = pSubSequenceModifierListener;
		this.mSubSequenceModifiers = pModifiers;

		this.mDuration = ModifierUtils.getSequenceDurationOfModifier(pModifiers);

		pModifiers[0].setModifierListener(new InternalModifierListener());
	}

	@SuppressWarnings("unchecked")
	protected SequenceModifier(final SequenceModifier<T> pSequenceModifier) {
		super(pSequenceModifier.mModifierListener);
		this.mSubSequenceModiferListener = pSequenceModifier.mSubSequenceModiferListener;

		this.mDuration = pSequenceModifier.mDuration;

		final IModifier<T>[] otherModifiers = pSequenceModifier.mSubSequenceModifiers;
		this.mSubSequenceModifiers = (IModifier<T>[])new IModifier[otherModifiers.length];

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

	public ISubSequenceModifierListener<T> getSubSequenceModiferListener() {
		return this.mSubSequenceModiferListener;
	}

	public void setSubSequenceModiferListener(final ISubSequenceModifierListener<T> pSubSequenceModiferListener) {
		this.mSubSequenceModiferListener = pSubSequenceModiferListener;
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

			if(this.mSubSequenceModiferListener != null) {
				this.mSubSequenceModiferListener.onSubSequenceFinished(pModifier, pItem, this.mCurrentSubSequenceModifier);
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
