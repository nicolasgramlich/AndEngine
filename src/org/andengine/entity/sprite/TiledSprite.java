package org.andengine.entity.sprite;

import java.nio.FloatBuffer;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:30:13 - 09.03.2010
 */
public class TiledSprite extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_SIZE = Sprite.VERTEX_SIZE;
	public static final int VERTICES_PER_TILEDSPRITE = 6;
	public static final int TILEDSPRITE_SIZE = TiledSprite.VERTEX_SIZE * TiledSprite.VERTICES_PER_TILEDSPRITE;

	// ===========================================================
	// Fields
	// ===========================================================

	private int mCurrentTileIndex;
	private final ITiledSpriteVertexBufferObject mTiledSpriteVertexBufferObject;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight(), pTiledTextureRegion, pVertexBufferObjectManager, pDrawType);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight(), pTiledTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		this(pX, pY, pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight(), pTiledTextureRegion, pTiledSpriteVertexBufferObject);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight(), pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, new HighPerformanceTiledSpriteVertexBufferObject(pVertexBufferObjectManager, TiledSprite.TILEDSPRITE_SIZE * pTiledTextureRegion.getTileCount(), pDrawType, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, new HighPerformanceTiledSpriteVertexBufferObject(pVertexBufferObjectManager, TiledSprite.TILEDSPRITE_SIZE * pTiledTextureRegion.getTileCount(), pDrawType, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);

		this.mTiledSpriteVertexBufferObject = pTiledSpriteVertexBufferObject;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ITextureRegion getTextureRegion() {
		return this.getTiledTextureRegion().getTextureRegion(this.mCurrentTileIndex);
	}

	public ITiledTextureRegion getTiledTextureRegion() {
		return (ITiledTextureRegion) this.mTextureRegion;
	}

	@Override
	public ITiledSpriteVertexBufferObject getVertexBufferObject() {
		return (ITiledSpriteVertexBufferObject) super.getVertexBufferObject();
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mTiledSpriteVertexBufferObject.draw(GLES20.GL_TRIANGLES, this.mCurrentTileIndex * TiledSprite.VERTICES_PER_TILEDSPRITE, TiledSprite.VERTICES_PER_TILEDSPRITE);
	}

	@Override
	protected void onUpdateColor() {
		this.getVertexBufferObject().onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.getVertexBufferObject().onUpdateVertices(this);
	}

	@Override
	protected void onUpdateTextureCoordinates() {
		this.getVertexBufferObject().onUpdateTextureCoordinates(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public int getCurrentTileIndex() {
		return this.mCurrentTileIndex;
	}

	public void setCurrentTileIndex(final int pCurrentTileIndex) {
		this.mCurrentTileIndex = pCurrentTileIndex;
	}

	public int getTileCount() {
		return this.getTiledTextureRegion().getTileCount();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ITiledSpriteVertexBufferObject extends ISpriteVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onUpdateColor(final TiledSprite pTiledSprite);
		public void onUpdateVertices(final TiledSprite pTiledSprite);
		public void onUpdateTextureCoordinates(final TiledSprite pTiledSprite);
	}

	public static class HighPerformanceTiledSpriteVertexBufferObject extends HighPerformanceSpriteVertexBufferObject implements ITiledSpriteVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public HighPerformanceTiledSpriteVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final TiledSprite pTiledSprite) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pTiledSprite.getColor().getABGRPackedFloat();

			final int tileCount = pTiledSprite.getTileCount();
			int bufferDataOffset = 0;
			for(int i = 0; i < tileCount; i++) {
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;
				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX] = packedColor;

				bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
			}

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final TiledSprite pTiledSprite) {
			final float[] bufferData = this.mBufferData;

			final float x = 0;
			final float y = 0;
			final float x2 = pTiledSprite.getWidth(); // TODO Optimize with field access?
			final float y2 = pTiledSprite.getHeight(); // TODO Optimize with field access?

			final int tileCount = pTiledSprite.getTileCount();
			int bufferDataOffset = 0;
			for(int i = 0; i < tileCount; i++) {
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y;

				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x;
				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y] = y2;

				bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
			}

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateTextureCoordinates(final TiledSprite pTiledSprite) {
			final float[] bufferData = this.mBufferData;

			final ITiledTextureRegion tiledTextureRegion = pTiledSprite.getTiledTextureRegion();

			final int tileCount = pTiledSprite.getTileCount();
			int bufferDataOffset = 0;
			for(int i = 0; i < tileCount; i++) {
				final ITextureRegion textureRegion = tiledTextureRegion.getTextureRegion(i);

				final float u;
				final float v;
				final float u2;
				final float v2;

				if(pTiledSprite.isFlippedVertical()) { // TODO Optimize with field access?
					if(pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
						u = textureRegion.getU2();
						u2 = textureRegion.getU();
						v = textureRegion.getV2();
						v2 = textureRegion.getV();
					} else {
						u = textureRegion.getU();
						u2 = textureRegion.getU2();
						v = textureRegion.getV2();
						v2 = textureRegion.getV();
					}
				} else {
					if(pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
						u = textureRegion.getU2();
						u2 = textureRegion.getU();
						v = textureRegion.getV();
						v2 = textureRegion.getV2();
					} else {
						u = textureRegion.getU();
						u2 = textureRegion.getU2();
						v = textureRegion.getV();
						v2 = textureRegion.getV2();
					}
				}

				if(textureRegion.isRotated()) {
					bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
					bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

					bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
					bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

					bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
					bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

					bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
					bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

					bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
					bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

					bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
					bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
				} else {
					bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
					bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

					bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
					bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

					bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
					bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

					bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
					bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

					bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
					bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

					bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
					bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
				}

				bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
			}

			this.setDirtyOnHardware();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class LowMemoryTiledSpriteVertexBufferObject extends LowMemorySpriteVertexBufferObject implements ITiledSpriteVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public LowMemoryTiledSpriteVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final TiledSprite pTiledSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final float packedColor = pTiledSprite.getColor().getABGRPackedFloat();

			final int tileCount = pTiledSprite.getTileCount();
			int bufferDataOffset = 0;
			for(int i = 0; i < tileCount; i++) {
				bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
				bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
				bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
				bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
				bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);
				bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.COLOR_INDEX, packedColor);

				bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
			}

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final TiledSprite pTiledSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final float x = 0;
			final float y = 0;
			final float x2 = pTiledSprite.getWidth(); // TODO Optimize with field access?
			final float y2 = pTiledSprite.getHeight(); // TODO Optimize with field access?

			final int tileCount = pTiledSprite.getTileCount();
			int bufferDataOffset = 0;
			for(int i = 0; i < tileCount; i++) {
				bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, x);
				bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, y);

				bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, x);
				bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, y2);

				bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, y);

				bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, y);

				bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, x);
				bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, y2);

				bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_X, x2);
				bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.VERTEX_INDEX_Y, y2);

				bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
			}

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateTextureCoordinates(final TiledSprite pTiledSprite) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final ITiledTextureRegion tiledTextureRegion = pTiledSprite.getTiledTextureRegion();

			final int tileCount = pTiledSprite.getTileCount();
			int bufferDataOffset = 0;
			for(int i = 0; i < tileCount; i++) {
				final ITextureRegion textureRegion = tiledTextureRegion.getTextureRegion(i);

				final float u;
				final float v;
				final float u2;
				final float v2;

				if(pTiledSprite.isFlippedVertical()) { // TODO Optimize with field access?
					if(pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
						u = textureRegion.getU2();
						u2 = textureRegion.getU();
						v = textureRegion.getV2();
						v2 = textureRegion.getV();
					} else {
						u = textureRegion.getU();
						u2 = textureRegion.getU2();
						v = textureRegion.getV2();
						v2 = textureRegion.getV();
					}
				} else {
					if(pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
						u = textureRegion.getU2();
						u2 = textureRegion.getU();
						v = textureRegion.getV();
						v2 = textureRegion.getV2();
					} else {
						u = textureRegion.getU();
						u2 = textureRegion.getU2();
						v = textureRegion.getV();
						v2 = textureRegion.getV2();
					}
				}

				if(textureRegion.isRotated()) {
					bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
					bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

					bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
					bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

					bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
					bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

					bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
					bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

					bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
					bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

					bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
					bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);
				} else {
					bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
					bufferData.put(bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

					bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
					bufferData.put(bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

					bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
					bufferData.put(bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

					bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
					bufferData.put(bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v);

					bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u);
					bufferData.put(bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);

					bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U, u2);
					bufferData.put(bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V, v2);
				}

				bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
			}

			this.setDirtyOnHardware();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
