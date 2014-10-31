package org.andengine.extension.scripting.generator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.andengine.extension.scripting.generator.util.Util;
import org.andengine.extension.scripting.generator.util.adt.CppFormatter;
import org.andengine.extension.scripting.generator.util.adt.JavaFormatter;
import org.andengine.extension.scripting.generator.util.adt.io.JavaScriptCppClassFileWriter;
import org.andengine.extension.scripting.generator.util.adt.io.JavaScriptCppClassFileWriter.JavaScriptClassSourceFileSegment;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyCppClassFileWriter;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyCppClassFileWriter.ProxyCppClassHeaderFileSegment;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyCppClassFileWriter.ProxyCppClassSourceFileSegment;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyJavaClassFileWriter;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyJavaClassFileWriter.ProxyJavaClassSourceFileSegment;

import com.thoughtworks.paranamer.ParameterNamesNotFoundException;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:48:25 - 20.03.2012
 */
public class ClassGenerator extends Generator {
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

	public ClassGenerator(final File pProxyJavaRoot, final File pProxyCppRoot, final File pJavaScriptCppRoot, final JavaFormatter pGenJavaFormatter, final CppFormatter pGenCppFormatter, final CppFormatter pJavaScriptCppFormatter, final Util pUtil, boolean pGenerateJavaScriptClass) {
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

	public void generateClassCode(final Class<?> pClass) throws IOException {
		final ProxyJavaClassFileWriter proxyJavaClassFileWriter = new ProxyJavaClassFileWriter(this.mProxyJavaRoot, pClass, this.mUtil, this.mProxyJavaFormatter);
		final ProxyCppClassFileWriter proxyCppClassFileWriter = new ProxyCppClassFileWriter(this.mProxyCppRoot, pClass, this.mUtil, this.mProxyCppFormatter);
		final JavaScriptCppClassFileWriter javaScriptCppClassFileWriter = new JavaScriptCppClassFileWriter(this.mJavaScriptCppRoot, pClass, this.mUtil, this.mJavaScriptCppFormatter);

		proxyJavaClassFileWriter.begin();
		proxyCppClassFileWriter.begin();
		javaScriptCppClassFileWriter.begin();

		this.generateClassHeader(pClass, proxyJavaClassFileWriter, proxyCppClassFileWriter);
		this.generateClassFields(pClass, proxyJavaClassFileWriter, proxyCppClassFileWriter);

		this.generateClassConstructors(pClass, proxyJavaClassFileWriter, proxyCppClassFileWriter);

		if(!Modifier.isAbstract(pClass.getModifiers())) {
			this.generateClassMethods(pClass, proxyJavaClassFileWriter, proxyCppClassFileWriter);
		}
		this.generateClassFooter(pClass, proxyJavaClassFileWriter, proxyCppClassFileWriter);

		proxyJavaClassFileWriter.end();
		proxyCppClassFileWriter.end();
		javaScriptCppClassFileWriter.end();
	}

	private void generateClassHeader(final Class<?> pClass, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		final String genJavaClassName = this.mUtil.getGenJavaClassName(pClass);
		final String genJavaClassPackageName = this.mUtil.getGenJavaClassPackageName(pClass);
		final String genCppClassName = this.mUtil.getGenCppClassName(pClass);
		final String genCppNativeInitClassJNIExportMethodName = this.mUtil.getJNIExportMethodName(pClass, "initClass");

		/* Generate Java header. */
		{
			/* Package. */
			pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.PACKAGE, "package %s;", genJavaClassPackageName).end();

			/* Imports. */
			pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.IMPORTS, "import %s;", pClass.getName()).end();

			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.CONSTANTS);
			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.CONSTRUCTORS);
			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.FIELDS);
			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.GETTERS_SETTERS);
			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.METHODS);
			pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.STATIC_METHODS);

			/* Class. */
			if(Modifier.isAbstract(pClass.getModifiers())) {
				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CLASS_START, "public abstract class %s extends %s {", genJavaClassName, pClass.getSimpleName()).end();
			} else {
				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CLASS_START, "public class %s extends %s{", genJavaClassName, pClass.getSimpleName()).end();
			}

			pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.STATIC_METHODS, "public static native void nativeInitClass();").end();
		}

		/* Generate native header. */
		{
			/* Header. */
			{
				/* #ifdef. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_IFDEF_HEAD, "#ifndef " + genCppClassName + "_H").end();
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_IFDEF_HEAD, "#define " + genCppClassName + "_H").end();

				/* Imports. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, "#include <memory>").end();
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, "#include <jni.h>").end();
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, "#include \"src/AndEngineScriptingExtension.h\"").end();

				/* Externs. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.EXTERNS, "extern \"C\" {").end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.EXTERNS);

				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.EXTERNS, "JNIEXPORT void JNICALL %s(JNIEnv*, jclass);", genCppNativeInitClassJNIExportMethodName).end();

				/* Class. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, "class %s : ", genCppClassName);
				final Class<?> superclass = pClass.getSuperclass();
				if(Object.class.equals(superclass)) {
					pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, "#include \"src/Wrapper.h\"").end();
					pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, "public Wrapper");
				} else {
					pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, this.mUtil.getGenCppClassInclude(superclass)).end();
					pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, "public %s", this.mUtil.getGenCppClassName(superclass));
				}
				final Class<?>[] interfaces = pClass.getInterfaces();
				for(final Class<?> interfaze : interfaces) {
					if(this.mUtil.isProxyClassIncluded(interfaze)) {
						this.generateIncludes(pProxyCppClassFileWriter, interfaze);
						pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, ", public %s", this.mUtil.getGenCppClassName(interfaze));
					}
				}
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.CLASS_START, " {").end();

				/* Methods. */
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC);
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "public:").end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC);

				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.METHODS_PROTECTED);
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PROTECTED, "protected:").end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.METHODS_PROTECTED);

				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.METHODS_PRIVATE);
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PRIVATE, "private:").end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassHeaderFileSegment.METHODS_PRIVATE);

				/* Wrapper-Constructor. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "%s(jobject);", genCppClassName).end();

				/* Unwrapper. */
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "virtual jobject unwrap();").end();
			}

			/* Source. */
			{
				/* Includes. */
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.INCLUDES, "#include <cstdlib>").end();
				final String genCppClassInclude = this.mUtil.getGenCppClassInclude(pClass);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.INCLUDES, genCppClassInclude).end();

				/* Statics. */
				final String genCppStaticClassMemberName = this.mUtil.getGenCppStaticClassMemberName(pClass);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.STATICS, "static jclass %s;", genCppStaticClassMemberName).end();

				/* Class init. */
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.CLASS_INIT, "JNIEXPORT void JNICALL %s(JNIEnv* pJNIEnv, jclass pJClass) {", genCppNativeInitClassJNIExportMethodName).end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassSourceFileSegment.CLASS_INIT);

				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.CLASS_INIT, "%s = (jclass)JNI_ENV()->NewGlobalRef(pJClass);", genCppStaticClassMemberName).end();

				/* Wrapper-Constructor. */
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "%s::%s(jobject p%s) {", genCppClassName, genCppClassName, genJavaClassName).end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassSourceFileSegment.METHODS);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "this->mUnwrapped = p%s;", genJavaClassName).end();
				pProxyCppClassFileWriter.decrementIndent(ProxyCppClassSourceFileSegment.METHODS);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "}").end();

				/* Unwrapper. */
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "jobject %s::unwrap() {", genCppClassName).end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassSourceFileSegment.METHODS);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "return this->mUnwrapped;").end();
				pProxyCppClassFileWriter.decrementIndent(ProxyCppClassSourceFileSegment.METHODS);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "}").end();
			}
		}
	}

	private void generateClassFooter(final Class<?> pClass, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
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

	private void generateClassFields(final Class<?> pClass, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.FIELDS, "private final long mAddress;").end();
	}

	private void generateClassConstructors(final Class<?> pClass, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) throws ParameterNamesNotFoundException {
		boolean zeroArgumentConstructorFound = false;
		final Constructor<?>[] constructors = pClass.getConstructors();
		for(final Constructor<?> constructor : constructors) {
			if(constructor.getParameterTypes().length == 0) {
				zeroArgumentConstructorFound = true;
			}
			this.generateClassConstructor(pClass, constructor, pProxyJavaClassFileWriter, pProxyCppClassFileWriter);
		}

		/* We need to generate a zero-arg constructor on the native side, so that the subclasses can make use of this constructor. */
		// TODO Think if generating a protected zero-arg constructor is viable in all cases.
		if(!zeroArgumentConstructorFound) {
			this.generateZeroArgumentNativeConstructor(pClass, pProxyJavaClassFileWriter, pProxyCppClassFileWriter);
		}
	}

	private void generateZeroArgumentNativeConstructor(final Class<?> pClass, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		final String genCppClassName = this.mUtil.getGenCppClassName(pClass);
		/* Header. */
		pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, genCppClassName).append("();").end();

		/* Source. */
		pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "%s::%s() {", genCppClassName, genCppClassName).end();
		pProxyCppClassFileWriter.endLine(ProxyCppClassSourceFileSegment.METHODS);
		pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "}").end();
	}

	private void generateClassConstructor(final Class<?> pClass, final Constructor<?> pConstructor, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		final String genJavaClassName = this.mUtil.getGenJavaClassName(pClass);
		final String genCppClassName = this.mUtil.getGenCppClassName(pClass);

		final int modifiers = pConstructor.getModifiers();
		if(!Modifier.isPrivate(modifiers)) {
			final String visibilityModifiers = this.mUtil.getVisibilityModifiersAsString(pConstructor);

			/* Generate Java constructors. */
			{
				final String methodParamatersAsString = this.mUtil.getJavaMethodParamatersAsString(pConstructor);
				final String methodCallParamatersAsString = this.mUtil.getJavaMethodCallParamatersAsString(pConstructor);

				if(pConstructor.isAnnotationPresent(Deprecated.class)) {
					pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, "@Deprecated").end();
				}
				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, "%s %s(", visibilityModifiers, genJavaClassName);
				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, "final long pAddress");
				if(methodParamatersAsString != null) {
					pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, ", %s", methodParamatersAsString);
				}
				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, ") ");
				final Class<?>[] exceptions = pConstructor.getExceptionTypes();
				if(exceptions.length > 0) {
					pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, "throws ");
					for(int i = 0; i < exceptions.length; i++) {
						final Class<?> exception = exceptions[i];
						pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, exception.getSimpleName());
						this.generateImports(pProxyJavaClassFileWriter, exception);
						final boolean isLastException = (i == (exceptions.length - 1));
						if(!isLastException) {
							pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, ", ");
						}
					}
				}
				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, "{").end();
				pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.CONSTRUCTORS);
				/* Super call. */
				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, "super(");
				if(methodCallParamatersAsString != null) {
					pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, methodCallParamatersAsString);
				}
				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, ");").end();

				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, "this.mAddress = pAddress;").end();
				pProxyJavaClassFileWriter.decrementIndent(ProxyJavaClassSourceFileSegment.CONSTRUCTORS);
				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.CONSTRUCTORS, "}").end();

				/* Add imports. */
				this.generateParameterImportsAndIncludes(pConstructor, pProxyJavaClassFileWriter, pProxyCppClassFileWriter);
			}

			/* Generate native constructor. */
			{
				final ProxyCppClassHeaderFileSegment proxyCppClassHeaderFileSegment = this.mUtil.getGenCppClassHeaderFileSegmentByVisibilityModifier(modifiers);

				final String genCppMethodHeaderParamatersAsString = this.mUtil.getGenCppMethodHeaderParamatersAsString(pConstructor);
				final String genCppMethodParamatersAsString = this.mUtil.getGenCppMethodParamatersAsString(pConstructor);
				final String genJNIMethodCallParamatersAsString = this.mUtil.getJNIMethodCallParamatersAsString(pConstructor);
				final String genCppStaticClassMemberName = this.mUtil.getGenCppStaticClassMemberName(pClass);
				final String jniMethodSignature = this.mUtil.getJNIMethodSignature(pConstructor);

				/* Header. */
				pProxyCppClassFileWriter.append(proxyCppClassHeaderFileSegment, genCppClassName);
				pProxyCppClassFileWriter.append(proxyCppClassHeaderFileSegment, "(");
				if(genCppMethodHeaderParamatersAsString != null) {
					pProxyCppClassFileWriter.append(proxyCppClassHeaderFileSegment, genCppMethodHeaderParamatersAsString);
				}
				pProxyCppClassFileWriter.append(proxyCppClassHeaderFileSegment, ");").end();

				final String constructorName = this.mUtil.getGenCppStaticMethodIDFieldName(pConstructor);
				/* Source. */
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.STATICS, "static jmethodID %s;", constructorName).end();

				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.CLASS_INIT, "%s = JNI_ENV()->GetMethodID(%s, \"<init>\", \"%s\");", constructorName, genCppStaticClassMemberName, jniMethodSignature).end();

				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "%s::%s(", genCppClassName, genCppClassName);
				if(genCppMethodParamatersAsString != null) {
					pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, genCppMethodParamatersAsString);
				}
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, ") {").end();
				pProxyCppClassFileWriter.incrementIndent(ProxyCppClassSourceFileSegment.METHODS);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "this->mUnwrapped = JNI_ENV()->NewObject(%s, %s", genCppStaticClassMemberName, constructorName);
				if(genJNIMethodCallParamatersAsString != null) {
					pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, ", (jlong)this, %s);", genJNIMethodCallParamatersAsString).end();
				} else {
					pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, ", (jlong)this);").end();
				}
				pProxyCppClassFileWriter.decrementIndent(ProxyCppClassSourceFileSegment.METHODS);
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "}").end();
			}
		}
	}

	private void generateClassMethods(final Class<?> pClass, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		for(final Method method : pClass.getMethods()) {
			if(this.mUtil.isProxyMethodIncluded(method)) {
				final String methodName = method.getName();
				if(methodName.startsWith("on")) {
					this.generateClassCallback(pClass, method, pProxyJavaClassFileWriter, pProxyCppClassFileWriter);
				} else {
					this.generateClassMethod(pClass, method, pProxyJavaClassFileWriter, pProxyCppClassFileWriter);
				}
			}
		}
	}

	private void generateClassCallback(final Class<?> pClass, final Method pMethod, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		final Class<?> returnType = pMethod.getReturnType();

		final String methodName = pMethod.getName();
		if((returnType == Boolean.TYPE) || (returnType == Void.TYPE)) {
			if(Modifier.isPublic(pMethod.getModifiers())) { // TODO Is this check correct?
				this.generateParameterImportsAndIncludes(pMethod, pProxyJavaClassFileWriter, pProxyCppClassFileWriter);

				final String[] parameterNames = this.mUtil.getParameterNames(pMethod);
				final Class<?>[] parameterTypes = this.mUtil.getParameterTypes(pMethod);

				/* Generate Java side of the callback. */
				final String javaNativeMethodName = this.mUtil.getJavaNativeMethodName(pMethod);
				final String jniExportMethodName = this.mUtil.getJNIExportMethodName(pClass, pMethod);
				final String genCppClassName = this.mUtil.getGenCppClassName(pClass);
				final String uncapitalizedGenCppClassName = this.mUtil.uncapitalizeFirstCharacter(genCppClassName);

				{
					final String visibilityModifier = this.mUtil.getVisibilityModifiersAsString(pMethod);
					final String methodParamatersAsString = this.mUtil.getJavaMethodParamatersAsString(pMethod);
					final String methodCallParamatersAsString = this.mUtil.getJavaMethodCallParamatersAsString(pMethod);

					/* Source. */
					pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "@Override").end();
					pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "%s %s %s(%s) {", visibilityModifier, returnType.getSimpleName(), methodName, (methodParamatersAsString != null) ? methodParamatersAsString : "").end();

					pProxyJavaClassFileWriter.incrementIndent(ProxyJavaClassSourceFileSegment.METHODS);
					if(returnType == Void.TYPE) {
						pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "if(!this.%s(this.mAddress%s)) {", javaNativeMethodName, (methodCallParamatersAsString != null) ? ", " + methodCallParamatersAsString : "");
						pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "\tsuper.%s(%s);", methodName, (methodCallParamatersAsString != null) ? methodCallParamatersAsString : "");
						pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "}").end();
					} else if(returnType == Boolean.TYPE) {
						pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "final boolean handledNative = this.%s(this.mAddress%s);", javaNativeMethodName, (methodCallParamatersAsString != null) ?  ", " + methodCallParamatersAsString : "");
						pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "if(handledNative) {").end();
						pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "\treturn true;").end();
						pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "} else {").end();
						pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "\treturn super.%s(%s);", methodName, (methodParamatersAsString != null) ? methodCallParamatersAsString : "");
						pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "}").end();
					} else {
						throw new IllegalStateException("Unexpected return type: '" + returnType.getName() + "'.");
					}
					pProxyJavaClassFileWriter.decrementIndent(ProxyJavaClassSourceFileSegment.METHODS);
					pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "}").end();

					pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.METHODS, "private native boolean %s(final long pAddress%s);", javaNativeMethodName, (methodParamatersAsString != null) ? ", " + methodParamatersAsString : "");
				}

				/* Generate native side of the callback. */
				{
					final String jniExportMethodHeaderParamatersAsString = this.mUtil.getJNIExportMethodHeaderParamatersAsString(pMethod);
					final String jniExportMethodParamatersAsString = this.mUtil.getJNIExportMethodParamatersAsString(pMethod);
					final String cppMethodHeaderParamatersAsString = this.mUtil.getGenCppMethodHeaderParamatersAsString(pMethod);
					final String cppMethodParamatersAsString = this.mUtil.getGenCppMethodParamatersAsString(pMethod);
					final String cppMethodCallParamatersAsString = this.mUtil.getGenCppMethodCallParamatersAsString(pMethod);

					/* Header. */
					{
						pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.EXTERNS, "JNIEXPORT jboolean JNICALL %s(%s);", jniExportMethodName, jniExportMethodHeaderParamatersAsString).end();

						pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "virtual jboolean %s(%s);", methodName, (cppMethodHeaderParamatersAsString != null) ? cppMethodHeaderParamatersAsString : "").end();
					}

					/* Source. */
					{
						pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.JNI_EXPORTS, "JNIEXPORT jboolean JNICALL %s(%s) {", jniExportMethodName, jniExportMethodParamatersAsString);
						pProxyCppClassFileWriter.incrementIndent(ProxyCppClassSourceFileSegment.JNI_EXPORTS);
						pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.JNI_EXPORTS, "%s* %s = (%s*)pAddress;", genCppClassName, uncapitalizedGenCppClassName, genCppClassName).end();

						/* Wrap non-primitives in local variables on the stack. */
						{
							this.generateIncludes(pProxyCppClassFileWriter, parameterTypes);
							for(int i = 0; i < parameterTypes.length; i++) {
								final Class<?> parameterType = parameterTypes[i];
								final String parameterName = parameterNames[i];
								if(!this.mUtil.isPrimitiveType(parameterType)) {
									final String genCppParameterTypeName = this.mUtil.getGenCppClassName(parameterType);
									final String uncapitalizedGenCppParameterTypeName = this.mUtil.getGenCppLocalVariableParameterName(parameterName);
									pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.JNI_EXPORTS, "%s %s(%s);", genCppParameterTypeName, uncapitalizedGenCppParameterTypeName, parameterName).end();
								}
							}
						}

						pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.JNI_EXPORTS, "return %s->%s(%s);", uncapitalizedGenCppClassName, methodName, (cppMethodCallParamatersAsString != null) ? cppMethodCallParamatersAsString : "");

						pProxyCppClassFileWriter.decrementIndent(ProxyCppClassSourceFileSegment.JNI_EXPORTS);
						pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.JNI_EXPORTS, "}").end();

						pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "jboolean %s::%s(%s) {", genCppClassName, methodName, (cppMethodParamatersAsString != null) ? cppMethodParamatersAsString : "").end();
						pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "\treturn false;").end();
						pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "}").end();
					}
				}
			} else {
				System.err.println("Skipping callback: " + pClass.getSimpleName() + "." + methodName + " -> " + returnType);
			}
		} else {
			System.err.println("Skipping callback: " + pClass.getSimpleName() + "." + methodName + " -> " + returnType);
		}
	}

	private void generateClassMethod(final Class<?> pClass, final Method pMethod, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		final Class<?> returnType = pMethod.getReturnType();

		this.generateParameterImportsAndIncludes(pMethod, pProxyJavaClassFileWriter, pProxyCppClassFileWriter);

		final String genCppMethodHeaderParamatersAsString = this.mUtil.getGenCppMethodHeaderParamatersAsString(pMethod);
		final String genCppMethodParamatersAsString = this.mUtil.getGenCppMethodParamatersAsString(pMethod);
		final String jniMethodCallParamatersAsString = this.mUtil.getJNIMethodCallParamatersAsString(pMethod);
		final String genCppStaticClassMemberName = this.mUtil.getGenCppStaticClassMemberName(pClass);
		final String genCppStaticMethodIDFieldName = this.mUtil.getGenCppStaticMethodIDFieldName(pMethod);
		final String jniMethodSignature = this.mUtil.getJNIMethodSignature(pMethod);
		final String returnTypeGenCppParameterTypeName = this.mUtil.getGenCppParameterTypeName(pMethod.getReturnType(), true);
		final String returnTypeGenCppParameterTypeNameWithoutPtr = this.mUtil.getGenCppParameterTypeName(pMethod.getReturnType(), false);
		final String genCppClassName = this.mUtil.getGenCppClassName(pClass);
		final String methodName = pMethod.getName();

		/* Generate native side of the getter. */
		{
			/* Generate virtual method in Header. */
			pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.METHODS_PUBLIC, "virtual %s %s(%s);", returnTypeGenCppParameterTypeName, methodName, (genCppMethodHeaderParamatersAsString != null) ? genCppMethodHeaderParamatersAsString : "").end(); // TODO Visiblity Modifier?

			/* Generate static methodID field. */
			pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.STATICS, "static jmethodID %s;", genCppStaticMethodIDFieldName).end();

			/* Cache static methodID field. */
			pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.CLASS_INIT, "%s = JNI_ENV()->GetMethodID(%s, \"%s\", \"%s\");", genCppStaticMethodIDFieldName, genCppStaticClassMemberName, methodName, jniMethodSignature).end();

			/* Call java method using static methodID field. */
			pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "%s %s::%s(%s) {", returnTypeGenCppParameterTypeName, genCppClassName, methodName, (genCppMethodParamatersAsString != null) ? genCppMethodParamatersAsString : "").end();

			final boolean primitiveReturnType = this.mUtil.isPrimitiveType(returnType, false);
			if(primitiveReturnType) {
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "\t");
				if(returnType != Void.TYPE) {
					pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "return ");
				}
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "JNI_ENV()->%s(this->mUnwrapped, %s%s);", this.mUtil.getJNICallXYZMethodName(pMethod.getReturnType()), genCppStaticMethodIDFieldName, (jniMethodCallParamatersAsString != null) ? ", " + jniMethodCallParamatersAsString: "").end();
			} else {
				pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "\treturn new %s(JNI_ENV()->%s(this->mUnwrapped, %s%s));", returnTypeGenCppParameterTypeNameWithoutPtr, this.mUtil.getJNICallXYZMethodName(pMethod.getReturnType()), genCppStaticMethodIDFieldName, (jniMethodCallParamatersAsString != null) ? ", " + jniMethodCallParamatersAsString: "").end();
			}
			pProxyCppClassFileWriter.append(ProxyCppClassSourceFileSegment.METHODS, "}").end();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}