package org.andengine.entity.sprite;

import org.andengine.entity.sprite.vbo.HighPerformanceDiamondSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.IDiamondSpriteVertexBufferObject;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Unlike {@link Sprite}, the {@link DiamondSprite} class doesn't render the rectangular outline of a {@link ITextureRegion}, but cuts out a diamond.
 * <pre>
 * +--------+
 * |   /\   |
 * |  /  \  |
 * | /    \ |
 * |/      \|
 * |\      /|
 * | \    / |
 * |  \  /  |
 * |   \/   |
 * +--------+
 * </pre>
 *
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 20:26:32 - 24.10.2011
 */
public class DiamondSprite extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DiamondSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public DiamondSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public DiamondSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pDrawType);
	}

	public DiamondSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public DiamondSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final IDiamondSpriteVertexBufferObject pDiamondSpriteVertexBufferObject) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pDiamondSpriteVertexBufferObject);
	}

	public DiamondSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final IDiamondSpriteVertexBufferObject pDiamondSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pDiamondSpriteVertexBufferObject, pShaderProgram);
	}

	public DiamondSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public DiamondSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public DiamondSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, new HighPerformanceDiamondSpriteVertexBufferObject(pVertexBufferObjectManager, Sprite.SPRITE_SIZE, pDrawType, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public DiamondSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, new HighPerformanceDiamondSpriteVertexBufferObject(pVertexBufferObjectManager, Sprite.SPRITE_SIZE, pDrawType, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}

	public DiamondSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final IDiamondSpriteVertexBufferObject pDiamondSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pDiamondSpriteVertexBufferObject);
	}

	public DiamondSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final IDiamondSpriteVertexBufferObject pDiamondSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pDiamondSpriteVertexBufferObject, pShaderProgram);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
