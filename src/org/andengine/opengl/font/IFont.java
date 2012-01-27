package org.andengine.opengl.font;

import org.andengine.opengl.font.exception.LetterNotFoundException;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureManager;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:15:25 - 03.11.2011
 */
public interface IFont {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @return itself for method chaining.
	 */
	public IFont load(final TextureManager pTextureManager, final FontManager pFontManager);
	/**
	 * @return itself for method chaining.
	 */
	public IFont unload(final TextureManager pTextureManager, final FontManager pFontManager);

	public ITexture getTexture();

	public float getLineHeight();

	public Letter getLetter(final char pChar) throws LetterNotFoundException;
}
