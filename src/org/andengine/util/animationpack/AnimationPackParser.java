package org.andengine.util.animationpack;

import java.util.ArrayList;

import org.andengine.entity.sprite.IAnimationData;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.list.LongArrayList;
import org.andengine.util.animationpack.exception.AnimationPackParseException;
import org.andengine.util.texturepack.TexturePack;
import org.andengine.util.texturepack.TexturePackLibrary;
import org.andengine.util.texturepack.TexturePackLoader;
import org.andengine.util.texturepack.TexturePackTextureRegion;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.res.AssetManager;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:19:26 - 29.07.2011
 */
public class AnimationPackParser extends DefaultHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String TAG_ANIMATIONPACK = "animationpack";
	private static final String TAG_ANIMATIONPACK_ATTRIBUTE_VERSION = "version";

	private static final String TAG_TEXTUREPACKS = "texturepacks";

	private static final String TAG_TEXTUREPACK = "texturepack";
	private static final String TAG_TEXTUREPACK_ATTRIBUTE_FILENAME = "filename";

	private static final String TAG_ANIMATIONS = "animations";

	private static final String TAG_ANIMATION = "animation";
	private static final String TAG_ANIMATION_ATTRIBUTE_NAME = "name";
	private static final String TAG_ANIMATION_ATTRIBUTE_LOOPCOUNT = "loopcount";

	private static final String TAG_ANIMATIONFRAME = "animationframe";
	private static final String TAG_ANIMATIONFRAME_ATTRIBUTE_DURATION = "duration";
	private static final String TAG_ANIMATIONFRAME_ATTRIBUTE_TEXTUREREGION = "textureregion";

	// ===========================================================
	// Fields
	// ===========================================================

	private final AssetManager mAssetManager;
	private final String mAssetBasePath;
	private final TextureManager mTextureManager;

	private AnimationPack mAnimationPack;
	private AnimationPackTiledTextureRegionLibrary mAnimationPackTiledTextureRegionLibrary;
	private TexturePackLibrary mTexturePackLibrary;
	private TexturePackLoader mTexturePackLoader;

	private String mCurrentAnimationName;
	private int mCurrentAnimationLoopCount = IAnimationData.LOOP_CONTINUOUS;
	private final LongArrayList mCurrentAnimationFrameDurations = new LongArrayList();
	private final ArrayList<TexturePackTextureRegion> mCurrentAnimationFrameTexturePackTextureRegions = new ArrayList<TexturePackTextureRegion>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimationPackParser(final AssetManager pAssetManager, final String pAssetBasePath, final TextureManager pTextureManager) {
		this.mAssetManager = pAssetManager;
		this.mAssetBasePath = pAssetBasePath;
		this.mTextureManager = pTextureManager;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public AnimationPack getAnimationPack() {
		return this.mAnimationPack;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void startElement(final String pUri, final String pLocalName, final String pQualifiedName, final Attributes pAttributes) throws SAXException {
		if (pLocalName.equals(AnimationPackParser.TAG_ANIMATIONPACK)) {
			final int version = SAXUtils.getIntAttributeOrThrow(pAttributes, AnimationPackParser.TAG_ANIMATIONPACK_ATTRIBUTE_VERSION);
			if (version != 1) {
				throw new AnimationPackParseException("Unexpected version: '" + version + "'.");
			}

			this.mTexturePackLoader = new TexturePackLoader(this.mAssetManager, this.mTextureManager);
			this.mTexturePackLibrary = new TexturePackLibrary();
			this.mAnimationPackTiledTextureRegionLibrary = new AnimationPackTiledTextureRegionLibrary();
			this.mAnimationPack = new AnimationPack(this.mTexturePackLibrary, this.mAnimationPackTiledTextureRegionLibrary);
		} else if (pLocalName.equals(AnimationPackParser.TAG_TEXTUREPACKS)) {
			/* Nothing. */
		} else if (pLocalName.equals(AnimationPackParser.TAG_TEXTUREPACK)) {
			final String texturePackName = SAXUtils.getAttributeOrThrow(pAttributes, AnimationPackParser.TAG_TEXTUREPACK_ATTRIBUTE_FILENAME);
			final String texturePackPath = this.mAssetBasePath + texturePackName;

			final TexturePack texturePack = this.mTexturePackLoader.loadFromAsset(texturePackPath, this.mAssetBasePath);
			this.mTexturePackLibrary.put(texturePackName, texturePack);
			texturePack.loadTexture();
		} else if (pLocalName.equals(AnimationPackParser.TAG_ANIMATIONS)) {
			/* Nothing. */
		} else if (pLocalName.equals(AnimationPackParser.TAG_ANIMATION)) {
			this.mCurrentAnimationName = SAXUtils.getAttributeOrThrow(pAttributes, AnimationPackParser.TAG_ANIMATION_ATTRIBUTE_NAME);
			this.mCurrentAnimationLoopCount = SAXUtils.getIntAttribute(pAttributes, AnimationPackParser.TAG_ANIMATION_ATTRIBUTE_LOOPCOUNT, IAnimationData.LOOP_CONTINUOUS);
		} else if (pLocalName.equals(AnimationPackParser.TAG_ANIMATIONFRAME)) {
			final int duration = SAXUtils.getIntAttributeOrThrow(pAttributes, AnimationPackParser.TAG_ANIMATIONFRAME_ATTRIBUTE_DURATION);
			this.mCurrentAnimationFrameDurations.add(duration);

			final String textureRegionName = SAXUtils.getAttributeOrThrow(pAttributes, AnimationPackParser.TAG_ANIMATIONFRAME_ATTRIBUTE_TEXTUREREGION);
			final TexturePackTextureRegion texturePackTextureRegion = this.mTexturePackLibrary.getTexturePackTextureRegion(textureRegionName);
			this.mCurrentAnimationFrameTexturePackTextureRegions.add(texturePackTextureRegion);
		} else {
			throw new AnimationPackParseException("Unexpected tag: '" + pLocalName + "'.");
		}
	}

	@Override
	public void endElement(final String pUri, final String pLocalName, final String pQualifiedName) throws SAXException {
		if (pLocalName.equals(AnimationPackParser.TAG_ANIMATIONPACK)) {
			/* Nothing. */
		} else if (pLocalName.equals(AnimationPackParser.TAG_TEXTUREPACKS)) {
			/* Nothing. */
		} else if (pLocalName.equals(AnimationPackParser.TAG_TEXTUREPACK)) {
			/* Nothing. */
		} else if (pLocalName.equals(AnimationPackParser.TAG_ANIMATIONS)) {
			/* Nothing. */
		} else if (pLocalName.equals(AnimationPackParser.TAG_ANIMATION)) {
			final int currentAnimationFrameFrameCount = this.mCurrentAnimationFrameDurations.size();
			final long[] frameDurations = this.mCurrentAnimationFrameDurations.toArray();
			final TexturePackTextureRegion[] textureRegions = new TexturePackTextureRegion[currentAnimationFrameFrameCount];
			this.mCurrentAnimationFrameTexturePackTextureRegions.toArray(textureRegions);

			final AnimationPackTiledTextureRegion animationPackTiledTextureRegion = new AnimationPackTiledTextureRegion(this.mCurrentAnimationName, frameDurations, this.mCurrentAnimationLoopCount, textureRegions[0].getTexture(), textureRegions);
			this.mAnimationPackTiledTextureRegionLibrary.put(animationPackTiledTextureRegion);

			this.mCurrentAnimationName = null;
			this.mCurrentAnimationLoopCount = IAnimationData.LOOP_CONTINUOUS;
			this.mCurrentAnimationFrameDurations.clear();
			this.mCurrentAnimationFrameTexturePackTextureRegions.clear();
		} else if (pLocalName.equals(AnimationPackParser.TAG_ANIMATIONFRAME)) {
			/* Nothing. */
		} else {
			throw new AnimationPackParseException("Unexpected end tag: '" + pLocalName + "'.");
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
