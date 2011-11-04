package org.anddev.andengine.entity.text;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.font.IFont;
import org.anddev.andengine.opengl.font.Letter;
import org.anddev.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.util.GLState;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttribute;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.VertexBufferObjectAttributesBuilder;
import org.anddev.andengine.util.HorizontalAlign;
import org.anddev.andengine.util.StringUtils;
import org.anddev.andengine.util.data.DataConstants;

import android.opengl.GLES20;

/**
 * TODO Try Degenerate Triangles?
 *
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:54:59 - 03.04.2010
 */
public class Text extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float LEADING_DEFAULT = 0;

	public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = Text.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = Text.VERTEX_INDEX_Y + 1;
	public static final int TEXTURECOORDINATES_INDEX_U = Text.COLOR_INDEX + 1;
	public static final int TEXTURECOORDINATES_INDEX_V = Text.TEXTURECOORDINATES_INDEX_U + 1;

	public static final int VERTEX_SIZE = 2 + 1 + 2;
	public static final int VERTICES_PER_LETTER = 6;
	public static final int LETTER_SIZE = Text.VERTEX_SIZE * Text.VERTICES_PER_LETTER;
	public static final int VERTEX_STRIDE = Text.VERTEX_SIZE * DataConstants.BYTES_PER_FLOAT;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(3)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	private String mText;
	private String[] mLines;
	private float[] mWidths;

	private final IFont mFont;

	private float mMaximumLineWidth;

	protected final float mLeading;
	protected final int mCharactersMaximum;
	protected final int mVertexCount;
	private final HorizontalAlign mHorizontalAlign;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link Mesh} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Text#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Text(final float pX, final float pY, final IFont pFont, final String pText) {
		this(pX, pY, pFont, pText, Text.LEADING_DEFAULT);
	}

	/**
	 * Uses a default {@link Mesh} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Text#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 * @param pLeading gap between lines.
	 */
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final float pLeading) {
		this(pX, pY, pFont, pText, pLeading, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Text#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, Text.LEADING_DEFAULT, pDrawType);
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Text#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final float pLeading, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, HorizontalAlign.LEFT, pLeading, pDrawType);
	}

	/**
	 * Uses a default {@link Mesh} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Text#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign) {
		this(pX, pY, pFont, pText, pHorizontalAlign, Text.LEADING_DEFAULT);
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Text#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pHorizontalAlign, Text.LEADING_DEFAULT, pDrawType);
	}

	/**
	 * Uses a default {@link Mesh} in {@link DrawType#STATIC} with the {@link VertexBufferObjectAttribute}s: {@link Text#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 * @param pLeading gap between lines.
	 */
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading) {
		this(pX, pY, pFont, pText, pHorizontalAlign, pLeading, DrawType.STATIC);
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Text#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 * @param pLeading gap between lines.
	 */
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pHorizontalAlign, pLeading, pDrawType, pText.length() - StringUtils.countOccurrences(pText, '\n'));
	}

	/**
	 * Uses a default {@link Mesh} with the {@link VertexBufferObjectAttribute}s: {@link Text#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 * @param pLeading gap between lines.
	 */
	protected Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading, final DrawType pDrawType, final int pCharactersMaximum) {
		super(pX, pY, 0, 0, new Mesh(Text.LETTER_SIZE * pCharactersMaximum, pDrawType, true, Text.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), PositionColorTextureCoordinatesShaderProgram.getInstance());

		this.mCharactersMaximum = pCharactersMaximum;
		this.mVertexCount = Text.VERTICES_PER_LETTER * this.mCharactersMaximum;
		this.mFont = pFont;
		this.mHorizontalAlign = pHorizontalAlign;
		this.mLeading = pLeading;

		this.onUpdateColor();
		this.updateText(pText);

		this.setBlendingEnabled(true);
		this.initBlendFunction();
	}

	protected void updateText(final String pText) {
		this.mText = pText;
		final IFont font = this.mFont;

		this.mLines = StringUtils.split(this.mText, '\n', this.mLines);
		final String[] lines = this.mLines;

		final int lineCount = lines.length;
		final boolean widthsReusable = this.mWidths != null && this.mWidths.length == lineCount;
		if(!widthsReusable) {
			this.mWidths = new float[lineCount];
		}
		final float[] widths = this.mWidths;

		float maximumLineWidth = 0;

		for (int i = lineCount - 1; i >= 0; i--) {
			widths[i] = font.getStringWidth(lines[i]);
			maximumLineWidth = Math.max(maximumLineWidth, widths[i]);
		}
		this.mMaximumLineWidth = maximumLineWidth;

		super.mWidth = this.mMaximumLineWidth;
		final float width = super.mWidth;
		super.mBaseWidth = width;

		super.mHeight = lineCount * font.getLineHeight() + (lineCount - 1) * this.mLeading;

		final float height = super.mHeight;
		super.mBaseHeight = height;

		this.mRotationCenterX = width * 0.5f;
		this.mRotationCenterY = height * 0.5f;

		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;

		this.onUpdateVertices();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getText() {
		return this.mText;
	}

	public int getCharactersMaximum() {
		return this.mCharactersMaximum;
	}

	public HorizontalAlign getHorizontalAlign() {
		return this.mHorizontalAlign;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		GLState.enableTextures();
		this.mFont.getTexture().bind();
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.mMesh.draw(this.mShaderProgram, GLES20.GL_TRIANGLES, this.mVertexCount);
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		GLState.disableTextures();

		super.postDraw(pCamera);
	}

	@Override
	protected void onUpdateColor() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final float packedColor = this.mColor.getPacked();

		int index = 0;
		for(int i = 0; i < this.mCharactersMaximum; i++) {
			bufferData[index + 0 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;
			bufferData[index + 1 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;
			bufferData[index + 2 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;
			bufferData[index + 3 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;
			bufferData[index + 4 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;
			bufferData[index + 5 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;

			index += Text.LETTER_SIZE;
		}

		vertexBufferObject.setDirtyOnHardware();
	}

	@Override
	protected void onUpdateVertices() {
		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final IFont font = this.mFont;
		final String[] lines = this.mLines;
		final float lineHeight = font.getLineHeight();
		final float[] widths = this.mWidths;

		int bufferDataOffset = 0;

		final int lineCount = lines.length;
		for (int row = 0; row < lineCount; row++) {
			final String line = lines[row];

			float xBase;
			switch(this.mHorizontalAlign) {
				case RIGHT:
					xBase = this.mMaximumLineWidth - widths[row];
					break;
				case CENTER:
					xBase = (this.mMaximumLineWidth - widths[row]) * 0.5f;
					break;
				case LEFT:
				default:
					xBase = 0;
			}

			final float yBase = row * (lineHeight + this.mLeading);

			final int lineLength = line.length();
			for(int i = 0; i < lineLength; i++) {
				final Letter letter = font.getLetter(line.charAt(i));

				final float x = xBase + letter.mOffsetX;
				final float y = yBase + letter.mOffsetY;

				final float y2 = y + letter.mHeight;
				final float x2 = x + letter.mWidth;

				final float u = letter.mU;
				final float v = letter.mV;
				final float u2 = letter.mU2;
				final float v2 = letter.mV2;

				bufferData[bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x;
				bufferData[bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y;
				bufferData[bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x;
				bufferData[bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y2;
				bufferData[bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x2;
				bufferData[bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y;
				bufferData[bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x;
				bufferData[bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y;
				bufferData[bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v;

				xBase += letter.mAdvance;

				bufferDataOffset += Text.LETTER_SIZE;
			}
		}

		vertexBufferObject.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initBlendFunction() {
		if(this.mFont.getTexture().getTextureOptions().mPreMultipyAlpha) {
			this.setBlendFunction(IShape.BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT, IShape.BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}