package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.IShapeModifier;
import org.anddev.andengine.entity.shape.Shape;
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

	private IModifierListener mModiferListener;
	private final IShapeModifier[] mShapeModifiers;

	private final float mDuration;

	private boolean mFinished;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParallelModifier(final IShapeModifier ... pShapeModifiers) {
		this(null, pShapeModifiers);
	}

	public ParallelModifier(final IModifierListener pModiferListener, final IShapeModifier ... pShapeModifiers) {
		assert(pShapeModifiers.length > 0) : "pShapeModifiers must not be empty!";

		this.mModiferListener = pModiferListener;
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isFinished() {
		return this.mFinished;
	}

	@Override
	public float getDuration() {
		return this.mDuration;
	}

	public IModifierListener getModiferListener() {
		return this.mModiferListener;
	}

	public void setModiferListener(final IModifierListener pModiferListener) {
		this.mModiferListener = pModiferListener;
	}

	@Override
	public void onUpdateShape(final float pSecondsElapsed, final Shape pShape) {
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

	private class InternalModifierListener implements IModifierListener  {
		@Override
		public void onModifierFinished(final IShapeModifier pShapeModifier, final Shape pShape) {
			ParallelModifier.this.mFinished = true;
			if(ParallelModifier.this.mModiferListener != null) {
				ParallelModifier.this.mModiferListener.onModifierFinished(ParallelModifier.this, pShape);
			}
		}
	}
}
