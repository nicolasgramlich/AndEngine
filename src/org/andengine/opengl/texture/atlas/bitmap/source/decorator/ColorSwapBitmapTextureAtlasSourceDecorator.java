package org.andengine.opengl.texture.atlas.bitmap.source.decorator;

import org.andengine.opengl.texture.atlas.bitmap.source.ColorSwapBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape.IBitmapTextureAtlasSourceDecoratorShape;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.system.SystemUtils;

import android.graphics.AvoidXfermode;
import android.graphics.AvoidXfermode.Mode;
import android.os.Build;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @deprecated On {@link Build.VERSION_CODES#JELLY_BEAN} and higher!
 *
 * @see {@link ColorSwapBitmapTextureAtlasSource}
 *
 * @author Nicolas Gramlich
 * @since 19:41:39 - 07.06.2011
 */
@Deprecated
public class ColorSwapBitmapTextureAtlasSourceDecorator extends BaseShapeBitmapTextureAtlasSourceDecorator {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int TOLERANCE_DEFAULT = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mColorKeyColorARGBPackedInt;
	protected final int mTolerance;
	protected final int mColorSwapColorARGBPackedInt;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final Color pColorKeyColor, final Color pColorSwapColor) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor.getARGBPackedInt(), pColorSwapColor.getARGBPackedInt());
	}

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColorARGBPackedInt, final int pColorSwapColorARGBPackedInt) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColorARGBPackedInt, TOLERANCE_DEFAULT, pColorSwapColorARGBPackedInt, null);
	}

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final Color pColorKeyColor, final Color pColorSwapColor, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor.getARGBPackedInt(), pColorSwapColor.getARGBPackedInt(), pTextureAtlasSourceDecoratorOptions);
	}

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColorARGBPackedInt, final int pColorSwapColorARGBPackedInt, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColorARGBPackedInt, TOLERANCE_DEFAULT, pColorSwapColorARGBPackedInt, pTextureAtlasSourceDecoratorOptions);
	}

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final Color pColorKeyColor, final int pTolerance, final Color pColorSwapColor) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor.getARGBPackedInt(), pTolerance, pColorSwapColor.getARGBPackedInt());
	}

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColorARGBPackedInt, final int pTolerance, final int pColorSwapColorARGBPackedInt) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColorARGBPackedInt, pTolerance, pColorSwapColorARGBPackedInt, null);
	}

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final Color pColorKeyColor, final int pTolerance, final Color pColorSwapColor, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		this(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pColorKeyColor.getARGBPackedInt(), pTolerance, pColorSwapColor.getARGBPackedInt(), pTextureAtlasSourceDecoratorOptions);
	}

	public ColorSwapBitmapTextureAtlasSourceDecorator(final IBitmapTextureAtlasSource pBitmapTextureAtlasSource, final IBitmapTextureAtlasSourceDecoratorShape pBitmapTextureAtlasSourceDecoratorShape, final int pColorKeyColorARGBPackedInt, final int pTolerance, final int pColorSwapColorARGBPackedInt, final TextureAtlasSourceDecoratorOptions pTextureAtlasSourceDecoratorOptions) {
		super(pBitmapTextureAtlasSource, pBitmapTextureAtlasSourceDecoratorShape, pTextureAtlasSourceDecoratorOptions);

		this.mColorKeyColorARGBPackedInt = pColorKeyColorARGBPackedInt;
		this.mTolerance = pTolerance;
		this.mColorSwapColorARGBPackedInt = pColorSwapColorARGBPackedInt;
		this.mPaint.setXfermode(new AvoidXfermode(pColorKeyColorARGBPackedInt, pTolerance, Mode.TARGET));
		this.mPaint.setColor(pColorSwapColorARGBPackedInt);

		if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.JELLY_BEAN)) {
			Debug.w("The class " + ColorSwapBitmapTextureAtlasSourceDecorator.class.getSimpleName() + " is deprecated for Android API Level: '" + Build.VERSION_CODES.JELLY_BEAN + "' and higher, since the class " + AvoidXfermode.class.getSimpleName() + " is deprecated since then.");
		}
	}

	@Override
	public ColorSwapBitmapTextureAtlasSourceDecorator deepCopy() {
		return new ColorSwapBitmapTextureAtlasSourceDecorator(this.mBitmapTextureAtlasSource, this.mBitmapTextureAtlasSourceDecoratorShape, this.mColorKeyColorARGBPackedInt, this.mTolerance, this.mColorSwapColorARGBPackedInt, this.mTextureAtlasSourceDecoratorOptions);
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
