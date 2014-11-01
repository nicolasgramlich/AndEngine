package org.andengine.extension.svg.util;

import org.andengine.extension.svg.adt.SVGPaint;
import org.andengine.extension.svg.adt.SVGProperties;
import org.andengine.extension.svg.util.constants.ISVGConstants;

import android.graphics.Canvas;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:53:50 - 25.05.2011
 */
public class SVGLineParser implements ISVGConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static void parse(final SVGProperties pSVGProperties, final Canvas pCanvas, final SVGPaint pSVGPaint) {
		final float x1 = pSVGProperties.getFloatAttribute(ATTRIBUTE_X1, 0f);
		final float x2 = pSVGProperties.getFloatAttribute(ATTRIBUTE_X2, 0f);
		final float y1 = pSVGProperties.getFloatAttribute(ATTRIBUTE_Y1, 0f);
		final float y2 = pSVGProperties.getFloatAttribute(ATTRIBUTE_Y2, 0f);
		if (pSVGPaint.setStroke(pSVGProperties)) {
			pSVGPaint.ensureComputedBoundsInclude(x1, y1);
			pSVGPaint.ensureComputedBoundsInclude(x2, y2);
			pCanvas.drawLine(x1, y1, x2, y2, pSVGPaint.getPaint());
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
