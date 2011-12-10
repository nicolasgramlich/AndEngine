package org.andengine.entity.sprite;

import java.nio.FloatBuffer;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
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

	public static final int VERTEX_SIZE = 2 + 1 + 2;
	public static final int VERTICES_PER_TILEDSPRITE = 6;
	public static final int TILEDSPRITE_SIZE = TiledSprite.VERTEX_SIZE * TiledSprite.VERTICES_PER_TILEDSPRITE;

	// ===========================================================
	// Fields
	// ===========================================================

	private final ITiledTextureRegion mTiledTextureRegion;
	private final ITiledSpriteVertexBufferObject mTiledSpriteVertexBufferObject;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion) {
		this(pX, pY, pTiledTextureRegion, DrawType.STATIC);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTiledTextureRegion, DrawType.STATIC, pShaderProgram);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType) {
		this(pX, pY, pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight(), pTiledTextureRegion, pDrawType);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight(), pTiledTextureRegion, pDrawType, pShaderProgram);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		this(pX, pY, pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight(), pTiledTextureRegion, pTiledSpriteVertexBufferObject);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTiledTextureRegion.getWidth(), pTiledTextureRegion.getHeight(), pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, DrawType.STATIC);
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, DrawType.STATIC, pShaderProgram);
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, new HighPerformanceTiledSpriteVertexBufferObject(TiledSprite.TILEDSPRITE_SIZE * pTiledTextureRegion.getTileCount(), pDrawType, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, new HighPerformanceTiledSpriteVertexBufferObject(TiledSprite.TILEDSPRITE_SIZE * pTiledTextureRegion.getTileCount(), pDrawType, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram, true);
	}

	protected TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram, final boolean pInit) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram, false);

		this.mTiledTextureRegion = pTiledTextureRegion;
		this.mTiledSpriteVertexBufferObject = pTiledSpriteVertexBufferObject;

		if(pInit) {
			this.onUpdateVertices();
			this.onUpdateColor();
			this.onUpdateTextureCoordinates();
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ITiledTextureRegion getTextureRegion() {
		return this.mTiledTextureRegion;
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mTiledSpriteVertexBufferObject.draw(GLES20.GL_TRIANGLES, this.getCurrentTileIndex() * TiledSprite.VERTICES_PER_TILEDSPRITE, TiledSprite.VERTICES_PER_TILEDSPRITE);
	}

	@Override
	protected void onUpdateColor() {
		this.mTiledSpriteVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mTiledSpriteVertexBufferObject.onUpdateVertices(this);
	}

	@Override
	protected void onUpdateTextureCoordinates() {
		this.mTiledSpriteVertexBufferObject.onUpdateTextureCoordinates(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public int getCurrentTileIndex() {
		return this.mTiledTextureRegion.getTileIndex();
	}

	public void setCurrentTileIndex(final int pTileIndex) {
		this.mTiledTextureRegion.setTileIndex(pTileIndex);
	}

	public void nextTile() {
		this.mTiledTextureRegion.nextTile();
	}

	public int getTileCount() {
		return this.mTiledTextureRegion.getTileCount();
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

		public HighPerformanceTiledSpriteVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
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

			final float packedColor = pTiledSprite.getColor().getPacked();

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

			final ITiledTextureRegion tiledTextureRegion = pTiledSprite.getTextureRegion();

			final int tileCount = pTiledSprite.getTileCount();
			int bufferDataOffset = 0;
			for(int i = 0; i < tileCount; i++) {
				final float u;
				final float v;
				final float u2;
				final float v2;

				if(pTiledSprite.isFlippedVertical()) { // TODO Optimize with field access?
					if(pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
						u = tiledTextureRegion.getU2(i);
						u2 = tiledTextureRegion.getU(i);
						v = tiledTextureRegion.getV2(i);
						v2 = tiledTextureRegion.getV(i);
					} else {
						u = tiledTextureRegion.getU(i);
						u2 = tiledTextureRegion.getU2(i);
						v = tiledTextureRegion.getV2(i);
						v2 = tiledTextureRegion.getV(i);
					}
				} else {
					if(pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
						u = tiledTextureRegion.getU2(i);
						u2 = tiledTextureRegion.getU(i);
						v = tiledTextureRegion.getV(i);
						v2 = tiledTextureRegion.getV2(i);
					} else {
						u = tiledTextureRegion.getU(i);
						u2 = tiledTextureRegion.getU2(i);
						v = tiledTextureRegion.getV(i);
						v2 = tiledTextureRegion.getV2(i);
					}
				}

				if(tiledTextureRegion.isRotated(i)) {
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

		public LowMemoryTiledSpriteVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
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

			final float packedColor = pTiledSprite.getColor().getPacked();

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

			final ITiledTextureRegion textureRegion = pTiledSprite.getTextureRegion();

			final int tileCount = pTiledSprite.getTileCount();
			int bufferDataOffset = 0;
			for(int i = 0; i < tileCount; i++) {
				final float u;
				final float v;
				final float u2;
				final float v2;

				if(pTiledSprite.isFlippedVertical()) { // TODO Optimize with field access?
					if(pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
						u = textureRegion.getU2(i);
						u2 = textureRegion.getU(i);
						v = textureRegion.getV2(i);
						v2 = textureRegion.getV(i);
					} else {
						u = textureRegion.getU(i);
						u2 = textureRegion.getU2(i);
						v = textureRegion.getV2(i);
						v2 = textureRegion.getV(i);
					}
				} else {
					if(pTiledSprite.isFlippedHorizontal()) { // TODO Optimize with field access?
						u = textureRegion.getU2(i);
						u2 = textureRegion.getU(i);
						v = textureRegion.getV(i);
						v2 = textureRegion.getV2(i);
					} else {
						u = textureRegion.getU(i);
						u2 = textureRegion.getU2(i);
						v = textureRegion.getV(i);
						v2 = textureRegion.getV2(i);
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
