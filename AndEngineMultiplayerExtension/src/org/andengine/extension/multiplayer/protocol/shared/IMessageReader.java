package org.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:50:53 - 04.03.2011
 */
public interface IMessageReader<C extends Connection, CC extends Connector<C>, M extends IMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerMessage(final short pFlag, final Class<? extends M> pMessageClass);
	public void registerMessageHandler(final short pFlag, final IMessageHandler<C, CC, M> pMessageHandler);
	public void registerMessage(final short pFlag, final Class<? extends M> pMessageClass, final IMessageHandler<C, CC, M> pMessageHandler);

	public M readMessage(final DataInputStream pDataInputStream) throws IOException;

	public void handleMessage(final CC pConnector, final M pMessage) throws IOException;

	public void recycleMessage(final M pMessage);
}
