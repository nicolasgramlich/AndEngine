package org.andengine.util.level;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

/**
 * (c) 2012 Michal Stawinski
 * 
 * @author Michal Stawinski <michal.stawinski@gmail.com>
 * @since 22:41:08 - 14.07.2012
 */
public interface ILevelEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	/**
	 * Used when dumping level with {@link LevelLoaderUtils#dumpChildren(org.andengine.entity.IEntity, XmlSerializer)}
	 * Fills given {@link XmlSerializer} with all parameters that should be saved. You can use for example
	 * {@link LevelLoaderUtils#dumpAttributePosition(org.andengine.entity.Entity, XmlSerializer)}, or work with bare {@link XmlSerializer}.
	 * 
	 * @param pSerializer
	 * @throws IOException
	 */
	public void fillLevelTag(XmlSerializer pSerializer) throws IOException;
	/**
	 * @return Tag which will be used when working with {@link LevelLoaderUtils#dumpChildren(org.andengine.entity.IEntity, XmlSerializer)}
	 */
	public String getLevelTagName();
}