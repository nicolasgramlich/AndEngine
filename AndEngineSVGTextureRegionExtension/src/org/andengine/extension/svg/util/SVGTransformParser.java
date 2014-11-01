package org.andengine.extension.svg.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andengine.extension.svg.exception.SVGParseException;
import org.andengine.extension.svg.util.SVGNumberParser.SVGNumberParserFloatResult;
import org.andengine.extension.svg.util.constants.ISVGConstants;

import android.graphics.Matrix;



/**
 * @author Larva Labs, LLC
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:56:54 - 21.05.2011
 */
public class SVGTransformParser implements ISVGConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final Pattern MULTITRANSFORM_PATTERN = Pattern.compile("(\\w+\\([\\d\\s\\-eE,]*\\))");

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

	public static Matrix parseTransform(final String pString) {
		if(pString == null) {
			return null;
		}

		/* If ')' is contained only once, we have a simple/single transform.
		 * Otherwise, we have to split multi-transforms like this:
		 * "translate(-10,-20) scale(2) rotate(45) translate(5,10)". */
		final boolean singleTransform = pString.indexOf(')') == pString.lastIndexOf(')');
		if(singleTransform) {
			return SVGTransformParser.parseSingleTransform(pString);
		} else {
			return SVGTransformParser.parseMultiTransform(pString);
		}
	}

	private static Matrix parseMultiTransform(final String pString) {
		final Matcher matcher = MULTITRANSFORM_PATTERN.matcher(pString);

		final Matrix matrix = new Matrix();
		while(matcher.find()) {
			matrix.preConcat(SVGTransformParser.parseSingleTransform(matcher.group(1)));
		}
		return matrix;
	}

	private static Matrix parseSingleTransform(final String pString) {
		try {
			if (pString.startsWith(ATTRIBUTE_TRANSFORM_VALUE_MATRIX)) {
				return SVGTransformParser.parseTransformMatrix(pString);
			} else if (pString.startsWith(ATTRIBUTE_TRANSFORM_VALUE_TRANSLATE)) {
				return SVGTransformParser.parseTransformTranslate(pString);
			} else if (pString.startsWith(ATTRIBUTE_TRANSFORM_VALUE_SCALE)) {
				return SVGTransformParser.parseTransformScale(pString);
			} else if (pString.startsWith(ATTRIBUTE_TRANSFORM_VALUE_SKEW_X)) {
				return SVGTransformParser.parseTransformSkewX(pString);
			} else if (pString.startsWith(ATTRIBUTE_TRANSFORM_VALUE_SKEW_Y)) {
				return SVGTransformParser.parseTransformSkewY(pString);
			} else if (pString.startsWith(ATTRIBUTE_TRANSFORM_VALUE_ROTATE)) {
				return SVGTransformParser.parseTransformRotate(pString);
			} else {
				throw new SVGParseException("Unexpected transform type: '" + pString + "'.");
			}
		} catch (final SVGParseException e) {
			throw new SVGParseException("Could not parse transform: '" + pString + "'.", e);
		}
	}

	public static Matrix parseTransformRotate(final String pString) {
		final SVGNumberParserFloatResult svgNumberParserFloatResult = SVGNumberParser.parseFloats(pString.substring(ATTRIBUTE_TRANSFORM_VALUE_ROTATE.length() + 1, pString.indexOf(')')));
		SVGTransformParser.assertNumberParserResultNumberCountMinimum(svgNumberParserFloatResult, 1);

		final float angle = svgNumberParserFloatResult.getNumber(0);
		float cx = 0;
		float cy = 0;
		if (svgNumberParserFloatResult.getNumberCount() > 2) {
			cx = svgNumberParserFloatResult.getNumber(1);
			cy = svgNumberParserFloatResult.getNumber(2);
		}
		final Matrix matrix = new Matrix();
		matrix.postTranslate(cx, cy);
		matrix.postRotate(angle);
		matrix.postTranslate(-cx, -cy);
		return matrix;
	}

	private static Matrix parseTransformSkewY(final String pString) {
		final SVGNumberParserFloatResult svgNumberParserFloatResult = SVGNumberParser.parseFloats(pString.substring(ATTRIBUTE_TRANSFORM_VALUE_SKEW_Y.length() + 1, pString.indexOf(')')));
		SVGTransformParser.assertNumberParserResultNumberCountMinimum(svgNumberParserFloatResult, 1);

		final float angle = svgNumberParserFloatResult.getNumber(0);
		final Matrix matrix = new Matrix();
		matrix.postSkew(0, (float) Math.tan(angle));
		return matrix;
	}

	private static Matrix parseTransformSkewX(final String pString) {
		final SVGNumberParserFloatResult svgNumberParserFloatResult = SVGNumberParser.parseFloats(pString.substring(ATTRIBUTE_TRANSFORM_VALUE_SKEW_X.length() + 1, pString.indexOf(')')));
		SVGTransformParser.assertNumberParserResultNumberCountMinimum(svgNumberParserFloatResult, 1);

		final float angle = svgNumberParserFloatResult.getNumber(0);
		final Matrix matrix = new Matrix();
		matrix.postSkew((float) Math.tan(angle), 0);
		return matrix;
	}

	private static Matrix parseTransformScale(final String pString) {
		final SVGNumberParserFloatResult svgNumberParserFloatResult = SVGNumberParser.parseFloats(pString.substring(ATTRIBUTE_TRANSFORM_VALUE_SCALE.length() + 1, pString.indexOf(')')));
		SVGTransformParser.assertNumberParserResultNumberCountMinimum(svgNumberParserFloatResult, 1);
		final float sx = svgNumberParserFloatResult.getNumber(0);
		float sy = 0;
		if (svgNumberParserFloatResult.getNumberCount() > 1) {
			sy = svgNumberParserFloatResult.getNumber(1);
		}
		final Matrix matrix = new Matrix();
		matrix.postScale(sx, sy);
		return matrix;
	}

	private static Matrix parseTransformTranslate(final String pString) {
		final SVGNumberParserFloatResult svgNumberParserFloatResult = SVGNumberParser.parseFloats(pString.substring(ATTRIBUTE_TRANSFORM_VALUE_TRANSLATE.length() + 1, pString.indexOf(')')));
		SVGTransformParser.assertNumberParserResultNumberCountMinimum(svgNumberParserFloatResult, 1);
		final float tx = svgNumberParserFloatResult.getNumber(0);
		float ty = 0;
		if (svgNumberParserFloatResult.getNumberCount() > 1) {
			ty = svgNumberParserFloatResult.getNumber(1);
		}
		final Matrix matrix = new Matrix();
		matrix.postTranslate(tx, ty);
		return matrix;
	}

	private static Matrix parseTransformMatrix(final String pString) {
		final SVGNumberParserFloatResult svgNumberParserFloatResult = SVGNumberParser.parseFloats(pString.substring(ATTRIBUTE_TRANSFORM_VALUE_MATRIX.length() + 1, pString.indexOf(')')));
		SVGTransformParser.assertNumberParserResultNumberCount(svgNumberParserFloatResult, 6);
		final Matrix matrix = new Matrix();
		matrix.setValues(new float[]{
				// Row 1
				svgNumberParserFloatResult.getNumber(0),
				svgNumberParserFloatResult.getNumber(2),
				svgNumberParserFloatResult.getNumber(4),
				// Row 2
				svgNumberParserFloatResult.getNumber(1),
				svgNumberParserFloatResult.getNumber(3),
				svgNumberParserFloatResult.getNumber(5),
				// Row 3
				0,
				0,
				1,
		});
		return matrix;
	}

	private static void assertNumberParserResultNumberCountMinimum(final SVGNumberParserFloatResult pSVGNumberParserFloatResult, final int pNumberParserResultNumberCountMinimum) {
		final int svgNumberParserFloatResultNumberCount = pSVGNumberParserFloatResult.getNumberCount();
		if(svgNumberParserFloatResultNumberCount < pNumberParserResultNumberCountMinimum) {
			throw new SVGParseException("Not enough data. Minimum Expected: '" + pNumberParserResultNumberCountMinimum + "'. Actual: '" + svgNumberParserFloatResultNumberCount + "'.");
		}
	}

	private static void assertNumberParserResultNumberCount(final SVGNumberParserFloatResult pSVGNumberParserFloatResult, final int pNumberParserResultNumberCount) {
		final int svgNumberParserFloatResultNumberCount = pSVGNumberParserFloatResult.getNumberCount();
		if(svgNumberParserFloatResultNumberCount != pNumberParserResultNumberCount) {
			throw new SVGParseException("Unexpected number count. Expected: '" + pNumberParserResultNumberCount + "'. Actual: '" + svgNumberParserFloatResultNumberCount + "'.");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
