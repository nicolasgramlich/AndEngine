package org.anddev.andengine.opengl.texture;

import org.anddev.andengine.opengl.texture.buffer.TextureRegionBuffer;

/**
 * @author Nicolas Gramlich
 * @since 14:29:59 - 08.03.2010
 */
public class TextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mWidth;
	private final int mHeight;
	private int mTexturePositionX;
	private int mTexturePositionY;
	private Texture mTexture;
	private final TextureRegionBuffer mTextureBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegion(final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		this.mTexturePositionX = pTexturePositionX;
		this.mTexturePositionY = pTexturePositionY;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mTextureBuffer = onCreateTextureRegionBuffer();
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getWidth() {
		return this.mWidth;
	}

	public int getHeight() {
		return this.mHeight;
	}

	public void setTexturePosition(final int pX, final int pY) {
		this.mTexturePositionX = pX;
		this.mTexturePositionY = pY;
		this.mTextureBuffer.update();
	}

	public int getTexturePositionX() {
		return this.mTexturePositionX;
	}

	public int getTexturePositionY() {
		return this.mTexturePositionY;
	}
	
	public void setTexture(final Texture pTexture) {
		this.mTexture = pTexture;
		this.mTextureBuffer.update();
	}

	public Texture getTexture() {
		return this.mTexture;
	}
	
	public TextureRegionBuffer getTextureBuffer() {
		return this.mTextureBuffer;
	}
	
	public boolean isFlippedHorizontal() {
		return this.mTextureBuffer.isFlippedHorizontal();
	}
	
	public void setFlippedHorizontal(final boolean pFlippedHorizontal) {
		this.mTextureBuffer.setFlippedHorizontal(pFlippedHorizontal);
	}
	
	public boolean isFlippedVertical() {
		return this.mTextureBuffer.isFlippedVertical();
	}
	
	public void setFlippedVertical(final boolean pFlippedVertical) {
		this.mTextureBuffer.setFlippedVertical(pFlippedVertical);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public TextureRegion clone() {
		final TextureRegion clone = new TextureRegion(this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight);
		clone.setTexture(this.getTexture());
		return clone;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	protected void updateTextureBuffer() {
		this.mTextureBuffer.update();
	}

	protected TextureRegionBuffer onCreateTextureRegionBuffer() {
		return new TextureRegionBuffer(this);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
