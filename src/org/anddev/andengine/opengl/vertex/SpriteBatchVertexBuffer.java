package org.anddev.andengine.opengl.vertex;

import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.util.Transformation;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:53:48 - 14.06.2011
 */
public class SpriteBatchVertexBuffer extends VertexBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTICES_PER_RECTANGLE = 6;

	private static final float[] VERTICES_TMP = new float[8];

	private static final Transformation TRANSFORATION_TMP = new Transformation();

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mIndex;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SpriteBatchVertexBuffer(final int pCapacity, final int pDrawType, final boolean pManaged) {
		super(pCapacity * 2 * VERTICES_PER_RECTANGLE, pDrawType, pManaged);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getIndex() {
		return this.mIndex;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void setIndex(final int pIndex) {
		this.mIndex = pIndex;
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pRotation around the center (pWidth * 0.5f, pHeight * 0.5f)
	 */
	public void add(final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation) {
		final float widthHalf = pWidth * 0.5f;
		final float heightHalf = pHeight * 0.5f;

		TRANSFORATION_TMP.setToIdentity();
		
		TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
		TRANSFORATION_TMP.postRotate(pRotation);
		TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
		TRANSFORATION_TMP.postTranslate(pX, pY);
		
		this.add(pWidth, pHeight, TRANSFORATION_TMP);
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pRotation around the center (pWidth * 0.5f, pHeight * 0.5f)
	 * @param pScaleX around the center (pWidth * 0.5f, pHeight * 0.5f)
	 * @param pScaleY around the center (pWidth * 0.5f, pHeight * 0.5f)
	 */
	public void add(final float pX, final float pY, final float pWidth, final float pHeight, final float pScaleX, final float pScaleY) {
		final float widthHalf = pWidth * 0.5f;
		final float heightHalf = pHeight * 0.5f;
		
		TRANSFORATION_TMP.setToIdentity();
		
		TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
		TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
		TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
		TRANSFORATION_TMP.postTranslate(pX, pY);
		
		this.add(pWidth, pHeight, TRANSFORATION_TMP);
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pRotation around the center (pWidth * 0.5f, pHeight * 0.5f)
	 * @param pScaleX around the center (pWidth * 0.5f, pHeight * 0.5f)
	 * @param pScaleY around the center (pWidth * 0.5f, pHeight * 0.5f)
	 */
	public void add(final float pX, final float pY, final float pWidth, final float pHeight, final float pRotation, final float pScaleX, final float pScaleY) {
		final float widthHalf = pWidth * 0.5f;
		final float heightHalf = pHeight * 0.5f;

		TRANSFORATION_TMP.setToIdentity();
		
		TRANSFORATION_TMP.postTranslate(-widthHalf, -heightHalf);
		TRANSFORATION_TMP.postScale(pScaleX, pScaleY);
		TRANSFORATION_TMP.postRotate(pRotation);
		TRANSFORATION_TMP.postTranslate(widthHalf, heightHalf);
		TRANSFORATION_TMP.postTranslate(pX, pY);
		
		this.add(pWidth, pHeight, TRANSFORATION_TMP);
	}

	/**
	 * 
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pTransformation
	 */
	public void add(final float pWidth, final float pHeight, final Transformation pTransformation) {
		VERTICES_TMP[0] = 0;
		VERTICES_TMP[1] = 0;

		VERTICES_TMP[2] = 0;
		VERTICES_TMP[3] = pHeight;

		VERTICES_TMP[4] = pWidth;
		VERTICES_TMP[5] = 0;

		VERTICES_TMP[6] = pWidth;
		VERTICES_TMP[7] = pHeight;

		pTransformation.transform(VERTICES_TMP);

		this.addInner(VERTICES_TMP[0], VERTICES_TMP[1], VERTICES_TMP[2], VERTICES_TMP[3],  VERTICES_TMP[4], VERTICES_TMP[5], VERTICES_TMP[6], VERTICES_TMP[7]);
	}

	public void add(final float pX, final float pY, final float pWidth, final float pHeight) {
		this.addInner(pX, pY, pX + pWidth, pY + pHeight);
	}

	/**
	 * 1-+
	 * |X|
	 * +-2
	 */
	public void addInner(final float pX1, final float pY1, final float pX2, final float pY2) {
		final int x1 = Float.floatToRawIntBits(pX1);
		final int y1 = Float.floatToRawIntBits(pY1);
		final int x2 = Float.floatToRawIntBits(pX2);
		final int y2 = Float.floatToRawIntBits(pY2);

		final int[] bufferData = this.mBufferData;
		int index = this.mIndex;
		bufferData[index++] = x1;
		bufferData[index++] = y1;

		bufferData[index++] = x1;
		bufferData[index++] = y2;

		bufferData[index++] = x2;
		bufferData[index++] = y1;

		bufferData[index++] = x2;
		bufferData[index++] = y1;

		bufferData[index++] = x1;
		bufferData[index++] = y2;

		bufferData[index++] = x2;
		bufferData[index++] = y2;
		this.mIndex = index;
	}

	/**
	 * 1-3
	 * |X|
	 * 2-4
	 */
	public void addInner(final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		final int x1 = Float.floatToRawIntBits(pX1);
		final int y1 = Float.floatToRawIntBits(pY1);
		final int x2 = Float.floatToRawIntBits(pX2);
		final int y2 = Float.floatToRawIntBits(pY2);
		final int x3 = Float.floatToRawIntBits(pX3);
		final int y3 = Float.floatToRawIntBits(pY3);
		final int x4 = Float.floatToRawIntBits(pX4);
		final int y4 = Float.floatToRawIntBits(pY4);

		final int[] bufferData = this.mBufferData;
		int index = this.mIndex;
		bufferData[index++] = x1;
		bufferData[index++] = y1;

		bufferData[index++] = x2;
		bufferData[index++] = y2;

		bufferData[index++] = x3;
		bufferData[index++] = y3;

		bufferData[index++] = x3;
		bufferData[index++] = y3;

		bufferData[index++] = x2;
		bufferData[index++] = y2;

		bufferData[index++] = x4;
		bufferData[index++] = y4;
		this.mIndex = index;
	}

	public void submit() {
		final FastFloatBuffer buffer = this.mFloatBuffer;
		buffer.position(0);
		buffer.put(this.mBufferData);
		buffer.position(0);

		super.setHardwareBufferNeedsUpdate();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
