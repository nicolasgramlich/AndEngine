package org.andengine.opengl.vbo;

import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.util.GLState;
import org.andengine.util.IDisposable;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:32:10 - 15.11.2011
 */
public interface IVertexBufferObject extends IDisposable {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isAutoDispose();

	public int getHardwareBufferID();

	public boolean isLoadedToHardware();
	public void setLoadedToHardware(final boolean pLoadedToHardware);
	public void loadToHardware(final GLState pGLState);
	public void unloadFromHardware(final GLState pGLState);

	public boolean isDirtyOnHardware();
	public void setDirtyOnHardware();

	public int getCapacity();
	public int getByteCapacity();

	public void bind(final GLState pGLState, final ShaderProgram pShaderProgram);
	public void unbind(final GLState pGLState, final ShaderProgram pShaderProgram);

	public VertexBufferObjectManager getVertexBufferObjectManager();

	public void draw(final int pPrimitiveType, final int pCount);
	public void draw(final int pPrimitiveType, final int pOffset, final int pCount);
}