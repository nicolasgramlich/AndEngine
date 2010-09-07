package org.anddev.andengine.opengl.vertex;

import java.nio.FloatBuffer;

import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.Letter;
import org.anddev.andengine.util.HorizontalAlign;

/**
 * @author Nicolas Gramlich
 * @since 18:05:08 - 07.04.2010
 */
public class TextVertexBuffer extends VertexBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTICES_PER_CHARACTER = 6;

	// ===========================================================
	// Fields
	// ===========================================================

	private final HorizontalAlign mHorizontalAlign;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextVertexBuffer(final int pCharacterCount, final HorizontalAlign pHorizontalAlign, final int pDrawType) {
		super(2 * VERTICES_PER_CHARACTER * BYTES_PER_FLOAT * pCharacterCount, pDrawType);

		this.mHorizontalAlign = pHorizontalAlign;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized void update(final Font font, final int pMaximumLineWidth, final int[] pWidths, final String[] pLines) {
		final FloatBuffer vertexFloatBuffer = this.getFloatBuffer();
		vertexFloatBuffer.position(0);

		final int lineHeight = font.getLineHeight();

		final int lineCount = pLines.length;
		for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
			final String line = pLines[lineIndex];

			int lineX;
			switch(this.mHorizontalAlign) {
				case RIGHT:
					lineX = pMaximumLineWidth - pWidths[lineIndex];
					break;
				case CENTER:
					lineX = (pMaximumLineWidth - pWidths[lineIndex]) >> 1;
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

				vertexFloatBuffer.put(lineX);
				vertexFloatBuffer.put(lineY);

				vertexFloatBuffer.put(lineX);
				vertexFloatBuffer.put(lineY2);

				vertexFloatBuffer.put(lineX2);
				vertexFloatBuffer.put(lineY2);

				vertexFloatBuffer.put(lineX2);
				vertexFloatBuffer.put(lineY2);

				vertexFloatBuffer.put(lineX2);
				vertexFloatBuffer.put(lineY);

				vertexFloatBuffer.put(lineX);
				vertexFloatBuffer.put(lineY);

				lineX += letter.mAdvance;
			}
		}
		vertexFloatBuffer.position(0);

		super.update();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
