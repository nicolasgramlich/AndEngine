package org.andengine.opengl.font;

import org.andengine.opengl.font.exception.LetterNotFoundException;
import org.andengine.opengl.texture.ITexture;

/**
 * A font for drawing text to the screen<br>
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


	public void load();
	public void unload();

	public ITexture getTexture();

	/**
	 * 
	 * @return The height of one line of text 
	 */
	public float getLineHeight();

	/**
	 * 
	 * @param pChar The character that is mapped to a {@link Letter}
	 * @return The {@link Letter} of this font that corresponds to the character given
	 * @throws LetterNotFoundException
	 */
	public Letter getLetter(final char pChar) throws LetterNotFoundException;
}
