package org.andengine.entity.primitive;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle.HighPerformanceRectangleVertexBufferObject;
import org.andengine.entity.shape.PolygonShape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.LowMemoryVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

import android.opengl.GLES20;
import android.util.Log;

/**
 * 
 * @author Rodrigo Castro
 * @since 22:43:05 - 30.01.2012
 */
public abstract class PolygonBase extends PolygonShape {
	// ===========================================================
		// Constants
		// ===========================================================
		
		protected static final int VERTEX_INDEX_X = 0;
		protected static final int VERTEX_INDEX_Y = VERTEX_INDEX_X + 1;
		protected static final int COLOR_INDEX = VERTEX_INDEX_Y + 1;

		protected static final int VERTEX_SIZE = 2 + 1;

		public static final float VERTEX_SIZE_DEFAULT_RATIO = 1.f;
		public static final float VERTEX_SIZE_EXTRA_RATIO = 1.3f;
		
		public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
			.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
			.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
			.build();
		
		// ===========================================================
		// Fields
		// ===========================================================
		
		protected final IPolygonBaseVertexBufferObject mPolygonVertexBufferObject;
		protected ArrayList<Vector2d> mVertices;
		protected int mCapacity;
		protected DrawMode mDrawMode;

		// ===========================================================
		// Constructors
		// ===========================================================

		/**
		 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Rectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
		 */
		protected PolygonBase(final float pX, final float pY, ArrayList<Vector2d> pVertices, final float vertexSizeRatio, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawMode pDrawMode, final DrawType pDrawType) {
			super(pX, pY, PositionColorShaderProgram.getInstance());

			mDrawMode = pDrawMode;
			mVertices = pVertices;
			mCapacity = (int) (vertexSizeRatio * VERTEX_SIZE * pVertices.size());
			this.mPolygonVertexBufferObject = new HighPerformancePolygonBaseVertexBufferObject(pVertexBufferObjectManager, mCapacity, pDrawType, true, Polygon.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
			
			onUpdateVertices();
			this.onUpdateColor();

			this.setBlendingEnabled(true);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================
		
		/**
		 * 
		 * @param pVertexX
		 * @param pVertexY
		 * @return 	true if vertices were correctly updated
		 * 			false otherwise
		 */
		protected boolean updateVertices( ArrayList<Vector2d> pVertices )
		{
			
			if (mCapacity < pVertices.size())
			{
				Log.e("AndEngine", "Error: Polygon - Not enough space to accomodate extra vertices");
				return false;
			}
			else
			{
				mVertices = pVertices;
			}
			
			onUpdateVertices();
			
			return true;
		}
		
		protected ArrayList<Vector2d> getVertices()
		{
			return mVertices;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public IPolygonBaseVertexBufferObject getVertexBufferObject() {
			return this.mPolygonVertexBufferObject;
		}

		@Override
		protected void preDraw(final GLState pGLState, final Camera pCamera) {
			super.preDraw(pGLState, pCamera);

			this.mPolygonVertexBufferObject.bind(pGLState, this.mShaderProgram);
		}

		@Override
		protected void draw(final GLState pGLState, final Camera pCamera) {
			this.mPolygonVertexBufferObject.draw(mDrawMode.getUsage(), getVertices().size());
		}

		@Override
		protected void postDraw(final GLState pGLState, final Camera pCamera) {
			this.mPolygonVertexBufferObject.unbind(pGLState, this.mShaderProgram);

			super.postDraw(pGLState, pCamera);
		}

		@Override
		protected void onUpdateColor() {
			this.mPolygonVertexBufferObject.onUpdateColor(this);
		}

		@Override
		protected void onUpdateVertices() {
			this.mPolygonVertexBufferObject.onUpdateVertices(this);
		}

		// ===========================================================
		// Methods
		// ===========================================================
		
		protected static ArrayList<Vector2d> buildVector2dList(float[] pVertexX, float[] pVertexY)
		{
			assert( pVertexX.length == pVertexY.length );
			
			ArrayList<Vector2d> vertices = new ArrayList<PolygonBase.Vector2d>(pVertexX.length);
			for( int i = 0; i < pVertexX.length; i++)
			{
				Vector2d v = new Vector2d(pVertexX[i], pVertexY[i]);
				vertices.add(v);
			}
			return vertices;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static interface IPolygonBaseVertexBufferObject extends IVertexBufferObject {
			// ===========================================================
			// Constants
			// ===========================================================

			// ===========================================================
			// Methods
			// ===========================================================

			public void onUpdateColor(final PolygonBase pPolygon);
			public void onUpdateVertices(final PolygonBase pPolygon);
		}

		public static class HighPerformancePolygonBaseVertexBufferObject extends HighPerformanceVertexBufferObject implements IPolygonBaseVertexBufferObject {
			// ===========================================================
			// Constants
			// ===========================================================

			// ===========================================================
			// Fields
			// ===========================================================

			// ===========================================================
			// Constructors
			// ===========================================================

			public HighPerformancePolygonBaseVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
				super(pVertexBufferObjectManager, pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
			}

			// ===========================================================
			// Getter & Setter
			// ===========================================================

			// ===========================================================
			// Methods for/from SuperClass/Interfaces
			// ===========================================================

			@Override
			public void onUpdateColor(final PolygonBase pPolygon) {
				final float[] bufferData = this.mBufferData;

				final float packedColor = pPolygon.getColor().getFloatPacked();
				
				int nbVertexInTriangles = pPolygon.getVertices().size();
				for( int i = 0; i < nbVertexInTriangles; i++)
				{	
					// TODO use color per vertex
					bufferData[i * Polygon.VERTEX_SIZE + Polygon.COLOR_INDEX] = packedColor;
				}

				this.setDirtyOnHardware();
			}

			@Override
			public void onUpdateVertices(final PolygonBase pPolygon) {
				final float[] bufferData = this.mBufferData;
				
				ArrayList<Vector2d> vertexTriangles = pPolygon.getVertices();
				int nbVertexInTriangles = vertexTriangles.size();
				
				for( int i = 0; i < nbVertexInTriangles; i++) 
				{	
					bufferData[i * Polygon.VERTEX_SIZE + Polygon.VERTEX_INDEX_X] = vertexTriangles.get(i).getX();
					bufferData[i * Polygon.VERTEX_SIZE + Polygon.VERTEX_INDEX_Y] = vertexTriangles.get(i).getY();
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

		public static class LowMemoryPolygonBaseVertexBufferObject extends LowMemoryVertexBufferObject implements IPolygonBaseVertexBufferObject {
			// ===========================================================
			// Constants
			// ===========================================================

			// ===========================================================
			// Fields
			// ===========================================================

			// ===========================================================
			// Constructors
			// ===========================================================

			public LowMemoryPolygonBaseVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
				super(pVertexBufferObjectManager, pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
			}

			// ===========================================================
			// Getter & Setter
			// ===========================================================

			// ===========================================================
			// Methods for/from SuperClass/Interfaces
			// ===========================================================

			@Override
			public void onUpdateColor(final PolygonBase pPolygon) {
				
				// TODO
				this.setDirtyOnHardware();
			}

			@Override
			public void onUpdateVertices(final PolygonBase pPolygon) {
				
				// TODO
				this.setDirtyOnHardware();
			}

			// ===========================================================
			// Methods
			// ===========================================================

			// ===========================================================
			// Inner and Anonymous Classes
			// ===========================================================
		}

		protected static class Vector2d
		{
			
			// ===========================================================
			// Constants
			// ===========================================================

			// ===========================================================
			// Fields
			// ===========================================================

			private float mX;
			private float mY;
			
			// ===========================================================
			// Constructors
			// ===========================================================
			
			public Vector2d(float x, float y)
			{
				set(x,y);
			};
			
			// ===========================================================
			// Getter & Setter
			// ===========================================================

			public float getX()
			{
				return mX; 
			}
			
			public float getY()
			{
				return mY; 
			}

			public void  set(float x, float y)
			{
				mX = x;
				mY = y;
			}
			
			protected static Vector2d[] floatToList( float pX[], float pY[] )
			{
				assert( pX.length == pY.length );
				Vector2d[] vertex = new Vector2d[pX.length];
				for( int i = 0; i < pX.length; i++ )
				{	
					vertex[i] = new Vector2d(pX[i], pY[i]);
				}
				return vertex;
			}
		}
		
		public static enum DrawMode {
			// ===========================================================
			// Elements
			// ===========================================================

			GL_POINTS(GLES20.GL_POINTS),
			GL_LINE_STRIP(GLES20.GL_LINE_STRIP),
			GL_LINE_LOOP(GLES20.GL_LINE_LOOP),
			GL_LINES(GLES20.GL_LINES),
			GL_TRIANGLE_STRIP(GLES20.GL_TRIANGLE_STRIP),
            GL_TRIANGLE_FAN(GLES20.GL_TRIANGLE_FAN),
            GL_TRIANGLES(GLES20.GL_TRIANGLES);
            

			// ===========================================================
			// Constants
			// ===========================================================

			private final int mUsage;

			// ===========================================================
			// Fields
			// ===========================================================

			// ===========================================================
			// Constructors
			// ===========================================================

			private DrawMode(final int pUsage) {
				this.mUsage = pUsage;
			}

			// ===========================================================
			// Getter & Setter
			// ===========================================================

			public int getUsage() {
				return this.mUsage;
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
