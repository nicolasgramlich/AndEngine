package org.andengine.extension.texturepacker.opengl.texture.util.texturepacker;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.compressed.etc1.ETC1Texture;
import org.andengine.opengl.texture.compressed.pvr.PVRCCZTexture;
import org.andengine.opengl.texture.compressed.pvr.PVRGZTexture;
import org.andengine.opengl.texture.compressed.pvr.PVRTexture;
import org.andengine.opengl.texture.compressed.pvr.PVRTexture.PVRTextureFormat;
import org.andengine.opengl.texture.compressed.pvr.pixelbufferstrategy.SmartPVRTexturePixelBufferStrategy;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.DataConstants;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:19:26 - 29.07.2011
 */
public abstract class TexturePackParser extends DefaultHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String TAG_TEXTURE = "texture";
	private static final String TAG_TEXTURE_ATTRIBUTE_VERSION = "version";
	private static final String TAG_TEXTURE_ATTRIBUTE_FILE = "file";
//	private static final String TAG_TEXTURE_ATTRIBUTE_WIDTH = "width";
//	private static final String TAG_TEXTURE_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_TEXTURE_ATTRIBUTE_MINFILTER = "minfilter";
	private static final String TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_NEAREST = "nearest";
	private static final String TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_LINEAR = "linear";
	private static final String TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_LINEAR_MIPMAP_LINEAR = "linear_mipmap_linear";
	private static final String TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_LINEAR_MIPMAP_NEAREST = "linear_mipmap_nearest";
	private static final String TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_NEAREST_MIPMAP_LINEAR = "nearest_mipmap_linear";
	private static final String TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_NEAREST_MIPMAP_NEAREST = "nearest_mipmap_nearest";
	private static final String TAG_TEXTURE_ATTRIBUTE_MAGFILTER = "magfilter";
	private static final String TAG_TEXTURE_ATTRIBUTE_MAGFILTER_VALUE_NEAREST = "nearest";
	private static final String TAG_TEXTURE_ATTRIBUTE_MAGFILTER_VALUE_LINEAR = "linear";
	private static final String TAG_TEXTURE_ATTRIBUTE_WRAPT = "wrapt";
	private static final String TAG_TEXTURE_ATTRIBUTE_WRAPS = "wraps";
	private static final String TAG_TEXTURE_ATTRIBUTE_WRAP_VALUE_CLAMP = "clamp";
	private static final String TAG_TEXTURE_ATTRIBUTE_WRAP_VALUE_CLAMP_TO_EDGE = "clamp_to_edge";
	private static final String TAG_TEXTURE_ATTRIBUTE_WRAP_VALUE_REPEAT = "repeat";
	private static final String TAG_TEXTURE_ATTRIBUTE_PREMULTIPLYALPHA = "premultiplyalpha";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE = "type";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVRCCZ = "pvrccz";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVRGZ = "pvrgz";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVR = "pvr";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_BITMAP = "bitmap";
	private static final String TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_ETC1 = "etc1";
	private static final String TAG_TEXTURE_ATTRIBUTE_PIXELFORMAT = "pixelformat";

	private static final String TAG_TEXTUREREGION = "textureregion";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_ID = "id";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_X = "x";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_Y = "y";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_ROTATED = "rotated";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_TRIMMED = "trimmed";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_SOURCE = "src";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_X = "srcx";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_Y = "srcy";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_WIDTH = "srcwidth";
	private static final String TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_HEIGHT = "srcheight";

	// ===========================================================
	// Fields
	// ===========================================================

	private final TextureManager mTextureManager;

	private TexturePack mTexturePack;
	private TexturePackTextureRegionLibrary mTextureRegionLibrary;
	private ITexture mTexture;
	private int mVersion;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePackParser(final TextureManager pTextureManager) {
		mTextureManager = pTextureManager;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public TexturePack getTexturePack() {
		return this.mTexturePack;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	protected abstract InputStream onGetInputStream(final String pFilename) throws IOException;

	@Override
	public void startElement(final String pUri, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException {
		if(pLocalName.equals(TexturePackParser.TAG_TEXTURE)) {
			this.mVersion = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTURE_ATTRIBUTE_VERSION);
			this.mTexture = this.parseTexture(pAttributes);
			this.mTextureRegionLibrary = new TexturePackTextureRegionLibrary(10);

			this.mTexturePack = new TexturePack(this.mTexture, this.mTextureRegionLibrary);
		} else if(pLocalName.equals(TexturePackParser.TAG_TEXTUREREGION)) {
			final int id = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTUREREGION_ATTRIBUTE_ID);
			final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTUREREGION_ATTRIBUTE_X);
			final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTUREREGION_ATTRIBUTE_Y);
			final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTUREREGION_ATTRIBUTE_WIDTH);
			final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTUREREGION_ATTRIBUTE_HEIGHT);
			
			final String source = SAXUtils.getAttributeOrThrow(pAttributes, TAG_TEXTUREREGION_ATTRIBUTE_SOURCE);

			// TODO Not sure how trimming could be transparently supported...
			final boolean trimmed = SAXUtils.getBooleanAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTUREREGION_ATTRIBUTE_TRIMMED);
			final boolean rotated = SAXUtils.getBooleanAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTUREREGION_ATTRIBUTE_ROTATED);
			final int sourceX = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_X);
			final int sourceY = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_Y);
			final int sourceWidth = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_WIDTH);
			final int sourceHeight = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_TEXTUREREGION_ATTRIBUTE_SOURCE_HEIGHT);

			this.mTextureRegionLibrary.add(new TexturePackerTextureRegion(this.mTexture, x, y, width, height, id, source, rotated, trimmed, sourceX, sourceY, sourceWidth, sourceHeight));
		} else {
			throw new TexturePackParseException("Unexpected end tag: '" + pLocalName + "'.");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private ITexture parseTexture(final Attributes pAttributes) throws TexturePackParseException {
		final String file = SAXUtils.getAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTURE_ATTRIBUTE_FILE);
		final String type = SAXUtils.getAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTURE_ATTRIBUTE_TYPE);
		final PixelFormat pixelFormat = TexturePackParser.parsePixelFormat(pAttributes);

		final TextureOptions textureOptions = this.parseTextureOptions(pAttributes);

		if(type.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_BITMAP)) {
			try {
				return new BitmapTexture(this.mTextureManager, new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return TexturePackParser.this.onGetInputStream(file);
					}
				}, BitmapTextureFormat.fromPixelFormat(pixelFormat), textureOptions);
			} catch (final IOException e) {
				throw new TexturePackParseException(e);
			}
		} else if(type.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVR)) {
			try {
				return new PVRTexture(this.mTextureManager, PVRTextureFormat.fromPixelFormat(pixelFormat), new SmartPVRTexturePixelBufferStrategy(DataConstants.BYTES_PER_MEGABYTE / 8), textureOptions) {
					@Override
					protected InputStream onGetInputStream() throws IOException {
						return TexturePackParser.this.onGetInputStream(file);
					}
				};
			} catch (final IOException e) {
				throw new TexturePackParseException(e);
			}
		} else if(type.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVRGZ)) {
			try {
				return new PVRGZTexture(this.mTextureManager, PVRTextureFormat.fromPixelFormat(pixelFormat), new SmartPVRTexturePixelBufferStrategy(DataConstants.BYTES_PER_MEGABYTE / 8), textureOptions) {
					@Override
					protected InputStream onGetInputStream() throws IOException {
						return TexturePackParser.this.onGetInputStream(file);
					}
				};
			} catch (final IOException e) {
				throw new TexturePackParseException(e);
			}
		} else if(type.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_PVRCCZ)) {
			try {
				return new PVRCCZTexture(this.mTextureManager, PVRTextureFormat.fromPixelFormat(pixelFormat), new SmartPVRTexturePixelBufferStrategy(DataConstants.BYTES_PER_MEGABYTE / 8), textureOptions) {
					@Override
					protected InputStream onGetInputStream() throws IOException {
						return TexturePackParser.this.onGetInputStream(file);
					}
				};
			} catch (final IOException e) {
				throw new TexturePackParseException(e);
			}
		} else if(type.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_TYPE_VALUE_ETC1)) {
			try {
				return new ETC1Texture(this.mTextureManager, textureOptions) {
					@Override
					protected InputStream getInputStream() throws IOException {
						return TexturePackParser.this.onGetInputStream(file);
					}
				};
			} catch (final IOException e) {
				throw new TexturePackParseException(e);
			}
		} else {
			throw new TexturePackParseException(new IllegalArgumentException("Unsupported pTextureFormat: '" + type + "'."));
		}
	}

	private static PixelFormat parsePixelFormat(final Attributes pAttributes) {
		return PixelFormat.valueOf(SAXUtils.getAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTURE_ATTRIBUTE_PIXELFORMAT));
	}

	private TextureOptions parseTextureOptions(final Attributes pAttributes) {
		final int minFilter = TexturePackParser.parseMinFilter(pAttributes);
		final int magFilter = TexturePackParser.parseMagFilter(pAttributes);
		final int wrapT = this.parseWrapT(pAttributes);
		final int wrapS = this.parseWrapS(pAttributes);
		final boolean preMultiplyAlpha = this.parsePremultiplyalpha(pAttributes);

		return new TextureOptions(minFilter, magFilter, wrapT, wrapS, preMultiplyAlpha);
	}

	private static int parseMinFilter(final Attributes pAttributes) {
		final String minFilter = SAXUtils.getAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MINFILTER);
		if(minFilter.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_NEAREST)) {
			return GL10.GL_NEAREST;
		} else if(minFilter.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_LINEAR)) {
			return GL10.GL_LINEAR;
		} else if(minFilter.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_LINEAR_MIPMAP_LINEAR)) {
			return GL10.GL_LINEAR_MIPMAP_LINEAR;
		} else if(minFilter.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_LINEAR_MIPMAP_NEAREST)) {
			return GL10.GL_LINEAR_MIPMAP_NEAREST;
		} else if(minFilter.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_NEAREST_MIPMAP_LINEAR)) {
			return GL10.GL_NEAREST_MIPMAP_LINEAR;
		} else if(minFilter.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MINFILTER_VALUE_NEAREST_MIPMAP_NEAREST)) {
			return GL10.GL_NEAREST_MIPMAP_NEAREST;
		} else {
			throw new IllegalArgumentException("Unexpected " + TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MINFILTER + " attribute: '" + minFilter + "'.");
		}
	}

	private static int parseMagFilter(final Attributes pAttributes) {
		final String magFilter = SAXUtils.getAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MAGFILTER);
		if(magFilter.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MAGFILTER_VALUE_NEAREST)) {
			return GL10.GL_NEAREST;
		} else if(magFilter.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MAGFILTER_VALUE_LINEAR)) {
			return GL10.GL_LINEAR;
		} else {
			throw new IllegalArgumentException("Unexpected " + TexturePackParser.TAG_TEXTURE_ATTRIBUTE_MAGFILTER + " attribute: '" + magFilter + "'.");
		}
	}

	private int parseWrapT(final Attributes pAttributes) {
		return this.parseWrap(pAttributes, TexturePackParser.TAG_TEXTURE_ATTRIBUTE_WRAPT);
	}

	private int parseWrapS(final Attributes pAttributes) {
		return this.parseWrap(pAttributes, TexturePackParser.TAG_TEXTURE_ATTRIBUTE_WRAPS);
	}

	private int parseWrap(final Attributes pAttributes, final String pWrapAttributeName) {
		final String wrapAttribute = SAXUtils.getAttributeOrThrow(pAttributes, pWrapAttributeName);
		if(this.mVersion == 1 && wrapAttribute.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_WRAP_VALUE_CLAMP)) {
			return GL10.GL_CLAMP_TO_EDGE;
		} else if(wrapAttribute.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_WRAP_VALUE_CLAMP_TO_EDGE)) {
			return GL10.GL_CLAMP_TO_EDGE;
		} else if(wrapAttribute.equals(TexturePackParser.TAG_TEXTURE_ATTRIBUTE_WRAP_VALUE_REPEAT)) {
			return GL10.GL_REPEAT;
		} else {
			throw new IllegalArgumentException("Unexpected " + pWrapAttributeName + " attribute: '" + wrapAttribute + "'.");
		}
	}

	private boolean parsePremultiplyalpha(final Attributes pAttributes) {
		return SAXUtils.getBooleanAttributeOrThrow(pAttributes, TexturePackParser.TAG_TEXTURE_ATTRIBUTE_PREMULTIPLYALPHA);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
