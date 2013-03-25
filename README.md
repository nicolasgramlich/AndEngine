# AndEngine

## Building

### Eclipse
 * AndEngine has to be build with ADT-17 or higher!

### IntelliJ IDEA
 * AndEngine relies on ADT to auto-generate a "BuildConfig" class. IntelliJ IDEA (as of 11.1.1) has not fully integrated with ADT-17+. In order to build AndEngine with IntelliJ IDEA, you can simply add the following class yourself in the root package (org.andengine): 
  
```java
package org.andengine;

public final class BuildConfig { 
    public final static boolean DEBUG = true;
}
```


## Branches
 * OpenGL ES 2:
    * Support: [> 93% of all Android devices ([Apr. 2012](http://developer.android.com/resources/dashboard/platform-versions.html))
    * Branch: [`GLES2-AnchorCenter`][URI_AndEngine_GLES2_AnchorCenter]
      * Active development. 
      * The leatest and greatest.
      * More [`cocos2d`][URI_cocos2d]-ish (Coordinate-System in the lower left corner, powerful AnchorCenter system.)
    * Branch: [`GLES2`][URI_AndEngine_GLES2]
      * Not in active development.
 * OpenGL ES 1: 
    * Branch: [`master`][URI_AndEngine_master]
    * Not in active development.
    * Support: > 99.0% of all Android devices ([Apr. 2012](http://developer.android.com/resources/dashboard/platform-versions.html))

## Examples

 * [`AndEngineExamples`][URI_AndEngineExamples]
 * [`AndEngineRobotiumExtensionExample`][URI_AndEngineRobotiumExtensionExample]

## Tests
 * [`AndEngineTest`][URI_AndEngineTest]
 * [`AndEngineRobotiumExtensionExampleTest`][URI_AndEngineRobotiumExtensionExampleTest]

## Extensions

 * [`AndEngineAugmentedRealityExtension`][URI_AndEngineAugmentedRealityExtension]
 * [`AndEngineLiveWallpaperExtension`][URI_AndEngineLiveWallpaperExtension]
 * [`AndEngineMODPlayerExtension`][URI_AndEngineMODPlayerExtension]
 * [`AndEngineMultiplayerExtension`][URI_AndEngineMultiplayerExtension]
 * [`AndEngineMultiTouchExtension`][URI_AndEngineMultiTouchExtension] (Merged into the 'GLES2' branch.)
 * [`AndEnginePhysicsBox2DExtension`][URI_AndEnginePhysicsBox2DExtension]
 * [`AndEngineRobotiumExtension`][URI_AndEngineRobotiumExtension]
 * [`AndEngineScriptingExtension`][URI_AndEngineScriptingExtension]
 * [`AndEngineScriptingExtensionGenerator`][URI_AndEngineScriptingExtensionGenerator]
 * [`AndEngineSVGTextureRegionExtension`][URI_AndEngineSVGTextureRegionExtension]
 * [`AndEngineTexturePackerExtension`][URI_AndEngineTexturePackerExtension] (Merged into the 'GLES2-AnchorCenter' branch.)
 * [`AndEngineTMXTiledMapExtension`][URI_AndEngineTMXTiledMapExtension] (Merged into the 'GLES1' branch.)


[URI_AndEngine_GLES2_AnchorCenter]: https://github.com/nicolasgramlich/AndEngine/tree/GLES2-AnchorCenter
[URI_AndEngine_GLES2]: https://github.com/nicolasgramlich/AndEngine/tree/GLES2
[URI_AndEngine_master]: https://github.com/nicolasgramlich/AndEngine/tree/master
[URI_cocos2d]: https://github.com/cocos2d
[URI_AndEngineExamples]: https://github.com/nicolasgramlich/AndEngineExamples
[URI_AndEngineRobotiumExtensionExample]: https://github.com/nicolasgramlich/AndEngineRobotiumExtensionExample
[URI_AndEngineTest]: https://github.com/nicolasgramlich/AndEngineTest
[URI_AndEngineRobotiumExtensionExampleTest]: https://github.com/nicolasgramlich/AndEngineRobotiumExtensionExampleTest
[URI_AndEngineAugmentedRealityExtension]: https://github.com/nicolasgramlich/AndEngineAugmentedRealityExtension
[URI_AndEngineLiveWallpaperExtension]: https://github.com/nicolasgramlich/AndEngineLiveWallpaperExtension
[URI_AndEngineMODPlayerExtension]: https://github.com/nicolasgramlich/AndEngineMODPlayerExtension
[URI_AndEngineMultiplayerExtension]: https://github.com/nicolasgramlich/AndEngineMultiplayerExtension
[URI_AndEngineMultiTouchExtension]: https://github.com/nicolasgramlich/AndEngineMultiTouchExtension
[URI_AndEnginePhysicsBox2DExtension]: https://github.com/nicolasgramlich/AndEnginePhysicsBox2DExtension
[URI_AndEngineRobotiumExtension]: https://github.com/nicolasgramlich/AndEngineRobotiumExtension
[URI_AndEngineScriptingExtension]: https://github.com/nicolasgramlich/AndEngineScriptingExtension
[URI_AndEngineScriptingExtensionGenerator]: https://github.com/nicolasgramlich/AndEngineScriptingExtensionGenerator
[URI_AndEngineSVGTextureRegionExtension]: https://github.com/nicolasgramlich/AndEngineSVGTextureRegionExtension
[URI_AndEngineTexturePackerExtension]: https://github.com/nicolasgramlich/AndEngineTexturePackerExtension
[URI_AndEngineTMXTiledMapExtension]: https://github.com/nicolasgramlich/AndEngineTMXTiledMapExtension
