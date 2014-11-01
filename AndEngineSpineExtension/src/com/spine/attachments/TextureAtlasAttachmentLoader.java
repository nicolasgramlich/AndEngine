package com.spine.attachments;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import android.content.Context;

import com.spine.Attachment;
import com.spine.AttachmentLoader;
import com.spine.AttachmentType;

public class TextureAtlasAttachmentLoader implements AttachmentLoader {
	private BitmapTextureAtlas atlas;
	private Context context;

	public TextureAtlasAttachmentLoader (BitmapTextureAtlas mBitmapTextureAtlas, Context context) {
		if (mBitmapTextureAtlas == null) throw new IllegalArgumentException("atlas cannot be null.");
		this.atlas = mBitmapTextureAtlas;
		this.context = context;
	}

	public Attachment newAttachment (AttachmentType type, String name) {
		Attachment attachment = null;
		switch (type) {
		case region:
			attachment = new RegionAttachment(name);
			break;
		case regionSequence:
			attachment = new RegionAttachment(name);
			break;
		default:
			throw new IllegalArgumentException("Unknown attachment type: " + type);
		}

		/*if (attachment instanceof RegionAttachment) {
			TextureRegion region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.atlas, this.context, attachment.getName() + ".png", 0, 0);
			if (region == null)
				throw new RuntimeException("Region not found in atlas: " + attachment + " (" + type + " attachment: " + name + ")");
			((RegionAttachment)attachment).setRegion(region);
		}*/

		return attachment;
	}
	
	public Context getContext() {
		return context;
	}
	
	public BitmapTextureAtlas getAtlas() {
		return atlas;
	}
}