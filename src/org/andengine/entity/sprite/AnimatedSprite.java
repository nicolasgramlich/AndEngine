package org.andengine.entity.sprite;

import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.time.TimeConstants;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:25:46 - 10.03.2010
 */
public class AnimatedSprite extends TiledSprite {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int FRAMEINDEX_INVALID = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mAnimationRunning;
	private boolean mAnimationStartedFired;

	private int mCurrentFrameIndex;
	private long mAnimationProgress;
	private int mRemainingLoopCount;

	@SuppressWarnings("deprecation")
	private final IAnimationData mAnimationData = new AnimationData();
	private IAnimationListener mAnimationListener;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.DYNAMIC);
	}

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.DYNAMIC, pShaderProgram);
	}

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType);
	}

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pTiledTextureRegion, pTiledSpriteVertexBufferObject);
	}

	public AnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);
	}

	public AnimatedSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.DYNAMIC);
	}

	public AnimatedSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.DYNAMIC, pShaderProgram);
	}

	public AnimatedSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType);
	}

	public AnimatedSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
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
			final int loopCount = this.mAnimationData.getLoopCount();
			final int[] frames = this.mAnimationData.getFrames();
			final long animationDuration = this.mAnimationData.getAnimationDuration();

			if(!this.mAnimationStartedFired && (this.mAnimationProgress == 0)) {
				this.mAnimationStartedFired = true;
				if(frames == null) {
					this.setCurrentTileIndex(this.mAnimationData.getFirstFrameIndex());
				} else {
					this.setCurrentTileIndex(frames[0]);
				}
				this.mCurrentFrameIndex = 0;
				if(this.mAnimationListener != null) {
					this.mAnimationListener.onAnimationStarted(this, loopCount);
					this.mAnimationListener.onAnimationFrameChanged(this, AnimatedSprite.FRAMEINDEX_INVALID, 0);
				}
			}
			final long nanoSecondsElapsed = (long) (pSecondsElapsed * TimeConstants.NANOSECONDS_PER_SECOND);
			this.mAnimationProgress += nanoSecondsElapsed;

			if(loopCount == IAnimationData.LOOP_CONTINUOUS) {
				while(this.mAnimationProgress > animationDuration ) {
					this.mAnimationProgress -= animationDuration;
					if(this.mAnimationListener != null) {
						this.mAnimationListener.onAnimationLoopFinished(this, this.mRemainingLoopCount, loopCount);
					}
				}
			} else {
				while(this.mAnimationProgress > animationDuration) {
					this.mAnimationProgress -= animationDuration;
					this.mRemainingLoopCount--;
					if(this.mRemainingLoopCount < 0) {
						break;
					} else if(this.mAnimationListener != null) {
						this.mAnimationListener.onAnimationLoopFinished(this, this.mRemainingLoopCount, loopCount);
					}
				}
			}

			if((loopCount == IAnimationData.LOOP_CONTINUOUS) || (this.mRemainingLoopCount >= 0)) {
				final int newFrameIndex = this.mAnimationData.calculateCurrentFrameIndex(this.mAnimationProgress);

				if(this.mCurrentFrameIndex != newFrameIndex) {
					if(frames == null) {
						this.setCurrentTileIndex(this.mAnimationData.getFirstFrameIndex() + newFrameIndex);
					} else {
						this.setCurrentTileIndex(frames[newFrameIndex]);
					}
					if(this.mAnimationListener != null) {
						this.mAnimationListener.onAnimationFrameChanged(this, this.mCurrentFrameIndex, newFrameIndex);
					}
				}
				this.mCurrentFrameIndex = newFrameIndex;
			} else {
				this.mAnimationRunning = false;
				if(this.mAnimationListener != null) {
					this.mAnimationListener.onAnimationFinished(this);
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

	public void animate(final long pFrameDurationEach) {
		this.animate(pFrameDurationEach, null);
	}

	public void animate(final long pFrameDurationEach, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurationEach, this.getTileCount());

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long pFrameDurationEach, final boolean pLoop) {
		this.animate(pFrameDurationEach, pLoop, null);
	}

	public void animate(final long pFrameDurationEach, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurationEach, this.getTileCount(), pLoop);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long pFrameDurationEach, final int pLoopCount) {
		this.animate(pFrameDurationEach, pLoopCount, null);
	}

	public void animate(final long pFrameDurationEach, final int pLoopCount, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurationEach, this.getTileCount(), pLoopCount);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long[] pFrameDurations) {
		this.animate(pFrameDurations, (IAnimationListener)null);
	}

	public void animate(final long[] pFrameDurations, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long[] pFrameDurations, final boolean pLoop) {
		this.animate(pFrameDurations, pLoop, null);
	}

	public void animate(final long[] pFrameDurations, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pLoop);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long[] pFrameDurations, final int pLoopCount) {
		this.animate(pFrameDurations, pLoopCount, null);
	}

	public void animate(final long[] pFrameDurations, final int pLoopCount, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pLoopCount);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop) {
		this.animate(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoop, null);
	}

	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoop);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final int pLoopCount) {
		this.animate(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoopCount, null);
	}

	public void animate(final long[] pFrameDurations, final int pFirstTileIndex, final int pLastTileIndex, final int pLoopCount, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pFirstTileIndex, pLastTileIndex, pLoopCount);

		this.initAnimation(pAnimationListener);
	}

	/**
	 * Animate specifics frames.
	 * 
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames) {
		this.animate(pFrameDurations, pFrames, null);
	}

	/**
	 * Animate specifics frames.
	 * 
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pAnimationListener
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pFrames);

		this.initAnimation(pAnimationListener);
	}

	/**
	 * Animate specifics frames.
	 * 
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoop
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames, final boolean pLoop) {
		this.animate(pFrameDurations, pFrames, pLoop, null);
	}

	/**
	 * Animate specifics frames.
	 * 
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoop
	 * @param pAnimationListener
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames, final boolean pLoop, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pFrames, pLoop);

		this.initAnimation(pAnimationListener);
	}

	/**
	 * Animate specifics frames.
	 * 
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoopCount
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames, final int pLoopCount) {
		this.animate(pFrameDurations, pFrames, pLoopCount, null);
	}

	/**
	 * Animate specifics frames.
	 * 
	 * @param pFrameDurations must have the same length as pFrames.
	 * @param pFrames indices of the frames to animate.
	 * @param pLoopCount
	 * @param pAnimationListener
	 */
	public void animate(final long[] pFrameDurations, final int[] pFrames, final int pLoopCount, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pFrameDurations, pFrames, pLoopCount);

		this.initAnimation(pAnimationListener);
	}

	public void animate(final IAnimationData pAnimationData) {
		this.animate(pAnimationData, null);
	}

	public void animate(final IAnimationData pAnimationData, final IAnimationListener pAnimationListener) {
		this.mAnimationData.set(pAnimationData);

		this.initAnimation(pAnimationListener);
	}

	private void initAnimation(final IAnimationListener pAnimationListener) {
		this.mAnimationStartedFired = false;
		this.mAnimationListener = pAnimationListener;
		this.mRemainingLoopCount = this.mAnimationData.getLoopCount();

		this.mAnimationProgress = 0;
		this.mAnimationRunning = true;
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

		/**
		 * @param pAnimatedSprite
		 * @param pInitialLoopCount is {@link AnimatedSprite#LOOP_CONTINUOUS} when {@link AnimatedSprite} loops infinitely.
		 */
		public void onAnimationStarted(final AnimatedSprite pAnimatedSprite, final int pInitialLoopCount);
		/**
		 * @param pAnimatedSprite
		 * @param pOldFrameIndex equals {@link AnimatedSprite#FRAMEINDEX_INVALID}, the first time {@link IAnimationListener#onAnimationFrameChanged(AnimatedSprite, int, int)} is called.
		 * @param pNewFrameIndex the new frame index of the currently active animation.
		 */
		public void onAnimationFrameChanged(final AnimatedSprite pAnimatedSprite, final int pOldFrameIndex, final int pNewFrameIndex);
		/**
		 * @param pAnimatedSprite
		 * @param pRemainingLoopCount is {@link AnimatedSprite#LOOP_CONTINUOUS} when {@link AnimatedSprite} loops infinitely.
		 * @param pInitialLoopCount is {@link AnimatedSprite#LOOP_CONTINUOUS} when {@link AnimatedSprite} loops infinitely.
		 */
		public void onAnimationLoopFinished(final AnimatedSprite pAnimatedSprite, final int pRemainingLoopCount, final int pInitialLoopCount);
		public void onAnimationFinished(final AnimatedSprite pAnimatedSprite);
	}
}
