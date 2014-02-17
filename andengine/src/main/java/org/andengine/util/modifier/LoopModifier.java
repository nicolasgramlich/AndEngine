package org.andengine.util.modifier;

import org.andengine.util.modifier.IModifier.IModifierListener;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:18:37 - 03.09.2010
 * @param <T>
 */
public class LoopModifier<T> extends BaseModifier<T> implements IModifierListener<T> {
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

	private boolean mModifierStartedCalled;
	private boolean mFinishedCached;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LoopModifier(final IModifier<T> pModifier) {
		this(pModifier, LoopModifier.LOOP_CONTINUOUS);
	}

	public LoopModifier(final IModifier<T> pModifier, final int pLoopCount) {
		this(pModifier, pLoopCount, null, (IModifierListener<T>)null);
	}

	public LoopModifier(final IModifier<T> pModifier, final int pLoopCount, final IModifierListener<T> pModifierListener) {
		this(pModifier, pLoopCount, null, pModifierListener);
	}

	public LoopModifier(final IModifier<T> pModifier, final int pLoopCount, final ILoopModifierListener<T> pLoopModifierListener) {
		this(pModifier, pLoopCount, pLoopModifierListener, (IModifierListener<T>)null);
	}

	public LoopModifier(final IModifier<T> pModifier, final int pLoopCount, final ILoopModifierListener<T> pLoopModifierListener, final IModifierListener<T> pModifierListener) {
		super(pModifierListener);

		BaseModifier.assertNoNullModifier(pModifier);

		this.mModifier = pModifier;
		this.mLoopCount = pLoopCount;
		this.mLoopModifierListener = pLoopModifierListener;

		this.mLoop = 0;
		this.mDuration = (pLoopCount == LoopModifier.LOOP_CONTINUOUS) ? Float.POSITIVE_INFINITY : pModifier.getDuration() * pLoopCount; // TODO Check if POSITIVE_INFINITY works correct with i.e. SequenceModifier

		this.mModifier.addModifierListener(this);
	}

	protected LoopModifier(final LoopModifier<T> pLoopModifier) throws DeepCopyNotSupportedException {
		this(pLoopModifier.mModifier.deepCopy(), pLoopModifier.mLoopCount);
	}

	@Override
	public LoopModifier<T> deepCopy() throws DeepCopyNotSupportedException {
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

			this.mFinishedCached = false;
			while((secondsElapsedRemaining > 0) && !this.mFinishedCached) {
				secondsElapsedRemaining -= this.mModifier.onUpdate(secondsElapsedRemaining, pItem);
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
		this.mLoop = 0;
		this.mSecondsElapsed = 0;
		this.mModifierStartedCalled = false;

		this.mModifier.reset();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void onModifierStarted(final IModifier<T> pModifier, final T pItem) {
		if(!this.mModifierStartedCalled) {
			this.mModifierStartedCalled = true;
			this.onModifierStarted(pItem);
		}
		if(this.mLoopModifierListener != null) {
			this.mLoopModifierListener.onLoopStarted(this, this.mLoop, this.mLoopCount);
		}
	}

	@Override
	public void onModifierFinished(final IModifier<T> pModifier, final T pItem) {
		if(this.mLoopModifierListener != null) {
			this.mLoopModifierListener.onLoopFinished(this, this.mLoop, this.mLoopCount);
		}

		if(this.mLoopCount == LoopModifier.LOOP_CONTINUOUS) {
			this.mSecondsElapsed = 0;
			this.mModifier.reset();
		} else {
			this.mLoop++;
			if(this.mLoop >= this.mLoopCount) {
				this.mFinished = true;
				this.mFinishedCached = true;
				this.onModifierFinished(pItem);
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
}
