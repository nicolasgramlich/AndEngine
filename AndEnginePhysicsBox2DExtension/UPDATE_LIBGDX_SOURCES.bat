"C:\Program Files (x86)\wget\bin\wget.exe" -r -k --tries 10 --reject=html -np -P jni/Box2D http://libgdx.googlecode.com/svn/trunk/gdx/jni/Box2D/
pause
xcopy /Y /E jni\Box2D\libgdx.googlecode.com\svn\trunk\gdx\jni\Box2D\**.* jni\Box2D
pause
rmdir /q/s jni\Box2D\libgdx.googlecode.com\
pause

"C:\Program Files (x86)\wget\bin\wget.exe" -r -k --tries 10 --reject=html -np -P src http://libgdx.googlecode.com/svn/trunk/gdx/src/com/badlogic/gdx/physics/box2d/
xcopy /Y /E src\libgdx.googlecode.com\svn\trunk\gdx\src\com\badlogic\gdx\physics\**.* src\com\badlogic\gdx\physics
rmdir /q/s src\libgdx.googlecode.com\
pause