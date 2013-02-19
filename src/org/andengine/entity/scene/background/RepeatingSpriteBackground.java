package org.andengine.entity.scene.background;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:11:10 - 19.07.2010
 */
public class RepeatingSpriteBackground extends SpriteBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float SCALE_DEFAULT = 1;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RepeatingSpriteBackground(final float pCameraWidth, final float pCameraHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) throws IllegalArgumentException {
		this(pCameraWidth, pCameraHeight, pTextureRegion, SCALE_DEFAULT, pVertexBufferObjectManager);
	}

	public RepeatingSpriteBackground(final float pCameraWidth, final float pCameraHeight, final ITextureRegion pTextureRegion, final float pScale, final VertexBufferObjectManager pVertexBufferObjectManager) throws IllegalArgumentException {
		super(RepeatingSpriteBackground.createSprite(pCameraWidth, pCameraHeight, pTextureRegion, pScale, pVertexBufferObjectManager));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private static Sprite createSprite(final float pCameraWidth, final float pCameraHeight, final ITextureRegion pTextureRegion, final float pScale, final VertexBufferObjectManager pVertexBufferObjectManager) throws IllegalArgumentException {
		final int width = Math.round(pCameraWidth / pScale);
		final int height = Math.round(pCameraHeight / pScale);

		pTextureRegion.setTextureSize(width, height);

		return new Sprite(pCameraWidth * 0.5f, pCameraHeight * 0.5f, width * pScale, height * pScale, pTextureRegion, pVertexBufferObjectManager);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
