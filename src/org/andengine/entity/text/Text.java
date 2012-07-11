package org.andengine.entity.text;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.entity.text.exception.OutOfCharactersException;
import org.andengine.entity.text.vbo.HighPerformanceTextVertexBufferObject;
import org.andengine.entity.text.vbo.ITextVertexBufferObject;
import org.andengine.opengl.font.FontUtils;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.shader.PositionColorTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.adt.DataConstants;
import org.andengine.util.adt.list.FloatArrayList;
import org.andengine.util.adt.list.IFloatList;

import android.opengl.GLES20;

/**
 * A {@link RectangularShape} that contains a certain {@link IFont Font},
 * character sequence and all other information needed to render a text to the
 * screen. <br>
 * TODO Try Degenerate Triangles?
 * 
 * (c) 2010 Nicolas Gramlich<br>
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
	public static final int LETTER_SIZE = Text.VERTEX_SIZE
			* Text.VERTICES_PER_LETTER;
	public static final int VERTEX_STRIDE = Text.VERTEX_SIZE
			* DataConstants.BYTES_PER_FLOAT;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(
			3)
			.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION,
					ShaderProgramConstants.ATTRIBUTE_POSITION, 2,
					GLES20.GL_FLOAT, false)
			.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION,
					ShaderProgramConstants.ATTRIBUTE_COLOR, 4,
					GLES20.GL_UNSIGNED_BYTE, true)
			.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION,
					ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2,
					GLES20.GL_FLOAT, false).build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IFont mFont;

	protected float mLineWidthMaximum;
	protected float mLineAlignmentWidth;

	protected TextOptions mTextOptions;
	protected final int mCharactersMaximum;
	protected int mCharactersToDraw;
	protected int mVertexCountToDraw;
	protected final int mVertexCount;

	protected final ITextVertexBufferObject mTextVertexBufferObject;

	protected CharSequence mText;
	protected ArrayList<CharSequence> mLines = new ArrayList<CharSequence>(1);
	protected IFloatList mLineWidths = new FloatArrayList(1);

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in The
	 *            {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String}) A {@link CharSequence} that
	 *            contains the text to be displayed (most likely a
	 *            {@link String})
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.) (Probably the same for all shapes in the scene.)
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pFont, pText, pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in The
	 *            {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String}) A {@link CharSequence} that
	 *            contains the text to be displayed (most likely a
	 *            {@link String})
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 * @param pShaderProgram
	 *            The {@link ShaderProgram} that specifies how the text should
	 *            be shaded.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pVertexBufferObjectManager, DrawType.STATIC,
				pShaderProgram);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in The
	 *            {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String}) A {@link CharSequence} that
	 *            contains the text to be displayed (most likely a
	 *            {@link String})
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 * @param pDrawType
	 *            The {@link DrawType} that specifies how the text shoulod be
	 *            drawn. Typically {@link DrawType#STATIC}.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType) {
		this(pX, pY, pFont, pText, new TextOptions(),
				pVertexBufferObjectManager, pDrawType);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for all shapes in
	 *            the scene.)
	 * @param pDrawType
	 *            The {@link DrawType} that specifies how the text shoulod be
	 *            drawn. Typically {@link DrawType#STATIC}.
	 * @param pShaderProgram
	 *            The {@link ShaderProgram} that specifies how the text should
	 *            be shaded.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, new TextOptions(),
				pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pTextOptions
	 *            The {@link TextOptions} that specify how the {@link Text}
	 *            should be rendered
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final TextOptions pTextOptions,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pFont, pText, pTextOptions, pVertexBufferObjectManager,
				DrawType.STATIC);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pTextOptions
	 *            The {@link TextOptions} that specify how the {@link Text}
	 *            should be rendered
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 * @param pShaderProgram
	 *            The {@link ShaderProgram} that specifies how the text should
	 *            be shaded.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final TextOptions pTextOptions,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pTextOptions, pVertexBufferObjectManager,
				DrawType.STATIC, pShaderProgram);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pTextOptions
	 *            The {@link TextOptions} that specify how the {@link Text}
	 *            should be rendered
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 * @param pDrawType
	 *            The {@link DrawType} that specifies how the text shoulod be
	 *            drawn. Typically {@link DrawType#STATIC}.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final TextOptions pTextOptions,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pText.length(), pTextOptions,
				pVertexBufferObjectManager, pDrawType);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pTextOptions
	 *            The {@link TextOptions} that specify how the {@link Text}
	 *            should be rendered
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 * @param pDrawType
	 *            The {@link DrawType} that specifies how the text shoulod be
	 *            drawn. Typically {@link DrawType#STATIC}.
	 * @param pShaderProgram
	 *            The {@link ShaderProgram} that specifies how the text should
	 *            be shaded.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final TextOptions pTextOptions,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pText.length(), pTextOptions,
				pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pCharactersMaximum
	 *            The maximum number of character this Text will display
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final int pCharactersMaximum,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pFont, pText, pCharactersMaximum,
				pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pCharactersMaximum
	 *            The maximum number of character this Text will display
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 * @param pShaderProgram
	 *            The {@link ShaderProgram} that specifies how the text should
	 *            be shaded.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final int pCharactersMaximum,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pCharactersMaximum,
				pVertexBufferObjectManager, DrawType.STATIC, pShaderProgram);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pCharactersMaximum
	 *            The maximum number of character this Text will display
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 * @param pDrawType
	 *            The {@link DrawType} that specifies how the text shoulod be
	 *            drawn. Typically {@link DrawType#STATIC}.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final int pCharactersMaximum,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pCharactersMaximum, new TextOptions(),
				pVertexBufferObjectManager, pDrawType);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pCharactersMaximum
	 *            The maximum number of character this Text will display
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 * @param pDrawType
	 *            The {@link DrawType} that specifies how the text shoulod be
	 *            drawn. Typically {@link DrawType#STATIC}.
	 * @param pShaderProgram
	 *            The {@link ShaderProgram} that specifies how the text should
	 *            be shaded.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final int pCharactersMaximum,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pCharactersMaximum, new TextOptions(),
				pVertexBufferObjectManager, pDrawType, pShaderProgram);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pCharactersMaximum
	 *            The maximum number of character this Text will display
	 * @param pTextOptions
	 *            The {@link TextOptions} that specify how the {@link Text}
	 *            should be rendered
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final int pCharactersMaximum,
			final TextOptions pTextOptions,
			final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions,
				pVertexBufferObjectManager, DrawType.STATIC);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pCharactersMaximum
	 *            The maximum number of character this Text will display
	 * @param pTextOptions
	 *            The {@link TextOptions} that specify how the {@link Text}
	 *            should be rendered
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 * @param pDrawType
	 *            The {@link DrawType} that specifies how the text shoulod be
	 *            drawn. Typically {@link DrawType#STATIC}.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final int pCharactersMaximum,
			final TextOptions pTextOptions,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions,
				new HighPerformanceTextVertexBufferObject(
						pVertexBufferObjectManager, Text.LETTER_SIZE
								* pCharactersMaximum, pDrawType, true,
						Text.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT));
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pCharactersMaximum
	 *            The maximum number of character this Text will display
	 * @param pTextOptions
	 *            The {@link TextOptions} that specify how the {@link Text}
	 *            should be rendered
	 * @param pVertexBufferObjectManager
	 *            The {@link VertexBufferObjectManager} that handles the OpenGL
	 *            Vertex Buffer Objects. (Probably the same for every shape in
	 *            the scene.)
	 * @param pDrawType
	 *            The {@link DrawType} that specifies how the text shoulod be
	 *            drawn. Typically {@link DrawType#STATIC}.
	 * @param pShaderProgram
	 *            The {@link ShaderProgram} that specifies how the text should
	 *            be shaded.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final int pCharactersMaximum,
			final TextOptions pTextOptions,
			final VertexBufferObjectManager pVertexBufferObjectManager,
			final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		this(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions,
				new HighPerformanceTextVertexBufferObject(
						pVertexBufferObjectManager, Text.LETTER_SIZE
								* pCharactersMaximum, pDrawType, true,
						Text.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT),
				pShaderProgram);
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pCharactersMaximum
	 *            The maximum number of character this Text will display
	 * @param pTextOptions
	 *            The {@link TextOptions} that specify how the {@link Text}
	 *            should be rendered
	 * @param pTextVertexBufferObject
	 *            The {@link ITextVertexBufferObject} that serves as the OpenGL
	 *            Vertex Buffer Object for this text
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final int pCharactersMaximum,
			final TextOptions pTextOptions,
			final ITextVertexBufferObject pTextVertexBufferObject) {
		this(pX, pY, pFont, pText, pCharactersMaximum, pTextOptions,
				pTextVertexBufferObject,
				PositionColorTextureCoordinatesShaderProgram.getInstance());
	}

	/**
	 * @param pX
	 *            The upper left x coordinate
	 * @param pY
	 *            The upper left y coordinate
	 * @param pFont
	 *            The {@link IFont} the text should be displayed in
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @param pCharactersMaximum
	 *            The maximum number of character this Text will display
	 * @param pTextOptions
	 *            The {@link TextOptions} that specify how the {@link Text}
	 *            should be rendered
	 * @param pTextVertexBufferObject
	 *            The {@link ITextVertexBufferObject} that serves as the OpenGL
	 *            Vertex Buffer Object for this text
	 * @param pShaderProgram
	 *            The {@link ShaderProgram} that specifies how the text should
	 *            be shaded.
	 */
	public Text(final float pX, final float pY, final IFont pFont,
			final CharSequence pText, final int pCharactersMaximum,
			final TextOptions pTextOptions,
			final ITextVertexBufferObject pTextVertexBufferObject,
			final ShaderProgram pShaderProgram) {
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

	/**
	 * @return The {@link IFont} associated with this {@link Text}
	 */
	public IFont getFont() {
		return this.mFont;
	}

	/**
	 * @return The maximum number of characters that this {@link Text} shows
	 */
	public int getCharactersMaximum() {
		return this.mCharactersMaximum;
	}

	/**
	 * @return the {@link CharSequence} that this {@link Text} shows
	 */
	public CharSequence getText() {
		return this.mText;
	}

	/**
	 * @param pText
	 *            A {@link CharSequence} that contains the text to be displayed
	 *            (most likely a {@link String})
	 * @throws OutOfCharactersException
	 *             leaves this {@link Text} object in an undefined state, until
	 *             {@link Text#setText(CharSequence)} is called again and no
	 *             {@link OutOfCharactersException} is thrown.
	 */
	public void setText(final CharSequence pText)
			throws OutOfCharactersException {
		this.mText = pText;
		final IFont font = this.mFont;

		this.mLines.clear();
		this.mLineWidths.clear();

		if (this.mTextOptions.mAutoWrap == AutoWrap.NONE) {
			this.mLines = FontUtils.splitLines(this.mText, this.mLines); // TODO
																			// Add
																			// whitespace-trimming.
		} else {
			this.mLines = FontUtils.splitLines(this.mFont, this.mText,
					this.mLines, this.mTextOptions.mAutoWrap,
					this.mTextOptions.mAutoWrapWidth);
		}

		final int lineCount = this.mLines.size();
		float maximumLineWidth = 0;
		for (int i = 0; i < lineCount; i++) {
			final float lineWidth = FontUtils.measureText(font,
					this.mLines.get(i));
			maximumLineWidth = Math.max(maximumLineWidth, lineWidth);

			this.mLineWidths.add(lineWidth);
		}
		this.mLineWidthMaximum = maximumLineWidth;

		if (this.mTextOptions.mAutoWrap == AutoWrap.NONE) {
			this.mLineAlignmentWidth = this.mLineWidthMaximum;
		} else {
			this.mLineAlignmentWidth = this.mTextOptions.mAutoWrapWidth;
		}

		super.mWidth = this.mLineAlignmentWidth;
		super.mHeight = lineCount * font.getLineHeight() + (lineCount - 1)
				* this.mTextOptions.mLeading;

		this.mRotationCenterX = super.mWidth * 0.5f;
		this.mRotationCenterY = super.mHeight * 0.5f;

		this.mScaleCenterX = this.mRotationCenterX;
		this.mScaleCenterY = this.mRotationCenterY;

		this.onUpdateVertices();
	}

	/**
	 * @return An {@link ArrayList} of {@link CharSequence}s that make up the
	 *         individual lines of the displayed text
	 */
	public ArrayList<CharSequence> getLines() {
		return this.mLines;
	}

	/**
	 * @return An {@link IFloatList} that contains the widths of the individual
	 *         lines
	 */
	public IFloatList getLineWidths() {
		return this.mLineWidths;
	}

	/**
	 * @return The alignment width of the text
	 */
	public float getLineAlignmentWidth() {
		return this.mLineAlignmentWidth;
	}

	/**
	 * @return The maximum line width of the text
	 */
	public float getLineWidthMaximum() {
		return this.mLineWidthMaximum;
	}

	/**
	 * @return The leading (difference in height of successive lines) of the
	 *         text
	 */
	public float getLeading() {
		return this.mTextOptions.mLeading;
	}

	/**
	 * @param pLeading
	 *            The leading (difference in height of successive lines) of the
	 *            text
	 */
	public void setLeading(final float pLeading) {
		this.mTextOptions.mLeading = pLeading;

		this.invalidateText();
	}

	/**
	 * @return The {@link HorizontalAlign} that specifies how the text should
	 *         align horizontally
	 */
	public HorizontalAlign getHorizontalAlign() {
		return this.mTextOptions.mHorizontalAlign;
	}

	/**
	 * @param pHorizontalAlign
	 *            The {@link HorizontalAlign} that specifies how the text should
	 *            align horizontally
	 */
	public void setHorizontalAlign(final HorizontalAlign pHorizontalAlign) {
		this.mTextOptions.mHorizontalAlign = pHorizontalAlign;

		this.invalidateText();
	}

	/**
	 * @return The {@link AutoWrap} that specifies how the text should be
	 *         wrapped in the bounding box
	 */
	public AutoWrap getAutoWrap() {
		return this.mTextOptions.mAutoWrap;
	}

	/**
	 * @param pAutoWrap
	 *            The {@link AutoWrap} that specifies how the text should be
	 *            wrapped in the bounding box
	 */
	public void setAutoWrap(final AutoWrap pAutoWrap) {
		this.mTextOptions.mAutoWrap = pAutoWrap;

		this.invalidateText();
	}

	public float getAutoWrapWidth() {
		return this.mTextOptions.mAutoWrapWidth;
	}

	public void setAutoWrapWidth(final float pAutoWrapWidth) {
		this.mTextOptions.mAutoWrapWidth = pAutoWrapWidth;

		this.invalidateText();
	}

	/**
	 * @return The {@link TextOptions} associated with this {@link Text}
	 */
	public TextOptions getTextOptions() {
		return this.mTextOptions;
	}

	/**
	 * @param pTextOptions
	 *            Associate new {@link TextOptions} with this {@link Text}
	 */
	public void setTextOptions(final TextOptions pTextOptions) {
		this.mTextOptions = pTextOptions;
	}

	/**
	 * @param pCharactersToDraw
	 *            The number of character to draw, cannot exceed the maximum
	 *            amount of number the {@link Text} is allowed to draw.
	 * @see Text#setText(CharSequence)
	 */
	public void setCharactersToDraw(final int pCharactersToDraw) {
		if (pCharactersToDraw > this.mCharactersMaximum) {
			throw new OutOfCharactersException("Characters: maximum: '"
					+ this.mCharactersMaximum + "' required: '"
					+ pCharactersToDraw + "'.");
		}
		this.mCharactersToDraw = pCharactersToDraw;
		this.mVertexCountToDraw = pCharactersToDraw * Text.VERTICES_PER_LETTER;
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
		this.mTextVertexBufferObject.draw(GLES20.GL_TRIANGLES,
				this.mVertexCountToDraw);
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
}