package org.anddev.andengine.entity.sprite;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.BaseEntity;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.vertex.VertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 11:38:53 - 08.03.2010
 */
public abstract class BaseSprite extends BaseEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private float mRed = 1;
	private float mGreen = 1;
	private float mBlue = 1;
	private float mAlpha = 1f;

	private int mX = 0;
	private int mY = 0;
	
	protected VertexBuffer mVertexBuffer = new VertexBuffer();
	
	protected Texture mTexture;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public BaseSprite(final int pX, final int pY, final Texture pTexture) {
		this.mX = pX;
		this.mY = pY;
		
		assert(pTexture != null);
		this.mTexture = pTexture;
		
		onUpdateVertexBuffer();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public int getX() {
		return this.mX;
	}
	
	public int getY() {
		return this.mY;
	}
	
	public void setAlpha(final float pAlpha) {
		this.mAlpha = pAlpha;
	}
	
	public Texture getTexture() {
		return this.mTexture;
	}
	
	public void setTexture(final Texture pTexture){
		this.mTexture = pTexture;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public abstract int getWidth();
	
	public abstract int getHeight();

	@Override
	public void onManagedDraw(final GL10 pGL) {
		GLHelper.color4f(pGL, this.mRed, this.mGreen, this.mBlue, this.mAlpha);
        GLHelper.enableVertexArray(pGL);
        GLHelper.enableTextures(pGL);
        GLHelper.enableTexCoordArray(pGL);
		GLHelper.blendMode(pGL, GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); // TODO "Default" and "Custom(Sprite-specific)" blend functions.
        
        pGL.glPushMatrix();              
        GLHelper.vertexPointer(pGL, this.mVertexBuffer.getByteBuffer(), GL10.GL_FLOAT);
        
        /* Translate */
        onApplyTranslation(pGL);
        
        /* Rotate */
        onApplyRotation(pGL);
        
        /* Scale */
        onApplyScale(pGL);
        
        /* Texture */
        onApplyTexture(pGL);
        
        pGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        pGL.glPopMatrix();
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onApplyTranslation(final GL10 pGL) {
        pGL.glTranslatef(getX(), getY(), 0);
	}

	protected void onApplyRotation(GL10 pGL) {
		
	}

	protected void onApplyScale(GL10 pGL) {
		
	}
	
    protected void onUpdateVertexBuffer(){
        this.mVertexBuffer.update(0, 0, getWidth(), getHeight());
    }

	protected void onApplyTexture(final GL10 pGL) {
		GLHelper.bindTexture(pGL, this.mTexture.getTextureAtlas().getHardwareTextureID());            
		GLHelper.texCoordPointer(pGL, this.mTexture.getTextureBuffer().getByteBuffer(), GL10.GL_FLOAT);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
