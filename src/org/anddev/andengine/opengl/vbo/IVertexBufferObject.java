package org.anddev.andengine.opengl.vbo;

import org.anddev.andengine.opengl.shader.ShaderProgram;

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

	public abstract boolean isManaged();
	public abstract void setManaged(final boolean pManaged);

	public abstract int getHardwareBufferID();

	public abstract boolean isLoadedToHardware();
	public abstract void loadToHardware();
	public abstract void unloadFromHardware();

	public abstract boolean isDirtyOnHardware();
	/** Mark this {@link IVertexBufferObject} dirty so it gets updated on the hardware. */
	public abstract void setDirtyOnHardware();

	public abstract int getCapacity();

	public abstract int getSize();

	public abstract void bind(final ShaderProgram pShaderProgram);
	public abstract void unbind(final ShaderProgram pShaderProgram);

	public abstract void loadToActiveBufferObjectManager();
	public abstract void unloadFromActiveBufferObjectManager();
}