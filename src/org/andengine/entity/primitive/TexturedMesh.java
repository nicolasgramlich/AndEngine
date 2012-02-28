package org.andengine.entity.primitive;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
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
import org.andengine.util.debug.Debug;
import org.andengine.util.exception.MethodNotSupportedException;

import android.opengl.GLES20;
import android.renderscript.Mesh;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:44:50 - 09.02.2012
 */
public class TexturedMesh extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = TexturedMesh.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = TexturedMesh.VERTEX_INDEX_Y + 1;
	public static final int TEXTURECOORDINATES_INDEX_U = TexturedMesh.COLOR_INDEX + 1;
	public static final int TEXTURECOORDINATES_INDEX_V = TexturedMesh.TEXTURECOORDINATES_INDEX_U + 1;
	

	public static final int VERTEX_SIZE = 5;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(3)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ITexturedMeshVertexBufferObject mMeshVertexBufferObject;
	private int mVertexCountToDraw;
	private int mDrawMode;
	protected ITextureRegion mTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	/**
	 * Uses a default {@link HighPerformanceTexturedMeshVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TexturedMesh(final float pX, final float pY, final float[] pBufferData, final int pVertexCount, final DrawMode pDrawMode, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pBufferData, pVertexCount, pDrawMode, null, pVertexBufferObjectManager, DrawType.STATIC);
	}
	
	/**
	 * Uses a default {@link HighPerformanceTexturedMeshVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TexturedMesh(final float pX, final float pY, final float[] pBufferData, final int pVertexCount, final DrawMode pDrawMode, final ITextureRegion pTextureRegion,  final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pBufferData, pVertexCount, pDrawMode, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}
	
	/**
	 * Uses a default {@link HighPerformanceTexturedMeshVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TexturedMesh(final float pX, final float pY, final float[] pBufferData, final int pVertexCount, final DrawMode pDrawMode, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pBufferData, pVertexCount, pDrawMode, null, pVertexBufferObjectManager, pDrawType);
	}

	/**
	 * Uses a default {@link HighPerformanceTexturedMeshVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TexturedMesh(final float pX, final float pY, final float[] pBufferData, final int pVertexCount, final DrawMode pDrawMode, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pVertexCount, pDrawMode, pTextureRegion, new HighPerformanceTexturedMeshVertexBufferObject(pVertexBufferObjectManager, pBufferData, pVertexCount, pDrawType, true, TexturedMesh.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}
	
	/**
	 * Uses a default {@link HighPerformanceTexturedMeshVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TexturedMesh(final float pX, final float pY, final float[] pVertexX, final float[] pVertexY, final DrawMode pDrawMode, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pVertexX.length, pDrawMode, pTextureRegion, new HighPerformanceTexturedMeshVertexBufferObject(pVertexBufferObjectManager, buildVertexList(pVertexX, pVertexY), pVertexX.length, pDrawType, true, TexturedMesh.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public TexturedMesh(final float pX, final float pY, final int pVertexCount, final DrawMode pDrawMode, final ITextureRegion pTextureRegion, final ITexturedMeshVertexBufferObject pMeshVertexBufferObject) {
		super(pX, pY, PositionColorTextureCoordinatesShaderProgram.getInstance());

		this.mDrawMode = pDrawMode.getDrawMode();
		this.mTextureRegion = pTextureRegion;
		this.mMeshVertexBufferObject = pMeshVertexBufferObject;
		this.mVertexCountToDraw = pVertexCount;
		
		if( pTextureRegion != null)
		{
			this.setBlendingEnabled(true);
			this.initBlendFunction(pTextureRegion);
			this.onUpdateTextureCoordinates();
		
		}
		
		this.onUpdateVertices();
		this.onUpdateColor();

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
	public ITexturedMeshVertexBufferObject getVertexBufferObject() {
		return this.mMeshVertexBufferObject;
	}
	
	@Override
	public void reset() {
		super.reset();

		this.initBlendFunction(this.getTextureRegion().getTexture());
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		
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
		
		float[] bufferData = new float[TexturedMesh.VERTEX_SIZE * pVertexX.length];
		updateVertexList(pVertexX, pVertexY, bufferData);
		return bufferData;
	}
	
	protected static void updateVertexList(float[] pVertexX, float[] pVertexY, float[] pBufferData)
	{
		for( int i = 0; i < pVertexX.length; i++)
		{
			pBufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_X] = pVertexX[i];
			pBufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_Y] = pVertexY[i];
		}
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ITexturedMeshVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public float[] getBufferData();
		public void onUpdateColor(final TexturedMesh pMesh);
		public void onUpdateVertices(final TexturedMesh pMesh);
		public void onUpdateTextureCoordinates(final TexturedMesh pMesh);
	}

	public static class HighPerformanceTexturedMeshVertexBufferObject extends HighPerformanceVertexBufferObject implements ITexturedMeshVertexBufferObject {
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

		public HighPerformanceTexturedMeshVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final float[] pBufferData, final int pVertexCount, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
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
		public void onUpdateColor(final TexturedMesh pMesh) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pMesh.getColor().getABGRPackedFloat();

			for(int i = 0; i < this.mVertexCount; i++) {
				bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.COLOR_INDEX] = packedColor;
			}

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final TexturedMesh pMesh) {
			/* Since the buffer data is managed from the caller, we just mark the buffer data as dirty. */

			this.setDirtyOnHardware();
		}
		
		@Override
		public void onUpdateTextureCoordinates(final TexturedMesh pMesh) {
			final float[] bufferData = this.mBufferData;

			final ITextureRegion textureRegion = pMesh.getTextureRegion(); // TODO Optimize with field access?
			
			float textureWidth = textureRegion.getWidth();
			float textureHeight = textureRegion.getHeight();
			
			// x0 is mapped to u0
			// y0 is mapped to v0
			
			// TODO get initial mapping
			float x0 = 0; // pMesh.getX0();
			float y0 = 0; //pMesh.getY0();

			
			for(int i = 0; i < this.mVertexCount; i++) {
				float x = bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_X];
				float y = bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_Y];
				
				float u = (x - x0) / textureWidth;
				float v = (y - y0) / textureHeight;
				
				Debug.d("u = " + u );
				Debug.d("v = " + v );
				
				Debug.d("x = " + x );
				Debug.d("y = " + y );
				
				bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.TEXTURECOORDINATES_INDEX_V] = v;
			}
			
			Debug.d("v ---" );

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
