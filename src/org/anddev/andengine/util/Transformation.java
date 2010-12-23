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

//	public void translate(final float pX, final float pY) {
//		this.tx += pX;
//		this.ty += pY;
//	}
	
	public void setToTranslate(final float pX, final float pY) {
		this.a = 1;
		this.b = 0;
		this.c = 0;
		this.d = 1;
		this.tx = pX;
		this.ty = pY;
	}

//	public void scale(final float pScaleX, final float pScaleY) {
//		this.a = pScaleX;
//		this.d = pScaleY;
//		this.tx = 0;
//		this.ty = 0;
//	}
	
	public void setToScale(final float pScaleX, final float pScaleY) {
		this.a = pScaleX;
		this.b = 0;
		this.c = 0;
		this.d = pScaleY;
		this.tx = 0;
		this.ty = 0;
	}

//	public void rotate(final float pAngle) {
//		final float angleRad = MathUtils.degToRad(pAngle);
//		
//		final float sin = FloatMath.sin(angleRad);
//		final float cos = FloatMath.cos(angleRad);
//		
//		final float a = this.a;
//		final float b = this.b;
//		final float c = this.c;
//		final float d = this.d;
//		final float tx = this.tx;
//		final float ty = this.ty;
//		
//		this.a = a * cos - b * sin;
//		this.b = a * sin + b * cos;
//		this.c = c * cos - d * sin;
//		this.d = c * sin + d * cos;
//		this.tx = tx * cos - ty * sin;
//		this.ty = tx * sin + ty * cos;
//	}
	
	public void setToRotate(final float pAngle) {
		final float angleRad = MathUtils.degToRad(pAngle);
		
		final float sin = FloatMath.sin(angleRad);
		final float cos = FloatMath.cos(angleRad);
		
		this.a = cos;
		this.b = sin;
		this.c = -sin;
		this.d = cos;
		this.tx = 0;
		this.ty = 0;
	}
	
	public void concat(final Transformation pTransformation) {
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
		this.b = c1 * a2 + d1 * c2;
		this.c = a1 * b2 * b1 * d2;
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