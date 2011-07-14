package org.anddev.andengine.opengl.texture.compressed.pvr;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.ITexture.ITextureStateListener;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.Texture.TextureFormat;
import org.anddev.andengine.opengl.texture.TextureOptions;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 16:18:10 - 13.07.2011
 */
public class PVRTexture extends Texture {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int PVR_TEXTURE_FLAG_TYPE_MASK = 0xFF;

	private static final byte[] MAGIC_IDENTIFIER = {
		(byte)'P',
		(byte)'V',
		(byte)'R',
		(byte)'!'
	};

	private static final int FLAG_MIPMAP = (1<<8); // has mip map levels
	private static final int FLAG_TWIDDLE = (1<<9); // is twiddled
	private static final int FLAG_BUMPMAP = (1<<10); // has normals encoded for a bump map
	private static final int FLAG_TILING = (1<<11); // is bordered for tiled pvr
	private static final int FLAG_CUBEMAP = (1<<12); // is a cubemap/skybox
	private static final int FLAG_FALSEMIPCOL = (1<<13); // are there false colored MIP levels
	private static final int FLAG_VOLUME = (1<<14); // is this a volume texture
	private static final int FLAG_ALPHA = (1<<15); // v2.1 is there transparency info in the texture
	private static final int FLAG_VERTICALFLIP = (1<<16); // v2.1 is the texture vertically flipped

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	//	- (id)initWithContentsOfFile:(NSString *)path
	//	{
	//		if((self = [super init]))
	//		{
	//			unsigned char *pvrdata = NULL;
	//			NSInteger pvrlen = 0;
	//			NSString *lowerCase = [path lowercaseString];
	//
	//			if ( [lowerCase hasSuffix:@".ccz"])
	//				pvrlen = ccInflateCCZFile( [path UTF8String], &pvrdata );
	//
	//			else if( [lowerCase hasSuffix:@".gz"] )
	//				pvrlen = ccInflateGZipFile( [path UTF8String], &pvrdata );
	//
	//			else
	//				pvrlen = ccLoadFileIntoMemory( [path UTF8String], &pvrdata );
	//
	//			if( pvrlen < 0 ) {
	//				[self release];
	//				return nil;
	//			}
	//
	//
	//			numberOfMipmaps_ = 0;
	//
	//			name_ = 0;
	//			width_ = height_ = 0;
	//			tableFormatIndex_ = -1;
	//			hasAlpha_ = FALSE;
	//
	//			retainName_ = NO; // cocos2d integration
	//
	//			if( ! [self unpackPVRData:pvrdata PVRLen:pvrlen] || ![self createGLTexture] ) {
	//				free(pvrdata);
	//				[self release];
	//				return nil;
	//			}
	//
	//			free(pvrdata);
	//		}
	//
	//		return self;
	//	}

	public PVRTexture(final int pWidth, final int pHeight, final PVRTextureFormat pPVRTextureFormat) {
		this(pWidth, pHeight, pPVRTextureFormat, TextureOptions.DEFAULT, null);
	}

	public PVRTexture(final int pWidth, final int pHeight, final PVRTextureFormat pPVRTextureFormat, final ITextureStateListener pTextureStateListener) {
		this(pWidth, pHeight, pPVRTextureFormat, TextureOptions.DEFAULT, pTextureStateListener);
	}

	public PVRTexture(final int pWidth, final int pHeight, final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions) throws IllegalArgumentException {
		this(pWidth, pHeight, pPVRTextureFormat, pTextureOptions, null);
	}

	public PVRTexture(final int pWidth, final int pHeight, final PVRTextureFormat pPVRTextureFormat, final TextureOptions pTextureOptions, final ITextureStateListener pTextureStateListener) throws IllegalArgumentException {
		super(pWidth, pHeight, pPVRTextureFormat.getTextureFormat(), pTextureOptions, pTextureStateListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void writeTextureToHardware(final GL10 pGL) throws IOException {
//		final InputStream inputStream = ...
//		pGL.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels)
		// TODO ...
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	static class PVRTexHeader {
		// ===========================================================
		// Constants
		// ===========================================================

		public static final int SIZE = 13 * 4;

		// ===========================================================
		// Fields
		// ===========================================================

		ByteBuffer bb;

		// ===========================================================
		// Constructors
		// ===========================================================


		public PVRTexHeader(final ByteBuffer b) {
			this.bb = b;
			this.bb.rewind();
			this.bb.order(ByteOrder.LITTLE_ENDIAN);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int headerLength() {
			return this.bb.getInt(0 * 4);
		}

		public int height() {
			return this.bb.getInt(1 * 4);
		}

		public int width() {
			return this.bb.getInt(2 * 4);
		}

		public int numMipmaps() {
			return this.bb.getInt(3 * 4);
		}

		public int flags() {
			return this.bb.getInt(4 * 4);
		}

		public int dataLength() {
			return this.bb.getInt(5 * 4);
		}

		public int bpp() {
			return this.bb.getInt(6 * 4);
		}

		public int bitmaskRed() {
			return this.bb.getInt(7 * 4);
		}

		public int bitmaskGreen() {
			return this.bb.getInt(8 * 4);
		}

		public int bitmaskBlue() {
			return this.bb.getInt(9 * 4);
		}

		public int bitmaskAlpha() {
			return this.bb.getInt(10 * 4);
		}

		public int pvrTag() {
			return this.bb.getInt(11 * 4);
		}

		public int numSurfs() {
			return this.bb.getInt(12 * 4);
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

	public static enum PVRTextureFormat {
		// ===========================================================
		// Elements
		// ===========================================================

		RGBA_4444(0x10, false, TextureFormat.RGBA_4444), 
		RGBA_5551(0x11, false, TextureFormat.RGBA_5551), 
		RGBA_8888(0x12, false, TextureFormat.RGBA_8888), 
		RGB_565(0x13, false, TextureFormat.RGB_565),
//		RGB_555( 0x14, ...),
//		RGB_888( 0x15, ...),
		I_8(0x16, false, TextureFormat.I_8), 
		AI_88(0x17, false, TextureFormat.AI_8),
//		PVRTC_2(0x18, GL10.GL_COMPRESSED_RGBA_PVRTC_2BPPV1_IMG, true, TextureFormat.???),
//		PVRTC_4(0x19, GL10.GL_COMPRESSED_RGBA_PVRTC_4BPPV1_IMG, true, TextureFormat.???),
//		BGRA_8888(0x1A, GL10.GL_RGBA, TextureFormat.???),
		A_8(0x1B, false, TextureFormat.A_8);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mID;
		private final boolean mCompressed;
		private final TextureFormat mTextureFormat;

		// ===========================================================
		// Constructors
		// ===========================================================

		private PVRTextureFormat(final int pID, final boolean pCompressed, final org.anddev.andengine.opengl.texture.Texture.TextureFormat pTextureFormat) {
			this.mID = pID;
			this.mCompressed = pCompressed;
			this.mTextureFormat = pTextureFormat;
		}

		public static void fromID(final int pID) {
			// TODO
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getID() {
			return this.mID;
		}

		public boolean isCompressed() {
			return this.mCompressed;
		}

		public TextureFormat getTextureFormat() {
			return this.mTextureFormat;
		}

		// ===========================================================
		// Methods from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
