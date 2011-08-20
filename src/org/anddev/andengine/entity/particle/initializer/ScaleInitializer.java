package org.anddev.andengine.entity.particle.initializer;

import org.anddev.andengine.entity.particle.Particle;
import org.anddev.andengine.entity.particle.initializer.BaseSingleValueInitializer;

/**
 * (c) 2011 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Scott Kennedy
 * @since 15:14:00 - 08.08.2011
 */
public class ScaleInitializer extends BaseSingleValueInitializer {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================
    
    public ScaleInitializer(final float pScale) {
        this(pScale, pScale);
    }
    
    public ScaleInitializer(final float pMinScale, final float pMaxScale) {
        super(pMinScale, pMaxScale);
    }
    
    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    protected void onInitializeParticle(final Particle pParticle, final float pScale) {
        pParticle.setScale(pScale);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}
