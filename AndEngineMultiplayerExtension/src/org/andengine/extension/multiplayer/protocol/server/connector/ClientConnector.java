package org.andengine.extension.multiplayer.protocol.server.connector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.server.IClientMessageHandler;
import org.andengine.extension.multiplayer.protocol.server.IClientMessageReader;
import org.andengine.extension.multiplayer.protocol.server.IClientMessageReader.ClientMessageReader;
import org.andengine.extension.multiplayer.protocol.shared.Connection;
import org.andengine.extension.multiplayer.protocol.shared.Connector;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.call.ParameterCallable;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public class ClientConnector<C extends Connection> extends Connector<C> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IClientMessageReader<C> mClientMessageReader;

	private final ParameterCallable<IClientConnectorListener<C>> mOnStartedParameterCallable = new ParameterCallable<ClientConnector.IClientConnectorListener<C>>() {
		@Override
		public void call(final IClientConnectorListener<C> pClientConnectorListener) {
			pClientConnectorListener.onStarted(ClientConnector.this);
		}
	};

	private final ParameterCallable<IClientConnectorListener<C>> mOnTerminatedParameterCallable = new ParameterCallable<ClientConnector.IClientConnectorListener<C>>() {
		@Override
		public void call(final IClientConnectorListener<C> pClientConnectorListener) {
			pClientConnectorListener.onTerminated(ClientConnector.this);
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	public ClientConnector(final C pConnection) throws IOException {
		this(pConnection, new ClientMessageReader<C>());
	}

	public ClientConnector(final C pConnection, final IClientMessageReader<C> pClientMessageReader) throws IOException {
		super(pConnection);

		this.mClientMessageReader = pClientMessageReader;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public IClientMessageReader<C> getClientMessageReader() {
		return this.mClientMessageReader;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SmartList<IClientConnectorListener<C>> getConnectorListeners() {
		return (SmartList<IClientConnectorListener<C>>) super.getConnectorListeners();
	}

	public void addClientConnectorListener(final IClientConnectorListener<C> pClientConnectorListener) {
		super.addConnectorListener(pClientConnectorListener);
	}

	public void removeClientConnectorListener(final IClientConnectorListener<C> pClientConnectorListener) {
		super.removeConnectorListener(pClientConnectorListener);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onStarted(final Connection pConnection) {
		this.getConnectorListeners().call(this.mOnStartedParameterCallable);
	}

	@Override
	public void onTerminated(final Connection pConnection) {
		this.getConnectorListeners().call(this.mOnTerminatedParameterCallable);
	}

	@Override
	public void read(final DataInputStream pDataInputStream) throws IOException {
		final IClientMessage clientMessage = this.mClientMessageReader.readMessage(pDataInputStream);
		this.mClientMessageReader.handleMessage(this, clientMessage);
		this.mClientMessageReader.recycleMessage(clientMessage);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerClientMessage(final short pFlag, final Class<? extends IClientMessage> pClientMessageClass) {
		this.mClientMessageReader.registerMessage(pFlag, pClientMessageClass);
	}

	public void registerClientMessage(final short pFlag, final Class<? extends IClientMessage> pClientMessageClass, final IClientMessageHandler<C> pClientMessageHandler) {
		this.mClientMessageReader.registerMessage(pFlag, pClientMessageClass, pClientMessageHandler);
	}

	public void registerClientMessageHandler(final short pFlag, final IClientMessageHandler<C> pClientMessageHandler) {
		this.mClientMessageReader.registerMessageHandler(pFlag, pClientMessageHandler);
	}

	public synchronized void sendServerMessage(final IServerMessage pServerMessage) throws IOException {
		final DataOutputStream dataOutputStream = this.mConnection.getDataOutputStream();
		pServerMessage.write(dataOutputStream);
		dataOutputStream.flush();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IClientConnectorListener<T extends Connection> extends IConnectorListener<ClientConnector<T>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		@Override
		public void onStarted(final ClientConnector<T> pClientConnector);

		@Override
		public void onTerminated(final ClientConnector<T> pClientConnector);
	}
}
