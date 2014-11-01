package org.andengine.examples.adt.messages.client;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:00:00 - 21.05.2011
 */
public interface ClientMessageFlags {
	// ===========================================================
	// Final Fields
	// ===========================================================

	/* Connection Flags. */
	public static final short FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE = Short.MIN_VALUE;
	public static final short FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH = FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE + 1;
	public static final short FLAG_MESSAGE_CLIENT_CONNECTION_PING = FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH + 1;

	// ===========================================================
	// Methods
	// ===========================================================
}
