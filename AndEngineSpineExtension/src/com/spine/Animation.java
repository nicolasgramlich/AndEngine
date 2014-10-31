package com.spine;

import java.util.LinkedList;

import org.andengine.util.debug.Debug;

import com.MathUtils;

public class Animation {
	private final LinkedList<Timeline> timelines;
	private float duration;

	public Animation (LinkedList<Timeline> timelines, float duration) {
		if (timelines == null) throw new IllegalArgumentException("timelines cannot be null.");
		this.timelines = timelines;
		this.duration = duration;
	}

	public LinkedList<Timeline> getTimelines () {
		return timelines;
	}

	/** Returns the duration of the animation in seconds. Defaults to the max {@link Timeline#getDuration() duration} of the
	 * timelines. */
	public float getDuration () {
		return duration;
	}

	public void setDuration (float duration) {
		this.duration = duration;
	}

	/** Poses the skeleton at the specified time for this animation. */
	public void apply (Skeleton skeleton, float time, boolean loop) {
		if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");

		if (loop && duration != 0) time %= duration;

		LinkedList<Timeline> timelines = this.timelines;
		for (int i = 0, n = timelines.size(); i < n; i++)
			timelines.get(i).apply(skeleton, time, 1);
	}

	/** Poses the skeleton at the specified time for this animation mixed with the current pose.
	 * @param alpha The amount of this animation that affects the current pose. */
	public void mix (Skeleton skeleton, float time, boolean loop, float alpha) {
		if (skeleton == null) throw new IllegalArgumentException("skeleton cannot be null.");

		if (loop && duration != 0) time %= duration;

		LinkedList<Timeline> timelines = this.timelines;
		for (int i = 0, n = timelines.size(); i < n; i++)
			timelines.get(i).apply(skeleton, time, alpha);
	}

	/** @param target After the first and before the last entry. */
	static int binarySearch (float[] values, float target, int step) {
		int low = 0;
		int high = values.length / step - 2;
		if (high == 0) return step;
		int current = high >>> 1;
		while (true) {
			if (values[(current + 1) * step] <= target)
				low = current + 1;
			else
				high = current;
			if (low == high) return (low + 1) * step;
			current = (low + high) >>> 1;
		}
	}

	static int linearSearch (float[] values, float target, int step) {
		for (int i = 0, last = values.length - step; i <= last; i += step) {
			if (values[i] <= target) continue;
			return i;
		}
		return -1;
	}

	/** The keyframes for a single animation timeline. */
	static public interface Timeline {
		/** Returns the time in seconds of the last keyframe. */
		public float getDuration ();

		public int getKeyframeCount ();

		/** Sets the value(s) for the specified time. */
		public void apply (Skeleton skeleton, float time, float alpha);
	}

	/** Base class for frames that use an interpolation bezier curve. */
	static public abstract class CurveTimeline implements Timeline {
		static private final float LINEAR = 0;
		static private final float STEPPED = -1;
		static private final int BEZIER_SEGMENTS = 10;

		private final float[] curves; // dfx, dfy, ddfx, ddfy, dddfx, dddfy, ..

		public CurveTimeline (int keyframeCount) {
			curves = new float[(keyframeCount - 1) * 6];
		}

		public void setLinear (int keyframeIndex) {
			curves[keyframeIndex * 6] = LINEAR;
		}

		public void setStepped (int keyframeIndex) {
			curves[keyframeIndex * 6] = STEPPED;
		}

		/** Sets the control handle positions for an interpolation bezier curve used to transition from this keyframe to the next.
		 * cx1 and cx2 are from 0 to 1, representing the percent of time between the two keyframes. cy1 and cy2 are the percent of
		 * the difference between the keyframe's values. */
		public void setCurve (int keyframeIndex, float cx1, float cy1, float cx2, float cy2) {
			float subdiv_step = 1f / BEZIER_SEGMENTS;
			float subdiv_step2 = subdiv_step * subdiv_step;
			float subdiv_step3 = subdiv_step2 * subdiv_step;
			float pre1 = 3 * subdiv_step;
			float pre2 = 3 * subdiv_step2;
			float pre4 = 6 * subdiv_step2;
			float pre5 = 6 * subdiv_step3;
			float tmp1x = -cx1 * 2 + cx2;
			float tmp1y = -cy1 * 2 + cy2;
			float tmp2x = (cx1 - cx2) * 3 + 1;
			float tmp2y = (cy1 - cy2) * 3 + 1;
			int i = keyframeIndex * 6;
			float[] curves = this.curves;
			curves[i] = cx1 * pre1 + tmp1x * pre2 + tmp2x * subdiv_step3;
			curves[i + 1] = cy1 * pre1 + tmp1y * pre2 + tmp2y * subdiv_step3;
			curves[i + 2] = tmp1x * pre4 + tmp2x * pre5;
			curves[i + 3] = tmp1y * pre4 + tmp2y * pre5;
			curves[i + 4] = tmp2x * pre5;
			curves[i + 5] = tmp2y * pre5;
		}

		public float getCurvePercent (int keyframeIndex, float percent) {
			int curveIndex = keyframeIndex * 6;
			float[] curves = this.curves;
			float dfx = curves[curveIndex];
			if (dfx == LINEAR) return percent;
			if (dfx == STEPPED) return 0;
			float dfy = curves[curveIndex + 1];
			float ddfx = curves[curveIndex + 2];
			float ddfy = curves[curveIndex + 3];
			float dddfx = curves[curveIndex + 4];
			float dddfy = curves[curveIndex + 5];
			float x = dfx, y = dfy;
			int i = BEZIER_SEGMENTS - 2;
			while (true) {
				if (x >= percent) {
					float lastX = x - dfx;
					float lastY = y - dfy;
					return lastY + (y - lastY) * (percent - lastX) / (x - lastX);
				}
				if (i == 0) break;
				i--;
				dfx += ddfx;
				dfy += ddfy;
				ddfx += dddfx;
				ddfy += dddfy;
				x += dfx;
				y += dfy;
			}
			return y + (1 - y) * (percent - x) / (1 - x); // Last point is 1,1.
		}
	}

	static public class RotateTimeline extends CurveTimeline {
		static private final int LAST_FRAME_TIME = -2;
		static private final int FRAME_VALUE = 1;

		private int boneIndex;
		private final float[] frames; // time, value, ..

		public RotateTimeline (int keyframeCount) {
			super(keyframeCount);
			frames = new float[keyframeCount * 2];
		}

		public float getDuration () {
			return frames[frames.length - 2];
		}

		public int getKeyframeCount () {
			return frames.length / 2;
		}

		public void setBoneIndex (int boneIndex) {
			this.boneIndex = boneIndex;
		}

		public int getBoneIndex () {
			return boneIndex;
		}

		public float[] getKeyframes () {
			return frames;
		}

		/** Sets the time and value of the specified keyframe. */
		public void setKeyframe (int keyframeIndex, float time, float value) {
			keyframeIndex *= 2;
			frames[keyframeIndex] = time;
			frames[keyframeIndex + 1] = value;
		}

		public void apply (Skeleton skeleton, float time, float alpha) {
			float[] frames = this.frames;
			if (time < frames[0]) return; // Time is before first frame.

			Bone bone = skeleton.bones.get(boneIndex);

			if (time >= frames[frames.length - 2]) { // Time is after last frame.
				float amount = bone.rotation + frames[frames.length - 1] - bone.rotation;
				while (amount > 180)
					amount -= 360;
				while (amount < -180)
					amount += 360;
				bone.rotation += amount * alpha;
				return;
			}

			// Interpolate between the last frame and the current frame.
			int frameIndex = binarySearch(frames, time, 2);
			float lastFrameValue = frames[frameIndex - 1];
			float frameTime = frames[frameIndex];
			float percent = MathUtils.clamp(1 - (time - frameTime) / (frames[frameIndex + LAST_FRAME_TIME] - frameTime), 0, 1);
			percent = getCurvePercent(frameIndex / 2 - 1, percent);

			float amount = frames[frameIndex + FRAME_VALUE] - lastFrameValue;
			while (amount > 180)
				amount -= 360;
			while (amount < -180)
				amount += 360;
			amount = bone.rotation + (lastFrameValue + amount * percent) - bone.rotation;
			while (amount > 180)
				amount -= 360;
			while (amount < -180)
				amount += 360;
			bone.rotation += amount * alpha;
		}
	}

	static public class TranslateTimeline extends CurveTimeline {
		static final int LAST_FRAME_TIME = -3;
		static final int FRAME_X = 1;
		static final int FRAME_Y = 2;

		int boneIndex;
		final float[] frames; // time, value, value, ..

		public TranslateTimeline (int keyframeCount) {
			super(keyframeCount);
			frames = new float[keyframeCount * 3];
		}

		public float getDuration () {
			return frames[frames.length - 3];
		}

		public int getKeyframeCount () {
			return frames.length / 3;
		}

		public void setBoneIndex (int boneIndex) {
			this.boneIndex = boneIndex;
		}

		public int getBoneIndex () {
			return boneIndex;
		}

		public float[] getKeyframes () {
			return frames;
		}

		/** Sets the time and value of the specified keyframe. */
		public void setKeyframe (int keyframeIndex, float time, float x, float y) {
			keyframeIndex *= 3;
			frames[keyframeIndex] = time;
			frames[keyframeIndex + 1] = x;
			frames[keyframeIndex + 2] = y;
		}

		public void apply (Skeleton skeleton, float time, float alpha) {
			float[] frames = this.frames;
			if (time < frames[0]) return; // Time is before first frame.

			Bone bone = skeleton.bones.get(boneIndex);

			if (time >= frames[frames.length - 3]) { // Time is after last frame.
				bone.x += (bone.x + frames[frames.length - 2] - bone.x) * alpha;
				bone.y += (bone.y + frames[frames.length - 1] - bone.y) * alpha;
				return;
			}

			// Interpolate between the last frame and the current frame.
			int frameIndex = binarySearch(frames, time, 3);
			float lastFrameX = frames[frameIndex - 2];
			float lastFrameY = frames[frameIndex - 1];
			float frameTime = frames[frameIndex];
			float percent = MathUtils.clamp(1 - (time - frameTime) / (frames[frameIndex + LAST_FRAME_TIME] - frameTime), 0, 1);
			percent = getCurvePercent(frameIndex / 3 - 1, percent);

			bone.x += (bone.x + lastFrameX + (frames[frameIndex + FRAME_X] - lastFrameX) * percent - bone.x) * alpha;
			bone.y += (bone.y + lastFrameY + (frames[frameIndex + FRAME_Y] - lastFrameY) * percent - bone.y) * alpha;
		}
	}

	static public class ScaleTimeline extends TranslateTimeline {
		public ScaleTimeline (int keyframeCount) {
			super(keyframeCount);
		}

		public void apply (Skeleton skeleton, float time, float alpha) {
			float[] frames = this.frames;
			if (time < frames[0]) return; // Time is before first frame.

			Bone bone = skeleton.bones.get(boneIndex);
			if (time >= frames[frames.length - 3]) { // Time is after last frame.
				bone.scaleX += (bone.scaleX - 1 + frames[frames.length - 2] - bone.scaleX) * alpha;
				bone.scaleY += (bone.scaleY - 1 + frames[frames.length - 1] - bone.scaleY) * alpha;
				return;
			}

			// Interpolate between the last frame and the current frame.
			int frameIndex = binarySearch(frames, time, 3);
			float lastFrameX = frames[frameIndex - 2];
			float lastFrameY = frames[frameIndex - 1];
			float frameTime = frames[frameIndex];
			float percent = MathUtils.clamp(1 - (time - frameTime) / (frames[frameIndex + LAST_FRAME_TIME] - frameTime), 0, 1);
			percent = getCurvePercent(frameIndex / 3 - 1, percent);

			bone.scaleX += (bone.scaleX - 1 + lastFrameX + (frames[frameIndex + FRAME_X] - lastFrameX) * percent - bone.scaleX)
				* alpha;
			bone.scaleY += (bone.scaleY - 1 + lastFrameY + (frames[frameIndex + FRAME_Y] - lastFrameY) * percent - bone.scaleY)
				* alpha;
		}
	}

	static public class ColorTimeline extends CurveTimeline {
		static private final int LAST_FRAME_TIME = -5;
		static private final int FRAME_R = 1;
		static private final int FRAME_G = 2;
		static private final int FRAME_B = 3;
		static private final int FRAME_A = 4;

		private int slotIndex;
		private final float[] frames; // time, r, g, b, a, ..

		public ColorTimeline (int keyframeCount) {
			super(keyframeCount);
			frames = new float[keyframeCount * 5];
		}

		public float getDuration () {
			return frames[frames.length - 5];
		}

		public int getKeyframeCount () {
			return frames.length / 5;
		}

		public void setSlotIndex (int slotIndex) {
			this.slotIndex = slotIndex;
		}

		public int getSlotIndex () {
			return slotIndex;
		}

		public float[] getKeyframes () {
			return frames;
		}

		/** Sets the time and value of the specified keyframe. */
		public void setKeyframe (int keyframeIndex, float time, float r, float g, float b, float a) {
			keyframeIndex *= 5;
			frames[keyframeIndex] = time;
			frames[keyframeIndex + 1] = r;
			frames[keyframeIndex + 2] = g;
			frames[keyframeIndex + 3] = b;
			frames[keyframeIndex + 4] = a;
		}

		public void apply (Skeleton skeleton, float time, float alpha) {
			float[] frames = this.frames;
			if (time < frames[0]) return; // Time is before first frame.

			//Color color = skeleton.slots.get(slotIndex).color;

			if (time >= frames[frames.length - 5]) { // Time is after last frame.
				int i = frames.length - 1;
				float r = frames[i - 3];
				float g = frames[i - 2];
				float b = frames[i - 1];
				float a = frames[i];
				//color.set(r, g, b, a);
				return;
			}

			// Interpolate between the last frame and the current frame.
			int frameIndex = binarySearch(frames, time, 5);
			float lastFrameR = frames[frameIndex - 4];
			float lastFrameG = frames[frameIndex - 3];
			float lastFrameB = frames[frameIndex - 2];
			float lastFrameA = frames[frameIndex - 1];
			float frameTime = frames[frameIndex];
			float percent = MathUtils.clamp(1 - (time - frameTime) / (frames[frameIndex + LAST_FRAME_TIME] - frameTime), 0, 1);
			percent = getCurvePercent(frameIndex / 5 - 1, percent);

			float r = lastFrameR + (frames[frameIndex + FRAME_R] - lastFrameR) * percent;
			float g = lastFrameG + (frames[frameIndex + FRAME_G] - lastFrameG) * percent;
			float b = lastFrameB + (frames[frameIndex + FRAME_B] - lastFrameB) * percent;
			float a = lastFrameA + (frames[frameIndex + FRAME_A] - lastFrameA) * percent;
			if (alpha < 1) {
				//color.add((r - color.r) * alpha, (g - color.g) * alpha, (b - color.b) * alpha, (a - color.a) * alpha);
			} else {
				//color.set(r, g, b, a);
			}
		}
	}

	static public class AttachmentTimeline implements Timeline {
		private int slotIndex;
		private final float[] frames; // time, ..
		private final String[] attachmentNames;

		public AttachmentTimeline (int keyframeCount) {
			frames = new float[keyframeCount];
			attachmentNames = new String[keyframeCount];
		}

		public float getDuration () {
			return frames[frames.length - 1];
		}

		public int getKeyframeCount () {
			return frames.length;
		}

		public int getSlotIndex () {
			return slotIndex;
		}

		public void setSlotIndex (int slotIndex) {
			this.slotIndex = slotIndex;
		}

		public float[] getKeyframes () {
			return frames;
		}

		public String[] getAttachmentNames () {
			return attachmentNames;
		}

		/** Sets the time and value of the specified keyframe. */
		public void setKeyframe (int keyframeIndex, float time, String attachmentName) {
			frames[keyframeIndex] = time;
			attachmentNames[keyframeIndex] = attachmentName;
		}

		public void apply (Skeleton skeleton, float time, float alpha) {
			float[] frames = this.frames;
			if (time < frames[0]) return; // Time is before first frame.

			int frameIndex;
			if (time >= frames[frames.length - 1]) // Time is after last frame.
				frameIndex = frames.length - 1;
			else
				frameIndex = binarySearch(frames, time, 1) - 1;

			String attachmentName = attachmentNames[frameIndex];
			//skeleton.slots.get(slotIndex).setAttachment(
				//attachmentName == null ? null : skeleton.getAttachment(slotIndex, attachmentName));
		}
	}
}
