package com.spine;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.andengine.opengl.texture.region.TextureRegion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.parser.AtlasParser;
import com.spine.Animation.CurveTimeline;
import com.spine.Animation.RotateTimeline;
import com.spine.Animation.ScaleTimeline;
import com.spine.Animation.Timeline;
import com.spine.Animation.TranslateTimeline;
import com.spine.attachments.RegionAttachment;
import com.spine.attachments.RegionSequenceAttachment;
import com.spine.attachments.RegionSequenceAttachment.Mode;
import com.spine.attachments.TextureAtlasAttachmentLoader;

public class SkeletonJson {
	static public final String TIMELINE_SCALE = "scale";
	static public final String TIMELINE_ROTATE = "rotate";
	static public final String TIMELINE_TRANSLATE = "translate";
	static public final String TIMELINE_ATTACHMENT = "attachment";
	static public final String TIMELINE_COLOR = "color";

	private final AttachmentLoader attachmentLoader;
	private float scale = 1;
	
	private LinkedList<TextureRegion> regions = new LinkedList<TextureRegion>();
	
	private AtlasParser atlasParser;
	private SkeletonData skeletonData;

	public SkeletonJson (AtlasParser pAtlasParser, Context context) {
		atlasParser = pAtlasParser;
		attachmentLoader = new TextureAtlasAttachmentLoader(atlasParser.getAtlas(), context);
	}

	public SkeletonJson (AttachmentLoader attachmentLoader) {
		this.attachmentLoader = attachmentLoader;
	}

	public float getScale () {
		return scale;
	}

	/** Scales the bones, images, and animations as they are loaded. */
	public void setScale (float scale) {
		this.scale = scale;
	}
	
	public LinkedList<TextureRegion> getRegions() {
		return regions;
	}
	
	private JSONObject streamToJson(InputStream in) throws IOException, JSONException {
		int size = in.available();
		byte[] buffer = new byte[size];
		in.read(buffer);
		in.close();
		String bufferString = new String(buffer);
		
		return new JSONObject(bufferString);
	}
	
	private JSONArray getJSONArray(JSONObject json, String pKey) {
		JSONArray array = null;
		try {
			array = json.getJSONArray(pKey);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return array;
	}
	
	public SkeletonData readSkeletonData (InputStream file) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");
		
		skeletonData = new SkeletonData();
		
		try {
			JSONObject json = streamToJson(file);
			
			// Bones.
			JSONArray oBones = getJSONArray(json, "bones");
			for(int i = 0; i < oBones.length(); i++) {
				JSONObject boneMap = (JSONObject) oBones.get(i);
				BoneData parent = null;
				String parentName = boneMap.has("parent") ? (String) boneMap.get("parent") : null;
				if (parentName != null) {
					parent = skeletonData.findBone(parentName);
					if (parent == null) throw new Exception("Parent bone not found: " + parentName);
				}
				BoneData boneData = new BoneData((String)boneMap.get("name"), parent);
				boneData.length = getFloat(boneMap, "length", 0) * scale;
				boneData.x = getFloat(boneMap, "x", 0) * scale;
				boneData.y = getFloat(boneMap, "y", 0) * scale;
				boneData.rotation = getFloat(boneMap, "rotation", 0);
				boneData.scaleX = getFloat(boneMap, "scaleX", 1);
				boneData.scaleY = getFloat(boneMap, "scaleY", 1);
				skeletonData.addBone(boneData);
			}
	
			// Slots.
			JSONArray slots = json.getJSONArray("slots");
			if (slots != null) {
				for(int i = 0; i < slots.length(); i++) {
					JSONObject slotMap = slots.getJSONObject(i);
					
					String slotName = getString(slotMap, "name", "");
					String boneName = getString(slotMap, "bone", "");
					BoneData boneData = skeletonData.findBone(boneName);
					if (boneData == null) throw new Exception("Slot bone not found: " + boneName);
					SlotData slotData = new SlotData(slotName, boneData);
	
					// String color = getString(slotMap, "color", "");
					// TODO: Define color
					//if (color != null) slotData.getColor().set(ColorUtils.(color));
	
					slotData.setAttachmentName(getString(slotMap, "attachment", ""));
	
					skeletonData.addSlot(slotData);
				}
			}

			// Skins.
			JSONObject skins = json.getJSONObject("skins");
			if (skins != null) {
				JSONArray keys = skins.names();
				for(int i=0; i < keys.length(); i++) {
					String key = (String) keys.get(i);
					JSONObject skinDatas = skins.getJSONObject(key);
					
					Skin skin = new Skin(key);
					JSONArray pAtt= skinDatas.names();
					for(int j=0; j < pAtt.length(); j++)
					{
						String akey = pAtt.getString(j);
						int slotIndex = skeletonData.findSlotIndex(akey);
						JSONObject data = skinDatas.getJSONObject(akey);
						Attachment attachment = readAttachment(akey, data);
						skin.addAttachment(slotIndex, akey, attachment);
					}
					
					skeletonData.addSkin(skin);
					if (skin.name.equals("default")) skeletonData.setDefaultSkin(skin);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return skeletonData;
	}

	private Attachment readAttachment (String name, JSONObject map)
	{
		Attachment attachment = null;
		try {
			AttachmentType type = AttachmentType.region; //AttachmentType.valueOf((String) map.get(AttachmentType.region.name()));
			attachment = attachmentLoader.newAttachment(type, name);
	
			if (attachment instanceof RegionSequenceAttachment) {
				RegionSequenceAttachment regionSequenceAttachment = (RegionSequenceAttachment)attachment;
	
				Float fps = getFloat(map, "fps", 0);
				if (fps == null) throw new Exception("Region sequence attachment missing fps: " + name);
				regionSequenceAttachment.setFrameTime(fps);
	
				String modeString = (String)map.get("mode");
				regionSequenceAttachment.setMode(modeString == null ? Mode.forward : Mode.valueOf(modeString));
			}
	
			if (attachment instanceof RegionAttachment) {
				RegionAttachment regionAttachment = (RegionAttachment) attachment;
				
				String regionName = map.names().getString(0);
				
				regionAttachment.setX(getFloat(map, "x", 0) * scale);
				regionAttachment.setY(getFloat(map, "y", 0) * scale);
				regionAttachment.setScaleX(getFloat(map, "scaleX", 1));
				regionAttachment.setScaleY(getFloat(map, "scaleY", 1));
				regionAttachment.setRotation(getFloat(map, "rotation", 0));
				regionAttachment.setWidth(getFloat(map, "width", 32) * scale);
				regionAttachment.setHeight(getFloat(map, "height", 32) * scale);
				regionAttachment.updateOffset();
				
				TextureRegion region = atlasParser.getRegion(regionName);
				((RegionAttachment)attachment).setRegion(region);
				
				regions.add(region);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return attachment;
	}

	private float getFloat (JSONObject map, String name, float defaultValue) throws JSONException {
		String value = map.has(name) ? map.getString(name) : null;
		if (value == null) return defaultValue;
		return Float.parseFloat(value);
	}
	
	private String getString (JSONObject map, String name, String defaultValue) throws JSONException {
		String value = map.has(name) ? map.getString(name) : null;
		if (value == null) return defaultValue;
		return value;
	}

	public Animation readAnimation (InputStream file, SkeletonData skeletonData) {
		if (file == null) throw new IllegalArgumentException("file cannot be null.");
		if (skeletonData == null) throw new IllegalArgumentException("skeletonData cannot be null.");
		
		Animation anim = null;
		try {
			JSONObject map = streamToJson(file);

			LinkedList<Timeline> timelines = new LinkedList<Timeline>();
			float duration = 0;
			
			map = map.getJSONObject("bones");
			final JSONArray pval = map.names();
			for(int i = 0; i < pval.length(); i++) {
				String boneName = pval.getString(i);
				
				int boneIndex = skeletonData.findBoneIndex(boneName);
				
				JSONObject pData = map.getJSONObject(boneName);
				
				JSONArray pAnim = pData.names();
				for(int j=0; j < pAnim.length(); j++)
				{
					String timelineName = pAnim.getString(j);
					JSONArray pAnimData = pData.getJSONArray(timelineName);
					if (timelineName.equals(TIMELINE_ROTATE)) {
						RotateTimeline timeline = new RotateTimeline(pAnimData.length());
						timeline.setBoneIndex(boneIndex);

						int keyframeIndex = 0;
						while(!pAnimData.isNull(keyframeIndex)) {
							JSONObject valueMap = pAnimData.getJSONObject(keyframeIndex);
							float time = getFloat(valueMap, "time", 0);
							timeline.setKeyframe(keyframeIndex, time, getFloat(valueMap, "angle", 0));
							readCurve(timeline, keyframeIndex, valueMap);
							keyframeIndex++;
						}
						
						timelines.add(timeline);
						duration = Math.max(duration, timeline.getDuration());

					} else if (timelineName.equals(TIMELINE_TRANSLATE) || timelineName.equals(TIMELINE_SCALE)) {
						TranslateTimeline timeline;
						float timelineScale = 1;
						if (timelineName.equals(TIMELINE_SCALE))
							timeline = new ScaleTimeline(pAnimData.length());
						else {
							timeline = new TranslateTimeline(pAnimData.length());
							timelineScale = scale;
						}
						timeline.setBoneIndex(j);

						int keyframeIndex = 0;
						while(!pAnimData.isNull(keyframeIndex)) {
							JSONObject valueMap = pAnimData.getJSONObject(keyframeIndex);
							float time = getFloat(valueMap, "time", 0);
							Float x = getFloat(valueMap, "x", 0), y = getFloat(valueMap, "y", 0);
							timeline.setKeyframe(keyframeIndex, time, x == null ? 0 : (x * timelineScale), y == null ? 0
								: (y * timelineScale));
							readCurve(timeline, keyframeIndex, valueMap);
							keyframeIndex++;
						}
						
						timelines.add(timeline);
						duration = Math.max(duration, timeline.getDuration());

					} else {
						throw new RuntimeException("Invalid timeline type for a bone: " + timelineName + " (" + boneName + ")");
					}
				}
			}
			
			anim = new Animation(timelines, duration);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return anim;
	}

	private void readCurve (CurveTimeline timeline, int keyframeIndex, JSONObject valueMap) throws JSONException {
		Object curveObject = valueMap.has("curve") ? valueMap.get("curve") : null;
		if (curveObject == null) return;
		if (curveObject.equals("stepped"))
			timeline.setStepped(keyframeIndex);
		else if (curveObject instanceof JSONArray) {
			JSONArray curve = (JSONArray) curveObject;
			float c0 = Float.parseFloat(curve.getString(0));
			float c1 = Float.parseFloat(curve.getString(1));
			float c2 = Float.parseFloat(curve.getString(2));
			float c3 = Float.parseFloat(curve.getString(3));
			timeline.setCurve(keyframeIndex, c0, c1, c2, c3);
		}
	}
}
