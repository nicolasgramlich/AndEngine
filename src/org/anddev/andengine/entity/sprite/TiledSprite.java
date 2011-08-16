package org.anddev.andengine.entity.sprite;

import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.texture.region.ITiledTextureRegion;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttribute;

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
		super(pX, pY, pTiledTextureRegion, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Sprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType) {
		super(pX, pY, pTiledTextureRegion, pDrawType);
	}

	public TiledSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final Mesh pMesh) {
		super(pX, pY, pTiledTextureRegion, pMesh);
	}

	/**
	 * Uses a default {@link Mesh} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Sprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Sprite#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TiledSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final DrawType pDrawType) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion, pDrawType);
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

	// ===========================================================
	// Methods
	// ===========================================================

	public int getCurrentTileIndex() {
		return this.getTextureRegion().getTileIndex();
	}

	public void setCurrentTileIndex(final int pTileIndex) {
		if(this.getTextureRegion().setTileIndex(pTileIndex)) {
			this.onUpdateTextureCoordinates();
		}
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
