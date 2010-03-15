package org.anddev.andengine.entity.sprite;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.primitives.Rectangle;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.texture.TextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 11:38:53 - 08.03.2010
 */
public abstract class BaseSprite extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected TextureRegion mTextureRegion;
	protected int mSourceBlendFunction = GL10.GL_ONE;
	protected int mDestinationBlendFunction = GL10.GL_ONE_MINUS_SRC_ALPHA;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight);

		assert(pTextureRegion != null);
		this.mTextureRegion = pTextureRegion;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public TextureRegion getTextureRegion() {
		return this.mTextureRegion;
	}

	public void setTextureRegion(final TextureRegion pTextureRegion){
		this.mTextureRegion = pTextureRegion;
	}
	
	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mSourceBlendFunction = pSourceBlendFunction;
		this.mDestinationBlendFunction = pDestinationBlendFunction;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected void onInitDraw(GL10 pGL) {
		super.onInitDraw(pGL);
		GLHelper.enableTextures(pGL);
		GLHelper.enableTexCoordArray(pGL);
		GLHelper.blendFunction(pGL, this.mSourceBlendFunction, this.mDestinationBlendFunction);
	}
	
	@Override
	protected void onPostTransformations(final GL10 pGL) {
		super.onPostTransformations(pGL);
		applyTexture(pGL);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void applyTexture(final GL10 pGL) {
		GLHelper.bindTexture(pGL, this.mTextureRegion.getTexture().getHardwareTextureID());
		GLHelper.texCoordPointer(pGL, this.mTextureRegion.getTextureBuffer().getByteBuffer(), GL10.GL_FLOAT);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
