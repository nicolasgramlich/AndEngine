package org.anddev.andengine.util.modifier;

import org.anddev.andengine.util.modifier.util.ModifierUtils;

/**
 * @author Nicolas Gramlich
 * @since 11:21:22 - 03.09.2010
 * @param <T>
 */
public class ParallelModifier<T> extends BaseModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IModifier<T>[] mModifiers;

	private final float mDuration;

	private boolean mFinishedCached;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParallelModifier(final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		this(null, pModifiers);
	}

	public ParallelModifier(final IModifierListener<T> pModifierListener, final IModifier<T> ... pModifiers) throws IllegalArgumentException {
		super(pModifierListener);
		if(pModifiers.length == 0) {
			throw new IllegalArgumentException("pModifiers must not be empty!");
		}

		this.mModifiers = pModifiers;

		final IModifier<T> shapeModifierWithLongestDuration = ModifierUtils.getModifierWithLongestDuration(pModifiers);
		this.mDuration = shapeModifierWithLongestDuration.getDuration();
		shapeModifierWithLongestDuration.setModifierListener(new InternalModifierListener());
	}

	@SuppressWarnings("unchecked")
	protected ParallelModifier(final ParallelModifier<T> pParallelModifier) {
		super(pParallelModifier.mModifierListener);

		final IModifier<T>[] otherModifiers = pParallelModifier.mModifiers;
		this.mModifiers = new IModifier[otherModifiers.length];

		final IModifier<T>[] shapeModifiers = this.mModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i] = otherModifiers[i].clone();
		}

		final IModifier<T> shapeModifierWithLongestDuration = ModifierUtils.getModifierWithLongestDuration(shapeModifiers);
		this.mDuration = shapeModifierWithLongestDuration.getDuration();
		shapeModifierWithLongestDuration.setModifierListener(new InternalModifierListener());
	}

	@Override
	public ParallelModifier<T> clone(){
		return new ParallelModifier<T>(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getDuration() {
		return this.mDuration;
	}

	@Override
	public void onUpdate(final float pSecondsElapsed, final T pItem) {
		this.mFinishedCached = false;

		final IModifier<T>[] shapeModifiers = this.mModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i].onUpdate(pSecondsElapsed, pItem);

			if(this.mFinishedCached) {
				return;
			}
		}

		this.mFinishedCached = false;
	}

	@Override
	public void reset() {
		this.mFinished = false;

		final IModifier<T>[] shapeModifiers = this.mModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i].reset();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class InternalModifierListener implements IModifierListener<T>  {
		@Override
		public void onModifierFinished(final IModifier<T> pModifier, final T pItem) {
			ParallelModifier.this.mFinished = true;
			ParallelModifier.this.mFinishedCached = true;
			if(ParallelModifier.this.mModifierListener != null) {
				ParallelModifier.this.mModifierListener.onModifierFinished(ParallelModifier.this, pItem);
			}
		}
	}
}
