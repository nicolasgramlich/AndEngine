package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.Gradient;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.color.ColorUtils;
import org.andengine.util.math.MathConstants;
import org.andengine.util.math.MathUtils;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:27:09 - 23.04.2012
 */
public class HighPerformanceGradientVertexBufferObject extends HighPerformanceVertexBufferObject implements IGradientVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceGradientVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdateColor(final Gradient pGradient) {
		final float[] bufferData = this.mBufferData;

		final float gradientVectorX = pGradient.getGradientVectorX();
		final float gradientVectorY = pGradient.getGradientVectorY();

		final float length = MathUtils.length(gradientVectorX, gradientVectorY);
		if (length == 0) {
			return;
		}

		float dX = gradientVectorX / length;
		float dY = gradientVectorY / length;

		final float c = MathConstants.SQRT_2;
		final float c2 = 2 * c;

		if (pGradient.isGradientFitToBounds()) {
			final float tmp = 1 / (Math.abs(dX) + Math.abs(dY));
			dX *= tmp * c;
			dY *= tmp * c;
		}

		final float fromRed = pGradient.getFromRed();
		final float fromGreen = pGradient.getFromGreen();
		final float fromBlue = pGradient.getFromBlue();
		final float fromAlpha = pGradient.getFromAlpha(); // TODO * 'this.mOverallOpactity'

		final float toRed = pGradient.getToRed();
		final float toGreen = pGradient.getToGreen();
		final float toBlue = pGradient.getToBlue();
		final float toAlpha = pGradient.getToAlpha(); // TODO * 'this.mOverallOpactity'

		/* Pre-fetch some calculations. */
		final float dRed = fromRed - toRed;
		final float dGreen = fromGreen - toGreen;
		final float dBlue = fromBlue - toBlue;
		final float dAlpha = fromAlpha - toAlpha;


		final float v0 = (c + dX + dY) / c2;
		bufferData[(0 * Gradient.VERTEX_SIZE) + Gradient.COLOR_INDEX] = ColorUtils.convertRGBAToABGRPackedFloat(toRed + (dRed * v0), toGreen + (dGreen * v0), toBlue + (dBlue * v0), toAlpha + (dAlpha * v0));

		final float v1 = (c + dX - dY) / c2;
		bufferData[(1 * Gradient.VERTEX_SIZE) + Gradient.COLOR_INDEX] = ColorUtils.convertRGBAToABGRPackedFloat(toRed + (dRed * v1), toGreen + (dGreen * v1), toBlue + (dBlue * v1), toAlpha + (dAlpha * v1));

		final float v2 = (c - dX + dY) / c2;
		bufferData[(2 * Gradient.VERTEX_SIZE) + Gradient.COLOR_INDEX] = ColorUtils.convertRGBAToABGRPackedFloat(toRed + (dRed * v2), toGreen + (dGreen * v2), toBlue + (dBlue * v2), toAlpha + (dAlpha * v2));

		final float v3 = (c - dX - dY) / c2;
		bufferData[(3 * Gradient.VERTEX_SIZE) + Gradient.COLOR_INDEX] = ColorUtils.convertRGBAToABGRPackedFloat(toRed + (dRed * v3), toGreen + (dGreen * v3), toBlue + (dBlue * v3), toAlpha + (dAlpha * v3));


		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateVertices(final Gradient pGradient) {
		final float[] bufferData = this.mBufferData;

		final float width = pGradient.getWidth(); // TODO Optimize with field access?
		final float height = pGradient.getHeight(); // TODO Optimize with field access?

		bufferData[(0 * Gradient.VERTEX_SIZE) + Gradient.VERTEX_INDEX_X] = 0;
		bufferData[(0 * Gradient.VERTEX_SIZE) + Gradient.VERTEX_INDEX_Y] = 0;

		bufferData[(1 * Gradient.VERTEX_SIZE) + Gradient.VERTEX_INDEX_X] = 0;
		bufferData[(1 * Gradient.VERTEX_SIZE) + Gradient.VERTEX_INDEX_Y] = height;

		bufferData[(2 * Gradient.VERTEX_SIZE) + Gradient.VERTEX_INDEX_X] = width;
		bufferData[(2 * Gradient.VERTEX_SIZE) + Gradient.VERTEX_INDEX_Y] = 0;

		bufferData[(3 * Gradient.VERTEX_SIZE) + Gradient.VERTEX_INDEX_X] = width;
		bufferData[(3 * Gradient.VERTEX_SIZE) + Gradient.VERTEX_INDEX_Y] = height;

		this.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}