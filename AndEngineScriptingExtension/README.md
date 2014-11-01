# AndEngineScriptingExtension

## Building

 * The AndEngineScriptingExtension has to be build with ADT-17 or higher!

## Branches

 * GLES2: 'GLES2'
    * Active development. 
 * GLES1: 'master'
    * Unsupported.

## State
Currently the AndEngineScriptingExtension is capable of executing very simple scripts, like the following:
```
var entity = new andengine.Entity(10, 20);

entity.onAttached = function() {
	return true;
}

entity.x = entity.y * 2;
```

## ABI-Support
 * armeabi
 * armeabi-v7a
 * x86 (coming soon)

## Extensions

 * AndEngineScriptingExtensionGenerator