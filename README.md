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

 * GLES2: 'GLES2'
    * Active development. 
    * Support: [> 93% of all Android devices ([Apr. 2012](http://developer.android.com/resources/dashboard/platform-versions.html))
 * GLES1: 'master'
    * Not in active development.
    * Support: > 99.0% of all Android devices ([Apr. 2012](http://developer.android.com/resources/dashboard/platform-versions.html))

## Examples

 * AndEngineExamples
 * AndEngineRobotiumExtensionExample

## Tests
 * AndEngineTest
 * AndEngineRobotiumExtensionExampleTest

## Extensions

 * AndEngineAugmentedRealityExtension
 * AndEngineLiveWallpaperExtension
 * AndEngineMODPlayerExtension
 * AndEngineMultiplayerExtension
 * AndEngineMultiTouchExtension (Merged into the 'GLES2' branch.)
 * AndEnginePhysicsBox2DExtension
 * AndEngineRobotiumExtension
 * AndEngineScriptingExtension
 * AndEngineScriptingExtensionGenerator
 * AndEngineSVGTextureRegionExtension
 * AndEngineTexturePackerExtension
 * AndEngineTMXTiledMapExtension (Merged into the 'GLES1' branch.)