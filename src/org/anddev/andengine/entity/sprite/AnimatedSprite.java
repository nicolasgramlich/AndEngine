package org.anddev.andengine.entity.sprite;

import java.util.Arrays;

import org.anddev.andengine.opengl.texture.TiledTextureRegion;
import org.anddev.andengine.util.MathUtils;
import org.anddev.andengine.util.constants.TimeConstants;

/**
 * @author Nicolas Gramlich
 * @since 15:25:46 - 10.03.2010
 */
public class AnimatedSprite extends TiledSprite implements TimeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mAnimationRunning;
	private long mFrameStart;

	private int[] mFrameEndsInMilliseconds;
	private long mAnimationDuration;

	private int mFirstTileIndex;
	private long mAnimationStart;
	private boolean mLoop;
	private IAnimationListener mAnimationListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimatedSprite(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isAnimationRunning() {
		return this.mAnimationRunning;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if(this.mAnimationRunning) {
			final long now = System.nanoTime();
			final long elapsedSinceFrameStart = now - this.mFrameStart;
			final long elapsedNanosecondsInFrame = elapsedSinceFrameStart % this.mAnimationDuration;
			final int currentFrameIndex = this.determineCurrentFrameIndex((int)(elapsedNanosecondsInFrame / NANOSECONDSPERMILLISECOND));
			this.setCurrentTileIndex(this.mFirstTileIndex + currentFrameIndex);

			this.mFrameStart = now - elapsedNanosecondsInFrame;
			if(!this.mLoop) {
				this.stopAnimationIfDurationExceeded(now);
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void stopAnimation() {
		this.mAnimationRunning = false;
	}
	
	public void stopAnimation(final int pTileIndex) {
		this.mAnimationRunning = false;
		this.setCurrentTileIndex(pTileIndex);
	}

	private void stopAnimationIfDurationExceeded(final long now) {
		if(now - this.mAnimationStart > this.mAnimationDuration){
			this.mAnimationRunning = false;
			if(this.mAnimationListener != null)
				this.mAnimationListener.onAnimationEnd(this);
		}
	}

	private int determineCurrentFrameIndex(final int pElapsedRelativeInFrame) {
		final int[] frameEnds = this.mFrameEndsInMilliseconds;
		final int frameCount = frameEnds.length;
		for(int i = 0; i < frameCount; i++) {
			if(frameEnds[i] > pElapsedRelativeInFrame) {
				return i;
			}
		}

		return frameCount - 1;
	}

	public void animate(final int pFrameDurationEach) {
		this.animate(pFrameDurationEach, true);
	}
	
	public void animate(final int pFrameDurationEach, final boolean pLoop) {
		this.animate(pFrameDurationEach, pLoop, null);
	}
	
	public void animate(final int pFrameDurationEach, final boolean pLoop, final IAnimationListener pAnimationListener) {
		final int[] frameDurations = new int[this.getTextureRegion().getTileCount() - 1];
		Arrays.fill(frameDurations, pFrameDurationEach);
		this.animate(frameDurations, pLoop, pAnimationListener);
	}
	
	public void animate(final int[] pFrameDurations) {
		this.animate(pFrameDurations, true);
	}
	
	public void animate(final int[] pFrameDurations, final boolean pLoop) {
		this.animate(pFrameDurations, pLoop, null);
	}
	
	public void animate(final int[] pFrameDurations, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.animate(pFrameDurations, 0, this.getTextureRegion().getTileCount() - 1, pLoop, pAnimationListener);
	}
	
	public void animate(final int[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop) {
		animate(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoop, null);
	}

	/**
	 * @param pFrameDurations must have the same length as the tile-indexes specify.
	 * @param pFirstTileIndex
	 * @param pLastTileIndex
	 */
	public void animate(final int[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop, final IAnimationListener pAnimationListener) {
		assert(pLastTileIndex - pFirstTileIndex >= 2);
		assert(pFrameDurations.length == (pLastTileIndex - pFirstTileIndex) - 1);
		
		this.mAnimationListener = pAnimationListener;

		this.mLoop = pLoop;
		this.mFirstTileIndex = pFirstTileIndex;

		MathUtils.arraySumInternal(pFrameDurations);
		this.mFrameEndsInMilliseconds = pFrameDurations;

		final long lastFrameEnd = this.mFrameEndsInMilliseconds[this.mFrameEndsInMilliseconds.length - 1];
		this.mAnimationDuration = lastFrameEnd * NANOSECONDSPERMILLISECOND;

		this.mAnimationStart = System.nanoTime();
		this.mFrameStart = this.mAnimationStart;
		this.mAnimationRunning = true;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public static interface IAnimationListener {
		public void onAnimationEnd(final AnimatedSprite pAnimatedSprite);
	}
}
