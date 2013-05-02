package org.andengine.util.animationpack;

import java.util.HashMap;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:17:37 - 03.05.2012
 */
public class AnimationPackTiledTextureRegionLibrary {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final HashMap<String, AnimationPackTiledTextureRegion> mAnimationPackTiledTextureRegionMapping = new HashMap<String, AnimationPackTiledTextureRegion>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void put(final AnimationPackTiledTextureRegion pAnimationPackTiledTextureRegion) {
		this.mAnimationPackTiledTextureRegionMapping.put(pAnimationPackTiledTextureRegion.getAnimationName(), pAnimationPackTiledTextureRegion);
	}

	public AnimationPackTiledTextureRegion get(final String pAnimationName) {
		return this.mAnimationPackTiledTextureRegionMapping.get(pAnimationName);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
