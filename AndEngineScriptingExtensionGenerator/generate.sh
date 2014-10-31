#!/bin/bash

ANDENGINE_SCRIPTINGEXTENSION_GENERATOR_ROOT="/Users/ngramlich/Workspace/gdk/graphic_engines/AndEngine/AndEngineScriptingExtensionGenerator/"
ANDENGINE_ROOT="/Users/ngramlich/Workspace/gdk/graphic_engines/AndEngine/AndEngine/"
ANDENGINE_SCRIPTINGEXTENSION_ROOT="/Users/ngramlich/Workspace/gdk/graphic_engines/AndEngine/AndEngineScriptingExtension/"


pushd ${ANDENGINE_SCRIPTINGEXTENSION_GENERATOR_ROOT}bin > /dev/null

####################################
# Generate bindings:
####################################
echo "####################################"
echo "# Generating bindings ..."

java \
	-cp '.:../lib/*' \
	org/andengine/extension/scripting/generator/AndEngineScriptingExtensionGenerator \
		-in-java-root ${ANDENGINE_ROOT}src \
		-in-javabin-root ${ANDENGINE_ROOT}bin \
		-proxy-java-root ${ANDENGINE_SCRIPTINGEXTENSION_ROOT}src \
		-proxy-cpp-root ${ANDENGINE_SCRIPTINGEXTENSION_ROOT}jni/src \
		-proxy-java-formatter jalopy \
		-proxy-java-class-suffix Proxy \
		-proxy-class-exclude java.util.Comparator \
		-proxy-class-exclude android.hardware.SensorEventListener \
		-proxy-class-exclude android.view.View\$OnTouchListener \
		-proxy-class-exclude org.andengine.input.touch.controller.ITouchEventCallback \
		-proxy-class-exclude android.location.LocationListener \
		-proxy-method-include getX \
		-proxy-method-include getY \
		-proxy-method-include setX \
		-proxy-method-include setY \
		-proxy-method-include getScaleX \
		-proxy-method-include getScaleY \
		-proxy-method-include setScale \
		-proxy-method-include getRotation \
		-proxy-method-include setRotation \
		-proxy-method-include getSkewX \
		-proxy-method-include getSkewY \
		-proxy-method-include setSkew \
		-proxy-method-include getRed \
		-proxy-method-include getGreen \
		-proxy-method-include getBlue \
		-proxy-method-include getAlpha \
		-proxy-method-include getColor \
		-proxy-method-include setRed \
		-proxy-method-include setGreen \
		-proxy-method-include setBlue \
		-proxy-method-include setAlpha \
		-proxy-method-include setColor \
		-proxy-method-include attachChild \
		-proxy-method-include detachChild \
		-proxy-method-include onAttached \
		-proxy-method-include onDetached \
		-proxy-method-include detachSelf \
		-proxy-method-include onAreaTouched \
		-proxy-method-include swapChildren \
		-proxy-method-include getChild \
		-proxy-method-include getChildCount \
		-proxy-method-include getVertexBufferObjectManager \
		-proxy-method-include getTextureManager \
		-proxy-method-include getFontManager \
		-proxy-method-include load \
		-proxy-method-include unload \
		-proxy-method-include getWidth \
		-proxy-method-include getHeight \
		-proxy-class org.andengine.engine.Engine \
		-proxy-class org.andengine.engine.options.EngineOptions \
		-proxy-class org.andengine.engine.options.ScreenOrientation \
		-proxy-class org.andengine.engine.options.resolutionpolicy.IResolutionPolicy \
		-proxy-class org.andengine.engine.handler.IUpdateHandler \
		-proxy-class org.andengine.engine.handler.IDrawHandler \
		-proxy-class org.andengine.engine.camera.Camera \
		-proxy-class org.andengine.engine.camera.hud.HUD \
		-proxy-class org.andengine.entity.scene.ITouchArea \
		-proxy-class org.andengine.entity.scene.IOnAreaTouchListener \
		-proxy-class org.andengine.entity.scene.IOnSceneTouchListener \
		-proxy-class org.andengine.input.touch.TouchEvent \
		-proxy-class org.andengine.util.adt.transformation.Transformation \
		-proxy-class org.andengine.util.color.Color \
		-proxy-class org.andengine.util.IDisposable \
		-proxy-class org.andengine.util.IMatcher \
		-proxy-class org.andengine.opengl.util.GLState \
		-proxy-class org.andengine.opengl.shader.ShaderProgram \
		-proxy-class org.andengine.opengl.shader.source.IShaderSource \
		-proxy-class org.andengine.opengl.vbo.IVertexBufferObject \
		-proxy-class org.andengine.opengl.vbo.VertexBufferObjectManager \
		-proxy-class org.andengine.opengl.vbo.DrawType \
		-proxy-class org.andengine.opengl.texture.TextureManager \
		-proxy-class org.andengine.opengl.texture.ITexture \
		-proxy-class org.andengine.opengl.texture.Texture \
		-proxy-class org.andengine.opengl.texture.ITextureStateListener \
		-proxy-class org.andengine.opengl.texture.TextureOptions \
		-proxy-class org.andengine.opengl.texture.PixelFormat \
		-proxy-class org.andengine.opengl.texture.bitmap.BitmapTexture \
		-proxy-class org.andengine.opengl.texture.bitmap.BitmapTextureFormat \
		-proxy-class org.andengine.util.adt.io.in.IInputStreamOpener \
		-proxy-class org.andengine.util.adt.io.in.AssetInputStreamOpener \
		-proxy-class org.andengine.opengl.texture.bitmap.AssetBitmapTexture \
		-proxy-class org.andengine.opengl.texture.region.ITextureRegion \
		-proxy-class org.andengine.opengl.texture.region.BaseTextureRegion \
		-proxy-class org.andengine.opengl.texture.region.TextureRegion \
		-proxy-class org.andengine.opengl.font.FontManager \
		-proxy-class org.andengine.entity.Entity \
		-proxy-class org.andengine.entity.IEntity \
		-proxy-class org.andengine.entity.IEntityMatcher \
		-proxy-class org.andengine.entity.IEntityComparator \
		-proxy-class org.andengine.entity.scene.background.IBackground \
		-proxy-class org.andengine.entity.primitive.Rectangle \
		-proxy-class org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject \
		-proxy-class org.andengine.entity.primitive.Line \
		-proxy-class org.andengine.entity.primitive.vbo.ILineVertexBufferObject \
		-proxy-class org.andengine.entity.scene.Scene \
		-proxy-class org.andengine.entity.scene.CameraScene \
		-proxy-class org.andengine.entity.sprite.Sprite \
		-proxy-class org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject \
		-proxy-class org.andengine.entity.shape.IShape \
		-proxy-class org.andengine.entity.shape.Shape \
		-proxy-class org.andengine.entity.shape.IAreaShape \
		-proxy-class org.andengine.entity.shape.RectangularShape \
		\
		-javascript-cpp-root ${ANDENGINE_SCRIPTINGEXTENSION_ROOT}jni/src \
		-javascript-cpp-class-prefix X_ \
		-javascript-method-include getX \
		-javascript-method-include getY \
		-javascript-class org.andengine.entity.Entity

echo "# Done."
echo "####################################"

popd > /dev/null



pushd ${ANDENGINE_SCRIPTINGEXTENSION_ROOT} > /dev/null

####################################
# Inject forward declarations:
####################################
echo "####################################"
echo "# Injecting forward declarations ..."


echo -n "Injecting into: src/org/andengine/engine/Engine.h ..."
sed -i "" '/class Engine/ i\
	class VertexBufferObjectManager; // Forward declaration\
	class FontManager; // Forward declaration\
	class TextureManager; // Forward declaration\
	\
	' jni/src/org/andengine/engine/Engine.h
echo " done!"


echo "# Done."
echo "####################################"


####################################
# Fix other stuff:
####################################
echo "####################################"
echo "# Fix other stuff ..."


echo -n "Fixing: 'new IEntity(..) => new Entity(..)' ..."
find ./jni -type f -iname "*.cpp" -exec sed -i "" 's/new IEntity/new Entity/' {} \;
echo " done!"
echo -n "Fixing: 'return IEntity(..) => return Entity(..)' ..."
find ./jni -type f -iname "*.cpp" -exec sed -i "" 's/return IEntity/return Entity/' {} \;
echo " done!"


echo "# Done."
echo "####################################"

popd > /dev/null