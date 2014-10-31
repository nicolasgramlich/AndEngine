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
 * @since 14:27:32 - 10.04.2012
 */
public class JavaScriptCppClassFileWriter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final GenFileWriter<JavaScriptClassSourceFileSegment> mGenCppClassSourceFileWriter;
	private final GenFileWriter<GenCppClassHeaderFileSegment> mGenCppClassHeaderFileWriter;

	// ===========================================================
	// Constructors
	// ===========================================================

	public JavaScriptCppClassFileWriter(final File pProxyCppRoot, final Class<?> pClass, final Util pUtil, final CppFormatter pCppFormatter) {
		this.mGenCppClassSourceFileWriter = new GenFileWriter<JavaScriptClassSourceFileSegment>(pUtil.getJavaScriptCppClassSourceFile(pProxyCppRoot, pClass), pCppFormatter);
		this.mGenCppClassHeaderFileWriter = new GenFileWriter<GenCppClassHeaderFileSegment>(pUtil.getJavaScriptCppClassHeaderFile(pProxyCppRoot, pClass), pCppFormatter);
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

	public GenFileWriterSegment append(final JavaScriptClassSourceFileSegment pJavaScriptClassSourceFileSegment, final String pString) {
		return this.mGenCppClassSourceFileWriter.append(pJavaScriptClassSourceFileSegment, pString);
	}

	public GenFileWriterSegment append(final JavaScriptClassSourceFileSegment pJavaScriptClassSourceFileSegment, final String pString, final Object ... pArguments) {
		return this.mGenCppClassSourceFileWriter.append(pJavaScriptClassSourceFileSegment, pString, pArguments);
	}

	public GenFileWriterSegment endLine(final JavaScriptClassSourceFileSegment pJavaScriptClassSourceFileSegment) {
		return this.mGenCppClassSourceFileWriter.endLine(pJavaScriptClassSourceFileSegment);
	}

	public GenFileWriterSegment append(final GenCppClassHeaderFileSegment pGenCppClassHeaderFileSegment, final String pString) {
		return this.mGenCppClassHeaderFileWriter.append(pGenCppClassHeaderFileSegment, pString);
	}

	public GenFileWriterSegment append(final GenCppClassHeaderFileSegment pGenCppClassHeaderFileSegment, final String pString, final Object ... pArguments) {
		return this.mGenCppClassHeaderFileWriter.append(pGenCppClassHeaderFileSegment, pString, pArguments);
	}

	public GenFileWriterSegment endLine(final GenCppClassHeaderFileSegment pGenCppClassHeaderFileSegment) {
		return this.mGenCppClassHeaderFileWriter.endLine(pGenCppClassHeaderFileSegment);
	}

	public GenFileWriterSegment incrementIndent(final GenCppClassHeaderFileSegment pGenCppClassHeaderFileSegment) {
		return this.mGenCppClassHeaderFileWriter.incrementIndent(pGenCppClassHeaderFileSegment);
	}

	public GenFileWriterSegment incrementIndent(final JavaScriptClassSourceFileSegment pJavaScriptClassSourceFileSegment) {
		return this.mGenCppClassSourceFileWriter.incrementIndent(pJavaScriptClassSourceFileSegment);
	}

	public GenFileWriterSegment decrementIndent(final GenCppClassHeaderFileSegment pGenCppClassHeaderFileSegment) {
		return this.mGenCppClassHeaderFileWriter.decrementIndent(pGenCppClassHeaderFileSegment);
	}

	public GenFileWriterSegment decrementIndent(final JavaScriptClassSourceFileSegment pJavaScriptClassSourceFileSegment) {
		return this.mGenCppClassSourceFileWriter.decrementIndent(pJavaScriptClassSourceFileSegment);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static enum JavaScriptClassSourceFileSegment {
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

	public static enum GenCppClassHeaderFileSegment {
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
