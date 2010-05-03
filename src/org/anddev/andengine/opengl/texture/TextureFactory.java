package org.anddev.andengine.opengl.texture;

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
	
	public static Texture createForTextureSourceSize(final ITextureSource pTextureSource) {
		return createForTextureSourceSize(pTextureSource, TextureOptions.DEFAULT);
	}
	
	public static Texture createForTextureSourceSize(final ITextureSource pTextureSource, final TextureOptions pTextureOptions) {
		final int loadingScreenWidth = pTextureSource.getWidth();
		final int loadingScreenHeight = pTextureSource.getHeight();
		return new Texture(MathUtils.nextPowerOfTwo(loadingScreenWidth), MathUtils.nextPowerOfTwo(loadingScreenHeight), pTextureOptions);
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
