package org.andengine.opengl.shader.source;

import org.andengine.opengl.shader.exception.ShaderProgramException;
import org.andengine.opengl.shader.source.criteria.IShaderSourceCriteria;
import org.andengine.opengl.util.GLState;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:30:12 - 10.10.2011
 */
public class CriteriaShaderSource implements IShaderSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final CriteriaShaderSourceEntry[] mCriteriaShaderSourceEntries;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CriteriaShaderSource(final CriteriaShaderSourceEntry ... pCriteriaShaderSourceEntries) {
		this.mCriteriaShaderSourceEntries = pCriteriaShaderSourceEntries;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String getShaderSource(final GLState pGLState) {
		for(int i = 0; i < this.mCriteriaShaderSourceEntries.length; i++) {
			final CriteriaShaderSourceEntry criteriaShaderSourceEntry = this.mCriteriaShaderSourceEntries[i];
			if(criteriaShaderSourceEntry.isMet(pGLState)) {
				return criteriaShaderSourceEntry.getShaderSource();
			}
		}
		throw new ShaderProgramException("No " + CriteriaShaderSourceEntry.class.getSimpleName() + " met!");
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class CriteriaShaderSourceEntry {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final String mShaderSource;
		private final IShaderSourceCriteria[] mShaderSourceCriterias;

		// ===========================================================
		// Constructors
		// ===========================================================
		
		public CriteriaShaderSourceEntry(final String pShaderSource) {
			this(pShaderSource, (IShaderSourceCriteria[]) null);
		}

		public CriteriaShaderSourceEntry(final String pShaderSource, final IShaderSourceCriteria ... pCriterias) {
			this.mShaderSourceCriterias = pCriterias;
			this.mShaderSource = pShaderSource;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public String getShaderSource() {
			return this.mShaderSource;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public boolean isMet(final GLState pGLState) {
			if(this.mShaderSourceCriterias != null) {
				for(IShaderSourceCriteria shaderSourceCriteria : this.mShaderSourceCriterias) {
					if(!shaderSourceCriteria.isMet(pGLState)) {
						return false;
					}
				}
			}
			return true;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
