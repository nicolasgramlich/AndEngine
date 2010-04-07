package org.anddev.andengine.opengl.texture.region;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.texture.Texture;
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

	private Texture mTexture;
	
	private final TextureRegionBuffer mTextureRegionBuffer;
	
	private int mWidth;
	private int mHeight;
	
	private int mTexturePositionX;
	private int mTexturePositionY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegion(final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		this.mTexturePositionX = pTexturePositionX;
		this.mTexturePositionY = pTexturePositionY;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mTextureRegionBuffer = this.onCreateTextureRegionBuffer();
		BufferObjectManager.loadBufferObject(this.mTextureRegionBuffer);
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

	public void setWidth(final int pWidth) {
		this.mWidth = pWidth;
		this.mTextureRegionBuffer.onUpdated();
	}

	public void setHeight(final int pHeight) {
		this.mHeight = pHeight;
		this.mTextureRegionBuffer.onUpdated();
	}

	public void setTexturePosition(final int pX, final int pY) {
		this.mTexturePositionX = pX;
		this.mTexturePositionY = pY;
		this.mTextureRegionBuffer.onUpdated();
	}

	public int getTexturePositionX() {
		return this.mTexturePositionX;
	}

	public int getTexturePositionY() {
		return this.mTexturePositionY;
	}

	public void setTexture(final Texture pTexture) {
		this.mTexture = pTexture;
		this.mTextureRegionBuffer.onUpdated();
	}

	public Texture getTexture() {
		return this.mTexture;
	}

	public TextureRegionBuffer getTextureBuffer() {
		return this.mTextureRegionBuffer;
	}

	public boolean isFlippedHorizontal() {
		return this.mTextureRegionBuffer.isFlippedHorizontal();
	}

	public void setFlippedHorizontal(final boolean pFlippedHorizontal) {
		this.mTextureRegionBuffer.setFlippedHorizontal(pFlippedHorizontal);
	}

	public boolean isFlippedVertical() {
		return this.mTextureRegionBuffer.isFlippedVertical();
	}

	public void setFlippedVertical(final boolean pFlippedVertical) {
		this.mTextureRegionBuffer.setFlippedVertical(pFlippedVertical);
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
		this.mTextureRegionBuffer.onUpdated();
	}

	protected TextureRegionBuffer onCreateTextureRegionBuffer() {
		return new TextureRegionBuffer(this, GL11.GL_STATIC_DRAW);
	}

	public void onApply(final GL10 pGL) {
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			final GL11 gl11 = (GL11)pGL;

			this.mTextureRegionBuffer.selectOnHardware(gl11);

			GLHelper.bindTexture(pGL, this.mTexture.getHardwareTextureID());
			GLHelper.texCoordZeroPointer(gl11);
		} else {
			GLHelper.bindTexture(pGL, this.mTexture.getHardwareTextureID());
			GLHelper.texCoordPointer(pGL, this.mTextureRegionBuffer.getFloatBuffer());	
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
