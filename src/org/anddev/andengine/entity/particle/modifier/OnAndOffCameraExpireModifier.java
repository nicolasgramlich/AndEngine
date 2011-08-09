package org.anddev.andengine.entity.particle.modifier;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.particle.Particle;
import org.anddev.andengine.entity.particle.modifier.OffCameraExpireModifier;

/**
 * <p>Causes the particle to expire after it has entered and left the view of the camera.</p>
 * 
 * <p>This allows you to create a particle off camera, move it on camera, and have it expire once it leaves the camera.</p>
 * 
 * <p>(c) 2011 Nicolas Gramlich<br> 
 * (c) 2011 Zynga Inc.</p>
 * 
 * @author Scott Kennedy
 * @since 27:10:00 - 08.08.2011
 */
public class OnAndOffCameraExpireModifier extends OffCameraExpireModifier {

 // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private boolean mHasBeenOnCamera = false;

    // ===========================================================
    // Constructors
    // ===========================================================

    public OnAndOffCameraExpireModifier(final Camera pCamera) {
            super(pCamera);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public void onUpdateParticle(final Particle pParticle) {
        if(!this.mHasBeenOnCamera && getCamera().isRectangularShapeVisible(pParticle)) {
            mHasBeenOnCamera = true;
        }
    
        if(this.mHasBeenOnCamera) {
            super.onUpdateParticle(pParticle);
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

}
