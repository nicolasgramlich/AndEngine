package org.andengine.entity.scene.menu.item;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 20:15:20 - 01.04.2010
 */
public class SpriteMenuItem extends Sprite implements IMenuItem {
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

	public SpriteMenuItem(final int pID, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pTextureRegion, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(0, 0, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(0, 0, pTextureRegion, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(0, 0, pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pVertexBufferObject) {
		super(0, 0, pTextureRegion, pVertexBufferObject);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(0, 0, pTextureRegion, pVertexBufferObject, pShaderProgram);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(0, 0, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(0, 0, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(0, 0, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pSpriteVertexBufferObject) {
		super(0, 0, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject);

		this.mID = pID;
	}

	public SpriteMenuItem(final int pID, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final ISpriteVertexBufferObject pSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(0, 0, pWidth, pHeight, pTextureRegion, pSpriteVertexBufferObject, pShaderProgram);

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
