package org.anddev.andengine.entity.text;

import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.primitives.Shape;
import org.anddev.andengine.opengl.BaseBuffer;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.text.Font;
import org.anddev.andengine.opengl.text.Glyph;
import org.anddev.andengine.opengl.texture.buffer.TextureBuffer;
import org.anddev.andengine.opengl.vertex.VertexBuffer;
import org.anddev.andengine.util.StringUtils;

/**
 * @author Nicolas Gramlich
 * @since 10:54:59 - 03.04.2010
 */
public class Text extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int BLENDFUNCTION_SOURCE_DEFAULT = GL10.GL_ONE;
	public static final int BLENDFUNCTION_DESTINATION_DEFAULT = GL10.GL_ONE_MINUS_SRC_ALPHA;

	private static final int VERTICES_PER_CHARACTER = 6;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mVertexCount;

	private final TextureBuffer mTextureBuffer;

	protected int mSourceBlendFunction = BLENDFUNCTION_SOURCE_DEFAULT;
	protected int mDestinationBlendFunction = BLENDFUNCTION_DESTINATION_DEFAULT;

	private final String mText;
	private final String[] mLines;
	private final int[] mWidths;
	
	private final Font mFont;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Text(final float pX, final float pY, final Font pFont, final String pText) {
		super(pX, pY, 0, 0, new VertexBuffer(2 * VERTICES_PER_CHARACTER * BaseBuffer.BYTES_PER_FLOAT * (pText.length() - StringUtils.countOccurences(pText, "\n"))));
		
		this.mVertexCount = VERTICES_PER_CHARACTER * (pText.length() - StringUtils.countOccurences(pText, "\n"));
		
		this.mTextureBuffer = new TextureBuffer(2 * this.mVertexCount * BaseBuffer.BYTES_PER_FLOAT);
		this.mFont = pFont;
		this.mText = pText;

		/* Init Metrics. */
		{
			this.mLines = this.mText.split("\n");
			final String[] lines = this.mLines;
			final int lineCount = lines.length;
			
			this.mWidths = new int[lineCount];
			final int[] widths = this.mWidths;
			
			for (int i = lineCount - 1; i >= 0; i--) {
				widths[i] = pFont.getStringWidth(lines[i]);
			}
		}
		
		this.initTextureBuffer();
		this.updateVertexBuffer();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected void onInitDraw(final GL10 pGL) {
		super.onInitDraw(pGL);
		GLHelper.enableTextures(pGL);
		GLHelper.enableTexCoordArray(pGL);
		GLHelper.blendFunction(pGL, this.mSourceBlendFunction, this.mDestinationBlendFunction);
	}
	
	@Override
	protected void drawVertices(final GL10 pGL) {
		pGL.glDrawArrays(GL10.GL_TRIANGLES, 0, this.mVertexCount);
	}

	@Override
	protected void updateVertexBuffer() {
		final Font font = this.mFont;
		if(font != null) {
			final ByteBuffer vertexByteBuffer = this.getVertexBuffer().getByteBuffer();
			vertexByteBuffer.position(0);
	
			final int lineHeight = font.getLineHeight();
			final String[] lines = this.mLines;

			final float baseX = this.mX;
			final float baseY = this.mY;
	
			final int lineCount = lines.length;
			for (int i = 0; i < lineCount; i++) {
				final String line = lines[i];
				int x = 0;
				int y = 0;
	
				y += i * (font.getLineHeight() + font.getLineGap());
	
				final int lineLength = line.length();
				for (int j = 0; j < lineLength; j++) {
					final Glyph glyph = font.getGlyph(line.charAt(j));
	
					vertexByteBuffer.putFloat(baseX + x);
					vertexByteBuffer.putFloat(baseY + y);
					
					vertexByteBuffer.putFloat(baseX + x + glyph.mWidth);
					vertexByteBuffer.putFloat(baseY + y);
					
					vertexByteBuffer.putFloat(baseX + x + glyph.mWidth);
					vertexByteBuffer.putFloat(baseY + y + lineHeight);
					
					vertexByteBuffer.putFloat(baseX + x + glyph.mWidth);
					vertexByteBuffer.putFloat(baseY + y + lineHeight);
					
					vertexByteBuffer.putFloat(baseX + x);
					vertexByteBuffer.putFloat(baseY + y + lineHeight);

					vertexByteBuffer.putFloat(baseX + x);
					vertexByteBuffer.putFloat(baseY + y);
					
					x += glyph.mAdvance;
				}
			}
			vertexByteBuffer.position(0);
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

	protected void applyTexture(final GL10 pGL) {
		GLHelper.bindTexture(pGL, this.mFont.getTexture().getHardwareTextureID());
		GLHelper.texCoordPointer(pGL, this.mTextureBuffer.getByteBuffer(), GL10.GL_FLOAT);
	}

	private void initTextureBuffer() {
		final ByteBuffer textureByteBuffer = this.mTextureBuffer.getByteBuffer();
		textureByteBuffer.position(0);

		final Font font = this.mFont;
		final String[] lines = this.mLines;

		final int lineCount = lines.length;
		for (int i = 0; i < lineCount; i++) {
			final String line = lines[i];

			final int lineLength = line.length();
			for (int j = 0; j < lineLength; j++) {
				final Glyph glyph = font.getGlyph(line.charAt(j));

				final float glyphTextureX = glyph.mTextureX;
				final float glyphTextureY = glyph.mTextureY;
				final float glyphTextureWidth = glyph.mTextureWidth;
				final float glyphTextureHeight = glyph.mTextureHeight;

				textureByteBuffer.putFloat(glyphTextureX);
				textureByteBuffer.putFloat(glyphTextureY);
				
				textureByteBuffer.putFloat(glyphTextureX + glyphTextureWidth);
				textureByteBuffer.putFloat(glyphTextureY);
				
				textureByteBuffer.putFloat(glyphTextureX + glyphTextureWidth);
				textureByteBuffer.putFloat(glyphTextureY + glyphTextureHeight);
				
				textureByteBuffer.putFloat(glyphTextureX + glyphTextureWidth);
				textureByteBuffer.putFloat(glyphTextureY + glyphTextureHeight);
				
				textureByteBuffer.putFloat(glyphTextureX);
				textureByteBuffer.putFloat(glyphTextureY + glyphTextureHeight);

				textureByteBuffer.putFloat(glyphTextureX);
				textureByteBuffer.putFloat(glyphTextureY);
			}
		}
		textureByteBuffer.position(0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}