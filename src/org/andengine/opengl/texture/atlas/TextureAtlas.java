package org.andengine.opengl.texture.atlas;

import java.util.ArrayList;

import org.andengine.opengl.texture.ITextureStateListener;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:22:55 - 14.07.2011
 */
public abstract class TextureAtlas<T extends ITextureAtlasSource> extends Texture implements ITextureAtlas<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mWidth;
	protected final int mHeight;

	protected final ArrayList<T> mTextureAtlasSources = new ArrayList<T>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureAtlas(final TextureManager pTextureManager, final int pWidth, final int pHeight, final PixelFormat pPixelFormat, final TextureOptions pTextureOptions, final ITextureAtlasStateListener<T> pTextureAtlasStateListener) {
		super(pTextureManager, pPixelFormat, pTextureOptions, pTextureAtlasStateListener);

		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getWidth() {
		return this.mWidth;
	}

	@Override
	public int getHeight() {
		return this.mHeight;
	}

	@Deprecated
	@Override
	public boolean hasTextureStateListener() {
		return super.hasTextureStateListener();
	}

	@Override
	public boolean hasTextureAtlasStateListener() {
		return super.hasTextureStateListener();
	}

	/**
	 * @see {@link ITextureAtlas#setTextureStateListener(ITextureAtlasStateListener)}
	 */
	@Deprecated
	@Override
	public ITextureAtlasStateListener<T> getTextureStateListener() {
		return this.getTextureAtlasStateListener();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ITextureAtlasStateListener<T> getTextureAtlasStateListener() {
		return (ITextureAtlasStateListener<T>) super.getTextureStateListener();
	}

	/**
	 * @see {@link ITextureAtlas#setTextureStateListener(ITextureAtlasStateListener)}
	 */
	@Deprecated
	@Override
	public void setTextureStateListener(final ITextureStateListener pTextureStateListener) {
		super.setTextureStateListener(pTextureStateListener);
	}

	@Override
	public void setTextureAtlasStateListener(final ITextureAtlasStateListener<T> pTextureAtlasStateListener) {
		super.setTextureStateListener(pTextureAtlasStateListener);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY) throws IllegalArgumentException {
		this.checkTextureAtlasSourcePosition(pTextureAtlasSource, pTextureX, pTextureY);
		pTextureAtlasSource.setTextureX(pTextureX);
		pTextureAtlasSource.setTextureY(pTextureY);
		this.mTextureAtlasSources.add(pTextureAtlasSource);
		this.mUpdateOnHardwareNeeded = true;
	}

	@Override
	public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY, final int pTextureAtlasSourcePadding) throws IllegalArgumentException {
		this.addTextureAtlasSource(pTextureAtlasSource, pTextureX, pTextureY);

		if(pTextureAtlasSourcePadding > 0) {
			/* Left padding. */
			if(pTextureX >= pTextureAtlasSourcePadding) {
				this.addEmptyTextureAtlasSource(pTextureX - pTextureAtlasSourcePadding, pTextureY, pTextureAtlasSourcePadding, pTextureAtlasSource.getTextureHeight());
			}

			/* Top padding. */
			if(pTextureY >= pTextureAtlasSourcePadding) {
				this.addEmptyTextureAtlasSource(pTextureX, pTextureY - pTextureAtlasSourcePadding, pTextureAtlasSource.getTextureWidth(), pTextureAtlasSourcePadding);
			}

			/* Right padding. */
			if(pTextureX + pTextureAtlasSource.getTextureWidth() - 1 + pTextureAtlasSourcePadding <= this.getWidth()) {
				this.addEmptyTextureAtlasSource(pTextureX + pTextureAtlasSource.getTextureWidth(), pTextureY, pTextureAtlasSourcePadding, pTextureAtlasSource.getTextureHeight());
			}

			/* Bottom padding. */
			if(pTextureY + pTextureAtlasSource.getTextureHeight() - 1 + pTextureAtlasSourcePadding <= this.getHeight()) {
				this.addEmptyTextureAtlasSource(pTextureX, pTextureY + pTextureAtlasSource.getTextureHeight(), pTextureAtlasSource.getTextureWidth(), pTextureAtlasSourcePadding);
			}
		}
	}

	@Override
	public void removeTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY) {
		final ArrayList<T> textureSources = this.mTextureAtlasSources;
		for(int i = textureSources.size() - 1; i >= 0; i--) {
			final T textureSource = textureSources.get(i);
			if(textureSource == pTextureAtlasSource && textureSource.getTextureX() == pTextureX && textureSource.getTextureY() == pTextureY) {
				textureSources.remove(i);
				this.mUpdateOnHardwareNeeded = true;
				return;
			}
		}
	}

	@Override
	public void clearTextureAtlasSources() {
		this.mTextureAtlasSources.clear();
		this.mUpdateOnHardwareNeeded = true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void checkTextureAtlasSourcePosition(final T pTextureAtlasSource, final int pTextureX, final int pTextureY) throws IllegalArgumentException {
		if(pTextureX < 0) {
			throw new IllegalArgumentException("Illegal negative pTextureX supplied: '" + pTextureX + "'");
		} else if(pTextureY < 0) {
			throw new IllegalArgumentException("Illegal negative pTextureY supplied: '" + pTextureY + "'");
		} else if(pTextureX + pTextureAtlasSource.getTextureWidth() > this.getWidth() || pTextureY + pTextureAtlasSource.getTextureHeight() > this.getHeight()) {
			throw new IllegalArgumentException("Supplied pTextureAtlasSource must not exceed bounds of Texture.");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
