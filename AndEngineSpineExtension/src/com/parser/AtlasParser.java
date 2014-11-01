package com.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.debug.Debug;

import android.content.Context;

public class AtlasParser
{
	public String file;
	public String format;
	public String[] filter = {"", ""};
	public String repeat;
	
	private TextureManager textureManager;
	private Context context;
	
	public HashMap<String, Asset> assets = new HashMap<String, Asset>();
	public HashMap<String, TextureRegion> regions = new HashMap<String, TextureRegion>();
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mTextureRegion;
	
	public static final String ATTR_FORMAT = "format";
	public static final String ATTR_FILTER = "filter";
	public static final String ATTR_REPEAT = "repeat";
	public static final String ATTR_ROTATE = "rotate";
	public static final String ATTR_XY = "xy";
	public static final String ATTR_SIZE = "size";
	public static final String ATTR_ORIG = "orig";
	public static final String ATTR_OFFSET = "offset";
	public static final String ATTR_INDEX = "index";
	
	public AtlasParser(InputStream in, TextureManager pTextureManager, Context pContext)
	{
		textureManager = pTextureManager;
		context = pContext;
		
		this.read(in);
		
	}
	
	private void read(InputStream in) {
		InputStreamReader input;
		try {
			//input = new FileReader(path);
			input = new InputStreamReader(in);
			BufferedReader bufRead = new BufferedReader(input);
			String myLine = null;
	
			int line = 1;
			String last = "";
			Asset asset = new Asset();
			while ( (myLine = bufRead.readLine()) != null)
			{
				if(myLine.length() == 0) {
					continue;
				} else if(myLine.contains(":")) {
				    String[] array = myLine.split(":");
				    String key = array[0].trim();
				    if(last == "" && key.equals(ATTR_FORMAT)) {
				    	format = array[1].trim();
				    } else if(last == "" && key.equals(ATTR_FILTER)) {
				    	filter = array[1].split(",");
				    } else if(last == "" && key.equals(ATTR_REPEAT)) {
				    	repeat = array[1].trim();
				    } else if(last == "" && key.equals(ATTR_ROTATE)) {
				    	asset.setRotate(array[1].trim());
				    } else if(key.equals(ATTR_XY)) {
				    	final String[] xy = array[1].split(",");
				    	asset.setPosition(xy[0], xy[1]);
				    } else if(key.equals(ATTR_SIZE)) {
				    	final String[] size = array[1].split(",");
				    	asset.setSize(size[0], size[1]);
				    } else if(key.equals(ATTR_ORIG)) {
				    	final String[] orig = array[1].split(",");
				    	asset.setOrigin(orig[0], orig[1]);
				    } else if(key.equals(ATTR_OFFSET)) {
				    	final String[] offset = array[1].split(",");
				    	asset.setOffset(offset[0], offset[1]);
				    } else if(key.equals(ATTR_INDEX)) {
				    	final String index = array[1].trim();
				    	asset.setIndex(index);
				    } else {
				    	Debug.d("missing property: " + key);
				    }
				} else if(line == 1) {
					file = myLine.trim();
				} else if(!myLine.contains(".") && !last.equals(myLine)) {
					last = myLine.trim();
					asset = new Asset();
					asset.setName(last);
					
					assets.put(last, asset);
				}
				
				line++;
			}
			
			bufRead.close();
			
			this.createAtlas();
			this.createRegions();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createAtlas() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(textureManager, 1028, 1028, BitmapTextureFormat.RGBA_8888);
		this.mTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, context, file, 0, 0);
		this.mBitmapTextureAtlas.load();
	}
	
	private void createRegions() {
		for(Asset asset : assets.values()) {
			TextureRegion region = new TextureRegion(mBitmapTextureAtlas, asset.x, asset.y, asset.width, asset.height, 1, asset.rotate);
			regions.put(asset.name, region);
		}
	}
	
	public TextureRegion getRegion() {
		return this.mTextureRegion;
	}
	
	public BitmapTextureAtlas getAtlas() {
		return mBitmapTextureAtlas;
	}
	
	public TextureRegion getRegion(String key) {
		if(!regions.containsKey(key)) return null;
		return regions.get(key);
	}
	
	public class Asset {
		public String name;
		public boolean rotate;
		public float x;
		public float y;
		public float width;
		public float height;
		public float orgX;
		public float orgY;
		public float offsetX;
		public float offsetY;
		public int index;
		
		public Asset() {
			
		}
		
		public void setIndex(String pIndex) {
			index = (int) Float.parseFloat(pIndex);
		}

		public void setOffset(String offX, String offY) {
			offsetX = Float.parseFloat(offX);
			offsetY = Float.parseFloat(offY);
		}

		public void setOrigin(String origX, String origY) {
			orgX = Float.parseFloat(origX);
			orgY = Float.parseFloat(origY);
		}

		public void setSize(String pWidth, String pHeight) {
			width = Float.parseFloat(pWidth);
			height = Float.parseFloat(pHeight);
		}

		public void setPosition(String pX, String pY) {
			x = Float.parseFloat(pX);
			y = Float.parseFloat(pY);
		}

		public void setRotate(String trim) {
			rotate = trim.equals("true") ? true : false;
		}

		public void setName(String pName) {
			name = pName;
		}
	}
}