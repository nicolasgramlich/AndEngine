package org.anddev.andengine.opengl.font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.bitmap.BitmapTexture;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.StreamUtils;
import org.anddev.andengine.util.StringUtils;

import android.content.Context;
import android.util.SparseArray;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:54:59 - 01.11.2011
 */
public class BitmapFont {
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
//	private static final String TAG_CHAR_ATTRIBUTE_PAGE = "page";
//	private static final String TAG_CHAR_ATTRIBUTE_CHANNEL = "chnl";

	private static final int TAG_CHAR_ATTRIBUTE_ID_INDEX = 1;
	private static final int TAG_CHAR_ATTRIBUTE_X_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_ID_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_Y_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_X_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_WIDTH_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_Y_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_HEIGHT_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_WIDTH_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_XOFFSET_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_HEIGHT_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_YOFFSET_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_XOFFSET_INDEX + 1;
	private static final int TAG_CHAR_ATTRIBUTE_XADVANCE_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_YOFFSET_INDEX + 1;
//	private static final int TAG_CHAR_ATTRIBUTE_PAGE_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_XADVANCE_INDEX + 1;
//	private static final int TAG_CHAR_ATTRIBUTE_CHANNEL_INDEX = BitmapFont.TAG_CHAR_ATTRIBUTE_PAGE_INDEX + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private final SparseArray<Letter> mCharacterToLetterMap = new SparseArray<Letter>();

	private BitmapFontInfo mBitmapFontInfo;
	private BitmapFontPage[] mBitmapFontPages;

	private int mLineHeight;
	private int mBase;
	private int mScaleWidth;
	private int mScaleHeight;
	private int mPageCount;
	private boolean mPacked;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BitmapFont(final Context pContext, final String pAssetPath) {
		InputStream in = null;
		try {
			in = pContext.getAssets().open(pAssetPath);
			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in), StreamUtils.IO_BUFFER_SIZE);

			final String assetBasePath;
			if(pAssetPath.indexOf('/') == -1) {
				assetBasePath = "";
			} else {
				assetBasePath = pAssetPath.substring(pAssetPath.lastIndexOf('/') + 1);
			}

			/* Info. */
			{
				this.mBitmapFontInfo = new BitmapFontInfo(bufferedReader.readLine());
			}

			/* Common. */
			{
				final String[] commonAttributes = StringUtils.SPLITPATTERN_SPACE.split(bufferedReader.readLine(), BitmapFont.TAG_COMMON_ATTRIBUTECOUNT + 1);
				if(commonAttributes.length - 1 != BitmapFont.TAG_COMMON_ATTRIBUTECOUNT) {
					throw new FontException("Expected: '" + BitmapFont.TAG_COMMON_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_COMMON + " attributes, found: '" + (commonAttributes.length - 1) + "'.");
				}
				if(!commonAttributes[0].equals(BitmapFont.TAG_COMMON)) {
					throw new FontException("Expected: '" + BitmapFont.TAG_COMMON + "' attributes.");
				}
				this.mLineHeight = BitmapFont.getIntAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_LINEHEIGHT_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_LINEHEIGHT);
				this.mBase = BitmapFont.getIntAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_BASE_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_BASE);
				this.mScaleWidth = BitmapFont.getIntAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_SCALEWIDTH_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_SCALEWIDTH);
				this.mScaleHeight = BitmapFont.getIntAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_SCALEHEIGHT_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_SCALEHEIGHT);
				this.mPageCount = BitmapFont.getIntAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_PAGES_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_PAGES);
				this.mPacked = BitmapFont.getBooleanAttribute(commonAttributes, BitmapFont.TAG_COMMON_ATTRIBUTE_PACKED_INDEX, BitmapFont.TAG_COMMON_ATTRIBUTE_PACKED);

				if(this.mPageCount != 1) {
					throw new FontException("Only a single page is supported.");
				}
				this.mBitmapFontPages = new BitmapFontPage[this.mPageCount];

				if(this.mPacked) {
					throw new FontException("Packed is not supported.");
				}
			}

			/* Pages. */
			for(int i = 0; i < this.mPageCount; i++) {
				this.mBitmapFontPages[i] = new BitmapFontPage(pContext, assetBasePath, bufferedReader.readLine());

				final String[] charsAttributes = StringUtils.SPLITPATTERN_SPACE.split(bufferedReader.readLine(), BitmapFont.TAG_CHARS_ATTRIBUTECOUNT + 1);
				if(charsAttributes.length - 1 != BitmapFont.TAG_CHARS_ATTRIBUTECOUNT) {
					throw new FontException("Expected: '" + BitmapFont.TAG_CHARS_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_CHARS + " attributes, found: '" + (charsAttributes.length - 1) + "'.");
				}
				if(!charsAttributes[0].equals(BitmapFont.TAG_CHARS)) {
					throw new FontException("Expected: '" + BitmapFont.TAG_CHARS + "' attributes.");
				}

				final int characterCount = BitmapFont.getIntAttribute(charsAttributes, BitmapFont.TAG_CHARS_ATTRIBUTE_COUNT_INDEX, BitmapFont.TAG_CHARS_ATTRIBUTE_COUNT);

				this.parseCharacters(this.mBitmapFontPages[i], characterCount, bufferedReader);
			}
		} catch (final IOException e) {
			Debug.e("Failed loading BitmapFont. AssetPath: " + pAssetPath, e);
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

	public int getLineHeight() {
		return this.mLineHeight;
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

	public int getPageCount() {
		return this.mPageCount;
	}

	public boolean isPacked() {
		return this.mPacked;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private void parseCharacters(final BitmapFontPage pBitmapFontPage, final int pCharacterCount, final BufferedReader pBufferedReader) throws IOException {
		for(int i = pCharacterCount; i >= 0; i--) {
			final String[] charAttributes = StringUtils.SPLITPATTERN_SPACE.split(pBufferedReader.readLine(), BitmapFont.TAG_CHAR_ATTRIBUTECOUNT + 1);
			if(charAttributes.length - 1 != BitmapFont.TAG_CHAR_ATTRIBUTECOUNT) {
				throw new FontException("Expected: '" + BitmapFont.TAG_CHAR_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_CHAR + " attributes, found: '" + (charAttributes.length - 1) + "'.");
			}
			if(!charAttributes[0].equals(BitmapFont.TAG_CHAR)) {
				throw new FontException("Expected: '" + BitmapFont.TAG_CHAR + "' attributes.");
			}

			final int id = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_ID_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_ID);
			final int x = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_X_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_X);
			final int y = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_Y_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_Y);
			final int height = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_WIDTH_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_HEIGHT);
			final int width = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_HEIGHT_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_WIDTH);
			final int xOffset = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_XOFFSET_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_XOFFSET);
			final int yOffset = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_YOFFSET_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_YOFFSET);
			final int xAdvance = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_XADVANCE_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_XADVANCE);
//			final int page = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_PAGE_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_PAGE);
//			final int channel = BitmapFont.getIntAttribute(charAttributes, BitmapFont.TAG_CHAR_ATTRIBUTE_CHANNEL_INDEX, BitmapFont.TAG_CHAR_ATTRIBUTE_CHANNEL);

			final ITexture bitmapFontPageTexture = pBitmapFontPage.getTexture();
			final int textureWidth = bitmapFontPageTexture.getWidth();
			final int textureHeight = bitmapFontPageTexture.getHeight();

			final float u = x / textureWidth;
			final float v = y / textureHeight;
			final float u2 = (x + width) / textureWidth;
			final float v2 = (y + height) / textureHeight;

			this.mCharacterToLetterMap.put(id, new Letter((char)id, x, y, width, height, xAdvance, xOffset, yOffset, u, v, u2, v2));
		}
	}

	private static boolean getBooleanAttribute(final String[] pData, final int pPosition, final String pAttribute) {
		final String data = pData[pPosition];
		final int attributeLength = pAttribute.length();

		if(!data.startsWith(pAttribute)) {
			if(data.charAt(attributeLength) != '=') {
				throw new FontException("Expected '" + pAttribute + "' at position '" + pPosition + "', but found: '" + data + "'.");
			}
		}

		return Integer.parseInt(data.substring(attributeLength + 1)) != 0;
	}

	private static int getIntAttribute(final String[] pData, final int pPosition, final String pAttribute) {
		final String data = pData[pPosition];
		final int attributeLength = pAttribute.length();

		if(!data.startsWith(pAttribute)) {
			if(data.charAt(attributeLength) != '=') {
				throw new FontException("Expected '" + pAttribute + "' at position '" + pPosition + "', but found: '" + data + "'.");
			}
		}

		return Integer.parseInt(data.substring(attributeLength + 1));
	}

	private static String getStringAttribute(final String[] pData, final int pPosition, final String pAttribute) {
		final String data = pData[pPosition];
		final int attributeLength = pAttribute.length();

		if(!data.startsWith(pAttribute)) {
			if(data.charAt(attributeLength) != '=') {
				throw new FontException("Expected '" + pAttribute + "' at position '" + pPosition + "', but found: '" + data + "'.");
			}
		}

		return data.substring(attributeLength + 1, data.length() - 1);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class BitmapFontInfo {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final int LEFT_INDEX = 0;
		private static final int TOP_INDEX = BitmapFontInfo.LEFT_INDEX + 1;
		private static final int RIGHT_INDEX = BitmapFontInfo.TOP_INDEX + 1;
		private static final int BOTTOM_INDEX = BitmapFontInfo.RIGHT_INDEX + 1;

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

		private final int mSpacingLeft;
		private final int mSpacingTop;
		private final int mSpacingRight;
		private final int mSpacingBottom;

		// ===========================================================
		// Constructors
		// ===========================================================

		public BitmapFontInfo(final String pData) throws FontException {
			if(pData == null) {
				throw new FontException("pData must not be null.");
			}

			final String[] infoAttributes = StringUtils.SPLITPATTERN_SPACE.split(pData, BitmapFont.TAG_INFO_ATTRIBUTECOUNT + 1);

			if(infoAttributes.length - 1 != BitmapFont.TAG_INFO_ATTRIBUTECOUNT) {
				throw new FontException("Expected: '" + BitmapFont.TAG_INFO_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_INFO + " attributes, found: '" + (infoAttributes.length - 1) + "'.");
			}
			if(!infoAttributes[0].equals(BitmapFont.TAG_INFO)) {
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

			final String padding = BitmapFont.getStringAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_PADDING_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_PADDING);
			final String[] paddings = StringUtils.SPLITPATTERN_COMMA.split(padding);
			this.mPaddingLeft = Integer.parseInt(paddings[BitmapFontInfo.LEFT_INDEX]);
			this.mPaddingTop = Integer.parseInt(paddings[BitmapFontInfo.TOP_INDEX]);
			this.mPaddingRight = Integer.parseInt(paddings[BitmapFontInfo.RIGHT_INDEX]);
			this.mPaddingBottom = Integer.parseInt(paddings[BitmapFontInfo.BOTTOM_INDEX]);

			final String spacing = BitmapFont.getStringAttribute(infoAttributes, BitmapFont.TAG_INFO_ATTRIBUTE_SPACING_INDEX, BitmapFont.TAG_INFO_ATTRIBUTE_SPACING);
			final String[] spacings = StringUtils.SPLITPATTERN_COMMA.split(spacing);
			this.mSpacingLeft = Integer.parseInt(spacings[BitmapFontInfo.LEFT_INDEX]);
			this.mSpacingTop = Integer.parseInt(spacings[BitmapFontInfo.TOP_INDEX]);
			this.mSpacingRight = Integer.parseInt(spacings[BitmapFontInfo.RIGHT_INDEX]);
			this.mSpacingBottom = Integer.parseInt(spacings[BitmapFontInfo.BOTTOM_INDEX]);
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

		public int getSpacingLeft() {
			return this.mSpacingLeft;
		}

		public int getSpacingTop() {
			return this.mSpacingTop;
		}

		public int getSpacingRight() {
			return this.mSpacingRight;
		}

		public int getSpacingBottom() {
			return this.mSpacingBottom;
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

	public static class BitmapFontPage {
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

		public BitmapFontPage(final Context pContext, final String pAssetBasePath, final String pData) throws IOException {
			final String[] pageAttributes = StringUtils.SPLITPATTERN_SPACE.split(pData, BitmapFont.TAG_PAGE_ATTRIBUTECOUNT + 1);

			if(pageAttributes.length - 1 != BitmapFont.TAG_PAGE_ATTRIBUTECOUNT) {
				throw new FontException("Expected: '" + BitmapFont.TAG_PAGE_ATTRIBUTECOUNT + "' " + BitmapFont.TAG_PAGE + " attributes, found: '" + (pageAttributes.length - 1) + "'.");
			}
			if(!pageAttributes[0].equals(BitmapFont.TAG_PAGE)) {
				throw new FontException("Expected: '" + BitmapFont.TAG_PAGE + "' attributes.");
			}

			this.mID = BitmapFont.getIntAttribute(pageAttributes, BitmapFont.TAG_PAGE_ATTRIBUTE_ID_INDEX, BitmapFont.TAG_PAGE_ATTRIBUTE_ID);
			final String file = BitmapFont.getStringAttribute(pageAttributes, BitmapFont.TAG_PAGE_ATTRIBUTE_FILE_INDEX, BitmapFont.TAG_PAGE_ATTRIBUTE_FILE);

			final String assetPath = pAssetBasePath + file;
			this.mTexture = new BitmapTexture() {
				@Override
				protected InputStream onGetInputStream() throws IOException {
					return pContext.getAssets().open(assetPath);
				}
			};

			TextureManager.loadTexture(this.mTexture);
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
