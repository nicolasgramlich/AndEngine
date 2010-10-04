package org.anddev.andengine.opengl.texture.region.buffer;

import static org.anddev.andengine.opengl.vertex.RectangleVertexBuffer.VERTICES_PER_RECTANGLE;

import java.nio.FloatBuffer;

import org.anddev.andengine.opengl.buffer.BufferObject;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.BaseTextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 19:05:50 - 09.03.2010
 */
public abstract class BaseTextureRegionBuffer extends BufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final BaseTextureRegion mTextureRegion;
	private boolean mFlippedVertical;
	private boolean mFlippedHorizontal;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseTextureRegionBuffer(final BaseTextureRegion pBaseTextureRegion, final int pDrawType) {
		super(2 * VERTICES_PER_RECTANGLE * BYTES_PER_FLOAT, pDrawType);
		this.mTextureRegion = pBaseTextureRegion;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public BaseTextureRegion getTextureRegion() {
		return this.mTextureRegion;
	}

	public boolean isFlippedHorizontal() {
		return this.mFlippedHorizontal;
	}

	public void setFlippedHorizontal(final boolean pFlippedHorizontal) {
		if(this.mFlippedHorizontal != pFlippedHorizontal) {
			this.mFlippedHorizontal = pFlippedHorizontal;
			this.setHardwareBufferNeedsUpdate();
		}
	}

	public boolean isFlippedVertical() {
		return this.mFlippedVertical;
	}

	public void setFlippedVertical(final boolean pFlippedVertical) {
		if(this.mFlippedVertical != pFlippedVertical) {
			this.mFlippedVertical = pFlippedVertical;
			this.setHardwareBufferNeedsUpdate();
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

	@Override
	public synchronized void setHardwareBufferNeedsUpdate() {
		final BaseTextureRegion textureRegion = this.mTextureRegion;
		final Texture texture = textureRegion.getTexture();

		if(texture == null) {
			return;
		}

		final float x1 = this.getX1();
		final float y1 = this.getY1();
		final float x2 = this.getX2();
		final float y2 = this.getY2();

		final FloatBuffer buffer = this.getFloatBuffer();
		buffer.position(0);

		if(this.mFlippedVertical) {
			if(this.mFlippedHorizontal){
				buffer.put(x2); buffer.put(y2);

				buffer.put(x2); buffer.put(y1);

				buffer.put(x1); buffer.put(y2);

				buffer.put(x1); buffer.put(y1);
			} else {
				buffer.put(x1); buffer.put(y2);

				buffer.put(x1); buffer.put(y1);

				buffer.put(x2); buffer.put(y2);

				buffer.put(x2); buffer.put(y1);
			}
		} else {
			if(this.mFlippedHorizontal){
				buffer.put(x2); buffer.put(y1);

				buffer.put(x2); buffer.put(y2);

				buffer.put(x1); buffer.put(y1);

				buffer.put(x1); buffer.put(y2);
			} else {
				buffer.put(x1); buffer.put(y1);

				buffer.put(x1); buffer.put(y2);

				buffer.put(x2); buffer.put(y1);

				buffer.put(x2); buffer.put(y2);
			}
		}
		buffer.position(0);

		super.setHardwareBufferNeedsUpdate();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

