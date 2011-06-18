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

	private float mSecondsElapsed;
	private final float mDuration;

	private final IModifier<T> mModifier;

	private ILoopModifierListener<T> mLoopModifierListener;

	private final int mLoopCount;
	private int mLoop;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LoopModifier(final IModifier<T> pModifier) {
		this(pModifier, LOOP_CONTINUOUS, null, null);
	}

	public LoopModifier(final IModifier<T> pModifier, final int pLoopCount) {
		this(pModifier, pLoopCount, null, null);
	}

	public LoopModifier(final IModifier<T> pModifier, final int pLoopCount, final IModifierListener<T> pModifierListener) {
		this(pModifier, pLoopCount, null, pModifierListener);
	}

	public LoopModifier(final IModifier<T> pModifier, final int pLoopCount, final ILoopModifierListener<T> pLoopModifierListener) {
		this(pModifier, pLoopCount, pLoopModifierListener, null);
	}

	public LoopModifier(final IModifier<T> pModifier, final int pLoopCount, final ILoopModifierListener<T> pLoopModifierListener, final IModifierListener<T> pModifierListener) {
		super(pModifierListener);
		this.mModifier = pModifier;
		this.mLoopCount = pLoopCount;
		this.mLoopModifierListener = pLoopModifierListener;

		this.mLoop = 0;
		this.mDuration = pLoopCount == LOOP_CONTINUOUS ? Float.POSITIVE_INFINITY : pModifier.getDuration() * pLoopCount; // TODO Check if POSITIVE_INFINITY works correct with i.e. SequenceModifier

		pModifier.setModifierListener(new InternalModifierListener());
	}

	protected LoopModifier(final LoopModifier<T> pLoopModifier) throws CloneNotSupportedException {
		this(pLoopModifier.mModifier.clone(), pLoopModifier.mLoopCount, pLoopModifier.mModifierListener);
	}

	@Override
	public LoopModifier<T> clone() throws CloneNotSupportedException{
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
			while(secondsElapsedRemaining > 0 && !this.mFinished) {
				secondsElapsedRemaining -= this.mModifier.onUpdate(secondsElapsedRemaining, pItem);
			}
			
			final float secondsElapsedUsed = pSecondsElapsed - secondsElapsedRemaining;
			this.mSecondsElapsed += secondsElapsedUsed;
			return secondsElapsedUsed;
		}
	}

	@Override
	public void reset() {
		this.mLoop = 0;
		this.mSecondsElapsed = 0;

		this.mModifier.reset();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleLoopStarted(final T pItem) {
		if(this.mLoopModifierListener != null) {
			this.mLoopModifierListener.onLoopStarted(this, this.mLoop, this.mLoopCount);
		}
	}

	public void onHandleLoopFinished(final T pItem) {
		if(this.mLoopModifierListener != null) {
			this.mLoopModifierListener.onLoopFinished(this, this.mLoop, this.mLoopCount);
		}

		if(this.mLoopCount == LOOP_CONTINUOUS) {
			this.mSecondsElapsed = 0;
			this.mModifier.reset();
		} else {
			this.mLoop++;
			if(this.mLoop >= this.mLoopCount) {
				this.mFinished = true;
				if(this.mModifierListener != null) {
					this.mModifierListener.onModifierFinished(this, pItem);
				}
			} else {
				this.mSecondsElapsed = 0;
				this.mModifier.reset();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface ILoopModifierListener<T> {
		public void onLoopStarted(final LoopModifier<T> pLoopModifier, final int pLoop, final int pLoopCount);
		public void onLoopFinished(final LoopModifier<T> pLoopModifier, final int pLoop, final int pLoopCount);
	}

	private class InternalModifierListener implements IModifierListener<T> {
		@Override
		public void onModifierStarted(final IModifier<T> pModifier, final T pItem) {
			LoopModifier.this.onHandleLoopStarted(pItem);
		}

		@Override
		public void onModifierFinished(final IModifier<T> pModifier, final T pItem) {
			LoopModifier.this.onHandleLoopFinished(pItem);
		}
	}
}
