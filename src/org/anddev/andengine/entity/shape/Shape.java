package org.anddev.andengine.entity.shape;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.entity.BaseDynamicEntity;
import org.anddev.andengine.entity.shape.modifier.IShapeModifier;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vertex.VertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 11:51:27 - 13.03.2010
 */
public abstract class Shape extends BaseDynamicEntity implements IShape {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int BLENDFUNCTION_SOURCE_DEFAULT = GL10.GL_ONE;
	private static final int BLENDFUNCTION_DESTINATION_DEFAULT = GL10.GL_ONE_MINUS_SRC_ALPHA;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final VertexBuffer mVertexBuffer;

	protected float mRed = 1;
	protected float mGreen = 1;
	protected float mBlue = 1;
	protected float mAlpha = 1f;

	protected int mSourceBlendFunction = BLENDFUNCTION_SOURCE_DEFAULT;
	protected int mDestinationBlendFunction = BLENDFUNCTION_DESTINATION_DEFAULT;

	private final ArrayList<IShapeModifier> mShapeModifiers = new ArrayList<IShapeModifier>();
	private int mShapeModifierCount = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Shape(final float pX, final float pY, final VertexBuffer pVertexBuffer) {
		super(pX, pY);
		this.mVertexBuffer = pVertexBuffer;
		BufferObjectManager.loadBufferObject(this.mVertexBuffer);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public float getRed() {
		return this.mRed;
	}

	@Override
	public float getGreen() {
		return this.mGreen;
	}

	@Override
	public float getBlue() {
		return this.mBlue;
	}

	@Override
	public float getAlpha() {
		return this.mAlpha;
	}

	@Override
	public VertexBuffer getVertexBuffer() {
		return this.mVertexBuffer;
	}

	@Override
	public void setAlpha(final float pAlpha) {
		this.mAlpha = pAlpha;
	}

	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
	}

	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
		this.mAlpha = pAlpha;
	}

	@Override
	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mSourceBlendFunction = pSourceBlendFunction;
		this.mDestinationBlendFunction = pDestinationBlendFunction;
	}

	@Override
	public float getWidthScaled() {
		return this.getWidth() * this.mScaleX;
	}

	@Override
	public float getHeightScaled() {
		return this.getHeight() * this.mScaleY;
	}

	@Override
	public void addShapeModifier(final IShapeModifier pShapeModifier) {
		this.mShapeModifiers.add(pShapeModifier);
		this.mShapeModifierCount++;
	}

	@Override
	public void removeShapeModifier(final IShapeModifier pShapeModifier) {
		this.mShapeModifierCount--;
		this.mShapeModifiers.remove(pShapeModifier);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		this.updateShapeModifiers(pSecondsElapsed);
	}

	protected abstract void onUpdateVertexBuffer();

	@Override
	protected void onManagedDraw(final GL10 pGL) {
		this.onInitDraw(pGL);

		pGL.glPushMatrix();

		this.onApplyVertices(pGL);

		this.onApplyTransformations(pGL);

		this.drawVertices(pGL);
		pGL.glPopMatrix();
	}

	protected abstract void applyOffset(final GL10 pGL);
	protected abstract void applyTranslation(final GL10 pGL);
	protected abstract void applyRotation(final GL10 pGL);
	protected abstract void applyScale(final GL10 pGL);
	protected abstract void drawVertices(final GL10 pGL);

	@Override
	public void reset() {
		super.reset();
		this.mRed = 1.0f;
		this.mGreen = 1.0f;
		this.mBlue = 1.0f;
		this.mAlpha = 1.0f;

		this.mSourceBlendFunction = BLENDFUNCTION_SOURCE_DEFAULT;
		this.mDestinationBlendFunction = BLENDFUNCTION_DESTINATION_DEFAULT;

		final ArrayList<IShapeModifier> shapeModifiers = this.mShapeModifiers;
		for(int i = shapeModifiers.size() - 1; i >= 0; i--) {
			shapeModifiers.get(i).reset();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void updateVertexBuffer() {
		this.onUpdateVertexBuffer();
	}

	private void updateShapeModifiers(final float pSecondsElapsed) {
		final ArrayList<IShapeModifier> shapeModifiers = this.mShapeModifiers;
		final int shapeModifierCount = this.mShapeModifierCount;
		if(shapeModifierCount > 0) {
			for(int i = shapeModifierCount - 1; i >= 0; i--) {
				final IShapeModifier shapeModifier = shapeModifiers.get(i);
				shapeModifier.onUpdateShape(pSecondsElapsed, this);
				if(shapeModifier.isFinished() && shapeModifier.isRemoveWhenFinished()) { // TODO <-- could be combined into one function.
					this.mShapeModifierCount--;
					shapeModifiers.remove(i);
				}
			}
		}
	}

	protected void onInitDraw(final GL10 pGL) {
		GLHelper.setColor(pGL, this.mRed, this.mGreen, this.mBlue, this.mAlpha);

		GLHelper.enableVertexArray(pGL);
		GLHelper.blendFunction(pGL, this.mSourceBlendFunction, this.mDestinationBlendFunction);
	}

	private void onApplyVertices(final GL10 pGL) {
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			final GL11 gl11 = (GL11)pGL;

			this.mVertexBuffer.selectOnHardware(gl11);
			GLHelper.vertexZeroPointer(gl11);
		} else {
			GLHelper.vertexPointer(pGL, this.getVertexBuffer().getFloatBuffer());
		}
	}

	protected void onApplyTransformations(final GL10 pGL) {
		this.applyOffset(pGL);

		this.applyTranslation(pGL);

		this.applyRotation(pGL);

		this.applyScale(pGL);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
