## Lead Developer:

* Nicolas Gramlich <nicolasgramlich@gmail.com>, <ngramlich@zynga.com>

## Contributors:

### Zynga
* Scott Kennedy
  * Added EngineOptions->AudioOptions->SoundOptions->setMaxSimultaneousStreams.
  * Fixed a bug where BaseGameActivity would not resume.
  * Added ButtonSprite.
  * Fixed anti-pattern in Sprite/TiledSprite constructors.
  * Fixed an infinite recursion.
* Greg Haynes
  * Added SharedMemoryVertexBufferObject and ZeroMemoryVertexBufferObject.
  * Enhanced Pool class to be able to have a maximum number of items to permanently hold.
  * Fixed a bug in Entity.getSceneToLocalTransformation.
  * Fixed Hold/Click/Scroll-Detector when MultiTouch is enabled.
* Xi Yu
  * Added TextOptions->AutoWrap.CJK.

### Others
* Rodrigo Moraes <rodrigo.moraes@gmail.com>
  * Added optimizations and enhancements in the TMXTiledMapExtension.
* Janne Sinivirta
  * Added ParticleSystem.BlendFunctionInitializer.
  * Fix a surplus semi-colon preventing from building with a stock JDK.
* Michal Stawinski <michal.stawinski@gmail.com>
  * Fixed a race condition in SoundManager/SoundFactory.
  * Added Missing constructors in IMenuItem implementations.
* Francesco Zoffoli
  * Added callback method in IAnimationListener.
* Karl Erik Asbjørnsen
  * Fix in MultiKeyHashMap to preventing from building with a stock JDK.
* Erik Eloff <erik@eloff.se>
  * Memory fix in BufferUtils.
* Sergio Viudes <djpep.dj@gmail.com>
  * Fixed using an API Level 9 method.
* Pawel Plewa
  * Added CubicBezierMoveModifier and QuadraticBezierMoveModifier.
* Levi Notik <levi.notik@gmail.com>
  * Added project setup instructions for IntelliJ IDEA.
* Steven Jackson <stevosaurus@gmail.com>
  * Fixed wrong method signature in StrokeFont.
* Daniel Epstein <theturtleboy@gmail.com>
  * Added possibility to define scaleX/scaleY instead of just scale.
  * Fixed horizontal/vertical flipping of BaseTextureRegionBuffer.

## Suggestions / Issue-Reports:
* Leandro De Brasi
  * Fixed copy-paste error in SpriteBatchVertexBuffer.
  * Fixed missing super.finalize() call in BaseSprite.
  * Fixed a synchronization issue in TextureManager, FontManager and ShaderProgramManager.
  * Added support for specifying individual frames in an animation instead of just the first and the last frame.
* dorasoft
  * Fixed IModifierListeners being overwritten in certain constellations of nesting SequenceModifier, ParallelModifier and LoopModifier).
* brig
  * Fixed Entity.sortChildren(pImmediate=false) not resetting after sorting was performed.
* oldskool73
  * Fixed a bug when using MenuScene and ZoomCamera.
* chozabu
  * Fixed a bug when using MenuScene and ZoomCamera.
* stormtroopa
  * Fixed a bug when using MenuScene and ZoomCamera.
* whalabi
  * Fixed a bug where Textures turned/remained 'white', when they are are reloaded/updated.
* AlexNunn
  * Fixed a bug where Textures turned/remained 'white', when they are are reloaded/updated.
  * Fixed TextureSourceDecorator using Stroke.FILL to draw one pixel too few.
* Akirasan
  * Added methods to Entity allowing to attach/get/detach children based on their index.
* blissoft
  * Fixed a bug when changing the volume of a Sound.
* While(E)
  * Fixed a copy-paste error not allowing TiledSprite/AnimatedSprite to be used in a SpriteBatch.
* dironto
  * Fixed TextureFormat.RGB_565 working with TextureOptions.XYZ_(NON-)_PREMULTIPLYALPHA.
* moppelg
  * Fixed invisible Sprites in a SpriteBatch still drawing.
* Time-Over
  * Fixed a bug in Line vs Rectangle collision.
* aerobowl
  * Added add/remove methods in SpriteGroup.
* KyleHatch
  * Added convenience methods to convert from Local to Scene coordinates (and vice versa).
* daniboy
  * Added support for multiple stops in LinearGradientFillTextureSourceDecorator and RadialGradientFillTextureSourceDecorator.
* ddrscott
  * Added support for binding TouchEvents to OnSceneTouchListeners.
* rickw
  * Fixed Entity being able to hold ITouchAreas (only Scene should be able to).
* g1adrift
  * Added new WakeLockOption not requiring the WAKE_LOCK permission.
* H3R3T1C
  * Fixed the calculation of the GlobalTileID in the TMXTiledMapExtension when data is read from a GZipInputStream.
* valentin.milea
  * Added spacing to BlackPawnTextureBuilder.
* goran.mrzljak
  * Fixed a bug in TMXLayer when the TiledMap is not squared.
* cpasjuste
  * Added onCompletionListener to the Music class.
  * Added method to creae Sounds and Music from Files/Paths.
* k3vlar
  * Fixed resetting the position of Particles, so that moving ParticleSystems are possible.
* mr_deimos
  * Added PREMULTIPLYALPHA option to the TextureOption class.
* oleg
  * Added PREMULTIPLYALPHA option to the TextureOption class.
* NightJumper
  * Added MathUtils.distance.
* crino
  * Fixed check if OpenGL 1.0 or 1.1 is present.