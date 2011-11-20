package org.anddev.andengine.entity.text;

import java.nio.FloatBuffer;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.opengl.font.IFont;
import org.anddev.andengine.opengl.font.Letter;
import org.anddev.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.anddev.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.anddev.andengine.opengl.vbo.IVertexBufferObject;
import org.anddev.andengine.opengl.vbo.LowMemoryVertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
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

	protected static final float LEADING_DEFAULT = 0;

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

	protected final IFont mFont;

	protected float mMaximumLineWidth;

	protected final float mLeading;
	protected final int mCharactersMaximum;
	protected final int mVertexCount;
	protected final HorizontalAlign mHorizontalAlign;

	protected final ITextVertexBufferObject mTextVertexBufferObject;

	protected String mText;
	protected String[] mLines;
	protected float[] mWidths;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Text(final float pX, final float pY, final IFont pFont, final String pText) {
		this(pX, pY, pFont, pText, Text.LEADING_DEFAULT);
	}
	
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, Text.LEADING_DEFAULT, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final float pLeading) {
		this(pX, pY, pFont, pText, pLeading, DrawType.STATIC);
	}
	
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final float pLeading, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pLeading, DrawType.STATIC, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, Text.LEADING_DEFAULT, pDrawType);
	}
	
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, Text.LEADING_DEFAULT, pDrawType, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final float pLeading, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, HorizontalAlign.LEFT, pLeading, pDrawType);
	}
	
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final float pLeading, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, HorizontalAlign.LEFT, pLeading, pDrawType, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign) {
		this(pX, pY, pFont, pText, pHorizontalAlign, Text.LEADING_DEFAULT);
	}
	
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pHorizontalAlign, Text.LEADING_DEFAULT, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pHorizontalAlign, Text.LEADING_DEFAULT, pDrawType);
	}
	
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pHorizontalAlign, Text.LEADING_DEFAULT, pDrawType, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading) {
		this(pX, pY, pFont, pText, pHorizontalAlign, pLeading, DrawType.STATIC);
	}
	
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pHorizontalAlign, pLeading, DrawType.STATIC, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pHorizontalAlign, pLeading, pDrawType, pText.length() - StringUtils.countOccurrences(pText, '\n'));
	}
	
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pHorizontalAlign, pLeading, pDrawType, pText.length() - StringUtils.countOccurrences(pText, '\n'), pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading, final DrawType pDrawType, final int pCharactersMaximum) {
		this(pX, pY, pFont, pText, pHorizontalAlign, pLeading, pCharactersMaximum, new HighPerformanceTextVertexBufferObject(Text.LETTER_SIZE * pCharactersMaximum, pDrawType, true, Text.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}
	
	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading, final DrawType pDrawType, final int pCharactersMaximum, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pHorizontalAlign, pLeading, pCharactersMaximum, new HighPerformanceTextVertexBufferObject(Text.LETTER_SIZE * pCharactersMaximum, pDrawType, true, Text.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading, final int pCharactersMaximum, final ITextVertexBufferObject pTextVertexBufferObject) {
		this(pX, pY, pFont, pText, pHorizontalAlign, pLeading, pCharactersMaximum, pTextVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pLeading, final int pCharactersMaximum, final ITextVertexBufferObject pTextVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, 0, 0, pShaderProgram);

		this.mCharactersMaximum = pCharactersMaximum;
		this.mVertexCount = Text.VERTICES_PER_LETTER * this.mCharactersMaximum;
		this.mFont = pFont;
		this.mHorizontalAlign = pHorizontalAlign;
		this.mLeading = pLeading;
		this.mTextVertexBufferObject = pTextVertexBufferObject;

		this.onUpdateColor();
		this.updateText(pText);

		this.setBlendingEnabled(true);
		this.initBlendFunction(this.mFont.getTexture());
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

	public float[] getWidths() {
		return this.mWidths;
	}

	public String[] getLines() {
		return this.mLines;
	}

	public float getMaximumLineWidth() {
		return this.mMaximumLineWidth;
	}

	public float getLeading() {
		return this.mLeading;
	}

	public IFont getFont() {
		return this.mFont;
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
	public ITextVertexBufferObject getVertexBufferObject() {
		return this.mTextVertexBufferObject;
	}

	@Override
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		this.mFont.getTexture().bind();

		this.mTextVertexBufferObject.bind(this.mShaderProgram);
	}

	@Override
	protected void draw(final Camera pCamera) {
		this.mTextVertexBufferObject.draw(GLES20.GL_TRIANGLES, this.mVertexCount);
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		this.mTextVertexBufferObject.unbind(this.mShaderProgram);

		super.postDraw(pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.mTextVertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.mTextVertexBufferObject.onUpdateVertices(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ITextVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onUpdateColor(final Text pText);
		public void onUpdateVertices(final Text pText);
	}

	public static class HighPerformanceTextVertexBufferObject extends HighPerformanceVertexBufferObject implements ITextVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public HighPerformanceTextVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final Text pText) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pText.getColor().getPacked();

			int bufferDataOffset = 0;
			final int charactersMaximum = pText.getCharactersMaximum();
			for(int i = 0; i < charactersMaximum; i++) {
				bufferData[bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;
				bufferData[bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;
				bufferData[bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;
				bufferData[bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;
				bufferData[bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;
				bufferData[bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.COLOR_INDEX] = packedColor;

				bufferDataOffset += Text.LETTER_SIZE;
			}

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final Text pText) {
			final float[] bufferData = this.mBufferData;

			// TODO Optimize with field access?
			final IFont font = pText.getFont();
			final String[] lines = pText.getLines();
			final float lineHeight = font.getLineHeight();
			final float[] widths = pText.getWidths();

			int bufferDataOffset = 0;

			final int lineCount = lines.length;
			for (int row = 0; row < lineCount; row++) {
				final String line = lines[row];

				float xBase;
				switch(pText.getHorizontalAlign()) {
					case RIGHT:
						xBase = pText.getMaximumLineWidth() - widths[row];
						break;
					case CENTER:
						xBase = (pText.getMaximumLineWidth() - widths[row]) * 0.5f;
						break;
					case LEFT:
					default:
						xBase = 0;
				}

				final float yBase = row * (lineHeight + pText.getLeading());

				final int lineLength = line.length();
				Letter previousLetter = null;
				for(int i = 0; i < lineLength; i++) {
					final Letter letter = font.getLetter(line.charAt(i));
					if(previousLetter != null) {
						xBase += previousLetter.getKerning(letter.mCharacter);
					}

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

			this.setDirtyOnHardware();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class LowMemoryTextVertexBufferObject extends LowMemoryVertexBufferObject implements ITextVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public LowMemoryTextVertexBufferObject(final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final Text pText) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			final float packedColor = pText.getColor().getPacked();

			int bufferDataOffset = 0;
			final int charactersMaximum = pText.getCharactersMaximum();
			for(int i = 0; i < charactersMaximum; i++) {
				bufferData.put(bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.COLOR_INDEX, packedColor);
				bufferData.put(bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.COLOR_INDEX, packedColor);
				bufferData.put(bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.COLOR_INDEX, packedColor);
				bufferData.put(bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.COLOR_INDEX, packedColor);
				bufferData.put(bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.COLOR_INDEX, packedColor);
				bufferData.put(bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.COLOR_INDEX, packedColor);

				bufferDataOffset += Text.LETTER_SIZE;
			} 

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final Text pText) {
			final FloatBuffer bufferData = this.mFloatBuffer;

			// TODO Optimize with field access?
			final IFont font = pText.getFont();
			final String[] lines = pText.getLines();
			final float lineHeight = font.getLineHeight();
			final float[] widths = pText.getWidths();

			int bufferDataOffset = 0;

			final int lineCount = lines.length;
			for (int row = 0; row < lineCount; row++) {
				final String line = lines[row];

				float xBase;
				switch(pText.getHorizontalAlign()) {
					case RIGHT:
						xBase = pText.getMaximumLineWidth() - widths[row];
						break;
					case CENTER:
						xBase = (pText.getMaximumLineWidth() - widths[row]) * 0.5f;
						break;
					case LEFT:
					default:
						xBase = 0;
				}

				final float yBase = row * (lineHeight + pText.getLeading());

				final int lineLength = line.length();
				Letter previousLetter = null;
				for(int i = 0; i < lineLength; i++) {
					final Letter letter = font.getLetter(line.charAt(i));
					if(previousLetter != null) {
						xBase += previousLetter.getKerning(letter.mCharacter);
					}

					final float x = xBase + letter.mOffsetX;
					final float y = yBase + letter.mOffsetY;

					final float y2 = y + letter.mHeight;
					final float x2 = x + letter.mWidth;

					final float u = letter.mU;
					final float v = letter.mV;
					final float u2 = letter.mU2;
					final float v2 = letter.mV2;

					bufferData.put(bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X, x);
					bufferData.put(bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y, y);
					bufferData.put(bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U, u);
					bufferData.put(bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V, v);

					bufferData.put(bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X, x);
					bufferData.put(bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y, y2);
					bufferData.put(bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U, u);
					bufferData.put(bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V, v2);

					bufferData.put(bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X, x2);
					bufferData.put(bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y, y2);
					bufferData.put(bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U, u2);
					bufferData.put(bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V, v2);

					bufferData.put(bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X, x2);
					bufferData.put(bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y, y2);
					bufferData.put(bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U, u2);
					bufferData.put(bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V, v2);

					bufferData.put(bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X, x2);
					bufferData.put(bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y, y);
					bufferData.put(bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U, u2);
					bufferData.put(bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V, v);

					bufferData.put(bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X, x);
					bufferData.put(bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y, y);
					bufferData.put(bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U, u);
					bufferData.put(bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V, v);

					xBase += letter.mAdvance;

					bufferDataOffset += Text.LETTER_SIZE;
				}
			}

			this.setDirtyOnHardware();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}