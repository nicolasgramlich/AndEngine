package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.util.ShapeModifierUtils;

/**
 * @author Nicolas Gramlich
 * @since 19:39:25 - 19.03.2010
 */
public class ParallelModifier implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private IShapeModifierListener mModiferListener;
	private final IShapeModifier[] mShapeModifiers;

	private final float mDuration;

	private boolean mFinished;
	private boolean mRemoveWhenFinished = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParallelModifier(final IShapeModifier ... pShapeModifiers) throws IllegalArgumentException {
		this(null, pShapeModifiers);
	}

	public ParallelModifier(final IShapeModifierListener pShapeModiferListener, final IShapeModifier ... pShapeModifiers) throws IllegalArgumentException {
		if(pShapeModifiers.length == 0) {
			throw new IllegalArgumentException("pShapeModifiers must not be empty!");
		}

		this.mModiferListener = pShapeModiferListener;
		this.mShapeModifiers = pShapeModifiers;

		final IShapeModifier shapeModifierWithLongestDuration = ShapeModifierUtils.getShapeModifierWithLongestDuration(pShapeModifiers);
		this.mDuration = shapeModifierWithLongestDuration.getDuration();
		shapeModifierWithLongestDuration.setModiferListener(new InternalModifierListener());
	}

	public ParallelModifier(final ParallelModifier pSequenceModifier) {
		this.mModiferListener = pSequenceModifier.mModiferListener;

		final IShapeModifier[] otherShapeModifiers = pSequenceModifier.mShapeModifiers;
		this.mShapeModifiers = new IShapeModifier[otherShapeModifiers.length];

		final IShapeModifier[] shapeModifiers = this.mShapeModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i] = otherShapeModifiers[i].clone();
		}

		final IShapeModifier shapeModifierWithLongestDuration = ShapeModifierUtils.getShapeModifierWithLongestDuration(shapeModifiers);
		this.mDuration = shapeModifierWithLongestDuration.getDuration();
		shapeModifierWithLongestDuration.setModiferListener(new InternalModifierListener());
	}

	@Override
	public ParallelModifier clone(){
		return new ParallelModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final void setRemoveWhenFinished(final boolean pRemoveWhenFinished) {
		this.mRemoveWhenFinished = pRemoveWhenFinished;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isFinished() {
		return this.mFinished;
	}
	
	@Override
	public final boolean isRemoveWhenFinished() {
		return this.mRemoveWhenFinished;
	}

	@Override
	public float getDuration() {
		return this.mDuration;
	}

	public IShapeModifierListener getModiferListener() {
		return this.mModiferListener;
	}

	public void setModiferListener(final IShapeModifierListener pShapeModiferListener) {
		this.mModiferListener = pShapeModiferListener;
	}

	@Override
	public void onUpdateShape(final float pSecondsElapsed, final IShape pShape) {
		final IShapeModifier[] shapeModifiers = this.mShapeModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i].onUpdateShape(pSecondsElapsed, pShape);
		}
	}

	@Override
	public void reset() {
		this.mFinished = false;

		final IShapeModifier[] shapeModifiers = this.mShapeModifiers;
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

	private class InternalModifierListener implements IShapeModifierListener  {
		@Override
		public void onModifierFinished(final IShapeModifier pShapeModifier, final IShape pShape) {
			ParallelModifier.this.mFinished = true;
			if(ParallelModifier.this.mModiferListener != null) {
				ParallelModifier.this.mModiferListener.onModifierFinished(ParallelModifier.this, pShape);
			}
		}
	}
}
