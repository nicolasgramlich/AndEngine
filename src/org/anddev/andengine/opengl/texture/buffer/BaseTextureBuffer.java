package org.anddev.andengine.opengl.texture.buffer;

import java.nio.ByteBuffer;

import org.anddev.andengine.opengl.BaseBuffer;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureAtlas;

/**
 * @author Nicolas Gramlich
 * @since 19:05:50 - 09.03.2010
 */
public abstract class BaseTextureBuffer extends BaseBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final Texture mTexture;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTextureBuffer(final Texture pTexture) {
		this.mTexture = pTexture;
		this.update();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Texture getTexture() {
		return this.mTexture;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract float getX1();
	protected abstract float getY1();
	protected abstract float getX2();
	protected abstract float getY2();

	// ===========================================================
	// Methods
	// ===========================================================

	public void update() {
		final Texture texture = this.mTexture;
		final TextureAtlas textureAtlas = texture.getTextureAtlas();

		if(textureAtlas == null) {
			return;
		}

		final float x1 = getX1();
		final float y1 = getY1();
		final float x2 = getX2();
		final float y2 = getY2();

		final ByteBuffer buffer = this.getByteBuffer();
		buffer.position(0);

		buffer.putFloat(x1); 
		buffer.putFloat(y1);

		buffer.putFloat(x2);
		buffer.putFloat(y1);

		buffer.putFloat(x1);
		buffer.putFloat(y2);

		buffer.putFloat(x2);
		buffer.putFloat(y2);

		buffer.position(0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

