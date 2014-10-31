package org.andengine.opengl.font;

import java.util.List;

import org.andengine.entity.text.AutoWrap;
import org.andengine.util.TextUtils;
import org.andengine.util.exception.MethodNotYetImplementedException;


/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:06:32 - 25.01.2012
 */
public class FontUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int UNSPECIFIED = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pFont
	 * @param pText
	 * @return the width of pText.
	 */
	public static float measureText(final IFont pFont, final CharSequence pText) {
		return FontUtils.measureText(pFont, pText, null);
	}

	/**
	 * @param pFont
	 * @param pText
	 * @param pStart the index of the first character to start measuring.
	 * @param pEnd <code>1</code> beyond the index of the last character to measure.
	 * @return the width of pText.
	 */
	public static float measureText(final IFont pFont, final CharSequence pText, final int pStart, final int pEnd) {
		return FontUtils.measureText(pFont, pText, pStart, pEnd, null);
	}

	/**
	 * @param pFont
	 * @param pText
	 * @param pWidths (optional) If not <code>null</code>, returns the actual width measured.
	 * @return the width of pText.
	 */
	public static float measureText(final IFont pFont, final CharSequence pText, final float[] pWidths) {
		return FontUtils.measureText(pFont, pText, 0, pText.length(), pWidths);
	}

	/**
	 * Does not respect linebreaks!
	 *
	 * @param pFont
	 * @param pText
	 * @param pStart the index of the first character to start measuring.
	 * @param pEnd <code>1</code> beyond the index of the last character to measure.
	 * @param pWidths (optional) If not <code>null</code>, returns the actual width after each character.
	 * @return the width of pText.
	 */
	public static float measureText(final IFont pFont, final CharSequence pText, final int pStart, final int pEnd, final float[] pWidths) {
		final int textLength = pEnd - pStart;
		/* Early exits. */
		if(pStart == pEnd) {
			return 0;
		} else if(textLength == 1) {
			return pFont.getLetter(pText.charAt(pStart)).mWidth;
		}

		Letter previousLetter = null;
		float width = 0;
		for(int pos = pStart, i = 0; pos < pEnd; pos++, i++) {
			final Letter letter = pFont.getLetter(pText.charAt(pos));
			if(previousLetter != null) {
				width += previousLetter.getKerning(letter.mCharacter);
			}
			previousLetter = letter;

			/* Check if this is the last character. */
			if(pos == (pEnd - 1)) {
				width += letter.mOffsetX + letter.mWidth;
			} else {
				width += letter.mAdvance;
			}

			if(pWidths != null) {
				pWidths[i] = width;
			}
		}
		return width;
	}

	/**
	 * Measure the text, stopping early if the measured width exceeds pMaximumWidth.
	 *
	 * @param pFont
	 * @param pText
	 * @param pMeasureDirection If {@link MeasureDirection#FORWARDS}, starts with the first character in the {@link CharSequence}. If {@link MeasureDirection#BACKWARDS} starts with the last character in the {@link CharSequence}.
	 * @param pWidthMaximum
	 * @param pMeasuredWidth (optional) If not <code>null</code>, returns the actual width measured. Must be an Array of size <code>1</code> or bigger.
	 * @return the number of chars that were measured.
	 */
	public static int breakText(final IFont pFont, final CharSequence pText, final MeasureDirection pMeasureDirection, final float pWidthMaximum, final float[] pMeasuredWidth) {
		throw new MethodNotYetImplementedException();
	}

	public static <L extends List<CharSequence>> L splitLines(final CharSequence pText, final L pResult) {
		return TextUtils.split(pText, '\n', pResult);
	}

	/**
	 * Does not respect linebreaks!
	 * 
	 * @param pFont
	 * @param pText
	 * @param pResult
	 * @param pAutoWrapWidth
	 * @return
	 */
	public static <L extends List<CharSequence>> L splitLines(final IFont pFont, final CharSequence pText, final L pResult, final AutoWrap pAutoWrap, final float pAutoWrapWidth) {
		/**
		 * TODO In order to respect already existing linebreaks, {@link FontUtils#split(CharSequence, List)} could be leveraged and than the following methods could be called for each line.
		 */
		switch(pAutoWrap) {
			case LETTERS:
				return FontUtils.splitLinesByLetters(pFont, pText, pResult, pAutoWrapWidth);
			case WORDS:
				return FontUtils.splitLinesByWords(pFont, pText, pResult, pAutoWrapWidth);
			case CJK:
				return FontUtils.splitLinesByCJK(pFont, pText, pResult, pAutoWrapWidth);
			case NONE:
			default:
				throw new IllegalArgumentException("Unexpected " + AutoWrap.class.getSimpleName() + ": '" + pAutoWrap + "'.");
		}
	}

	private static <L extends List<CharSequence>> L splitLinesByLetters(final IFont pFont, final CharSequence pText, final L pResult, final float pAutoWrapWidth) {
		final int textLength = pText.length();

		int lineStart = 0;
		int lineEnd = 0;
		int lastNonWhitespace = 0;
		boolean charsAvailable = false;

		for(int i = 0; i < textLength; i++) {
			final char character = pText.charAt(i);
			if(character != ' ') {
				if(charsAvailable) {
					lastNonWhitespace = i + 1;
				} else {
					charsAvailable = true;
					lineStart = i;
					lastNonWhitespace = lineStart + 1;
					lineEnd = lastNonWhitespace;
				}
			}

			if(charsAvailable) {
//				/* Just for debugging. */
//				final CharSequence line = pText.subSequence(lineStart, lineEnd);
//				final float lineWidth = FontUtils.measureText(pFont, pText, lineStart, lineEnd);
//
//				final CharSequence lookaheadLine = pText.subSequence(lineStart, lastNonWhitespace);
				final float lookaheadLineWidth = FontUtils.measureText(pFont, pText, lineStart, lastNonWhitespace);

				final boolean isEndReached = (i == (textLength - 1));
				if(isEndReached) {
					/* When the end of the string is reached, add remainder to result. */
					if(lookaheadLineWidth <= pAutoWrapWidth) {
						pResult.add(pText.subSequence(lineStart, lastNonWhitespace));
					} else {
						pResult.add(pText.subSequence(lineStart, lineEnd));
						/* Avoid special case where last line is added twice. */
						if(lineStart != i) {
							pResult.add(pText.subSequence(i, lastNonWhitespace));
						}
					}
				} else {
					if(lookaheadLineWidth <= pAutoWrapWidth) {
						lineEnd = lastNonWhitespace;
					} else {
						pResult.add(pText.subSequence(lineStart, lineEnd));
						i = lineEnd - 1;
						charsAvailable = false;
					}
				}
			}
		}

		return pResult;
	}

	private static <L extends List<CharSequence>> L splitLinesByWords(final IFont pFont, final CharSequence pText, final L pResult, final float pAutoWrapWidth) {
		final int textLength = pText.length();

		if(textLength == 0) {
			return pResult;
		}

		final float spaceWidth = pFont.getLetter(' ').mAdvance;

		int lastWordEnd = FontUtils.UNSPECIFIED;
		int lineStart = FontUtils.UNSPECIFIED;
		int lineEnd = FontUtils.UNSPECIFIED;

		float lineWidthRemaining = pAutoWrapWidth;
		boolean firstWordInLine = true;
		int i = 0;
		while(i < textLength) {
			int spacesSkipped = 0;
			/* Find next word. */
			{ /* Skip whitespaces. */
				while((i < textLength) && (pText.charAt(i) == ' ')) {
					i++;
					spacesSkipped++;
				}
			}
			final int wordStart = i;

			/* Mark beginning of a new line. */
			if(lineStart == FontUtils.UNSPECIFIED) {
				lineStart = wordStart;
			}

			{ /* Skip non-whitespaces. */
				while((i < textLength) && (pText.charAt(i) != ' ')) {
					i++;
				}
			}
			final int wordEnd = i;

			/* Nothing more could be read. */
			if(wordStart == wordEnd) {
				if(!firstWordInLine) {
					pResult.add(pText.subSequence(lineStart, lineEnd));
				}
				break;
			}

//			/* Just for debugging. */
//			final CharSequence word = pText.subSequence(wordStart, wordEnd);

			final float wordWidth = FontUtils.measureText(pFont, pText, wordStart, wordEnd);

			/* Determine the width actually needed for the current word. */
			final float widthNeeded;
			if(firstWordInLine) {
				widthNeeded = wordWidth;
			} else {
				widthNeeded = (spacesSkipped * spaceWidth) + wordWidth;
			}

			/* Check if the word fits into the rest of the line. */
			if (widthNeeded <= lineWidthRemaining) {
				if(firstWordInLine) {
					firstWordInLine = false;
				} else {
					lineWidthRemaining -= FontUtils.getAdvanceCorrection(pFont, pText, lastWordEnd - 1);
				}
				lineWidthRemaining -= widthNeeded;
				lastWordEnd = wordEnd;
				lineEnd = wordEnd;

				/* Check if the end was reached. */
				if(wordEnd == textLength) {
					pResult.add(pText.subSequence(lineStart, lineEnd));
					/* Added the last line. */
					break;
				}
			} else {
				/* Special case for lines with only one word. */
				if(firstWordInLine) {
					/* Check for lines that are just too big. */
					if(wordWidth >= pAutoWrapWidth) {
						pResult.add(pText.subSequence(wordStart, wordEnd));
						lineWidthRemaining = pAutoWrapWidth;
					} else {
						lineWidthRemaining = pAutoWrapWidth - wordWidth;

						/* Check if the end was reached. */
						if(wordEnd == textLength) {
							pResult.add(pText.subSequence(wordStart, wordEnd));
							/* Added the last line. */
							break;
						}
					}

					/* Start a completely new line. */
					firstWordInLine = true;
					lastWordEnd = FontUtils.UNSPECIFIED;
					lineStart = FontUtils.UNSPECIFIED;
					lineEnd = FontUtils.UNSPECIFIED;
				} else {
					/* Finish the current line. */
					pResult.add(pText.subSequence(lineStart, lineEnd));

					/* Check if the end was reached. */
					if(wordEnd == textLength) {
						/* Add the last word. */
						pResult.add(pText.subSequence(wordStart, wordEnd)); // TODO Does this cover all cases?
						break;
					} else {
						/* Start a new line, carrying over the current word. */
						lineWidthRemaining = pAutoWrapWidth - wordWidth;
						firstWordInLine = false;
						lastWordEnd = wordEnd;
						lineStart = wordStart;
						lineEnd = wordEnd;
					}
				}
			}
		}
		return pResult;
	}
	
	private static <L extends List<CharSequence>> L splitLinesByCJK(final IFont pFont, final CharSequence pText, final L pResult, final float pAutoWrapWidth) {
		final int textLength = pText.length();

		int lineStart = 0;
		int lineEnd = 0;

		/* Skip whitespaces at the beginning of the string. */
		while((lineStart < textLength) && (pText.charAt(lineStart) == ' ')) {
			lineStart++;
			lineEnd++;
		}
		
		int i = lineEnd;
		while(i < textLength) {
			lineStart = lineEnd;

			{ /* Look for a sub string */
				boolean charsAvailable = true;
				while(i < textLength) {
					
					{ /* Skip whitespaces at the end of the string */
						int j = lineEnd;
						while ( j < textLength ) {
							if ( pText.charAt( j ) == ' ' ) {
								j++;
							}
							else {
								break;
							}
						}
						if ( j == textLength ) {
							if ( lineStart == lineEnd ) {
								charsAvailable = false;
							}
							i = textLength;
							break;
						}
					}
					
					lineEnd++;

					final float lineWidth = FontUtils.measureText(pFont, pText, lineStart, lineEnd);

					if(lineWidth > pAutoWrapWidth) {
						if ( lineStart < lineEnd - 1 ) {
							lineEnd--;
						}
						
						pResult.add(pText.subSequence(lineStart, lineEnd));
						charsAvailable = false;
						i = lineEnd;
						break;
					}
					i = lineEnd;
				}

				if(charsAvailable) {
					pResult.add(pText.subSequence(lineStart, lineEnd));
				}
			}
		}

		return pResult;
	}

	private static float getAdvanceCorrection(final IFont pFont, final CharSequence pText, final int pIndex) {
		final Letter lastWordLastLetter = pFont.getLetter(pText.charAt(pIndex));
		return -(lastWordLastLetter.mOffsetX + lastWordLastLetter.mWidth) + lastWordLastLetter.mAdvance;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public enum MeasureDirection {
		// ===========================================================
		// Elements
		// ===========================================================

		FORWARDS,
		BACKWARDS;

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
