package org.anddev.andengine.opengl.font;

import org.anddev.andengine.opengl.font.exception.LetterNotFoundException;
import org.anddev.andengine.opengl.texture.ITexture;

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
	public IFont load();
	/**
	 * @return itself for method chaining.
	 */
	public IFont unload();

	public ITexture getTexture();

	public float getLineHeight();
	public float getStringWidth(final String pString);

	public Letter getLetter(final char pChar) throws LetterNotFoundException;
}
