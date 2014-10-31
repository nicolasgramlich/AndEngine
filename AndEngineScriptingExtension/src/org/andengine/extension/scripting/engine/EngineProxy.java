package org.andengine.extension.scripting.engine;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;

public class EngineProxy extends Engine {
	private final long mAddress;

	public EngineProxy(final long pAddress, final EngineOptions pEngineOptions) {
		super(pEngineOptions);
		this.mAddress = pAddress;
	}

	public static native void nativeInitClass();
}
