package com.spine;

import org.andengine.entity.primitive.Line;

import com.MathUtils;
import com.gdx.utils.Matrix3;

public class Bone {
	final BoneData data;
	final Bone parent;
	float x, y;
	float rotation;
	float scaleX = 1, scaleY = 1;

	float m00, m01, worldX; // a b x
	float m10, m11, worldY; // c d y
	float worldRotation;
	float worldScaleX, worldScaleY;
	
	Line boneDebug;

	/** @param parent May be null. */
	public Bone (BoneData data, Bone parent) {
		if (data == null) throw new IllegalArgumentException("data cannot be null.");
		this.data = data;
		this.parent = parent;
		setToBindPose();
	}

	/** Copy constructor.
	 * @param parent May be null. */
	public Bone (Bone bone, Bone parent) {
		if (bone == null) throw new IllegalArgumentException("bone cannot be null.");
		this.parent = parent;
		data = bone.data;
		x = bone.x;
		y = bone.y;
		rotation = bone.rotation;
		scaleX = bone.scaleX;
		scaleY = bone.scaleY;
	}

	/** Computes the world SRT using the parent bone and the local SRT. */
	public void updateWorldTransform (boolean flipX, boolean flipY) {
		Bone parent = this.parent;
		if (parent != null) {
			worldX = x * parent.m00 + y * parent.m01 + parent.worldX;
			worldY = x * parent.m10 + y * parent.m11 + parent.worldY;
			worldScaleX = parent.worldScaleX * scaleX;
			worldScaleY = parent.worldScaleY * scaleY;
			worldRotation = parent.worldRotation + rotation;
		} else {
			worldX = x;
			worldY = y;
			worldScaleX = scaleX;
			worldScaleY = scaleY;
			worldRotation = rotation;
		}
		float cos = MathUtils.cosDeg(worldRotation);
		float sin = MathUtils.sinDeg(worldRotation);
		m00 = cos * worldScaleX;
		m10 = sin * worldScaleX;
		m01 = -sin * worldScaleY;
		m11 = cos * worldScaleY;
		if (flipX) {
			m00 = -m00;
			m01 = -m01;
		}
		if (flipY) {
			m10 = -m10;
			m11 = -m11;
		}
	}

	public void setToBindPose () {
		BoneData data = this.data;
		x = data.x;
		y = data.y;
		rotation = data.rotation;
		scaleX = data.scaleX;
		scaleY = data.scaleY;
	}

	public BoneData getData () {
		return data;
	}

	public Bone getParent () {
		return parent;
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

	public float getRotation () {
		return rotation;
	}

	public void setRotation (float rotation) {
		this.rotation = rotation;
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

	public float getM00 () {
		return m00;
	}

	public float getM01 () {
		return m01;
	}

	public float getM10 () {
		return m10;
	}

	public float getM11 () {
		return m11;
	}

	public float getWorldX () {
		return worldX;
	}

	public float getWorldY () {
		return worldY;
	}

	public float getWorldRotation () {
		return worldRotation;
	}

	public float getWorldScaleX () {
		return worldScaleX;
	}

	public float getWorldScaleY () {
		return worldScaleY;
	}

	public Matrix3 getWorldTransform (Matrix3 worldTransform) {
		if (worldTransform == null) throw new IllegalArgumentException("worldTransform cannot be null.");
		float[] val = worldTransform.val;
		val[Matrix3.M00] = m00;
		val[Matrix3.M01] = m01;
		val[Matrix3.M02] = worldX;
		val[Matrix3.M10] = m10;
		val[Matrix3.M11] = m11;
		val[Matrix3.M12] = worldY;
		val[Matrix3.M20] = 0;
		val[Matrix3.M21] = 0;
		val[Matrix3.M22] = 1;
		return worldTransform;
	}

	public String toString () {
		return data.name;
	}
}
