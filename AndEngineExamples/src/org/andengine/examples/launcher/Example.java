package org.andengine.examples.launcher;

import org.andengine.examples.AnalogOnScreenControlExample;
import org.andengine.examples.AnalogOnScreenControlsExample;
import org.andengine.examples.AnimatedSpritesExample;
import org.andengine.examples.AutoParallaxBackgroundExample;
import org.andengine.examples.BitmapFontExample;
import org.andengine.examples.BoundCameraExample;
import org.andengine.examples.CanvasTextureCompositingExample;
import org.andengine.examples.CardinalSplineMoveModifierExample;
import org.andengine.examples.CollisionDetectionExample;
import org.andengine.examples.ColorKeyTextureSourceDecoratorExample;
import org.andengine.examples.CoordinateConversionExample;
import org.andengine.examples.CustomFontExample;
import org.andengine.examples.DigitalOnScreenControlExample;
import org.andengine.examples.ETC1TextureExample;
import org.andengine.examples.EaseFunctionExample;
import org.andengine.examples.EntityModifierExample;
import org.andengine.examples.EntityModifierIrregularExample;
import org.andengine.examples.HullAlgorithmExample;
import org.andengine.examples.ImageFormatsExample;
import org.andengine.examples.LevelLoaderExample;
import org.andengine.examples.LineExample;
import org.andengine.examples.LoadTextureExample;
import org.andengine.examples.MenuExample;
import org.andengine.examples.ModPlayerExample;
import org.andengine.examples.MotionStreakExample;
import org.andengine.examples.MovingBallExample;
import org.andengine.examples.MultiTouchExample;
import org.andengine.examples.MultiplayerBluetoothExample;
import org.andengine.examples.MultiplayerExample;
import org.andengine.examples.MultiplayerServerDiscoveryExample;
import org.andengine.examples.MusicExample;
import org.andengine.examples.PVRCCZTextureExample;
import org.andengine.examples.PVRGZTextureExample;
import org.andengine.examples.PVRTextureExample;
import org.andengine.examples.ParticleSystemCoolExample;
import org.andengine.examples.ParticleSystemNexusExample;
import org.andengine.examples.ParticleSystemSimpleExample;
import org.andengine.examples.PathModifierExample;
import org.andengine.examples.PauseExample;
import org.andengine.examples.PhysicsCollisionFilteringExample;
import org.andengine.examples.PhysicsExample;
import org.andengine.examples.PhysicsFixedStepExample;
import org.andengine.examples.PhysicsJumpExample;
import org.andengine.examples.PhysicsMouseJointExample;
import org.andengine.examples.PhysicsRemoveExample;
import org.andengine.examples.PhysicsRevoluteJointExample;
import org.andengine.examples.PinchZoomExample;
import org.andengine.examples.R;
import org.andengine.examples.RadialBlurExample;
import org.andengine.examples.RectangleExample;
import org.andengine.examples.RepeatingSpriteBackgroundExample;
import org.andengine.examples.Rotation3DExample;
import org.andengine.examples.RunnablePoolUpdateHandlerExample;
import org.andengine.examples.SVGTextureRegionExample;
import org.andengine.examples.ScreenCaptureExample;
import org.andengine.examples.SoundExample;
import org.andengine.examples.SplitScreenExample;
import org.andengine.examples.SpriteBatchExample;
import org.andengine.examples.SpriteExample;
import org.andengine.examples.SpriteRemoveExample;
import org.andengine.examples.StrokeFontExample;
import org.andengine.examples.SubMenuExample;
import org.andengine.examples.TMXTiledMapExample;
import org.andengine.examples.TextBreakExample;
import org.andengine.examples.TextExample;
import org.andengine.examples.TextMenuExample;
import org.andengine.examples.TextureOptionsExample;
import org.andengine.examples.TexturePackerExample;
import org.andengine.examples.TickerTextExample;
import org.andengine.examples.TouchDragExample;
import org.andengine.examples.UnloadResourcesExample;
import org.andengine.examples.UpdateTextureExample;
import org.andengine.examples.XMLLayoutExample;
import org.andengine.examples.ZoomExample;
import org.andengine.examples.app.cityradar.CityRadarActivity;
import org.andengine.examples.benchmark.AnimationBenchmark;
import org.andengine.examples.benchmark.AttachDetachBenchmark;
import org.andengine.examples.benchmark.EntityModifierBenchmark;
import org.andengine.examples.benchmark.PhysicsBenchmark;
import org.andengine.examples.benchmark.SpriteBenchmark;
import org.andengine.examples.benchmark.TickerTextBenchmark;
import org.andengine.examples.game.pong.PongGameActivity;
import org.andengine.examples.game.racer.RacerGameActivity;
import org.andengine.examples.game.snake.SnakeGameActivity;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 20:42:27 - 16.06.2010
 */
enum Example {
	// ===========================================================
	// Elements
	// ===========================================================

	ANALOGONSCREENCONTROL(AnalogOnScreenControlExample.class, R.string.example_analogonscreencontrol),
	ANALOGONSCREENCONTROLS(AnalogOnScreenControlsExample.class, R.string.example_analogonscreencontrols),
	ANIMATEDSPRITES(AnimatedSpritesExample.class, R.string.example_animatedsprites),
//	AUGMENTEDREALITY(AugmentedRealityExample.class, R.string.example_augmentedreality),
//	AUGMENTEDREALITYHORIZON(AugmentedRealityHorizonExample.class, R.string.example_augmentedrealityhorizon),
	AUTOPARALLAXBACKGROUND(AutoParallaxBackgroundExample.class, R.string.example_autoparallaxbackground),
	BITMAPFONT(BitmapFontExample.class, R.string.example_bitmapfont),
	BOUNDCAMERA(BoundCameraExample.class, R.string.example_boundcamera),
	CARDINALSPLINEMOVEMODIFIER(CardinalSplineMoveModifierExample.class, R.string.example_cardinalsplinemovemodifier),
	CANVASTEXTURECOMPOSITING(CanvasTextureCompositingExample.class, R.string.example_canvastexturecompositing),
	CHANGEABLETEXT(TextExample.class, R.string.example_changeabletext),
	COLLISIONDETECTION(CollisionDetectionExample.class, R.string.example_collisiondetection),
	COLORKEYTEXTURESOURCEDECORATOR(ColorKeyTextureSourceDecoratorExample.class, R.string.example_colorkeytexturesourcedecorator),
	COORDINATECONVERSION(CoordinateConversionExample.class, R.string.example_coordinateconversion),
	CUSTOMFONT(CustomFontExample.class, R.string.example_customfont),
	DIGITALONSCREENCONTROL(DigitalOnScreenControlExample.class, R.string.example_digitalonscreencontrol),
	EASEFUNCTION(EaseFunctionExample.class, R.string.example_easefunction),
	ENTITYMODIFIER(EntityModifierExample.class, R.string.example_entitymodifier),
	ENTITYMODIFIERIRREGULAR(EntityModifierIrregularExample.class, R.string.example_entitymodifierirregular),
	ETC1TEXTURE(ETC1TextureExample.class, R.string.example_etc1texture),
	HULLALGORITHM(HullAlgorithmExample.class, R.string.example_hullalgorithm),
	IMAGEFORMATS(ImageFormatsExample.class, R.string.example_imageformats),
	LEVELLOADER(LevelLoaderExample.class, R.string.example_levelloader),
	LINE(LineExample.class, R.string.example_line),
	LOADTEXTURE(LoadTextureExample.class, R.string.example_loadtexture),
	MENU(MenuExample.class, R.string.example_menu),
	MODPLAYER(ModPlayerExample.class, R.string.example_modplayer),
	MOTIONSTREAK(MotionStreakExample.class, R.string.example_motionstreak),
	MOVINGBALL(MovingBallExample.class, R.string.example_movingball),
	MULTIPLAYER(MultiplayerExample.class, R.string.example_multiplayer),
	MULTIPLAYERSERVERDISCOVERY(MultiplayerServerDiscoveryExample.class, R.string.example_multiplayerserverdiscovery),
	MULTIPLAYERBLUETOOTH(MultiplayerBluetoothExample.class, R.string.example_multiplayerbluetooth),
	MULTITOUCH(MultiTouchExample.class, R.string.example_multitouch),
	MUSIC(MusicExample.class, R.string.example_music),
	PAUSE(PauseExample.class, R.string.example_pause),
	PATHMODIFIER(PathModifierExample.class, R.string.example_pathmodifier),
	PARTICLESYSTEMNEXUS(ParticleSystemNexusExample.class, R.string.example_particlesystemnexus),
	PARTICLESYSTEMCOOL(ParticleSystemCoolExample.class, R.string.example_particlesystemcool),
	PARTICLESYSTEMSIMPLE(ParticleSystemSimpleExample.class, R.string.example_particlesystemsimple),
	PHYSICSCOLLISIONFILTERING(PhysicsCollisionFilteringExample.class, R.string.example_physicscollisionfiltering),
	PHYSICS(PhysicsExample.class, R.string.example_physics),
	PHYSICSFIXEDSTEP(PhysicsFixedStepExample.class, R.string.example_physicsfixedstep),
	PHYSICSMOUSEJOINT(PhysicsMouseJointExample.class, R.string.example_physicsmousejoint),
	PHYSICSJUMP(PhysicsJumpExample.class, R.string.example_physicsjump),
	PHYSICSREVOLUTEJOINT(PhysicsRevoluteJointExample.class, R.string.example_physicsrevolutejoint),
	PHYSICSREMOVE(PhysicsRemoveExample.class, R.string.example_physicsremove),
	PINCHZOOM(PinchZoomExample.class, R.string.example_pinchzoom),
	PVRCCZTEXTURE(PVRCCZTextureExample.class, R.string.example_pvrccztexture),
	PVRGZTEXTURE(PVRGZTextureExample.class, R.string.example_pvrgztexture),
	PVRTEXTURE(PVRTextureExample.class, R.string.example_pvrtexture),
	RADIALBLUR(RadialBlurExample.class, R.string.example_radialblur),
	RECTANGLE(RectangleExample.class, R.string.example_rectangle),
	REPEATINGSPRITEBACKGROUND(RepeatingSpriteBackgroundExample.class, R.string.example_repeatingspritebackground),
	ROTATION3D(Rotation3DExample.class, R.string.example_rotation3d),
	RUNNABLEPOOLUPDATEHANDLER(RunnablePoolUpdateHandlerExample.class, R.string.example_runnablepoolupdatehandler),
	SCREENCAPTURE(ScreenCaptureExample.class, R.string.example_screencapture),
	SOUND(SoundExample.class, R.string.example_sound),
	SPLITSCREEN(SplitScreenExample.class, R.string.example_splitscreen),
	SPRITEBATCH(SpriteBatchExample.class, R.string.example_spritebatch),
	SPRITE(SpriteExample.class, R.string.example_sprite),
	SPRITEREMOVE(SpriteRemoveExample.class, R.string.example_spriteremove),
	STROKEFONT(StrokeFontExample.class, R.string.example_strokefont),
	SUBMENU(SubMenuExample.class, R.string.example_submenu),
	SVGTEXTUREREGION(SVGTextureRegionExample.class, R.string.example_svgtextureregion),
	TEXT(TextExample.class, R.string.example_text),
	TEXTBREAK(TextBreakExample.class, R.string.example_textbreak),
	TEXTMENU(TextMenuExample.class, R.string.example_textmenu),
	TEXTUREOPTIONS(TextureOptionsExample.class, R.string.example_textureoptions),
	TEXTUREPACKER(TexturePackerExample.class, R.string.example_texturepacker),
	TMXTILEDMAP(TMXTiledMapExample.class, R.string.example_tmxtiledmap),
	TICKERTEXT(TickerTextExample.class, R.string.example_tickertext),
	TOUCHDRAG(TouchDragExample.class, R.string.example_touchdrag),
	UNLOADRESOURCES(UnloadResourcesExample.class, R.string.example_unloadresources),
	UPDATETEXTURE(UpdateTextureExample.class, R.string.example_updatetexture),
	XMLLAYOUT(XMLLayoutExample.class, R.string.example_xmllayout),
	ZOOM(ZoomExample.class, R.string.example_zoom),

	BENCHMARK_ANIMATION(AnimationBenchmark.class, R.string.example_benchmark_animation),
	BENCHMARK_ATTACHDETACH(AttachDetachBenchmark.class, R.string.example_benchmark_attachdetach),
//	BENCHMARK_PARTICLESYSTEM(ParticleSystemBenchmark.class, R.string.example_benchmark_particlesystem),
	BENCHMARK_PHYSICS(PhysicsBenchmark.class, R.string.example_benchmark_physics),
	BENCHMARK_ENTITYMODIFIER(EntityModifierBenchmark.class, R.string.example_benchmark_entitymodifier),
	BENCHMARK_SPRITE(SpriteBenchmark.class, R.string.example_benchmark_sprite),
	BENCHMARK_TICKERTEXT(TickerTextBenchmark.class, R.string.example_benchmark_tickertext),

	APP_CITYRADAR(CityRadarActivity.class, R.string.example_app_cityradar),

	GAME_PONG(PongGameActivity.class, R.string.example_game_pong),
	GAME_SNAKE(SnakeGameActivity.class, R.string.example_game_snake),
	GAME_RACER(RacerGameActivity.class, R.string.example_game_racer);
	
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public final Class<? extends BaseGameActivity> CLASS;
	public final int NAMERESID;

	// ===========================================================
	// Constructors
	// ===========================================================

	private Example(final Class<? extends BaseGameActivity> pExampleClass, final int pNameResID) {
		this.CLASS = pExampleClass;
		this.NAMERESID = pNameResID;
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