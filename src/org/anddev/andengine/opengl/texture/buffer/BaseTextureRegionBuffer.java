package org.anddev.andengine.opengl.texture.buffer;

import java.nio.ByteBuffer;

import org.anddev.andengine.opengl.BaseBuffer;
import org.anddev.andengine.opengl.texture.TextureRegion;
import org.anddev.andengine.opengl.texture.Texture;

/**
 * @author Nicolas Gramlich
 * @since 19:05:50 - 09.03.2010
 */
public abstract class BaseTextureRegionBuffer extends BaseBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final TextureRegion mTextureRegion;
	private boolean mFlippedVertical;
	private boolean mFlippedHorizontal;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTextureRegionBuffer(final TextureRegion pTextureRegion) {
		this.mTextureRegion = pTextureRegion;
		this.update();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public TextureRegion getTextureRegion() {
		return this.mTextureRegion;
	}
	
	public boolean isFlippedHorizontal() {
		return this.mFlippedHorizontal;
	}
	
	public void setFlippedHorizontal(final boolean pFlippedHorizontal) {
		if(this.mFlippedHorizontal != pFlippedHorizontal){
			this.mFlippedHorizontal = pFlippedHorizontal;
			update();
		}
	}
	
	public boolean isFlippedVertical() {
		return this.mFlippedVertical;
	}

	public void setFlippedVertical(final boolean pFlippedVertical) {
		if(this.mFlippedVertical != pFlippedVertical){
			this.mFlippedVertical = pFlippedVertical;
			update();
		}
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
		final TextureRegion textureRegion = this.mTextureRegion;
		final Texture texture = textureRegion.getTexture();

		if(texture == null) {
			return;
		}

		final float x1 = getX1();
		final float y1 = getY1();
		final float x2 = getX2();
		final float y2 = getY2();

		final ByteBuffer buffer = this.getByteBuffer();
		buffer.position(0);

		if(this.mFlippedVertical) {
			if(this.mFlippedHorizontal){
				buffer.putFloat(x2); buffer.putFloat(y2);
	
				buffer.putFloat(x1); buffer.putFloat(y2);
	
				buffer.putFloat(x2); buffer.putFloat(y1);
	
				buffer.putFloat(x1); buffer.putFloat(y1);	
			} else {
				buffer.putFloat(x1); buffer.putFloat(y2);
	
				buffer.putFloat(x2); buffer.putFloat(y2);
	
				buffer.putFloat(x1); buffer.putFloat(y1);
	
				buffer.putFloat(x2); buffer.putFloat(y1);				
			}
		} else {
			if(this.mFlippedHorizontal){
				buffer.putFloat(x2); buffer.putFloat(y1);
	
				buffer.putFloat(x1); buffer.putFloat(y1);
	
				buffer.putFloat(x2); buffer.putFloat(y2);
	
				buffer.putFloat(x1); buffer.putFloat(y2);
			} else {
				buffer.putFloat(x1); buffer.putFloat(y1);
	
				buffer.putFloat(x2); buffer.putFloat(y1);
	
				buffer.putFloat(x1); buffer.putFloat(y2);
	
				buffer.putFloat(x2); buffer.putFloat(y2);
			}
		}

		buffer.position(0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

