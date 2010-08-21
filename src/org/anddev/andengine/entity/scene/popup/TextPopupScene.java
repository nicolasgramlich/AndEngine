package org.anddev.andengine.entity.scene.popup;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.modifier.IShapeModifier;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.util.HorizontalAlign;

/**
 * @author Nicolas Gramlich
 * @since 17:19:30 - 03.08.2010
 */
public class TextPopupScene extends PopupScene {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextPopupScene(final Camera pCamera, final Scene pParentScene, final Font pFont, final String pText, final float pDurationSeconds) {
		this(pCamera, pParentScene, pFont, pText, pDurationSeconds, null);
	}

	public TextPopupScene(final Camera pCamera, final Scene pParentScene, final Font pFont, final String pText, final float pDurationSeconds, final IShapeModifier pShapeModifier) {
		this(pCamera, pParentScene, pFont, pText, pDurationSeconds, pShapeModifier, null);
	}

	public TextPopupScene(final Camera pCamera, final Scene pParentScene, final Font pFont, final String pText, final float pDurationSeconds, final IShapeModifier pShapeModifier, final Runnable pRunnable) {
		super(pCamera, pParentScene, pDurationSeconds, pRunnable);

		final Text text = new Text(0, 0, pFont, pText, HorizontalAlign.CENTER);
		pCamera.centerShapeInCamera(text);

		if(pShapeModifier != null) {
			text.addShapeModifier(pShapeModifier);
		}

		this.getTopLayer().addEntity(text);
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
