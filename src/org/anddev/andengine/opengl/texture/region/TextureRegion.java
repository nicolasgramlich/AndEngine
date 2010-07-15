package org.anddev.andengine.opengl.texture.region;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.buffer.TextureRegionBuffer;
import org.anddev.andengine.opengl.util.GLHelper;

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

	protected final Texture mTexture;

	protected final TextureRegionBuffer mTextureRegionBuffer;

	private int mWidth;
	private int mHeight;

	private int mTexturePositionX;
	private int mTexturePositionY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegion(final Texture pTexture, final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		this.mTexture = pTexture;
		this.mTexturePositionX = pTexturePositionX;
		this.mTexturePositionY = pTexturePositionY;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		
		this.mTextureRegionBuffer = this.onCreateTextureRegionBuffer();
		
		BufferObjectManager.getActiveInstance().loadBufferObject(this.mTextureRegionBuffer);

		this.initTextureBuffer();
	}

	protected void initTextureBuffer() {
		this.updateTextureRegionBuffer();
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
		this.updateTextureRegionBuffer();
	}

	public void setHeight(final int pHeight) {
		this.mHeight = pHeight;
		this.updateTextureRegionBuffer();
	}

	public void setTexturePosition(final int pX, final int pY) {
		this.mTexturePositionX = pX;
		this.mTexturePositionY = pY;
		this.updateTextureRegionBuffer();
	}

	public int getTexturePositionX() {
		return this.mTexturePositionX;
	}

	public int getTexturePositionY() {
		return this.mTexturePositionY;
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
		return new TextureRegion(this.mTexture, this.mTexturePositionX, this.mTexturePositionY, this.mWidth, this.mHeight);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void updateTextureRegionBuffer() {
		this.mTextureRegionBuffer.update();
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
