package org.andengine.examples.launcher;

import org.andengine.examples.R;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:13:34 - 27.06.2010
 */
public enum ExampleGroup {
	// ===========================================================
	// Elements
	// ===========================================================

	SIMPLE(R.string.examplegroup_simple, 
			Example.LINE, Example.RECTANGLE, Example.SPRITE, Example.SPRITEREMOVE, Example.SPRITEBATCH),
	MODIFIER_AND_ANIMATION(R.string.examplegroup_modifier_and_animation, 
			Example.MOVINGBALL, Example.ENTITYMODIFIER, Example.ENTITYMODIFIERIRREGULAR, Example.CARDINALSPLINEMOVEMODIFIER, Example.PATHMODIFIER, Example.ANIMATEDSPRITES, Example.EASEFUNCTION, Example.ROTATION3D ),
	TOUCH(R.string.examplegroup_touch, 
			Example.TOUCHDRAG, Example.MULTITOUCH, Example.ANALOGONSCREENCONTROL, Example.DIGITALONSCREENCONTROL, Example.ANALOGONSCREENCONTROLS, Example.COORDINATECONVERSION, Example.PINCHZOOM),
	PARTICLESYSTEM(R.string.examplegroup_particlesystems,
			Example.PARTICLESYSTEMSIMPLE, Example.PARTICLESYSTEMCOOL, Example.PARTICLESYSTEMNEXUS),
	MULTIPLAYER(R.string.examplegroup_multiplayer,
			Example.MULTIPLAYER, Example.MULTIPLAYERSERVERDISCOVERY, Example.MULTIPLAYERBLUETOOTH),
	PHYSICS(R.string.examplegroup_physics,
			Example.COLLISIONDETECTION, Example.PHYSICS, Example.PHYSICSFIXEDSTEP, Example.PHYSICSCOLLISIONFILTERING, Example.PHYSICSJUMP, Example.PHYSICSREVOLUTEJOINT, Example.PHYSICSMOUSEJOINT, Example.PHYSICSREMOVE),
	TEXT(R.string.examplegroup_text,
			Example.TEXT, Example.TICKERTEXT, Example.CHANGEABLETEXT, Example.TEXTBREAK, Example.CUSTOMFONT, Example.STROKEFONT, Example.BITMAPFONT),
	AUDIO(R.string.examplegroup_audio, 
			Example.SOUND, Example.MUSIC, Example.MODPLAYER),
	ADVANCED(R.string.examplegroup_advanced, 
			Example.SPLITSCREEN, Example.BOUNDCAMERA, Example.HULLALGORITHM), // Example.AUGMENTEDREALITY, Example.AUGMENTEDREALITYHORIZON),
	POSTPROCESSING(R.string.examplegroup_postprocessing, 
			Example.MOTIONSTREAK, Example.RADIALBLUR),
	BACKGROUND(R.string.examplegroup_background, 
			Example.REPEATINGSPRITEBACKGROUND, Example.AUTOPARALLAXBACKGROUND, Example.TMXTILEDMAP),
	OTHER(R.string.examplegroup_other, 
			Example.SCREENCAPTURE, Example.PAUSE, Example.MENU, Example.SUBMENU, Example.TEXTMENU, Example.ZOOM , Example.IMAGEFORMATS, Example.PVRTEXTURE, Example.PVRCCZTEXTURE, Example.PVRGZTEXTURE, Example.ETC1TEXTURE, Example.TEXTUREOPTIONS, Example.CANVASTEXTURECOMPOSITING, Example.TEXTUREPACKER, Example.COLORKEYTEXTURESOURCEDECORATOR, Example.LOADTEXTURE, Example.UPDATETEXTURE, Example.UNLOADRESOURCES, Example.RUNNABLEPOOLUPDATEHANDLER, Example.SVGTEXTUREREGION, Example.XMLLAYOUT, Example.LEVELLOADER),
	APP(R.string.examplegroup_app, 
			Example.APP_CITYRADAR),
	GAME(R.string.examplegroup_game, 
			Example.GAME_PONG, Example.GAME_SNAKE, Example.GAME_RACER),
	BENCHMARK(R.string.examplegroup_benchmark, 
			Example.BENCHMARK_SPRITE, Example.BENCHMARK_ATTACHDETACH, Example.BENCHMARK_ENTITYMODIFIER, Example.BENCHMARK_ANIMATION, Example.BENCHMARK_TICKERTEXT, /* Example.BENCHMARK_PARTICLESYSTEM, */ Example.BENCHMARK_PHYSICS);

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public final Example[] mExamples;
	public final int mNameResourceID;

	// ===========================================================
	// Constructors
	// ===========================================================

	private ExampleGroup(final int pNameResourceID, final Example ... pExamples) {
		this.mNameResourceID = pNameResourceID;
		this.mExamples = pExamples;
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
