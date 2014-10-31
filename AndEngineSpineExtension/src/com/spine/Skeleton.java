package com.spine;

import java.util.LinkedList;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

public class Skeleton {
	final SkeletonData data;
	final LinkedList<Bone> bones;
	final LinkedList<Slot> slots;
	final LinkedList<Slot> drawOrder;
	Skin skin;
	final Color color;
	float time;
	boolean flipX, flipY;

	public Skeleton (SkeletonData data) {
		if (data == null) throw new IllegalArgumentException("data cannot be null.");
		this.data = data;

		bones = new LinkedList<Bone>();
		for (BoneData boneData : data.bones) {
			Bone parent = boneData.parent == null ? null : bones.get(data.bones.indexOf(boneData.parent));
			bones.add(new Bone(boneData, parent));
		}

		slots = new LinkedList<Slot>();
		drawOrder = new LinkedList<Slot>();
		for (SlotData slotData : data.slots) {
			Bone bone = bones.get(data.bones.indexOf(slotData.boneData));
			Slot slot = new Slot(slotData, this, bone);
			slots.add(slot);
			drawOrder.add(slot);
		}

		color = new Color(1, 1, 1, 1);
	}

	/** Copy constructor. */
	public Skeleton (Skeleton skeleton) {
		if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");
		data = skeleton.data;

		bones = new LinkedList<Bone>();
		for (Bone bone : skeleton.bones) {
			Bone parent = bones.get(skeleton.bones.indexOf(bone.parent));
			bones.add(new Bone(bone, parent));
		}

		slots = new LinkedList<Slot>();
		for (Slot slot : skeleton.slots) {
			Bone bone = bones.get(skeleton.bones.indexOf(slot.bone));
			Slot newSlot = new Slot(slot, this, bone);
			slots.add(newSlot);
		}

		drawOrder = new LinkedList<Slot>();
		for (Slot slot : skeleton.drawOrder)
			drawOrder.add(slots.get(skeleton.slots.indexOf(slot)));

		skin = skeleton.skin;
		color = new Color(skeleton.color);
		time = skeleton.time;
	}

	/** Updates the world transform for each bone. */
	public void updateWorldTransform () {
		boolean flipX = this.flipX;
		boolean flipY = this.flipY;
		LinkedList<Bone> bones = this.bones;
		for (int i = 0, n = bones.size(); i < n; i++)
			bones.get(i).updateWorldTransform(flipX, flipY);
	}

	/** Sets the bones and slots to their bind pose values. */
	public void setToBindPose () {
		setBonesToBindPose();
		setSlotsToBindPose();
	}

	public void setBonesToBindPose () {
		LinkedList<Bone> bones = this.bones;
		for (int i = 0, n = bones.size(); i < n; i++)
			bones.get(i).setToBindPose();
	}

	public void setSlotsToBindPose () {
		LinkedList<Slot> slots = this.slots;
		for (int i = 0, n = slots.size(); i < n; i++)
			slots.get(i).setToBindPose(i);
	}

	public void draw (SpriteBatch batch) {
		LinkedList<Slot> drawOrder = this.drawOrder;
		for (int i = 0, n = drawOrder.size(); i < n; i++) {
			Slot slot = drawOrder.get(i);
			Attachment attachment = slot.attachment;
			if (attachment != null) attachment.draw(batch, slot);
		}
	}
	
	public void createAttachementBitmaps() {
		
	}

	public void drawDebug (Scene scene, VertexBufferObjectManager vertex) {
		for (int i = 0, n = bones.size(); i < n; i++) {
			Bone bone = bones.get(i);
			if (bone.parent == null) continue;
			if(bone.boneDebug == null) {
				Line line = new Line(bone.worldX, bone.worldY, 0, 0, vertex);
				line.setColor(Color.RED);
				scene.attachChild(line);
				bone.boneDebug = line;
			}
			
			float x = bone.data.length * bone.m00 + bone.worldX;
			float y = bone.data.length * bone.m10 + bone.worldY;
			
			bone.boneDebug.setPosition(bone.worldX, bone.worldY, x, y);
		}
		
		for (int i = 0, n = bones.size(); i < n; i++) {
			Bone bone = bones.get(i);
			if(bone.boneDebug != null) {
				IEntity point = bone.boneDebug.getChildByTag(15);
				if(point == null) {
					Rectangle rect = new Rectangle(bone.worldX, bone.worldY, 3, 3, vertex);
					bone.boneDebug.attachChild(rect);
					rect.setTag(15);
					rect.setColor(Color.GREEN);
					point = rect;
				}
				
				point.setPosition(bone.worldX, bone.worldY);
			}
		}
	}

	public SkeletonData getData () {
		return data;
	}

	public LinkedList<Bone> getBones () {
		return bones;
	}

	/** @return May return null. */
	public Bone getRootBone () {
		if (bones.size() == 0) return null;
		return bones.getFirst();
	}

	/** @return May be null. */
	public Bone findBone (String boneName) {
		if (boneName == null) throw new IllegalArgumentException("boneName cannot be null.");
		LinkedList<Bone> bones = this.bones;
		for (int i = 0, n = bones.size(); i < n; i++) {
			Bone bone = bones.get(i);
			if (bone.data.name.equals(boneName)) return bone;
		}
		return null;
	}

	/** @return -1 if the bone was not found. */
	public int findBoneIndex (String boneName) {
		if (boneName == null) throw new IllegalArgumentException("boneName cannot be null.");
		LinkedList<Bone> bones = this.bones;
		for (int i = 0, n = bones.size(); i < n; i++)
			if (bones.get(i).data.name.equals(boneName)) return i;
		return -1;
	}

	public LinkedList<Slot> getSlots () {
		return slots;
	}

	/** @return May be null. */
	public Slot findSlot (String slotName) {
		if (slotName == null) throw new IllegalArgumentException("slotName cannot be null.");
		LinkedList<Slot> slots = this.slots;
		for (int i = 0, n = slots.size(); i < n; i++) {
			Slot slot = slots.get(i);
			if (slot.data.name.equals(slotName)) return slot;
		}
		return null;
	}

	/** @return -1 if the bone was not found. */
	public int findSlotIndex (String slotName) {
		if (slotName == null) throw new IllegalArgumentException("slotName cannot be null.");
		LinkedList<Slot> slots = this.slots;
		for (int i = 0, n = slots.size(); i < n; i++)
			if (slots.get(i).data.name.equals(slotName)) return i;
		return -1;
	}

	/** Returns the slots in the order they will be drawn. The returned array may be modified to change the draw order. */
	public LinkedList<Slot> getDrawOrder () {
		return drawOrder;
	}

	/** @return May be null. */
	public Skin getSkin () {
		return skin;
	}

	/** Sets a skin by name.
	 * @see #setSkin(Skin) */
	public void setSkin (String skinName) {
		Skin skin = data.findSkin(skinName);
		if (skin == null) throw new IllegalArgumentException("Skin not found: " + skinName);
		setSkin(skin);
	}

	/** Sets the skin used to look up attachments not found in the {@link SkeletonData#getDefaultSkin() default skin}. Attachments
	 * from the new skin are attached if the corresponding attachment from the old skin is currently attached.
	 * @param newSkin May be null. */
	public void setSkin (Skin newSkin) {
		if (skin != null && newSkin != null) newSkin.attachAll(this, skin);
		skin = newSkin;
	}

	/** @return May be null. */
	public Attachment getAttachment (String slotName, String attachmentName) {
		return getAttachment(data.findSlotIndex(slotName), attachmentName);
	}

	/** @return May be null. */
	public Attachment getAttachment (int slotIndex, String attachmentName) {
		if (attachmentName == null) throw new IllegalArgumentException("attachmentName cannot be null.");
		if (data.defaultSkin != null) {
			Attachment attachment = data.defaultSkin.getAttachment(slotIndex, attachmentName);
			if (attachment != null) return attachment;
		}
		if (skin != null) return skin.getAttachment(slotIndex, attachmentName);
		return null;
	}

	/** @param attachmentName May be null. */
	public void setAttachment (String slotName, String attachmentName) {
		if (slotName == null) throw new IllegalArgumentException("slotName cannot be null.");
		if (attachmentName == null) throw new IllegalArgumentException("attachmentName cannot be null.");
		for (int i = 0, n = slots.size(); i < n; i++) {
			Slot slot = slots.get(i);
			if (slot.data.name.equals(slotName)) {
				slot.setAttachment(getAttachment(i, attachmentName));
				return;
			}
		}
		throw new IllegalArgumentException("Slot not found: " + slotName);
	}

	public Color getColor () {
		return color;
	}

	public boolean getFlipX () {
		return flipX;
	}

	public void setFlipX (boolean flipX) {
		this.flipX = flipX;
	}

	public boolean getFlipY () {
		return flipY;
	}

	public void setFlipY (boolean flipY) {
		this.flipY = flipY;
	}

	public float getTime () {
		return time;
	}

	public void setTime (float time) {
		this.time = time;
	}

	public void update (float delta) {
		time += delta;
	}
}