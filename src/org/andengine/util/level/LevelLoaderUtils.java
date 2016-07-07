package org.andengine.util.level;

import java.io.IOException;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.IEntityParameterCallable;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;
import org.xmlpull.v1.XmlSerializer;

/**
 * (c) 2012 Michal Stawinski
 * 
 * @author Michal Stawinski <michal.stawinski@gmail.com>
 * @since 22:41:08 - 14.07.2012
 */
public class LevelLoaderUtils {
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

	/**
	 * Dumps to {@link XmlSerializer} all children of a given {@link IEntity} that can be serialized, i.e. they implement
	 * {@link ILevelEntity}. This can be used to save whole Scene to a file by calling {@link LevelLoaderUtils#dumpChildren(IEntity, XmlSerializer)}
	 * on a Scene that extends {@link ILevelEntity}.
	 * 
	 * @param pEntity
	 * @param pSerializer
	 */
	static public void dumpChildren(final IEntity pEntity, final XmlSerializer pSerializer) {
		pEntity.callOnChildren(new IEntityParameterCallable() {
			@Override
			public void call(final IEntity pEntity) {
				final ILevelEntity child = (ILevelEntity) pEntity;
				try {
					pSerializer.startTag("", child.getLevelTagName());
					child.fillLevelTag(pSerializer);
					pSerializer.endTag("", child.getLevelTagName());
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			}
		}, new IEntityMatcher() {
			@Override
			public boolean matches(final IEntity pEntity) {
				return (pEntity instanceof ILevelEntity);
			}
		});
	}

	static public void dumpAttributePosition(final Entity pEntity, final XmlSerializer pSerializer) throws IOException {
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_POSITION_X, "" + pEntity.getX());
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_POSITION_Y, "" + pEntity.getY());
	}

	static public void dumpAttributeSize(final Entity pEntity, final XmlSerializer pSerializer) throws IOException {
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_WIDTH, "" + pEntity.getWidth());
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_HEIGHT, "" + pEntity.getHeight());
	}

	static public void dumpAttributeScale(final Entity pEntity, final XmlSerializer pSerializer) throws IOException {
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_SCALE_X, "" + pEntity.getScaleX());
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_SCALE_Y, "" + pEntity.getScaleY());
	}

	static public void dumpAttributeRotation(final Entity pEntity, final XmlSerializer pSerializer) throws IOException {
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_ROTATION, "" + pEntity.getRotation());
	}

	static public void dumpAttributeZindex(final Entity pEntity, final XmlSerializer pSerializer) throws IOException {
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_ZINDEX, "" + pEntity.getZIndex());
	}

	static public void dumpAttributeColor(final Entity pEntity, final XmlSerializer pSerializer) throws IOException {
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_COLOR_R, "" + pEntity.getRed());
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_COLOR_G, "" + pEntity.getGreen());
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_COLOR_B, "" + pEntity.getBlue());
	}

	static public void dumpAttributeFlipped(final Sprite pSprite, final XmlSerializer pSerializer) throws IOException {
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_FLIP_VER, "" + pSprite.isFlippedVertical());
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_FLIP_HOR, "" + pSprite.isFlippedHorizontal());
	}

	static public void dumpAttributeBlending(final IShape pShape, final XmlSerializer pSerializer) throws IOException {
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_BLEND_SOURCE, "" + pShape.getBlendFunctionSource());
		pSerializer.attribute("", LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_BLEND_DESTINATION, "" + pShape.getBlendFunctionDestination());
	}

	/**
	 * Sets position (x and y) of given {@link IEntity} according to value of {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_POSITION_X} and
	 * {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_POSITION_Y} in given pAttributes
	 * 
	 * @param pAttributes
	 * @param pEntity
	 * @note throws an exception if pAttributes does not contain appropriate tags
	 */
	static public void setPosition(final Attributes pAttributes, final IEntity pEntity) {
		final float x = SAXUtils.getFloatAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_POSITION_X);
		final float y = SAXUtils.getFloatAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_POSITION_Y);
		pEntity.setPosition(x, y);
	}

	/**
	 * Sets size (width and height) of given {@link IEntity} according to value of {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_WIDTH} and
	 * {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_HEIGHT} in given pAttributes
	 * 
	 * @param pAttributes
	 * @param pEntity
	 * @note throws an exception if pAttributes does not contain appropriate tags
	 */
	static public void setSize(final Attributes pAttributes, final IEntity pEntity) {
		final float w = SAXUtils.getFloatAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_WIDTH);
		final float h = SAXUtils.getFloatAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_HEIGHT);
		pEntity.setSize(w, h);
	}

	/**
	 * Sets scale (both x and y) of given {@link IEntity} according to value of {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_SCALE_X} and
	 * {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_SCALE_Y} in given pAttributes
	 * 
	 * @param pAttributes
	 * @param pEntity
	 * @note if pAttributes does not contain appropriate tags scale 1 will be used
	 */
	static public void setScale(final Attributes pAttributes, final IEntity pEntity) {
		final float scalex = SAXUtils.getFloatAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_SCALE_X, 1);
		final float scaley = SAXUtils.getFloatAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_SCALE_Y, 1);
		pEntity.setScale(scalex, scaley);
	}

	/**
	 * Sets size of given {@link IEntity} according to value of {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_ROTATION} in given pAttributes
	 * 
	 * @param pAttributes
	 * @param pEntity
	 * @note if pAttributes does not contain appropriate tag rotation of 0 will be used
	 */
	static public void setRotation(final Attributes pAttributes, final IEntity pEntity) {
		final float rot = SAXUtils.getFloatAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_ROTATION, 0);
		pEntity.setRotation(rot);
	}

	/**
	 * Sets Zindex of given {@link IEntity} according to value of {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_ZINDEX} in given pAttributes
	 * 
	 * @param pAttributes
	 * @param pEntity
	 * @note if pAttributes does not contain appropriate tag zindex of 0 will be used
	 */
	static public void setZindex(final Attributes pAttributes, final IEntity pEntity) {
		final int zindex = SAXUtils.getIntAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_ZINDEX, 0);
		pEntity.setZIndex(zindex);
	}

	/**
	 * Sets color (r, g and b) of given {@link IEntity} according to value of {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_COLOR_R},
	 * {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_COLOR_G} and {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_COLOR_B}
	 * in given pAttributes
	 * 
	 * @param pAttributes
	 * @param pEntity
	 * @note if pAttributes does not contain appropriate tag value of 1 will be used
	 */
	static public void setColor(final Attributes pAttributes, final IEntity pEntity) {
		final float r = SAXUtils.getFloatAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_COLOR_R, 1);
		final float g = SAXUtils.getFloatAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_COLOR_G, 1);
		final float b = SAXUtils.getFloatAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_COLOR_B, 1);
		pEntity.setColor(new Color(r, g, b));
	}

	/**
	 * Sets flipped (vertical and horizontal) of given {@link Sprite} according to value of {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_FLIP_HOR} and
	 * {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_FLIP_VER}
	 * in given pAttributes
	 * 
	 * @param pAttributes
	 * @param pEntity
	 * @note if pAttributes does not contain appropriate tag, false (not flipped) will be used
	 */
	static public void setFlipped(final Attributes pAttributes, final Sprite pSprite) {
		final boolean ver = SAXUtils.getBooleanAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_FLIP_HOR, false);
		final boolean hor = SAXUtils.getBooleanAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_FLIP_VER, false);
		pSprite.setFlipped(hor, ver);
	}

	/**
	 * Sets blending value of given {@link IShape} according to value of {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_BLEND_SOURCE} and
	 * {@link LevelConstants#TAG_LEVEL_ENTITY_ATTRIBUTE_BLEND_DESTINATION}
	 * in given pAttributes
	 * 
	 * @param pAttributes
	 * @param pEntity
	 * @note if pAttributes does not contain appropriate tag, default blending will be used
	 */
	static public void setBlending(final Attributes pAttributes, final IShape pShape) {
		final int src = SAXUtils.getIntAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_BLEND_SOURCE, IShape.BLENDFUNCTION_SOURCE_DEFAULT);
		final int dst = SAXUtils.getIntAttribute(pAttributes, LevelConstants.TAG_LEVEL_ENTITY_ATTRIBUTE_BLEND_DESTINATION, IShape.BLENDFUNCTION_DESTINATION_DEFAULT);
		pShape.setBlendFunction(src, dst);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
