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

	private static final int BLENDFUNCTION_SOURCE_DEFAULT = GL10.GL_SRC_ALPHA;
	private static final int BLENDFUNCTION_DESTINATION_DEFAULT = GL10.GL_ONE_MINUS_SRC_ALPHA;

	// ===========================================================
	// Fields
	// ===========================================================

	protected TextureRegion mTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight);

		assert(pTextureRegion != null);
		this.mTextureRegion = pTextureRegion;
		this.setBlendFunction(BLENDFUNCTION_SOURCE_DEFAULT, BLENDFUNCTION_DESTINATION_DEFAULT);
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
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public void reset() {
		super.reset();

		this.setBlendFunction(BLENDFUNCTION_SOURCE_DEFAULT, BLENDFUNCTION_DESTINATION_DEFAULT);
	}

	@Override
	protected void onInitDraw(final GL10 pGL) {
		super.onInitDraw(pGL);
		GLHelper.enableTextures(pGL);
		GLHelper.enableTexCoordArray(pGL);
	}

	@Override
	protected void onPostTransformations(final GL10 pGL) {
		super.onPostTransformations(pGL);
		
		this.mTextureRegion.onApply(pGL);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
