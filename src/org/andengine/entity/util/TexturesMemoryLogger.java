package org.andengine.entity.util;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.util.debug.Debug;
import org.andengine.BuildConfig;

public class TexturesMemoryLogger implements IUpdateHandler {
    // ===========================================================
    // Constants
    // ===========================================================
    private static final float AVERAGE_DURATION_DEFAULT = 5;

    // ===========================================================
    // Fields
    // ===========================================================
    private final TextureManager mTextureManager;
    private final float mAverageDuration;
    private float mSecondsElapsed;
    private int mMaxMemoryUsed;
    
    public TexturesMemoryLogger(final TextureManager pTextureManager) {
        this(pTextureManager, AVERAGE_DURATION_DEFAULT);
    }
    
    public TexturesMemoryLogger(final TextureManager pTextureManager, final float pAverageDuration) {
        this.mTextureManager = pTextureManager;
        this.mAverageDuration = pAverageDuration;
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {
        mSecondsElapsed += pSecondsElapsed;
        
        if (mSecondsElapsed > mAverageDuration) {
            this.onHandleAverageDurationElapsed();

            this.mSecondsElapsed -= this.mAverageDuration;            
        }
    }

    @Override
    public void reset() {
        mSecondsElapsed = 0;
    }

    private void onHandleAverageDurationElapsed() {
        if (BuildConfig.DEBUG) {
            /* Get memory used in KB */
            int memoryUsed = mTextureManager.getTextureMemoryUsed();
            /* Update maximum */
            mMaxMemoryUsed = Math.max(memoryUsed, mMaxMemoryUsed);

            Debug.d(String.format("MEM. USED: %.2fM in %d textures (MAX: %.2fM)", memoryUsed / 1024f, mTextureManager.getNumTextureMemoryUsed(), mMaxMemoryUsed / 1024f));
        }
    }
}
