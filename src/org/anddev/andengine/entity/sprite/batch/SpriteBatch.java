package org.anddev.andengine.entity.sprite.batch;

import static org.anddev.andengine.opengl.vertex.SpriteBatchVertexBuffer.VERTICES_PER_RECTANGLE;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.region.BaseTextureRegion;
import org.anddev.andengine.opengl.texture.region.buffer.SpriteBatchTextureRegionBuffer;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vertex.SpriteBatchVertexBuffer;

/**
 * TODO Texture could be semi-changeable, being resetting to null in end(...)
 * TODO Add sth like "SpriteGroup extends SpriteBatch"-subclass that has: "private final SmartList<BaseSprite> mSprites = new SmartList<BaseSprite>();" and draws all of them in onDrawSpriteBatch().
 * TODO Make use of pGL.glColorPointer(size, type, stride, pointer) which should allow individual color tinting.
 * 
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:45:48 - 14.06.2011
 */
public class SpriteBatch extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ITexture mTexture;
	protected final int mCapacity;

	private int mIndex;
	private int mVertices;
	private boolean mDirty = false;

	private int mSourceBlendFunction;
	private int mDestinationBlendFunction;

	private final SpriteBatchVertexBuffer mSpriteBatchVertexBuffer;
	private final SpriteBatchTextureRegionBuffer mSpriteBatchTextureRegionBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteBatch(final ITexture pTexture, final int pCapacity) {
		this(pTexture, pCapacity, new SpriteBatchVertexBuffer(pCapacity, GL11.GL_STATIC_DRAW, true), new SpriteBatchTextureRegionBuffer(pCapacity, GL11.GL_STATIC_DRAW, true));
	}
	
	public SpriteBatch(final ITexture pTexture, final int pCapacity, final SpriteBatchVertexBuffer pSpriteBatchVertexBuffer, final SpriteBatchTextureRegionBuffer pSpriteBatchTextureRegionBuffer) {
		this.mTexture = pTexture;
		this.mCapacity = pCapacity;
		this.mSpriteBatchVertexBuffer = pSpriteBatchVertexBuffer;
		this.mSpriteBatchTextureRegionBuffer = pSpriteBatchTextureRegionBuffer;

		this.initBlendFunction();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mSourceBlendFunction = pSourceBlendFunction;
		this.mDestinationBlendFunction = pDestinationBlendFunction;
	}

	public int getIndex() {
		return this.mIndex;
	}

	public void setIndex(final int pIndex) {
		this.assertCapacity(pIndex);

		this.mIndex = pIndex;

		final int vertexIndex = pIndex * VERTICES_PER_RECTANGLE;

		this.mSpriteBatchVertexBuffer.setIndex(vertexIndex);
		this.mSpriteBatchTextureRegionBuffer.setIndex(vertexIndex);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void doDraw(final GL10 pGL, final Camera pCamera) {
		this.onInitDraw(pGL);

		this.begin(pGL);
		this.onDrawSpriteBatch();

		if(this.mDirty) {
			this.submit();
		}

		this.onApplyVertices(pGL);
		this.onApplyTextureRegion(pGL);
		this.drawVertices(pGL, pCamera);

		this.end(pGL);
	}

	@Override
	public void reset() {
		super.reset();

		this.initBlendFunction();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		if(this.mSpriteBatchVertexBuffer.isManaged()) {
			this.mSpriteBatchVertexBuffer.unloadFromActiveBufferObjectManager();
		}
		if(this.mSpriteBatchTextureRegionBuffer.isManaged()) {
			this.mSpriteBatchTextureRegionBuffer.unloadFromActiveBufferObjectManager();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void begin(@SuppressWarnings("unused") final GL10 pGL) {
//		GLHelper.disableDepthMask(pGL);
	}

	protected void onDrawSpriteBatch() {

	}

	protected void end(@SuppressWarnings("unused") final GL10 pGL) {
//		GLHelper.enableDepthMask(pGL);
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float)}.
	 */
	public void draw(final BaseTextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
		this.mDirty = true;
	}
	
	public void drawWithoutChecks(final BaseTextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
		this.mDirty = true;
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float, float)}.
	 */
	public void draw(final BaseTextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pRotation);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
		this.mDirty = true;
	}
	
	public void drawWithoutChecks(final BaseTextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation) {
		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pRotation);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
		this.mDirty = true;
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float, float, float)}.
	 */
	public void draw(final BaseTextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pScaleX, pScaleY);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
		this.mDirty = true;
	}
	
	public void drawWithoutChecks(final BaseTextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY) {
		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pScaleX, pScaleY);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
		this.mDirty = true;
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float, float, float, float)}.
	 */
	public void draw(final BaseTextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
		this.mDirty = true;
	}
	
	public void drawWithoutChecks(final BaseTextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY) {
		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
		this.mDirty = true;
	}

	/**
	 * {@link SpriteBatchVertexBuffer#add(float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final BaseTextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		this.assertCapacity();
		this.assertTexture(pTextureRegion);

		this.mSpriteBatchVertexBuffer.addInner(pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
		this.mDirty = true;
	}
	
	public void drawWithoutChecks(final BaseTextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		this.mSpriteBatchVertexBuffer.addInner(pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mIndex++;
		this.mDirty = true;
	}

	/**
	 * {@link SpriteBatchVertexBuffer#add(BaseSprite)}.
	 */
	public void draw(final BaseSprite pBaseSprite) {
		if(pBaseSprite.isVisible()) {
			this.assertCapacity();
	
			final BaseTextureRegion textureRegion = pBaseSprite.getTextureRegion();
			this.assertTexture(textureRegion);
	
			if(pBaseSprite.getRotation() == 0 && !pBaseSprite.isScaled()) {
				this.mSpriteBatchVertexBuffer.add(pBaseSprite.getX(), pBaseSprite.getY(), pBaseSprite.getWidth(), pBaseSprite.getHeight());
			} else {
				this.mSpriteBatchVertexBuffer.add(pBaseSprite.getWidth(), pBaseSprite.getHeight(), pBaseSprite.getLocalToParentTransformation());
			}
	
			this.mSpriteBatchTextureRegionBuffer.add(textureRegion);
	
			this.mIndex++;
		}
		this.mDirty = true;
	}
	
	public void drawWithoutChecks(final BaseSprite pBaseSprite) {
		if(pBaseSprite.isVisible()) {
			final BaseTextureRegion textureRegion = pBaseSprite.getTextureRegion();
	
			if(pBaseSprite.getRotation() == 0 && !pBaseSprite.isScaled()) {
				this.mSpriteBatchVertexBuffer.add(pBaseSprite.getX(), pBaseSprite.getY(), pBaseSprite.getWidth(), pBaseSprite.getHeight());
			} else {
				this.mSpriteBatchVertexBuffer.add(pBaseSprite.getWidth(), pBaseSprite.getHeight(), pBaseSprite.getLocalToParentTransformation());
			}
	
			this.mSpriteBatchTextureRegionBuffer.add(textureRegion);
	
			this.mIndex++;
		}
		this.mDirty = true;
	}

	public void submit() {
		this.mVertices = this.mIndex * VERTICES_PER_RECTANGLE;

		this.mSpriteBatchVertexBuffer.submit();
		this.mSpriteBatchTextureRegionBuffer.submit();

		this.mIndex = 0;
		this.mSpriteBatchVertexBuffer.setIndex(0);
		this.mSpriteBatchTextureRegionBuffer.setIndex(0);
		this.mDirty = false;
		
		this.mDirty = (this.mIndex > 0) ? true : false;
        this.mIndex = 0;
	}

	private void initBlendFunction() {
		if(this.mTexture.getTextureOptions().mPreMultipyAlpha) {
			this.setBlendFunction(Shape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, Shape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT);
		}
	}

	protected void onInitDraw(final GL10 pGL) {
		GLHelper.setColor(pGL, this.mRed, this.mGreen, this.mBlue, this.mAlpha);

		GLHelper.enableVertexArray(pGL);
		GLHelper.blendFunction(pGL, this.mSourceBlendFunction, this.mDestinationBlendFunction);

		GLHelper.enableTextures(pGL);
		GLHelper.enableTexCoordArray(pGL);
	}

	protected void onApplyVertices(final GL10 pGL) {
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			final GL11 gl11 = (GL11)pGL;

			this.mSpriteBatchVertexBuffer.selectOnHardware(gl11);
			GLHelper.vertexZeroPointer(gl11);
		} else {
			GLHelper.vertexPointer(pGL, this.mSpriteBatchVertexBuffer.getFloatBuffer());
		}
	}

	private void onApplyTextureRegion(final GL10 pGL) {
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			final GL11 gl11 = (GL11)pGL;

			this.mSpriteBatchTextureRegionBuffer.selectOnHardware(gl11);

			this.mTexture.bind(pGL);
			GLHelper.texCoordZeroPointer(gl11);
		} else {
			this.mTexture.bind(pGL);
			GLHelper.texCoordPointer(pGL, this.mSpriteBatchTextureRegionBuffer.getFloatBuffer());
		}
	}

	private void drawVertices(final GL10 pGL, @SuppressWarnings("unused") final Camera pCamera) {
		pGL.glDrawArrays(GL10.GL_TRIANGLES, 0, this.mVertices);
	}

	private void assertCapacity(final int pIndex) {
		if(pIndex >= this.mCapacity) {
			throw new IllegalStateException("This supplied pIndex: '" + pIndex + "' is exceeding the capacity: '" + this.mCapacity + "' of this SpriteBatch!");
		}
	}

	private void assertCapacity() {
		if(this.mIndex == this.mCapacity) {
			throw new IllegalStateException("This SpriteBatch has already reached its capacity (" + this.mCapacity + ") !");
		}
	}

	protected void assertTexture(final BaseTextureRegion pTextureRegion) {
		if(pTextureRegion.getTexture() != this.mTexture) {
			throw new IllegalArgumentException("The supplied Texture does match the Texture of this SpriteBatch!");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
