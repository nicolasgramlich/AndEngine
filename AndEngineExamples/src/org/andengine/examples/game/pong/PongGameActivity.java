package org.andengine.examples.game.pong;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.examples.adt.messages.MessageConstants;
import org.andengine.examples.adt.messages.client.ConnectionPingClientMessage;
import org.andengine.examples.adt.messages.server.ConnectionCloseServerMessage;
import org.andengine.examples.adt.messages.server.ConnectionEstablishedServerMessage;
import org.andengine.examples.adt.messages.server.ConnectionPongServerMessage;
import org.andengine.examples.adt.messages.server.ConnectionRejectedProtocolMissmatchServerMessage;
import org.andengine.examples.adt.messages.server.ServerMessageFlags;
import org.andengine.examples.game.pong.adt.messages.client.MovePaddleClientMessage;
import org.andengine.examples.game.pong.adt.messages.server.SetPaddleIDServerMessage;
import org.andengine.examples.game.pong.adt.messages.server.UpdateBallServerMessage;
import org.andengine.examples.game.pong.adt.messages.server.UpdatePaddleServerMessage;
import org.andengine.examples.game.pong.adt.messages.server.UpdateScoreServerMessage;
import org.andengine.examples.game.pong.util.constants.PongConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.server.IServerMessage;
import org.andengine.extension.multiplayer.protocol.client.IServerMessageHandler;
import org.andengine.extension.multiplayer.protocol.client.connector.ServerConnector;
import org.andengine.extension.multiplayer.protocol.client.connector.SocketConnectionServerConnector.ISocketConnectionServerConnectorListener;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector.ISocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.WifiUtils;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 19:36:45 - 28.02.2011
 */
public class PongGameActivity extends SimpleBaseGameActivity implements PongConstants, IOnSceneTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String LOCALHOST_IP = "127.0.0.1";

	private static final int CAMERA_WIDTH = GAME_WIDTH;
	private static final int CAMERA_HEIGHT = GAME_HEIGHT;

	private static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
	private static final int DIALOG_ENTER_SERVER_IP_ID = DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;
	private static final int DIALOG_SHOW_SERVER_IP_ID = DIALOG_ENTER_SERVER_IP_ID + 1;

	private static final int PADDLEID_NOT_SET = -1;

	private static final int MENU_PING = Menu.FIRST;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private String mServerIP = LOCALHOST_IP;

	private int mPaddleID = PADDLEID_NOT_SET;

	private PongServer mServer;
	private PongServerConnector mServerConnector;

	private Rectangle mBall;
	private final SparseArray<Rectangle> mPaddleMap = new SparseArray<Rectangle>();
	private final SparseArray<Text> mScoreTextMap = new SparseArray<Text>();

	private Font mScoreFont;

	private float mPaddleCenterY;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mCamera.setCenter(0,0);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	public Engine onCreateEngine(final EngineOptions pEngineOptions) {
		this.showDialog(DIALOG_CHOOSE_SERVER_OR_CLIENT_ID);

		return new LimitedFPSEngine(pEngineOptions, FPS);
	}

	@Override
	public void onCreateResources() {
		final ITexture scoreFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);

		FontFactory.setAssetBasePath("font/");
		this.mScoreFont = FontFactory.createFromAsset(this.getFontManager(), scoreFontTexture, this.getAssets(), "LCD.ttf", 32, true, Color.WHITE);
		this.mScoreFont.load();
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();

		final Scene scene = new Scene();

		/* Ball */
		this.mBall = new Rectangle(0, 0, BALL_WIDTH, BALL_HEIGHT, vertexBufferObjectManager);
		scene.attachChild(this.mBall);

		/* Walls */
		scene.attachChild(new Line(-GAME_WIDTH_HALF + 1, -GAME_HEIGHT_HALF, -GAME_WIDTH_HALF + 1, GAME_HEIGHT_HALF, vertexBufferObjectManager)); // Left
		scene.attachChild(new Line(GAME_WIDTH_HALF, -GAME_HEIGHT_HALF, GAME_WIDTH_HALF, GAME_HEIGHT_HALF, vertexBufferObjectManager)); // Right
		scene.attachChild(new Line(-GAME_WIDTH_HALF, -GAME_HEIGHT_HALF + 1, GAME_WIDTH_HALF , -GAME_HEIGHT_HALF + 1, vertexBufferObjectManager)); // Top
		scene.attachChild(new Line(-GAME_WIDTH_HALF, GAME_HEIGHT_HALF, GAME_WIDTH_HALF, GAME_HEIGHT_HALF, vertexBufferObjectManager)); // Bottom

		scene.attachChild(new Line(0, -GAME_HEIGHT_HALF, 0, GAME_HEIGHT_HALF, vertexBufferObjectManager)); // Middle

		/* Paddles */
		final Rectangle paddleLeft = new Rectangle(0, 0, PADDLE_WIDTH, PADDLE_HEIGHT, vertexBufferObjectManager);
		final Rectangle paddleRight = new Rectangle(0, 0, PADDLE_WIDTH, PADDLE_HEIGHT, vertexBufferObjectManager);
		this.mPaddleMap.put(PADDLE_LEFT.getOwnerID(), paddleLeft);
		this.mPaddleMap.put(PADDLE_RIGHT.getOwnerID(), paddleRight);

		scene.attachChild(paddleLeft);
		scene.attachChild(paddleRight);

		/* Scores */
		final Text scoreLeft = new Text(0, -GAME_HEIGHT_HALF + SCORE_PADDING, this.mScoreFont, "0", 2, vertexBufferObjectManager);
		scoreLeft.setPosition(-scoreLeft.getWidth() - SCORE_PADDING, scoreLeft.getY());
		final Text scoreRight = new Text(SCORE_PADDING, -GAME_HEIGHT_HALF + SCORE_PADDING, this.mScoreFont, "0", 2, vertexBufferObjectManager);
		this.mScoreTextMap.put(PADDLE_LEFT.getOwnerID(), scoreLeft);
		this.mScoreTextMap.put(PADDLE_RIGHT.getOwnerID(), scoreRight);

		scene.attachChild(scoreLeft);
		scene.attachChild(scoreRight);

		scene.setOnSceneTouchListener(this);

		scene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				if(PongGameActivity.this.mPaddleID != PADDLEID_NOT_SET) {
					try {
						PongGameActivity.this.mServerConnector.sendClientMessage(new MovePaddleClientMessage(PongGameActivity.this.mPaddleID, PongGameActivity.this.mPaddleCenterY));
					} catch (final IOException e) {
						Debug.e(e);
					}
				}
			}

			@Override
			public void reset() {}
		});

		return scene;
	}

	@Override
	public void onGameCreated() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(Menu.NONE, MENU_PING, Menu.NONE, "Ping Server");
		return super.onCreateOptionsMenu(pMenu);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onMenuItemSelected(final int pFeatureId, final MenuItem pItem) {
		switch(pItem.getItemId()) {
			case MENU_PING:
				try {
					final ConnectionPingClientMessage connectionPingClientMessage = new ConnectionPingClientMessage(); // TODO Pooling
					connectionPingClientMessage.setTimestamp(System.currentTimeMillis());
					this.mServerConnector.sendClientMessage(connectionPingClientMessage);
				} catch (final IOException e) {
					Debug.e(e);
				}
				return true;
			default:
				return super.onMenuItemSelected(pFeatureId, pItem);
		}
	}

	@Override
	protected Dialog onCreateDialog(final int pID) {
		switch(pID) {
			case DIALOG_SHOW_SERVER_IP_ID:
				try {
					return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("Your Server-IP ...")
					.setCancelable(false)
					.setMessage("The IP of your Server is:\n" + WifiUtils.getWifiIPv4Address(this))
					.setPositiveButton(android.R.string.ok, null)
					.create();
				} catch (final UnknownHostException e) {
					return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Your Server-IP ...")
					.setCancelable(false)
					.setMessage("Error retrieving IP of your Server: " + e)
					.setPositiveButton(android.R.string.ok, new OnClickListener() {
						@Override
						public void onClick(final DialogInterface pDialog, final int pWhich) {
							PongGameActivity.this.finish();
						}
					})
					.create();
				}
			case DIALOG_ENTER_SERVER_IP_ID:
				final EditText ipEditText = new EditText(this);
				return new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Enter Server-IP ...")
				.setCancelable(false)
				.setView(ipEditText)
				.setPositiveButton("Connect", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						PongGameActivity.this.mServerIP = ipEditText.getText().toString();
						PongGameActivity.this.initClient();
					}
				})
				.setNegativeButton(android.R.string.cancel, new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						PongGameActivity.this.finish();
					}
				})
				.create();
			case DIALOG_CHOOSE_SERVER_OR_CLIENT_ID:
				return new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Be Server or Client ...")
				.setCancelable(false)
				.setPositiveButton("Client", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						PongGameActivity.this.showDialog(DIALOG_ENTER_SERVER_IP_ID);
					}
				})
				.setNeutralButton("Server", new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						PongGameActivity.this.initServerAndClient();
						PongGameActivity.this.showDialog(DIALOG_SHOW_SERVER_IP_ID);
					}
				})
				.create();
			default:
				return super.onCreateDialog(pID);
		}
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		this.mPaddleCenterY = pSceneTouchEvent.getY();
		return true;
	}

	@Override
	protected void onDestroy() {
		if(this.mServer != null) {
			try {
				this.mServer.sendBroadcastServerMessage(new ConnectionCloseServerMessage());
			} catch (final IOException e) {
				Debug.e(e);
			}
			this.mServer.terminate();
		}

		if(this.mServerConnector != null) {
			this.mServerConnector.terminate();
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

	public void updateScore(final int pPaddleID, final int pPoints) {
		final Text scoreText = this.mScoreTextMap.get(pPaddleID);
		scoreText.setText(String.valueOf(pPoints));

		/* Adjust position of left Score, so that it doesn't overlap the middle line. */
		if(pPaddleID == PADDLE_LEFT.getOwnerID()) {
			scoreText.setPosition(-scoreText.getWidth() - SCORE_PADDING, scoreText.getY());
		}
	}

	public void setPaddleID(final int pPaddleID) {
		this.mPaddleID = pPaddleID;
	}

	public void updatePaddle(final int pPaddleID, final float pX, final float pY) {
		this.mPaddleMap.get(pPaddleID).setPosition(pX, pY);
	}

	public void updateBall(final float pX, final float pY) {
		this.mBall.setPosition(pX, pY);
	}

	private void initServerAndClient() {
		PongGameActivity.this.initServer();

		/* Wait some time after the server has been started, so it actually can start up. */
		try {
			Thread.sleep(500);
		} catch (final Throwable t) {
			Debug.e(t);
		}

		PongGameActivity.this.initClient();
	}

	private void initServer() {
		this.mServer = new PongServer(new ExampleClientConnectorListener());

		this.mServer.start();

		this.mEngine.registerUpdateHandler(this.mServer);
	}

	private void initClient() {
		try {
			this.mServerConnector = new PongServerConnector(this.mServerIP, new ExampleServerConnectorListener());

			this.mServerConnector.getConnection().start();
		} catch (final Throwable t) {
			Debug.e(t);
		}
	}

	private void toast(final String pMessage) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(PongGameActivity.this, pMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private class PongServerConnector extends ServerConnector<SocketConnection> implements PongConstants, ServerMessageFlags {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public PongServerConnector(final String pServerIP, final ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
			super(new SocketConnection(new Socket(pServerIP, SERVER_PORT)), pSocketConnectionServerConnectorListener);

			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, ConnectionCloseServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					PongGameActivity.this.finish();
				}
			});

			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED, ConnectionEstablishedServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					Debug.d("CLIENT: Connection established.");
				}
			});

			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH, ConnectionRejectedProtocolMissmatchServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ConnectionRejectedProtocolMissmatchServerMessage connectionRejectedProtocolMissmatchServerMessage = (ConnectionRejectedProtocolMissmatchServerMessage)pServerMessage;
					if(connectionRejectedProtocolMissmatchServerMessage.getProtocolVersion() > MessageConstants.PROTOCOL_VERSION) {
						//						Toast.makeText(context, text, duration).show();
					} else if(connectionRejectedProtocolMissmatchServerMessage.getProtocolVersion() < MessageConstants.PROTOCOL_VERSION) {
						//						Toast.makeText(context, text, duration).show();
					}
					PongGameActivity.this.finish();
				}
			});

			this.registerServerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PONG, ConnectionPongServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final ConnectionPongServerMessage connectionPongServerMessage = (ConnectionPongServerMessage) pServerMessage;
					final long roundtripMilliseconds = System.currentTimeMillis() - connectionPongServerMessage.getTimestamp();
					Debug.v("Ping: " + roundtripMilliseconds / 2 + "ms");
				}
			});

			this.registerServerMessage(FLAG_MESSAGE_SERVER_SET_PADDLEID, SetPaddleIDServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final SetPaddleIDServerMessage setPaddleIDServerMessage = (SetPaddleIDServerMessage) pServerMessage;
					PongGameActivity.this.setPaddleID(setPaddleIDServerMessage.mPaddleID);
				}
			});

			this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_SCORE, UpdateScoreServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final UpdateScoreServerMessage updateScoreServerMessage = (UpdateScoreServerMessage) pServerMessage;
					PongGameActivity.this.updateScore(updateScoreServerMessage.mPaddleID, updateScoreServerMessage.mScore);
				}
			});

			this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_BALL, UpdateBallServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final UpdateBallServerMessage updateBallServerMessage = (UpdateBallServerMessage) pServerMessage;
					PongGameActivity.this.updateBall(updateBallServerMessage.mX, updateBallServerMessage.mY);
				}
			});

			this.registerServerMessage(FLAG_MESSAGE_SERVER_UPDATE_PADDLE, UpdatePaddleServerMessage.class, new IServerMessageHandler<SocketConnection>() {
				@Override
				public void onHandleMessage(final ServerConnector<SocketConnection> pServerConnector, final IServerMessage pServerMessage) throws IOException {
					final UpdatePaddleServerMessage updatePaddleServerMessage = (UpdatePaddleServerMessage) pServerMessage;
					PongGameActivity.this.updatePaddle(updatePaddleServerMessage.mPaddleID, updatePaddleServerMessage.mX, updatePaddleServerMessage.mY);
				}
			});

		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	private class ExampleServerConnectorListener implements ISocketConnectionServerConnectorListener {
		@Override
		public void onStarted(final ServerConnector<SocketConnection> pServerConnector) {
			PongGameActivity.this.toast("CLIENT: Connected to server.");
		}

		@Override
		public void onTerminated(final ServerConnector<SocketConnection> pServerConnector) {
			PongGameActivity.this.toast("CLIENT: Disconnected from Server.");
			PongGameActivity.this.finish();
		}
	}

	private class ExampleClientConnectorListener implements ISocketConnectionClientConnectorListener {
		@Override
		public void onStarted(final ClientConnector<SocketConnection> pClientConnector) {
			PongGameActivity.this.toast("SERVER: Client connected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}

		@Override
		public void onTerminated(final ClientConnector<SocketConnection> pClientConnector) {
			PongGameActivity.this.toast("SERVER: Client disconnected: " + pClientConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}
	}
}
