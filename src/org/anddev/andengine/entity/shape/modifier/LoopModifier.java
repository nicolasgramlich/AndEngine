package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;

/**
 * @author Nicolas Gramlich
 * @since 22:55:13 - 19.03.2010
 */
public class LoopModifier extends BaseShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int LOOP_CONTINUOUS = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mDuration;

	private final IShapeModifier mShapeModifier;

	private ILoopModifierListener mLoopModifierListener;

	private final int mInitialLoopCount;
	private int mLoopCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LoopModifier(final IShapeModifier pShapeModifier) {
		this(null, LOOP_CONTINUOUS, pShapeModifier);
	}

	public LoopModifier(final int pLoopCount, final IShapeModifier pShapeModifier) {
		this(null, pLoopCount, pShapeModifier);
	}

	public LoopModifier(final IShapeModifierListener pShapeModiferListener, final int pLoopCount, final IShapeModifier pShapeModifier) {
		this(pShapeModiferListener, pLoopCount, null, pShapeModifier);
	}

	public LoopModifier(final IShapeModifierListener pShapeModiferListener, final int pLoopCount, final ILoopModifierListener pLoopModifierListener, final IShapeModifier pShapeModifier) {
		super(pShapeModiferListener);
		this.mLoopModifierListener = pLoopModifierListener;
		this.mShapeModifier = pShapeModifier;
		this.mInitialLoopCount = pLoopCount;
		this.mLoopCount = pLoopCount;
		this.mDuration = pLoopCount == LOOP_CONTINUOUS ? Float.POSITIVE_INFINITY : pShapeModifier.getDuration() * pLoopCount; // TODO Check if POSITIVE_INFINITY works correct with i.e. SequenceModifier

		pShapeModifier.setShapeModifierListener(new InternalModifierListener());
	}

	protected LoopModifier(final LoopModifier pLoopModifier) {
		this(pLoopModifier.mShapeModifierListener, pLoopModifier.mInitialLoopCount, pLoopModifier.mShapeModifier.clone());
	}

	@Override
	public LoopModifier clone(){
		return new LoopModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ILoopModifierListener getLoopModifierListener() {
		return this.mLoopModifierListener;
	}

	public void setLoopModifierListener(final ILoopModifierListener pLoopModifierListener) {
		this.mLoopModifierListener = pLoopModifierListener;
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
			this.mShapeModifier.onUpdateShape(pSecondsElapsed, pShape);
		}
	}

	@Override
	public void reset() {
		this.mLoopCount = this.mInitialLoopCount;

		this.mShapeModifier.reset();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleLoopFinished(final IShape pShape) {
		if(this.mLoopModifierListener != null) {
			this.mLoopModifierListener.onLoopFinished(this, this.mLoopCount);
		}

		if(this.mInitialLoopCount == LOOP_CONTINUOUS) {
			this.mShapeModifier.reset();
		} else {
			this.mLoopCount--;
			if(this.mLoopCount < 0) {
				this.mFinished = true;
				if(this.mShapeModifierListener != null) {
					this.mShapeModifierListener.onModifierFinished(this, pShape);
				}
			} else {
				this.mShapeModifier.reset();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface ILoopModifierListener {
		public void onLoopFinished(final LoopModifier pLoopModifier, final int pLoopsRemaining);
	}

	private class InternalModifierListener implements IShapeModifierListener {
		@Override
		public void onModifierFinished(final IShapeModifier pShapeModifier, final IShape pShape) {
			LoopModifier.this.onHandleLoopFinished(pShape);
		}
	}
}
