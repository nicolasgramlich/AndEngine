package org.andengine.entity.sprite;

import java.util.Arrays;

import org.andengine.util.adt.array.ArrayUtils;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;
import org.andengine.util.time.TimeConstants;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:43:01 - 04.05.2012
 */
public class AnimationData implements IAnimationData {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mFrameCount;
	private int[] mFrames;
	private long[] mFrameDurations;
	private int mFirstFrameIndex;

	private int mLoopCount;
	private long[] mFrameEndsInNanoseconds;

	private long mAnimationDuration;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Deprecated
	public AnimationData() {

	}

	public AnimationData(final long pFrameDurationEach, final int pFrameCount) {
		this.set(pFrameDurationEach, pFrameCount);
	}

	public AnimationData(final long pFrameDurationEach, final int pFrameCount, final boolean pLoop) {
		this.set(pFrameDurationEach, pFrameCount, pLoop);
	}

	public AnimationData(final long pFrameDurationEach, final int pFrameCount, final int pLoopCount) {
		this.set(pFrameDurationEach, pFrameCount, pLoopCount);
	}

	public AnimationData(final long[] pFrameDurations) {
		this.set(pFrameDurations);
	}

	public AnimationData(final long[] pFrameDurations, final boolean pLoop) {
		this.set(pFrameDurations, pLoop);
	}

	public AnimationData(final long[] pFrameDurations, final int pLoopCount) {
		this.set(pFrameDurations, pLoopCount);
	}

	public AnimationData(final long[] pFrameDurations, final int pFirstFrameIndex, final int pLastFrameIndex, final boolean pLoop) {
		this.set(pFrameDurations, pFirstFrameIndex, pLastFrameIndex, pLoop);
	}

	/**
	 * Animate specifics frames.
	 *
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoopCount
	 */
	public AnimationData(final long[] pFrameDurations, final int[] pFrames, final int pLoopCount) {
		this.set(pFrameDurations, pFrames, pLoopCount);
	}

	/**
	 * @param pFrameDurations must have the same length as pFirstFrameIndex to pLastFrameIndex.
	 * @param pFirstFrameIndex
	 * @param pLastFrameIndex
	 * @param pLoopCount
	 */
	public AnimationData(final long[] pFrameDurations, final int pFirstFrameIndex, final int pLastFrameIndex, final int pLoopCount) {
		this.set(pFrameDurations, pFirstFrameIndex, pLastFrameIndex, pLoopCount);
	}

	public AnimationData(final IAnimationData pAnimationData) {
		this.set(pAnimationData);
	}

	@Override
	public IAnimationData deepCopy() throws DeepCopyNotSupportedException {
		return new AnimationData(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setLoopCount(final int pLoopCount) {
		this.mLoopCount = pLoopCount;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int[] getFrames() {
		return this.mFrames;
	}

	@Override
	public long[] getFrameDurations() {
		return this.mFrameDurations;
	}

	@Override
	public int getLoopCount() {
		return this.mLoopCount;
	}

	@Override
	public int getFrameCount() {
		return this.mFrameCount;
	}

	@Override
	public int getFirstFrameIndex() {
		return this.mFirstFrameIndex;
	}

	@Override
	public long getAnimationDuration() {
		return this.mAnimationDuration;
	}

	/**
	 * @param pAnimationProgress in milliseconds.
	 * @return
	 */
	@Override
	public int calculateCurrentFrameIndex(final long pAnimationProgress) {
		final long[] frameEnds = this.mFrameEndsInNanoseconds;
		final int frameCount = this.mFrameCount;
		for (int i = 0; i < frameCount; i++) {
			if (frameEnds[i] > pAnimationProgress) {
				return i;
			}
		}

		return frameCount - 1;
	}

	@Override
	public void set(final long pFrameDurationEach, final int pFrameCount) {
		this.set(pFrameDurationEach, pFrameCount, true);
	}

	@Override
	public void set(final long pFrameDurationEach, final int pFrameCount, final boolean pLoop) {
		this.set(pFrameDurationEach, pFrameCount, (pLoop) ? IAnimationData.LOOP_CONTINUOUS : 0);
	}

	@Override
	public void set(final long pFrameDurationEach, final int pFrameCount, final int pLoopCount) {
		this.set(AnimationData.fillFrameDurations(pFrameDurationEach, pFrameCount), pLoopCount);
	}

	@Override
	public void set(final long[] pFrameDurations) {
		this.set(pFrameDurations, true);
	}

	@Override
	public void set(final long[] pFrameDurations, final boolean pLoop) {
		this.set(pFrameDurations, (pLoop) ? IAnimationData.LOOP_CONTINUOUS : 0);
	}

	@Override
	public void set(final long[] pFrameDurations, final int pLoopCount) {
		this.set(pFrameDurations, 0, pFrameDurations.length - 1, pLoopCount);
	}

	@Override
	public void set(final long[] pFrameDurations, final int pFirstFrameIndex, final int pLastFrameIndex) {
		this.set(pFrameDurations, pFirstFrameIndex, pLastFrameIndex, true);
	}

	@Override
	public void set(final long[] pFrameDurations, final int pFirstFrameIndex, final int pLastFrameIndex, final boolean pLoop) {
		this.set(pFrameDurations, pFirstFrameIndex, pLastFrameIndex, (pLoop) ? IAnimationData.LOOP_CONTINUOUS : 0);
	}

	/**
	 * @param pFrameDurations must have the same length as pFirstFrameIndex to pLastFrameIndex.
	 * @param pFirstFrameIndex
	 * @param pLastFrameIndex
	 * @param pLoopCount
	 */
	@Override
	public void set(final long[] pFrameDurations, final int pFirstFrameIndex, final int pLastFrameIndex, final int pLoopCount) {
		this.set(pFrameDurations, (pLastFrameIndex - pFirstFrameIndex) + 1, null, pFirstFrameIndex, pLoopCount);

		if ((pFirstFrameIndex + 1) > pLastFrameIndex) {
			throw new IllegalArgumentException("An animation needs at least two tiles to animate between.");
		}
	}

	/**
	 * Animate specifics frames.
	 *
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 */
	@Override
	public void set(final long[] pFrameDurations, final int[] pFrames) {
		this.set(pFrameDurations, pFrames, true);
	}

	/**
	 * Animate specifics frames.
	 *
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoop
	 */
	@Override
	public void set(final long[] pFrameDurations, final int[] pFrames, final boolean pLoop) {
		this.set(pFrameDurations, pFrames, (pLoop) ? IAnimationData.LOOP_CONTINUOUS : 0);
	}

	/**
	 * Animate specifics frames.
	 *
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoopCount
	 */
	@Override
	public void set(final long[] pFrameDurations, final int[] pFrames, final int pLoopCount) {
		this.set(pFrameDurations, pFrames.length, pFrames, 0, pLoopCount);
	}

	@Override
	public void set(final IAnimationData pAnimationData) {
		this.set(pAnimationData.getFrameDurations(), pAnimationData.getFrameCount(), pAnimationData.getFrames(), pAnimationData.getFirstFrameIndex(), pAnimationData.getLoopCount());
	}

	private void set(final long[] pFrameDurations, final int pFrameCount, final int[] pFrames, final int pFirstFrameIndex, final int pLoopCount) {
		if (pFrameDurations.length != pFrameCount) {
			throw new IllegalArgumentException("pFrameDurations does not equal pFrameCount!");
		}

		this.mFrameDurations = pFrameDurations;
		this.mFrameCount = pFrameCount;
		this.mFrames = pFrames;
		this.mFirstFrameIndex = pFirstFrameIndex;
		this.mLoopCount = pLoopCount;

		if ((this.mFrameEndsInNanoseconds == null) || (this.mFrameCount > this.mFrameEndsInNanoseconds.length)) {
			this.mFrameEndsInNanoseconds = new long[this.mFrameCount];
		}

		final long[] frameEndsInNanoseconds = this.mFrameEndsInNanoseconds;
		ArrayUtils.sumCummulative(this.mFrameDurations, TimeConstants.NANOSECONDS_PER_MILLISECOND, frameEndsInNanoseconds);

		final long lastFrameEnd = frameEndsInNanoseconds[this.mFrameCount - 1];
		this.mAnimationDuration = lastFrameEnd;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Scales the duration of this {@link AnimationData} by a given <code>pFactor</code>.
	 *
	 * @param pScale
	 */
	public void scale(final float pScale) {
		ArrayUtils.multiply(this.mFrameDurations, pScale);
		ArrayUtils.multiply(this.mFrameEndsInNanoseconds, pScale);
		this.mAnimationDuration *= pScale;
	}

	private static long[] fillFrameDurations(final long pFrameDurationEach, final int pFrameCount) {
		final long[] frameDurations = new long[pFrameCount];
		Arrays.fill(frameDurations, pFrameDurationEach);
		return frameDurations;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
