package org.andengine.entity.text.vbo;

import java.util.ArrayList;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.font.Letter;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.list.IFloatList;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:38:43 - 29.03.2012
 */
public class HighPerformanceTextVertexBufferObject extends HighPerformanceVertexBufferObject implements ITextVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceTextVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
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

		final float packedColor = pText.getColor().getABGRPackedFloat();

		int bufferDataOffset = 0;
		final int charactersMaximum = pText.getCharactersMaximum();
		for (int i = 0; i < charactersMaximum; i++) {
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

		final IFont font = pText.getFont();
		final HorizontalAlign horizontalAlign = pText.getHorizontalAlign();
		final ArrayList<CharSequence> lines = pText.getLines();
		final float lineHeight = font.getLineHeight();
		final IFloatList lineWidths = pText.getLineWidths();
		final float leading = pText.getLeading();
		final float ascent = font.getAscent();

		final float lineAlignmentWidth = pText.getLineAlignmentWidth();

		int charactersToDraw = 0;
		int bufferDataOffset = 0;

		final int lineCount = lines.size();
		for (int row = 0; row < lineCount; row++) {
			final CharSequence line = lines.get(row);

			float xBase;
			switch (horizontalAlign) {
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

			final float yBase = (lineCount - row) * lineHeight + ((lineCount - row - 1) * leading) + ascent;

			final int lineLength = line.length();
			Letter previousLetter = null;
			for (int i = 0; i < lineLength; i++) {
				final Letter letter = font.getLetter(line.charAt(i));
				if (previousLetter != null) {
					xBase += previousLetter.getKerning(letter.mCharacter);
				}

				if (!letter.isWhitespace()) {
					final float x = xBase + letter.mOffsetX;
					final float y = yBase - letter.mOffsetY;

					final float x2 = x + letter.mWidth;
					final float y2 = y - letter.mHeight;

					final float u = letter.mU;
					final float v = letter.mV;
					final float u2 = letter.mU2;
					final float v2 = letter.mV2;

					bufferData[bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x;
					bufferData[bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y2;
					bufferData[bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u;
					bufferData[bufferDataOffset + 0 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v2;

					bufferData[bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x;
					bufferData[bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y;
					bufferData[bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u;
					bufferData[bufferDataOffset + 1 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v;

					bufferData[bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x2;
					bufferData[bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y2;
					bufferData[bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u2;
					bufferData[bufferDataOffset + 2 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v2;

					bufferData[bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x2;
					bufferData[bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y2;
					bufferData[bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u2;
					bufferData[bufferDataOffset + 3 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v2;

					bufferData[bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x;
					bufferData[bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y;
					bufferData[bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u;
					bufferData[bufferDataOffset + 4 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v;

					bufferData[bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_X] = x2;
					bufferData[bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.VERTEX_INDEX_Y] = y;
					bufferData[bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_U] = u2;
					bufferData[bufferDataOffset + 5 * Text.VERTEX_SIZE + Text.TEXTURECOORDINATES_INDEX_V] = v;

					bufferDataOffset += Text.LETTER_SIZE;
					charactersToDraw++;
				}

				xBase += letter.mAdvance;

				previousLetter = letter;
			}
		}
		pText.setCharactersToDraw(charactersToDraw);

		this.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}