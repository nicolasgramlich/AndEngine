package org.andengine.extension.svg.adt;

import java.util.HashMap;

import org.andengine.extension.svg.adt.SVGGradient.SVGGradientStop;
import org.andengine.extension.svg.adt.filter.SVGFilter;
import org.andengine.extension.svg.adt.filter.element.SVGFilterElementGaussianBlur;
import org.andengine.extension.svg.exception.SVGParseException;
import org.andengine.extension.svg.util.SAXHelper;
import org.andengine.extension.svg.util.SVGParserUtils;
import org.andengine.extension.svg.util.constants.ColorUtils;
import org.andengine.extension.svg.util.constants.ISVGConstants;
import org.xml.sax.Attributes;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:01:39 - 23.05.2011
 */
public class SVGPaint implements ISVGConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mPaint = new Paint();

	private final ISVGColorMapper mSVGColorMapper;

	/** Multi purpose dummy rectangle. */
	private final RectF mRect = new RectF();
	private final RectF mComputedBounds = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

	private final HashMap<String, SVGGradient> mSVGGradientMap = new HashMap<String, SVGGradient>();
	private final HashMap<String, SVGFilter> mSVGFilterMap = new HashMap<String, SVGFilter>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGPaint(final ISVGColorMapper pSVGColorMapper) {
		this.mSVGColorMapper = pSVGColorMapper;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Paint getPaint() {
		return this.mPaint;
	}

	public RectF getComputedBounds() {
		return this.mComputedBounds;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void resetPaint(final Style pStyle) {
		this.mPaint.reset();
		this.mPaint.setAntiAlias(true); // TODO AntiAliasing could be made optional through some SVGOptions object.
		this.mPaint.setStyle(pStyle);
	}

	/**
	 *  TODO Would it be better/cleaner to throw a SVGParseException when sth could not be parsed instead of simply returning false?
	 */
	public boolean setFill(final SVGProperties pSVGProperties) {
		if(this.isDisplayNone(pSVGProperties) || this.isFillNone(pSVGProperties)) {
			return false;
		}

		this.resetPaint(Paint.Style.FILL);

		final String fillProperty = pSVGProperties.getStringProperty(ATTRIBUTE_FILL);
		if(fillProperty == null) {
			if(pSVGProperties.getStringProperty(ATTRIBUTE_STROKE) == null) {
				/* Default is black fill. */
				this.mPaint.setColor(0xFF000000); // TODO Respect color mapping?
				return true;
			} else {
				return false;
			}
		} else {
			return this.applyPaintProperties(pSVGProperties, true);
		}
	}

	public boolean setStroke(final SVGProperties pSVGProperties) {
		if(this.isDisplayNone(pSVGProperties) || this.isStrokeNone(pSVGProperties)) {
			return false;
		}

		this.resetPaint(Paint.Style.STROKE);

		return this.applyPaintProperties(pSVGProperties, false);
	}

	private boolean isDisplayNone(final SVGProperties pSVGProperties) {
		return VALUE_NONE.equals(pSVGProperties.getStringProperty(ATTRIBUTE_DISPLAY));
	}

	private boolean isFillNone(final SVGProperties pSVGProperties) {
		return VALUE_NONE.equals(pSVGProperties.getStringProperty(ATTRIBUTE_FILL));
	}

	private boolean isStrokeNone(final SVGProperties pSVGProperties) {
		return VALUE_NONE.equals(pSVGProperties.getStringProperty(ATTRIBUTE_STROKE));
	}

	public boolean applyPaintProperties(final SVGProperties pSVGProperties, final boolean pModeFill) {
		if(this.setColorProperties(pSVGProperties, pModeFill)) {
			if(pModeFill) {
				return this.applyFillProperties(pSVGProperties);
			} else {
				return this.applyStrokeProperties(pSVGProperties);
			}
		} else {
			return false;
		}
	}

	private boolean setColorProperties(final SVGProperties pSVGProperties, final boolean pModeFill) { // TODO throw SVGParseException
		final String colorProperty = pSVGProperties.getStringProperty(pModeFill ? ATTRIBUTE_FILL : ATTRIBUTE_STROKE);
		if(colorProperty == null) {
			return false;
		}

		final String filterProperty = pSVGProperties.getStringProperty(ATTRIBUTE_FILTER);
		if(filterProperty != null) {
			if(SVGProperties.isURLProperty(filterProperty)) {
				final String filterID = SVGParserUtils.extractIDFromURLProperty(filterProperty);

				this.getFilter(filterID).applyFilterElements(this.mPaint);
			} else {
				return false;
			}
		}

		if(SVGProperties.isURLProperty(colorProperty)) {
			final String gradientID = SVGParserUtils.extractIDFromURLProperty(colorProperty);

			this.mPaint.setShader(this.getGradientShader(gradientID));
			return true;
		} else {
			final Integer color = this.parseColor(colorProperty);
			if(color != null) {
				this.applyColor(pSVGProperties, color, pModeFill);
				return true;
			} else {
				return false;
			}
		}
	}

	private boolean applyFillProperties(final SVGProperties pSVGProperties) {
		return true;
	}

	private boolean applyStrokeProperties(final SVGProperties pSVGProperties) {
		final Float width = pSVGProperties.getFloatProperty(ATTRIBUTE_STROKE_WIDTH);
		if (width != null) {
			this.mPaint.setStrokeWidth(width);
		}
		final String linecap = pSVGProperties.getStringProperty(ATTRIBUTE_STROKE_LINECAP);
		if (ATTRIBUTE_STROKE_LINECAP_VALUE_ROUND.equals(linecap)) {
			this.mPaint.setStrokeCap(Paint.Cap.ROUND);
		} else if (ATTRIBUTE_STROKE_LINECAP_VALUE_SQUARE.equals(linecap)) {
			this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
		} else if (ATTRIBUTE_STROKE_LINECAP_VALUE_BUTT.equals(linecap)) {
			this.mPaint.setStrokeCap(Paint.Cap.BUTT);
		}
		final String linejoin = pSVGProperties.getStringProperty(ATTRIBUTE_STROKE_LINEJOIN_VALUE_);
		if (ATTRIBUTE_STROKE_LINEJOIN_VALUE_MITER.equals(linejoin)) {
			this.mPaint.setStrokeJoin(Paint.Join.MITER);
		} else if (ATTRIBUTE_STROKE_LINEJOIN_VALUE_ROUND.equals(linejoin)) {
			this.mPaint.setStrokeJoin(Paint.Join.ROUND);
		} else if (ATTRIBUTE_STROKE_LINEJOIN_VALUE_BEVEL.equals(linejoin)) {
			this.mPaint.setStrokeJoin(Paint.Join.BEVEL);
		}
		return true;
	}

	private void applyColor(final SVGProperties pSVGProperties, final Integer pColor, final boolean pModeFill) {
		final int c = (ColorUtils.COLOR_MASK_32BIT_ARGB_RGB & pColor) | ColorUtils.COLOR_MASK_32BIT_ARGB_ALPHA;
		this.mPaint.setColor(c);
		this.mPaint.setAlpha(SVGPaint.parseAlpha(pSVGProperties, pModeFill));
	}

	private static int parseAlpha(final SVGProperties pSVGProperties, final boolean pModeFill) {
		Float opacity = pSVGProperties.getFloatProperty(ATTRIBUTE_OPACITY);
		if(opacity == null) {
			opacity = pSVGProperties.getFloatProperty(pModeFill ? ATTRIBUTE_FILL_OPACITY : ATTRIBUTE_STROKE_OPACITY);
		}
		if(opacity == null) {
			return 255;
		} else {
			return (int) (255 * opacity);
		}
	}

	public void ensureComputedBoundsInclude(final float pX, final float pY) {
		if (pX < this.mComputedBounds.left) {
			this.mComputedBounds.left = pX;
		}
		if (pX > this.mComputedBounds.right) {
			this.mComputedBounds.right = pX;
		}
		if (pY < this.mComputedBounds.top) {
			this.mComputedBounds.top = pY;
		}
		if (pY > this.mComputedBounds.bottom) {
			this.mComputedBounds.bottom = pY;
		}
	}

	public void ensureComputedBoundsInclude(final float pX, final float pY, final float pWidth, final float pHeight) {
		this.ensureComputedBoundsInclude(pX, pY);
		this.ensureComputedBoundsInclude(pX + pWidth, pY + pHeight);
	}

	public void ensureComputedBoundsInclude(final Path pPath) {
		pPath.computeBounds(this.mRect, false);
		this.ensureComputedBoundsInclude(this.mRect.left, this.mRect.top);
		this.ensureComputedBoundsInclude(this.mRect.right, this.mRect.bottom);
	}

	// ===========================================================
	// Methods for Colors
	// ===========================================================

	private Integer parseColor(final String pString, final Integer pDefault) {
		final Integer color = this.parseColor(pString);
		if(color == null) {
			return this.applySVGColorMapper(pDefault);
		} else {
			return color;
		}
	}

	private Integer parseColor(final String pString) {
		/* TODO Test if explicit pattern matching is faster:
		 * 
		 * RGB:		/^rgb\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3})\)$/
		 * #RRGGBB:	/^(\w{2})(\w{2})(\w{2})$/
		 * #RGB:	/^(\w{1})(\w{1})(\w{1})$/
		 */

		final Integer parsedColor;
		if(pString == null) {
			parsedColor = null;
		} else if(SVGProperties.isHexProperty(pString)) {
			parsedColor = SVGParserUtils.extractColorFromHexProperty(pString);
		} else if(SVGProperties.isRGBProperty(pString)) {
			parsedColor = SVGParserUtils.extractColorFromRGBProperty(pString);
		} else {
			final Integer colorByName = ColorUtils.getColorByName(pString.trim());
			if(colorByName != null) {
				parsedColor = colorByName;
			} else {
				parsedColor = SVGParserUtils.extraColorIntegerProperty(pString);
			}
		}
		return this.applySVGColorMapper(parsedColor);
	}

	private Integer applySVGColorMapper(final Integer pColor) {
		if(this.mSVGColorMapper == null) {
			return pColor;
		} else {
			return this.mSVGColorMapper.mapColor(pColor);
		}
	}

	// ===========================================================
	// Methods for Gradients
	// ===========================================================

	public SVGFilter parseFilter(final Attributes pAttributes) {
		final String id = SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_ID);
		if(id == null) {
			return null;
		}

		final SVGFilter svgFilter = new SVGFilter(id, pAttributes);
		this.mSVGFilterMap.put(id, svgFilter);
		return svgFilter;
	}

	public SVGGradient parseGradient(final Attributes pAttributes, final boolean pLinear) {
		final String id = SAXHelper.getStringAttribute(pAttributes, ATTRIBUTE_ID);
		if(id == null) {
			return null;
		}

		final SVGGradient svgGradient = new SVGGradient(id, pLinear, pAttributes);
		this.mSVGGradientMap.put(id, svgGradient);
		return svgGradient;
	}

	public SVGGradientStop parseGradientStop(final SVGProperties pSVGProperties) {
		final float offset = pSVGProperties.getFloatProperty(ATTRIBUTE_OFFSET, 0f);
		final String stopColor = pSVGProperties.getStringProperty(ATTRIBUTE_STOP_COLOR);
		final int rgb = this.parseColor(stopColor.trim(), Color.BLACK);
		final int alpha = this.parseGradientStopAlpha(pSVGProperties);
		return new SVGGradientStop(offset, alpha | rgb);
	}

	private int parseGradientStopAlpha(final SVGProperties pSVGProperties) {
		final String opacityStyle = pSVGProperties.getStringProperty(ATTRIBUTE_STOP_OPACITY);
		if(opacityStyle != null) {
			final float alpha = Float.parseFloat(opacityStyle);
			final int alphaInt = Math.round(255 * alpha);
			return (alphaInt << 24);
		} else {
			return ColorUtils.COLOR_MASK_32BIT_ARGB_ALPHA;
		}
	}

	private Shader getGradientShader(final String pGradientShaderID) {
		final SVGGradient svgGradient = this.mSVGGradientMap.get(pGradientShaderID);
		if(svgGradient == null) {
			throw new SVGParseException("No SVGGradient found for id: '" + pGradientShaderID + "'.");
		} else {
			final Shader gradientShader = svgGradient.getShader();
			if(gradientShader != null) {
				return gradientShader;
			} else {
				svgGradient.ensureHrefResolved(this.mSVGGradientMap);
				return svgGradient.createShader();
			}
		}
	}

	// ===========================================================
	// Methods for Filters
	// ===========================================================

	private SVGFilter getFilter(final String pSVGFilterID) {
		final SVGFilter svgFilter = this.mSVGFilterMap.get(pSVGFilterID);
		if(svgFilter == null) {
			return null; // TODO Better a SVGParseException here?
		} else {
			svgFilter.ensureHrefResolved(this.mSVGFilterMap);
			return svgFilter;
		}
	}

	public SVGFilterElementGaussianBlur parseFilterElementGaussianBlur(final Attributes pAttributes) {
		final float standardDeviation = SAXHelper.getFloatAttribute(pAttributes, ATTRIBUTE_FILTER_ELEMENT_FEGAUSSIANBLUR_STANDARDDEVIATION);
		return new SVGFilterElementGaussianBlur(standardDeviation);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
