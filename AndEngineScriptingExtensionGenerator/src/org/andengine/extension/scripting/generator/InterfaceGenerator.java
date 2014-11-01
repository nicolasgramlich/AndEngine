package org.andengine.extension.scripting.generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import org.andengine.extension.scripting.generator.util.Util;
import org.andengine.extension.scripting.generator.util.adt.CppFormatter;
import org.andengine.extension.scripting.generator.util.adt.JavaFormatter;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyCppClassFileWriter;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyCppClassFileWriter.ProxyCppClassHeaderFileSegment;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:48:25 - 20.03.2012
 */
public class InterfaceGenerator extends Generator {
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

	public InterfaceGenerator(final File pProxyJavaRoot, final File pProxyCppRoot, final File pJavaScriptCppRoot, final JavaFormatter pGenJavaFormatter, final CppFormatter pGenCppFormatter, final CppFormatter pJavaScriptCppFormatter, final Util pUtil, boolean pGenerateJavaScriptClass) {
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

	public void generateInterfaceCode(final Class<?> pClass) throws IOException {
		final ProxyCppClassFileWriter proxyCppClassFileWriter = new ProxyCppClassFileWriter(this.mProxyCppRoot, pClass, this.mUtil, this.mProxyCppFormatter, true);
		proxyCppClassFileWriter.begin();

		this.generateInterfaceHeader(pClass, proxyCppClassFileWriter);
		this.generateInterfaceMethods(pClass, proxyCppClassFileWriter);
		this.generateInterfaceFooter(pClass, proxyCppClassFileWriter);

		proxyCppClassFileWriter.end();
	}

	private void generateInterfaceHeader(final Class<?> pClass, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		final String genCppClassName = this.mUtil.getGenCppClassName(pClass);
		/* Generate native header. */
		{
			/* Header. */
			{
				/* #ifdef. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_IFDEF_HEAD, "#ifndef %s_H", genCppClassName).end();
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_IFDEF_HEAD, "#define %s_H", genCppClassName).end();

				/* Imports. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, "#include <memory>").end();
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, "#include <jni.h>").end();

				/* Class. */
				final Class<?>[] interfaces = pClass.getInterfaces();
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, "class %s", genCppClassName);
				this.generateIncludes(pProxyCppClassFileWriter, interfaces);
				if(interfaces.length > 0) {
					pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, " : ");

					for(int i = 0; i < interfaces.length; i++) {
						if(i > 0) {
							pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, ", ");
						}
						final Class<?> interfaze = interfaces[i];
						pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, "public %s", this.mUtil.getGenCppClassName(interfaze));
					}
				}
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, " {").end();

				/* Methods. */
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC);
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "public:").end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC);
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "virtual ~%s() { };", genCppClassName).end();
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "virtual jobject unwrap() = 0;").end();
			}
		}
	}

	private void generateInterfaceMethods(final Class<?> pClass, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		for(final Method method : pClass.getMethods()) {
			if(this.mUtil.isProxyMethodIncluded(method)) {
				final String methodName = method.getName();
				if(methodName.startsWith("on")) {
					this.generateIncludes(pProxyCppClassFileWriter, method.getParameterTypes());
					this.generateInterfaceCallback(pClass, method, pProxyCppClassFileWriter);
				} else {
					this.generateIncludes(pProxyCppClassFileWriter, method);
					this.generateInterfaceMethod(pClass, method, pProxyCppClassFileWriter);
				}
			}
		}
	}

	private void generateInterfaceMethod(final Class<?> pClass, final Method pMethod, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		final String genCppMethodHeaderParamatersAsString = this.mUtil.getGenCppMethodHeaderParamatersAsString(pMethod);
		final String methodName = pMethod.getName();

		final String returnTypeName = this.mUtil.getGenCppParameterTypeName(pMethod.getReturnType(), true);

		pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "virtual %s %s(%s) = 0;", returnTypeName, methodName, (genCppMethodHeaderParamatersAsString != null) ? genCppMethodHeaderParamatersAsString : "").end();
	}

	private void generateInterfaceCallback(final Class<?> pClass, final Method pMethod, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		final String genCppMethodHeaderParamatersAsString = this.mUtil.getGenCppMethodHeaderParamatersAsString(pMethod);
		final String methodName = pMethod.getName();

		final String returnTypeName;
		if(pMethod.getReturnType() == Void.TYPE) {
			returnTypeName = this.mUtil.getGenCppParameterTypeName(Boolean.TYPE);
		} else {
			returnTypeName = this.mUtil.getGenCppParameterTypeName(pMethod.getReturnType(), true);
		}

		pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "virtual %s %s(%s) = 0;", returnTypeName, methodName, (genCppMethodHeaderParamatersAsString != null) ? genCppMethodHeaderParamatersAsString : "");
	}

	private void generateInterfaceFooter(final Class<?> pClass, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		/* Generate native footer. */
		{
			/* Class. */
			pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_END, "};").end();
			pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_END, "#endif").end();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}