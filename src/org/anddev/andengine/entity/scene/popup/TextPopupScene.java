package org.anddev.andengine.entity.scene.popup;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.modifier.IEntityModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.util.HorizontalAlign;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	private final Text mText;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextPopupScene(final Camera pCamera, final Scene pParentScene, final Font pFont, final String pText, final float pDurationSeconds) {
		this(pCamera, pParentScene, pFont, pText, pDurationSeconds, null, null);
	}

	public TextPopupScene(final Camera pCamera, final Scene pParentScene, final Font pFont, final String pText, final float pDurationSeconds, final IEntityModifier pShapeModifier) {
		this(pCamera, pParentScene, pFont, pText, pDurationSeconds, pShapeModifier, null);
	}

	public TextPopupScene(final Camera pCamera, final Scene pParentScene, final Font pFont, final String pText, final float pDurationSeconds, final Runnable pRunnable) {
		this(pCamera, pParentScene, pFont, pText, pDurationSeconds, null, pRunnable);
	}

	public TextPopupScene(final Camera pCamera, final Scene pParentScene, final Font pFont, final String pText, final float pDurationSeconds, final IEntityModifier pShapeModifier, final Runnable pRunnable) {
		super(pCamera, pParentScene, pDurationSeconds, pRunnable);

		this.mText = new Text(0, 0, pFont, pText, HorizontalAlign.CENTER);
		this.centerShapeInCamera(this.mText);

		if(pShapeModifier != null) {
			this.mText.registerEntityModifier(pShapeModifier);
		}

		this.attachChild(this.mText);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Text getText() {
		return this.mText;
	}

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
