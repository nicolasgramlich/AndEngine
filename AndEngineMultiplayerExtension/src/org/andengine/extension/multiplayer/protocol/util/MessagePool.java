package org.andengine.extension.multiplayer.protocol.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.andengine.util.adt.pool.GenericPool;
import org.andengine.util.adt.pool.MultiPool;
import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:33:23 - 02.03.2011
 */
public class MessagePool<M extends IMessage> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final MultiPool<M> mMessageMultiPool = new MultiPool<M>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void registerMessage(final short pFlag, final Class<? extends M> pMessageClass) {
		this.mMessageMultiPool.registerPool(pFlag,
			new GenericPool<M>() {
				@Override
				protected M onAllocatePoolItem() {
					try {
						return pMessageClass.newInstance();
					} catch (final Throwable t) {
						Debug.e(t);
						return null;
					}
				}
			}
		);
	}

	public M obtainMessage(final short pFlag) {
		return this.mMessageMultiPool.obtainPoolItem(pFlag);
	}

	public M obtainMessage(final short pFlag, final DataInputStream pDataInputStream) throws IOException {
		final M message = this.mMessageMultiPool.obtainPoolItem(pFlag);
		if (message != null) {
			message.read(pDataInputStream);
			return message;
		} else {
			throw new IllegalArgumentException("No message found for pFlag='" + pFlag + "'.");
		}
	}

	public void recycleMessage(final M pMessage) {
		this.mMessageMultiPool.recyclePoolItem(pMessage.getFlag(), pMessage);
	}

	public void recycleMessages(final List<? extends M> pMessages) {
		final MultiPool<M> messageMultiPool = this.mMessageMultiPool;
		for (int i = pMessages.size() - 1; i >= 0; i--) {
			final M message = pMessages.get(i);
			messageMultiPool.recyclePoolItem(message.getFlag(), message);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
