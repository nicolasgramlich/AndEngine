package org.andengine.opengl.font;


import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.andengine.entity.text.AutoWrap;
import org.andengine.opengl.font.exception.LetterNotFoundException;
import org.andengine.opengl.texture.ITexture;
import org.andengine.util.AssertUtils;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:16:00 - 25.01.2012
 */
public class FontUtilsTest extends TestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final int WIDTH = 10;
	protected static final int HEIGHT = 0;
	protected static final float OFFSET_X = 0;
	protected static final float OFFSET_Y = 0;
	protected static final float ADVANCE = 10;

	// ===========================================================
	// Fields
	// ===========================================================

	private final IFont mFont = new IFont() {
		@Override
		public void unload() {
		}

		@Override
		public void load() {
		}

		@Override
		public ITexture getTexture() {
			return null;
		}

		@Override
		public float getLineHeight() {
			return 0;
		}

		@Override
		public Letter getLetter(final char pChar) throws LetterNotFoundException {
			return new Letter(pChar, 0, 0, FontUtilsTest.WIDTH, FontUtilsTest.HEIGHT, FontUtilsTest.OFFSET_X, FontUtilsTest.OFFSET_Y, FontUtilsTest.ADVANCE, 0, 0, 0, 0);
		}
	};

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

	public void testMeasureSingleChar() {
		this.runMeasureTextTest("A", 10);
	}

	public void testMeasureMultipleChars() {
		this.runMeasureTextTest("AAA", 30);
	}

	public void testMeasureMultipleCharsSubstring() {
		this.runMeasureTextTest("AAA", 0, 1, 10);
	}

	public void testMeasureMultipleCharsInnerSpace() {
		this.runMeasureTextTest("A A", 0, 3, 30);
	}

	public void testMeasureMultipleCharsMultipleInnerSpaces() {
		this.runMeasureTextTest("A   A", 0, 5, 50);
	}


	public void testSplitSingleByWordsLineZeroWidth() {
		this.runSplitLinesByWordsTest("A", 0, "A");
	}

	public void testSplitSingleLineByWords() {
		this.runSplitLinesByWordsTest("AAA", 100, "AAA");
		this.runSplitLinesByWordsTest("AAA", 30,  "AAA");
	}

	public void testSplitSingleLineByWordsMultiWordTrailingSpaces() {
		this.runSplitLinesByWordsTest("AAA AAA   ", 100, "AAA AAA");
	}

	public void testSplitSingleLineByWordsExactWidth() {
		this.runSplitLinesByWordsTest("AAA", 29,  "AAA");
	}

	public void testSplitSingleLineByWordsOverWidth() {
		this.runSplitLinesByWordsTest("AAA", 29,  "AAA");
	}

	public void testMultiLineByWords() {
		this.runSplitLinesByWordsTest("AAA A", 30, "AAA", "A");
	}

	public void testMultipleLinesByWordsOverWidth() {
		this.runSplitLinesByWordsTest("AAA AAAA A AAA", 30, "AAA", "AAAA", "A", "AAA");
	}

	public void testSplitSingleLineByWordsWidthLeadingSpaces() {
		this.runSplitLinesByWordsTest("         A", 30, "A");
	}

	public void testSplitSingleLineByWordsTrailingSpaces() {
		this.runSplitLinesByWordsTest("A         ", 30, "A");
	}

	public void testSplitSingleLineByWordsLeadingAndTrailingSpaces() {
		this.runSplitLinesByWordsTest("         A         ", 30, "A");
	}

	public void testSplitMultipleLinesByWordsMultiWords() {
		this.runSplitLinesByWordsTest("AA AA AA AA AA", 50, "AA AA", "AA AA", "AA");
	}

	public void testSplitMultipleLinesByWordsInnerSpaces() {
		this.runSplitLinesByWordsTest("A   A", 100, "A   A");
	}

	public void testSplitMultipleLinesByWordsInnerSpacesExactWidth() {
		this.runSplitLinesByWordsTest("A   A", 50, "A   A");
	}

	public void testSplitMultipleLinesByWordsInnerSpacesOverWidth() {
		this.runSplitLinesByWordsTest("A    A", 50, "A", "A");
	}

	public void testSplitMultipleLinesByWordsMultipleInnerSpacesOverWidth() {
		this.runSplitLinesByWordsTest("AAAAA  A", 50, "AAAAA", "A");
		this.runSplitLinesByWordsTest("AAAAA A", 50, "AAAAA", "A");
		this.runSplitLinesByWordsTest("AAAAA   A", 50, "AAAAA", "A");
		this.runSplitLinesByWordsTest(" AAAAA   A", 50, "AAAAA", "A");
		this.runSplitLinesByWordsTest("AAAAA   A ", 50, "AAAAA", "A");
		this.runSplitLinesByWordsTest(" AAAAA   A ", 50, "AAAAA", "A");
		this.runSplitLinesByWordsTest(" AAAAA A ", 50, "AAAAA", "A");
	}

	public void testSplitMultipleLinesByLettersLineZeroWidth() {
		this.runSplitLinesByLettersTest("A    ", 0, "A");
		this.runSplitLinesByLettersTest("AAAAA", 0, "A", "A", "A", "A", "A");
		this.runSplitLinesByLettersTest("    A", 0, "A");
	}

	public void testSplitMultipleLinesByLetters1() {
		this.runSplitLinesByLettersTest("A      ", 0, "A");
	}

	public void testSplitMultipleLinesByLetters2() {
		this.runSplitLinesByLettersTest("ABCDEF", 30, "ABC", "DEF");
	}

	public void testSplitMultipleLinesByLetters3() {
		this.runSplitLinesByLettersTest("ABCDEF", 32, "ABC", "DEF");
	}

	public void testSplitMultipleLinesByLetters4() {
		this.runSplitLinesByLettersTest("ABC DE", 50, "ABC D", "E");
	}

	public void testSplitMultipleLinesByLetters5() {
		this.runSplitLinesByLettersTest("ABC  D", 50, "ABC", "D");
	}

	public void testSplitMultipleLinesByLetters6() {
		this.runSplitLinesByLettersTest("ABCD E", 50, "ABCD", "E");
	}

	public void testSplitMultipleLinesByLetters7() {
		this.runSplitLinesByLettersTest("     A", 50, "A");
	}

	public void testSplitMultipleLinesByLetters8() {
		this.runSplitLinesByLettersTest("A     ", 50, "A");
	}

	public void testSplitMultipleLinesByCJK(){
		this.runSplitLinesByCJKTest("A      ", 0, "A");
		this.runSplitLinesByCJKTest("AAA AA", 50, "AAA A", "A");
		this.runSplitLinesByCJKTest("AAA  A", 50, "AAA  ", "A");
		this.runSplitLinesByCJKTest("AAAA A", 50, "AAAA ", "A");
		this.runSplitLinesByCJKTest("AAAA  A", 50, "AAAA ", " A");
		this.runSplitLinesByCJKTest("AAAAA A", 50, "AAAAA", " A");
		this.runSplitLinesByCJKTest("A  A A", 50, "A  A ", "A");
		this.runSplitLinesByCJKTest("     A", 50, "A");
		this.runSplitLinesByCJKTest("A     ", 50, "A");
		this.runSplitLinesByCJKTest("AAAAAA", 32, "AAA", "AAA");
		this.runSplitLinesByCJKTest("夏洛特 吉尔伯特想要搬进你的塔楼！", 135, "夏洛特 吉尔伯特想要搬进你", "的塔楼！");
		this.runSplitLinesByCJKTest("你正在出售胡萝卜， 很快就会财源滚滚来，然后用这些钱去建造摩天大楼！", 165, "你正在出售胡萝卜， 很快就会财源", "滚滚来，然后用这些钱去建造摩天大", "楼！");
	}

	private void runSplitLinesByLettersTest(final String pText, final int pLineWidthMaximum, final String ... pExpectedLines) {
		final ArrayList<CharSequence> result = new ArrayList<CharSequence>();
		Assert.assertEquals(pExpectedLines.length, FontUtils.splitLines(this.mFont, pText, result, AutoWrap.LETTERS, pLineWidthMaximum).size());
		AssertUtils.assertArrayEquals(pExpectedLines, result.toArray(new String[pExpectedLines.length]));
	}

	private void runSplitLinesByWordsTest(final String pText, final int pLineWidthMaximum, final String ... pExpectedLines) {
		final ArrayList<CharSequence> result = new ArrayList<CharSequence>();
		Assert.assertEquals(pExpectedLines.length, FontUtils.splitLines(this.mFont, pText, result, AutoWrap.WORDS, pLineWidthMaximum).size());
		AssertUtils.assertArrayEquals(pExpectedLines, result.toArray(new String[pExpectedLines.length]));
	}
	
	private void runSplitLinesByCJKTest(final String pText, final int pLineWidthMaximum, final String ... pExpectedLines) {
		final ArrayList<CharSequence> result = new ArrayList<CharSequence>();
		Assert.assertEquals(pExpectedLines.length, FontUtils.splitLines(this.mFont, pText, result, AutoWrap.CJK, pLineWidthMaximum).size());
		AssertUtils.assertArrayEquals(pExpectedLines, result.toArray(new String[pExpectedLines.length]));
	}

	private void runMeasureTextTest(final String pText, final float pExpectedWidth) {
		Assert.assertEquals(pExpectedWidth, FontUtils.measureText(this.mFont, pText));
	}

	private void runMeasureTextTest(final String pText, final int pStart, final int pEnd, final float pExpectedWidth) {
		Assert.assertEquals(pExpectedWidth, FontUtils.measureText(this.mFont, pText, pStart, pEnd));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
