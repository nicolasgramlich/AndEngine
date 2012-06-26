package org.andengine.util.animationpack;

import org.andengine.entity.sprite.AnimationData;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:17:17 - 03.05.2012
 */
public class AnimationPackTiledTextureRegion extends TiledTextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mAnimationName;
	private final AnimationData mAnimationData;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimationPackTiledTextureRegion(final String pAnimationName, final long[] pFrameDurations, final int pLoopCount, final ITexture pTexture, final ITextureRegion ... pTextureRegions) {
		super(pTexture, pTextureRegions);

		this.mAnimationName = pAnimationName;
		final int frameCount = pFrameDurations.length;

		final int[] frames= new int[frameCount];
		for(int i = 0; i < frameCount; i++) {
			frames[i] = i;
		}

		this.mAnimationData = new AnimationData(pFrameDurations, frames, pLoopCount);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getAnimationName() {
		return this.mAnimationName;
	}

	public AnimationData getAnimationData() {
		return this.mAnimationData;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
