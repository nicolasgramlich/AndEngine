package org.andengine.opengl.texture.atlas.buildable;

import org.andengine.opengl.texture.atlas.ITextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;
import org.andengine.util.call.Callback;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:17:47 - 23.01.2012
 */
public interface IBuildableTextureAtlas<S extends ITextureAtlasSource, T extends ITextureAtlas<S>> extends ITextureAtlas<S>{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Most likely this is not the method you'd want to be using, as the {@link ITextureAtlasSource} won't get built through this {@link BuildableTextureAtlas}.
	 * @deprecated Use {@link BuildableTextureAtlas#addTextureAtlasSource(ITextureAtlasSource)} instead.
	 */
	@Override
	@Deprecated
	public void addTextureAtlasSource(final S pTextureAtlasSource, final int pTextureX, final int pTextureY);

	/**
	 * Most likely this is not the method you'd want to be using, as the {@link ITextureAtlasSource} won't get built through this {@link BuildableTextureAtlas}.
	 * @deprecated Use {@link BuildableTextureAtlas#addTextureAtlasSource(ITextureAtlasSource)} instead.
	 */
	@Override
	@Deprecated
	public void addTextureAtlasSource(final S pTextureAtlasSource, final int pTextureX, final int pTextureY, final int pTextureAtlasSourcePadding);

	/**
	 * When all {@link ITextureAtlasSource}s are added you have to call {@link BuildableBitmapTextureAtlas#build(ITextureAtlasBuilder)}.
	 *
	 * @param pTextureAtlasSource to be added.
	 * @param pCallback
	 */
	public void addTextureAtlasSource(final S pTextureAtlasSource, final Callback<S> pCallback);

	/**
	 * Removes a {@link ITextureAtlasSource} before {@link BuildableBitmapTextureAtlas#build(ITextureAtlasBuilder)} is called.
	 * @param pBitmapTextureAtlasSource to be removed.
	 */
	public void removeTextureAtlasSource(final ITextureAtlasSource pTextureAtlasSource);

	/**
	 * May draw over already added {@link ITextureAtlasSource}.
	 *
	 * @param pTextureAtlasBuilder the {@link ITextureAtlasBuilder} to use for building the {@link ITextureAtlasSource} in this {@link BuildableBitmapTextureAtlas}.
	 * @return itself for method chaining.
	 * @throws TextureAtlasBuilderException i.e. when the {@link ITextureAtlasSource} didn't fit into this {@link BuildableBitmapTextureAtlas}.
	 */
	public IBuildableTextureAtlas<S, T> build(final ITextureAtlasBuilder<S, T> pTextureAtlasBuilder) throws TextureAtlasBuilderException;
}