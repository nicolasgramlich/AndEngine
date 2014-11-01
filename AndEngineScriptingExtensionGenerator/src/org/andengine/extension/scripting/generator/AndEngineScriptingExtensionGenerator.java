package org.andengine.extension.scripting.generator;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.andengine.extension.scripting.generator.util.Util;
import org.andengine.extension.scripting.generator.util.adt.CppFormatter;
import org.andengine.extension.scripting.generator.util.adt.JavaFormatter;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:32:51 - 03.04.2012
 */
public class AndEngineScriptingExtensionGenerator {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	@Option(required = true, name = "-in-java-root") private File mInJavaRoot;
	@Option(required = true, name = "-in-javabin-root") private File mInJavaBinRoot;
	@Option(required = true, name = "-proxy-cpp-root") private File mProxyCppRoot;
	@Option(required = true, name = "-proxy-java-root") private File mProxyJavaRoot;

	@Option(required = false, name = "-proxy-java-class-suffix") private String mProxyJavaClassSuffix = "Proxy";
	@Option(required = false, name = "-proxy-java-formatter") private JavaFormatter mProxyJavaFormatter;

	@Option(required = false, name = "-proxy-cpp-class-suffix") private String mProxyCppClassSuffix = "";
	@Option(required = false, name = "-proxy-cpp-formatter") private CppFormatter mProxyCppFormatter;

	@Option(required = false, name = "-proxy-method-include", multiValued = true) private List<String> mProxyMethodsInclude;
	@Option(required = false, name = "-proxy-class-exclude", multiValued = true) private List<String> mProxyClassesExclude;

	@Option(required = true, name = "-proxy-class", multiValued = true) private List<String> mProxyClassNames;


	@Option(required = false, name = "-javascript-cpp-root") private File mJavaScriptCppRoot;

	@Option(required = false, name = "-javascript-cpp-class-prefix") private String mJavaScriptCppClassPrefix = "S_"; 
	@Option(required = false, name = "-javascript-cpp-class-suffix") private String mJavaScriptCppClassSuffix = "";

	@Option(required = false, name = "-javascript-cpp-formatter") private CppFormatter mJavaScriptCppFormatter;

	@Option(required = false, name = "-javascript-method-include", multiValued = true) private List<String> mJavaScriptMethodsInclude;

	@Option(required = true, name = "-javascript-class", multiValued = true) private List<String> mJavaScriptClassNames;

	private final File mInJavaBinRootClasses;

	private final Util mUtil;

	// ===========================================================
	// Constructors
	// ===========================================================

	public static void main(final String[] pArgs) throws Exception {
		new AndEngineScriptingExtensionGenerator(pArgs);
	}


	public AndEngineScriptingExtensionGenerator(final String[] pArgs) throws CmdLineException {
		final CmdLineParser parser = new CmdLineParser(this);
		parser.parseArgument(pArgs);

		this.mInJavaBinRootClasses = new File(this.mInJavaBinRoot, "classes/");
		this.mUtil = new Util(this.mProxyJavaClassSuffix, this.mProxyCppClassSuffix, this.mJavaScriptCppClassPrefix, this.mJavaScriptCppClassSuffix, this.mProxyMethodsInclude, this.mProxyClassesExclude, this.mJavaScriptMethodsInclude);

		this.checkArguments();

		this.generateCode();
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

	private void checkArguments() {
		if(!this.mInJavaRoot.exists()) {
			throw new IllegalArgumentException("TODO Explain!");
		}

		if(!this.mInJavaBinRoot.exists()) {
			throw new IllegalArgumentException("TODO Explain!");
		}

		if(!this.mProxyCppRoot.exists()) {
			throw new IllegalArgumentException("TODO Explain!");
		}

		if(!this.mProxyJavaRoot.exists()) {
			throw new IllegalArgumentException("TODO Explain!");
		}

		if(this.mProxyJavaClassSuffix.contains(" ")) {
			throw new IllegalArgumentException("TODO Explain!");
		}

		for(final String className : this.mProxyClassNames) {
			final File classSourceFile = this.mUtil.getInJavaClassSourceFile(this.mInJavaRoot, className);
			if(!classSourceFile.exists()) {
				throw new IllegalArgumentException("'" + classSourceFile + "' does not exist!");
			}
			final File classFile = this.mUtil.getInJavaClassFile(this.mInJavaBinRootClasses, className);
			if(!classFile.exists()) {
				throw new IllegalArgumentException("'" + classFile + "' does not exist!");
			}
		}
	}

	private void generateCode() {
		for(final String className : this.mProxyClassNames) {
			try {
				final URI uri = this.mInJavaBinRootClasses.toURI();
				final ClassLoader classLoader = new URLClassLoader(new URL[]{uri.toURL()});

				final Class<?> clazz = classLoader.loadClass(className);
				
				final boolean generateJavaScriptClass = this.hasJavaScriptClassName(className);

				System.out.format("Generating: '%s' ...", className);
				if(clazz.isInterface()) {
					new InterfaceGenerator(this.mProxyJavaRoot, this.mProxyCppRoot, this.mJavaScriptCppRoot, this.mProxyJavaFormatter, this.mProxyCppFormatter, this.mJavaScriptCppFormatter, this.mUtil, generateJavaScriptClass).generateInterfaceCode(clazz);
				} else if(clazz.isEnum()) {
					new EnumGenerator(this.mProxyJavaRoot, this.mProxyCppRoot, this.mJavaScriptCppRoot, this.mProxyJavaFormatter, this.mProxyCppFormatter, this.mJavaScriptCppFormatter, this.mUtil, generateJavaScriptClass).generateEnumCode(clazz);
				} else {
					new ClassGenerator(this.mProxyJavaRoot, this.mProxyCppRoot, this.mJavaScriptCppRoot, this.mProxyJavaFormatter, this.mProxyCppFormatter, this.mJavaScriptCppFormatter, this.mUtil, generateJavaScriptClass).generateClassCode(clazz);
				}
				System.out.println(" done!");
			} catch (final Throwable t) {
				t.printStackTrace();
				System.out.println(" ERROR!");
			}
		}
	}

	private boolean hasJavaScriptClassName(final String pClassName) {
		for(final String className : this.mJavaScriptClassNames) {
			if(className.equals(pClassName)) {
				return true;
			}
		}
		return false;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
