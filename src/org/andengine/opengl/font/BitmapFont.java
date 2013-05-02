package org.andengine.opengl.font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.exception.FontException;
import org.andengine.opengl.font.exception.LetterNotFoundException;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.util.StreamUtils;
import org.andengine.util.TextUtils;
import org.andengine.util.adt.io.in.AssetInputStreamOpener;

import android.content.res.AssetManager;
import android.util.SparseArray;

/**
 * The {@link BitmapFont} class is capable of parsing and loading <code>*.fnt</code> fonts.
 * A {@link BitmapFont}, just like a {@link Font} can be used to create {@link Text} objects.
 *
 * Use any of these editors to generate <code>*.fnt</code> fonts:
 *
 * <ul>
 * <li><a href="http://glyphdesigner.71squared.com/">http://glyphdesigner.71squared.com/</a> (Commercial, Mac OS X)</li>
 * <li><a href="http://www.n4te.com/hiero/hiero.jnlp">http://www.n4te.com/hiero/hiero.jnlp</a> (Free, Java)</li>
 * <li><a href="http://slick.cokeandcode.com/demos/hiero.jnlp">http://slick.cokeandcode.com/demos/hiero.jnlp</a> (Free, Java)</li>
 * <li><a href="http://www.angelcode.com/products/bmfont/">http://www.angelcode.com/products/bmfont/</a> (Free, Windows only)</li>
 * </ul>
 *
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:54:59 - 01.11.2011
 */
public class BitmapFont implements IFont {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String TAG_INFO = "info";
	private static final int TAG_INFO_ATTRIBUTECOUNT = 11;

	private static final String TAG_INFO_ATTRIBUTE_FACE = "face";
	private static final String TAG_INFO_ATTRIBUTE_SIZE = "size";
	private static final String TAG_INFO_ATTRIBUTE_BOLD = "bold";
	private static final String TAG_INFO_ATTRIBUTE_ITALIC = "italic";
	private static final String TAG_INFO_ATTRIBUTE_CHARSET = "charset";
	private static final String TAG_INFO_ATTRIBUTE_UNICODE = "unicode";
	private static final String TAG_INFO_ATTRIBUTE_STRETCHHEIGHT = "stretchH";
	private static final String TAG_INFO_ATTRIBUTE_SMOOTH = "smooth";
	private static final String TAG_INFO_ATTRIBUTE_ANTIALIASED = "aa";
	private static final String TAG_INFO_ATTRIBUTE_PADDING = "padding";
	private static final String TAG_INFO_ATTRIBUTE_SPACING = "spacing";

	private static final int TAG_INFO_ATTRIBUTE_FACE_INDEX = 1;
	private static final int TAG_INFO_ATTRIBUTE_SIZE_INDEX = BitmapFont.TAG_INFO_ATTRIBUTE_FACE_INDEX + 1;
	private static final int TAG_INFO_ATTRIBUTE_BOLD_INDEX = BitmapFont.TAG_INFO_ATTRIBUTE_SIZE_INDEX + 1;
	private static final int TAG_INFO_ATTRIBUTE_ITALIC_INDEX = BitmapFont.TAG_INFO_ATTRIBUTE_BOLD_INDEX + 1;
	private static final int TAG_INFO_ATTRIBUTE_CHARSET_INDEX = BitmapFont.TAG_INFO_ATTRIBUTE_ITALIC_INDEX + 1;
	private static final int TAG_INFO_ATTRIBUTE_UNICODE_INDEX = BitmapFont.TAG_INFO_ATTRIBUTE_CHARSET_INDEX + 1;
	private static final int TAG_INFO_ATTRIBUTE_STRETCHHEIGHT_INDEX = BitmapFont.TAG_INFO_ATTRIBUTE_UNICODE_INDEX + 1;
	private static final int TAG_INFO_ATTRIBUTE_SMOOTH_INDEX = BitmapFont.TAG_INFO_ATTRIBUTE_STRETCHHEIGHT_INDEX + 1;
	private static final int TAG_INFO_ATTRIBUTE_ANTIALIASED_INDEX = BitmapFont.TAG_INFO_ATTRIBUTE_SMOOTH_INDEX + 1;
	private static final int TAG_INFO_ATTRIBUTE_PADDING_INDEX = BitmapFont.TAG_INFO_ATTRIBUTE_ANTIALIASED_INDEX + 1;
	private static final int TAG_INFO_ATTRIBUTE_SPACING_INDEX = BitmapFont.TAG_INFO_ATTRIBUTE_PADDING_INDEX + 1;

	private static final String TAG_COMMON = "common";
	private static final int TAG_COMMON_ATTRIBUTECOUNT = 6;

	private static final String TAG_COMMON_ATTRIBUTE_LINEHEIGHT = "lineHeight";
	private static final String TAG_COMMON_ATTRIBUTE_BASE = "base";
	private static final String TAG_COMMON_ATTRIBUTE_SCALEWIDTH = "scaleW";
	private static final String TAG_COMMON_ATTRIBUTE_SCALEHEIGHT = "scaleH";
	private static final String TAG_COMMON_ATTRIBUTE_PAGES = "pages";
	private static final String TAG_COMMON_ATTRIBUTE_PACKED = "packed";

	private static final int TAG_COMMON_ATTRIBUTE_LINEHEIGHT_INDEX = 1;
	private static final int TAG_COMMON_ATTRIBUTE_BASE_INDEX = BitmapFont.TAG_COMMON_ATTRIBUTE_LINEHEIGHT_INDEX + 1;
	private static final int TAG_COMMON_ATTRIBUTE_SCALEWIDTH_INDEX = BitmapFont.TAG_COMMON_ATTRIBUTE_BASE_INDEX + 1;
	private static final int TAG_COMMON_ATTRIBUTE_SCALEHEIGHT_INDEX = BitmapFont.TAG_COMMON_ATTRIBUTE_SCALEWIDTH_INDEX + 1;
	private static final int TAG_COMMON_ATTRIBUTE_PAGES_INDEX = BitmapFont.TAG_COMMON_ATTRIBUTE_SCALEHEIGHT_INDEX + 1;
	private static final int TAG_COMMON_ATTRIBUTE_PACKED_INDEX = BitmapFont.TAG_COMMON_ATTRIBUTE_PAGES_INDEX + 1;

	private static final String TAG_PAGE = "page";
	private static final int TAG_PAGE_ATTRIBUTECOUNT = 2;

	private static final String TAG_PAGE_ATTRIBUTE_ID = "id";
	private static final String TAG_PAGE_ATTRIBUTE_FILE = "file";

	private static final int TAG_PAGE_ATTRIBUTE_ID_INDEX = 1;
	private static final int TAG_PAGE_ATTRIBUTE_FILE_INDEX = BitmapFont.TAG_PAGE_ATTRIBUTE_ID_INDEX + 1;

	private static final String TAG_CHARS = "chars";
	private static final int TAG_CHARS_ATTRIBUTECOUNT = 1;

	private static final String TAG_CHARS_ATTRIBUTE_COUNT = "count";

	private static final int TAG_CHARS_ATTRIBUTE_COUNT_INDEX = 1;

	private static final String TAG_CHAR = "char";
	private static final int TAG_CHAR_ATTRIBUTECOUNT = 10;

	private static final String TAG_CHAR_ATTRIBUTE_ID = "id";
	private static final String TAG_CHAR_ATTRIBUTE_X = "x";
	private static final String TAG_CHAR_ATTRIBUTE_Y = "y";
	private static final String TAG_CHAR_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_CHAR_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_CHAR_ATTRIBUTE_XOFFSET = "xoffset";
	private static final String TAG_CHAR_ATTRIBUTE_YOFFSET = "yoffset";
	private static final String TAG_CHAR_ATTRIBUTE_XADVANCE = "xadvance";
	private static final String TAG_CHAR_ATTRIBUTE_PAGE = "page";
//	private static final String TAG_CHAR_ATTRIBUTE_CHANNEL = "chnl";

	private static final int TAG_CHAR_ATTRIBUTE_ID_INDEX = 1;
	private static final int TAG_CHAR_ATTRIBUTE_X_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_ID_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_Y_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_X_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_WIDTH_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_Y_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_HEIGHT_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_WIDTH_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_XOFFSET_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_HEIGHT_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_YOFFSET_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_XOFFSET_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_XADVANCE_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_YOFFSET_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_PAGE_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_XADVANCE_INDEX + 1;
//	private static final int TAG_CHAR_ATTRIBUTE_CHANNEL_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_PAGE_INDEX + 1;

	private static final String TAG_KERNINGS = "kernings";
	private static final int TAG_KERNINGS_ATTRIBUTECOUNT = 1;

	private static final String TAG_KERNINGS_ATTRIBUTE_COUNT = "count";

	private static final int TAG_KERNINGS_ATTRIBUTE_COUNT_INDEX = 1;

	private static final String TAG_KERNING = "kerning";
	private static final int TAG_KERNING_ATTRIBUTECOUNT = 3;

	private static final String TAG_KERNING_ATTRIBUTE_FIRST = "first";
	private static final String TAG_KERNING_ATTRIBUTE_SECOND = "second";
	private static final String TAG_KERNING_ATTRIBUTE_AMOUNT = "amount";

	private static final int TAG_KERNING_ATTRIBUTE_FIRST_INDEX = 1;
	private static final int TAG_KERNING_ATTRIBUTE_SECOND_INDEX = BitmapFont.TAG_KERNING_ATTRIBUTE_FIRST_INDEX + 1;
	private static final int TAG_KERNING_ATTRIBUTE_AMOUNT_INDEX = BitmapFont.TAG_KERNING_ATTRIBUTE_SECOND_INDEX + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final TextureManager mTextureManager;

	private final BitmapTextureFormat mBitmapTextureFormat;
	private final TextureOptions mTextureOptions;

	private final SparseArray<Letter> mCharacterToLetterMap = new SparseArray<Letter>();

	private final BitmapFontInfo mBitmapFontInfo;
	private final BitmapFontPage[] mBitmapFontPages;

	private final int mLineHeight;
	private final int mBase;
	private final int mScaleWidth;
	private final int mScaleHeight;
	private final int mBitmapFontPageCount;
	private final boolean mPacked;
	private final BitmapFontOptions mBitmapFontOptions;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BitmapFont(final TextureManager pTextureManager, final AssetManager pAssetManager, final String pAssetPath) {
		this(pTextureManager, pAssetManager, pAssetPath, BitmapTextureFormat.RGBA_8888, TextureOptions.DEFAULT, BitmapFontOptions.DEFAULT);
	}

	public BitmapFont(final TextureManager pTextureManager, final AssetManager pAssetManager, final String pAssetPath, final BitmapTextureFormat pBitmapTextureFormat) {
		this(pTextureManager, pAssetManager, pAssetPath, pBitmapTextureFormat, TextureOptions.DEFAULT, BitmapFontOptions.DEFAULT);
	}

	public BitmapFont(final TextureManager pTextureManager, final AssetManager pAssetManager, final String pAssetPath, final TextureOptions pTextureOptions) {
		this(pTextureManager, pAssetManager, pAssetPath, BitmapTextureFormat.RGBA_8888, pTextureOptions, BitmapFontOptions.DEFAULT);
	}

	public BitmapFont(final TextureManager pTextureManager, final AssetManager pAssetManager, final String pAssetPath, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions) {
		this(pTextureManager, pAssetManager, pAssetPath, pBitmapTextureFormat, pTextureOptions, BitmapFontOptions.DEFAULT);
	}

	public BitmapFont(final TextureManager pTextureManager, final AssetManager pAssetManager, final String pAssetPath, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final BitmapFontOptions pBitmapFontOptions) {
		this.mTextureManager = pTextureManager;
		this.mBitmapTextureFormat = pBitmapTextureFormat;
		this.mTextureOptions = pTextureOptions;
		this.mBitmapFontOptions = pBitmapFontOptions;

		InputStream in = null;
		try {
			in = pAssetManager.open(pAssetPath);
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in), StreamUtils.IO_BUFFER_SIZE);

			final String assetBasePath;
			if (pAssetPath.indexOf('/') == -1) {
				assetBasePath = "";
			} else {
				assetBasePath = pAssetPath.substring(0, pAssetPath.lastIndexOf('/') + 1);
			}

			/* Info. */
			{
				this.mBitmapFontInfo = new BitmapFontInfo(bufferedReader.readLine());
			}

			/* Common. */
			{
				final String common = bufferedReader.readLine();
				if ((common != null) && common.startsWith(BitmapFont.TAG_COMMON)) {
					final String[] commonAttributes = TextUtils.SPLITPATTERN_SPACE.split(common, BitmapFont.TAG_COMMON_ATTRIBUTECOUNT + 1);
					if ((commonAttributes.length - 1) != BitmapFont.TAG_COMMON_ATTRIBUTECOUNT) {
						throw new FontException("Expected: '" + BitmapFont.TAG_COMMON_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_COMMON + " attributes, found: '" + (commonAttributes.length - 1) + "'.");
					}
					if (!commonAttributes[0].equals(BitmapFont.TAG_COMMON)) {
						throw new FontException("Expected: '" + BitmapFont.TAG_COMMON + "' attributes.");
					}
					this.mLineHeight = BitmapFont.getIntAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_LINEHEIGHT_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_LINEHEIGHT);
					this.mBase = BitmapFont.getIntAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_BASE_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_BASE);
					this.mScaleWidth = BitmapFont.getIntAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_SCALEWIDTH_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_SCALEWIDTH);
					this.mScaleHeight = BitmapFont.getIntAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_SCALEHEIGHT_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_SCALEHEIGHT);
					this.mBitmapFontPageCount = BitmapFont.getIntAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_PAGES_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_PAGES);
					this.mPacked = BitmapFont.getBooleanAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_PACKED_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_PACKED);

					if (this.mBitmapFontPageCount != 1) {
						throw new FontException("Only a single page is supported.");
					}
					this.mBitmapFontPages = new BitmapFontPage[this.mBitmapFontPageCount];

					if (this.mPacked) {
						throw new FontException("Packed is not supported.");
					}
				} else {
					throw new FontException("Expected: '" + BitmapFont.TAG_COMMON + "' attributes.");
				}
			}

			/* Pages. */
			for (int i = 0; i < this.mBitmapFontPageCount; i++) {
				this.mBitmapFontPages[i] = new BitmapFontPage(pAssetManager, assetBasePath, bufferedReader.readLine());
			}

			/* Chars. */
			{
				final String chars = bufferedReader.readLine();
				if ((chars != null) && chars.startsWith(BitmapFont.TAG_CHARS)) {
					final String[] charsAttributes = TextUtils.SPLITPATTERN_SPACE.split(chars, BitmapFont.TAG_CHARS_ATTRIBUTECOUNT + 1);
					if ((charsAttributes.length - 1) != BitmapFont.TAG_CHARS_ATTRIBUTECOUNT) {
						throw new FontException("Expected: '" + BitmapFont.TAG_CHARS_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_CHARS + " attributes, found: '" + (charsAttributes.length - 1) + "'.");
					}
					if (!charsAttributes[0].equals(BitmapFont.TAG_CHARS)) {
						throw new FontException("Expected: '" + BitmapFont.TAG_CHARS + "' attributes.");
					}

					final int characterCount = BitmapFont.getIntAttribute(charsAttributes, BitmapFont.TAG_CHARS_ATTRIBUTE_COUNT_INDEX, BitmapFont.TAG_CHARS_ATTRIBUTE_COUNT);

					this.parseCharacters(characterCount, bufferedReader);
				} else {
					throw new FontException("Expected: '" + BitmapFont.TAG_CHARS + "' attributes.");
				}
			}

			/* Kernings. */
			{
				final String kernings = bufferedReader.readLine();
				if ((kernings != null) && kernings.startsWith(BitmapFont.TAG_KERNINGS)) {
					final String[] kerningsAttributes = TextUtils.SPLITPATTERN_SPACE.split(kernings, BitmapFont.TAG_KERNINGS_ATTRIBUTECOUNT + 1);
					if ((kerningsAttributes.length - 1) != BitmapFont.TAG_KERNINGS_ATTRIBUTECOUNT) {
						throw new FontException("Expected: '" + BitmapFont.TAG_KERNINGS_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_KERNINGS + " attributes, found: '" + (kerningsAttributes.length - 1) + "'.");
					}
					if (!kerningsAttributes[0].equals(BitmapFont.TAG_KERNINGS)) {
						throw new FontException("Expected: '" + BitmapFont.TAG_KERNINGS + "' attributes.");
					}

					final int kerningsCount = BitmapFont.getIntAttribute(kerningsAttributes, BitmapFont.TAG_KERNINGS_ATTRIBUTE_COUNT_INDEX, BitmapFont.TAG_KERNINGS_ATTRIBUTE_COUNT);

					this.parseKernings(kerningsCount, bufferedReader);
				}
			}
		} catch (final IOException e) {
			throw new FontException("Failed loading BitmapFont. AssetPath: " + pAssetPath, e);
		} finally {
			StreamUtils.close(in);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public BitmapFontInfo getBitmapFontInfo() {
		return this.mBitmapFontInfo;
	}

	public int getBase() {
		return this.mBase;
	}

	public int getScaleWidth() {
		return this.mScaleWidth;
	}

	public int getScaleHeight() {
		return this.mScaleHeight;
	}

	public int getBitmapFontPageCount() {
		return this.mBitmapFontPageCount;
	}

	public BitmapFontPage[] getBitmapFontPages() {
		return this.mBitmapFontPages;
	}

	public BitmapFontPage getBitmapFontPage(final int pIndex) {
		return this.mBitmapFontPages[pIndex];
	}

	public boolean isPacked() {
		return this.mPacked;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ITexture getTexture() {
		return this.mBitmapFontPages[0].getTexture();
	}

	@Override
	public void load() {
		this.loadTextures();
	}

	@Override
	public void unload() {
		this.unloadTextures();
	}

	@Override
	public float getAscent() {
		return 0;
	}

	@Override
	public float getLineHeight() {
		return this.mLineHeight;
	}

	@Override
	public Letter getLetter(final char pChar) throws LetterNotFoundException {
		final Letter letter = this.mCharacterToLetterMap.get(pChar);
		if (letter == null) {
			throw new LetterNotFoundException("Letter '" + pChar + "' not found.");
		}
		return letter;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void loadTextures() {
		final BitmapFontPage[] bitmapFontPages = this.mBitmapFontPages;
		final int bitmapFontPageCount = bitmapFontPages.length;
		for (int i = 0; i < bitmapFontPageCount; i++) {
			bitmapFontPages[i].getTexture().load();
		}
	}

	public void unloadTextures() {
		final BitmapFontPage[] bitmapFontPages = this.mBitmapFontPages;
		final int bitmapFontPageCount = bitmapFontPages.length;
		for (int i = 0; i < bitmapFontPageCount; i++) {
			bitmapFontPages[i].getTexture().unload();
		}
	}

	private void parseCharacters(final int pCharacterCount, final BufferedReader pBufferedReader) throws IOException {
		for (int i = pCharacterCount - 1; i >= 0; i--) {
			final String character = pBufferedReader.readLine();
			final String[] charAttributes = TextUtils.SPLITPATTERN_SPACES.split(character, BitmapFont.TAG_CHAR_ATTRIBUTECOUNT + 1);
			if ((charAttributes.length - 1) != BitmapFont.TAG_CHAR_ATTRIBUTECOUNT) {
				throw new FontException("Expected: '" + BitmapFont.TAG_CHAR_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_CHAR + " attributes, found: '" + (charAttributes.length - 1) + "'.");
			}
			if (!charAttributes[0].equals(BitmapFont.TAG_CHAR)) {
				throw new FontException("Expected: '" + BitmapFont.TAG_CHAR + "' attributes.");
			}

			final char id = BitmapFont.getCharAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_ID_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_ID);
			final int x = this.mBitmapFontOptions.mTextureOffsetX + BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_X_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_X);
			final int y = this.mBitmapFontOptions.mTextureOffsetY + BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_Y_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_Y);
			final int width = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_WIDTH_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_WIDTH);
			final int height = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_HEIGHT_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_HEIGHT);
			final int xOffset = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_XOFFSET_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_XOFFSET);
			final int yOffset = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_YOFFSET_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_YOFFSET);
			final int xAdvance = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_XADVANCE_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_XADVANCE);
			final int page = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_PAGE_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_PAGE);
//			final int channel = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_CHANNEL_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_CHANNEL);

			final ITexture bitmapFontPageTexture = this.mBitmapFontPages[page].getTexture();
			final float textureWidth = bitmapFontPageTexture.getWidth();
			final float textureHeight = bitmapFontPageTexture.getHeight();

			final float u = x / textureWidth;
			final float v = y / textureHeight;
			final float u2 = (x + width) / textureWidth;
			final float v2 = (y + height) / textureHeight;

			this.mCharacterToLetterMap.put(id, new Letter((char) id, x, y, width, height, xOffset, yOffset, xAdvance, u, v, u2, v2));
		}
	}

	private void parseKernings(final int pKerningsCount, final BufferedReader pBufferedReader) throws IOException {
		for (int i = pKerningsCount - 1; i >= 0; i--) {
			final String kerning = pBufferedReader.readLine();
			final String[] charAttributes = TextUtils.SPLITPATTERN_SPACES.split(kerning, BitmapFont.TAG_KERNING_ATTRIBUTECOUNT + 1);
			if ((charAttributes.length - 1) != BitmapFont.TAG_KERNING_ATTRIBUTECOUNT) {
				throw new FontException("Expected: '" + BitmapFont.TAG_KERNING_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_KERNING + " attributes, found: '" + (charAttributes.length - 1) + "'.");
			}
			if (!charAttributes[0].equals(BitmapFont.TAG_KERNING)) {
				throw new FontException("Expected: '" + BitmapFont.TAG_KERNING + "' attributes.");
			}

			final int first = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_KERNING_ATTRIBUTE_FIRST_INDEX, BitmapFont.TAG_KERNING_ATTRIBUTE_FIRST);
			final int second = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_KERNING_ATTRIBUTE_SECOND_INDEX, BitmapFont.TAG_KERNING_ATTRIBUTE_SECOND);
			final int amount = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_KERNING_ATTRIBUTE_AMOUNT_INDEX, BitmapFont.TAG_KERNING_ATTRIBUTE_AMOUNT);

			this.mCharacterToLetterMap.get(first).addKerning(second, amount);
		}
	}

	private static boolean getBooleanAttribute(final String[] pData, final int pPosition, final String pAttribute) {
		final String data = pData[pPosition];
		final int attributeLength = pAttribute.length();

		if (!data.startsWith(pAttribute) || (data.charAt(attributeLength) != '=')) {
			throw new FontException("Expected '" + pAttribute + "' at position '" + pPosition + "', but found: '" + data + "'.");
		}

		return Integer.parseInt(data.substring(attributeLength + 1)) != 0;
	}

	private static char getCharAttribute(final String[] pData, final int pPosition, final String pAttribute) {
		return (char) BitmapFont.getIntAttribute(pData, pPosition, pAttribute);
	}

	private static int getIntAttribute(final String[] pData, final int pPosition, final String pAttribute) {
		final String data = pData[pPosition];
		final int attributeLength = pAttribute.length();

		if (!data.startsWith(pAttribute) || (data.charAt(attributeLength) != '=')) {
			throw new FontException("Expected '" + pAttribute + "' at position '" + pPosition + "', but found: '" + data + "'.");
		}

		return Integer.parseInt(data.substring(attributeLength + 1));
	}

	private static String getStringAttribute(final String[] pData, final int pPosition, final String pAttribute) {
		final String data = pData[pPosition];
		final int attributeLength = pAttribute.length();

		if (!data.startsWith(pAttribute) || (data.charAt(attributeLength) != '=')) {
			throw new FontException("Expected '" + pAttribute + "' at position '" + pPosition + "', but found: '" + data + "'.");
		}

		return data.substring(attributeLength + 2, data.length() - 1);
	}

	private static String getAttribute(final String[] pData, final int pPosition, final String pAttribute) {
		final String data = pData[pPosition];
		final int attributeLength = pAttribute.length();

		if (!data.startsWith(pAttribute)) {
			throw new FontException("Expected '" + pAttribute + "' at position '" + pPosition + "', but found: '" + data + "'.");
		}

		return data.substring(attributeLength + 1);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class BitmapFontOptions {
		// ===========================================================
		// Constants
		// ===========================================================

		public static final BitmapFontOptions DEFAULT = new BitmapFontOptions(0, 0);

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mTextureOffsetX;
		private final int mTextureOffsetY;

		// ===========================================================
		// Constructors
		// ===========================================================

		public BitmapFontOptions(final int pTextureOffsetX, final int pTextureOffsetY) {
			this.mTextureOffsetX = pTextureOffsetX;
			this.mTextureOffsetY = pTextureOffsetY;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getTextureOffsetX() {
			return this.mTextureOffsetX;
		}

		public int getTextureOffsetY() {
			return this.mTextureOffsetY;
		}

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

	public class BitmapFontInfo {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final int PADDING_LEFT_INDEX = 0;
		private static final int PADDING_TOP_INDEX = BitmapFontInfo.PADDING_LEFT_INDEX + 1;
		private static final int PADDING_RIGHT_INDEX = BitmapFontInfo.PADDING_TOP_INDEX + 1;
		private static final int PADDING_BOTTOM_INDEX = BitmapFontInfo.PADDING_RIGHT_INDEX + 1;

		private static final int SPACING_X_INDEX = 0;
		private static final int SPACING_Y_INDEX = BitmapFontInfo.SPACING_X_INDEX + 1;

		// ===========================================================
		// Fields
		// ===========================================================

		private final String mFace;
		private final int mSize;
		private final boolean mBold;
		private final boolean mItalic;
		private final String mCharset;
		private final int mUnicode;
		private final int mStretchHeight;
		private final boolean mSmooth;
		private final boolean mAntiAliased;

		private final int mPaddingLeft;
		private final int mPaddingTop;
		private final int mPaddingRight;
		private final int mPaddingBottom;

		private final int mSpacingX;
		private final int mSpacingY;

		// ===========================================================
		// Constructors
		// ===========================================================

		public BitmapFontInfo(final String pData) throws FontException {
			if (pData == null) {
				throw new FontException("pData must not be null.");
			}

			final String[] infoAttributes = TextUtils.SPLITPATTERN_SPACE.split(pData, BitmapFont.TAG_INFO_ATTRIBUTECOUNT + 1);

			if ((infoAttributes.length - 1) != BitmapFont.TAG_INFO_ATTRIBUTECOUNT) {
				throw new FontException("Expected: '" + BitmapFont.TAG_INFO_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_INFO + " attributes, found: '" + (infoAttributes.length - 1) + "'.");
			}
			if (!infoAttributes[0].equals(BitmapFont.TAG_INFO)) {
				throw new FontException("Expected: '" + BitmapFont.TAG_INFO + "' attributes.");
			}

			this.mFace = BitmapFont.getStringAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_FACE_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_FACE);
			this.mSize = BitmapFont.getIntAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_SIZE_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_SIZE);
			this.mBold = BitmapFont.getBooleanAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_BOLD_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_BOLD);
			this.mItalic = BitmapFont.getBooleanAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_ITALIC_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_ITALIC);
			this.mCharset = BitmapFont.getStringAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_CHARSET_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_CHARSET);
			this.mUnicode = BitmapFont.getIntAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_UNICODE_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_UNICODE);
			this.mStretchHeight = BitmapFont.getIntAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_STRETCHHEIGHT_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_STRETCHHEIGHT);
			this.mSmooth = BitmapFont.getBooleanAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_SMOOTH_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_SMOOTH);
			this.mAntiAliased = BitmapFont.getBooleanAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_ANTIALIASED_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_ANTIALIASED);

			final String padding = BitmapFont.getAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_PADDING_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_PADDING);
			final String[] paddings = TextUtils.SPLITPATTERN_COMMA.split(padding, 4);
			this.mPaddingLeft = Integer.parseInt(paddings[BitmapFontInfo.PADDING_LEFT_INDEX]);
			this.mPaddingTop = Integer.parseInt(paddings[BitmapFontInfo.PADDING_TOP_INDEX]);
			this.mPaddingRight = Integer.parseInt(paddings[BitmapFontInfo.PADDING_RIGHT_INDEX]);
			this.mPaddingBottom = Integer.parseInt(paddings[BitmapFontInfo.PADDING_BOTTOM_INDEX]);

			final String spacing = BitmapFont.getAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_SPACING_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_SPACING);
			final String[] spacings = TextUtils.SPLITPATTERN_COMMA.split(spacing, 2);
			this.mSpacingX = Integer.parseInt(spacings[BitmapFontInfo.SPACING_X_INDEX]);
			this.mSpacingY = Integer.parseInt(spacings[BitmapFontInfo.SPACING_Y_INDEX]);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public String getFace() {
			return this.mFace;
		}

		public int getSize() {
			return this.mSize;
		}

		public boolean isBold() {
			return this.mBold;
		}

		public boolean isItalic() {
			return this.mItalic;
		}

		public String getCharset() {
			return this.mCharset;
		}

		public int getUnicode() {
			return this.mUnicode;
		}

		public int getStretchHeight() {
			return this.mStretchHeight;
		}

		public boolean isSmooth() {
			return this.mSmooth;
		}

		public boolean isAntiAliased() {
			return this.mAntiAliased;
		}

		public int getPaddingLeft() {
			return this.mPaddingLeft;
		}

		public int getPaddingTop() {
			return this.mPaddingTop;
		}

		public int getPaddingRight() {
			return this.mPaddingRight;
		}

		public int getPaddingBottom() {
			return this.mPaddingBottom;
		}

		public int getSpacingX() {
			return this.mSpacingX;
		}

		public int getSpacingY() {
			return this.mSpacingY;
		}

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

	public class BitmapFontPage {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private int mID;
		private final ITexture mTexture;

		// ===========================================================
		// Constructors
		// ===========================================================

		public BitmapFontPage(final AssetManager pAssetManager, final String pAssetBasePath, final String pData) throws IOException {
			final String[] pageAttributes = TextUtils.SPLITPATTERN_SPACE.split(pData, BitmapFont.TAG_PAGE_ATTRIBUTECOUNT + 1);

			if ((pageAttributes.length - 1) != BitmapFont.TAG_PAGE_ATTRIBUTECOUNT) {
				throw new FontException("Expected: '" + BitmapFont.TAG_PAGE_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_PAGE + " attributes, found: '" + (pageAttributes.length - 1) + "'.");
			}
			if (!pageAttributes[0].equals(BitmapFont.TAG_PAGE)) {
				throw new FontException("Expected: '" + BitmapFont.TAG_PAGE + "' attributes.");
			}

			this.mID = BitmapFont.getIntAttribute(pageAttributes, BitmapFont.TAG_PAGE_ATTRIBUTE_ID_INDEX, BitmapFont.TAG_PAGE_ATTRIBUTE_ID);
			final String file = BitmapFont.getStringAttribute(pageAttributes, BitmapFont.TAG_PAGE_ATTRIBUTE_FILE_INDEX, BitmapFont.TAG_PAGE_ATTRIBUTE_FILE);

			final String assetPath = pAssetBasePath + file;
			this.mTexture = new BitmapTexture(BitmapFont.this.mTextureManager, new AssetInputStreamOpener(pAssetManager, assetPath), BitmapFont.this.mBitmapTextureFormat, BitmapFont.this.mTextureOptions);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getID() {
			return this.mID;
		}

		public ITexture getTexture() {
			return this.mTexture;
		}

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
