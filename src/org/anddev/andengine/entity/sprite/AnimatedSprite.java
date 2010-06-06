package org.anddev.andengine.entity.sprite;

import java.util.Arrays;

import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;
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

	private long mAnimationProgress;
	private long mAnimationDuration;
	private long[] mFrameEnds;

	private int mFirstTileIndex;
	private int mInitialLoopCount;
	private int mLoopCount;
	private IAnimationListener mAnimationListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimatedSprite(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion);
	}

	public AnimatedSprite(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final RectangleVertexBuffer pRectangleVertexBuffer) {
		super(pX, pY, pTiledTextureRegion, pRectangleVertexBuffer);
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
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if(this.mAnimationRunning) {
			final long nanoSecondsElapsed = (long)(pSecondsElapsed * NANOSECONDSPERSECOND);
			this.mAnimationProgress += nanoSecondsElapsed;

			if(this.mAnimationProgress > this.mAnimationDuration) {
				this.mAnimationProgress %= this.mAnimationDuration;
				this.mLoopCount--;
			}

			if(this.mInitialLoopCount == -1 || this.mLoopCount >= 0) {
				final int currentFrameIndex = this.calculateCurrentFrameIndex();
				this.setCurrentTileIndex(this.mFirstTileIndex + currentFrameIndex);
			} else {
				this.mAnimationRunning = false;
				if(this.mAnimationListener != null) {
					this.mAnimationListener.onAnimationEnd(this);
				}
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

	private int calculateCurrentFrameIndex() {
		final long animationProgress = this.mAnimationProgress;
		final long[] frameEnds = this.mFrameEnds;
		final int frameCount = frameEnds.length;
		for(int i = 0; i < frameCount; i++) {
			if(frameEnds[i] > animationProgress) {
				return i;
			}
		}

		return frameCount - 1;
	}

	public void animate(final long pFrameDurationEach) {
		this.animate(pFrameDurationEach, true);
	}

	public void animate(final long pFrameDurationEach, final boolean pLoop) {
		this.animate(pFrameDurationEach, (pLoop) ? -1 : 0, null);
	}

	public void animate(final long pFrameDurationEach, final int pLoopCount) {
		this.animate(pFrameDurationEach, pLoopCount, null);
	}

	public void animate(final long pFrameDurationEach, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.animate(pFrameDurationEach, (pLoop) ? -1 : 0, pAnimationListener);
	}

	public void animate(final long pFrameDurationEach, final int pLoopCount, final IAnimationListener pAnimationListener) {
		final long[] frameDurations = new long[this.getTextureRegion().getTileCount()];
		Arrays.fill(frameDurations, pFrameDurationEach);
		this.animate(frameDurations, pLoopCount, pAnimationListener);
	}

	public void animate(final long[] pFrameDurations) {
		this.animate(pFrameDurations, true);
	}

	public void animate(final long[] pFrameDurations, final boolean pLoop) {
		this.animate(pFrameDurations, (pLoop) ? -1 : 0, null);
	}

	public void animate(final long[] pFrameDurations, final int pLoopCount) {
		this.animate(pFrameDurations, pLoopCount, null);
	}

	public void animate(final long[] pFrameDurations, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.animate(pFrameDurations, (pLoop) ? -1 : 0, pAnimationListener);
	}

	public void animate(final long[] pFrameDurations, final int pLoopCount, final IAnimationListener pAnimationListener) {
		this.animate(pFrameDurations, 0, this.getTextureRegion().getTileCount() - 1, pLoopCount, pAnimationListener);
	}

	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop) {
		this.animate(pFrameDurations, pFirstTileIndex, pLastTileIndex, (pLoop) ? -1 : 0, null);
	}

	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final int pLoopCount) {
		this.animate(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoopCount, null);
	}

	/**
	 * @param pFrameDurations must have the same length as the tile-indexes specify.
	 * @param pFirstTileIndex
	 * @param pLastTileIndex
	 */
	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final int pLoopCount, final IAnimationListener pAnimationListener) {
		assert(pLastTileIndex - pFirstTileIndex >= 2);
		assert(pFrameDurations.length == (pLastTileIndex - pFirstTileIndex) - 1);

		this.mAnimationListener = pAnimationListener;

		this.mInitialLoopCount = pLoopCount;
		this.mLoopCount = pLoopCount;
		this.mFirstTileIndex = pFirstTileIndex;

		MathUtils.arraySumInternal(pFrameDurations, NANOSECONDSPERMILLISECOND);
		this.mFrameEnds = pFrameDurations;

		final long lastFrameEnd = this.mFrameEnds[this.mFrameEnds.length - 1];
		this.mAnimationDuration = lastFrameEnd;

		this.mAnimationProgress = 0;
		this.mAnimationRunning = true;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IAnimationListener {
		public void onAnimationEnd(final AnimatedSprite pAnimatedSprite);
	}
}
