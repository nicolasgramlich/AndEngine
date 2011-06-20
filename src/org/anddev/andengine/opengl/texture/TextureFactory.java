package org.anddev.andengine.opengl.texture;

import org.anddev.andengine.opengl.texture.Texture.TextureFormat;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.util.MathUtils;

/**
 * @author Nicolas Gramlich
 * @since 09:38:51 - 03.05.2010
 */
public class TextureFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public static Texture createForTextureSourceSize(final TextureFormat pTextureFormat, final TextureRegion pTextureRegion) {
		return TextureFactory.createForTextureRegionSize(pTextureFormat, pTextureRegion, TextureOptions.DEFAULT);
	}

	public static Texture createForTextureRegionSize(final TextureFormat pTextureFormat, final TextureRegion pTextureRegion, final TextureOptions pTextureOptions) {
		final int textureRegionWidth = pTextureRegion.getWidth();
		final int textureRegionHeight = pTextureRegion.getHeight();
		return new Texture(MathUtils.nextPowerOfTwo(textureRegionWidth), MathUtils.nextPowerOfTwo(textureRegionHeight), pTextureFormat, pTextureOptions);
	}

	public static Texture createForTextureSourceSize(final TextureFormat pTextureFormat, final ITextureSource pTextureSource) {
		return TextureFactory.createForTextureSourceSize(pTextureFormat, pTextureSource, TextureOptions.DEFAULT);
	}

	public static Texture createForTextureSourceSize(final TextureFormat pTextureFormat, final ITextureSource pTextureSource, final TextureOptions pTextureOptions) {
		final int textureSourceWidth = pTextureSource.getWidth();
		final int textureSourceHeight = pTextureSource.getHeight();
		return new Texture(MathUtils.nextPowerOfTwo(textureSourceWidth), MathUtils.nextPowerOfTwo(textureSourceHeight), pTextureFormat, pTextureOptions);
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
