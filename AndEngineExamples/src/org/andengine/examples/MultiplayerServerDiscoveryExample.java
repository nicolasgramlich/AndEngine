package org.andengine.examples;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.examples.adt.messages.client.ClientMessageFlags;
import org.andengine.examples.adt.messages.server.ConnectionCloseServerMessage;
import org.andengine.examples.adt.messages.server.ServerMessageFlags;
import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;
import org.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.protocol.client.SocketServerDiscoveryClient;
import org.andengine.extension.multiplayer.protocol.client.SocketServerDiscoveryClient.ISocketServerDiscoveryClientListener;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector.ISocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.protocol.server.SocketServer;
import org.andengine.extension.multiplayer.protocol.server.SocketServer.ISocketServerListener;
import org.andengine.extension.multiplayer.protocol.server.SocketServerDiscoveryServer;
import org.andengine.extension.multiplayer.protocol.server.SocketServerDiscoveryServer.ISocketServerDiscoveryServerListener;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector.ISocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.protocol.shared.IDiscoveryData.DefaultDiscoveryData;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.IPUtils;
import org.andengine.extension.multiplayer.protocol.util.MessagePool;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 17:10:24 - 19.06.2010
 */
public class MultiplayerServerDiscoveryExample extends SimpleBaseGameActivity implements ClientMessageFlags, ServerMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SERVER_PORT = 4444;
	private static final int DISCOVERY_PORT = 4445;
	private static final int LOCAL_PORT = 4446;

	private static final short FLAG_MESSAGE_SERVER_ADD_FACE = 1;
	private static final short FLAG_MESSAGE_SERVER_MOVE_FACE = FLAG_MESSAGE_SERVER_ADD_FACE + 1;

	private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

	private int mFaceIDCounter;
	private final SparseArray<Sprite> mFaces = new SparseArray<Sprite>();

	private SocketServer<SocketConnectionClientConnector> mSocketServer;
	private ServerConnector<SocketConnection> mServerConnector;

	private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();

	private SocketServerDiscoveryServer<DefaultDiscoveryData> mSocketServerDiscoveryServer;
	private SocketServerDiscoveryClient<DefaultDiscoveryData> mSocketServerDiscoveryClient;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MultiplayerServerDiscoveryExample() {
		this.initMessagePool();
	}

	private void initMessagePool() {
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_ADD_FACE, AddFaceServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_MOVE_FACE, MoveFaceServerMessage.class);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		/* We allow only the server to actively send around messages. */
		if(MultiplayerServerDiscoveryExample.this.mSocketServer != null) {
			scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
				@Override
				public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
					if(pSceneTouchEvent.isActionDown()) {
						try {
							final AddFaceServerMessage addFaceServerMessage = (AddFaceServerMessage) MultiplayerServerDiscoveryExample.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_ADD_FACE);
							addFaceServerMessage.set(MultiplayerServerDiscoveryExample.this.mFaceIDCounter++, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

							MultiplayerServerDiscoveryExample.this.mSocketServer.sendBroadcastServerMessage(addFaceServerMessage);

							MultiplayerServerDiscoveryExample.this.mMessagePool.recycleMessage(addFaceServerMessage);
						} catch (final IOException e) {
							Debug.e(e);
						}
						return true;
					} else {
						return false;
					}
				}
			});

			scene.setOnAreaTouchListener(new IOnAreaTouchListener() {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					try {
						final Sprite face = (Sprite)pTouchArea;
						final Integer faceID = (Integer)face.getUserData();

						final MoveFaceServerMessage moveFaceServerMessage = (MoveFaceServerMessage) MultiplayerServerDiscoveryExample.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_MOVE_FACE);
						moveFaceServerMessage.set(faceID, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

						MultiplayerServerDiscoveryExample.this.mSocketServer.sendBroadcastServerMessage(moveFaceServerMessage);

						MultiplayerServerDiscoveryExample.this.mMessagePool.recycleMessage(moveFaceServerMessage);
					} catch (final IOException e) {
						Debug.e(e);
						return false;
					}
					return true;
				}
			});

			scene.setTouchAreaBindingOnActionDownEnabled(true);
		}

		return scene;
	}

	@Override
	protected Dialog onCreateDialog(final int pID) {
		switch(pID) {
			case DIALOG_CHOOSE_SERVER_OR_CLIENT_ID:
				return new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Be Server or Client ...")
				.setMessage("For automatic ServerDiscovery to work, all devices need to be on the same WiFi!")
				.setCancelable(false)
				.setPositiveButton("Client", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerServerDiscoveryExample.this.initServerDiscovery();
					}
				})
				.setNeutralButton("Server", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerServerDiscoveryExample.this.toast("You can add and move sprites, which are only shown on the clients.");
						MultiplayerServerDiscoveryExample.this.initServer();
					}
				})
				.setNegativeButton("Both", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						MultiplayerServerDiscoveryExample.this.toast("You can add sprites and move them, by dragging them.");
						MultiplayerServerDiscoveryExample.this.initServerAndClient();
					}
				})
				.create();
			default:
				return super.onCreateDialog(pID);
		}
	}

	@Override
	protected void onDestroy() {
		if(this.mSocketServer != null) {
			try {
				this.mSocketServer.sendBroadcastServerMessage(new ConnectionCloseServerMessage());
			} catch (final IOException e) {
				Debug.e(e);
			}
			this.mSocketServer.terminate();
		}

		if(this.mSocketServerDiscoveryServer != null) {
			this.mSocketServerDiscoveryServer.terminate();
		}

		if(this.mServerConnector != null) {
			this.mServerConnector.terminate();
		}

		if(this.mSocketServerDiscoveryClient != null) {
			this.mSocketServerDiscoveryClient.terminate();
		}

		super.onDestroy();
	}

	@Override
	public boolean onKeyUp(final int pKeyCode, final KeyEvent pEvent) {
		switch(pKeyCode) {
			case KeyEvent.KEYCODE_BACK:
				this.finish();
				return true;
		}
		return super.onKeyUp(pKeyCode, pEvent);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void addFace(final int pID, final float pX, final float pY) {
		final Scene scene = this.mEngine.getScene();
		/* Create the face and add it to the scene. */
		final Sprite face = new Sprite(0, 0, this.mFaceTextureRegion, this.getVertexBufferObjectManager());
		face.setPosition(pX - face.getWidth() * 0.5f, pY - face.getHeight() * 0.5f);
		face.setUserData(pID);
		this.mFaces.put(pID, face);
		scene.registerTouchArea(face);
		scene.attachChild(face);
	}

	public void moveFace(final int pID, final float pX, final float pY) {
		/* Find and move the face. */
		final Sprite face = this.mFaces.get(pID);
		face.setPosition(pX - face.getWidth() * 0.5f, pY - face.getHeight() * 0.5f);
	}

	private void initServerAndClient() {
		this.initServer();

		/* Wait some time after the server has been started, so it actually can start up. */
		try {
			Thread.sleep(500);
		} catch (final Throwable t) {
			Debug.e(t);
		}

		this.initServerDiscovery();
	}

	private void initServer() {
		this.mSocketServer = new SocketServer<SocketConnectionClientConnector>(SERVER_PORT, new ExampleClientConnectorListener(), new ExampleServerStateListener()) {
			@Override
			protected SocketConnectionClientConnector newClientConnector(final SocketConnection pSocketConnection) throws IOException {
				return new SocketConnectionClientConnector(pSocketConnection);
			}
		};

		this.mSocketServer.start();

		try {
			final byte[] wifiIPv4Address = WifiUtils.getWifiIPv4AddressRaw(this);
			this.mSocketServerDiscoveryServer = new SocketServerDiscoveryServer<DefaultDiscoveryData>(DISCOVERY_PORT, new ExampleSocketServerDiscoveryServerListener()) {
				@Override
				protected DefaultDiscoveryData onCreateDiscoveryResponse() {
					return new DefaultDiscoveryData(wifiIPv4Address, SERVER_PORT);
				}
			};
			this.mSocketServerDiscoveryServer.start();
		} catch (final Throwable t) {
			Debug.e(t);
		}
	}

	private void initServerDiscovery() {
		try {
			this.mSocketServerDiscoveryClient = new SocketServerDiscoveryClient<DefaultDiscoveryData>(WifiUtils.getBroadcastIPAddressRaw(this), DISCOVERY_PORT, LOCAL_PORT, DefaultDiscoveryData.class, new ISocketServerDiscoveryClientListener<DefaultDiscoveryData>() {
				@Override
				public void onDiscovery(final SocketServerDiscoveryClient<DefaultDiscoveryData> pSocketServerDiscoveryClient, final DefaultDiscoveryData pDiscoveryData) {
					try {
						final String ipAddressAsString = IPUtils.ipAddressToString(pDiscoveryData.getServerIP());
						MultiplayerServerDiscoveryExample.this.toast("DiscoveryClient: Server discovered at: " + ipAddressAsString + ":" + pDiscoveryData.getServerPort());
						MultiplayerServerDiscoveryExample.this.initClient(ipAddressAsString, pDiscoveryData.getServerPort());
					} catch (final UnknownHostException e) {
						MultiplayerServerDiscoveryExample.this.toast("DiscoveryClient: IPException: " + e);
					}
				}

				@Override
				public void onTimeout(final SocketServerDiscoveryClient<DefaultDiscoveryData> pSocketServerDiscoveryClient, final SocketTimeoutException pSocketTimeoutException) {
					Debug.e(pSocketTimeoutException);
					MultiplayerServerDiscoveryExample.this.toast("DiscoveryClient: Timeout: " + pSocketTimeoutException);
				}

				@Override
				public void onException(final SocketServerDiscoveryClient<DefaultDiscoveryData> pSocketServerDiscoveryClient, final Throwable pThrowable) {
					Debug.e(pThrowable);
					MultiplayerServerDiscoveryExample.this.toast("DiscoveryClient: Exception: " + pThrowable);
				}
			});

			this.mSocketServerDiscoveryClient.discoverAsync();
		} catch (final Throwable t) {
			Debug.e(t);
		}
	}

	private void initClient(final String pIPAddress, final int pPort) {
		try {
			this.mServerConnector = new SocketConnectionServerConnector(new SocketConnection(new Socket(pIPAddress, pPort)), new ExampleServerConnectorListener());

			this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					MultiplayerServerDiscoveryExample.this.finish();
				}
			});

			this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_ADD_FACE, AddFaceServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final AddFaceServerMessage addFaceServerMessage = (AddFaceServerMessage)pServerMessage;
					MultiplayerServerDiscoveryExample.this.addFace(addFaceServerMessage.mID, addFaceServerMessage.mX, addFaceServerMessage.mY);
				}
			});

			this.mServerConnector.registerServerMessage(FLAG_MESSAGE_SERVER_MOVE_FACE, MoveFaceServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final MoveFaceServerMessage moveFaceServerMessage = (MoveFaceServerMessage)pServerMessage;
					MultiplayerServerDiscoveryExample.this.moveFace(moveFaceServerMessage.mID, moveFaceServerMessage.mX, moveFaceServerMessage.mY);
				}
			});

			this.mServerConnector.getConnection().start();
		} catch (final Throwable t) {
			Debug.e(t);
		}
	}

	private void log(final String pMessage) {
		Debug.d(pMessage);
	}

	private void toast(final String pMessage) {
		this.log(pMessage);
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MultiplayerServerDiscoveryExample.this, pMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class AddFaceServerMessage extends ServerMessage {
		private int mID;
		private float mX;
		private float mY;

		public AddFaceServerMessage() {

		}

		public AddFaceServerMessage(final int pID, final float pX, final float pY) {
			this.mID = pID;
			this.mX = pX;
			this.mY = pY;
		}

		public void set(final int pID, final float pX, final float pY) {
			this.mID = pID;
			this.mX = pX;
			this.mY = pY;
		}

		@Override
		public short getFlag() {
			return FLAG_MESSAGE_SERVER_ADD_FACE;
		}

		@Override
		protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
			this.mID = pDataInputStream.readInt();
			this.mX = pDataInputStream.readFloat();
			this.mY = pDataInputStream.readFloat();
		}

		@Override
		protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
			pDataOutputStream.writeInt(this.mID);
			pDataOutputStream.writeFloat(this.mX);
			pDataOutputStream.writeFloat(this.mY);
		}
	}

	public static class MoveFaceServerMessage extends ServerMessage {
		private int mID;
		private float mX;
		private float mY;

		public MoveFaceServerMessage() {

		}

		public MoveFaceServerMessage(final int pID, final float pX, final float pY) {
			this.mID = pID;
			this.mX = pX;
			this.mY = pY;
		}

		public void set(final int pID, final float pX, final float pY) {
			this.mID = pID;
			this.mX = pX;
			this.mY = pY;
		}

		@Override
		public short getFlag() {
			return FLAG_MESSAGE_SERVER_MOVE_FACE;
		}

		@Override
		protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
			this.mID = pDataInputStream.readInt();
			this.mX = pDataInputStream.readFloat();
			this.mY = pDataInputStream.readFloat();
		}

		@Override
		protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
			pDataOutputStream.writeInt(this.mID);
			pDataOutputStream.writeFloat(this.mX);
			pDataOutputStream.writeFloat(this.mY);
		}
	}

	private class ExampleServerConnectorListener implements ISocketConnectionServerConnectorListener {
		@Override
		public void onStarted(final ServerConnector<SocketConnection> pConnector) {
			MultiplayerServerDiscoveryExample.this.toast("Client: Connected to server.");
		}

		@Override
		public void onTerminated(final ServerConnector<SocketConnection> pConnector) {
			MultiplayerServerDiscoveryExample.this.toast("Client: Disconnected from Server...");
			MultiplayerServerDiscoveryExample.this.finish();
		}
	}

	private class ExampleServerStateListener implements ISocketServerListener<SocketConnectionClientConnector> {
		@Override
		public void onStarted(final SocketServer<SocketConnectionClientConnector> pSocketServer) {
			MultiplayerServerDiscoveryExample.this.toast("Server: Started.");
		}

		@Override
		public void onTerminated(final SocketServer<SocketConnectionClientConnector> pSocketServer) {
			MultiplayerServerDiscoveryExample.this.toast("Server: Terminated.");
		}

		@Override
		public void onException(final SocketServer<SocketConnectionClientConnector> pSocketServer, final Throwable pThrowable) {
			Debug.e(pThrowable);
			MultiplayerServerDiscoveryExample.this.toast("Server: Exception: " + pThrowable);
		}
	}

	private class ExampleClientConnectorListener implements ISocketConnectionClientConnectorListener {
		@Override
		public void onStarted(final ClientConnector<SocketConnection> pConnector) {
			MultiplayerServerDiscoveryExample.this.toast("Server: Client connected: " + pConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}

		@Override
		public void onTerminated(final ClientConnector<SocketConnection> pConnector) {
			MultiplayerServerDiscoveryExample.this.toast("Server: Client disconnected: " + pConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}
	}

	public class ExampleSocketServerDiscoveryServerListener implements ISocketServerDiscoveryServerListener<DefaultDiscoveryData> {
		@Override
		public void onStarted(final SocketServerDiscoveryServer<DefaultDiscoveryData> pSocketServerDiscoveryServer) {
			MultiplayerServerDiscoveryExample.this.toast("DiscoveryServer: Started.");
		}

		@Override
		public void onTerminated(final SocketServerDiscoveryServer<DefaultDiscoveryData> pSocketServerDiscoveryServer) {
			MultiplayerServerDiscoveryExample.this.toast("DiscoveryServer: Terminated.");
		}

		@Override
		public void onException(final SocketServerDiscoveryServer<DefaultDiscoveryData> pSocketServerDiscoveryServer, final Throwable pThrowable) {
			Debug.e(pThrowable);
			MultiplayerServerDiscoveryExample.this.toast("DiscoveryServer: Exception: " + pThrowable);
		}

		@Override
		public void onDiscovered(final SocketServerDiscoveryServer<DefaultDiscoveryData> pSocketServerDiscoveryServer, final InetAddress pInetAddress, final int pPort) {
			MultiplayerServerDiscoveryExample.this.toast("DiscoveryServer: Discovered by: " + pInetAddress.getHostAddress() + ":" + pPort);
		}
	}
}
