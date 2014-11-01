package org.andengine.extension.multiplayer.protocol.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.andengine.extension.multiplayer.protocol.server.SocketServerDiscoveryServer;
import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData;
import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData.DiscoveryDataFactory;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;
import org.andengine.util.SocketUtils;
import org.andengine.util.adt.pool.GenericPool;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:50:07 - 23.06.2011
 */
public class SocketServerDiscoveryClient<T extends IDiscoveryData> {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int LOCALPORT_DEFAULT = 9998;

	protected static final int TIMEOUT_DEFAULT = 5000;

	// ===========================================================
	// Fields
	// ===========================================================

	protected AtomicBoolean mTerminated = new AtomicBoolean(false);

	private final InetAddress mDiscoveryBroadcastInetAddress;
	private final int mDiscoveryPort;
	private final int mLocalPort;
	private int mTimeout = TIMEOUT_DEFAULT;
	private final ISocketServerDiscoveryClientListener<T> mSocketServerDiscoveryClientListener;

	private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

	private final byte[] mDiscoveryResponseData = new byte[1024];
	private final DatagramPacket mDiscoveryResponseDatagramPacket = new DatagramPacket(this.mDiscoveryResponseData, this.mDiscoveryResponseData.length);

	private final DatagramPacket mDiscoveryRequestDatagramPacket;

	private final GenericPool<T> mDiscoveryDataPool;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pDiscoveryBroadcastIPAddress
	 *            use
	 *            {@link WifiUtils#getBroadcastIPAddressRaw(android.content.Context)}
	 *            .
	 * @param pDiscoveryDataClass
	 * @param pSocketServerDiscoveryClientListener
	 * @throws UnknownHostException
	 */
	public SocketServerDiscoveryClient(final byte[] pDiscoveryBroadcastIPAddress, final Class<? extends T> pDiscoveryDataClass,
			final ISocketServerDiscoveryClientListener<T> pSocketServerDiscoveryClientListener) throws UnknownHostException {
		this(pDiscoveryBroadcastIPAddress, SocketServerDiscoveryServer.DISCOVERYPORT_DEFAULT, LOCALPORT_DEFAULT, pDiscoveryDataClass, pSocketServerDiscoveryClientListener);
	}

	/**
	 * @param pDiscoveryBroadcastIPAddress
	 *            use
	 *            {@link WifiUtils#getBroadcastIPAddressRaw(android.content.Context)}
	 *            .
	 * @param pDiscoveryPort
	 * @param pDiscoveryDataClass
	 * @param pSocketServerDiscoveryClientListener
	 * @throws UnknownHostException
	 */
	public SocketServerDiscoveryClient(final byte[] pDiscoveryBroadcastIPAddress, final int pDiscoveryPort, final Class<? extends T> pDiscoveryDataClass,
			final ISocketServerDiscoveryClientListener<T> pSocketServerDiscoveryClientListener) throws UnknownHostException {
		this(pDiscoveryBroadcastIPAddress, pDiscoveryPort, LOCALPORT_DEFAULT, pDiscoveryDataClass, pSocketServerDiscoveryClientListener);
	}

	/**
	 * @param pDiscoveryBroadcastIPAddress
	 *            use
	 *            {@link WifiUtils#getBroadcastIPAddressRaw(android.content.Context)}
	 *            .
	 * @param pDiscoveryPort
	 * @param pLocalPort
	 * @param pDiscoveryDataClass
	 * @param pSocketServerDiscoveryClientListener
	 * @throws UnknownHostException
	 */
	public SocketServerDiscoveryClient(final byte[] pDiscoveryBroadcastIPAddress, final int pDiscoveryPort, final int pLocalPort, final Class<? extends T> pDiscoveryDataClass,
			final ISocketServerDiscoveryClientListener<T> pSocketServerDiscoveryClientListener) throws UnknownHostException {
		this.mDiscoveryPort = pDiscoveryPort;
		this.mLocalPort = pLocalPort;
		this.mSocketServerDiscoveryClientListener = pSocketServerDiscoveryClientListener;

		this.mDiscoveryBroadcastInetAddress = InetAddress.getByAddress(pDiscoveryBroadcastIPAddress);

		final byte[] out = SocketServerDiscoveryServer.MAGIC_IDENTIFIER;
		this.mDiscoveryRequestDatagramPacket = new DatagramPacket(out, out.length, SocketServerDiscoveryClient.this.mDiscoveryBroadcastInetAddress, SocketServerDiscoveryClient.this.mDiscoveryPort);

		this.mDiscoveryDataPool = new GenericPool<T>() {
			@Override
			protected T onAllocatePoolItem() {
				try {
					return pDiscoveryDataClass.newInstance();
				} catch (final Throwable t) {
					return null;
				}
			}
		};
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getDiscoveryPort() {
		return this.mDiscoveryPort;
	}

	public int getLocalPort() {
		return this.mLocalPort;
	}

	public int getTimeout() {
		return this.mTimeout;
	}

	public void setTimeout(final int pTimeout) {
		this.mTimeout = pTimeout;
	}

	public InetAddress getDiscoveryBroadcastInetAddress() {
		return this.mDiscoveryBroadcastInetAddress;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void discoverAsync() throws IllegalStateException {
		if (this.mTerminated.get()) {
			throw new IllegalStateException(this.getClass().getSimpleName() + " was already terminated.");
		}

		this.mExecutorService.execute(new Runnable() {
			@Override
			public void run() {
				SocketServerDiscoveryClient.this.discover();
			}
		});
	}

	private void discover() {
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket(this.mLocalPort);
			datagramSocket.setBroadcast(true);

			this.sendDiscoveryRequest(datagramSocket);

			final byte[] discoveryResponseData = this.receiveDiscoveryResponseData(datagramSocket);

			this.handleDiscoveryResponseData(discoveryResponseData);
		} catch (final SocketTimeoutException t) {
			this.mSocketServerDiscoveryClientListener.onTimeout(this, t);
		} catch (final Throwable t) {
			this.mSocketServerDiscoveryClientListener.onException(this, t);
		} finally {
			SocketUtils.closeSocket(datagramSocket);
		}
	}

	private void sendDiscoveryRequest(final DatagramSocket datagramSocket) throws IOException {
		datagramSocket.send(this.mDiscoveryRequestDatagramPacket);
	}

	protected byte[] receiveDiscoveryResponseData(final DatagramSocket datagramSocket) throws SocketException, IOException {
		datagramSocket.setSoTimeout(this.mTimeout);
		datagramSocket.receive(this.mDiscoveryResponseDatagramPacket);

		final byte[] discoveryResponseData = new byte[this.mDiscoveryResponseDatagramPacket.getLength()];
		System.arraycopy(this.mDiscoveryResponseDatagramPacket.getData(), this.mDiscoveryResponseDatagramPacket.getOffset(), discoveryResponseData, 0,
				this.mDiscoveryResponseDatagramPacket.getLength());
		return discoveryResponseData;
	}

	private void handleDiscoveryResponseData(final byte[] pDiscoveryResponseData) {
		final T discoveryResponse = this.mDiscoveryDataPool.obtainPoolItem();

		try {
			DiscoveryDataFactory.read(pDiscoveryResponseData, discoveryResponse);
			this.mSocketServerDiscoveryClientListener.onDiscovery(SocketServerDiscoveryClient.this, discoveryResponse);
		} catch (final Throwable t) {
			this.mSocketServerDiscoveryClientListener.onException(this, t);
		}

		this.mDiscoveryDataPool.recyclePoolItem(discoveryResponse);
	}

	public void terminate() {
		if (!this.mTerminated.getAndSet(true)) {
			this.onTerminate();
		}
	}

	private void onTerminate() {
		this.mExecutorService.shutdownNow();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface ISocketServerDiscoveryClientListener<T extends IDiscoveryData> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onDiscovery(final SocketServerDiscoveryClient<T> pSocketServerDiscoveryClient, final T pDiscoveryData);

		public void onTimeout(final SocketServerDiscoveryClient<T> pSocketServerDiscoveryClient, final SocketTimeoutException pSocketTimeoutException);

		public void onException(final SocketServerDiscoveryClient<T> pSocketServerDiscoveryClient, final Throwable pThrowable);
	}
}
