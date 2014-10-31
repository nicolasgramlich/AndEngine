package org.andengine.examples.adt.messages.server;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:59:39 - 21.05.2011
 */
public interface ServerMessageFlags {
	// ===========================================================
	// Final Fields
	// ===========================================================

	/* Connection Flags. */
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_CLOSE = Short.MIN_VALUE;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED = FLAG_MESSAGE_SERVER_CONNECTION_CLOSE + 1;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH = FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED + 1;
	public static final short FLAG_MESSAGE_SERVER_CONNECTION_PONG = FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH + 1;

	// ===========================================================
	// Methods
	// ===========================================================
}
