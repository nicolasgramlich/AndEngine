package org.anddev.andengine.opengl.texture;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author Nicolas Gramlich
 * @since 15:01:03 - 11.07.2011
 */
public interface ITexture<T extends ITextureSource> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public int getWidth();
	public int getHeight();
	
	public int getHardwareTextureID();

	public boolean isLoadedToHardware();
	public void setLoadedToHardware(final boolean pLoadedToHardware);

	public boolean isUpdateOnHardwareNeeded();
	public void setUpdateOnHardwareNeeded(final boolean pUpdateOnHardwareNeeded);

	public void loadToHardware(final GL10 pGL) throws IOException;
	public void unloadFromHardware(final GL10 pGL);
	public void reloadToHardware(final GL10 pGL) throws IOException;

	public void bind(final GL10 pGL);

	public TextureOptions getTextureOptions();
	
	public void addTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException;
	public void removeTextureSource(final T pTextureSource, final int pTexturePositionX, final int pTexturePositionY);
	public void clearTextureSources();
}