package org.anddev.andengine.opengl.texture;

import org.anddev.andengine.opengl.texture.buffer.TextureBuffer;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

import android.graphics.Bitmap;

/**
 * @author Nicolas Gramlich
 * @since 14:29:59 - 08.03.2010
 */
public class Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mWidth;
	protected final int mHeight;
	protected int mAtlasPositionX;
	protected int mAtlasPositionY;
	protected TextureAtlas mTextureAtlas;
	private final ITextureSource mTextureSource;
	private final TextureBuffer mTextureBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	Texture(final TextureAtlas pTextureAtlas, final int pAtlasPositionX, final int pAtlasPositionY, final int pWidth, final int pHeight) {
		this.mTextureSource = null;
		this.mTextureAtlas = pTextureAtlas;
		this.mAtlasPositionX = pAtlasPositionX;
		this.mAtlasPositionY = pAtlasPositionY;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mTextureBuffer = onCreateTextureBuffer();
	}

	public Texture(final ITextureSource pTextureSource) {
		this.mTextureSource = pTextureSource;
		this.mWidth = pTextureSource.getWidth();
		this.mHeight = pTextureSource.getHeight();
		this.mTextureBuffer = onCreateTextureBuffer();
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

	public void setAtlasPosition(final int pX, final int pY) {
		this.mAtlasPositionX = pX;
		this.mAtlasPositionY = pY;
		this.mTextureBuffer.update();
	}

	public int getAtlasPositionX() {
		return this.mAtlasPositionX;
	}

	public int getAtlasPositionY() {
		return this.mAtlasPositionY;
	}
	
	public void setTextureAtlas(final TextureAtlas pTextureAtlas) {
		this.mTextureAtlas = pTextureAtlas;
		this.mTextureBuffer.update();
	}

	public TextureAtlas getTextureAtlas() {
		return this.mTextureAtlas;
	}

	public Bitmap getBitmap() {
		return this.mTextureSource.getBitmap();
	}
	
	public TextureBuffer getTextureBuffer() {
		return this.mTextureBuffer;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	protected void updateTextureBuffer() {
		this.mTextureBuffer.update();
	}

	protected TextureBuffer onCreateTextureBuffer() {
		return new TextureBuffer(this);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
