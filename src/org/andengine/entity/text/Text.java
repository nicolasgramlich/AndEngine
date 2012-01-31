package org.andengine.entity.text;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.font.Letter;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.util.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.LowMemoryVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.StringUtils;
import org.andengine.util.data.DataConstants;
import org.andengine.util.list.FloatArrayList;
import org.andengine.util.list.IFloatList;

import android.opengl.GLES20;

/**
 * TODO Use {@link CharSequence} instead of {@link String}.
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

	public static final float LEADING_DEFAULT = 0;

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

	protected float mLineWidthMaximum;
	protected float mLineAlignmentWidth;

	protected TextOptions mTextOptions;
	protected final int mCharactersMaximum;
	protected int mVertexCountToDraw;
	protected final int mVertexCount;

	protected final ITextVertexBufferObject mTextVertexBufferObject;

	protected String mText;
	protected ArrayList<String> mLines = new ArrayList<String>(1);
	protected IFloatList mLineWidths = new FloatArrayList(1);

	// ===========================================================
	// Constructors
	// ===========================================================

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pFont, pText, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, new TextOptions(), pVertexBufferObjectManager, pDrawType);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, new TextOptions(), pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pFont, pText, pTextOptions, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pTextOptions, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pText.length(), pTextOptions, pVertexBufferObjectManager, pDrawType);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pText.length(), pTextOptions, pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pFont, pText, pCharactersMaximum, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pCharactersMaximum, pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pCharactersMaximum, new TextOptions(), pVertexBufferObjectManager, pDrawType);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pCharactersMaximum, new TextOptions(), pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions, new HighPerformanceTextVertexBufferObject(pVertexBufferObjectManager, Text.LETTER_SIZE * pCharactersMaximum, pDrawType, true, Text.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions, new HighPerformanceTextVertexBufferObject(pVertexBufferObjectManager, Text.LETTER_SIZE * pCharactersMaximum, pDrawType, true, Text.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), pShaderProgram);
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum, final TextOptions pTextOptions, final ITextVertexBufferObject pTextVertexBufferObject) {
		this(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions, pTextVertexBufferObject, PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	public Text(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum, final TextOptions pTextOptions, final ITextVertexBufferObject pTextVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(pX, pY, 0, 0, pShaderProgram);

		this.mFont = pFont;
		this.mTextOptions = pTextOptions;
		this.mCharactersMaximum = pCharactersMaximum;
		this.mVertexCount = Text.VERTICES_PER_LETTER * this.mCharactersMaximum;
		this.mTextVertexBufferObject = pTextVertexBufferObject;

		this.onUpdateColor();
		this.setText(pText);

		this.setBlendingEnabled(true);
		this.initBlendFunction(this.mFont.getTexture());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IFont getFont() {
		return this.mFont;
	}

	public int getCharactersMaximum() {
		return this.mCharactersMaximum;
	}

	public String getText() {
		return this.mText;
	}

	public void setText(final String pText) {
		this.mText = pText;
		final IFont font = this.mFont;

		this.mLines.clear();
		this.mLineWidths.clear();

		if(this.mTextOptions.mAutoWordWrap) {
			this.mLines = FontUtils.splitLines(this.mFont, this.mText, this.mLines, this.mTextOptions.mAutoWordWrapWidth);
		} else {
			this.mLines = FontUtils.splitLines(this.mText, this.mLines); // TODO Add whitespace-trimming.
		}

		final int lineCount = this.mLines.size();
		float maximumLineWidth = 0;
		for (int i = 0; i < lineCount; i++) {
			final float lineWidth = FontUtils.measureText(font, this.mLines.get(i));
			maximumLineWidth = Math.max(maximumLineWidth, lineWidth);

			this.mLineWidths.add(lineWidth);
		}
		this.mLineWidthMaximum = maximumLineWidth;

		if(this.mTextOptions.mAutoWordWrap) {
			this.mLineAlignmentWidth = this.mTextOptions.mAutoWordWrapWidth;
		} else {
			this.mLineAlignmentWidth = this.mLineWidthMaximum;
		}

		super.mWidth = this.mLineAlignmentWidth;
		super.mBaseWidth = super.mWidth;

		super.mHeight = lineCount * font.getLineHeight() + (lineCount - 1) * this.mTextOptions.mLeading;

		super.mBaseHeight = super.mHeight;

		this.mRotationCenterX = super.mWidth * 0.5f;
		this.mRotationCenterY = super.mHeight * 0.5f;

		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;

		this.mVertexCountToDraw = StringUtils.countCharacters(this.mLines) * Text.VERTICES_PER_LETTER;
		this.onUpdateVertices();
	}

	public ArrayList<String> getLines() {
		return this.mLines;
	}

	public IFloatList getLineWidths() {
		return this.mLineWidths;
	}

	public float getLineAlignmentWidth() {
		return this.mLineAlignmentWidth;
	}

	public float getLineWidthMaximum() {
		return this.mLineWidthMaximum;
	}

	public float getLeading() {
		return this.mTextOptions.mLeading;
	}

	public void setLeading(final float pLeading) {
		this.mTextOptions.mLeading = pLeading;

		this.invalidateText();
	}

	public HorizontalAlign getHorizontalAlign() {
		return this.mTextOptions.mHorizontalAlign;
	}

	public void setHorizontalAlign(final HorizontalAlign pHorizontalAlign) {
		this.mTextOptions.mHorizontalAlign = pHorizontalAlign;

		this.invalidateText();
	}

	public boolean isAutoWordWrap() {
		return this.mTextOptions.mAutoWordWrap;
	}

	public void setAutoWordWrap(final boolean pAutoWordWrap) {
		this.mTextOptions.mAutoWordWrap = pAutoWordWrap;

		this.invalidateText();
	}

	public float getAutoWordWrapWidth() {
		return this.mTextOptions.mAutoWordWrapWidth;
	}

	public void setAutoWordWrapWidth(final float pAutoWordWrapWidth) {
		this.mTextOptions.mAutoWordWrapWidth = pAutoWordWrapWidth;

		this.invalidateText();
	}

	public TextOptions getTextOptions() {
		return this.mTextOptions;
	}

	public void setTextOptions(final TextOptions pTextOptions) {
		this.mTextOptions = pTextOptions;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ITextVertexBufferObject getVertexBufferObject() {
		return this.mTextVertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		this.mFont.getTexture().bind(pGLState);

		this.mTextVertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mTextVertexBufferObject.draw(GLES20.GL_TRIANGLES, this.mVertexCountToDraw);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.mTextVertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
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

	public void invalidateText() {
		this.setText(this.mText);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TextOptions {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		/* package */ boolean mAutoWordWrap;
		/* package */ float mAutoWordWrapWidth;
		/* package */ float mLeading;
		/* package */ HorizontalAlign mHorizontalAlign;

		// ===========================================================
		// Constructors
		// ===========================================================

		public TextOptions() {
			this(false, 0, Text.LEADING_DEFAULT, HorizontalAlign.LEFT);
		}

		public TextOptions(final HorizontalAlign pHorizontalAlign) {
			this(false, 0, Text.LEADING_DEFAULT, pHorizontalAlign);
		}

		public TextOptions(final boolean pAutoWordWrap, final float pAutoWordWrapWidth) {
			this(pAutoWordWrap, pAutoWordWrapWidth, Text.LEADING_DEFAULT, HorizontalAlign.LEFT);
		}

		public TextOptions(final boolean pAutoWordWrap, final float pAutoWordWrapWidth, final float pLeading, final HorizontalAlign pHorizontalAlign) {
			this.mAutoWordWrap = pAutoWordWrap;
			this.mAutoWordWrapWidth = pAutoWordWrapWidth;
			this.mLeading = pLeading;
			this.mHorizontalAlign = pHorizontalAlign;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public boolean isAutoWordWrap() {
			return this.mAutoWordWrap;
		}

		public void setAutoWordWrap(final boolean pAutoWordWrap) {
			this.mAutoWordWrap = pAutoWordWrap;
		}

		public float getAutoWordWrapWidth() {
			return this.mAutoWordWrapWidth;
		}

		public void setAutoWordWrapWidth(final float pAutoWordWrapWidth) {
			this.mAutoWordWrapWidth = pAutoWordWrapWidth;
		}

		public float getLeading() {
			return this.mLeading;
		}

		public void setLeading(final float pLeading) {
			this.mLeading = pLeading;
		}

		public HorizontalAlign getHorizontalAlign() {
			return this.mHorizontalAlign;
		}

		public void setHorizontalAlign(final HorizontalAlign pHorizontalAlign) {
			this.mHorizontalAlign = pHorizontalAlign;
		}

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

		public HighPerformanceTextVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
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
			final ArrayList<String> lines = pText.getLines();
			final float lineHeight = font.getLineHeight();
			final IFloatList lineWidths = pText.getLineWidths();

			final float lineAlignmentWidth = pText.getLineAlignmentWidth();

			int bufferDataOffset = 0;

			final int lineCount = lines.size();
			for (int row = 0; row < lineCount; row++) {
				final String line = lines.get(row);

				float xBase;
				switch(pText.getHorizontalAlign()) {
					case RIGHT:
						xBase = lineAlignmentWidth - lineWidths.get(row);
						break;
					case CENTER:
						xBase = (lineAlignmentWidth - lineWidths.get(row)) * 0.5f;
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

					previousLetter = letter;
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

		public LowMemoryTextVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pCapacity, pDrawType, pManaged, pVertexBufferObjectAttributes);
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
			final ArrayList<String> lines = pText.getLines();
			final float lineHeight = font.getLineHeight();
			final IFloatList lineWidths = pText.getLineWidths();

			final float lineAlignmentWidth = pText.getLineAlignmentWidth();

			int bufferDataOffset = 0;

			final int lineCount = lines.size();
			for (int i = 0; i < lineCount; i++) {
				final String line = lines.get(i);

				float xBase;

				switch(pText.getHorizontalAlign()) {
					case RIGHT:
						xBase = lineAlignmentWidth - lineWidths.get(i);
						break;
					case CENTER:
						xBase = (lineAlignmentWidth - lineWidths.get(i)) * 0.5f;
						break;
					case LEFT:
					default:
						xBase = 0;
				}

				final float yBase = i * (lineHeight + pText.getLeading());

				final int lineLength = line.length();
				Letter previousLetter = null;
				for(int j = 0; j < lineLength; j++) {
					final Letter letter = font.getLetter(line.charAt(j));
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

					previousLetter = letter;
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