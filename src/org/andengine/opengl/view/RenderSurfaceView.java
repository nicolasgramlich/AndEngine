package org.andengine.opengl.view;

import org.andengine.engine.Engine;
import org.andengine.engine.options.ConfigChooserOptions;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:57:29 - 08.03.2010
 */
public class RenderSurfaceView extends GLSurfaceView implements IResolutionPolicy.Callback {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private EngineRenderer mEngineRenderer;
	private ConfigChooser mConfigChooser;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RenderSurfaceView(final Context pContext) {
		super(pContext);

		this.setEGLContextClientVersion(2);
	}

	public RenderSurfaceView(final Context pContext, final AttributeSet pAttrs) {
		super(pContext, pAttrs);

		this.setEGLContextClientVersion(2);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ConfigChooser getConfigChooser() throws IllegalStateException {
		if (this.mConfigChooser == null) {
			throw new IllegalStateException(ConfigChooser.class.getSimpleName() + " not yet set.");
		}
		return this.mConfigChooser;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * @see android.view.View#measure(int, int)
	 */
	@Override
	protected void onMeasure(final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
		this.mEngineRenderer.mEngine.getEngineOptions().getResolutionPolicy().onMeasure(this, pWidthMeasureSpec, pHeightMeasureSpec);
	}

	@Override
	public void onResolutionChanged(final int pWidth, final int pHeight) {
		this.setMeasuredDimension(pWidth, pHeight);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void setRenderer(final Engine pEngine, final IRendererListener pRendererListener) {
		if (this.mConfigChooser == null) {
			final ConfigChooserOptions configChooserOptions = pEngine.getEngineOptions().getRenderOptions().getConfigChooserOptions();
			this.mConfigChooser = new ConfigChooser(configChooserOptions);

			// TODO We don't know yet if the requested color size will actually be accepted!
			if (configChooserOptions.isRequestedRGBA8888()) {
				this.getHolder().setFormat(android.graphics.PixelFormat.RGBA_8888);
			} else if (configChooserOptions.isRequestedRGB565()) {
				this.getHolder().setFormat(android.graphics.PixelFormat.RGB_565);
			}
		}
		this.setEGLConfigChooser(this.mConfigChooser);

		this.setOnTouchListener(pEngine);
		this.mEngineRenderer = new EngineRenderer(pEngine, this.mConfigChooser, pRendererListener);
		this.setRenderer(this.mEngineRenderer);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}