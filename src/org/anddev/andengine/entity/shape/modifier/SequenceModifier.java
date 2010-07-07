package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.util.ShapeModifierUtils;

/**
 * @author Nicolas Gramlich
 * @since 19:39:25 - 19.03.2010
 */
public class SequenceModifier implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private IShapeModifierListener mModiferListener;
	private ISubSequenceModifierListener mSubSequenceModiferListener;

	private final IShapeModifier[] mSubSequenceShapeModifiers;
	private int mCurrentSubSequenceShapeModifier;

	private final float mDuration;

	private boolean mFinished;
	private boolean mRemoveWhenFinished = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SequenceModifier(final IShapeModifier ... pShapeModifiers) throws IllegalArgumentException {
		this(null, pShapeModifiers);
	}

	public SequenceModifier(final IShapeModifierListener pShapeModiferListener, final IShapeModifier ... pShapeModifiers) throws IllegalArgumentException {
		this(pShapeModiferListener, null, pShapeModifiers);
	}

	public SequenceModifier(final IShapeModifierListener pShapeModiferListener, final ISubSequenceModifierListener pSubSequenceModifierListener, final IShapeModifier ... pShapeModifiers) throws IllegalArgumentException {
		if (pShapeModifiers.length == 0) {
			throw new IllegalArgumentException("pShapeModifiers must not be empty!");
		}

		this.mModiferListener = pShapeModiferListener;
		this.mSubSequenceModiferListener = pSubSequenceModifierListener;
		this.mSubSequenceShapeModifiers = pShapeModifiers;

		this.mDuration = ShapeModifierUtils.getSequenceDurationOfShapeModifier(pShapeModifiers);

		pShapeModifiers[0].setModiferListener(new InternalModifierListener());
	}

	public SequenceModifier(final SequenceModifier pSequenceModifier) {
		this.mModiferListener = pSequenceModifier.mModiferListener;
		this.mSubSequenceModiferListener = pSequenceModifier.mSubSequenceModiferListener;

		this.mDuration = pSequenceModifier.mDuration;

		final IShapeModifier[] otherShapeModifiers = pSequenceModifier.mSubSequenceShapeModifiers;
		this.mSubSequenceShapeModifiers = new IShapeModifier[otherShapeModifiers.length];

		final IShapeModifier[] shapeModifiers = this.mSubSequenceShapeModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i] = otherShapeModifiers[i].clone();
		}

		shapeModifiers[0].setModiferListener(new InternalModifierListener());
	}

	@Override
	public SequenceModifier clone(){
		return new SequenceModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final void setRemoveWhenFinished(final boolean pRemoveWhenFinished) {
		this.mRemoveWhenFinished = pRemoveWhenFinished;
	}

	public ISubSequenceModifierListener getSubSequenceModiferListener() {
		return this.mSubSequenceModiferListener;
	}

	public void setSubSequenceModiferListener(final ISubSequenceModifierListener pSubSequenceModiferListener) {
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

	@Override
	public void setModiferListener(final IShapeModifierListener pShapeModiferListener) {
		this.mModiferListener = pShapeModiferListener;
	}

	@Override
	public void onUpdateShape(final float pSecondsElapsed, final IShape pShape) {
		if(!this.mFinished) {
			this.mSubSequenceShapeModifiers[this.mCurrentSubSequenceShapeModifier].onUpdateShape(pSecondsElapsed, pShape);
		}
	}

	@Override
	public void reset() {
		this.mCurrentSubSequenceShapeModifier = 0;
		this.mFinished = false;

		final IShapeModifier[] shapeModifiers = this.mSubSequenceShapeModifiers;
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

	public interface ISubSequenceModifierListener {
		public void onSubSequenceFinished(final IShapeModifier pShapeModifier, final IShape pShape, final int pIndex);
	}

	private class InternalModifierListener implements IShapeModifierListener  {
		@Override
		public void onModifierFinished(final IShapeModifier pShapeModifier, final IShape pShape) {
			final SequenceModifier wrappingSequenceModifier = SequenceModifier.this;

			wrappingSequenceModifier.mCurrentSubSequenceShapeModifier++;

			if(wrappingSequenceModifier.mCurrentSubSequenceShapeModifier < wrappingSequenceModifier.mSubSequenceShapeModifiers.length) {
				final IShapeModifier nextSubSequenceModifier = wrappingSequenceModifier.mSubSequenceShapeModifiers[wrappingSequenceModifier.mCurrentSubSequenceShapeModifier];
				nextSubSequenceModifier.setModiferListener(this);

				if(wrappingSequenceModifier.mSubSequenceModiferListener != null) {
					wrappingSequenceModifier.mSubSequenceModiferListener.onSubSequenceFinished(pShapeModifier, pShape, wrappingSequenceModifier.mCurrentSubSequenceShapeModifier);
				}
			} else {
				wrappingSequenceModifier.mFinished = true;

				if(wrappingSequenceModifier.mModiferListener != null) {
					wrappingSequenceModifier.mModiferListener.onModifierFinished(wrappingSequenceModifier, pShape);
				}
			}
		}
	}
}
