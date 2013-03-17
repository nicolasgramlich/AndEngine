package org.andengine.util.adt.transformation;

import org.andengine.util.math.MathConstants;


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
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:47:18 - 23.12.2010
 */
public class Transformation {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private float a = 1.0f; /* x scale */
	private float b = 0.0f; /* y skew */
	private float c = 0.0f; /* x skew */
	private float d = 1.0f; /* y scale */
	private float tx = 0.0f; /* x translation */
	private float ty = 0.0f; /* y translation */

	// ===========================================================
	// Constructors
	// ===========================================================

	public Transformation() {

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

	public final void reset() {
		this.setToIdentity();
	}

	public final void setToIdentity() {
		this.a = 1.0f;
		this.d = 1.0f;

		this.b = 0.0f;
		this.c = 0.0f;
		this.tx = 0.0f;
		this.ty = 0.0f;
	}

	public final void setTo(final Transformation pTransformation) {
		this.a = pTransformation.a;
		this.d = pTransformation.d;

		this.b = pTransformation.b;
		this.c = pTransformation.c;
		this.tx = pTransformation.tx;
		this.ty = pTransformation.ty;
	}

	public final void preTranslate(final float pX, final float pY) {
		this.tx += pX * this.a + pY * this.c;
		this.ty += pX * this.b + pY * this.d;
	}

	public final void postTranslate(final float pX, final float pY) {
		this.tx += pX;
		this.ty += pY;
	}

	public final Transformation setToTranslate(final float pX, final float pY) {
		this.a = 1.0f;
		this.b = 0.0f;
		this.c = 0.0f;
		this.d = 1.0f;
		this.tx = pX;
		this.ty = pY;

		return this;
	}

	public final void preRotate(final float pAngle) {
		final float angleRad = MathConstants.DEG_TO_RAD * pAngle;

		final float sin = (float) Math.sin(angleRad);
		final float cos = (float) Math.cos(angleRad);

		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;

		this.a = cos * a + sin * c;
		this.b = cos * b + sin * d;
		this.c = cos * c - sin * a;
		this.d = cos * d - sin * b;
	}

	public final void postRotate(final float pAngle) {
		final float angleRad = MathConstants.DEG_TO_RAD * pAngle;

		final float sin = (float) Math.sin(angleRad);
		final float cos = (float) Math.cos(angleRad);

		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;
		final float tx = this.tx;
		final float ty = this.ty;

		this.a = a * cos - b * sin;
		this.b = a * sin + b * cos;
		this.c = c * cos - d * sin;
		this.d = c * sin + d * cos;
		this.tx = tx * cos - ty * sin;
		this.ty = tx * sin + ty * cos;
	}

	public final Transformation setToRotate(final float pAngle) {
		final float angleRad = MathConstants.DEG_TO_RAD * pAngle;

		final float sin = (float) Math.sin(angleRad);
		final float cos = (float) Math.cos(angleRad);

		this.a = cos;
		this.b = sin;
		this.c = -sin;
		this.d = cos;
		this.tx = 0.0f;
		this.ty = 0.0f;

		return this;
	}

	public final void preScale(final float pScaleX, final float pScaleY) {
		this.a *= pScaleX;
		this.b *= pScaleX;
		this.c *= pScaleY;
		this.d *= pScaleY;
	}

	public final void postScale(final float pScaleX, final float pScaleY) {
		this.a = this.a * pScaleX;
		this.b = this.b * pScaleY;
		this.c = this.c * pScaleX;
		this.d = this.d * pScaleY;
		this.tx = this.tx * pScaleX;
		this.ty = this.ty * pScaleY;
	}

	public final Transformation setToScale(final float pScaleX, final float pScaleY) {
		this.a = pScaleX;
		this.b = 0.0f;
		this.c = 0.0f;
		this.d = pScaleY;
		this.tx = 0.0f;
		this.ty = 0.0f;

		return this;
	}

	public final void preSkew(final float pSkewX, final float pSkewY) {
		final float tanX = (float) Math.tan(-MathConstants.DEG_TO_RAD * pSkewX);
		final float tanY = (float) Math.tan(-MathConstants.DEG_TO_RAD * pSkewY);

		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;
		final float tx = this.tx;
		final float ty = this.ty;

		this.a = a + tanY * c;
		this.b = b + tanY * d;
		this.c = tanX * a + c;
		this.d = tanX * b + d;
		this.tx = tx;
		this.ty = ty;
	}

	public final void postSkew(final float pSkewX, final float pSkewY) {
		final float tanX = (float) Math.tan(-MathConstants.DEG_TO_RAD * pSkewX);
		final float tanY = (float) Math.tan(-MathConstants.DEG_TO_RAD * pSkewY);

		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;
		final float tx = this.tx;
		final float ty = this.ty;

		this.a = a + b * tanX;
		this.b = a * tanY + b;
		this.c = c + d * tanX;
		this.d = c * tanY + d;
		this.tx = tx + ty * tanX;
		this.ty = tx * tanY + ty;
	}

	public final Transformation setToSkew(final float pSkewX, final float pSkewY) {
		this.a = 1.0f;
		this.b = (float) Math.tan(-MathConstants.DEG_TO_RAD * pSkewY);
		this.c = (float) Math.tan(-MathConstants.DEG_TO_RAD * pSkewX);
		this.d = 1.0f;
		this.tx = 0.0f;
		this.ty = 0.0f;

		return this;
	}

	public final void postConcat(final Transformation pTransformation) {
		this.postConcat(pTransformation.a, pTransformation.b, pTransformation.c, pTransformation.d, pTransformation.tx, pTransformation.ty);
	}

	private void postConcat(final float pA, final float pB, final float pC, final float pD, final float pTX, final float pTY) {
		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;
		final float tx = this.tx;
		final float ty = this.ty;

		this.a = a * pA + b * pC;
		this.b = a * pB + b * pD;
		this.c = c * pA + d * pC;
		this.d = c * pB + d * pD;
		this.tx = tx * pA + ty * pC + pTX;
		this.ty = tx * pB + ty * pD + pTY;
	}

	public final void preConcat(final Transformation pTransformation) {
		this.preConcat(pTransformation.a, pTransformation.b, pTransformation.c, pTransformation.d, pTransformation.tx, pTransformation.ty);
	}

	private void preConcat(final float pA, final float pB, final float pC, final float pD, final float pTX, final float pTY) {
		final float a = this.a;
		final float b = this.b;
		final float c = this.c;
		final float d = this.d;
		final float tx = this.tx;
		final float ty = this.ty;

		this.a = pA * a + pB * c;
		this.b = pA * b + pB * d;
		this.c = pC * a + pD * c;
		this.d = pC * b + pD * d;
		this.tx = pTX * a + pTY * c + tx;
		this.ty = pTX * b + pTY * d + ty;
	}

	public final void transform(final float[] pVertices) {
		int count = pVertices.length >> 1;
		int i = 0;
		int j = 0;
		while (--count >= 0) {
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