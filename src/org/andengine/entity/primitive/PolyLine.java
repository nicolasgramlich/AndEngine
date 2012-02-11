package org.andengine.entity.primitive;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle.HighPerformanceRectangleVertexBufferObject;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;

/**
 * 
 * @author Rodrigo Castro
 * @since 22:54:17 - 30.01.2012
 */
public class PolyLine extends Mesh {
	// ===========================================================
	// Constants
	// ===========================================================
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private float mLineWidth;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link PolygoonBase#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public PolyLine(final float pX, final float pY, final float[] pVertexX, final float[] pVertexY, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pVertexX, pVertexY, Line.LINE_WIDTH_DEFAULT, pVertexBufferObjectManager);
	}
	
	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link PolygoonBase#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public PolyLine(final float pX, final float pY, final float[] pVertexX, final float[] pVertexY, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pVertexX, pVertexY, pLineWidth, pVertexBufferObjectManager, DrawMode.LINE_LOOP);
	}
	
	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link PolygoonBase#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public PolyLine(final float pX, final float pY, final float[] pVertexX, final float[] pVertexY, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager, DrawMode pDrawMode) {
		this(pX, pY, pVertexX, pVertexY, pLineWidth, pVertexBufferObjectManager, pDrawMode, DrawType.STATIC);
	}


	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Rectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public PolyLine(final float pX, final float pY, final float[] pVertexX, final float[] pVertexY, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager, DrawMode pDrawMode, final DrawType pDrawType) {
		this(pX, pY, buildVertexList(pVertexX, pVertexY), pLineWidth, pVertexBufferObjectManager, pDrawMode, pDrawType);
	}
	
	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Rectangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public PolyLine(final float pX, final float pY, final float[] pBufferData, final float pLineWidth, final VertexBufferObjectManager pVertexBufferObjectManager, DrawMode pDrawMode, final DrawType pDrawType) {
		super(pX, pY, pBufferData, pBufferData.length / VERTEX_SIZE, pDrawMode, pVertexBufferObjectManager, pDrawType);
		
		mLineWidth = pLineWidth;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public float getLineWidth()
	{
		return mLineWidth;
	}
	
	public void setLineWidth( float pLineWidth )
	{
		mLineWidth = pLineWidth;
	}
	
	/**
	 * 
	 * @param pVertexX
	 * @param pVertexY
	 * @return 	true if vertices were correctly updated
	 * 			false otherwise
	 */
	/*public boolean updateVertices( float[] pVertexX, float[] pVertexY )
	{
		assert( pVertexX.length == pVertexY.length );

		return this.updateVertices(buildVertexList(pVertexX, pVertexY));
	}*/

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		pGLState.lineWidth(this.mLineWidth);

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
