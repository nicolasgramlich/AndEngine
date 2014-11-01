package org.andengine.opengl.texture.atlas.bitmap.source.decorator.shape;

import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator.TextureAtlasSourceDecoratorOptions;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:52:55 - 04.01.2011
 */
public class ArcBitmapTextureAtlasSourceDecoratorShape implements IBitmapTextureAtlasSourceDecoratorShape {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float STARTANGLE_DEFAULT = 0;
	private static final float SWEEPANGLE_DEFAULT = 360;
	private static final boolean USECENTER_DEFAULT = true;

	// ===========================================================
	// Fields
	// ===========================================================

	private static ArcBitmapTextureAtlasSourceDecoratorShape sDefaultInstance;

	private final RectF mRectF = new RectF();

	private final float mStartAngle;
	private final float mSweepAngle;
	private final boolean mUseCenter;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ArcBitmapTextureAtlasSourceDecoratorShape() {
		this(STARTANGLE_DEFAULT, SWEEPANGLE_DEFAULT, USECENTER_DEFAULT);
	}

	public ArcBitmapTextureAtlasSourceDecoratorShape(final float pStartAngle, final float pSweepAngle, final boolean pUseCenter) {
		this.mStartAngle = pStartAngle;
		this.mSweepAngle = pSweepAngle;
		this.mUseCenter = pUseCenter;
	}

	@Deprecated
	public static ArcBitmapTextureAtlasSourceDecoratorShape getDefaultInstance() {
		if(sDefaultInstance == null) {
			sDefaultInstance = new ArcBitmapTextureAtlasSourceDecoratorShape();
		}
		return sDefaultInstance;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onDecorateBitmap(final Canvas pCanvas, final Paint pPaint, final TextureAtlasSourceDecoratorOptions pDecoratorOptions) {
		final float left = pDecoratorOptions.getInsetLeft();
		final float top = pDecoratorOptions.getInsetTop();
		final float right = pCanvas.getWidth() - pDecoratorOptions.getInsetRight();
		final float bottom = pCanvas.getHeight() - pDecoratorOptions.getInsetBottom();

		this.mRectF.set(left, top, right, bottom);

		pCanvas.drawArc(this.mRectF, this.mStartAngle, this.mSweepAngle, this.mUseCenter, pPaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

