package org.andengine.opengl.shader;

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

	private static final ArrayList<ShaderProgram> sShaderProgramsManaged = new ArrayList<ShaderProgram>();

	// ===========================================================
	// Constructors
	// ===========================================================

	private ShaderProgramManager() {

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

	public synchronized static void onCreate() {
		ShaderProgramManager.loadShaderProgram(PositionColorTextureCoordinatesShaderProgram.getInstance());
		ShaderProgramManager.loadShaderProgram(PositionTextureCoordinatesShaderProgram.getInstance());
		ShaderProgramManager.loadShaderProgram(PositionColorShaderProgram.getInstance());
		ShaderProgramManager.loadShaderProgram(PositionTextureCoordinatesTextureSelectShaderProgram.getInstance());
		ShaderProgramManager.loadShaderProgram(PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.getInstance());
	}

	public static synchronized void onDestroy() {
		final ArrayList<ShaderProgram> managedShaderPrograms = ShaderProgramManager.sShaderProgramsManaged;
		for(int i = managedShaderPrograms.size() - 1; i >= 0; i--) {
			managedShaderPrograms.get(i).setCompiled(false);
		}

		ShaderProgramManager.sShaderProgramsManaged.clear();
	}

	public static synchronized void loadShaderProgram(final ShaderProgram pShaderProgram) {
		if(pShaderProgram == null) {
			throw new IllegalArgumentException("pShaderProgram must not be null!");
		}
		ShaderProgramManager.sShaderProgramsManaged.add(pShaderProgram);
	}

	public static void loadShaderPrograms(final ShaderProgram ... pShaderPrograms) {
		for(int i = pShaderPrograms.length - 1; i >= 0; i--) {
			ShaderProgramManager.loadShaderProgram(pShaderPrograms[i]);
		}
	}

	public static synchronized void onReload() {
		final ArrayList<ShaderProgram> managedShaderPrograms = ShaderProgramManager.sShaderProgramsManaged;
		for(int i = managedShaderPrograms.size() - 1; i >= 0; i--) {
			managedShaderPrograms.get(i).setCompiled(false);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
