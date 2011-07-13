package org.anddev.andengine.opengl.vertex;

import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.Letter;
import org.anddev.andengine.opengl.util.FastFloatBuffer;
import org.anddev.andengine.util.HorizontalAlign;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	public TextVertexBuffer(final int pCharacterCount, final HorizontalAlign pHorizontalAlign, final int pDrawType, final boolean pManaged) {
		super(2 * VERTICES_PER_CHARACTER * pCharacterCount, pDrawType, pManaged);

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
		final int[] bufferData = this.mBufferData;
		int i = 0;

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
			final int lineYBits = Float.floatToRawIntBits(lineY);

			final int lineLength = line.length();
			for (int letterIndex = 0; letterIndex < lineLength; letterIndex++) {
				final Letter letter = font.getLetter(line.charAt(letterIndex));

				final int lineY2 = lineY + lineHeight;
				final int lineX2 = lineX + letter.mWidth;

				final int lineXBits = Float.floatToRawIntBits(lineX);
				final int lineX2Bits = Float.floatToRawIntBits(lineX2);
				final int lineY2Bits = Float.floatToRawIntBits(lineY2);

				bufferData[i++] = lineXBits;
				bufferData[i++] = lineYBits;

				bufferData[i++] = lineXBits;
				bufferData[i++] = lineY2Bits;

				bufferData[i++] = lineX2Bits;
				bufferData[i++] = lineY2Bits;

				bufferData[i++] = lineX2Bits;
				bufferData[i++] = lineY2Bits;

				bufferData[i++] = lineX2Bits;
				bufferData[i++] = lineYBits;

				bufferData[i++] = lineXBits;
				bufferData[i++] = lineYBits;

				lineX += letter.mAdvance;
			}
		}

		final FastFloatBuffer vertexFloatBuffer = this.mFloatBuffer;
		vertexFloatBuffer.position(0);
		vertexFloatBuffer.put(bufferData);
		vertexFloatBuffer.position(0);

		super.setHardwareBufferNeedsUpdate();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
