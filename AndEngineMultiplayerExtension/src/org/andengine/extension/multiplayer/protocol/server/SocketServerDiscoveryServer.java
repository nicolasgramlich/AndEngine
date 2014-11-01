package org.andengine.extension.multiplayer.protocol.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData;
import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData.DiscoveryDataFactory;
import org.andengine.extension.multiplayer.protocol.server.SocketServerDiscoveryServer.ISocketServerDiscoveryServerListener.DefaultSocketServerDiscoveryServerListener;
import org.andengine.util.SocketUtils;
import org.andengine.util.adt.array.ArrayUtils;
import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:08:20 - 23.06.2011
 */
public abstract class SocketServerDiscoveryServer<T extends IDiscoveryData> extends Thread {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int DISCOVERYPORT_DEFAULT = 9999;

	public static final byte[] MAGIC_IDENTIFIER = new byte[]{
		(byte)'A',
		(byte)'n',
		(byte)'d',
		(byte)'E',
		(byte)'n',
		(byte)'g',
		(byte)'i',
		(byte)'n',
		(byte)'e',
		(byte)'-',
		(byte)'S',
		(byte)'o',
		(byte)'c',
		(byte)'k',
		(byte)'e',
		(byte)'t',
		(byte)'S',
		(byte)'e',
		(byte)'r',
		(byte)'v',
		(byte)'e',
		(byte)'r',
		(byte)'D',
		(byte)'i',
		(byte)'s',
		(byte)'c',
		(byte)'o',
		(byte)'v',
		(byte)'e',
		(byte)'r',
		(byte)'y'
	};

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mDiscoveryPort;

	private DatagramSocket mDatagramSocket;

	private final byte[] mDiscoveryRequestData = new byte[1024];
	private final DatagramPacket mDiscoveryRequestDatagramPacket = new DatagramPacket(this.mDiscoveryRequestData, this.mDiscoveryRequestData.length);

	protected ISocketServerDiscoveryServerListener<T> mSocketServerDiscoveryServerListener;
	protected AtomicBoolean mRunning = new AtomicBoolean(false);
	protected AtomicBoolean mTerminated = new AtomicBoolean(false);

	// ===========================================================
	// Constructors
	// ===========================================================

	public SocketServerDiscoveryServer() {
		this(DISCOVERYPORT_DEFAULT);
	}

	public SocketServerDiscoveryServer(final int pDiscoveryPort) {
		this(pDiscoveryPort, new DefaultSocketServerDiscoveryServerListener<T>());
	}

	public SocketServerDiscoveryServer(final ISocketServerDiscoveryServerListener<T> pSocketServerDiscoveryServerListener) {
		this(DISCOVERYPORT_DEFAULT, pSocketServerDiscoveryServerListener);
	}

	public SocketServerDiscoveryServer(final int pDiscoveryPort, final ISocketServerDiscoveryServerListener<T> pSocketServerDiscoveryServerListener) {
		this.mDiscoveryPort = pDiscoveryPort;
		this.mSocketServerDiscoveryServerListener = pSocketServerDiscoveryServerListener;

		this.initName();
	}

	private void initName() {
		this.setName(this.getClass().getName());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isRunning() {
		return this.mRunning.get();
	}

	public boolean isTerminated() {
		return this.mTerminated.get();
	}

	public int getDiscoveryPort() {
		return this.mDiscoveryPort;
	}

	public boolean hasSocketServerDiscoveryServerListener() {
		return this.mSocketServerDiscoveryServerListener != null;
	}

	public ISocketServerDiscoveryServerListener<T> getSocketServerDiscoveryServerListener() {
		return this.mSocketServerDiscoveryServerListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract T onCreateDiscoveryResponse();

	@Override
	public void run() {
		try {
			this.onStart();

			this.mRunning.set(true);

//			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);  // TODO What ThreadPriority makes sense here?

			while(!Thread.interrupted() && this.mRunning.get() && !this.mTerminated.get()) {
				try {
					this.mDatagramSocket.receive(this.mDiscoveryRequestDatagramPacket);

					if (this.verifyDiscoveryRequest(this.mDiscoveryRequestDatagramPacket)) {
						this.onDiscovered(this.mDiscoveryRequestDatagramPacket);
						this.sendDiscoveryResponse(this.mDiscoveryRequestDatagramPacket);
					}
				} catch (final Throwable pThrowable) {
					this.onException(pThrowable);
				}
			}
		} catch (final Throwable pThrowable) {
			this.onException(pThrowable);
		} finally {
			this.terminate();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		this.terminate();
		super.finalize();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected boolean verifyDiscoveryRequest(final DatagramPacket pDiscoveryRequest) {
		return ArrayUtils.equals(MAGIC_IDENTIFIER, 0, pDiscoveryRequest.getData(), pDiscoveryRequest.getOffset(), MAGIC_IDENTIFIER.length);
	}

	protected void onDiscovered(final DatagramPacket pDiscoveryRequest) throws IOException {
		this.mSocketServerDiscoveryServerListener.onDiscovered(this, pDiscoveryRequest.getAddress(), pDiscoveryRequest.getPort());
	}

	protected void sendDiscoveryResponse(final DatagramPacket pDatagramPacket) throws IOException {
		final byte[] discoveryResponseData = DiscoveryDataFactory.write(this.onCreateDiscoveryResponse());

		this.mDatagramSocket.send(new DatagramPacket(discoveryResponseData, discoveryResponseData.length, pDatagramPacket.getAddress(), pDatagramPacket.getPort()));
	}

	protected void onStart() throws SocketException {
		this.mDatagramSocket = new DatagramSocket(this.mDiscoveryPort);

		this.mSocketServerDiscoveryServerListener.onStarted(this);
	}

	protected void onTerminate() {
		SocketUtils.closeSocket(this.mDatagramSocket);
		this.mSocketServerDiscoveryServerListener.onTerminated(this);
	}

	protected void onException(final Throwable pThrowable) {
		this.mSocketServerDiscoveryServerListener.onException(this, pThrowable);
	}

	public void terminate() {
		if (!this.mTerminated.getAndSet(true)) {
			this.mRunning.set(false);

			this.interrupt();

			this.onTerminate();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ISocketServerDiscoveryServerListener<T extends IDiscoveryData> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onStarted(final SocketServerDiscoveryServer<T> pSocketServerDiscoveryServer);
		public void onDiscovered(final SocketServerDiscoveryServer<T> pSocketServerDiscoveryServer, final InetAddress pInetAddress, final int pPort);
		public void onTerminated(final SocketServerDiscoveryServer<T> pSocketServerDiscoveryServer);
		public void onException(final SocketServerDiscoveryServer<T> pSocketServerDiscoveryServer, final Throwable pThrowable);

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================

		public static class DefaultSocketServerDiscoveryServerListener<T extends IDiscoveryData> implements ISocketServerDiscoveryServerListener<T> {
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
			public void onStarted(final SocketServerDiscoveryServer<T> pSocketServerDiscoveryServer) {
				Debug.d("SocketServerDiscoveryServer started on discoveryPort: " + pSocketServerDiscoveryServer.getDiscoveryPort());
			}

			@Override
			public void onTerminated(final SocketServerDiscoveryServer<T> pSocketServerDiscoveryServer) {
				Debug.d("SocketServerDiscoveryServer terminated on discoveryPort: " + pSocketServerDiscoveryServer.getDiscoveryPort());
			}

			@Override
			public void onDiscovered(final SocketServerDiscoveryServer<T> pSocketServerDiscoveryServer, final InetAddress pInetAddress, final int pPort) {
				Debug.d("SocketServerDiscoveryServer discovered by: " + pInetAddress.getHostAddress() + ":" + pPort);
			}

			@Override
			public void onException(final SocketServerDiscoveryServer<T> pSocketServerDiscoveryServer, final Throwable pThrowable) {
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