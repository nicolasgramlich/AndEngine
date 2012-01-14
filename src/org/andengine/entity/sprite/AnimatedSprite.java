package org.andengine.entity.sprite;

import java.util.Arrays;

import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.util.constants.TimeConstants;
import org.andengine.util.math.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:25:46 - 10.03.2010
 */
public class AnimatedSprite extends TiledSprite implements TimeConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int LOOP_CONTINUOUS = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mAnimationRunning;

	private long mAnimationProgress;
	private long mAnimationDuration;
	private long[] mFrameEndsInNanoseconds;

	private int mFirstTileIndex;
	private int mInitialLoopCount;
	private int mLoopCount;
	private IAnimationListener mAnimationListener;

	private int mFrameCount;
	private int[] mFrames;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion, DrawType.DYNAMIC);
	}

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTiledTextureRegion, DrawType.DYNAMIC, pShaderProgram);
	}

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType) {
		super(pX, pY, pTiledTextureRegion, pDrawType);
	}

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTiledTextureRegion, pDrawType, pShaderProgram);
	}

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pTiledTextureRegion, pTiledSpriteVertexBufferObject);
	}

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);
	}

	public AnimatedSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, DrawType.DYNAMIC);
	}

	public AnimatedSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, DrawType.DYNAMIC, pShaderProgram);
	}

	public AnimatedSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pDrawType);
	}

	public AnimatedSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pDrawType, pShaderProgram);
	}

	public AnimatedSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public AnimatedSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);
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
			final long nanoSecondsElapsed = (long) (pSecondsElapsed * TimeConstants.NANOSECONDS_PER_SECOND);
			this.mAnimationProgress += nanoSecondsElapsed;

			if(this.mAnimationProgress > this.mAnimationDuration) {
				this.mAnimationProgress %= this.mAnimationDuration;
				if(this.mInitialLoopCount != AnimatedSprite.LOOP_CONTINUOUS) {
					this.mLoopCount--;
				}
			}

			if(this.mInitialLoopCount == AnimatedSprite.LOOP_CONTINUOUS || this.mLoopCount >= 0) {
				final int currentFrameIndex = this.calculateCurrentFrameIndex();

				if(this.mFrames == null) {
					this.setCurrentTileIndex(this.mFirstTileIndex + currentFrameIndex);
				} else {
					this.setCurrentTileIndex(this.mFrames[currentFrameIndex]);
				}
				this.mAnimationListener.onPerformedFrameAnimation(currentFrameIndex);
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
		final long[] frameEnds = this.mFrameEndsInNanoseconds;
		final int frameCount = this.mFrameCount;
		for(int i = 0; i < frameCount; i++) {
			if(frameEnds[i] > animationProgress) {
				return i;
			}
		}

		return frameCount - 1;
	}

	public AnimatedSprite animate(final long pFrameDurationEach) {
		return this.animate(pFrameDurationEach, true);
	}

	public AnimatedSprite animate(final long pFrameDurationEach, final boolean pLoop) {
		return this.animate(pFrameDurationEach, (pLoop) ? AnimatedSprite.LOOP_CONTINUOUS : 0, null);
	}

	public AnimatedSprite animate(final long pFrameDurationEach, final int pLoopCount) {
		return this.animate(pFrameDurationEach, pLoopCount, null);
	}

	public AnimatedSprite animate(final long pFrameDurationEach, final boolean pLoop, final IAnimationListener pAnimationListener) {
		return this.animate(pFrameDurationEach, (pLoop) ? AnimatedSprite.LOOP_CONTINUOUS : 0, pAnimationListener);
	}

	public AnimatedSprite animate(final long pFrameDurationEach, final int pLoopCount, final IAnimationListener pAnimationListener) {
		final long[] frameDurations = new long[this.getTextureRegion().getTileCount()];
		Arrays.fill(frameDurations, pFrameDurationEach);
		return this.animate(frameDurations, pLoopCount, pAnimationListener);
	}

	public AnimatedSprite animate(final long[] pFrameDurations) {
		return this.animate(pFrameDurations, true);
	}

	public AnimatedSprite animate(final long[] pFrameDurations, final boolean pLoop) {
		return this.animate(pFrameDurations, (pLoop) ? AnimatedSprite.LOOP_CONTINUOUS : 0, null);
	}

	public AnimatedSprite animate(final long[] pFrameDurations, final int pLoopCount) {
		return this.animate(pFrameDurations, pLoopCount, null);
	}

	public AnimatedSprite animate(final long[] pFrameDurations, final boolean pLoop, final IAnimationListener pAnimationListener) {
		return this.animate(pFrameDurations, (pLoop) ? AnimatedSprite.LOOP_CONTINUOUS : 0, pAnimationListener);
	}

	public AnimatedSprite animate(final long[] pFrameDurations, final int pLoopCount, final IAnimationListener pAnimationListener) {
		return this.animate(pFrameDurations, 0, this.getTextureRegion().getTileCount() - 1, pLoopCount, pAnimationListener);
	}

	public AnimatedSprite animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop) {
		return this.animate(pFrameDurations, pFirstTileIndex, pLastTileIndex, (pLoop) ? AnimatedSprite.LOOP_CONTINUOUS : 0, null);
	}

	public AnimatedSprite animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final int pLoopCount) {
		return this.animate(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoopCount, null);
	}

	public AnimatedSprite animate(final long[] pFrameDurations, final int[] pFrames, final int pLoopCount) {
		return this.animate(pFrameDurations, pFrames, pLoopCount, null);
	}

	/**
	 * Animate specifics frames
	 * 
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoopCount
	 * @param pAnimationListener
	 */
	public AnimatedSprite animate(final long[] pFrameDurations, final int[] pFrames, final int pLoopCount, final IAnimationListener pAnimationListener) {
		final int frameCount = pFrames.length;
		if(pFrameDurations.length != frameCount) {
			throw new IllegalArgumentException("pFrameDurations must have the same length as pFrames.");
		}

		return this.init(pFrameDurations, frameCount, pFrames, 0, pLoopCount, pAnimationListener);
	}

	/**
	 * @param pFrameDurations
	 *            must have the same length as pFirstTileIndex to
	 *            pLastTileIndex.
	 * @param pFirstTileIndex
	 * @param pLastTileIndex
	 * @param pLoopCount
	 * @param pAnimationListener
	 */
	public AnimatedSprite animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final int pLoopCount, final IAnimationListener pAnimationListener) {
		if(pLastTileIndex - pFirstTileIndex < 1) {
			throw new IllegalArgumentException("An animation needs at least two tiles to animate between.");
		}

		final int frameCount = (pLastTileIndex - pFirstTileIndex) + 1;
		if(pFrameDurations.length != frameCount) {
			throw new IllegalArgumentException("pFrameDurations must have the same length as pFirstTileIndex to pLastTileIndex.");
		}

		return this.init(pFrameDurations, frameCount, null, pFirstTileIndex, pLoopCount, pAnimationListener);
	}

	private AnimatedSprite init(final long[] pFrameDurations, final int frameCount, final int[] pFrames, final int pFirstTileIndex, final int pLoopCount, final IAnimationListener pAnimationListener) {
		this.mFrameCount = frameCount;
		this.mAnimationListener = pAnimationListener;
		this.mInitialLoopCount = pLoopCount;
		this.mLoopCount = pLoopCount;
		this.mFrames = pFrames;
		this.mFirstTileIndex = pFirstTileIndex;

		if(this.mFrameEndsInNanoseconds == null || this.mFrameCount > this.mFrameEndsInNanoseconds.length) {
			this.mFrameEndsInNanoseconds = new long[this.mFrameCount];
		}

		final long[] frameEndsInNanoseconds = this.mFrameEndsInNanoseconds;
		MathUtils.arraySumInto(pFrameDurations, frameEndsInNanoseconds, TimeConstants.NANOSECONDS_PER_MILLISECOND);

		final long lastFrameEnd = frameEndsInNanoseconds[this.mFrameCount - 1];
		this.mAnimationDuration = lastFrameEnd;

		this.mAnimationProgress = 0;
		this.mAnimationRunning = true;

		return this;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IAnimationListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		public void onPerformedFrameAnimation(final int pPerformedFrame);
		public void onAnimationEnd(final AnimatedSprite pAnimatedSprite);
	}
}
