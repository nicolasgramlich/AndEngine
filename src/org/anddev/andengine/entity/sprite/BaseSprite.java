package org.anddev.andengine.entity.sprite;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import org.anddev.andengine.entity.primitives.BaseRectangle;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 11:38:53 - 08.03.2010
 */
public abstract class BaseSprite extends BaseRectangle {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int BLENDFUNCTION_SOURCE_DEFAULT = GL10.GL_SRC_ALPHA;
	private static final int BLENDFUNCTION_DESTINATION_DEFAULT = GL10.GL_ONE_MINUS_SRC_ALPHA;

	// ===========================================================
	// Fields
	// ===========================================================

	private final TextureRegion mTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight);

		this.mTextureRegion = pTextureRegion;
		this.setBlendFunction(BLENDFUNCTION_SOURCE_DEFAULT, BLENDFUNCTION_DESTINATION_DEFAULT);
	}

	public BaseSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureRegion pTextureRegion, final RectangleVertexBuffer pRectangleVertexBuffer) {
		super(pX, pY, pWidth, pHeight, pRectangleVertexBuffer);

		this.mTextureRegion = pTextureRegion;
		this.setBlendFunction(BLENDFUNCTION_SOURCE_DEFAULT, BLENDFUNCTION_DESTINATION_DEFAULT);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public TextureRegion getTextureRegion() {
		return this.mTextureRegion;
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
	protected void onManagedDraw(GL10 pGL) {
		if(GLHelper.EXTENSIONS_DRAWTEXTURE && this.mAngle == 0) {
			GLHelper.setColor(pGL, 1, 0, 0, 0.5f);
			GLHelper.disableBlend(pGL);
			GLHelper.enableTextures(pGL);
			GLHelper.bindTexture(pGL, this.mTextureRegion.getTexture().getHardwareTextureID());

			((GL11) pGL).glTexParameterfv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, this.mTextureRegion.getTextureBuffer().getFloatBuffer());

            ((GL11Ext) pGL).glDrawTexfOES(this.mX, this.mY, 0, this.getWidthScaled(), this.getHeightScaled());
			GLHelper.enableBlend(pGL);
		} else {
			super.onManagedDraw(pGL);
		}
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
