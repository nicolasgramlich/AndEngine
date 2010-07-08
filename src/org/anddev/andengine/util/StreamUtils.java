package org.anddev.andengine.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Scanner;

/**
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

	public static final String readFully(final InputStream in) throws IOException {
		final StringBuilder sb = new StringBuilder();
		final Scanner sc = new Scanner(in);
		while(sc.hasNextLine()){
			sb.append(sc.nextLine());
		}
		return sb.toString();
	}

	public static byte[] streamToBytes(final InputStream in) throws IOException {
		return streamToBytes(in, -1);
	}

	public static byte[] streamToBytes(final InputStream in, final int pReadLimit) throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream(Math.min(pReadLimit, IO_BUFFER_SIZE));
		copy(in, os, pReadLimit);
		return os.toByteArray();
	}

	public static void copy(final InputStream in, final OutputStream out) throws IOException {
		copy(in, out, -1);
	}

	public static boolean copyAndClose(final InputStream in, final OutputStream out) {
		try {
			copy(in, out, -1);
			return true;
		} catch (final IOException e) {
			return false;
		} finally {
			StreamUtils.closeStream(in);
			StreamUtils.closeStream(out);
		}
	}

	/**
	 * Copy the content of the input stream into the output stream, using a temporary
	 * byte array buffer whose size is defined by {@link #IO_BUFFER_SIZE}.
	 *
	 * @param in The input stream to copy from.
	 * @param out The output stream to copy to.
	 * @param pByteLimit not more than so much bytes to read, or unlimited if smaller than 0.
	 *
	 * @throws IOException If any error occurs during the copy.
	 */
	public static void copy(final InputStream in, final OutputStream out, final long pByteLimit) throws IOException {
		final byte[] b = new byte[IO_BUFFER_SIZE];
		long pBytesLeftToRead = pByteLimit;
		int read;
		if(pByteLimit < 0){
			while ((read = in.read(b)) != -1) {
				out.write(b, 0, read);
			}
		}else{
			while ((read = in.read(b)) != -1) {
				if(pBytesLeftToRead > read){
					out.write(b, 0, read);
					pBytesLeftToRead -= read;
				} else {
					out.write(b, 0, (int)pBytesLeftToRead);
					break;
				}
			}
		}
		out.flush();
	}

	/**
	 * Closes the specified stream.
	 *
	 * @param pStream The stream to close.
	 */
	public static void closeStream(final Closeable pStream) {
		if (pStream != null) {
			try {
				pStream.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Flushes and closes the specified stream.
	 *
	 * @param pStream The stream to close.
	 */
	public static void flushCloseStream(final OutputStream pStream) {
		if (pStream != null) {
			try {
				pStream.flush();
				closeStream(pStream);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Flushes and closes the specified stream.
	 *
	 * @param pWriter The Writer to close.
	 */
	public static void flushCloseWriter(final Writer pWriter) {
		if (pWriter != null) {
			try {
				pWriter.flush();
				closeStream(pWriter);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
