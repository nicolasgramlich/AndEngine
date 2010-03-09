package org.anddev.andengine.opengl.texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.os.Build;

public class TextureBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private ByteBuffer mByteBffer;
	private final Texture mTexture;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureBuffer(final Texture pTexture) {
		this.mTexture = pTexture;
		allocateByteBuffer();
		this.mByteBffer.order(ByteOrder.nativeOrder());
		this.update();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ByteBuffer getUVMappingByteBuffer() {
		return this.mByteBffer;
	}

	public Texture getTexture() {
		return this.mTexture;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private void allocateByteBuffer() {
		if(Build.VERSION.SDK_INT == 3) {
			this.mByteBffer = ByteBuffer.allocate(8*4);
		} else {
			this.mByteBffer = ByteBuffer.allocateDirect(8*4);
		}
	}
	
	public void update() {
		final Texture texture = this.mTexture;
		final ByteBuffer buffer = this.mByteBffer;
		
		if(texture.getTextureAtlas() == null) {
			return;
		}

		final float x1 = (float)texture.getAtlasPositionX()  / texture.getTextureAtlas().getWidth();
		final float y1 = (float)texture.getAtlasPositionY() / texture.getTextureAtlas().getHeight();
		final float x2 = (float)(texture.getAtlasPositionX() + texture.getWidth()) / texture.getTextureAtlas().getWidth();
		final float y2 = (float)(texture.getAtlasPositionY() + texture.getHeight()) / texture.getTextureAtlas().getHeight();
		
		buffer.position(0);
		buffer.putFloat(x1); buffer.putFloat(y1);
		buffer.putFloat(x2); buffer.putFloat(y1);
		buffer.putFloat(x1); buffer.putFloat(y2);
		buffer.putFloat(x2); buffer.putFloat(y2);
		buffer.position(0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
