package org.andengine.entity.scene.menu.item;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class TiledSpriteMenuItem extends TiledSprite implements IMenuItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TiledSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pVertexBufferObject) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObject);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObject, pShaderProgram);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pSpriteVertexBufferObject) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pSpriteVertexBufferObject);

		this.mID = pID;
	}

	public TiledSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pSpriteVertexBufferObject, pShaderProgram);

		this.mID = pID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getID() {
		return this.mID;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onSelected() {
		setCurrentTileIndex(1);
	}

	@Override
	public void onUnselected() {
		setCurrentTileIndex(0);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

