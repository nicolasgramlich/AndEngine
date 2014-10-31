package org.andengine.util.modifier;

import java.util.Arrays;

import org.andengine.util.modifier.IModifier.IModifierListener;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:21:22 - 03.09.2010
 * @param <T>
 */
public class ParallelModifier<T> extends BaseModifier<T> implements IModifierListener<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float mSecondsElapsed;
	private final float mDuration;

	private final IModifier<T>[] mModifiers;
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

		BaseModifier.assertNoNullModifier(pModifiers);

		Arrays.sort(pModifiers, MODIFIER_COMPARATOR_DURATION_DESCENDING);
		this.mModifiers = pModifiers;

		final IModifier<T> modifierWithLongestDuration = pModifiers[0];
		this.mDuration = modifierWithLongestDuration.getDuration();
		modifierWithLongestDuration.addModifierListener(this);
	}

	@SuppressWarnings("unchecked")
	protected ParallelModifier(final ParallelModifier<T> pParallelModifier) throws DeepCopyNotSupportedException {
		final IModifier<T>[] otherModifiers = pParallelModifier.mModifiers;
		this.mModifiers = new IModifier[otherModifiers.length];

		final IModifier<T>[] modifiers = this.mModifiers;
		for(int i = modifiers.length - 1; i >= 0; i--) {
			modifiers[i] = otherModifiers[i].deepCopy();
		}

		final IModifier<T> modifierWithLongestDuration = modifiers[0];
		this.mDuration = modifierWithLongestDuration.getDuration();
		modifierWithLongestDuration.addModifierListener(this);
	}

	@Override
	public ParallelModifier<T> deepCopy() throws DeepCopyNotSupportedException{
		return new ParallelModifier<T>(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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

			final IModifier<T>[] shapeModifiers = this.mModifiers;

			this.mFinishedCached = false;
			while(secondsElapsedRemaining > 0 && !this.mFinishedCached) {
				float secondsElapsedUsed = 0;
				for(int i = shapeModifiers.length - 1; i >= 0; i--) {
					secondsElapsedUsed = Math.max(secondsElapsedUsed, shapeModifiers[i].onUpdate(pSecondsElapsed, pItem));
				}
				secondsElapsedRemaining -= secondsElapsedUsed;
			}
			this.mFinishedCached = false;

			final float secondsElapsedUsed = pSecondsElapsed - secondsElapsedRemaining;
			this.mSecondsElapsed += secondsElapsedUsed;
			return secondsElapsedUsed;
		}
	}

	@Override
	public void reset() {
		this.mFinished = false;
		this.mSecondsElapsed = 0;

		final IModifier<T>[] shapeModifiers = this.mModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i].reset();
		}
	}

	@Override
	public void onModifierStarted(final IModifier<T> pModifier, final T pItem) {
		this.onModifierStarted(pItem);
	}

	@Override
	public void onModifierFinished(final IModifier<T> pModifier, final T pItem) {
		this.mFinished = true;
		this.mFinishedCached = true;
		this.onModifierFinished(pItem);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
