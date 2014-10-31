set -e

echo "\nDownloading libgdx box2d native headers..."
wget -r -k --tries 10 --reject=html -np -P jni/Box2D http://libgdx.googlecode.com/svn/trunk/gdx/jni/Box2D/

echo "\nDownloading libgdx box2d java libs..."
wget -r -k --tries 10 --reject=html -np -P src http://libgdx.googlecode.com/svn/trunk/gdx/src/com/badlogic/gdx/physics/box2d/
