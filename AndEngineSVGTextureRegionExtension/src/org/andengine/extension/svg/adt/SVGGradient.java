package org.andengine.extension.svg.adt;

import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.extension.svg.exception.SVGParseException;
import org.andengine.extension.svg.util.SVGParserUtils;
import org.andengine.extension.svg.util.SVGTransformParser;
import org.andengine.extension.svg.util.constants.ISVGConstants;
import org.xml.sax.Attributes;

import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

/**
 * @author Larva Labs, LLC
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:50:09 - 21.05.2011
 */
public class SVGGradient implements ISVGConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final String mID;
	private final String mHref;
	private SVGGradient mParent;

	private Shader mShader;

	private final SVGAttributes mSVGAttributes;
	private final boolean mLinear;
	private Matrix mMatrix;

	private ArrayList<SVGGradientStop> mSVGGradientStops;
	private float[] mSVGGradientStopsPositions;
	private int[] mSVGGradientStopsColors;
	private boolean mSVGGradientStopsBuilt;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGGradient(final String pID, final boolean pLinear, final Attributes pAttributes) {
		this.mID = pID;
		this.mHref = SVGParserUtils.parseHref(pAttributes);
		this.mLinear = pLinear;
		this.mSVGAttributes = new SVGAttributes(pAttributes, true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean hasHref() {
		return this.mHref != null;
	}

	public String getHref() {
		return this.mHref;
	}

	public String getID() {
		return this.mID;
	}

	public boolean hasHrefResolved() {
		return this.mHref == null || this.mParent != null;
	}

	public Shader getShader() {
		return this.mShader;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public Shader createShader() {
		if(this.mShader != null) {
			return this.mShader;
		}

		if(!this.mSVGGradientStopsBuilt) {
			this.buildSVGGradientStopsArrays();
		}

		final TileMode tileMode = this.getTileMode();
		if(this.mLinear) {
			final float x1 =this.mSVGAttributes.getFloatAttribute(ATTRIBUTE_X1, true, 0f);
			final float x2 = this.mSVGAttributes.getFloatAttribute(ATTRIBUTE_X2, true, 0f);
			final float y1 = this.mSVGAttributes.getFloatAttribute(ATTRIBUTE_Y1, true, 0f);
			final float y2 = this.mSVGAttributes.getFloatAttribute(ATTRIBUTE_Y2, true, 0f);

			this.mShader = new LinearGradient(x1, y1, x2, y2, this.mSVGGradientStopsColors, this.mSVGGradientStopsPositions, tileMode);
		} else {
			final float centerX = this.mSVGAttributes.getFloatAttribute(ATTRIBUTE_CENTER_X, true, 0f);
			final float centerY = this.mSVGAttributes.getFloatAttribute(ATTRIBUTE_CENTER_Y, true, 0f);
			final float radius = this.mSVGAttributes.getFloatAttribute(ATTRIBUTE_RADIUS, true, 0f);

			this.mShader = new RadialGradient(centerX, centerY, radius, this.mSVGGradientStopsColors, this.mSVGGradientStopsPositions, tileMode);
		}
		this.mMatrix = this.getTransform();
		if (this.mMatrix != null) {
			this.mShader.setLocalMatrix(this.mMatrix);
		}

		return this.mShader;
	}

	private TileMode getTileMode() {
		final String spreadMethod = this.mSVGAttributes.getStringAttribute(ATTRIBUTE_SPREADMETHOD, true);
		if(spreadMethod == null || ATTRIBUTE_SPREADMETHOD_VALUE_PAD.equals(spreadMethod)) {
			return TileMode.CLAMP;
		} else if(ATTRIBUTE_SPREADMETHOD_VALUE_REFLECT.equals(spreadMethod)) {
			return TileMode.MIRROR;
		} else if(ATTRIBUTE_SPREADMETHOD_VALUE_REPEAT.equals(spreadMethod)) {
			return TileMode.REPEAT;
		} else {
			throw new SVGParseException("Unexpected spreadmethod: '" + spreadMethod + "'.");
		}
	}

	private Matrix getTransform() {
		if(this.mMatrix != null) {
			return this.mMatrix;
		} else {
			final String transfromString = this.mSVGAttributes.getStringAttribute(ATTRIBUTE_GRADIENT_TRANSFORM, false);
			if(transfromString != null) {
				this.mMatrix = SVGTransformParser.parseTransform(transfromString);
				return this.mMatrix;
			} else {
				if(this.mParent != null) {
					return this.mParent.getTransform();
				} else {
					return null;
				}
			}
		}
	}

	public void ensureHrefResolved(final HashMap<String, SVGGradient> pSVGGradientMap) {
		if(!this.hasHrefResolved()) {
			this.resolveHref(pSVGGradientMap);
		}
	}

	private void resolveHref(final HashMap<String, SVGGradient> pSVGGradientMap) {
		final SVGGradient parent = pSVGGradientMap.get(this.mHref);
		if(parent == null) {
			throw new SVGParseException("Could not resolve href: '" + this.mHref + "' of SVGGradient: '" + this.mID + "'.");
		} else {
			parent.ensureHrefResolved(pSVGGradientMap);
			this.mParent = parent;
			this.mSVGAttributes.setParentSVGAttributes(this.mParent.mSVGAttributes);
			if(this.mSVGGradientStops == null) {
				this.mSVGGradientStops = this.mParent.mSVGGradientStops;
				this.mSVGGradientStopsColors = this.mParent.mSVGGradientStopsColors;
				this.mSVGGradientStopsPositions = this.mParent.mSVGGradientStopsPositions;
			}
		}
	}

	private void buildSVGGradientStopsArrays() {
		this.mSVGGradientStopsBuilt = true;
		final ArrayList<SVGGradientStop> svgGradientStops = this.mSVGGradientStops;

		final int svgGradientStopCount = svgGradientStops.size();
		this.mSVGGradientStopsColors = new int[svgGradientStopCount];
		this.mSVGGradientStopsPositions = new float[svgGradientStopCount];

		for (int i = 0; i < svgGradientStopCount; i++) {
			final SVGGradientStop svgGradientStop = svgGradientStops.get(i);
			this.mSVGGradientStopsColors[i] = svgGradientStop.mColor;
			this.mSVGGradientStopsPositions[i] = svgGradientStop.mOffset;
		}
	}

	public void addSVGGradientStop(final SVGGradientStop pSVGGradientStop) {
		if(this.mSVGGradientStops == null) {
			this.mSVGGradientStops = new ArrayList<SVGGradient.SVGGradientStop>();
		}
		this.mSVGGradientStops.add(pSVGGradientStop);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class SVGGradientStop {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final float mOffset;
		private final int mColor;

		// ===========================================================
		// Constructors
		// ===========================================================

		public SVGGradientStop(final float pOffset, final int pColor) {
			this.mOffset = pOffset;
			this.mColor = pColor;
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

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}