package org.andengine.extension.svg.util;

import org.andengine.extension.svg.adt.SVGPaint;
import org.andengine.extension.svg.adt.SVGProperties;
import org.andengine.extension.svg.util.SVGNumberParser.SVGNumberParserFloatResult;
import org.andengine.extension.svg.util.constants.ISVGConstants;

import android.graphics.Canvas;
import android.graphics.Path;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:47:02 - 25.05.2011
 */
public class SVGPolygonParser implements ISVGConstants {
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
		final SVGNumberParserFloatResult svgNumberParserFloatResult = SVGNumberParser.parseFloats(pSVGProperties.getStringAttribute(ATTRIBUTE_POINTS));
		if (svgNumberParserFloatResult != null) {
			final float[] points = svgNumberParserFloatResult.getNumbers();
			if (points.length >= 2) {
				final Path path = SVGPolylineParser.parse(points);
				path.close();

				final boolean fill = pSVGPaint.setFill(pSVGProperties);
				if (fill) {
					pCanvas.drawPath(path, pSVGPaint.getPaint());
				}

				final boolean stroke = pSVGPaint.setStroke(pSVGProperties);
				if (stroke) {
					pCanvas.drawPath(path, pSVGPaint.getPaint());
				}

				if(fill || stroke) {
					pSVGPaint.ensureComputedBoundsInclude(path);
				}
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
