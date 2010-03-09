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
public class Sprite extends BaseEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private float mRed = 1; // TODO Background-Colors !?!
	private float mGreen = 1;
	private float mBlue = 1;
	private float mAlpha = 0.5f;

	private int mX = 0;
	private int mY = 0;
	private int mWidth;
	private int mHeight;
	
	private Texture mTexture;
	private VertexBuffer mVertexBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Sprite(final int pX, final int pY, final Texture pTexture) {
		this.mX = pX;
		this.mY = pY;
		setTexture(pTexture);
		updateVertexBuffer();
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

	private boolean hasTexture() {
		return this.mTexture != null;
	}

	private void setTexture(final Texture pTexture) {
		this.mTexture = pTexture;
		this.mWidth = pTexture.getWidth();
		this.mHeight = pTexture.getHeight();
	}
	
	public int getWidth() {
		return this.mWidth;
	}
	
	public int getHeight() {
		return this.mHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onManagedDraw(final GL10 pGL) {
		GLHelper.color4f(pGL, this.mRed, this.mGreen, this.mBlue, this.mAlpha);
		
//        if(this.hasCustomBlendFunction())
//                GLHelper.blendMode(_blendFunction);
//        else
		GLHelper.blendMode(pGL, GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        pGL.glPushMatrix();              
        GLHelper.enableVertexArray(pGL);
        GLHelper.vertexPointer(pGL, this.mVertexBuffer.getByteBuffer(), GL10.GL_FLOAT);
        
        /* Translate */
        pGL.glTranslatef(getX(), getY(), 0);
        
        /* Rotate */
        
        /* Scale */
        
        /* Texture */
        if(this.hasTexture()){
            GLHelper.enableTextures(pGL);
            GLHelper.enableTexCoordArray(pGL);
            GLHelper.bindTexture(pGL, this.mTexture.getTextureAtlas().getHardwareTextureID());            
            GLHelper.texCoordPointer(pGL, this.mTexture.getTextureBuffer().getUVMappingByteBuffer(), GL10.GL_FLOAT);
        }else{
            GLHelper.disableTexCoordArray(pGL);
            GLHelper.disableTextures(pGL);
        }
        
        pGL.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        pGL.glPopMatrix();

	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
    private void updateVertexBuffer() {
        this.mVertexBuffer = new VertexBuffer();
        this.mVertexBuffer.update(0, 0, getWidth(), getHeight());
    }

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
