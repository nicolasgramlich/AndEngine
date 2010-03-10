package org.anddev.andengine.opengl.texture.buffer;

import org.anddev.andengine.opengl.texture.TextureRegion;

public class TextureRegionBuffer extends BaseTextureRegionBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegionBuffer(final TextureRegion pTextureRegion) {
		super(pTextureRegion);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected float getX1() {
		final TextureRegion textureRegion = this.getTextureRegion();
		return (float)textureRegion.getTexturePositionX() / textureRegion.getTexture().getWidth();
	}
	
	@Override
	protected float getX2() {
		final TextureRegion textureRegion = this.getTextureRegion();
		return (float)(textureRegion.getTexturePositionX() + textureRegion.getWidth()) / textureRegion.getTexture().getWidth();
	}
	
	@Override
	protected float getY1() {
		final TextureRegion textureRegion = this.getTextureRegion();
		return (float)textureRegion.getTexturePositionY() / textureRegion.getTexture().getHeight();
	}
	
	@Override
	protected float getY2() {
		final TextureRegion textureRegion = this.getTextureRegion();
		return (float)(textureRegion.getTexturePositionY() + textureRegion.getHeight()) / textureRegion.getTexture().getHeight();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
