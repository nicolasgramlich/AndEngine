package org.anddev.andengine.entity.text;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.entity.primitive.RectangularShape;
import org.anddev.andengine.opengl.buffer.BufferObject;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.buffer.TextTextureBuffer;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vertex.TextVertexBuffer;
import org.anddev.andengine.util.HorizontalAlign;
import org.anddev.andengine.util.StringUtils;

/**
 * @author Nicolas Gramlich
 * @since 10:54:59 - 03.04.2010
 */
public class Text extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final TextTextureBuffer mTextTextureBuffer;

	private String mText;
	private String[] mLines;
	private int[] mWidths;

	private final Font mFont;

	private int mMaximumLineWidth;

	protected final int mCharactersMaximum;
	protected final int mVertexCount;

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
		super(pX, pY, 0, 0, new TextVertexBuffer(pCharactersMaximum, pHorizontalAlign, GL11.GL_STATIC_DRAW));

		this.mCharactersMaximum = pCharactersMaximum;
		this.mVertexCount = TextVertexBuffer.VERTICES_PER_CHARACTER * this.mCharactersMaximum;

		this.mTextTextureBuffer = new TextTextureBuffer(2 * this.mVertexCount * BufferObject.BYTES_PER_FLOAT, GL11.GL_STATIC_DRAW);
		BufferObjectManager.loadBufferObject(this.mTextTextureBuffer); // TODO Unload somewhen somehow...
		this.mFont = pFont;

		this.updateText(pText);
	}

	protected void updateText(final String pText) {
		this.mText = pText;
		final Font font = this.mFont;

		this.mLines = StringUtils.split(this.mText, '\n', this.mLines);
		final String[] lines = this.mLines;

		final int lineCount = lines.length;
		final boolean widthsReusable = this.mWidths != null && this.mWidths.length == lineCount;
		if(widthsReusable == false) {
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
		super.mBaseWidth = super.mWidth;

		super.mHeight = lineCount * font.getLineHeight() + (lineCount - 1) * font.getLineGap();
		super.mBaseHeight = super.mHeight;

		this.mTextTextureBuffer.update(font, lines);
		this.updateVertexBuffer();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getCharacterCount() {
		return this.mCharactersMaximum;
	}

	@Override
	public TextVertexBuffer getVertexBuffer() {
		return (TextVertexBuffer)super.getVertexBuffer();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onInitDraw(final GL10 pGL) {
		super.onInitDraw(pGL);
		GLHelper.enableTextures(pGL);
		GLHelper.enableTexCoordArray(pGL);
	}

	@Override
	protected void drawVertices(final GL10 pGL) {
		pGL.glDrawArrays(GL10.GL_TRIANGLES, 0, this.mVertexCount);
	}

	@Override
	protected void onUpdateVertexBuffer() {
		final Font font = this.mFont;
		if(font != null) {
			this.getVertexBuffer().update(this.mFont, this.mMaximumLineWidth, this.mWidths, this.mLines);
		}
	}


	@Override
	protected void onPostTransformations(final GL10 pGL) {
		super.onPostTransformations(pGL);
		this.applyTexture(pGL);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void applyTexture(final GL10 pGL) {
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			final GL11 gl11 = (GL11)pGL;

			this.mTextTextureBuffer.selectOnHardware(gl11);

			GLHelper.bindTexture(pGL, this.mFont.getTexture().getHardwareTextureID());
			GLHelper.texCoordZeroPointer(gl11);
		} else {
			GLHelper.bindTexture(pGL, this.mFont.getTexture().getHardwareTextureID());
			GLHelper.texCoordPointer(pGL, this.mTextTextureBuffer.getFloatBuffer());
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}