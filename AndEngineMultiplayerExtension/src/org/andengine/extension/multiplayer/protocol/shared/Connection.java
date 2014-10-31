package org.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public abstract class Connection extends Thread {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final DataInputStream mDataInputStream;
	protected final DataOutputStream mDataOutputStream;

	protected IConnectionListener mConnectionListener;
	protected AtomicBoolean mRunning = new AtomicBoolean(false);
	protected AtomicBoolean mTerminated = new AtomicBoolean(false);

	// ===========================================================
	// Constructors
	// ===========================================================

	public Connection(final DataInputStream pDataInputStream, final DataOutputStream pDataOutputStream) throws IOException {
		this.mDataInputStream = pDataInputStream;
		this.mDataOutputStream = pDataOutputStream;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public DataOutputStream getDataOutputStream() {
		return this.mDataOutputStream;
	}

	public DataInputStream getDataInputStream() {
		return this.mDataInputStream;
	}

	public boolean hasConnectionListener() {
		return this.mConnectionListener != null;
	}

	public IConnectionListener getConnectionListener() {
		return this.mConnectionListener;
	}

	public void setConnectionListener(final IConnectionListener pConnectionListener) {
		this.mConnectionListener = pConnectionListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void run() {
		this.onStart();

		this.mRunning.set(true);

//		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);  // TODO What ThreadPriority makes sense here?

		try {
			while (!Thread.interrupted() && this.mRunning.get() && !this.mTerminated.get()) {
				try {
					this.mConnectionListener.read(this.mDataInputStream);
				} catch (final SocketException se) {
					this.terminate();
				} catch (final EOFException eof) {
					this.terminate();
				} catch (final Throwable pThrowable) {
					Debug.e(pThrowable);
				}
			}
		} catch (final Throwable pThrowable) {
			Debug.e(pThrowable);
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

	public void terminate() {
		if (!this.mTerminated.getAndSet(true)) {
			this.mRunning.set(false);

			this.interrupt();

			this.onTerminate();
		}
	}

	protected void onStart() {
		if (this.mConnectionListener != null) {
			this.mConnectionListener.onStarted(this);
		}
	}

	protected void onTerminate() {
		if (this.mConnectionListener != null) {
			this.mConnectionListener.onTerminated(this);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IConnectionListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onStarted(final Connection pConnection);
		public void onTerminated(final Connection pConnection);

		public void read(final DataInputStream pDataInputStream) throws IOException;
	}
}