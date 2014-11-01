package org.andengine.entity.text.vbo;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.font.Letter;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.LowMemoryVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.adt.list.IFloatList;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:38:22 - 29.03.2012
 */
public class LowMemoryTextVertexBufferObject extends LowMemoryVertexBufferObject implements ITextVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public LowMemoryTextVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
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
		final FloatBuffer bufferData = this.mFloatBuffer;

		final float packedColor = pText.getColor().getABGRPackedFloat();

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
		final ArrayList<CharSequence> lines = pText.getLines();
		final float lineHeight = font.getLineHeight();
		final IFloatList lineWidths = pText.getLineWidths();

		final float lineAlignmentWidth = pText.getLineAlignmentWidth();

		int charactersToDraw = 0;
		int bufferDataOffset = 0;

		final int lineCount = lines.size();
		for (int i = 0; i < lineCount; i++) {
			final CharSequence line = lines.get(i);

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

				if(!letter.isWhitespace()) {
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