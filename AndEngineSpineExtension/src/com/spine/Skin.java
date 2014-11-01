package com.spine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/** Stores attachments by slot index and attachment name. */
public class Skin {
	static private final Key lookup = new Key();

	final String name;
	final HashMap<Key, Attachment> attachments = new HashMap<Key, Attachment>();

	public Skin (String name) {
		if (name == null) throw new IllegalArgumentException("name cannot be null.");
		this.name = name;
	}

	public void addAttachment (int slotIndex, String name, Attachment attachment) {
		if (attachment == null) throw new IllegalArgumentException("attachment cannot be null.");
		Key key = new Key();
		key.set(slotIndex, name);
		attachments.put(key, attachment);
	}

	/** @return May be null. */
	public Attachment getAttachment (int slotIndex, String name) {
		lookup.set(slotIndex, name);
		return attachments.get(lookup);
	}

	public void findNamesForSlot (int slotIndex, LinkedList<String> names) {
		if (names == null) throw new IllegalArgumentException("names cannot be null.");
		for (Key key : attachments.keySet())
			if (key.slotIndex == slotIndex) names.add(key.name);
	}

	public void findAttachmentsForSlot (int slotIndex, LinkedList<Attachment> attachments) {
		if (attachments == null) throw new IllegalArgumentException("attachments cannot be null.");
		for (Entry<Key, Attachment> entry : this.attachments.entrySet())
			if (entry.getKey().slotIndex == slotIndex) attachments.add(entry.getValue());
	}

	public void clear () {
		attachments.clear();
	}

	public String getName () {
		return name;
	}

	public String toString () {
		return name;
	}

	static class Key {
		int slotIndex;
		String name;
		int hashCode;

		public void set (int slotName, String name) {
			if (name == null) throw new IllegalArgumentException("attachmentName cannot be null.");
			this.slotIndex = slotName;
			this.name = name;
			hashCode = 31 * (31 + name.hashCode()) + slotIndex;
		}

		public int hashCode () {
			return hashCode;
		}

		public boolean equals (Object object) {
			if (object == null) return false;
			Key other = (Key)object;
			if (slotIndex != other.slotIndex) return false;
			if (!name.equals(other.name)) return false;
			return true;
		}

		public String toString () {
			return slotIndex + ":" + name;
		}
	}

	/** Attach all attachments from this skin if the corresponding attachment from the old skin is currently attached. */
	void attachAll (Skeleton skeleton, Skin oldSkin) {
		for (Entry<Key, Attachment> entry : oldSkin.attachments.entrySet()) {
			int slotIndex = entry.getKey().slotIndex;
			Slot slot = skeleton.slots.get(slotIndex);
			if (slot.attachment == entry.getValue()) {
				Attachment attachment = getAttachment(slotIndex, entry.getKey().name);
				if (attachment != null) slot.setAttachment(attachment);
			}
		}
	}
}
