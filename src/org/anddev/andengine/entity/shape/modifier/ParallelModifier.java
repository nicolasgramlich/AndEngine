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
	private boolean mExpired;
	private float mDuration;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParallelModifier(final IShapeModifier ... pShapeModifiers) {
		this(null, pShapeModifiers);
	}

	public ParallelModifier(final IModifierListener pModiferListener, final IShapeModifier ... pShapeModifiers) {
		assert(pShapeModifiers.length > 0);

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

	public boolean isExpired() {
		return this.mExpired;
	}

	public void setExpired(final boolean pExpired) {
		this.mExpired = pExpired;
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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdateShape(final float pSecondsElapsed, final Shape pShape) {
		final IShapeModifier[] shapeModifiers = this.mShapeModifiers;
		for(int i = shapeModifiers.length - 1; i >= 0; i--) {
			shapeModifiers[i].onUpdateShape(pSecondsElapsed, pShape);
		}
	}

	@Override
	public void reset() {
		this.mExpired = false;

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
		public void onModifierFinished(IShapeModifier pShapeModifier, Shape pShape) {
			ParallelModifier.this.setExpired(true);
			if(ParallelModifier.this.mModiferListener != null) {
				ParallelModifier.this.mModiferListener.onModifierFinished(ParallelModifier.this, pShape);
			}
		}
	}
}
