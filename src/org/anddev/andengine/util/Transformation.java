package org.anddev.andengine.util;

import android.util.FloatMath;


/**
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

	public float a;
	public float b;
	public float c;
	public float d;
	public float tx;
	public float ty;

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
		return "Matrix{[" + a + ", " + c + ", " + tx + "][" + b + ", " + d + ", " + ty + "][0.0, 0.0, 1.0]}";
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void identity() {
		this.a = 1.0f;
		this.d = 1.0f;

		this.b = 0.0f;
		this.c = 0.0f;
		this.tx = 0.0f;
		this.ty = 0.0f;
	}

	public void preTranslate(final float pX, final float pY) {
		this.preConcat(new Transformation().setToTranslate(pX, pY));
	}

	public void postTranslate(final float pX, final float pY) {
		this.postConcat(new Transformation().setToTranslate(pX, pY));
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
		this.preConcat(new Transformation().setToScale(pScaleX, pScaleY));
	}

	public void postScale(final float pScaleX, final float pScaleY) {
		this.postConcat(new Transformation().setToScale(pScaleX, pScaleY));
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
		this.preConcat(new Transformation().setToRotate(pAngle));
	}
	
	public void postRotate(final float pAngle) {
		this.postConcat(new Transformation().setToRotate(pAngle));
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