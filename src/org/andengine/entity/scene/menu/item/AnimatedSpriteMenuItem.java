package org.andengine.entity.scene.menu.item;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:44:39 - 07.07.2010
 */
public class AnimatedSpriteMenuItem extends AnimatedSprite implements IMenuItem {
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

	public AnimatedSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(0, 0, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(0, 0, pTiledTextureRegion, pTiledSpriteVertexBufferObject);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(0, 0, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject);

		this.mID = pID;
	}

	public AnimatedSpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITiledTextureRegion pTiledTextureRegion, final ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(0, 0, pWidth, pHeight, pTiledTextureRegion, pTiledSpriteVertexBufferObject, pShaderProgram);

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
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onSelected() {
		/* Nothing. */
	}

	@Override
	public void onUnselected() {
		/* Nothing. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
