package org.andengine.entity.sprite.batch;

import org.andengine.entity.sprite.batch.vbo.ISpriteBatchVertexBufferObject;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:48:21 - 27.07.2011
 */
public abstract class DynamicSpriteBatch extends SpriteBatch {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DynamicSpriteBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pTexture, pCapacity, pVertexBufferObjectManager, DrawType.DYNAMIC);
	}

	public DynamicSpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, DrawType.DYNAMIC);
	}

	public DynamicSpriteBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pTexture, pCapacity, pVertexBufferObjectManager, pDrawType);
	}

	public DynamicSpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, pDrawType);
	}

	public DynamicSpriteBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(pTexture, pCapacity, pVertexBufferObjectManager, DrawType.DYNAMIC, pShaderProgram);
	}

	public DynamicSpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, DrawType.DYNAMIC, pShaderProgram);
	}

	public DynamicSpriteBatch(final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject) {
		super(pTexture, pCapacity, pSpriteBatchVertexBufferObject);
	}

	public DynamicSpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject) {
		super(pX, pY, pTexture, pCapacity, pSpriteBatchVertexBufferObject);
	}

	public DynamicSpriteBatch(final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(pTexture, pCapacity, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public DynamicSpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTexture, pCapacity, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public DynamicSpriteBatch(final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pTexture, pCapacity, pSpriteBatchVertexBufferObject, pShaderProgram);
	}

	public DynamicSpriteBatch(final float pX, final float pY, final ITexture pTexture, final int pCapacity, final ISpriteBatchVertexBufferObject pSpriteBatchVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pTexture, pCapacity, pSpriteBatchVertexBufferObject, pShaderProgram);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * @return <code>true</code> to submit, if you made any changes, or <code>false</code> otherwise.
	 */
	protected abstract boolean onUpdateSpriteBatch();

	@Override
	protected void begin() {
		super.begin();

		if (this.onUpdateSpriteBatch()) {
			this.submit();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
