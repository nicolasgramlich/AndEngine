package org.anddev.andengine.entity.layer.tiled.tmx;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;

/**
 * @author Nicolas Gramlich
 * @since 19:10:45 - 20.07.2010
 */
public class TMXLoader {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public TMXTiledMap load(final Context pContext, final InputStream pInputStream) throws IOException, SAXException, ParserConfigurationException {
		final SAXParserFactory spf = SAXParserFactory.newInstance();
		final SAXParser sp = spf.newSAXParser();

		final XMLReader xr = sp.getXMLReader();
		final TMXParser tmxParser = new TMXParser();
		xr.setContentHandler(tmxParser);

		xr.parse(new InputSource(new BufferedInputStream(pInputStream)));

		return tmxParser.getTMXTiledMap();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
