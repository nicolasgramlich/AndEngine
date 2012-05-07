package org.andengine.util.animationpack;

import org.andengine.util.texturepack.TexturePackLibrary;


/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:58:15 - 03.05.2012
 */
public class AnimationPack {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final TexturePackLibrary mTexturePackLibrary;
	private final AnimationPackTiledTextureRegionLibrary mAnimationPackTiledTextureRegionLibrary;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimationPack(final TexturePackLibrary pTexturePackLibrary, final AnimationPackTiledTextureRegionLibrary pAnimationPackTiledTextureRegionLibrary) {
		this.mTexturePackLibrary = pTexturePackLibrary;
		this.mAnimationPackTiledTextureRegionLibrary = pAnimationPackTiledTextureRegionLibrary;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public TexturePackLibrary getTexturePackLibrary() {
		return this.mTexturePackLibrary;
	}

	public AnimationPackTiledTextureRegionLibrary getAnimationPackAnimationDataLibrary() {
		return this.mAnimationPackTiledTextureRegionLibrary;
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
