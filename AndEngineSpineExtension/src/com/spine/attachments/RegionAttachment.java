package com.spine.attachments;

import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.color.Color;

import com.MathUtils;
import com.gdx.utils.NumberUtils;
import com.spine.Attachment;
import com.spine.Bone;
import com.spine.Slot;

/** Attachment that displays a texture region. */
public class RegionAttachment extends Attachment
{
	static public final int X1 = 0;
	static public final int Y1 = 1;
	static public final int C1 = 2;
	static public final int U1 = 3;
	static public final int V1 = 4;
	static public final int X2 = 5;
	static public final int Y2 = 6;
	static public final int C2 = 7;
	static public final int U2 = 8;
	static public final int V2 = 9;
	static public final int X3 = 10;
	static public final int Y3 = 11;
	static public final int C3 = 12;
	static public final int U3 = 13;
	static public final int V3 = 14;
	static public final int X4 = 15;
	static public final int Y4 = 16;
	static public final int C4 = 17;
	static public final int U4 = 18;
	static public final int V4 = 19;
	
	private TextureRegion region;
	private float x, y, scaleX, scaleY, rotation, width, height;
	private final float[] vertices = new float[20];
	private final float[] offset = new float[8];

	public RegionAttachment (String name) {
		super(name);
	}

	public void updateOffset () {
		float width = getWidth();
		float height = getHeight();
		float localX2 = width / 2;
		float localY2 = height / 2;
		float localX = -localX2;
		float localY = -localY2;
		if (region instanceof TextureRegion) {
			TextureRegion region = (TextureRegion)this.region;
			/*if (region.isRotated()) {
				localX += region.getTextureX() / region.getTexture().getWidth() * height;
				localY += region.getTextureY() / region.getTexture().getHeight() * width;
				localX2 -= (region.getWidth() - region.getTextureX() - region.getWidth()) / region.getWidth() * width;
				localY2 -= (region.getHeight() - region.getTextureY() - region.getHeight()) / region.getHeight() * height;
			} else {
				localX += region.getTextureX() / region.getTexture().getWidth() * width;
				localY += region.getTextureY() / region.getTexture().getHeight() * height;
				localX2 -= (region.getTexture().getWidth() - region.getTextureX()- region.getWidth()) / region.getWidth() * width;
				localY2 -= (region.getTexture().getHeight() - region.getTextureY() - region.getHeight()) / region.getHeight() * height;
			}*/
			/*
			 * if (region.rotate) {
				localX += region.offsetX / region.originalWidth * height;
				localY += region.offsetY / region.originalHeight * width;
				localX2 -= (region.originalWidth - region.offsetX - region.packedHeight) / region.originalWidth * width;
				localY2 -= (region.originalHeight - region.offsetY - region.packedWidth) / region.originalHeight * height;
			} else {
				localX += region.offsetX / region.originalWidth * width;
				localY += region.offsetY / region.originalHeight * height;
				localX2 -= (region.originalWidth - region.offsetX - region.packedWidth) / region.originalWidth * width;
				localY2 -= (region.originalHeight - region.offsetY - region.packedHeight) / region.originalHeight * height;
			}
			 */
		}
		float scaleX = getScaleX();
		float scaleY = getScaleY();
		localX *= scaleX;
		localY *= scaleY;
		localX2 *= scaleX;
		localY2 *= scaleY;
		float rotation = getRotation();
		float cos = MathUtils.cosDeg(rotation);
		float sin = MathUtils.sinDeg(rotation);
		float x = getX();
		float y = getY();
		float localXCos = localX * cos + x;
		float localXSin = localX * sin;
		float localYCos = localY * cos + y;
		float localYSin = localY * sin;
		float localX2Cos = localX2 * cos + x;
		float localX2Sin = localX2 * sin;
		float localY2Cos = localY2 * cos + y;
		float localY2Sin = localY2 * sin;
		float[] offset = this.offset;
		offset[0] = localXCos - localYSin;
		offset[1] = localYCos + localXSin;
		offset[2] = localXCos - localY2Sin;
		offset[3] = localY2Cos + localXSin;
		offset[4] = localX2Cos - localY2Sin;
		offset[5] = localY2Cos + localX2Sin;
		offset[6] = localX2Cos - localYSin;
		offset[7] = localYCos + localX2Sin;
	}

	public void setRegion (TextureRegion region) {
		if (region == null) throw new IllegalArgumentException("region cannot be null.");
		//TextureRegion oldRegion = this.region;
		this.region = region;
		float[] vertices = this.vertices;
		if (region instanceof TextureRegion && ((TextureRegion)region).isRotated()) {
			vertices[U2] = region.getU();
			vertices[V2] = region.getV2();
			vertices[U3] = region.getU();
			vertices[V3] = region.getV();
			vertices[U4] = region.getU2();
			vertices[V4] = region.getV();
			vertices[U1] = region.getU2();
			vertices[V1] = region.getV2();
		} else {
			vertices[U1] = region.getU();
			vertices[V1] = region.getV2();
			vertices[U2] = region.getU();
			vertices[V2] = region.getV();
			vertices[U3] = region.getU2();
			vertices[V3] = region.getV();
			vertices[U4] = region.getU2();
			vertices[V4] = region.getV2();
		}
		updateOffset();
	}

	public TextureRegion getRegion () {
		if (region == null) throw new IllegalStateException("RegionAttachment is not resolved: " + this);
		return region;
	}

	public void draw (SpriteBatch batch, Slot slot) {
		if (region == null) throw new IllegalStateException("RegionAttachment is not resolved: " + this);

		Color skeletonColor = slot.getSkeleton().getColor();
		Color slotColor = slot.getColor();
		float color = NumberUtils.intToFloatColor( //
			((int)(255 * skeletonColor.getAlpha() * slotColor.getAlpha()) << 24) //
				| ((int)(255 * skeletonColor.getBlue() * slotColor.getBlue()) << 16) //
				| ((int)(255 * skeletonColor.getGreen() * slotColor.getGreen()) << 8) //
				| ((int)(255 * skeletonColor.getRed() * slotColor.getRed())));
		float[] vertices = this.vertices;
		vertices[C1] = color;
		vertices[C2] = color;
		vertices[C3] = color;
		vertices[C4] = color;

		updateWorldVertices(slot.getBone());

		batch.draw(region, 
					vertices[X1], 
					vertices[Y1], 
					vertices[X2], 
					vertices[Y2], 
					vertices[X3], 
					vertices[Y3], 
					vertices[X4], 
					vertices[Y4], 
					vertices[C1], 
					vertices[C2], 
					vertices[C3], 
					vertices[C4]);
	}

	public void updateWorldVertices (Bone bone) {
		float[] vertices = this.vertices;
		float[] offset = this.offset;
		float x = bone.getWorldX();
		float y = bone.getWorldY();
		float m00 = bone.getM00();
		float m01 = bone.getM01();
		float m10 = bone.getM10();
		float m11 = bone.getM11();
		vertices[X1] = offset[0] * m00 + offset[1] * m01 + x;
		vertices[Y1] = offset[0] * m10 + offset[1] * m11 + y;
		vertices[X2] = offset[2] * m00 + offset[3] * m01 + x;
		vertices[Y2] = offset[2] * m10 + offset[3] * m11 + y;
		vertices[X3] = offset[4] * m00 + offset[5] * m01 + x;
		vertices[Y3] = offset[4] * m10 + offset[5] * m11 + y;
		vertices[X4] = offset[6] * m00 + offset[7] * m01 + x;
		vertices[Y4] = offset[6] * m10 + offset[7] * m11 + y;
	}

	public float[] getWorldVertices () {
		return vertices;
	}

	public float getX () {
		return x;
	}

	public void setX (float x) {
		this.x = x;
	}

	public float getY () {
		return y;
	}

	public void setY (float y) {
		this.y = y;
	}

	public float getScaleX () {
		return scaleX;
	}

	public void setScaleX (float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY () {
		return scaleY;
	}

	public void setScaleY (float scaleY) {
		this.scaleY = scaleY;
	}

	public float getRotation () {
		return rotation;
	}

	public void setRotation (float rotation) {
		this.rotation = rotation;
	}

	public float getWidth () {
		return width;
	}

	public void setWidth (float width) {
		this.width = width;
	}

	public float getHeight () {
		return height;
	}

	public void setHeight (float height) {
		this.height = height;
	}
}
