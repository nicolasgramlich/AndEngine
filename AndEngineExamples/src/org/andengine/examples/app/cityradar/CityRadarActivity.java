package org.andengine.examples.app.cityradar;

import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.examples.adt.cityradar.City;
import org.andengine.input.sensor.location.ILocationListener;
import org.andengine.input.sensor.location.LocationProviderStatus;
import org.andengine.input.sensor.location.LocationSensorOptions;
import org.andengine.input.sensor.orientation.IOrientationListener;
import org.andengine.input.sensor.orientation.OrientationData;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.ease.EaseLinear;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class CityRadarActivity extends SimpleBaseGameActivity implements IOrientationListener, ILocationListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final boolean USE_MOCK_LOCATION = false;
	private static final boolean USE_ACTUAL_LOCATION = !USE_MOCK_LOCATION;

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 800;

	private static final int GRID_SIZE = 80;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private BuildableBitmapTextureAtlas mBuildableBitmapTextureAtlas;

	private ITextureRegion mRadarPointTextureRegion;
	private ITextureRegion mRadarTextureRegion;

	private Font mFont;

	private Location mUserLocation;

	private final ArrayList<City> mCities = new ArrayList<City>();
	private final HashMap<City, Sprite> mCityToCitySpriteMap = new HashMap<City, Sprite>();
	private final HashMap<City, Text> mCityToCityNameTextMap = new HashMap<City, Text>();
	private Scene mScene;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CityRadarActivity() {
		this.mCities.add(new City("London", 51.509, -0.118));
		this.mCities.add(new City("New York", 40.713, -74.006));
		//		this.mCities.add(new City("Paris", 48.857, 2.352));
		this.mCities.add(new City("Beijing", 39.929, 116.388));
		this.mCities.add(new City("Sydney", -33.850, 151.200));
		this.mCities.add(new City("Berlin", 52.518, 13.408));
		this.mCities.add(new City("Rio", -22.908, -43.196));
		this.mCities.add(new City("New Delhi", 28.636, 77.224));
		this.mCities.add(new City("Cape Town", -33.926, 18.424));

		this.mUserLocation = new Location(LocationManager.GPS_PROVIDER);

		if(USE_MOCK_LOCATION) {
			this.mUserLocation.setLatitude(51.518);
			this.mUserLocation.setLongitude(13.408);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CityRadarActivity.CAMERA_WIDTH, CityRadarActivity.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), this.mCamera);
	}

	@Override
	public org.andengine.engine.Engine onCreateEngine(final EngineOptions pEngineOptions) {
		return new org.andengine.engine.Engine(pEngineOptions);
	}

	@Override
	public void onCreateResources() {
		/* Init font. */
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.DEFAULT, 12, true, Color.WHITE);
		this.mFont.load();

		/* Init TextureRegions. */
		this.mBuildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 256, TextureOptions.BILINEAR);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mRadarTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "radar.png");
		this.mRadarPointTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBuildableBitmapTextureAtlas, this, "radarpoint.png");

		try {
			this.mBuildableBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.mBuildableBitmapTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mScene = new Scene();

		final HUD hud = new HUD();
		this.mCamera.setHUD(hud);

		/* BACKGROUND */
		this.initBackground(hud);

		/* CITIES */
		this.initCitySprites();

		return this.mScene;
	}

	private void initCitySprites() {
		final int cityCount = this.mCities.size();

		for(int i = 0; i < cityCount; i++) {
			final City city = this.mCities.get(i);

			final Sprite citySprite = new Sprite(CityRadarActivity.CAMERA_WIDTH / 2, CityRadarActivity.CAMERA_HEIGHT / 2, this.mRadarPointTextureRegion, this.getVertexBufferObjectManager());
			citySprite.setColor(0, 0.5f, 0, 1f);

			final Text cityNameText = new Text(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2, this.mFont, city.getName(), this.getVertexBufferObjectManager()) {
				@Override
				protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
					/* This ensures that the name of the city is always 'pointing down'. */
					this.setRotation(-CityRadarActivity.this.mCamera.getRotation());
					super.onManagedDraw(pGLState, pCamera);
				}
			};
			cityNameText.setRotationCenterY(- citySprite.getHeight() / 2);

			this.mCityToCityNameTextMap.put(city, cityNameText);
			this.mCityToCitySpriteMap.put(city, citySprite);

			this.mScene.attachChild(citySprite);
			this.mScene.attachChild(cityNameText);
		}
	}

	private void initBackground(final IEntity pEntity) {
		/* Vertical Grid lines. */
		for(int i = CityRadarActivity.GRID_SIZE / 2; i < CityRadarActivity.CAMERA_WIDTH; i += CityRadarActivity.GRID_SIZE) {
			final Line line = new Line(i, 0, i, CityRadarActivity.CAMERA_HEIGHT, this.getVertexBufferObjectManager());
			line.setColor(0, 0.5f, 0, 1f);
			pEntity.attachChild(line);
		}

		/* Horizontal Grid lines. */
		for(int i = CityRadarActivity.GRID_SIZE / 2; i < CityRadarActivity.CAMERA_HEIGHT; i += CityRadarActivity.GRID_SIZE) {
			final Line line = new Line(0, i, CityRadarActivity.CAMERA_WIDTH, i, this.getVertexBufferObjectManager());
			line.setColor(0, 0.5f, 0, 1f);
			pEntity.attachChild(line);
		}

		/* Radar sprite. */
		final Sprite radarSprite = new Sprite(CityRadarActivity.CAMERA_WIDTH / 2 - this.mRadarTextureRegion.getWidth(), CityRadarActivity.CAMERA_HEIGHT / 2 - this.mRadarTextureRegion.getHeight(), this.mRadarTextureRegion, this.getVertexBufferObjectManager());
		radarSprite.setColor(0, 1f, 0, 1f);
		radarSprite.setRotationCenter(radarSprite.getWidth(), radarSprite.getHeight());
		radarSprite.registerEntityModifier(new LoopEntityModifier(new RotationModifier(3, 0, 360, EaseLinear.getInstance())));
		pEntity.attachChild(radarSprite);

		/* Title. */
		final Text titleText = new Text(0, 0, this.mFont, "-- CityRadar --", this.getVertexBufferObjectManager());
		titleText.setPosition(CAMERA_WIDTH / 2 - titleText.getWidth() / 2, titleText.getHeight() + 35);
		titleText.setScale(2);
		titleText.setScaleCenterY(0);
		pEntity.attachChild(titleText);
	}

	@Override
	public void onGameCreated() {
		this.refreshCitySprites();
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.enableOrientationSensor(this);

		final LocationSensorOptions locationSensorOptions = new LocationSensorOptions();
		locationSensorOptions.setAccuracy(Criteria.ACCURACY_COARSE);
		locationSensorOptions.setMinimumTriggerTime(0);
		locationSensorOptions.setMinimumTriggerDistance(0);
		this.enableLocationSensor(this, locationSensorOptions);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.mEngine.disableOrientationSensor(this);
		this.mEngine.disableLocationSensor(this);
	}

	@Override
	public void onOrientationAccuracyChanged(final OrientationData pOrientationData) {

	}

	@Override
	public void onOrientationChanged(final OrientationData pOrientationData) {
		this.mCamera.setRotation(-pOrientationData.getYaw());
	}

	@Override
	public void onLocationChanged(final Location pLocation) {
		if(USE_ACTUAL_LOCATION) {
			this.mUserLocation = pLocation;
		}
		this.refreshCitySprites();
	}

	@Override
	public void onLocationLost() {
	}

	@Override
	public void onLocationProviderDisabled() {
	}

	@Override
	public void onLocationProviderEnabled() {
	}

	@Override
	public void onLocationProviderStatusChanged(final LocationProviderStatus pLocationProviderStatus, final Bundle pBundle) {
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void refreshCitySprites() {
		final double userLatitudeRad = MathUtils.degToRad((float) this.mUserLocation.getLatitude());
		final double userLongitudeRad = MathUtils.degToRad((float) this.mUserLocation.getLongitude());

		final int cityCount = this.mCities.size();

		double maxDistance = Double.MIN_VALUE;

		/* Calculate the distances and bearings of the cities to the location of the user. */
		for(int i = 0; i < cityCount; i++) {
			final City city = this.mCities.get(i);

			final double cityLatitudeRad = MathUtils.degToRad((float) city.getLatitude());
			final double cityLongitudeRad = MathUtils.degToRad((float) city.getLongitude());

			city.setDistanceToUser(GeoMath.calculateDistance(userLatitudeRad, userLongitudeRad, cityLatitudeRad, cityLongitudeRad));
			city.setBearingToUser(GeoMath.calculateBearing(userLatitudeRad, userLongitudeRad, cityLatitudeRad, cityLongitudeRad));

			maxDistance = Math.max(maxDistance, city.getDistanceToUser());
		}

		/* Calculate a scaleRatio so that all cities are visible at all times. */
		final double scaleRatio = (CityRadarActivity.CAMERA_WIDTH / 2) / maxDistance * 0.93f;

		for(int i = 0; i < cityCount; i++) {
			final City city = this.mCities.get(i);

			final Sprite citySprite = this.mCityToCitySpriteMap.get(city);
			final Text cityNameText = this.mCityToCityNameTextMap.get(city);

			final float bearingInRad = MathUtils.degToRad(90 - (float) city.getBearingToUser());

			final float x = (float) (CityRadarActivity.CAMERA_WIDTH / 2 + city.getDistanceToUser() * scaleRatio * Math.cos(bearingInRad));
			final float y = (float) (CityRadarActivity.CAMERA_HEIGHT / 2 - city.getDistanceToUser() * scaleRatio * Math.sin(bearingInRad));

			citySprite.setPosition(x - citySprite.getWidth() / 2, y - citySprite.getHeight() / 2);

			final float textX = x - cityNameText.getWidth() / 2;
			final float textY = y + citySprite.getHeight() / 2;

			cityNameText.setPosition(textX, textY);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	/**
	 * Note: Formulas taken from <a href="http://www.movable-type.co.uk/scripts/latlong.html">here</a>.
	 */
	private static class GeoMath {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final double RADIUS_EARTH_METERS = 6371000;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		/**
		 * @return the distance in meters.
		 */
		public static double calculateDistance(final double pLatitude1, final double pLongitude1, final double pLatitude2, final double pLongitude2) {
			return Math.acos(Math.sin(pLatitude1) * Math.sin(pLatitude2) + Math.cos(pLatitude1) * Math.cos(pLatitude2) * Math.cos(pLongitude2 - pLongitude1)) * RADIUS_EARTH_METERS;
		}

		/**
		 * @return the bearing in degrees.
		 */
		public static double calculateBearing(final double pLatitude1, final double pLongitude1, final double pLatitude2, final double pLongitude2) {
			final double y = Math.sin(pLongitude2 - pLongitude1) * Math.cos(pLatitude2);
			final double x = Math.cos(pLatitude1) * Math.sin(pLatitude2) - Math.sin(pLatitude1) * Math.cos(pLatitude2) * Math.cos(pLongitude2 - pLongitude1);
			final float bearing = MathUtils.radToDeg((float) Math.atan2(y, x));
			return (bearing + 360) % 360;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}