package org.anddev.andengine.opengl.texture;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.texture.buffer.TextureRegionBuffer;

/**
 * @author Nicolas Gramlich
 * @since 14:29:59 - 08.03.2010
 */
public class TextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final int[] HARDWAREBUFFERID_FETCHER = new int[1];

	// ===========================================================
	// Fields
	// ===========================================================

	private int mWidth;
	private int mHeight;
	private int mTexturePositionX;
	private int mTexturePositionY;
	private Texture mTexture;
	private final TextureRegionBuffer mTextureBuffer;
	
	private int mHardwareTextureBufferID = -1;
	private boolean mHardwareTextureBufferNeedsUpdate = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureRegion(final int pTexturePositionX, final int pTexturePositionY, final int pWidth, final int pHeight) {
		this.mTexturePositionX = pTexturePositionX;
		this.mTexturePositionY = pTexturePositionY;
		this.mWidth = pWidth;
		this.mHeight = pHeight;
		this.mTextureBuffer = this.onCreateTextureRegionBuffer();
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
		this.mTextureBuffer.update();
	}

	public void setHeight(final int pHeight) {
		this.mHeight = pHeight;
		this.mTextureBuffer.update();
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

		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
            this.mHardwareTextureBufferNeedsUpdate  = true;
		}
	}

	protected TextureRegionBuffer onCreateTextureRegionBuffer() {
		return new TextureRegionBuffer(this);
	}

	public void onApply(final GL10 pGL) {
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			final GL11 gl11 = (GL11)pGL;
			if(this.mHardwareTextureBufferID == -1) {
				gl11.glGenBuffers(1, HARDWAREBUFFERID_FETCHER, 0);
				this.mHardwareTextureBufferID = HARDWAREBUFFERID_FETCHER[0];
			}
			
			if(this.mHardwareTextureBufferNeedsUpdate) {
				this.mHardwareTextureBufferNeedsUpdate = false;
				GLHelper.bindBuffer(gl11, this.mHardwareTextureBufferID);
				GLHelper.bufferData(gl11, this.mTextureBuffer, GL11.GL_STATIC_DRAW);
			}
			
			GLHelper.bindBuffer(gl11, this.mHardwareTextureBufferID);

			GLHelper.bindTexture(pGL, this.mTexture.getHardwareTextureID());
			GLHelper.texCoordZeroPointer(gl11);
		} else {
			GLHelper.bindTexture(pGL, this.mTexture.getHardwareTextureID());
			GLHelper.texCoordPointer(pGL, this.mTextureBuffer.getFloatBuffer());	
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
