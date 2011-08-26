package org.anddev.andengine.opengl.shader;

import java.util.ArrayList;


/**
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:55:26 - 11.08.2011
 */
public class ShaderProgramManager {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<ShaderProgram> mShaderProgramsManaged = new ArrayList<ShaderProgram>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public ShaderProgramManager() {
		this.loadShaderProgram(PositionColorTextureCoordinatesShaderProgram.getInstance());
		this.loadShaderProgram(PositionTextureCoordinatesShaderProgram.getInstance());
		this.loadShaderProgram(PositionColorShaderProgram.getInstance());
		this.loadShaderProgram(PositionTextureCoordinatesTextureSelectShaderProgram.getInstance());
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

	public synchronized void clear() {
		this.mShaderProgramsManaged.clear();
	}

	public synchronized void loadShaderProgram(final ShaderProgram pShaderProgram) {
		if(pShaderProgram == null) {
			throw new IllegalArgumentException("pShaderProgram must not be null!");
		}
		this.mShaderProgramsManaged.add(pShaderProgram);
	}

	public void loadShaderPrograms(final ShaderProgram ... pShaderPrograms) {
		for(int i = pShaderPrograms.length - 1; i >= 0; i--) {
			this.loadShaderProgram(pShaderPrograms[i]);
		}
	}

	public synchronized void reloadShaderPrograms() {
		final ArrayList<ShaderProgram> managedShaderPrograms = this.mShaderProgramsManaged;
		for(int i = managedShaderPrograms.size() - 1; i >= 0; i--) {
			managedShaderPrograms.get(i).setCompiled(false);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
