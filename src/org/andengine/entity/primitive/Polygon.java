package org.andengine.entity.primitive;

import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle.HighPerformanceRectangleVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;

import android.util.Log;

/**
 * 
 * @author Rodrigo Castro
 * @since 22:10:11 - 28.01.2012
 */
public class Polygon extends PolygonBase {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected float[] mVertexX;
	protected float[] mVertexY;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link PolygoonBase#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Polygon(final float pX, final float pY, final float[] pVertexX, float[] pVertexY, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pVertexX, pVertexY, VERTEX_SIZE_DEFAULT_RATIO, pVertexBufferObjectManager, DrawType.STATIC);
	}
	
	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link PolygoonBase#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Polygon(final float pX, final float pY, final float[] pVertexX, float[] pVertexY, final float vertexSizeRatio, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pVertexX, pVertexY, vertexSizeRatio, pVertexBufferObjectManager, DrawType.STATIC);
	}


	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Rectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Polygon(final float pX, final float pY, final float[] pVertexX, float[] pVertexY, final float vertexSizeRatio, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, Triangulate.process(pVertexX, pVertexY), vertexSizeRatio, pVertexBufferObjectManager, DrawMode.GL_TRIANGLES, pDrawType);
		
		mVertexX = pVertexX;
		mVertexY = pVertexY;
		assert( mVertexX.length == mVertexY.length );

		onUpdateVertices();
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
	
	/**
	 * 
	 * @param pVertexX
	 * @param pVertexY
	 * @return 	true if vertices were correctly updated
	 * 			false otherwise
	 */
	public boolean updateVertices( float[] pVertexX, float[] pVertexY )
	{
		mVertexX = pVertexX;
		mVertexY = pVertexY;
		assert( mVertexX.length == mVertexY.length );
		
		ArrayList<Vector2d> vertices = Triangulate.process(pVertexX, pVertexY);
		if( vertices == null )
		{
			Log.e("AndEngine", "Error: Polygon - Polygon can't be triangulated. Will not update vertices");
			return false;
		}
		return this.updateVertices(vertices);
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
