package org.andengine.extension.multiplayer.protocol.client;

import java.io.DataInputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.shared.Connection;
import org.andengine.extension.multiplayer.protocol.shared.IMessageHandler;
import org.andengine.extension.multiplayer.protocol.shared.IMessageReader;
import org.andengine.extension.multiplayer.protocol.shared.MessageReader;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 13:11:07 - 02.03.2011
 */
public interface IServerMessageReader<C extends Connection> extends IMessageReader<C, ServerConnector<C>, IServerMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void registerMessage(final short pFlag, final Class<? extends IServerMessage> pServerMessageClass);

	@Override
	public void registerMessage(final short pFlag, final Class<? extends IServerMessage> pServerMessageClass, final IMessageHandler<C, ServerConnector<C>, IServerMessage> pServerMessageHandler);

	@Override
	public void registerMessageHandler(final short pFlag, final IMessageHandler<C, ServerConnector<C>, IServerMessage> pServerMessageHandler);

	@Override
	public IServerMessage readMessage(final DataInputStream pDataInputStream) throws IOException;

	@Override
	public void handleMessage(final ServerConnector<C> pServerConnector, final IServerMessage pServerMessage) throws IOException;

	@Override
	public void recycleMessage(final IServerMessage pServerMessage);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class ServerMessageReader<C extends Connection> extends MessageReader<C, ServerConnector<C>, IServerMessage> implements IServerMessageReader<C> {
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

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
