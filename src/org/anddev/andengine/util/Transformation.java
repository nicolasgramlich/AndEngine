package org.anddev.andengine.util;

import android.util.FloatMath;


/**
 * <p>This class is basically a java-space replacement for the native {@link android.graphics.Matrix} class.</p>
 * 
 * <p>Math taken from <a href="http://www.senocular.com/flash/tutorials/transformmatrix/">senocular.com</a>.</p>
 * 
 * This class represents an affine transformation with the following matrix:
 * <pre> [ a , b , 0 ]
 * [ c , d , 0 ]
 * [ tx, ty, 1 ]</pre>
 * where:
 * <ul>
 *  <li><b>a</b> is the <b>x scale</b></li>
 *  <li><b>b</b> is the <b>y skew</b></li>
 *  <li><b>c</b> is the <b>x skew</b></li>
 *  <li><b>d</b> is the <b>y scale</b></li>
 *  <li><b>tx</b> is the <b>x translation</b></li>
 *  <li><b>ty</b> is the <b>y translation</b></li>
 * </ul>
 *
 * <p>TODO Think if that caching of Transformation through the TransformationPool really needs to be thread-safe or if one simple reused static Transform object is enough.</p>
 * 
 * @author Nicolas Gramlich
 * @since 15:47:18 - 23.12.2010
 */
public class Transformation  {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float a; /* x scale */
	private float b; /* y skew */
	private float c; /* x skew */
	private float d; /* y scale */
	private float tx; /* x translation */
	private float ty; /* y translation */

	// ===========================================================
	// Constructors
	// ===========================================================

	public Transformation() {
		this.a = 1.0f;
		this.d = 1.0f;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		return "Transformation{[" + this.a + ", " + this.c + ", " + this.tx + "][" + this.b + ", " + this.d + ", " + this.ty + "][0.0, 0.0, 1.0]}";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset() {
		this.setToIdentity();
	}

	public void setToIdentity() {
		this.a = 1.0f;
		this.d = 1.0f;

		this.b = 0.0f;
		this.c = 0.0f;
		this.tx = 0.0f;
		this.ty = 0.0f;
	}

	public void preTranslate(final float pX, final float pY) {
		final Transformation transformation = TransformationPool.obtain();
		this.preConcat(transformation.setToTranslate(pX, pY));
		TransformationPool.recycle(transformation);
	}

	public void postTranslate(final float pX, final float pY) {
		final Transformation transformation = TransformationPool.obtain();
		this.postConcat(transformation.setToTranslate(pX, pY));
		TransformationPool.recycle(transformation);
	}

	public Transformation setToTranslate(final float pX, final float pY) {
		this.a = 1;
		this.b = 0;
		this.c = 0;
		this.d = 1;
		this.tx = pX;
		this.ty = pY;

		return this;
	}

	public void preScale(final float pScaleX, final float pScaleY) {
		final Transformation transformation = TransformationPool.obtain();
		this.preConcat(transformation.setToScale(pScaleX, pScaleY));
		TransformationPool.recycle(transformation);
	}

	public void postScale(final float pScaleX, final float pScaleY) {
		final Transformation transformation = TransformationPool.obtain();
		this.postConcat(transformation.setToScale(pScaleX, pScaleY));
		TransformationPool.recycle(transformation);
	}

	public Transformation setToScale(final float pScaleX, final float pScaleY) {
		this.a = pScaleX;
		this.b = 0;
		this.c = 0;
		this.d = pScaleY;
		this.tx = 0;
		this.ty = 0;

		return this;
	}

	public void preRotate(final float pAngle) {
		final Transformation transformation = TransformationPool.obtain();
		this.preConcat(transformation.setToRotate(pAngle));
		TransformationPool.recycle(transformation);
	}

	public void postRotate(final float pAngle) {
		final Transformation transformation = TransformationPool.obtain();
		this.postConcat(transformation.setToRotate(pAngle));
		TransformationPool.recycle(transformation);
	}

	public Transformation setToRotate(final float pAngle) {
		final float angleRad = MathUtils.degToRad(pAngle);

		final float sin = FloatMath.sin(angleRad);
		final float cos = FloatMath.cos(angleRad);

		this.a = cos;
		this.b = sin;
		this.c = -sin;
		this.d = cos;
		this.tx = 0;
		this.ty = 0;

		return this;
	}

	public void postConcat(final Transformation pTransformation) {
		final float a1 = this.a;
		final float a2 = pTransformation.a;

		final float b1 = this.b;
		final float b2 = pTransformation.b;

		final float c1 = this.c;
		final float c2 = pTransformation.c;

		final float d1 = this.d;
		final float d2 = pTransformation.d;

		final float tx1 = this.tx;
		final float tx2 = pTransformation.tx;

		final float ty1 = this.ty;
		final float ty2 = pTransformation.ty;

		this.a = a1 * a2 + b1 * c2;
		this.b = a1 * b2 + b1 * d2;
		this.c = c1 * a2 + d1 * c2;
		this.d = c1 * b2 + d1 * d2;
		this.tx = tx1 * a2 + ty1 * c2 + tx2;
		this.ty = tx1 * b2 + ty1 * d2 + ty2;
	}

	public void preConcat(final Transformation pTransformation) {
		final float a1 = pTransformation.a;
		final float a2 = this.a;

		final float b1 = pTransformation.b;
		final float b2 = this.b;

		final float c1 = pTransformation.c;
		final float c2 = this.c;

		final float d1 = pTransformation.d;
		final float d2 = this.d;

		final float tx1 = pTransformation.tx;
		final float tx2 = this.tx;

		final float ty1 = pTransformation.ty;
		final float ty2 = this.ty;

		this.a = a1 * a2 + b1 * c2;
		this.b = a1 * b2 + b1 * d2;
		this.c = c1 * a2 + d1 * c2;
		this.d = c1 * b2 + d1 * d2;
		this.tx = tx1 * a2 + ty1 * c2 + tx2;
		this.ty = tx1 * b2 + ty1 * d2 + ty2;
	}

	public void transform(final float[] pVertices) {
		int count = pVertices.length / 2;
		int i = 0;
		int j = 0;
		while(--count >= 0) {
			final float x = pVertices[i++];
			final float y = pVertices[i++];
			pVertices[j++] = x * this.a + y * this.c + this.tx;
			pVertices[j++] = x * this.b + y * this.d + this.ty;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}