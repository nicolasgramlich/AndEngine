/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.andengine.util.base64;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An InputStream that does Base64 decoding on the data read through
 * it.
 */
public class Base64InputStream extends FilterInputStream {
	private final Base64.Coder coder;

	private static byte[] EMPTY = new byte[0];

	private static final int BUFFER_SIZE = 2048;
	private boolean eof;
	private byte[] inputBuffer;
	private int outputStart;
	private int outputEnd;

	/**
	 * An InputStream that performs Base64 decoding on the data read
	 * from the wrapped stream.
	 *
	 * @param in the InputStream to read the source data from
	 * @param flags bit flags for controlling the decoder; see the
	 *        constants in {@link Base64}
	 */
	public Base64InputStream(final InputStream in, final int flags) {
		this(in, flags, false);
	}

	/**
	 * Performs Base64 encoding or decoding on the data read from the
	 * wrapped InputStream.
	 *
	 * @param in the InputStream to read the source data from
	 * @param flags bit flags for controlling the decoder; see the
	 *        constants in {@link Base64}
	 * @param encode true to encode, false to decode
	 *
	 * @hide
	 */
	public Base64InputStream(final InputStream in, final int flags, final boolean encode) {
		super(in);
		this.eof = false;
		this.inputBuffer = new byte[BUFFER_SIZE];
		if (encode) {
			this.coder = new Base64.Encoder(flags, null);
		} else {
			this.coder = new Base64.Decoder(flags, null);
		}
		this.coder.output = new byte[this.coder.maxOutputSize(BUFFER_SIZE)];
		this.outputStart = 0;
		this.outputEnd = 0;
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public void mark(final int readlimit) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reset() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() throws IOException {
		this.in.close();
		this.inputBuffer = null;
	}

	@Override
	public int available() {
		return this.outputEnd - this.outputStart;
	}

	@Override
	public long skip(final long n) throws IOException {
		if (this.outputStart >= this.outputEnd) {
			this.refill();
		}
		if (this.outputStart >= this.outputEnd) {
			return 0;
		}
		final long bytes = Math.min(n, this.outputEnd-this.outputStart);
		this.outputStart += bytes;
		return bytes;
	}

	@Override
	public int read() throws IOException {
		if (this.outputStart >= this.outputEnd) {
			this.refill();
		}
		if (this.outputStart >= this.outputEnd) {
			return -1;
		} else {
			return this.coder.output[this.outputStart++];
		}
	}

	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
		if (this.outputStart >= this.outputEnd) {
			this.refill();
		}
		if (this.outputStart >= this.outputEnd) {
			return -1;
		}
		final int bytes = Math.min(len, this.outputEnd-this.outputStart);
		System.arraycopy(this.coder.output, this.outputStart, b, off, bytes);
		this.outputStart += bytes;
		return bytes;
	}

	/**
	 * Read data from the input stream into inputBuffer, then
	 * decode/encode it into the empty coder.output, and reset the
	 * outputStart and outputEnd pointers.
	 */
	private void refill() throws IOException {
		if (this.eof) {
			return;
		}
		final int bytesRead = this.in.read(this.inputBuffer);
		boolean success;
		if (bytesRead == -1) {
			this.eof = true;
			success = this.coder.process(EMPTY, 0, 0, true);
		} else {
			success = this.coder.process(this.inputBuffer, 0, bytesRead, false);
		}
		if (!success) {
			throw new IOException("bad base-64");
		}
		this.outputEnd = this.coder.op;
		this.outputStart = 0;
	}
}
