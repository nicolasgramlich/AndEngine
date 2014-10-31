#include "src/AndEngineScriptingExtension.h"
#include "src/Test.h"
#include "src/Util.h"
#include <string>
#include "src/org/andengine/opengl/texture/ITexture.h"
#include "src/org/andengine/opengl/texture/Texture.h"
#include "src/org/andengine/opengl/texture/bitmap/BitmapTexture.h"
#include "src/org/andengine/opengl/texture/bitmap/BitmapTextureFormat.h"
#include "src/org/andengine/opengl/texture/bitmap/AssetBitmapTexture.h"
#include "src/org/andengine/opengl/texture/region/ITextureRegion.h"
#include "src/org/andengine/opengl/texture/region/TextureRegion.h"
#include "src/org/andengine/entity/Entity.h"
#include "src/org/andengine/entity/primitive/Rectangle.h"
#include "src/org/andengine/entity/sprite/Sprite.h"

// ===========================================================
// org.andengine.extension.scripting.Test
// ===========================================================

JNIEXPORT jobject JNICALL Java_org_andengine_extension_scripting_Test_test(JNIEnv* pJNIEnv, jclass pJClass) {
	Engine* engine = getEngine();
	Context* context = getContext();
	AssetManager* assetManager = context->getAssetManager();

	/* Fetch managers. */
	TextureManager* textureManager = engine->getTextureManager();
	VertexBufferObjectManager* vertexBufferObjectManager = engine->getVertexBufferObjectManager();

	/* Create texture. */
	AssetBitmapTexture texture(textureManager, assetManager, JNI_ENV()->NewStringUTF("gfx/box.png")); //, BitmapTextureFormat::RGBA_4444);
	texture.load();

	/* Extract TextureRegion. */
	TextureRegion textureRegion(&texture, 0, 0, texture.getWidth(), texture.getHeight());

	/* Create an Entity/Sprite. */
	int size = 100;
	Entity* entity = new Sprite(360 - (size / 2), 240 - (size / 2), size, size, &textureRegion, vertexBufferObjectManager);
//	Entity* entity = new Rectangle(360 - (size / 2), 240 - (size / 2), size, size, vertexBufferObjectManager);

	entity->setRotation(45);
	entity->setScale(2);

	/* Release managers. */
	delete vertexBufferObjectManager;
	delete textureManager;

	return entity->unwrap();
}
