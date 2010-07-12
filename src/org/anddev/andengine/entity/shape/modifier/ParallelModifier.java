package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.util.ShapeModifierUtils;

/**
 * @author Nicolas Gramlich
 * @since 19:39:25 - 19.03.2010
 */
public class ParallelModifier extends BaseShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IShapeModifier[] mShapeModifiers;

	private final float mDuration;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParallelModifier(final IShapeModifier ... pShapeModifiers) throws IllegalArgumentException {
		this(null, pShapeModifiers);
	}

	public ParallelModifier(final IShapeModifierListener pShapeModiferListener, final IShapeModifier ... pShapeModifiers) throws IllegalArgumentException {
		super(pShapeModiferListener);
		if(pShapeModifiers.length == 0) {
			throw new IllegalArgumentException("pShapeModifiers must not be empty!");
		}

		this.mShapeModifiers = pShapeModifiers;

		final IShapeModifier shapeModifierWithLongestDuration = ShapeModifierUtils.getShapeModifierWithLongestDuration(pShapeModifiers);
		this.mDuration = shapeModifierWithLongestDuration.getDuration();
		shapeModifierWithLongestDuration.setShapeModifierListener(new InternalModifierListener());
	}

	protected ParallelModifier(final ParallelModifier pParallelModifier) {
		super(pParallelModifier.mShapeModifierListener);

		final IShapeModifier[] otherShapeModifiers = pParallelModifier.mShapeModifiers;
		this.mShapeModifiers = new IShapeModifier[otherShapeModifiers.length];

		final IShapeModifier[] shapeModifiers = this.mShapeModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i] = otherShapeModifiers[i].clone();
		}

		final IShapeModifier shapeModifierWithLongestDuration = ShapeModifierUtils.getShapeModifierWithLongestDuration(shapeModifiers);
		this.mDuration = shapeModifierWithLongestDuration.getDuration();
		shapeModifierWithLongestDuration.setShapeModifierListener(new InternalModifierListener());
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
	public float getDuration() {
		return this.mDuration;
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
			if(ParallelModifier.this.mShapeModifierListener != null) {
				ParallelModifier.this.mShapeModifierListener.onModifierFinished(ParallelModifier.this, pShape);
			}
		}
	}
}
