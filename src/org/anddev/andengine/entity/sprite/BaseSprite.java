package org.anddev.andengine.entity.sprite;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.DynamicEntity;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.texture.TextureRegion;
import org.anddev.andengine.opengl.vertex.VertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 11:38:53 - 08.03.2010
 */
public abstract class BaseSprite extends DynamicEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final VertexBuffer mVertexBuffer = new VertexBuffer();

	private final float mRed = 1;
	private final float mGreen = 1;
	private final float mBlue = 1;
	private float mAlpha = 1f;

	protected TextureRegion mTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSprite(final float pX, final float pY, final int pWidth, final int pHeight, final TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight);

		assert(pTextureRegion != null);
		this.mTextureRegion = pTextureRegion;
		this.updateVertexBuffer();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public VertexBuffer getVertexBuffer() {
		return this.mVertexBuffer;
	}

	public void setAlpha(final float pAlpha) {
		this.mAlpha = pAlpha;
	}

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
	protected void onPositionChancged() {
		updateVertexBuffer();
	}

	@Override
	public void onManagedDraw(final GL10 pGL) {
		GLHelper.color4f(pGL, this.mRed, this.mGreen, this.mBlue, this.mAlpha);
		GLHelper.enableVertexArray(pGL);
		GLHelper.enableTextures(pGL);
		GLHelper.enableTexCoordArray(pGL);
		GLHelper.blendMode(pGL, GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); // TODO "Default" and "Custom(Sprite-specific)" blend functions.

		pGL.glPushMatrix();
		GLHelper.vertexPointer(pGL, this.getVertexBuffer().getByteBuffer(), GL10.GL_FLOAT);

		/* Translate */
		this.applyTranslation(pGL);

		/* Rotate */
		this.applyRotation(pGL);

		/* Scale */
		this.applyScale(pGL);

		/* Texture */
		this.applyTexture(pGL);

		pGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		pGL.glPopMatrix();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void applyTranslation(final GL10 pGL) {
		pGL.glTranslatef(this.getX(), this.getY(), 0);
	}

	protected void applyRotation(final GL10 pGL) {
		final float angleClockwise = getAngleClockwise();
		if(angleClockwise != 0) {
			pGL.glTranslatef(this.getWidth() / 2, this.getHeight() / 2, 0);
			pGL.glRotatef(angleClockwise, 0, 0, 1);
			pGL.glTranslatef(-this.getWidth() / 2, -this.getHeight() / 2, 0);
		}
	}

	protected void applyScale(final GL10 pGL) {

	}

	protected void applyTexture(final GL10 pGL) {
		GLHelper.bindTexture(pGL, this.mTextureRegion.getTexture().getHardwareTextureID());
		GLHelper.texCoordPointer(pGL, this.mTextureRegion.getTextureBuffer().getByteBuffer(), GL10.GL_FLOAT);
	}

	protected void updateVertexBuffer(){
		this.mVertexBuffer.update(0, 0, this.getWidth(), this.getHeight());
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
