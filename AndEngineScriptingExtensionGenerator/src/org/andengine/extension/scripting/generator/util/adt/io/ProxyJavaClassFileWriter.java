package org.andengine.extension.scripting.generator.util.adt.io;

import java.io.File;
import java.io.IOException;

import org.andengine.extension.scripting.generator.util.Util;
import org.andengine.extension.scripting.generator.util.adt.JavaFormatter;
import org.andengine.extension.scripting.generator.util.adt.io.GenFileWriter.GenFileWriterSegment;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:02:38 - 21.03.2012
 */
public class ProxyJavaClassFileWriter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final GenFileWriter<ProxyJavaClassSourceFileSegment> mGenJavaClassSourceFileWriter;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ProxyJavaClassFileWriter(final File pProxyJavaRoot, final Class<?> pClass, final Util pUtil, final JavaFormatter pJavaFormatter) {
		this.mGenJavaClassSourceFileWriter = new GenFileWriter<ProxyJavaClassSourceFileSegment>(pUtil.getGenJavaClassSourceFile(pProxyJavaRoot, pClass), pJavaFormatter);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void begin() {
		this.mGenJavaClassSourceFileWriter.begin();
	}

	public void end() throws IOException {
		this.mGenJavaClassSourceFileWriter.end();
	}

	public GenFileWriterSegment append(final ProxyJavaClassSourceFileSegment pProxyJavaClassSourceFileSegment, final String pString) {
		return this.mGenJavaClassSourceFileWriter.append(pProxyJavaClassSourceFileSegment, pString);
	}

	public GenFileWriterSegment append(final ProxyJavaClassSourceFileSegment pProxyJavaClassSourceFileSegment, final String pString, final Object ... pArguments) {
		return this.mGenJavaClassSourceFileWriter.append(pProxyJavaClassSourceFileSegment, pString, pArguments);
	}

	public GenFileWriterSegment endLine(final ProxyJavaClassSourceFileSegment pProxyJavaClassSourceFileSegment) {
		return this.mGenJavaClassSourceFileWriter.endLine(pProxyJavaClassSourceFileSegment);
	}

	public GenFileWriterSegment incrementIndent(final ProxyJavaClassSourceFileSegment pProxyJavaClassSourceFileSegment) {
		return this.mGenJavaClassSourceFileWriter.incrementIndent(pProxyJavaClassSourceFileSegment);
	}

	public GenFileWriterSegment decrementIndent(final ProxyJavaClassSourceFileSegment pProxyJavaClassSourceFileSegment) {
		return this.mGenJavaClassSourceFileWriter.decrementIndent(pProxyJavaClassSourceFileSegment);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum ProxyJavaClassSourceFileSegment {
		// ===========================================================
		// Elements
		// ===========================================================

		PACKAGE,
		IMPORTS,
		CLASS_START,
		STATIC_METHODS,
		CONSTANTS,
		FIELDS,
		CONSTRUCTORS,
		GETTERS_SETTERS,
		METHODS,
		CLASS_END;

		// ===========================================================
		// Constants
		// ===========================================================

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
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
