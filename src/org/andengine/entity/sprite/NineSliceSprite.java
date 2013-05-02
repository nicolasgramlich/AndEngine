package org.andengine.entity.sprite;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:50:42 - 25.04.2012
 */
public class NineSliceSprite extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int NINESLICESPRITE_CHILD_COUNT = 9;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ITextureRegion mTextureRegion;

	protected final ITextureRegion mTopLeftTextureRegion;
	protected final ITextureRegion mTopCenterTextureRegion;
	protected final ITextureRegion mTopRightTextureRegion;
	protected final ITextureRegion mCenterLeftTextureRegion;
	protected final ITextureRegion mCenterTextureRegion;
	protected final ITextureRegion mCenterRightTextureRegion;
	protected final ITextureRegion mBottomLeftTextureRegion;
	protected final ITextureRegion mBottomCenterTextureRegion;
	protected final ITextureRegion mBottomRightTextureRegion;

	protected final SpriteBatch mSpriteBatch;

	protected float mInsetLeft;
	protected float mInsetTop;
	protected float mInsetRight;
	protected float mInsetBottom;

	// ===========================================================
	// Constructors
	// ===========================================================

	public NineSliceSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pTextureRegion, pInsetLeft, pInsetTop, pInsetRight, pInsetBottom, pVertexBufferObjectManager, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public NineSliceSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pInsetLeft, pInsetTop, pInsetRight, pInsetBottom, pVertexBufferObjectManager, pShaderProgram);
	}

	public NineSliceSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pWidth, pHeight, pTextureRegion, pInsetLeft, pInsetTop, pInsetRight, pInsetBottom, pVertexBufferObjectManager, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public NineSliceSprite(final float pX, final float pY, final float pWidth, final float pHeight, final ITextureRegion pTextureRegion, final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(pX, pY, pWidth, pHeight);

		this.mTextureRegion = pTextureRegion;
		this.mInsetLeft = pInsetLeft;
		this.mInsetTop = pInsetTop;
		this.mInsetRight = pInsetRight;
		this.mInsetBottom = pInsetBottom;

		final ITexture texture = pTextureRegion.getTexture();
		this.mSpriteBatch = new SpriteBatch(texture, NINESLICESPRITE_CHILD_COUNT, pVertexBufferObjectManager, pShaderProgram);

		this.mTopLeftTextureRegion = new TextureRegion(texture, 0, 0, 0, 0);
		this.mTopCenterTextureRegion = new TextureRegion(texture, 0, 0, 0, 0);
		this.mTopRightTextureRegion = new TextureRegion(texture, 0, 0, 0, 0);
		this.mCenterLeftTextureRegion = new TextureRegion(texture, 0, 0, 0, 0);
		this.mCenterTextureRegion = new TextureRegion(texture, 0, 0, 0, 0);
		this.mCenterRightTextureRegion = new TextureRegion(texture, 0, 0, 0, 0);
		this.mBottomLeftTextureRegion = new TextureRegion(texture, 0, 0, 0, 0);
		this.mBottomCenterTextureRegion = new TextureRegion(texture, 0, 0, 0, 0);
		this.mBottomRightTextureRegion = new TextureRegion(texture, 0, 0, 0, 0);

		this.updateTextureRegions();
		this.updateVertices();

		this.attachChild(this.mSpriteBatch);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getInsetLeft() {
		return this.mInsetLeft;
	}

	public void setInsetLeft(final float pInsetLeft) {
		this.mInsetLeft = pInsetLeft;

		this.updateTextureRegions();
		this.updateVertices();
	}

	public float getInsetTop() {
		return this.mInsetTop;
	}

	public void setInsetTop(final float pInsetTop) {
		this.mInsetTop = pInsetTop;

		this.updateTextureRegions();
		this.updateVertices();
	}

	public float getInsetRight() {
		return this.mInsetRight;
	}

	public void setInsetRight(final float pInsetRight) {
		this.mInsetRight = pInsetRight;

		this.updateTextureRegions();
		this.updateVertices();
	}

	public float getInsetBottom() {
		return this.mInsetBottom;
	}

	public void setInsetBottom(final float pInsetBottom) {
		this.mInsetBottom = pInsetBottom;

		this.updateTextureRegions();
		this.updateVertices();
	}

	public void setInsets(final float pInsetLeft, final float pInsetTop, final float pInsetRight, final float pInsetBottom) {
		this.mInsetLeft = pInsetLeft;
		this.mInsetTop = pInsetTop;
		this.mInsetRight = pInsetRight;
		this.mInsetBottom = pInsetBottom;

		this.updateTextureRegions();
		this.updateVertices();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void setWidth(final float pWidth) {
		super.setWidth(pWidth);

		this.updateVertices();
	}

	@Override
	public void setHeight(final float pHeight) {
		super.setHeight(pHeight);

		this.updateVertices();
	}

	@Override
	public void setSize(final float pWidth, final float pHeight) {
		super.setSize(pWidth, pHeight);

		this.updateVertices();
	}

	@Override
	protected void onUpdateColor() {
		this.updateVertices();
	}

	@Override
	public void dispose() {
		super.dispose();

		this.mSpriteBatch.dispose();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void updateTextureRegions() {
		final float baseX = this.mTextureRegion.getTextureX();
		final float baseY = this.mTextureRegion.getTextureY();
		final float baseWidth = this.mTextureRegion.getWidth();
		final float baseHeight = this.mTextureRegion.getHeight();

		final float centerWidth = baseWidth - this.mInsetLeft - this.mInsetRight;
		final float centerHeight = baseHeight - this.mInsetTop - this.mInsetBottom;

		/* Cache some variables. */
		final float leftX = baseX;
		final float centerX = baseX + this.mInsetLeft;
		final float rightX = (baseX + baseWidth) - this.mInsetRight;

		/* Top. */
		final float topY = baseY;
		this.mTopLeftTextureRegion.set(leftX, topY, this.mInsetLeft, this.mInsetTop);
		this.mTopCenterTextureRegion.set(centerX, topY, centerWidth, this.mInsetTop);
		this.mTopRightTextureRegion.set(rightX, topY, this.mInsetRight, this.mInsetTop);

		/* Center. */
		final float centerY = baseY + this.mInsetTop;
		this.mCenterLeftTextureRegion.set(leftX, centerY, this.mInsetLeft, centerHeight);
		this.mCenterTextureRegion.set(centerX, centerY, centerWidth, centerHeight);
		this.mCenterRightTextureRegion.set(rightX, centerY, this.mInsetRight, centerHeight);

		/* Bottom. */
		final float bottomY = (baseY + baseHeight) - this.mInsetBottom;
		this.mBottomLeftTextureRegion.set(leftX, bottomY, this.mInsetLeft, this.mInsetBottom);
		this.mBottomCenterTextureRegion.set(centerX, bottomY, centerWidth, this.mInsetBottom);
		this.mBottomRightTextureRegion.set(rightX, bottomY, this.mInsetRight, this.mInsetBottom);
	}

	private void updateVertices() {
		this.mSpriteBatch.reset();

		final float color = this.mColor.getABGRPackedFloat();

		final float centerWidth = this.mWidth - this.mInsetLeft - this.mInsetRight;
		final float centerHeight = this.mHeight - this.mInsetTop - this.mInsetBottom;

		/* Cache some variables. */
		final float leftX = 0;
		final float centerX = this.mInsetLeft;
		final float rightX = this.mWidth - this.mInsetRight;

		/* Top. */
		final float topY = this.mHeight - this.mInsetTop;
		this.mSpriteBatch.draw(this.mTopLeftTextureRegion, leftX, topY, this.mInsetLeft, this.mInsetTop, color);
		this.mSpriteBatch.draw(this.mTopCenterTextureRegion, centerX, topY, centerWidth, this.mInsetTop, color);
		this.mSpriteBatch.draw(this.mTopRightTextureRegion, rightX, topY, this.mInsetRight, this.mInsetTop, color);

		/* Center. */
		final float centerY = this.mInsetBottom;
		this.mSpriteBatch.draw(this.mCenterLeftTextureRegion, leftX, centerY, this.mInsetLeft, centerHeight, color);
		this.mSpriteBatch.draw(this.mCenterTextureRegion, centerX, centerY, centerWidth, centerHeight, color);
		this.mSpriteBatch.draw(this.mCenterRightTextureRegion, rightX, centerY, this.mInsetRight, centerHeight, color);

		/* Bottom. */
		final float bottomY = 0;
		this.mSpriteBatch.draw(this.mBottomLeftTextureRegion, leftX, bottomY, this.mInsetLeft, this.mInsetBottom, color);
		this.mSpriteBatch.draw(this.mBottomCenterTextureRegion, centerX, bottomY, centerWidth, this.mInsetBottom, color);
		this.mSpriteBatch.draw(this.mBottomRightTextureRegion, rightX, bottomY, this.mInsetRight, this.mInsetBottom, color);

		this.mSpriteBatch.submit();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
