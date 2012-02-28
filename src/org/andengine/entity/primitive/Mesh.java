package org.andengine.entity.primitive;

import java.security.spec.MGF1ParameterSpec;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.exception.MethodNotSupportedException;

import android.opengl.GLES20;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:44:50 - 09.02.2012
 */
public class Mesh extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Mesh.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Mesh.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
	.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
	.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
	.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IMeshVertexBufferObject mMeshVertexBufferObject;
	private int mVertexCountToDraw;
	private int mDrawMode;
	protected ITextureRegion mTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	/**
	 * Uses a default {@link HighPerformanceMeshVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Mesh(final float pX, final float pY, final float[] pBufferData, final int pVertexCount, final DrawMode pDrawMode, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pBufferData, pVertexCount, pDrawMode, pVertexBufferObjectManager, DrawType.STATIC);
		mTextureRegion = pTextureRegion;
		this.onUpdateTextureCoordinates();
	}

	/**
	 * Uses a default {@link HighPerformanceMeshVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Mesh(final float pX, final float pY, final float[] pBufferData, final int pVertexCount, final DrawMode pDrawMode, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pBufferData, pVertexCount, pDrawMode, pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link HighPerformanceMeshVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Mesh(final float pX, final float pY, final float[] pBufferData, final int pVertexCount, final DrawMode pDrawMode, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pVertexCount, pDrawMode, new HighPerformanceMeshVertexBufferObject(pVertexBufferObjectManager, pBufferData, pVertexCount, pDrawType, true, Mesh.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}
	
	/**
	 * Uses a default {@link HighPerformanceMeshVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Mesh(final float pX, final float pY, final float[] pVertexX, final float[] pVertexY, final DrawMode pDrawMode, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pVertexX.length, pDrawMode, new HighPerformanceMeshVertexBufferObject(pVertexBufferObjectManager, buildVertexList(pVertexX, pVertexY), pVertexX.length, pDrawType, true, Mesh.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public Mesh(final float pX, final float pY, final int pVertexCount, final DrawMode pDrawMode, final IMeshVertexBufferObject pMeshVertexBufferObject) {
		super(pX, pY, PositionColorShaderProgram.getInstance());

		this.mDrawMode = pDrawMode.getDrawMode();
		this.mMeshVertexBufferObject = pMeshVertexBufferObject;
		this.mVertexCountToDraw = pVertexCount;

		this.mMeshVertexBufferObject.setDirtyOnHardware();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float[] getBufferData() {
		return this.mMeshVertexBufferObject.getBufferData();
	}

	public void setVertexCountToDraw(final int pVertexCountToDraw) {
		this.mVertexCountToDraw = pVertexCountToDraw;
	}

	public void setDrawMode(final DrawMode pDrawMode) {
		this.mDrawMode = pDrawMode.mDrawMode;
	}
	
	public ITextureRegion getTextureRegion() {
		return this.mTextureRegion;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public IMeshVertexBufferObject getVertexBufferObject() {
		return this.mMeshVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		
		// Check if polygon uses a texture
		if( mTextureRegion != null)
			this.mTextureRegion.getTexture().bind(pGLState);
		
		this.mMeshVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mMeshVertexBufferObject.draw(this.mDrawMode, this.mVertexCountToDraw);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mMeshVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mMeshVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mMeshVertexBufferObject.onUpdateVertices(this);
	}
	
	protected void onUpdateTextureCoordinates() {
		this.mMeshVertexBufferObject.onUpdateTextureCoordinates(this);
	}

	@Override
	@Deprecated
	public boolean contains(final float pX, final float pY) {
		throw new MethodNotSupportedException();
	}

	@Override
	public boolean collidesWith(final IShape pOtherShape) {
		if(pOtherShape instanceof Line) {
			// TODO
			return false;
		} else if(pOtherShape instanceof RectangularShape) {
			// TODO
			return false;
		} else {
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected static float[] buildVertexList(float[] pVertexX, float[] pVertexY)
	{
		assert( pVertexX.length == pVertexY.length );
		
		float[] bufferData = new float[VERTEX_SIZE * pVertexX.length];
		updateVertexList(pVertexX, pVertexY, bufferData);
		return bufferData;
	}
	
	protected static void updateVertexList(float[] pVertexX, float[] pVertexY, float[] pBufferData)
	{
		for( int i = 0; i < pVertexX.length; i++)
		{
			pBufferData[(i * Mesh.VERTEX_SIZE) + Mesh.VERTEX_INDEX_X] = pVertexX[i];
			pBufferData[(i * Mesh.VERTEX_SIZE) + Mesh.VERTEX_INDEX_Y] = pVertexY[i];
		}
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IMeshVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public float[] getBufferData();
		public void onUpdateColor(final Mesh pMesh);
		public void onUpdateVertices(final Mesh pMesh);
		public void onUpdateTextureCoordinates(final Mesh pMesh);
	}

	public static class HighPerformanceMeshVertexBufferObject extends HighPerformanceVertexBufferObject implements IMeshVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mVertexCount;

		// ===========================================================
		// Constructors
		// ===========================================================

		public HighPerformanceMeshVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final float[] pBufferData, final int pVertexCount, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pBufferData, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);

			this.mVertexCount = pVertexCount;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final Mesh pMesh) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pMesh.getColor().getABGRPackedFloat();

			for(int i = 0; i < this.mVertexCount; i++) {
				bufferData[(i * Mesh.VERTEX_SIZE) + Mesh.COLOR_INDEX] = packedColor;
			}

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final Mesh pMesh) {
			/* Since the buffer data is managed from the caller, we just mark the buffer data as dirty. */

			this.setDirtyOnHardware();
		}
		
		@Override
		public void onUpdateTextureCoordinates(final Mesh pMesh) {
			final float[] bufferData = this.mBufferData;

			final ITextureRegion textureRegion = pMesh.getTextureRegion(); // TODO Optimize with field access?

			final float u;
			final float v;
			final float u2;
			final float v2;

			u = textureRegion.getU();
			u2 = textureRegion.getU2();
			v = textureRegion.getV();
			v2 = textureRegion.getV2();

			if(textureRegion.isRotated()) {
				bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
			} else {
				bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[0 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[1 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[2 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[3 * Sprite.VERTEX_SIZE + Sprite.TEXTURECOORDINATES_INDEX_V] = v2;
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

	public static enum DrawMode {
		// ===========================================================
		// Elements
		// ===========================================================

		POINTS(GLES20.GL_POINTS),
		LINE_STRIP(GLES20.GL_LINE_STRIP),
		LINE_LOOP(GLES20.GL_LINE_LOOP),
		LINES(GLES20.GL_LINES),
		TRIANGLE_STRIP(GLES20.GL_TRIANGLE_STRIP),
		TRIANGLE_FAN(GLES20.GL_TRIANGLE_FAN),
		TRIANGLES(GLES20.GL_TRIANGLES);

		// ===========================================================
		// Constants
		// ===========================================================

		public final int mDrawMode;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		private DrawMode(final int pDrawMode) {
			this.mDrawMode = pDrawMode;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getDrawMode() {
			return this.mDrawMode;
		}

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
}
