package org.anddev.andengine.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Scanner;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:48:56 - 03.09.2009
 */
public class StreamUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int IO_BUFFER_SIZE = 8 * 1024;

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
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static final String readFully(final InputStream pInputStream) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final Scanner sc = new Scanner(pInputStream);
		while(sc.hasNextLine()) {
			sb.append(sc.nextLine());
		}
		return sb.toString();
	}

	public static byte[] streamToBytes(final InputStream pInputStream) throws IOException {
		return StreamUtils.streamToBytes(pInputStream, -1);
	}

	public static byte[] streamToBytes(final InputStream pInputStream, final int pReadLimit) throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream((pReadLimit == -1) ? IO_BUFFER_SIZE : pReadLimit);
		StreamUtils.copy(pInputStream, os, pReadLimit);
		return os.toByteArray();
	}

	public static void copy(final InputStream pInputStream, final OutputStream pOutputStream) throws IOException {
		StreamUtils.copy(pInputStream, pOutputStream, -1);
	}

	public static boolean copyAndClose(final InputStream pInputStream, final OutputStream pOutputStream) {
		try {
			StreamUtils.copy(pInputStream, pOutputStream, -1);
			return true;
		} catch (final IOException e) {
			return false;
		} finally {
			StreamUtils.close(pInputStream);
			StreamUtils.close(pOutputStream);
		}
	}

	/**
	 * Copy the content of the input stream into the output stream, using a temporary
	 * byte array buffer whose size is defined by {@link #IO_BUFFER_SIZE}.
	 *
	 * @param pInputStream The input stream to copy from.
	 * @param pOutputStream The output stream to copy to.
	 * @param pByteLimit not more than so much bytes to read, or unlimited if smaller than 0.
	 *
	 * @throws IOException If any error occurs during the copy.
	 */
	public static void copy(final InputStream pInputStream, final OutputStream pOutputStream, final long pByteLimit) throws IOException {
		if(pByteLimit < 0) {
			final byte[] b = new byte[IO_BUFFER_SIZE];
			int read;
			while((read = pInputStream.read(b)) != -1) {
				pOutputStream.write(b, 0, read);
			}
		} else {
			final byte[] b = new byte[IO_BUFFER_SIZE];
			final int bufferReadLimit = Math.min((int)pByteLimit, IO_BUFFER_SIZE);
			long pBytesLeftToRead = pByteLimit;
			
			int read;
			while((read = pInputStream.read(b, 0, bufferReadLimit)) != -1) {
				if(pBytesLeftToRead > read) {
					pOutputStream.write(b, 0, read);
					pBytesLeftToRead -= read;
				} else {
					pOutputStream.write(b, 0, (int) pBytesLeftToRead);
					break;
				}
			}
		}
		pOutputStream.flush();
	}

	/**
	 * Closes the specified stream.
	 *
	 * @param pCloseable The stream to close.
	 */
	public static void close(final Closeable pCloseable) {
		if(pCloseable != null) {
			try {
				pCloseable.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Flushes and closes the specified stream.
	 *
	 * @param pOutputStream The stream to close.
	 */
	public static void flushCloseStream(final OutputStream pOutputStream) {
		if(pOutputStream != null) {
			try {
				pOutputStream.flush();
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				StreamUtils.close(pOutputStream);
			}
		}
	}

	/**
	 * Flushes and closes the specified stream.
	 *
	 * @param pWriter The Writer to close.
	 */
	public static void flushCloseWriter(final Writer pWriter) {
		if(pWriter != null) {
			try {
				pWriter.flush();
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				StreamUtils.close(pWriter);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
