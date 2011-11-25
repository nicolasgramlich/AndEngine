package org.andengine.opengl.vbo;

import org.andengine.opengl.shader.ShaderProgram;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:32:10 - 15.11.2011
 */
public interface IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isManaged();
	public void setManaged(final boolean pManaged);

	public int getHardwareBufferID();

	public boolean isLoadedToHardware();
	public void loadToHardware();
	public void unloadFromHardware();

	public boolean isDirtyOnHardware();
	public void setDirtyOnHardware();

	public int getCapacity();
	public int getByteCapacity();

	public void bind(final ShaderProgram pShaderProgram);
	public void unbind(final ShaderProgram pShaderProgram);

	public void loadToActiveBufferObjectManager();
	public void unloadFromActiveBufferObjectManager();

	public void draw(final int pPrimitiveType, final int pCount);
	public void draw(final int pPrimitiveType, final int pOffset, final int pCount);
}