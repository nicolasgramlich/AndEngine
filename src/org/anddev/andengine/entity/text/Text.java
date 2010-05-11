package org.anddev.andengine.entity.text;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.entity.HorizontalAlign;
import org.anddev.andengine.entity.primitives.RectangularShape;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.buffer.BaseBuffer;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.buffer.TextTextureBuffer;
import org.anddev.andengine.opengl.vertex.TextVertexBuffer;
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

	protected final int mVertexCount;

	private final TextTextureBuffer mTextTextureBuffer;

	private final String mText;
	private final String[] mLines;
	private final int[] mWidths;

	private final Font mFont;

	private final int mMaximumLineWidth;

	protected final int mCharacterCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Text(final float pX, final float pY, final Font pFont, final String pText, final HorizontalAlign pHorizontalAlign) {
		super(pX, pY, 0, 0, new TextVertexBuffer(pText, pHorizontalAlign, GL11.GL_STATIC_DRAW));

		this.mCharacterCount = pText.length() - StringUtils.countOccurences(pText, "\n");
		this.mVertexCount = TextVertexBuffer.VERTICES_PER_CHARACTER * this.mCharacterCount;

		this.mTextTextureBuffer = new TextTextureBuffer(2 * this.mVertexCount * BaseBuffer.BYTES_PER_FLOAT, GL11.GL_STATIC_DRAW);
		BufferObjectManager.loadBufferObject(this.mTextTextureBuffer); // TODO Unload irgendwann oder so...
		this.mFont = pFont;
		this.mText = pText;

		/* Init Metrics. */
		{
			this.mLines = this.mText.split("\n");
			final String[] lines = this.mLines;
			final int lineCount = lines.length;

			this.mWidths = new int[lineCount];
			final int[] widths = this.mWidths;

			int maximumLineWidth = 0;

			for (int i = lineCount - 1; i >= 0; i--) {
				widths[i] = pFont.getStringWidth(lines[i]);
				maximumLineWidth = Math.max(maximumLineWidth, widths[i]);
			}
			this.mMaximumLineWidth = maximumLineWidth;
			
			super.mWidth = this.mMaximumLineWidth;
			super.mBaseWidth = super.mWidth;
			
			super.mHeight = lineCount * this.mFont.getLineHeight() + (lineCount - 1) * this.mFont.getLineGap();
			super.mBaseHeight = super.mHeight;
		}

		this.mTextTextureBuffer.update(this.mFont, this.mLines);
		this.updateVertexBuffer();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getCharacterCount() {
		return this.mCharacterCount;
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