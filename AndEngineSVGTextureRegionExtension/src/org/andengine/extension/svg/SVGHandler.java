package org.andengine.extension.svg;

import java.util.Stack;

import org.andengine.extension.svg.adt.ISVGColorMapper;
import org.andengine.extension.svg.adt.SVGGradient;
import org.andengine.extension.svg.adt.SVGGradient.SVGGradientStop;
import org.andengine.extension.svg.adt.SVGGroup;
import org.andengine.extension.svg.adt.SVGPaint;
import org.andengine.extension.svg.adt.SVGProperties;
import org.andengine.extension.svg.adt.filter.SVGFilter;
import org.andengine.extension.svg.adt.filter.element.ISVGFilterElement;
import org.andengine.extension.svg.util.SAXHelper;
import org.andengine.extension.svg.util.SVGCircleParser;
import org.andengine.extension.svg.util.SVGEllipseParser;
import org.andengine.extension.svg.util.SVGLineParser;
import org.andengine.extension.svg.util.SVGPathParser;
import org.andengine.extension.svg.util.SVGPolygonParser;
import org.andengine.extension.svg.util.SVGPolylineParser;
import org.andengine.extension.svg.util.SVGRectParser;
import org.andengine.extension.svg.util.SVGTransformParser;
import org.andengine.extension.svg.util.constants.ISVGConstants;
import org.andengine.util.debug.Debug;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.FloatMath;


/**
 * @author Larva Labs, LLC
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:50:02 - 21.05.2011
 */
public class SVGHandler extends DefaultHandler implements ISVGConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Canvas mCanvas;
	private final Picture mPicture;
	private final SVGPaint mSVGPaint;

	private boolean mBoundsMode;
	private RectF mBounds;

	private final Stack<SVGGroup> mSVGGroupStack = new Stack<SVGGroup>();
	private final SVGPathParser mSVGPathParser = new SVGPathParser();

	private SVGGradient mCurrentSVGGradient;
	private SVGFilter mCurrentSVGFilter;

	private boolean mHidden;

	/** Multi purpose dummy rectangle. */
	private final RectF mRect = new RectF();

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGHandler(final Picture pPicture, final ISVGColorMapper pSVGColorMapper) {
		this.mPicture = pPicture;
		this.mSVGPaint = new SVGPaint(pSVGColorMapper);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public RectF getBounds() {
		return this.mBounds;
	}

	public RectF getComputedBounds() {
		return this.mSVGPaint.getComputedBounds();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void startElement(final String pNamespace, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException {
		/* Ignore everything but rectangles in bounds mode. */
		if (this.mBoundsMode) {
			this.parseBounds(pLocalName, pAttributes);
			return;
		}
		if (pLocalName.equals(TAG_SVG)) {
			this.parseSVG(pAttributes);
		} else if(pLocalName.equals(TAG_DEFS)) {
			// Ignore
		} else if(pLocalName.equals(TAG_GROUP)) {
			this.parseGroup(pAttributes);
		} else if(pLocalName.equals(TAG_LINEARGRADIENT)) {
			this.parseLinearGradient(pAttributes);
		}  else if(pLocalName.equals(TAG_RADIALGRADIENT)) {
			this.parseRadialGradient(pAttributes);
		} else if(pLocalName.equals(TAG_STOP)) {
			this.parseGradientStop(pAttributes);
		} else if(pLocalName.equals(TAG_FILTER)) {
			this.parseFilter(pAttributes);
		} else if(pLocalName.equals(TAG_FILTER_ELEMENT_FEGAUSSIANBLUR)) {
			this.parseFilterElementGaussianBlur(pAttributes);
		} else if(!this.mHidden) {
			if(pLocalName.equals(TAG_RECTANGLE)) {
				this.parseRect(pAttributes);
			} else if(pLocalName.equals(TAG_LINE)) {
				this.parseLine(pAttributes);
			} else if(pLocalName.equals(TAG_CIRCLE)) {
				this.parseCircle(pAttributes);
			} else if(pLocalName.equals(TAG_ELLIPSE)) {
				this.parseEllipse(pAttributes);
			} else if(pLocalName.equals(TAG_POLYLINE)) {
				this.parsePolyline(pAttributes);
			} else if(pLocalName.equals(TAG_POLYGON)) {
				this.parsePolygon(pAttributes);
			} else if(pLocalName.equals(TAG_PATH)) {
				this.parsePath(pAttributes);
			} else {
				Debug.d("Unexpected SVG tag: '" + pLocalName + "'.");
			}
		} else {
			Debug.d("Unexpected SVG tag: '" + pLocalName + "'.");
		}
	}

	@Override
	public void endElement(final String pNamespace, final String pLocalName, final String pQualifiedName) throws SAXException {
		if (pLocalName.equals(TAG_SVG)) {
			this.mPicture.endRecording();
		} else if (pLocalName.equals(TAG_GROUP)) {
			this.parseGroupEnd();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void parseSVG(final Attributes pAttributes) {
		final int width = (int) FloatMath.ceil(SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_WIDTH, 0f));
		final int height = (int) FloatMath.ceil(SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_HEIGHT, 0f));
		this.mCanvas = this.mPicture.beginRecording(width, height);
	}

	private void parseBounds(final String pLocalName, final Attributes pAttributes) {
		if (pLocalName.equals(TAG_RECTANGLE)) {
			final float x = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_X, 0f);
			final float y = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_Y, 0f);
			final float width = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_WIDTH, 0f);
			final float height = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_HEIGHT, 0f);
			this.mBounds = new RectF(x, y, x + width, y + height);
		}
	}

	private void parseFilter(final Attributes pAttributes) {
		this.mCurrentSVGFilter = this.mSVGPaint.parseFilter(pAttributes);
	}

	private void parseFilterElementGaussianBlur(final Attributes pAttributes) {
		final ISVGFilterElement svgFilterElement = this.mSVGPaint.parseFilterElementGaussianBlur(pAttributes);
		this.mCurrentSVGFilter.addFilterElement(svgFilterElement);
	}

	private void parseLinearGradient(final Attributes pAttributes) {
		this.mCurrentSVGGradient = this.mSVGPaint.parseGradient(pAttributes, true);
	}

	private void parseRadialGradient(final Attributes pAttributes) {
		this.mCurrentSVGGradient = this.mSVGPaint.parseGradient(pAttributes, false);
	}

	private void parseGradientStop(final Attributes pAttributes) {
		final SVGGradientStop svgGradientStop = this.mSVGPaint.parseGradientStop(this.getSVGPropertiesFromAttributes(pAttributes));
		this.mCurrentSVGGradient.addSVGGradientStop(svgGradientStop);
	}

	private void parseGroup(final Attributes pAttributes) {
		/* Check to see if this is the "bounds" layer. */
		if ("bounds".equals(SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_ID))) {
			this.mBoundsMode = true;
		}

		final SVGGroup parentSVGGroup = (this.mSVGGroupStack.size() > 0) ? this.mSVGGroupStack.peek() : null;
		final boolean hasTransform = this.pushTransform(pAttributes);

		this.mSVGGroupStack.push(new SVGGroup(parentSVGGroup, this.getSVGPropertiesFromAttributes(pAttributes, true), hasTransform));

		this.updateHidden();
	}

	private void parseGroupEnd() {
		if (this.mBoundsMode) {
			this.mBoundsMode = false;
		}

		/* Pop group transform if there was one pushed. */
		if(this.mSVGGroupStack.pop().hasTransform()) {
			this.popTransform();
		}
		this.updateHidden();
	}

	private void updateHidden() {
		if(this.mSVGGroupStack.size() == 0) {
			this.mHidden = false;
		} else {
			this.mSVGGroupStack.peek().isHidden();
		}
	}

	private void parsePath(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		this.mSVGPathParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parsePolygon(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGPolygonParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parsePolyline(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGPolylineParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parseEllipse(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGEllipseParser.parse(svgProperties, this.mCanvas, this.mSVGPaint, this.mRect);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parseCircle(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGCircleParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parseLine(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGLineParser.parse(svgProperties, this.mCanvas, this.mSVGPaint);
		if(pushed) {
			this.popTransform();
		}
	}

	private void parseRect(final Attributes pAttributes) {
		final SVGProperties svgProperties = this.getSVGPropertiesFromAttributes(pAttributes);
		final boolean pushed = this.pushTransform(pAttributes);
		SVGRectParser.parse(svgProperties, this.mCanvas, this.mSVGPaint, this.mRect);
		if(pushed) {
			this.popTransform();
		}
	}

	private SVGProperties getSVGPropertiesFromAttributes(final Attributes pAttributes) {
		return this.getSVGPropertiesFromAttributes(pAttributes, false);
	}

	private SVGProperties getSVGPropertiesFromAttributes(final Attributes pAttributes, final boolean pDeepCopy) {
		if(this.mSVGGroupStack.size() > 0) {
			return new SVGProperties(this.mSVGGroupStack.peek().getSVGProperties(), pAttributes, pDeepCopy);
		} else {
			return new SVGProperties(null, pAttributes, pDeepCopy);
		}
	}

	private boolean pushTransform(final Attributes pAttributes) {
		final String transform = SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_TRANSFORM);
		if(transform == null) {
			return false;
		} else {
			final Matrix matrix = SVGTransformParser.parseTransform(transform);
			this.mCanvas.save();
			this.mCanvas.concat(matrix);
			return true;
		}
	}

	private void popTransform() {
		this.mCanvas.restore();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}