package org.andengine.util.adt.transformation;

public class TransformationUtils {


	/**
	 * Invert the given transformation
	 * @param pTransformation The transformation to invert
	 * @param pInverse The trasformation in which the inverse is stored
	 * @return A reference to pInverse
	 */
	public static Transformation invert(final Transformation pTransformation, final Transformation pInverse) {
		
		final float det = pTransformation.a*pTransformation.d - pTransformation.b*pTransformation.c;
		
		pInverse.a = pTransformation.d / det;
		pInverse.b = -pTransformation.b / det;
		pInverse.c = -pTransformation.c / det;
		pInverse.d = pTransformation.a / det;
		pInverse.tx = (pTransformation.c*pTransformation.ty - pTransformation.d*pTransformation.tx) / det;
		pInverse.ty = (-pTransformation.a*pTransformation.ty + pTransformation.b*pTransformation.tx) / det;
		
		return pInverse;
	}
	
	/**
	 * Transform the given vertices without taking into account the translation.
	 * @param pTransformation The transformation to use
	 * @param pVertices The vertices to transform
	 */
	public static void transformNormal(final Transformation pTransformation, final float[] pVertices) {
		int count = pVertices.length >> 1;
		int i = 0;
		int j = 0;
		while(--count >= 0) {
			final float x = pVertices[i++];
			final float y = pVertices[i++];
			pVertices[j++] = x * pTransformation.a + y * pTransformation.c;
			pVertices[j++] = x * pTransformation.b + y * pTransformation.d;
		}
	}
	
	/**
	 *Apply the transformation to a given vertex
	 * @param pTransformation The transformation to use
	 * @param pX The X coordinate of the vertex
	 * @param pY The Y coordinate of the vertex
	 * @param pContainer The vector in which store the result
	 * @return A reference to pContainer
	 */
	public static float[] transform(final Transformation pTransformation, final float pX, final float pY, final float[] pContainer) {
		pContainer[0] = pX * pTransformation.a + pY * pTransformation.c + pTransformation.tx;
		pContainer[1] = pX * pTransformation.b + pY * pTransformation.d + pTransformation.ty;
		
		return pContainer;
	}
}
