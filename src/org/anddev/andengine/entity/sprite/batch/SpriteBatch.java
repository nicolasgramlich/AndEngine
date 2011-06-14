package org.anddev.andengine.entity.sprite.batch;

import static org.anddev.andengine.opengl.vertex.SpriteBatchVertexBuffer.VERTICES_PER_RECTANGLE;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.BaseTextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.buffer.SpriteBatchTextureRegionBuffer;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vertex.SpriteBatchVertexBuffer;

/**
 * TODO Texture could be semi-changeable, being resetting to null in end(...)
 * TODO Add sth like "SpriteGroup extends SpriteBatch"-subclass that has: "private final SmartList<BaseSprite> mSprites = new SmartList<BaseSprite>();" and draws all of them in onDrawSpriteBatch().
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

	private final Texture mTexture;
	private final int mCapacity;

	private int mCount;
	private int mSubmitCount;
	private boolean mDirty = false;

	private int mSourceBlendFunction;
	private int mDestinationBlendFunction;

	private final SpriteBatchVertexBuffer mSpriteBatchVertexBuffer;
	private final SpriteBatchTextureRegionBuffer mSpriteBatchTextureRegionBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteBatch(final Texture pTexture, final int pCapacity) {
		this.mTexture = pTexture;
		this.mCapacity = pCapacity;
		this.mSpriteBatchVertexBuffer = new SpriteBatchVertexBuffer(pCapacity, GL11.GL_STATIC_DRAW);
		this.mSpriteBatchTextureRegionBuffer = new SpriteBatchTextureRegionBuffer(pCapacity, GL11.GL_STATIC_DRAW);

		final BufferObjectManager bufferObjectManager = BufferObjectManager.getActiveInstance();
		bufferObjectManager.loadBufferObject(this.mSpriteBatchVertexBuffer);
		bufferObjectManager.loadBufferObject(this.mSpriteBatchTextureRegionBuffer);

		this.initBlendFunction();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mSourceBlendFunction = pSourceBlendFunction;
		this.mDestinationBlendFunction = pDestinationBlendFunction;
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

	// ===========================================================
	// Methods
	// ===========================================================

	protected void begin(final GL10 pGL) {
		//		pGL.glDepthMask(false); // TODO --> GLHelper
	}

	protected void onDrawSpriteBatch() {

	}

	protected void end(final GL10 pGL) {
		//		pGL.glDepthMask(true); // TODO --> GLHelper
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float)}.
	 */
	public void draw(final TextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
		if(this.mCount == this.mCapacity) {
			throw new IllegalStateException("This SpriteBatch has already reached its capacity (" + this.mCapacity + ") !");
		}

		if(pTextureRegion.getTexture() != this.mTexture) {
			throw new IllegalArgumentException();
		}

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mCount++;
		this.mDirty = true;
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float, float)}.
	 */
	public void draw(final TextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation) {
		if(this.mCount == this.mCapacity) {
			throw new IllegalStateException("This SpriteBatch has already reached its capacity (" + this.mCapacity + ") !");
		}

		if(pTextureRegion.getTexture() != this.mTexture) {
			throw new IllegalArgumentException();
		}

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pRotation);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mCount++;
		this.mDirty = true;
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float, float, float)}.
	 */
	public void draw(final TextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY) {
		if(this.mCount == this.mCapacity) {
			throw new IllegalStateException("This SpriteBatch has already reached its capacity (" + this.mCapacity + ") !");
		}

		if(pTextureRegion.getTexture() != this.mTexture) {
			throw new IllegalArgumentException();
		}

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pScaleX, pScaleY);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mCount++;
		this.mDirty = true;
	}

	/**
	 * @see {@link SpriteBatchVertexBuffer#add(float, float, float, float, float, float, float)}.
	 */
	public void draw(final TextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY) {
		if(this.mCount == this.mCapacity) {
			throw new IllegalStateException("This SpriteBatch has already reached its capacity (" + this.mCapacity + ") !");
		}

		if(pTextureRegion.getTexture() != this.mTexture) {
			throw new IllegalArgumentException();
		}

		this.mSpriteBatchVertexBuffer.add(pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mCount++;
		this.mDirty = true;
	}

	/**
	 * {@link SpriteBatchVertexBuffer#add(float, float, float, float, float, float, float, float)}.
	 */
	public void draw(final TextureRegion pTextureRegion, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		if(this.mCount == this.mCapacity) {
			throw new IllegalStateException("This SpriteBatch has already reached its capacity (" + this.mCapacity + ") !");
		}

		if(pTextureRegion.getTexture() != this.mTexture) {
			throw new IllegalArgumentException();
		}

		this.mSpriteBatchVertexBuffer.addInner(pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4);
		this.mSpriteBatchTextureRegionBuffer.add(pTextureRegion);

		this.mCount++;
		this.mDirty = true;
	}

	/**
	 * {@link SpriteBatchVertexBuffer#add(BaseSprite)}.
	 */
	public void draw(final BaseSprite pBaseSprite) {
		if(this.mCount == this.mCapacity) {
			throw new IllegalStateException("This SpriteBatch has already reached its capacity (" + this.mCapacity + ") !");
		}

		final BaseTextureRegion textureRegion = pBaseSprite.getTextureRegion();
		if(textureRegion.getTexture() != this.mTexture) {
			throw new IllegalArgumentException();
		}

		if(pBaseSprite.getRotation() == 0 && !pBaseSprite.isScaled()) {
			this.mSpriteBatchVertexBuffer.add(pBaseSprite.getX(), pBaseSprite.getY(), pBaseSprite.getWidth(), pBaseSprite.getHeight());
		} else {
			this.mSpriteBatchVertexBuffer.add(pBaseSprite.getWidth(), pBaseSprite.getHeight(), pBaseSprite.getLocalToParentTransformation());
		}

		this.mSpriteBatchTextureRegionBuffer.add(textureRegion);

		this.mCount++;
		this.mDirty = true;
	}

	public void submit() {
		this.mSubmitCount = this.mCount;

		this.mSpriteBatchVertexBuffer.submit();
		this.mSpriteBatchTextureRegionBuffer.submit();

		this.mCount = 0;
		this.mSpriteBatchVertexBuffer.setIndex(0);
		this.mSpriteBatchTextureRegionBuffer.setIndex(0);
		this.mDirty = false;
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

			GLHelper.bindTexture(pGL, this.mTexture.getHardwareTextureID());
			GLHelper.texCoordZeroPointer(gl11);
		} else {
			GLHelper.bindTexture(pGL, this.mTexture.getHardwareTextureID());
			GLHelper.texCoordPointer(pGL, this.mSpriteBatchTextureRegionBuffer.getFloatBuffer());
		}
	}

	private void drawVertices(final GL10 pGL, final Camera pCamera) {
		pGL.glDrawArrays(GL10.GL_TRIANGLES, 0, this.mSubmitCount * VERTICES_PER_RECTANGLE);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
