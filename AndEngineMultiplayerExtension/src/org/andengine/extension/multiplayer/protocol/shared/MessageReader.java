package org.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.andengine.extension.multiplayer.protocol.util.MessagePool;

import android.util.SparseArray;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:05:58 - 21.09.2009
 */
public abstract class MessageReader<C extends Connection, CC extends Connector<C>, M extends IMessage> implements IMessageReader<C, CC, M> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final MessagePool<M> mMessagePool = new MessagePool<M>();
	private final SparseArray<IMessageHandler<C, CC, M>> mMessageHandlers = new SparseArray<IMessageHandler<C, CC, M>>();

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

	@Override
	public void registerMessage(final short pFlag, final Class<? extends M> pMessageClass) {
		this.mMessagePool.registerMessage(pFlag, pMessageClass);
	}

	@Override
	public void registerMessageHandler(final short pFlag, final IMessageHandler<C, CC, M> pMessageHandler) {
		this.mMessageHandlers.put(pFlag, pMessageHandler);
	}

	@Override
	public void registerMessage(final short pFlag, final Class<? extends M> pMessageClass, final IMessageHandler<C, CC, M> pMessageHandler) {
		this.registerMessage(pFlag, pMessageClass);
		this.registerMessageHandler(pFlag, pMessageHandler);
	}

	@Override
	public M readMessage(final DataInputStream pDataInputStream) throws IOException {
		final short flag = pDataInputStream.readShort();
		return this.mMessagePool.obtainMessage(flag, pDataInputStream);
	}

	@Override
	public void handleMessage(final CC pConnector, final M pMessage) throws IOException {
		final IMessageHandler<C, CC, M> messageHandler = this.mMessageHandlers.get(pMessage.getFlag());
		if (messageHandler != null) {
			messageHandler.onHandleMessage(pConnector, pMessage);
		}
	}

	@Override
	public void recycleMessage(final M pMessage) {
		this.mMessagePool.recycleMessage(pMessage);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
