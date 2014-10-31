package org.andengine.extension.scripting.generator;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import org.andengine.extension.scripting.generator.util.Util;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyCppClassFileWriter;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyCppClassFileWriter.ProxyCppClassHeaderFileSegment;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyJavaClassFileWriter;
import org.andengine.extension.scripting.generator.util.adt.io.ProxyJavaClassFileWriter.ProxyJavaClassSourceFileSegment;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:48:25 - 20.03.2012
 */
public class Generator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final Util mUtil;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Generator(final Util pUtil) {
		this.mUtil = pUtil;
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

	protected void generateParameterImportsAndIncludes(final AccessibleObject pAccessibleObject, final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final ProxyCppClassFileWriter pProxyCppClassFileWriter) {
		if(pAccessibleObject instanceof Constructor<?>) {
			final Constructor<?> constructor = (Constructor<?>)pAccessibleObject;
			final Class<?>[] parameterTypes = constructor.getParameterTypes();

			this.generateImports(pProxyJavaClassFileWriter, parameterTypes);
			this.generateIncludes(pProxyCppClassFileWriter, parameterTypes);
		} else if(pAccessibleObject instanceof Method) {
			final Method method = (Method)pAccessibleObject;
			final Class<?>[] parameterTypes = method.getParameterTypes();

			this.generateImports(pProxyJavaClassFileWriter, parameterTypes);
			this.generateIncludes(pProxyCppClassFileWriter, parameterTypes);

			this.generateImports(pProxyJavaClassFileWriter, method.getReturnType());
			this.generateIncludes(pProxyCppClassFileWriter, method.getReturnType());
		} else {
			throw new IllegalArgumentException();
		}
	}

	protected void generateIncludes(final ProxyCppClassFileWriter pProxyCppClassFileWriter, final AccessibleObject pAccessibleObject) {
		if(pAccessibleObject instanceof Constructor<?>) {
			final Constructor<?> constructor = (Constructor<?>)pAccessibleObject;

			this.generateIncludes(pProxyCppClassFileWriter, constructor.getParameterTypes());
		} else if(pAccessibleObject instanceof Method) {
			final Method method = (Method)pAccessibleObject;
			final Class<?>[] parameterTypes = method.getParameterTypes();

			this.generateIncludes(pProxyCppClassFileWriter, parameterTypes);
			this.generateIncludes(pProxyCppClassFileWriter, method.getReturnType());
		} else {
			throw new IllegalArgumentException();
		}
	}

	protected void generateImports(final ProxyJavaClassFileWriter pProxyJavaClassFileWriter, final Class<?> ... pTypes) {
		for(final Class<?> type : pTypes) {
			if(!this.mUtil.isPrimitiveType(type)) {
				final String genJavaImportClassName = this.mUtil.getGenJavaClassImport(type);
				pProxyJavaClassFileWriter.append(ProxyJavaClassSourceFileSegment.IMPORTS, genJavaImportClassName).end();
			}
		}
	}

	protected void generateIncludes(final ProxyCppClassFileWriter pProxyCppClassFileWriter, final Class<?> ... pTypes) {
		for(final Class<?> type : pTypes) {
			if(!this.mUtil.isPrimitiveType(type)) {
				final String genCppIncludeClassName = this.mUtil.getGenCppClassInclude(type);
				pProxyCppClassFileWriter.append(ProxyCppClassHeaderFileSegment.INCLUDES, genCppIncludeClassName).end();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}