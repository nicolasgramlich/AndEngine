package org.anddev.andengine.entity.text;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.RectangularShape;
import org.anddev.andengine.opengl.Mesh;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.Letter;
import org.anddev.andengine.opengl.shader.ShaderProgram;
import org.anddev.andengine.opengl.shader.util.constants.ShaderPrograms;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttributes;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.VertexBufferObjectAttributesBuilder;
import org.anddev.andengine.util.HorizontalAlign;
import org.anddev.andengine.util.StringUtils;
import org.anddev.andengine.util.constants.Constants;
import org.anddev.andengine.util.data.DataConstants;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * TODO Try Degenerate Triangles?
 * 
 * @author Nicolas Gramlich
 * @since 10:54:59 - 03.04.2010
 */
public class Text extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

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
		.add(ShaderPrograms.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderPrograms.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.add(ShaderPrograms.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	private String mText;
	private String[] mLines;
	private int[] mWidths;

	private final Font mFont;

	private int mMaximumLineWidth;

	protected final int mCharactersMaximum;
	protected final int mVertexCount;
	private final HorizontalAlign mHorizontalAlign;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Text(final float pX, final float pY, final Font pFont, final String pText) {
		this(pX, pY, pFont, pText, HorizontalAlign.LEFT);
	}

	public Text(final float pX, final float pY, final Font pFont, final String pText, final HorizontalAlign pHorizontalAlign) {
		this(pX, pY, pFont, pText, pHorizontalAlign, pText.length() - StringUtils.countOccurrences(pText, '\n'));
	}

	protected Text(final float pX, final float pY, final Font pFont, final String pText, final HorizontalAlign pHorizontalAlign, final int pCharactersMaximum) {
		super(pX, pY, 0, 0, new Mesh(Text.LETTER_SIZE * pCharactersMaximum, GLES20.GL_STATIC_DRAW, true, Text.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT), ShaderPrograms.SHADERPROGRAM_POSITION_COLOR_TEXTURECOORDINATES);

		this.mCharactersMaximum = pCharactersMaximum;
		this.mVertexCount = Text.VERTICES_PER_LETTER * this.mCharactersMaximum;
		this.mFont = pFont;
		this.mHorizontalAlign = pHorizontalAlign;

		this.onUpdateColor();
		this.updateText(pText);

		this.setBlendingEnabled(true);
		this.initBlendFunction();
	}

	protected void updateText(final String pText) {
		this.mText = pText;
		final Font font = this.mFont;

		this.mLines = StringUtils.split(this.mText, '\n', this.mLines);
		final String[] lines = this.mLines;

		final int lineCount = lines.length;
		final boolean widthsReusable = this.mWidths != null && this.mWidths.length == lineCount;
		if(!widthsReusable) {
			this.mWidths = new int[lineCount];
		}
		final int[] widths = this.mWidths;

		int maximumLineWidth = 0;

		for (int i = lineCount - 1; i >= 0; i--) {
			widths[i] = font.getStringWidth(lines[i]);
			maximumLineWidth = Math.max(maximumLineWidth, widths[i]);
		}
		this.mMaximumLineWidth = maximumLineWidth;

		super.mWidth = this.mMaximumLineWidth;
		final float width = super.mWidth;
		super.mBaseWidth = width;

		super.mHeight = lineCount * font.getLineHeight() + (lineCount - 1) * font.getLineGap();
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

	//	public void setHorizontalAlign(final HorizontalAlign pHorizontalAlign) {
	//		this.mHorizontalAlign = pHorizontalAlign; // TODO
	//	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void preDraw(final Camera pCamera) {
		super.preDraw(pCamera);

		GLHelper.enableTextures();
		this.mFont.getTexture().bind();
	}

	@Override
	protected void draw(final Camera pCamera) {
		final ShaderProgram shaderProgram = (this.mShaderProgram == null) ? ShaderPrograms.SHADERPROGRAM_POSITION_COLOR_TEXTURECOORDINATES : this.mShaderProgram;
		this.mMesh.draw(shaderProgram, GLES20.GL_TRIANGLES, this.mVertexCount);
	}

	@Override
	protected void postDraw(final Camera pCamera) {
		GLHelper.disableTextures();
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
		if(this.mFont == null) {
			return;
		}

		final VertexBufferObject vertexBufferObject = this.mMesh.getVertexBufferObject();
		final float[] bufferData = vertexBufferObject.getBufferData();

		final Font font = this.mFont;
		final String[] lines = this.mLines;
		final int lineHeight = font.getLineHeight();
		final int[] widths = this.mWidths;

		int index = 0;

		final int lineCount = lines.length;
		for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
			final String line = lines[lineIndex];

			int lineX;
			switch(this.mHorizontalAlign) {
				case RIGHT:
					lineX = this.mMaximumLineWidth - widths[lineIndex];
					break;
				case CENTER:
					lineX = (this.mMaximumLineWidth - widths[lineIndex]) >> 1;
				break;
					case LEFT:
				default:
					lineX = 0;
			}

			final int lineY = lineIndex * (font.getLineHeight() + font.getLineGap());

			final int lineLength = line.length();
			for (int letterIndex = 0; letterIndex < lineLength; letterIndex++) {
				final Letter letter = font.getLetter(line.charAt(letterIndex));

				final int lineY2 = lineY + lineHeight;
				final int lineX2 = lineX + letter.mWidth;

				final float u = letter.mU;
				final float v = letter.mV;
				final float u2 = letter.mU2;
				final float v2 = letter.mV2;

				bufferData[index + 0 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = lineX;
				bufferData[index + 0 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = lineY;
				bufferData[index + 0 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 0 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 1 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = lineX;
				bufferData[index + 1 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = lineY2;
				bufferData[index + 1 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 1 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 2 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = lineX2;
				bufferData[index + 2 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = lineY2;
				bufferData[index + 2 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 2 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 3 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = lineX2;
				bufferData[index + 3 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = lineY2;
				bufferData[index + 3 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 3 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v2;

				bufferData[index + 4 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = lineX2;
				bufferData[index + 4 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = lineY;
				bufferData[index + 4 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u2;
				bufferData[index + 4 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v;

				bufferData[index + 5 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_X] = lineX;
				bufferData[index + 5 * Text.VERTEX_SIZE + Constants.VERTEX_INDEX_Y] = lineY;
				bufferData[index + 5 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u;
				bufferData[index + 5 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v;

				lineX += letter.mAdvance;

				index += Text.LETTER_SIZE;
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