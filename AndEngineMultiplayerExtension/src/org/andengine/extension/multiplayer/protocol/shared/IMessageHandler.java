package org.andengine.extension.multiplayer.protocol.shared;

import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:57:21 - 04.03.2011
 */
public interface IMessageHandler<C extends Connection, CC extends Connector<C>, M extends IMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onHandleMessage(final CC pConnector, final M pMessage) throws IOException;
}
