package org.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector.IClientConnectorListener;
import org.andengine.extension.multiplayer.protocol.server.SocketServer.ISocketServerListener.DefaultSocketServerListener;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector.DefaultSocketConnectionClientConnectorListener;
import org.andengine.util.SocketUtils;
import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:55:09 - 03.03.2011
 */
public abstract class SocketServer<CC extends ClientConnector<SocketConnection>> extends Server<SocketConnection, CC> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mPort;
	private ServerSocket mServerSocket;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SocketServer(final int pPort) {
		this(pPort, new DefaultSocketConnectionClientConnectorListener());
	}

	public SocketServer(final int pPort, final IClientConnectorListener<SocketConnection> pClientConnectorListener) {
		this(pPort, pClientConnectorListener, new DefaultSocketServerListener<CC>());
	}

	public SocketServer(final int pPort, final ISocketServerListener<CC> pSocketServerListener) {
		this(pPort, new DefaultSocketConnectionClientConnectorListener(), pSocketServerListener);
	}

	public SocketServer(final int pPort, final IClientConnectorListener<SocketConnection> pClientConnectorListener, final ISocketServerListener<CC> pSocketServerListener) {
		super(pClientConnectorListener, pSocketServerListener);

		if (pPort < 0) {
			final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Illegal port '< 0'.");
			this.onException(illegalArgumentException);
			throw illegalArgumentException;
		}else{
			this.mPort = pPort;
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getPort() {
		return this.mPort;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ISocketServerListener<CC> getServerListener() {
		return (ISocketServerListener<CC>)super.getServerListener();
	}

	public void setSocketServerListener(final ISocketServerListener<CC> pSocketServerListener) {
		super.setServerListener(pSocketServerListener);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract CC newClientConnector(final SocketConnection pSocketConnection) throws IOException;

	@Override
	protected void onStart() throws IOException {
		this.mServerSocket = ServerSocketFactory.getDefault().createServerSocket(this.mPort);
		this.getServerListener().onStarted(this);
	}

	@Override
	protected CC acceptClientConnector() throws IOException {
		/* Wait for an incoming connection. */
		final Socket clientSocket = this.mServerSocket.accept();

		/* Spawn a new ClientConnector, which send and receive data to and from the client. */
		return this.newClientConnector(new SocketConnection(clientSocket));
	}

	@Override
	protected void onTerminate() {
		SocketUtils.closeSocket(this.mServerSocket);
		this.getServerListener().onTerminated(this);
	}

	@Override
	protected void onException(final Throwable pThrowable) {
		this.getServerListener().onException(this, pThrowable);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ISocketServerListener<CC extends ClientConnector<SocketConnection>> extends Server.IServerListener<SocketServer<CC>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		@Override
		public void onStarted(final SocketServer<CC> pSocketServer);

		@Override
		public void onTerminated(final SocketServer<CC> pSocketServer);

		@Override
		public void onException(final SocketServer<CC> pSocketServer, final Throwable pThrowable);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class DefaultSocketServerListener<CC extends ClientConnector<SocketConnection>> implements ISocketServerListener<CC> {
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

			@Override
			public void onStarted(final SocketServer<CC> pSocketServer) {
				Debug.d("SocketServer started on port: " + pSocketServer.getPort());
			}

			@Override
			public void onTerminated(final SocketServer<CC> pSocketServer) {
				Debug.d("SocketServer terminated on port: " + pSocketServer.getPort());
			}

			@Override
			public void onException(final SocketServer<CC> pServer, final Throwable pThrowable) {
				Debug.e(pThrowable);
			}

			// ===========================================================
			// Methods
			// ===========================================================

			// ===========================================================
			// Inner and Anonymous Classes
			// ===========================================================
		}
	}
}
