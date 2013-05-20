package org.andengine.util.animationpack;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.util.StreamUtils;
import org.andengine.util.animationpack.exception.AnimationPackParseException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.res.AssetManager;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:05:15 - 29.07.2011
 */
public class AnimationPackLoader {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final AssetManager mAssetManager;
	private final TextureManager mTextureManager;

	// ===========================================================
	// Constructors
	// ===========================================================

	public AnimationPackLoader(final AssetManager pAssetManager, final TextureManager pTextureManager) {
		this.mAssetManager = pAssetManager;
		this.mTextureManager = pTextureManager;
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

	public AnimationPack loadFromAsset(final String pAssetPath, final String pAssetBasePath) throws AnimationPackParseException {
		try {
			return this.load(this.mAssetManager.open(pAssetPath), pAssetBasePath);
		} catch (final IOException e) {
			throw new AnimationPackParseException("Could not load " + this.getClass().getSimpleName() + " data from asset: " + pAssetPath, e);
		}
	}

	public AnimationPack load(final InputStream pInputStream, final String pAssetBasePath) throws AnimationPackParseException {
		try {
			final SAXParserFactory spf = SAXParserFactory.newInstance();
			final SAXParser sp = spf.newSAXParser();

			final XMLReader xr = sp.getXMLReader();
			final AnimationPackParser animationPackParser = new AnimationPackParser(this.mAssetManager, pAssetBasePath, this.mTextureManager);
			xr.setContentHandler(animationPackParser);

			xr.parse(new InputSource(new BufferedInputStream(pInputStream)));

			return animationPackParser.getAnimationPack();
		} catch (final SAXException e) {
			throw new AnimationPackParseException(e);
		} catch (final ParserConfigurationException pe) {
			/* Doesn't happen. */
			return null;
		} catch (final IOException e) {
			throw new AnimationPackParseException(e);
		} finally {
			StreamUtils.close(pInputStream);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
