package org.andengine.entity.primitive;

import java.util.ArrayList;
import java.util.List;

import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;

import android.renderscript.Mesh;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;

/**
 * 
 * @author Rodrigo Castro
 * @since 22:10:11 - 28.01.2012
 */
public class TexturedPolygon extends TexturedMesh {
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final float VERTEX_SIZE_DEFAULT_RATIO = 1.f;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected float[] mVertexX;
	protected float[] mVertexY;

	protected static EarClippingTriangulator mTriangulator = new EarClippingTriangulator();

	// ===========================================================
	// Constructors
	// ===========================================================
	
	/**
	 * Uses a default {@link HighPerformanceTexturedMeshVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TexturedPolygon(final float pX, final float pY, final float[] pVertexX, final float[] pVertexY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pVertexX, pVertexY, pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}
	
	/**
	 * Uses a default {@link HighPerformanceTexturedMeshVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TexturedPolygon(final float pX, final float pY, final float[] pVertexX, float[] pVertexY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, buildVertexList(mTriangulator.computeTriangles(buildListOfVector2(pVertexX, pVertexY))), VERTEX_SIZE_DEFAULT_RATIO, pTextureRegion, pVertexBufferObjectManager, pDrawType );
		
		assert( mVertexX.length == mVertexY.length );
		mVertexX = pVertexX;
		mVertexY = pVertexY;
	}


	/**
	 * Uses a default {@link HighPerformanceTexturedMeshVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Mesh#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public TexturedPolygon(final float pX, final float pY, final float[] pBufferData, final float sizeRatio, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(pX, pY, (int) ((pBufferData.length / VERTEX_SIZE)* sizeRatio), DrawMode.TRIANGLES, 
				pTextureRegion, new HighPerformanceTexturedMeshVertexBufferObject(pVertexBufferObjectManager, pBufferData, (int) ((pBufferData.length / VERTEX_SIZE )*sizeRatio), pDrawType, true, TexturedMesh.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
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
		
		List<Vector2> verticesVectors = mTriangulator.computeTriangles(buildListOfVector2(pVertexX, pVertexY));
		if( verticesVectors.size() == 0 )
		{
			Log.e("AndEngine", "Error: Polygon - Polygon can't be triangulated. Will not update vertices");
			return false;
		}
		
		updateVertices(verticesVectors, getBufferData());
		onUpdateVertices();
		
		return true;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	protected static List<Vector2> buildListOfVector2(float[] pX, float [] pY )
	{
		assert(pX.length == pY.length );
		ArrayList<Vector2> vectors = new ArrayList<Vector2>( pX.length );
		
		for( int i = 0; i < pX.length; i++ )
		{
			// TODO avoid using new
			Vector2 v = new Vector2( pX[i], pY[i]);
			vectors.add(v);
		}
		
		return vectors;
	}
	
	protected static float[] buildVertexList(List<Vector2> vertices )
	{
		
		float[] bufferData = new float[VERTEX_SIZE * vertices.size()];
		updateVertices( vertices, bufferData );
		return bufferData;
	}
	
	protected static void updateVertices(List<Vector2> vertices, float[] pBufferData) {
		int i = 0;
		for( Vector2 vertex : vertices )
		{
			pBufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_X] = vertex.x;
			pBufferData[(i * TexturedMesh.VERTEX_SIZE) + TexturedMesh.VERTEX_INDEX_Y] = vertex.y;
			i++;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
}
