package com.spine;

import java.util.LinkedList;

public class SkeletonData {
	final LinkedList<BoneData> bones = new LinkedList<BoneData>(); // Ordered parents first.
	final LinkedList<SlotData> slots = new LinkedList<SlotData>(); // Bind pose draw order.
	final LinkedList<Skin> skins = new LinkedList<Skin>();
	Skin defaultSkin;

	public void clear () {
		bones.clear();
		slots.clear();
		skins.clear();
		defaultSkin = null;
	}

	// --- Bones.

	public void addBone (BoneData bone) {
		if (bone == null) throw new IllegalArgumentException("bone cannot be null.");
		bones.add(bone);
	}

	public LinkedList<BoneData> getBones () {
		return bones;
	}

	/** @return May be null. */
	public BoneData findBone (String boneName) {
		if (boneName == null) throw new IllegalArgumentException("boneName cannot be null.");
		LinkedList<BoneData> bones = this.bones;
		for (int i = 0, n = bones.size(); i < n; i++) {
			BoneData bone = bones.get(i);
			if (bone.name.equals(boneName)) return bone;
		}
		return null;
	}

	/** @return -1 if the bone was not found. */
	public int findBoneIndex (String boneName) {
		if (boneName == null) throw new IllegalArgumentException("boneName cannot be null.");
		LinkedList<BoneData> bones = this.bones;
		for (int i = 0, n = bones.size(); i < n; i++)
			if (bones.get(i).name.equals(boneName)) return i;
		return -1;
	}

	// --- Slots.

	public void addSlot (SlotData slot) {
		if (slot == null) throw new IllegalArgumentException("slot cannot be null.");
		slots.add(slot);
	}

	public LinkedList<SlotData> getSlots () {
		return slots;
	}

	/** @return May be null. */
	public SlotData findSlot (String slotName) {
		if (slotName == null) throw new IllegalArgumentException("slotName cannot be null.");
		LinkedList<SlotData> slots = this.slots;
		for (int i = 0, n = slots.size(); i < n; i++) {
			SlotData slot = slots.get(i);
			if (slot.name.equals(slotName)) return slot;
		}
		return null;
	}

	/** @return -1 if the bone was not found. */
	public int findSlotIndex (String slotName) {
		if (slotName == null) throw new IllegalArgumentException("slotName cannot be null.");
		LinkedList<SlotData> slots = this.slots;
		for (int i = 0, n = slots.size(); i < n; i++)
			if (slots.get(i).name.equals(slotName)) return i;
		return -1;
	}

	// --- Skins.

	/** @return May be null. */
	public Skin getDefaultSkin () {
		return defaultSkin;
	}

	/** @param defaultSkin May be null. */
	public void setDefaultSkin (Skin defaultSkin) {
		this.defaultSkin = defaultSkin;
	}

	public void addSkin (Skin skin) {
		if (skin == null) throw new IllegalArgumentException("skin cannot be null.");
		skins.add(skin);
	}

	/** @return May be null. */
	public Skin findSkin (String skinName) {
		if (skinName == null) throw new IllegalArgumentException("skinName cannot be null.");
		for (Skin skin : skins)
			if (skin.name.equals(skinName)) return skin;
		return null;
	}

	/** Returns all skins, including the default skin. */
	public LinkedList<Skin> getSkins () {
		return skins;
	}
}
