package org.andengine.extension.svg.util;

import org.andengine.extension.svg.adt.SVGPaint;
import org.andengine.extension.svg.adt.SVGProperties;
import org.andengine.extension.svg.util.constants.ISVGConstants;

import android.graphics.Canvas;
import android.graphics.RectF;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:57:25 - 25.05.2011
 */
public class SVGEllipseParser implements ISVGConstants {
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

	public static void parse(final SVGProperties pSVGProperties, final Canvas pCanvas, final SVGPaint pSVGPaint, final RectF pRect) {
		final Float centerX = pSVGProperties.getFloatAttribute(ATTRIBUTE_CENTER_X);
		final Float centerY = pSVGProperties.getFloatAttribute(ATTRIBUTE_CENTER_Y);
		final Float radiusX = pSVGProperties.getFloatAttribute(ATTRIBUTE_RADIUS_X);
		final Float radiusY = pSVGProperties.getFloatAttribute(ATTRIBUTE_RADIUS_Y);
		if (centerX != null && centerY != null && radiusX != null && radiusY != null) {
			pRect.set(centerX - radiusX, centerY - radiusY, centerX + radiusX, centerY + radiusY);

			final boolean fill = pSVGPaint.setFill(pSVGProperties);
			if (fill) {
				pCanvas.drawOval(pRect, pSVGPaint.getPaint());
			}

			final boolean stroke = pSVGPaint.setStroke(pSVGProperties);
			if (stroke) {
				pCanvas.drawOval(pRect, pSVGPaint.getPaint());
			}

			if(fill || stroke) {
				pSVGPaint.ensureComputedBoundsInclude(centerX - radiusX, centerY - radiusY);
				pSVGPaint.ensureComputedBoundsInclude(centerX + radiusX, centerY + radiusY);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
