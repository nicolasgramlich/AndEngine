package org.andengine.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:48:56 - 03.09.2009
 */
public final class StreamUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int IO_BUFFER_SIZE = 8 * 1024;

	private static final int END_OF_STREAM = -1;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private StreamUtils() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static String[] readLines(final InputStream pInputStream) throws IOException {
		return StreamUtils.readLines(new InputStreamReader(pInputStream));
	}

	public static String[] readLines(final Reader pReader) throws IOException {
		final BufferedReader reader = new BufferedReader(pReader);

		final ArrayList<String> lines = new ArrayList<String>();

		String line = null;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}

		return lines.toArray(new String[lines.size()]);
	}

	public static final String readFully(final InputStream pInputStream) throws IOException {
		final StringWriter writer = new StringWriter();
		final char[] buf = new char[StreamUtils.IO_BUFFER_SIZE];
		try {
			final Reader reader = new BufferedReader(new InputStreamReader(pInputStream, "UTF-8"));
			int read;
			while ((read = reader.read(buf)) != StreamUtils.END_OF_STREAM) {
				writer.write(buf, 0, read);
			}
		} finally {
			StreamUtils.close(pInputStream);
		}
		return writer.toString();
	}

	public static final byte[] streamToBytes(final InputStream pInputStream) throws IOException {
		return StreamUtils.streamToBytes(pInputStream, StreamUtils.END_OF_STREAM);
	}

	public static final byte[] streamToBytes(final InputStream pInputStream, final int pReadLimit) throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream((pReadLimit == StreamUtils.END_OF_STREAM) ? StreamUtils.IO_BUFFER_SIZE : pReadLimit);
		StreamUtils.copy(pInputStream, os, pReadLimit);
		return os.toByteArray();
	}

	/**
	 * @see {@link #streamToBytes(InputStream, int, byte[], int)}
	 */
	public static final void streamToBytes(final InputStream pInputStream, final int pByteLimit, final byte[] pData) throws IOException {
		StreamUtils.streamToBytes(pInputStream, pByteLimit, pData, 0);
	}

	/**
	 * @param pInputStream the sources of the bytes.
	 * @param pByteLimit the amount of bytes to read.
	 * @param pData the array to place the read bytes in.
	 * @param pOffset the offset within pData.
	 * @throws IOException
	 */
	public static final void streamToBytes(final InputStream pInputStream, final int pByteLimit, final byte[] pData, final int pOffset) throws IOException {
		if (pByteLimit > pData.length - pOffset) {
			throw new IOException("pData is not big enough.");
		}

		int pBytesLeftToRead = pByteLimit;
		int readTotal = 0;
		int read;
		while ((read = pInputStream.read(pData, pOffset + readTotal, pBytesLeftToRead)) != StreamUtils.END_OF_STREAM) {
			readTotal += read;
			if (pBytesLeftToRead > read) {
				pBytesLeftToRead -= read;
			} else {
				break;
			}
		}

		if (readTotal != pByteLimit) {
			throw new IOException("ReadLimit: '" + pByteLimit + "', Read: '" + readTotal + "'.");
		}
	}

	public static final void copy(final InputStream pInputStream, final OutputStream pOutputStream) throws IOException {
		StreamUtils.copy(pInputStream, pOutputStream, StreamUtils.END_OF_STREAM);
	}

	public static final void copy(final InputStream pInputStream, final byte[] pData) throws IOException {
		int dataOffset = 0;
		final byte[] buf = new byte[StreamUtils.IO_BUFFER_SIZE];
		int read;
		while ((read = pInputStream.read(buf)) != StreamUtils.END_OF_STREAM) {
			System.arraycopy(buf, 0, pData, dataOffset, read);
			dataOffset += read;
		}
	}

	public static final void copy(final InputStream pInputStream, final ByteBuffer pByteBuffer) throws IOException {
		final byte[] buf = new byte[StreamUtils.IO_BUFFER_SIZE];
		int read;
		while ((read = pInputStream.read(buf)) != StreamUtils.END_OF_STREAM) {
			pByteBuffer.put(buf, 0, read);
		}
	}

	/**
	 * Copy the content of the input stream into the output stream, using a temporary
	 * byte array buffer whose size is defined by {@link #IO_BUFFER_SIZE}.
	 *
	 * @param pInputStream The input stream to copy from.
	 * @param pOutputStream The output stream to copy to.
	 * @param pByteLimit not more than so much bytes to read, or unlimited if {@link #END_OF_STREAM}.
	 *
	 * @throws IOException If any error occurs during the copy.
	 */
	public static final void copy(final InputStream pInputStream, final OutputStream pOutputStream, final int pByteLimit) throws IOException {
		if (pByteLimit == StreamUtils.END_OF_STREAM) {
			final byte[] buf = new byte[StreamUtils.IO_BUFFER_SIZE];
			int read;
			while ((read = pInputStream.read(buf)) != StreamUtils.END_OF_STREAM) {
				pOutputStream.write(buf, 0, read);
			}
		} else {
			final byte[] buf = new byte[StreamUtils.IO_BUFFER_SIZE];
			final int bufferReadLimit = Math.min((int) pByteLimit, StreamUtils.IO_BUFFER_SIZE);
			long pBytesLeftToRead = pByteLimit;

			int read;
			while ((read = pInputStream.read(buf, 0, bufferReadLimit)) != StreamUtils.END_OF_STREAM) {
				if (pBytesLeftToRead > read) {
					pOutputStream.write(buf, 0, read);
					pBytesLeftToRead -= read;
				} else {
					pOutputStream.write(buf, 0, (int) pBytesLeftToRead);
					break;
				}
			}
		}
		pOutputStream.flush();
	}

	public static final boolean copyAndClose(final InputStream pInputStream, final OutputStream pOutputStream) {
		try {
			StreamUtils.copy(pInputStream, pOutputStream, StreamUtils.END_OF_STREAM);
			return true;
		} catch (final IOException e) {
			return false;
		} finally {
			StreamUtils.close(pInputStream);
			StreamUtils.close(pOutputStream);
		}
	}

	public static final void close(final Closeable pCloseable) {
		if (pCloseable != null) {
			try {
				pCloseable.close();
			} catch (final IOException e) {
				Debug.e("Error closing Closable", e);
			}
		}
	}

	public static final void flushAndCloseStream(final OutputStream pOutputStream) {
		if (pOutputStream != null) {
			try {
				pOutputStream.flush();
			} catch (final IOException e) {
				Debug.e("Error flusing OutputStream", e);
			} finally {
				StreamUtils.close(pOutputStream);
			}
		}
	}

	public static final void flushAndCloseWriter(final Writer pWriter) {
		if (pWriter != null) {
			try {
				pWriter.flush();
			} catch (final IOException e) {
				Debug.e("Error flusing Writer", e);
			} finally {
				StreamUtils.close(pWriter);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
