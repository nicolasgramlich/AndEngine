package org.anddev.andengine.entity.text;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.primitives.RectangularShape;
import org.anddev.andengine.opengl.BaseBuffer;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.text.Font;
import org.anddev.andengine.opengl.text.Letter;
import org.anddev.andengine.opengl.texture.buffer.TextureBuffer;
import org.anddev.andengine.opengl.vertex.VertexBuffer;
import org.anddev.andengine.util.StringUtils;

/**
 * @author Nicolas Gramlich
 * @since 10:54:59 - 03.04.2010
 */
public class Text extends RectangularShape {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int VERTICES_PER_CHARACTER = 6;

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mVertexCount;

	private final TextureBuffer mTextureBuffer;

	private final String mText;
	private final String[] mLines;
	private final int[] mWidths;
	
	private final Font mFont;
	
	private final HorizontalAlign mHorizontalAlign;
	
	private final int mMaximumeLineWidth;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Text(final float pX, final float pY, final Font pFont, final String pText, final HorizontalAlign pHorizontalAlign) {
		super(pX, pY, 0, 0, new VertexBuffer(2 * VERTICES_PER_CHARACTER * BaseBuffer.BYTES_PER_FLOAT * (pText.length() - StringUtils.countOccurences(pText, "\n"))));
		
		this.mVertexCount = VERTICES_PER_CHARACTER * (pText.length() - StringUtils.countOccurences(pText, "\n"));
		
		this.mTextureBuffer = new TextureBuffer(2 * this.mVertexCount * BaseBuffer.BYTES_PER_FLOAT);
		this.mFont = pFont;
		this.mText = pText;
		this.mHorizontalAlign = pHorizontalAlign;

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
			this.mMaximumeLineWidth = maximumLineWidth;
			super.mWidth = this.mMaximumeLineWidth;
//			this.setHeight(lineCount)
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
	}
	
	@Override
	protected void drawVertices(final GL10 pGL) {
		pGL.glDrawArrays(GL10.GL_TRIANGLES, 0, this.mVertexCount);
	}

	@Override
	protected void updateVertexBuffer() {
		final Font font = this.mFont;
		if(font != null) {
			final FloatBuffer vertexFloatBuffer = this.getVertexBuffer().getFloatBuffer();
			vertexFloatBuffer.position(0);
	
			final int lineHeight = font.getLineHeight();
			final String[] lines = this.mLines;
			
			final int lineCount = lines.length;
			for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
				final String line = lines[lineIndex];

				int lineX;
				switch(this.mHorizontalAlign) {
					case RIGHT:
						lineX = this.mMaximumeLineWidth - this.mWidths[lineIndex];
						break;
					case CENTER:
						lineX = (this.mMaximumeLineWidth - this.mWidths[lineIndex]) / 2;
						break;
					case LEFT: 
					default:
						lineX = 0;
				}
				
				final int lineY = lineIndex * (font.getLineHeight() + font.getLineGap());
	
				final int lineLength = line.length();
				for (int letterIndex = 0; letterIndex < lineLength; letterIndex++) {
					final Letter letter = font.getLetter(line.charAt(letterIndex));
	
					vertexFloatBuffer.put(lineX);
					vertexFloatBuffer.put(lineY);
					
					vertexFloatBuffer.put(lineX + letter.mWidth);
					vertexFloatBuffer.put(lineY);
					
					vertexFloatBuffer.put(lineX + letter.mWidth);
					vertexFloatBuffer.put(lineY + lineHeight);
					
					vertexFloatBuffer.put(lineX + letter.mWidth);
					vertexFloatBuffer.put(lineY + lineHeight);
					
					vertexFloatBuffer.put(lineX);
					vertexFloatBuffer.put(lineY + lineHeight);

					vertexFloatBuffer.put(lineX);
					vertexFloatBuffer.put(lineY);
					
					lineX += letter.mAdvance;
				}
			}
			vertexFloatBuffer.position(0);
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
		GLHelper.bindTexture(pGL, this.mFont.getTexture().getHardwareTextureID());
		GLHelper.texCoordPointer(pGL, this.mTextureBuffer.getFloatBuffer());
	}

	private void initTextureBuffer() {
		final FloatBuffer textureFloatBuffer = this.mTextureBuffer.getFloatBuffer();
		textureFloatBuffer.position(0);

		final Font font = this.mFont;
		final String[] lines = this.mLines;

		final int lineCount = lines.length;
		for (int i = 0; i < lineCount; i++) {
			final String line = lines[i];

			final int lineLength = line.length();
			for (int j = 0; j < lineLength; j++) {
				final Letter letter = font.getLetter(line.charAt(j));

				final float letterTextureX = letter.mTextureX;
				final float letterTextureY = letter.mTextureY;
				final float letterTextureWidth = letter.mTextureWidth;
				final float letterTextureHeight = letter.mTextureHeight;

				textureFloatBuffer.put(letterTextureX);
				textureFloatBuffer.put(letterTextureY);
				
				textureFloatBuffer.put(letterTextureX + letterTextureWidth);
				textureFloatBuffer.put(letterTextureY);
				
				textureFloatBuffer.put(letterTextureX + letterTextureWidth);
				textureFloatBuffer.put(letterTextureY + letterTextureHeight);
				
				textureFloatBuffer.put(letterTextureX + letterTextureWidth);
				textureFloatBuffer.put(letterTextureY + letterTextureHeight);
				
				textureFloatBuffer.put(letterTextureX);
				textureFloatBuffer.put(letterTextureY + letterTextureHeight);

				textureFloatBuffer.put(letterTextureX);
				textureFloatBuffer.put(letterTextureY);
			}
		}
		textureFloatBuffer.position(0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public enum HorizontalAlign {
		LEFT, CENTER, RIGHT;
	}
}