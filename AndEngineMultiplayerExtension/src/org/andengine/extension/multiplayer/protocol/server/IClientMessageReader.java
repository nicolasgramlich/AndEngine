package org.andengine.extension.multiplayer.protocol.server;

import java.io.DataInputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.shared.Connection;
import org.andengine.extension.multiplayer.protocol.shared.IMessageHandler;
import org.andengine.extension.multiplayer.protocol.shared.IMessageReader;
import org.andengine.extension.multiplayer.protocol.shared.MessageReader;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 13:39:29 - 02.03.2011
 */
public interface IClientMessageReader<C extends Connection> extends IMessageReader<C, ClientConnector<C>, IClientMessage> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void registerMessage(final short pFlag, final Class<? extends IClientMessage> pClientMessageClass);

	@Override
	public void registerMessage(final short pFlag, final Class<? extends IClientMessage> pClientMessageClass, final IMessageHandler<C, ClientConnector<C>, IClientMessage> pClientMessageHandler);

	@Override
	public void registerMessageHandler(final short pFlag, final IMessageHandler<C, ClientConnector<C>, IClientMessage> pClientMessageHandler);

	@Override
	public IClientMessage readMessage(final DataInputStream pDataInputStream) throws IOException;

	@Override
	public void handleMessage(final ClientConnector<C> pClientConnector, final IClientMessage pClientMessage) throws IOException;

	@Override
	public void recycleMessage(final IClientMessage pClientMessage);

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class ClientMessageReader<C extends Connection> extends MessageReader<C, ClientConnector<C>, IClientMessage> implements IClientMessageReader<C> {
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
