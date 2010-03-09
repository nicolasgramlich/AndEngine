package org.anddev.andengine.opengl.texture.buffer;

import org.anddev.andengine.opengl.texture.Texture;

public class TextureBuffer extends BaseTextureBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureBuffer(final Texture pTexture) {
		super(pTexture);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected float getX1() {
		final Texture texture = this.getTexture();
		return (float)texture.getAtlasPositionX() / texture.getTextureAtlas().getWidth();
	}
	
	@Override
	protected float getX2() {
		final Texture texture = this.getTexture();
		return (float)(texture.getAtlasPositionX() + texture.getWidth()) / texture.getTextureAtlas().getWidth();
	}
	
	@Override
	protected float getY1() {
		final Texture texture = this.getTexture();
		return (float)texture.getAtlasPositionY() / texture.getTextureAtlas().getHeight();
	}
	
	@Override
	protected float getY2() {
		final Texture texture = this.getTexture();
		return (float)(texture.getAtlasPositionY() + texture.getHeight()) / texture.getTextureAtlas().getHeight();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
