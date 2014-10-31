package org.andengine.extension.multiplayer.protocol.shared;

import org.andengine.extension.multiplayer.protocol.shared.Connection.IConnectionListener;
import org.andengine.util.adt.list.SmartList;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 13:51:22 - 03.03.2011
 */
public abstract class Connector<C extends Connection> implements IConnectionListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final C mConnection;
	protected SmartList<IConnectorListener<? extends Connector<C>>> mConnectorListeners = new SmartList<IConnectorListener<? extends Connector<C>>>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public Connector(final C pConnection) {
		this.mConnection = pConnection;
		this.mConnection.setConnectionListener(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public C getConnection() {
		return this.mConnection;
	}

	public boolean hasConnectorListener() {
		return this.mConnectorListeners != null;
	}

	public SmartList<? extends IConnectorListener<? extends Connector<C>>> getConnectorListeners() {
		return this.mConnectorListeners;
	}

	protected void addConnectorListener(final IConnectorListener<? extends Connector<C>> pConnectorListener) {
		if (pConnectorListener != null) {
			this.mConnectorListeners.add(pConnectorListener);
		}
	}

	protected boolean removeConnectorListener(final IConnectorListener<? extends Connector<C>> pConnectorListener) {
		if (pConnectorListener == null) {
			return false;
		} else {
			return this.mConnectorListeners.remove(pConnectorListener);
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void start() {
		this.getConnection().start();
	}

	public void terminate() {
		this.getConnection().terminate();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IConnectorListener<C extends Connector<?>> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onStarted(final C pConnector);
		public void onTerminated(final C pConnector);
	}
}
