package org.andengine.entity.util;

import org.andengine.BuildConfig;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.util.debug.Debug;
import org.andengine.util.debug.Debug.DebugLevel;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Arturo Gutiérrez
 * @author Nicolas Gramlich
 * @since 22:07:46 - 03.03.2013
 */
public class TextureMemoryLogger implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float AVERAGE_DURATION_DEFAULT = 5;

	// ===========================================================
	// Fields
	// ===========================================================

	private final TextureManager mTextureManager;

	private final float mAverageDuration;
	private float mSecondsElapsed;

	private int mTextureMemoryUsedMax;
	private int mTexturesLoadedCountMax;

	private final DebugLevel mDebugLevel;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureMemoryLogger(final TextureManager pTextureManager) {
		this(pTextureManager, DebugLevel.DEBUG);
	}

	public TextureMemoryLogger(final TextureManager pTextureManager, final DebugLevel pDebugLevel) {
		this(pTextureManager, TextureMemoryLogger.AVERAGE_DURATION_DEFAULT);
	}

	public TextureMemoryLogger(final TextureManager pTextureManager, final float pAverageDuration) {
		this(pTextureManager, pAverageDuration, DebugLevel.DEBUG);
	}

	public TextureMemoryLogger(final TextureManager pTextureManager, final float pAverageDuration, final DebugLevel pDebugLevel) {
		this.mTextureManager = pTextureManager;
		this.mAverageDuration = pAverageDuration;
		this.mDebugLevel = pDebugLevel;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mSecondsElapsed += pSecondsElapsed;

		if (this.mSecondsElapsed > this.mAverageDuration) {
			this.onHandleAverageDurationElapsed();

			this.mSecondsElapsed -= this.mAverageDuration;
		}
	}

	@Override
	public void reset() {
		this.mSecondsElapsed = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void onHandleAverageDurationElapsed() {
		if (BuildConfig.DEBUG) {
			final int texturesLoadedCount = this.mTextureManager.getTexturesLoadedCount();
			final int textureMemoryUsed = this.mTextureManager.getTextureMemoryUsed();

			this.mTexturesLoadedCountMax = Math.max(texturesLoadedCount, this.mTexturesLoadedCountMax);
			this.mTextureMemoryUsedMax = Math.max(textureMemoryUsed, this.mTextureMemoryUsedMax);

			Debug.log(this.mDebugLevel, String.format("MEM: %d kB in %d textures (MAX: %d kB in %d textures)", textureMemoryUsed, texturesLoadedCount, this.mTextureMemoryUsedMax, this.mTexturesLoadedCountMax));
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}