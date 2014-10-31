package org.andengine.extension.scripting.generator.util.adt.io;

import java.io.File;
import java.io.IOException;

import org.andengine.extension.scripting.generator.util.Util;
import org.andengine.extension.scripting.generator.util.adt.CppFormatter;
import org.andengine.extension.scripting.generator.util.adt.io.GenFileWriter.GenFileWriterSegment;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:02:38 - 21.03.2012
 */
public class ProxyCppClassFileWriter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final GenFileWriter<ProxyCppClassSourceFileSegment> mGenCppClassSourceFileWriter;
	private final GenFileWriter<ProxyCppClassHeaderFileSegment> mGenCppClassHeaderFileWriter;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ProxyCppClassFileWriter(final File pProxyCppRoot, final Class<?> pClass, final Util pUtil, final CppFormatter pCppFormatter) {
		this(pProxyCppRoot, pClass, pUtil, pCppFormatter, false);
	}

	public ProxyCppClassFileWriter(final File pProxyCppRoot, final Class<?> pClass, final Util pUtil, final CppFormatter pCppFormatter, final boolean pHeaderFileOnly) {
		if(pHeaderFileOnly) {
			this.mGenCppClassSourceFileWriter = null;
		} else {
			this.mGenCppClassSourceFileWriter = new GenFileWriter<ProxyCppClassSourceFileSegment>(pUtil.getProxyCppClassSourceFile(pProxyCppRoot, pClass), pCppFormatter);
		}
		this.mGenCppClassHeaderFileWriter = new GenFileWriter<ProxyCppClassHeaderFileSegment>(pUtil.getProxyCppClassHeaderFile(pProxyCppRoot, pClass), pCppFormatter);
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

	public void begin() throws IOException {
		if(this.mGenCppClassSourceFileWriter != null) {
			this.mGenCppClassSourceFileWriter.begin();
		}
		this.mGenCppClassHeaderFileWriter.begin();
	}

	public void end() throws IOException {
		if(this.mGenCppClassSourceFileWriter != null) {
			this.mGenCppClassSourceFileWriter.end();
		}
		this.mGenCppClassHeaderFileWriter.end();
	}

	public GenFileWriterSegment append(final ProxyCppClassSourceFileSegment pProxyCppClassSourceFileSegment, final String pString) {
		return this.mGenCppClassSourceFileWriter.append(pProxyCppClassSourceFileSegment, pString);
	}

	public GenFileWriterSegment append(final ProxyCppClassSourceFileSegment pProxyCppClassSourceFileSegment, final String pString, final Object ... pArguments) {
		return this.mGenCppClassSourceFileWriter.append(pProxyCppClassSourceFileSegment, pString, pArguments);
	}

	public GenFileWriterSegment endLine(final ProxyCppClassSourceFileSegment pProxyCppClassSourceFileSegment) {
		return this.mGenCppClassSourceFileWriter.endLine(pProxyCppClassSourceFileSegment);
	}

	public GenFileWriterSegment append(final ProxyCppClassHeaderFileSegment pProxyCppClassHeaderFileSegment, final String pString) {
		return this.mGenCppClassHeaderFileWriter.append(pProxyCppClassHeaderFileSegment, pString);
	}

	public GenFileWriterSegment append(final ProxyCppClassHeaderFileSegment pProxyCppClassHeaderFileSegment, final String pString, final Object ... pArguments) {
		return this.mGenCppClassHeaderFileWriter.append(pProxyCppClassHeaderFileSegment, pString, pArguments);
	}

	public GenFileWriterSegment endLine(final ProxyCppClassHeaderFileSegment pProxyCppClassHeaderFileSegment) {
		return this.mGenCppClassHeaderFileWriter.endLine(pProxyCppClassHeaderFileSegment);
	}

	public GenFileWriterSegment incrementIndent(final ProxyCppClassHeaderFileSegment pProxyCppClassHeaderFileSegment) {
		return this.mGenCppClassHeaderFileWriter.incrementIndent(pProxyCppClassHeaderFileSegment);
	}

	public GenFileWriterSegment incrementIndent(final ProxyCppClassSourceFileSegment pProxyCppClassSourceFileSegment) {
		return this.mGenCppClassSourceFileWriter.incrementIndent(pProxyCppClassSourceFileSegment);
	}

	public GenFileWriterSegment decrementIndent(final ProxyCppClassHeaderFileSegment pProxyCppClassHeaderFileSegment) {
		return this.mGenCppClassHeaderFileWriter.decrementIndent(pProxyCppClassHeaderFileSegment);
	}

	public GenFileWriterSegment decrementIndent(final ProxyCppClassSourceFileSegment pProxyCppClassSourceFileSegment) {
		return this.mGenCppClassSourceFileWriter.decrementIndent(pProxyCppClassSourceFileSegment);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum ProxyCppClassSourceFileSegment {
		// ===========================================================
		// Elements
		// ===========================================================

		INCLUDES,
		STATICS,
		CLASS_INIT,
		JNI_EXPORTS,
		METHODS;

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

	public static enum ProxyCppClassHeaderFileSegment {
		// ===========================================================
		// Elements
		// ===========================================================

		CLASS_IFDEF_HEAD,
		INCLUDES,
		EXTERNS,
		CLASS_START,
		METHODS_PUBLIC,
		METHODS_PROTECTED,
		METHODS_PRIVATE,
		CLASS_END,
		CLASS_IFDEF_FOOT;
		
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
