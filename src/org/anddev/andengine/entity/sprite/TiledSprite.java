package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.opengl.mesh.HighPerformanceMesh;
import org.anddev.andengine.opengl.mesh.Mesh;
import org.anddev.andengine.opengl.texture.region.ITiledTextureRegion;
import org.anddev.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;

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
		super(pX, pY, pTiledTextureRegion, new HighPerformanceMesh(TiledSprite.TILEDSPRITE_SIZE * pTiledTextureRegion.getTileCount(), pDrawType, true, TiledSprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final HighPerformanceMesh pMesh) {
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
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, new HighPerformanceMesh(TiledSprite.TILEDSPRITE_SIZE * pTiledTextureRegion.getTileCount(), pDrawType, true, TiledSprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final HighPerformanceMesh pMesh) {
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
		this.mMesh.draw(GLES20.GL_TRIANGLES, this.getCurrentTileIndex() * VERTICES_PER_TILEDSPRITE, TiledSprite.VERTICES_PER_TILEDSPRITE);
	}

	@Override
	protected void onUpdateVertices() {
		final HighPerformanceVertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final float x = 0;
		final float y = 0;
		final float x2 = this.mWidth;
		final float y2 = this.mHeight;

		int tileCount = this.getTileCount();
		int bufferDataOffset = 0;
		for(int i = 0; i < tileCount; i++) {
			bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x;
			bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y;

			bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x;
			bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y2;

			bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y;

			bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y;

			bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x;
			bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y2;

			bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.VERTEX_INDEX_Y] = y2;

			bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
		}

		vertexBufferObject.setDirtyOnHardware();
	}

	@Override
	protected void onUpdateColor() {
		final HighPerformanceVertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final float packedColor = this.mColor.getPacked();

		int tileCount = this.getTileCount();
		int bufferDataOffset = 0;
		for(int i = 0; i < tileCount; i++) {
			bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;
			bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;
			bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;
			bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;
			bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;
			bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.COLOR_INDEX] = packedColor;

			bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
		}

		vertexBufferObject.setDirtyOnHardware();
	}

	@Override
	protected void onUpdateTextureCoordinates() {
		final HighPerformanceVertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();
		
		final ITiledTextureRegion textureRegion = this.getTextureRegion();

		int tileCount = this.getTileCount();
		int bufferDataOffset = 0;
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
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;
				
				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;
				
				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;
			} else {
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 0 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 1 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 2 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;
				
				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 3 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 4 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;
				
				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 5 * TiledSprite.VERTEX_SIZE + TiledSprite.TEXTURECOORDINATES_INDEX_V] = v2;
			}

			bufferDataOffset += TiledSprite.TILEDSPRITE_SIZE;
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
	}

	public void nextTile() {
		this.getTextureRegion().nextTile();
	}

	public int getTileCount() {
		return this.getTextureRegion().getTileCount();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
