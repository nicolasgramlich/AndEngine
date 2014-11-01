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
 * @since 19:55:18 - 25.05.2011
 */
public class SVGCircleParser implements ISVGConstants {
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
		final Float centerX = pSVGProperties.getFloatAttribute(ATTRIBUTE_CENTER_X);
		final Float centerY = pSVGProperties.getFloatAttribute(ATTRIBUTE_CENTER_Y);
		final Float radius = pSVGProperties.getFloatAttribute(ATTRIBUTE_RADIUS);
		if (centerX != null && centerY != null && radius != null) {
			final boolean fill = pSVGPaint.setFill(pSVGProperties);
			if (fill) {
				pCanvas.drawCircle(centerX, centerY, radius, pSVGPaint.getPaint());
			}

			final boolean stroke = pSVGPaint.setStroke(pSVGProperties);
			if (stroke) {
				pCanvas.drawCircle(centerX, centerY, radius, pSVGPaint.getPaint());
			}

			if(fill || stroke) {
				pSVGPaint.ensureComputedBoundsInclude(centerX - radius, centerY - radius);
				pSVGPaint.ensureComputedBoundsInclude(centerX + radius, centerY + radius);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
