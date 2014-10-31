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
 * @since 19:27:42 - 25.05.2011
 */
public class SVGRectParser implements ISVGConstants {
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
		final float x = pSVGProperties.getFloatAttribute(ATTRIBUTE_X, 0f);
		final float y = pSVGProperties.getFloatAttribute(ATTRIBUTE_Y, 0f);
		final float width = pSVGProperties.getFloatAttribute(ATTRIBUTE_WIDTH, 0f);
		final float height = pSVGProperties.getFloatAttribute(ATTRIBUTE_HEIGHT, 0f);

		pRect.set(x, y, x + width, y + height);

		final Float rX = pSVGProperties.getFloatAttribute(ATTRIBUTE_RADIUS_X);
		final Float rY = pSVGProperties.getFloatAttribute(ATTRIBUTE_RADIUS_Y);

		final boolean rXSpecified = rX != null && rX >= 0;
		final boolean rYSpecified = rY != null && rY >= 0;

		final boolean rounded = rXSpecified || rYSpecified;
		final float rx;
		final float ry;
		if(rXSpecified && rYSpecified) {
			rx = Math.min(rX, width * 0.5f);
			ry = Math.min(rY, height * 0.5f);
		} else if(rXSpecified) {
			ry = rx = Math.min(rX, width * 0.5f);
		} else if(rYSpecified) {
			rx = ry = Math.min(rY, height * 0.5f);
		} else {
			rx = 0;
			ry = 0;
		}

		final boolean fill = pSVGPaint.setFill(pSVGProperties);
		if (fill) {
			if(rounded) {
				pCanvas.drawRoundRect(pRect, rx, ry, pSVGPaint.getPaint());
			} else {
				pCanvas.drawRect(pRect, pSVGPaint.getPaint());
			}
		}

		final boolean stroke = pSVGPaint.setStroke(pSVGProperties);
		if (stroke) {
			if(rounded) {
				pCanvas.drawRoundRect(pRect, rx, ry, pSVGPaint.getPaint());
			} else {
				pCanvas.drawRect(pRect, pSVGPaint.getPaint());
			}
		}

		if(fill || stroke) {
			pSVGPaint.ensureComputedBoundsInclude(x, y, width, height);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
