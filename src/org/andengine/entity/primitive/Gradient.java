package org.andengine.entity.primitive;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.vbo.HighPerformanceGradientVertexBufferObject;
import org.andengine.entity.primitive.vbo.IGradientVertexBufferObject;
import org.andengine.entity.shape.Shape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.adt.color.Color;
import org.andengine.util.math.MathUtils;

import android.opengl.GLES20;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:23:56 - 23.04.2012
 */
public class Gradient extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Gradient.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Gradient.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	public static final int VERTICES_PER_RECTANGLE = 4;
	public static final int RECTANGLE_SIZE = Gradient.VERTEX_SIZE * Gradient.VERTICES_PER_RECTANGLE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IGradientVertexBufferObject mGradientVertexBufferObject;

	private final Color mToColor = new Color(Color.WHITE);
	private float mGradientVectorX = 0;
	private float mGradientVectorY = -1;
	private boolean mGradientFitToBounds;
	private boolean mGradientDitherEnabled;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link HighPerformanceGradientVertexBufferObject} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Gradient#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Gradient(final float pX, final float pY, final float pWidth, final float pHeight, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pWidth, pHeight, pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link HighPerformanceGradientVertexBufferObject} with the {@link VertexBufferObjectAttribute}s: {@link Gradient#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Gradient(final float pX, final float pY, final float pWidth, final float pHeight, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pWidth, pHeight, new HighPerformanceGradientVertexBufferObject(pVertexBufferObjectManager, Gradient.RECTANGLE_SIZE, pDrawType, true, Gradient.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public Gradient(final float pX, final float pY, final float pWidth, final float pHeight, final IGradientVertexBufferObject pGradientVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, PositionColorShaderProgram.getInstance());

		this.mGradientVertexBufferObject = pGradientVertexBufferObject;

		this.onUpdateVertices();
		this.onUpdateColor();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isGradientFitToBounds() {
		return this.mGradientFitToBounds;
	}

	/**
	 * @param pGradientFitToBounds If <code>true</code> ensures the from and to color are always on the bounds of the {@link Gradient} entity.
	 * 								If <code>false</code> the from and to color might exceed the bounds of the {@link Gradient} entity, depending on the angle/vector supplied.
	 */
	public void setGradientFitToBounds(final boolean pGradientFitToBounds) {
		if (this.mGradientFitToBounds != pGradientFitToBounds) {
			this.mGradientFitToBounds = pGradientFitToBounds;

			this.onUpdateColor();
		}
	}

	public boolean isGradientDitherEnabled() {
		return this.mGradientDitherEnabled;
	}

	/**
	 * @param pGradientDitherEnabled <code>true</code> to enable dither, <code>false</code> to use the currently active dither setting in {@link GLState}.
	 */
	public void setGradientDitherEnabled(final boolean pGradientDitherEnabled) {
		this.mGradientDitherEnabled = pGradientDitherEnabled;
	}

	@Deprecated
	@Override
	public float getRed() {
		return super.getRed();
	}

	@Deprecated
	@Override
	public float getGreen() {
		return super.getGreen();
	}

	@Deprecated
	@Override
	public float getBlue() {
		return super.getBlue();
	}

	@Deprecated
	@Override
	public float getAlpha() {
		return super.getAlpha();
	}

	@Deprecated
	@Override
	public Color getColor() {
		return super.getColor();
	}

	@Deprecated
	@Override
	public void setRed(final float pRed) {
		super.setRed(pRed);
	}

	@Deprecated
	@Override
	public void setGreen(final float pGreen) {
		super.setGreen(pGreen);
	}

	@Deprecated
	@Override
	public void setBlue(final float pBlue) {
		super.setBlue(pBlue);
	}

	@Deprecated
	@Override
	public void setAlpha(final float pAlpha) {
		super.setAlpha(pAlpha);
	}

	@Deprecated
	@Override
	public void setColor(final Color pColor) {
		super.setColor(pColor);
	}

	@Deprecated
	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue) {
		super.setColor(pRed, pGreen, pBlue);
	}

	@Deprecated
	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		super.setColor(pRed, pGreen, pBlue, pAlpha);
	}

	public float getFromRed() {
		return super.getRed();
	}

	public float getFromGreen() {
		return super.getGreen();
	}

	public float getFromBlue() {
		return super.getBlue();
	}

	public float getFromAlpha() {
		return super.getAlpha();
	}

	public Color getFromColor() {
		return super.getColor();
	}

	/**
	 * @param pRed from <code>0.0f</code> to <code>1.0f</code>
	 */
	public void setFromRed(final float pRed) {
		super.setRed(pRed);
	}

	/**
	 * @param pGreen from <code>0.0f</code> to <code>1.0f</code>
	 */
	public void setFromGreen(final float pGreen) {
		super.setGreen(pGreen);
	}

	/**
	 * @param pBlue from <code>0.0f</code> to <code>1.0f</code>
	 */
	public void setFromBlue(final float pBlue) {
		super.setBlue(pBlue);
	}

	/**
	 * @param pAlpha from <code>0.0f</code> (transparent) to <code>1.0f</code> (opaque)
	 */
	public void setFromAlpha(final float pAlpha) {
		super.setAlpha(pAlpha);
	}

	/**
	 * @param pRed from <code>0.0f</code> to <code>1.0f</code>
	 * @param pGreen from <code>0.0f</code> to <code>1.0f</code>
	 * @param pBlue from <code>0.0f</code> to <code>1.0f</code>
	 */
	public void setFromColor(final float pRed, final float pGreen, final float pBlue) {
		super.setColor(pRed, pGreen, pBlue);
	}

	/**
	 * @param pRed from <code>0.0f</code> to <code>1.0f</code>
	 * @param pGreen from <code>0.0f</code> to <code>1.0f</code>
	 * @param pBlue from <code>0.0f</code> to <code>1.0f</code>
	 * @param pAlpha from <code>0.0f</code> (transparent) to <code>1.0f</code> (opaque)
	 */
	public void setFromColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		super.setColor(pRed, pGreen, pBlue, pAlpha);
	}

	public void setFromColor(final Color pColor) {
		super.setColor(pColor);
	}

	public float getToRed() {
		return this.mToColor.getRed();
	}

	public float getToGreen() {
		return this.mToColor.getGreen();
	}

	public float getToBlue() {
		return this.mToColor.getBlue();
	}

	public float getToAlpha() {
		return this.mToColor.getAlpha();
	}

	public Color getToColor() {
		return this.mToColor;
	}

	/**
	 * @param pRed from <code>0.0f</code> to <code>1.0f</code>
	 */
	public void setToRed(final float pRed) {
		if (this.mToColor.setRedChecking(pRed)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	/**
	 * @param pGreen from <code>0.0f</code> to <code>1.0f</code>
	 */
	public void setToGreen(final float pGreen) {
		if (this.mToColor.setGreenChecking(pGreen)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	/**
	 * @param pBlue from <code>0.0f</code> to <code>1.0f</code>
	 */
	public void setToBlue(final float pBlue) {
		if (this.mToColor.setBlueChecking(pBlue)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	/**
	 * @param pAlpha from <code>0.0f</code> (transparent) to <code>1.0f</code> (opaque)
	 */
	public void setToAlpha(final float pAlpha) {
		if (this.mToColor.setAlphaChecking(pAlpha)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	/**
	 * @param pRed from <code>0.0f</code> to <code>1.0f</code>
	 * @param pGreen from <code>0.0f</code> to <code>1.0f</code>
	 * @param pBlue from <code>0.0f</code> to <code>1.0f</code>
	 */
	public void setToColor(final float pRed, final float pGreen, final float pBlue) {
		if (this.mToColor.setChecking(pRed, pGreen, pBlue)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	/**
	 * @param pRed from <code>0.0f</code> to <code>1.0f</code>
	 * @param pGreen from <code>0.0f</code> to <code>1.0f</code>
	 * @param pBlue from <code>0.0f</code> to <code>1.0f</code>
	 * @param pAlpha from <code>0.0f</code> (transparent) to <code>1.0f</code> (opaque)
	 */
	public void setToColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if (this.mToColor.setChecking(pRed, pGreen, pBlue, pAlpha)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	public void setToColor(final Color pColor) {
		if (this.mToColor.setChecking(pColor)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	public float getGradientVectorX() {
		return this.mGradientVectorX;
	}

	public float getGradientVectorY() {
		return this.mGradientVectorY;
	}

	public void setGradientVectorX(final float pGradientVectorX) {
		this.mGradientVectorX = pGradientVectorX;

		this.onUpdateColor();
	}

	public void setGradientVectorY(final float pGradientVectorY) {
		this.mGradientVectorY = pGradientVectorY;

		this.onUpdateColor();
	}

	public void setGradientVector(final float pGradientVectorX, final float pGradientVectorY) {
		this.mGradientVectorX = pGradientVectorX;
		this.mGradientVectorY = pGradientVectorY;

		this.onUpdateColor();
	}

	/**
	 * @param pGradientAngle in degrees. <code>0</code> is a 'left-to-right', <code>90</code> is 'top-to-bottom', ...
	 */
	public void setGradientAngle(final float pGradientAngle) {
		final float angleInRad = MathUtils.degToRad(pGradientAngle);

		this.setGradientVector((float) Math.cos(angleInRad), (float) Math.sin(angleInRad));
	}

	public void setGradientColor(final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue) {
		this.mColor.set(pFromRed, pFromGreen, pFromBlue);
		this.mToColor.set(pToRed, pToGreen, pToBlue);

		this.onUpdateColor();
	}

	public void setGradientColor(final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final float pFromAlpha, final float pToAlpha) {
		this.mColor.set(pFromRed, pFromGreen, pFromBlue, pFromAlpha);
		this.mToColor.set(pToRed, pToGreen, pToBlue, pToAlpha);

		this.onUpdateColor();
	}

	public void setGradientColor(final Color pFromColor, final Color pToColor) {
		this.mColor.set(pFromColor);
		this.mToColor.set(pToColor);

		this.onUpdateColor();
	}

	public void setGradient(final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final float pGradientVectorX, final float pGradientVectorY) {
		this.mGradientVectorX = pGradientVectorX;
		this.mGradientVectorY = pGradientVectorY;

		this.mColor.set(pFromRed, pFromGreen, pFromBlue);
		this.mToColor.set(pToRed, pToGreen, pToBlue);

		this.onUpdateColor();
	}

	public void setGradient(final float pFromRed, final float pToRed, final float pFromGreen, final float pToGreen, final float pFromBlue, final float pToBlue, final float pFromAlpha, final float pToAlpha, final float pGradientVectorX, final float pGradientVectorY) {
		this.mGradientVectorX = pGradientVectorX;
		this.mGradientVectorY = pGradientVectorY;

		this.mColor.set(pFromRed, pFromGreen, pFromBlue, pFromAlpha);
		this.mToColor.set(pToRed, pToGreen, pToBlue, pToAlpha);

		this.onUpdateColor();
	}

	public void setGradient(final Color pFromColor, final Color pToColor, final float pGradientVectorX, final float pGradientVectorY) {
		this.mGradientVectorX = pGradientVectorX;
		this.mGradientVectorY = pGradientVectorY;

		this.mColor.set(pFromColor);
		this.mToColor.set(pToColor);

		this.onUpdateColor();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public IGradientVertexBufferObject getVertexBufferObject() {
		return this.mGradientVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		this.mGradientVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		if (this.mGradientDitherEnabled) {
			final boolean wasDitherEnabled = pGLState.enableDither();

			this.mGradientVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, Gradient.VERTICES_PER_RECTANGLE);

			pGLState.setDitherEnabled(wasDitherEnabled);
		} else {
			this.mGradientVertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, Gradient.VERTICES_PER_RECTANGLE);
		}
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mGradientVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mGradientVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mGradientVertexBufferObject.onUpdateVertices(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}