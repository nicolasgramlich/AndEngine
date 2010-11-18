package org.anddev.andengine.util.modifier;


/**
 * @author Nicolas Gramlich
 * @since 11:18:37 - 03.09.2010
 * @param <T>
 */
public class LoopModifier<T> extends BaseModifier<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int LOOP_CONTINUOUS = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mDuration;

	private final IModifier<T> mModifier;

	private ILoopModifierListener<T> mLoopModifierListener;

	private final int mInitialLoopCount;
	private int mLoopCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LoopModifier(final IModifier<T> pModifier) {
		this(null, LOOP_CONTINUOUS, pModifier);
	}

	public LoopModifier(final int pLoopCount, final IModifier<T> pModifier) {
		this(null, pLoopCount, pModifier);
	}

	public LoopModifier(final IModifierListener<T> pModifierListener, final int pLoopCount, final IModifier<T> pModifier) {
		this(pModifierListener, pLoopCount, null, pModifier);
	}

	public LoopModifier(final IModifierListener<T> pModifierListener, final int pLoopCount, final ILoopModifierListener<T> pLoopModifierListener, final IModifier<T> pModifier) {
		super(pModifierListener);
		this.mLoopModifierListener = pLoopModifierListener;
		this.mModifier = pModifier;
		this.mInitialLoopCount = pLoopCount;
		this.mLoopCount = pLoopCount;
		this.mDuration = pLoopCount == LOOP_CONTINUOUS ? Float.POSITIVE_INFINITY : pModifier.getDuration() * pLoopCount; // TODO Check if POSITIVE_INFINITY works correct with i.e. SequenceModifier

		pModifier.setModifierListener(new InternalModifierListener());
	}

	protected LoopModifier(final LoopModifier<T> pLoopModifier) {
		this(pLoopModifier.mModifierListener, pLoopModifier.mInitialLoopCount, pLoopModifier.mModifier.clone());
	}

	@Override
	public LoopModifier<T> clone(){
		return new LoopModifier<T>(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ILoopModifierListener<T> getLoopModifierListener() {
		return this.mLoopModifierListener;
	}

	public void setLoopModifierListener(final ILoopModifierListener<T> pLoopModifierListener) {
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
	public void onUpdate(final float pSecondsElapsed, final T pItem) {
		if(!this.mFinished) {
			this.mModifier.onUpdate(pSecondsElapsed, pItem);
		}
	}

	@Override
	public void reset() {
		this.mLoopCount = this.mInitialLoopCount;

		this.mModifier.reset();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleLoopFinished(final T pItem) {
		if(this.mLoopModifierListener != null) {
			this.mLoopModifierListener.onLoopFinished(this, this.mLoopCount);
		}

		if(this.mInitialLoopCount == LOOP_CONTINUOUS) {
			this.mModifier.reset();
		} else {
			this.mLoopCount--;
			if(this.mLoopCount < 0) {
				this.mFinished = true;
				if(this.mModifierListener != null) {
					this.mModifierListener.onModifierFinished(this, pItem);
				}
			} else {
				this.mModifier.reset();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface ILoopModifierListener<T> {
		public void onLoopFinished(final LoopModifier<T> pLoopModifier, final int pLoopsRemaining);
	}

	private class InternalModifierListener implements IModifierListener<T> {
		@Override
		public void onModifierFinished(final IModifier<T> pModifier, final T pItem) {
			LoopModifier.this.onHandleLoopFinished(pItem);
		}
	}
}
