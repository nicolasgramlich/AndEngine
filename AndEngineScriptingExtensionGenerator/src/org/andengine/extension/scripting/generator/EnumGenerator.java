package org.andengine.extension.scripting.generator;

import java.io.File;
import java.io.IOException;

import org.andengine.extension.scripting.generator.util.Util;
import org.andengine.extension.scripting.generator.util.adt.CppFormatter;
import org.andengine.extension.scripting.generator.util.adt.JavaFormatter;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyCppClassFileWriter;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyCppClassFileWriter.ProxyCppClassHeaderFileSegment;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyCppClassFileWriter.ProxyCppClassSourceFileSegment;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyJavaClassFileWriter;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyJavaClassFileWriter.ProxyJavaClassSourceFileSegment;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:24:02 - 03.04.2012
 */
public class EnumGenerator extends Generator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final File mProxyJavaRoot;
	private final File mProxyCppRoot;
	private final File mJavaScriptCppRoot;
	private final JavaFormatter mProxyJavaFormatter;
	private final CppFormatter mProxyCppFormatter;
	private final CppFormatter mJavaScriptCppFormatter;

	private final boolean mGenerateJavaScriptClass;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EnumGenerator(final File pProxyJavaRoot, final File pProxyCppRoot, final File pJavaScriptCppRoot, final JavaFormatter pGenJavaFormatter, final CppFormatter pGenCppFormatter, final CppFormatter pJavaScriptCppFormatter, final Util pUtil, boolean pGenerateJavaScriptClass) {
		super(pUtil);

		this.mProxyJavaRoot = pProxyJavaRoot;
		this.mProxyCppRoot = pProxyCppRoot;
		this.mJavaScriptCppRoot = pJavaScriptCppRoot;
		this.mProxyJavaFormatter = pGenJavaFormatter;
		this.mProxyCppFormatter = pGenCppFormatter;
		this.mJavaScriptCppFormatter = pJavaScriptCppFormatter;
		this.mGenerateJavaScriptClass = pGenerateJavaScriptClass;
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

	public void generateEnumCode(final Class<?> pClass) throws IOException {
		final ProxyJavaClassFileWriter proxyJavaClassFileWriter = new ProxyJavaClassFileWriter(this.mProxyJavaRoot, pClass, this.mUtil, this.mProxyJavaFormatter);
		final ProxyCppClassFileWriter proxyCppClassFileWriter = new ProxyCppClassFileWriter(this.mProxyCppRoot, pClass, this.mUtil, this.mProxyCppFormatter);

		proxyJavaClassFileWriter.begin();
		proxyCppClassFileWriter.begin();

		this.generateEnumHeader(pClass, proxyJavaClassFileWriter, proxyCppClassFileWriter);
		this.generateEnumMethods(pClass, proxyJavaClassFileWriter, proxyCppClassFileWriter);
		this.generateEnumFooter(pClass, proxyJavaClassFileWriter, proxyCppClassFileWriter);

		proxyJavaClassFileWriter.end();
		proxyCppClassFileWriter.end();
	}

	private void generateEnumHeader(final Class<?> pClass, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		final String genJavaClassName = this.mUtil.getGenJavaClassName(pClass);
		final String genCppClassName = this.mUtil.getGenCppClassName(pClass);
		final String genJavaClassPackageName = this.mUtil.getGenJavaClassPackageName(pClass);
		final String genCppNativeInitClassJNIExportMethodName = this.mUtil.getJNIExportMethodName(pClass, "initClass");

		/* Generate Java header. */
		{
			/* Package. */
			pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.PACKAGE, "package %s;", genJavaClassPackageName).end();

			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.CONSTANTS);
			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.CONSTRUCTORS);
			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.FIELDS);
			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.GETTERS_SETTERS);
			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.METHODS);
			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.STATIC_METHODS);

			/* Class. */
			pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CLASS_START, "public class %s {", genJavaClassName).end();

			pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.STATIC_METHODS, "public static native void nativeInitClass();").end();
		}

		/* Generate native header. */
		{
			/* Header. */
			{
				/* #ifdef. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_IFDEF_HEAD, "#ifndef %s_H", genCppClassName).end();
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_IFDEF_HEAD, "#define %s_H", genCppClassName).end();

				/* Imports. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, "#include <jni.h>").end();
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, "#include \"src/AndEngineScriptingExtension.h\"").end();
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, "#include \"src/Wrapper.h\"").end();

				/* Externs. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.EXTERNS, "extern \"C\" {").end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.EXTERNS);

				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.EXTERNS, "JNIEXPORT void JNICALL %s(JNIEnv*, jclass);", genCppNativeInitClassJNIExportMethodName).end();

				/* Class. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, "class %s : public Wrapper {", genCppClassName).end();

				/* Methods. */
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC);
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "public:").end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC);

				/* Wrapper-Constructor */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "%s(jobject);", genCppClassName).end();

				for(final Object enumConstant : pClass.getEnumConstants()) {
					pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "static %s* %s;", genCppClassName, enumConstant.toString()).end();
				}
			}

			/* Source. */
			{
				/* Includes. */
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.INCLUDES, "#include <cstdlib>").end();
				final String genCppClassInclude = this.mUtil.getGenCppClassInclude(pClass);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.INCLUDES, genCppClassInclude).end();

				/* Statics. */
				final String genCppStaticClassMemberName = this.mUtil.getGenCppStaticClassMemberName(pClass, true);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.STATICS, "static jclass %s;", genCppStaticClassMemberName).end();

				/* Class init. */
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.CLASS_INIT, "JNIEXPORT void JNICALL %s(JNIEnv* pJNIEnv, jclass pJClass) {", genCppNativeInitClassJNIExportMethodName).end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassSourceFileSegment.CLASS_INIT);

				final String genCppFullyQualifiedClassName = this.mUtil.getGenCppFullyQualifiedClassName(pClass, true);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.CLASS_INIT, "%s = (jclass)JNI_ENV()->NewGlobalRef(JNI_ENV()->FindClass(\"%s\"));", genCppStaticClassMemberName, genCppFullyQualifiedClassName).end();

				/* Enum-Values. */
				final String jniSignatureType = this.mUtil.getJNIMethodSignatureType(pClass);
				for(final Object enumConstant : pClass.getEnumConstants()) {
					final String enumName = enumConstant.toString();
					pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.STATICS, "%s* %s::%s = NULL;", genCppClassName, genCppClassName, enumName).end();

					final String jfieldIDLocalVariableName = genCppClassName + "_" + enumName + "_ID";
					pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.CLASS_INIT, "jfieldID %s = JNI_ENV()->GetStaticFieldID(%s, \"%s\", \"%s\");", jfieldIDLocalVariableName, genCppStaticClassMemberName, enumName, jniSignatureType).end();
					pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.CLASS_INIT, "%s::%s = new %s(JNI_ENV()->GetStaticObjectField(%s, %s));", genCppClassName, enumName, genCppClassName, genCppStaticClassMemberName, jfieldIDLocalVariableName).end();
				}

				/* Wrapper-Constructor. */
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "%s::%s(jobject p%s) {", genCppClassName, genCppClassName, genJavaClassName).end();
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "\tthis->mUnwrapped = p%s;", genJavaClassName).end();
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "}").end();
			}
		}
	}

	private void generateEnumMethods(final Class<?> pClass, ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		// TODO
	}

	private void generateEnumFooter(final Class<?> pClass, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		/* Generate Java footer. */
		{
			/* Class. */
			pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CLASS_END, "}").end();
		}

		/* Generate native footer. */
		{
			/* Externs. */
			pProxyCppClassFileWriter.decrementIndent(ProxyCppClassHeaderFileSegment.EXTERNS);
			pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.EXTERNS, "}").end();

			/* Class init. */
			pProxyCppClassFileWriter.decrementIndent(ProxyCppClassSourceFileSegment.CLASS_INIT);
			pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.CLASS_INIT, "}").end();

			/* Class. */
			pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_END, "};").end();
			pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_END, "#endif").end();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}