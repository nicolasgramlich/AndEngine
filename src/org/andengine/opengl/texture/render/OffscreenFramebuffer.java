package org.andengine.opengl.texture.render;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.render.RenderTexture;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;

/**
 * (c) 2012 Michal Stawinski
 * 
 * @author Michal Stawinski (nazgee)
 * @since 14:25:02 - 21.07.2012
 */
public class OffscreenFramebuffer extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private final RenderTexture mRenderTexture;
	private final Color mClearColor;
	// ===========================================================
	// Constructors
	// ===========================================================

	public OffscreenFramebuffer(final float pWidth, final float pHeight, final RenderTexture pRenderTexture) {
		this(pWidth, pHeight, pRenderTexture, null);
	}

	public OffscreenFramebuffer(final float pWidth, final float pHeight, final RenderTexture pRenderTexture, final Color pClearColor) {
		super(pWidth/2, pHeight/2, pWidth, pHeight);
		this.mRenderTexture = pRenderTexture;
		this.mClearColor = pClearColor;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public ITextureRegion getTextureRegion() {
		return TextureRegionFactory.extractFromTexture(this.mRenderTexture);
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(GLState pGLState, Camera pCamera) {
		if (!this.mRenderTexture.isInitialized()) {
			this.mRenderTexture.init(pGLState);
		}

		{
			if (mClearColor != null) {
				mRenderTexture.begin(pGLState, false, true, this.mClearColor);
			} else {
				this.mRenderTexture.begin(pGLState, false, true);
			}

			final float scaleX = this.mRenderTexture.getWidth()/getWidth();
			final float scaleY = this.mRenderTexture.getHeight()/getHeight();
			pGLState.scaleProjectionGLMatrixf(scaleX, scaleY, 1);

			super.onManagedDraw(pGLState, pCamera);

			this.mRenderTexture.end(pGLState);
		}
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
