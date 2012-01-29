package org.andengine.entity.primitive;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle.HighPerformanceRectangleVertexBufferObject;
import org.andengine.entity.shape.PolygonShape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.util.constants.ShaderProgramConstants;
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
 * @since 22:10:11 - 28.01.2012
 */
public class Polygon extends PolygonShape {
	// ===========================================================
	// Constants
	// ===========================================================
	
	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Rectangle.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Rectangle.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected final IPolygonVertexBufferObject mPolygonVertexBufferObject;
	protected float[] mVertexX;
	protected float[] mVertexY;
	protected ArrayList<Vector2d> mVertexTriangles;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Rectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Polygon(final float pX, final float pY, final float[] pVertexX, float[] pVertexY, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pVertexX, pVertexY, pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Rectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	private Polygon(final float pX, final float pY, final float[] pVertexX, float[] pVertexY, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, PositionColorShaderProgram.getInstance());
		
		mVertexX = pVertexX;
		mVertexY = pVertexY;
		assert( mVertexX.length == mVertexY.length );
		
		mVertexTriangles = Triangulate.process(pVertexX, pVertexY);
		assert( mVertexTriangles != null );

		this.mPolygonVertexBufferObject = new HighPerformancePolygonVertexBufferObject(pVertexBufferObjectManager, VERTEX_SIZE * mVertexTriangles.size(), pDrawType, true, Polygon.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
		
		onUpdateVertices();
		this.onUpdateColor();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public float[] getVertexX()
	{
		return mVertexX;
	}
	
	public float[] getVertexY()
	{
		return mVertexY;
	}
	
	public void updateVertices( float[] pVertexX, float[] pVertexY )
	{
		mVertexX = pVertexX;
		mVertexY = pVertexY;
		assert( mVertexX.length == mVertexY.length );
		
		int oldVertexCount = mVertexTriangles.size();
		
		mVertexTriangles = Triangulate.process(pVertexX, pVertexY);
		assert( mVertexTriangles != null );
		assert( oldVertexCount >= mVertexTriangles.size() );
		
		onUpdateVertices();
	}
	
	public ArrayList<Vector2d> getTriangles()
	{
		return mVertexTriangles;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public IPolygonVertexBufferObject getVertexBufferObject() {
		return this.mPolygonVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		this.mPolygonVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		// TODO let the user choose the mode
		this.mPolygonVertexBufferObject.draw(GLES20.GL_TRIANGLES, getTriangles().size());
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IPolygonVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onUpdateColor(final Polygon pPolygon);
		public void onUpdateVertices(final Polygon pPolygon);
	}

	public static class HighPerformancePolygonVertexBufferObject extends HighPerformanceVertexBufferObject implements IPolygonVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public HighPerformancePolygonVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final Polygon pPolygon) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pPolygon.getColor().getPacked();
			
			int nbVertexInTriangles = pPolygon.getTriangles().size();
			for( int i = 0; i < nbVertexInTriangles; i++)
			{	
				// TODO use color per vertex
				bufferData[i * Polygon.VERTEX_SIZE + Polygon.COLOR_INDEX] = packedColor;
			}

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final Polygon pPolygon) {
			final float[] bufferData = this.mBufferData;
			
			ArrayList<Vector2d> vertexTriangles = pPolygon.getTriangles();
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

	public static class LowMemoryPolygonVertexBufferObject extends LowMemoryVertexBufferObject implements IPolygonVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public LowMemoryPolygonVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final Polygon pPolygon) {
			
			// TODO
			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final Polygon pPolygon) {
			
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

	public static class Vector2d
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
	
	public static class Triangulate {
		// Original code by John W. RATCLIFF
		// Java port by Rodrigo Castro
		
		// COTD Entry submitted by John W. Ratcliff [jratcliff@verant.com]

		// ** THIS IS A CODE SNIPPET WHICH WILL EFFICIEINTLY TRIANGULATE ANY
		// ** POLYGON/CONTOUR (without holes) AS A STATIC CLASS.  THIS SNIPPET
		// ** IS COMPRISED OF 3 FILES, TRIANGULATE.H, THE HEADER FILE FOR THE
		// ** TRIANGULATE BASE CLASS, TRIANGULATE.CPP, THE IMPLEMENTATION OF
		// ** THE TRIANGULATE BASE CLASS, AND TEST.CPP, A SMALL TEST PROGRAM
		// ** DEMONSTRATING THE USAGE OF THE TRIANGULATOR.  THE TRIANGULATE
		// ** BASE CLASS ALSO PROVIDES TWO USEFUL HELPER METHODS, ONE WHICH
		// ** COMPUTES THE AREA OF A POLYGON, AND ANOTHER WHICH DOES AN EFFICENT
		// ** POINT IN A TRIANGLE TEST.
		// ** SUBMITTED BY JOHN W. RATCLIFF (jratcliff@verant.com) July 22, 2000

		
		/*****************************************************************/
		/** Static class to triangulate any contour/polygon efficiently **/
		/** You should replace Vector2d with whatever your own Vector   **/
		/** class might be.  Does not support polygons with holes.      **/
		/** Uses STL vectors to represent a dynamic array of vertices.  **/
		/** This code snippet was submitted to FlipCode.com by          **/
		/** John W. Ratcliff (jratcliff@verant.com) on July 22, 2000    **/
		/** I did not write the original code/algorithm for this        **/
		/** this triangulator, in fact, I can't even remember where I   **/
		/** found it in the first place.  However, I did rework it into **/
		/** the following black-box static class so you can make easy   **/
		/** use of it in your own code.  Simply replace Vector2d with   **/
		/** whatever your own Vector implementation might be.           **/
		/*****************************************************************/

		final static float EPSILON = 0.0000000001f;

		// compute area of a contour/polygon
		protected static float area(final Vector2d[] contour)
		{

			int n = contour.length;
	
			float A=0.0f;
	
			for(int p=n-1,q=0; q<n; p=q++)
			{
				A+= contour[p].getX()*contour[q].getY() - contour[q].getX()*contour[p].getY();
			}
			return A*0.5f;
		}

	   /*
	     InsideTriangle decides if a point P is Inside of the triangle
	     defined by A, B, C.
	   */
		public static boolean insideTriangle(float Ax, float Ay,
		                      float Bx, float By,
		                      float Cx, float Cy,
		                      float Px, float Py)

		{
			float ax, ay, bx, by, cx, cy, apx, apy, bpx, bpy, cpx, cpy;
			float cCROSSap, bCROSScp, aCROSSbp;
			
			ax = Cx - Bx;  ay = Cy - By;
			bx = Ax - Cx;  by = Ay - Cy;
			cx = Bx - Ax;  cy = By - Ay;
			apx= Px - Ax;  apy= Py - Ay;
			bpx= Px - Bx;  bpy= Py - By;
			cpx= Px - Cx;  cpy= Py - Cy;
			
			aCROSSbp = ax*bpy - ay*bpx;
			cCROSSap = cx*apy - cy*apx;
			bCROSScp = bx*cpy - by*cpx;
			
			return ((aCROSSbp >= 0.0f) && (bCROSScp >= 0.0f) && (cCROSSap >= 0.0f));
		};

		protected static boolean Snip(final Vector2d[] contour,int u,int v,int w,int n,int[] V)
		{
			int p;
			float Ax, Ay, Bx, By, Cx, Cy, Px, Py;
	
			Ax = contour[V[u]].getX();
			Ay = contour[V[u]].getY();
	
			Bx = contour[V[v]].getX();
			By = contour[V[v]].getY();
	
			Cx = contour[V[w]].getX();
			Cy = contour[V[w]].getY();
	
			if ( EPSILON > (((Bx-Ax)*(Cy-Ay)) - ((By-Ay)*(Cx-Ax))) ) 
				return false;
	
			for (p=0;p<n;p++)
			{
				if( (p == u) || (p == v) || (p == w) ) 
					continue;
				Px = contour[V[p]].getX();
				Py = contour[V[p]].getY();
				if (insideTriangle(Ax,Ay,Bx,By,Cx,Cy,Px,Py)) 
					return false;
			}
	
			return true;
		}
		
		// triangulate a contour/polygon, places results in a list
		// as series of triangles.
		public static ArrayList<Vector2d> process(final float[] pX, final float[] pY)
		{
			
			final Vector2d[] contour = Vector2d.floatToList(pX, pY);
			
			/* allocate and initialize list of Vertices in polygon */
			int n = contour.length;
			if ( n < 3 ) 
				return null;
	
			int[] V = new int[n];
			ArrayList<Vector2d> result = new ArrayList<Vector2d>(n);
			/* we want a counter-clockwise polygon in V */
	
			if ( 0.0f < area(contour) )
				for (int v=0; v<n; v++) 
					V[v] = v;
			else
				for(int v=0; v<n; v++) 
				V[v] = (n-1)-v;
	
			int nv = n;
	
			/*  remove nv-2 Vertices, creating 1 triangle every time */
			int count = 2*nv;   /* error detection */
			for(int v=nv-1; nv>2; )
			{
				/* if we loop, it is probably a non-simple polygon */
				if (0 >= (count--))
				{
					//** Triangulate: ERROR - probable bad polygon!
					return null;
				}
		
				/* three consecutive vertices in current polygon, <u,v,w> */
				int u = v  ; 
				if (nv <= u) 
					u = 0;     /* previous */
				v = u+1;
				if (nv <= v) 
					v = 0;     /* new v    */
				int w = v+1; 
				if (nv <= w) 
					w = 0;     /* next     */
				if ( Snip(contour,u,v,w,nv,V) )
				{
					int a,b,c,s,t;
			
					/* true names of the vertices */
					a = V[u]; b = V[v]; c = V[w];
					/* output Triangle */
					result.add( contour[a] );
					result.add( contour[b] );
					result.add( contour[c] );
			
					/* remove v from remaining polygon */
					for(s=v,t=v+1;t<nv;s++,t++) 
						V[s] = V[t]; nv--;
					
					/* resest error detection counter */
					count = 2*nv;
				}
			}
			return result;
		}
	}
}
