package org.anddev.andengine.opengl.view;

import javax.microedition.khronos.opengles.GL;

/**
 * An interface used to wrap a GL interface.
 * <p>
 * Typically used for implementing debugging and tracing on top of the default
 * GL interface. You would typically use this by creating your own class that
 * implemented all the GL methods by delegating to another GL instance. Then you
 * could add your own behavior before or after calling the delegate. All the
 * GLWrapper would do was instantiate and return the wrapper GL instance:
 * 
 * <pre class="prettyprint">
 * class MyGLWrapper implements GLWrapper {
 *     GL wrap(GL gl) {
 *         return new MyGLImplementation(gl);
 *     }
 *     static class MyGLImplementation implements GL,GL10,GL11,... {
 *         ...
 *     }
 * }
 * </pre>
 * 
 * @see #setGLWrapper(GLWrapper)
 *
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:53:38 - 28.06.2010
 */
public interface GLWrapper {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Wraps a gl interface in another gl interface.
	 * 
	 * @param pGL a GL interface that is to be wrapped.
	 * @return either the input argument or another GL object that wraps the
	 *         input argument.
	 */
	public GL wrap(final GL pGL);
}