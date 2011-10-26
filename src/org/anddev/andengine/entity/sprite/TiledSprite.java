package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.texture.region.ITiledTextureRegion;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttribute;

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

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link Mesh} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Sprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion) {
		this(pX, pY, pTiledTextureRegion, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Sprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType) {
		super(pX, pY, pTiledTextureRegion, new Mesh(TiledSprite.TILEDSPRITE_SIZE * pTiledTextureRegion.getTileCount(), pDrawType, true, TiledSprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final Mesh pMesh) {
		super(pX, pY, pTiledTextureRegion, pMesh);
	}

	/**
	 * Uses a default {@link Mesh} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Sprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion) {
		this(pX, pY, pWidth, pHeight, pTiledTextureRegion, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Sprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, new Mesh(TiledSprite.TILEDSPRITE_SIZE * pTiledTextureRegion.getTileCount(), pDrawType, true, TiledSprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final Mesh pMesh) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pMesh);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ITiledTextureRegion getTextureRegion() {
		return (ITiledTextureRegion)super.getTextureRegion();
	}
	
	@Override
	protected void draw(Camera pCamera) {
		this.mMesh.draw(this.mShaderProgram, GLES20.GL_TRIANGLES, this.getCurrentTileIndex() * VERTICES_PER_TILEDSPRITE, TiledSprite.VERTICES_PER_TILEDSPRITE);
	}

	@Override
	protected void onUpdateVertices() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final float x = 0;
		final float y = 0;
		final float x2 = this.mWidth;
		final float y2 = this.mHeight;

		int tileCount = this.getTileCount();
		int index = 0;
		for(int i = 0; i < tileCount; i++) {
			bufferData[index + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x;
			bufferData[index + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y;

			bufferData[index + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x;
			bufferData[index + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y2;

			bufferData[index + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x2;
			bufferData[index + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y;

			bufferData[index + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x2;
			bufferData[index + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y;

			bufferData[index + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x;
			bufferData[index + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y2;

			bufferData[index + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x2;
			bufferData[index + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y2;

			index += TiledSprite.TILEDSPRITE_SIZE;
		}
		vertexBufferObject.setDirtyOnHardware();
	}

	@Override
	protected void onUpdateColor() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final float packedColor = this.mColor.getPacked();

		int tileCount = this.getTileCount();
		int index = 0;
		for(int i = 0; i < tileCount; i++) {
			bufferData[index + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;
			bufferData[index + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;
			bufferData[index + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;
			bufferData[index + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;
			bufferData[index + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;
			bufferData[index + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;

			index += TiledSprite.TILEDSPRITE_SIZE;
		}

		vertexBufferObject.setDirtyOnHardware();
	}

	@Override
	protected void onUpdateTextureCoordinates() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();
		
		final ITiledTextureRegion textureRegion = this.getTextureRegion();

		int tileCount = this.getTileCount();
		int index = 0;
		for(int i = 0; i < tileCount; i++) {
			final float u;
			final float v;
			final float u2;
			final float v2;

			if(this.mFlippedVertical) {
				if(this.mFlippedHorizontal) {
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
				if(this.mFlippedHorizontal) {
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
				bufferData[index + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;
				
				bufferData[index + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;
				
				bufferData[index + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;
			} else {
				bufferData[index + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;
				
				bufferData[index + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;
				
				bufferData[index + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;
			}

			index += TiledSprite.TILEDSPRITE_SIZE;
		}
		vertexBufferObject.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public int getCurrentTileIndex() {
		return this.getTextureRegion().getTileIndex();
	}

	public void setCurrentTileIndex(final int pTileIndex) {
		this.getTextureRegion().setTileIndex(pTileIndex);
		this.onUpdateTextureCoordinates();
	}

	public void nextTile() {
		this.getTextureRegion().nextTile();
		this.onUpdateTextureCoordinates();
	}

	public int getTileCount() {
		return this.getTextureRegion().getTileCount();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
