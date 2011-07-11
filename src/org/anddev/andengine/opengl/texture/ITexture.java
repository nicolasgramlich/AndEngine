package org.anddev.andengine.opengl.texture;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Nicolas Gramlich
 * @since 15:01:03 - 11.07.2011
 */
public interface ITexture {
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
	public boolean isUpdateOnHardwareNeeded();
	public void setLoadedToHardware(final boolean pLoadedToHardware);

	public void loadToHardware(final GL10 pGL) throws IOException;
	public void unloadFromHardware(final GL10 pGL);
	public void reloadToHardware(final GL10 pGL) throws IOException;

	public void bind(final GL10 pGL);

	public TextureOptions getTextureOptions();
}