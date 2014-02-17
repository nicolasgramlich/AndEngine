package org.andengine.entity.sprite;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.vbo.HighPerformanceUniformColorSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.IUniformColorSpriteVertexBufferObject;
import org.andengine.opengl.shader.PositionTextureCoordinatesUniformColorShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:22:38 - 09.03.2010
 */
public class UniformColorSprite extends Sprite {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = UniformColorSprite.VERTEX_INDEX_X + 1;
	public static final int TEXTURECOORDINATES_INDEX_U = UniformColorSprite.VERTEX_INDEX_Y + 1;
	public static final int TEXTURECOORDINATES_INDEX_V = UniformColorSprite.TEXTURECOORDINATES_INDEX_U + 1;

	public static final int VERTEX_SIZE = 2 + 2;
	public static final int VERTICES_PER_SPRITE = 4;
	public static final int SPRITE_SIZE = UniformColorSprite.VERTEX_SIZE * UniformColorSprite.VERTICES_PER_SPRITE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public UniformColorSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public UniformColorSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public UniformColorSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pDrawType);
	}
	
	public UniformColorSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public UniformColorSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final IUniformColorSpriteVertexBufferObject pVertexBufferObject) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObject);
	}
	
	public UniformColorSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final IUniformColorSpriteVertexBufferObject pVertexBufferObject, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObject, pShaderProgram);
	}

	public UniformColorSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}
	
	public UniformColorSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public UniformColorSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager, pDrawType, PositionTextureCoordinatesUniformColorShaderProgram.getInstance());
	}

	public UniformColorSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, new HighPerformanceUniformColorSpriteVertexBufferObject(pVertexBufferObjectManager, UniformColorSprite.SPRITE_SIZE, pDrawType, true, UniformColorSprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}

	public UniformColorSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final IUniformColorSpriteVertexBufferObject pUniformColorSpriteVertexBufferObject) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pUniformColorSpriteVertexBufferObject, PositionTextureCoordinatesUniformColorShaderProgram.getInstance());
	}

	public UniformColorSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final IUniformColorSpriteVertexBufferObject pUniformColorSpriteVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pUniformColorSpriteVertexBufferObject, pShaderProgram);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		GLES20.glUniform4f(PositionTextureCoordinatesUniformColorShaderProgram.sUniformColorLocation, this.mColor.getRed(), this.mColor.getGreen(), this.mColor.getBlue(), this.mColor.getAlpha());
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mSpriteVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, UniformColorSprite.VERTICES_PER_SPRITE);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
