package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.util.ShapeModifierUtils;

/**
 * @author Nicolas Gramlich
 * @since 19:39:25 - 19.03.2010
 */
public class SequenceModifier extends BaseShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private ISubSequenceModifierListener mSubSequenceModiferListener;

	private final IShapeModifier[] mSubSequenceShapeModifiers;
	private int mCurrentSubSequenceShapeModifier;

	private final float mDuration;

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
		super(pShapeModiferListener);
		if (pShapeModifiers.length == 0) {
			throw new IllegalArgumentException("pShapeModifiers must not be empty!");
		}

		this.mSubSequenceModiferListener = pSubSequenceModifierListener;
		this.mSubSequenceShapeModifiers = pShapeModifiers;

		this.mDuration = ShapeModifierUtils.getSequenceDurationOfShapeModifier(pShapeModifiers);

		pShapeModifiers[0].setShapeModifierListener(new InternalModifierListener());
	}

	protected SequenceModifier(final SequenceModifier pSequenceModifier) {
		super(pSequenceModifier.mShapeModifierListener);
		this.mSubSequenceModiferListener = pSequenceModifier.mSubSequenceModiferListener;

		this.mDuration = pSequenceModifier.mDuration;

		final IShapeModifier[] otherShapeModifiers = pSequenceModifier.mSubSequenceShapeModifiers;
		this.mSubSequenceShapeModifiers = new IShapeModifier[otherShapeModifiers.length];

		final IShapeModifier[] shapeModifiers = this.mSubSequenceShapeModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i] = otherShapeModifiers[i].clone();
		}

		shapeModifiers[0].setShapeModifierListener(new InternalModifierListener());
	}

	@Override
	public SequenceModifier clone(){
		return new SequenceModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

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

	private void onHandleModifierFinished(final InternalModifierListener pInternalModifierListener, final IShapeModifier pShapeModifier, final IShape pShape) {
		this.mCurrentSubSequenceShapeModifier++;

		if(this.mCurrentSubSequenceShapeModifier < this.mSubSequenceShapeModifiers.length) {
			final IShapeModifier nextSubSequenceModifier = this.mSubSequenceShapeModifiers[this.mCurrentSubSequenceShapeModifier];
			nextSubSequenceModifier.setShapeModifierListener(pInternalModifierListener);

			if(this.mSubSequenceModiferListener != null) {
				this.mSubSequenceModiferListener.onSubSequenceFinished(pShapeModifier, pShape, this.mCurrentSubSequenceShapeModifier);
			}
		} else {
			this.mFinished = true;

			if(this.mShapeModifierListener != null) {
				this.mShapeModifierListener.onModifierFinished(this, pShape);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface ISubSequenceModifierListener {
		public void onSubSequenceFinished(final IShapeModifier pShapeModifier, final IShape pShape, final int pIndex);
	}

	private class InternalModifierListener implements IShapeModifierListener  {
		@Override
		public void onModifierFinished(final IShapeModifier pShapeModifier, final IShape pShape) {
			SequenceModifier.this.onHandleModifierFinished(this, pShapeModifier, pShape);
		}
	}
}
