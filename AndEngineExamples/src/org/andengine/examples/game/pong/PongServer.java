package org.andengine.examples.game.pong;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.examples.adt.messages.MessageConstants;
import org.andengine.examples.adt.messages.client.ClientMessageFlags;
import org.andengine.examples.adt.messages.client.ConnectionCloseClientMessage;
import org.andengine.examples.adt.messages.client.ConnectionEstablishClientMessage;
import org.andengine.examples.adt.messages.client.ConnectionPingClientMessage;
import org.andengine.examples.adt.messages.server.ConnectionEstablishedServerMessage;
import org.andengine.examples.adt.messages.server.ConnectionPongServerMessage;
import org.andengine.examples.adt.messages.server.ConnectionRejectedProtocolMissmatchServerMessage;
import org.andengine.examples.adt.messages.server.ServerMessageFlags;
import org.andengine.examples.game.pong.adt.PaddleUserData;
import org.andengine.examples.game.pong.adt.Score;
import org.andengine.examples.game.pong.adt.messages.client.MovePaddleClientMessage;
import org.andengine.examples.game.pong.adt.messages.server.SetPaddleIDServerMessage;
import org.andengine.examples.game.pong.adt.messages.server.UpdateBallServerMessage;
import org.andengine.examples.game.pong.adt.messages.server.UpdatePaddleServerMessage;
import org.andengine.examples.game.pong.adt.messages.server.UpdateScoreServerMessage;
import org.andengine.examples.game.pong.util.constants.PongConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.IMessage;
import org.andengine.extension.multiplayer.protocol.adt.message.client.IClientMessage;
import org.andengine.extension.multiplayer.protocol.server.IClientMessageHandler;
import org.andengine.extension.multiplayer.protocol.server.SocketServer;
import org.andengine.extension.multiplayer.protocol.server.SocketServer.ISocketServerListener.DefaultSocketServerListener;
import org.andengine.extension.multiplayer.protocol.server.connector.ClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector;
import org.andengine.extension.multiplayer.protocol.server.connector.SocketConnectionClientConnector.ISocketConnectionClientConnectorListener;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.extension.multiplayer.protocol.util.MessagePool;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.util.SparseArray;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:00:09 - 28.02.2011
 */
public class PongServer extends SocketServer<SocketConnectionClientConnector> implements IUpdateHandler, PongConstants, ContactListener, ClientMessageFlags, ServerMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final FixtureDef PADDLE_FIXTUREDEF = PhysicsFactory.createFixtureDef(1, 1, 0);
	private static final FixtureDef BALL_FIXTUREDEF = PhysicsFactory.createFixtureDef(1, 1, 0);
	private static final FixtureDef WALL_FIXTUREDEF = PhysicsFactory.createFixtureDef(1, 1, 0);

	// ===========================================================
	// Fields
	// ===========================================================

	private final PhysicsWorld mPhysicsWorld;
	private final Body mBallBody;
	private final SparseArray<Body> mPaddleBodies = new SparseArray<Body>();
	private boolean mResetBall = true;
	private final SparseArray<Score> mPaddleScores = new SparseArray<Score>();

	private final MessagePool<IMessage> mMessagePool = new MessagePool<IMessage>();
	private final ArrayList<UpdatePaddleServerMessage> mUpdatePaddleServerMessages = new ArrayList<UpdatePaddleServerMessage>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public PongServer(final ISocketConnectionClientConnectorListener pSocketConnectionClientConnectorListener) {
		super(SERVER_PORT, pSocketConnectionClientConnectorListener, new DefaultSocketServerListener<SocketConnectionClientConnector>());

		this.initMessagePool();

		this.mPaddleScores.put(PADDLE_LEFT.getOwnerID(), new Score());
		this.mPaddleScores.put(PADDLE_RIGHT.getOwnerID(), new Score());

		this.mPhysicsWorld = new FixedStepPhysicsWorld(FPS, 2, new Vector2(0, 0), false, 8, 8);

		this.mPhysicsWorld.setContactListener(this);

		/* Ball */
		this.mBallBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld, 0, 0, BALL_RADIUS, BodyType.DynamicBody, BALL_FIXTUREDEF);
		this.mBallBody.setBullet(true);

		/* Paddles */
		final Body paddleBodyLeft = PhysicsFactory.createBoxBody(this.mPhysicsWorld, -GAME_WIDTH_HALF, 0, PADDLE_WIDTH, PADDLE_HEIGHT, BodyType.KinematicBody, PADDLE_FIXTUREDEF);
		paddleBodyLeft.setUserData(PADDLE_LEFT);
		this.mPaddleBodies.put(PADDLE_LEFT.getOwnerID(), paddleBodyLeft);

		final Body paddleBodyRight = PhysicsFactory.createBoxBody(this.mPhysicsWorld, GAME_WIDTH_HALF - PADDLE_WIDTH_HALF, 0, PADDLE_WIDTH, PADDLE_HEIGHT, BodyType.KinematicBody, PADDLE_FIXTUREDEF);
		paddleBodyRight.setUserData(PADDLE_RIGHT);
		this.mPaddleBodies.put(PADDLE_RIGHT.getOwnerID(), paddleBodyRight);

		this.initWalls();
	}

	private void initMessagePool() {
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED, UpdateScoreServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_PONG, UpdateScoreServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_CONNECTION_CLOSE, UpdateScoreServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_UPDATE_SCORE, UpdateScoreServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_UPDATE_BALL, UpdateBallServerMessage.class);
		this.mMessagePool.registerMessage(FLAG_MESSAGE_SERVER_UPDATE_PADDLE, UpdatePaddleServerMessage.class);
	}

	private void initWalls() {
		WALL_FIXTUREDEF.isSensor = true;

		final Body leftBody = PhysicsFactory.createLineBody(this.mPhysicsWorld, -GAME_WIDTH_HALF, -GAME_HEIGHT_HALF, -GAME_WIDTH_HALF, GAME_HEIGHT_HALF, WALL_FIXTUREDEF);
		leftBody.setUserData(this.mPaddleBodies.get(PADDLE_LEFT.getOwnerID()));

		final Body rightBody = PhysicsFactory.createLineBody(this.mPhysicsWorld, GAME_WIDTH_HALF, -GAME_HEIGHT_HALF, GAME_WIDTH_HALF, GAME_HEIGHT_HALF, WALL_FIXTUREDEF);
		rightBody.setUserData(this.mPaddleBodies.get(PADDLE_RIGHT.getOwnerID()));


		WALL_FIXTUREDEF.isSensor = false;

		PhysicsFactory.createLineBody(this.mPhysicsWorld, -GAME_WIDTH_HALF, -GAME_HEIGHT_HALF, GAME_WIDTH_HALF, -GAME_HEIGHT_HALF, WALL_FIXTUREDEF);
		PhysicsFactory.createLineBody(this.mPhysicsWorld, -GAME_WIDTH_HALF, GAME_HEIGHT_HALF, GAME_WIDTH_HALF, GAME_HEIGHT_HALF, WALL_FIXTUREDEF);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void preSolve(final Contact pContact, final Manifold pManifold) {

	}

	@Override
	public void postSolve(final Contact pContact, final ContactImpulse pContactImpulse) {

	}

	@Override
	public void beginContact(final Contact pContact) {
		final Fixture fixtureA = pContact.getFixtureA();
		final Body bodyA = fixtureA.getBody();
		final Object userDataA = bodyA.getUserData();

		final Fixture fixtureB = pContact.getFixtureB();
		final Body bodyB = fixtureB.getBody();
		final Object userDataB = bodyB.getUserData();

		final boolean isScoreSensorA = userDataA != null && userDataA instanceof Body;
		final boolean isScoreSensorB = userDataB != null && userDataB instanceof Body;

		if(isScoreSensorA || isScoreSensorB) {
			this.mResetBall = true;

			final PaddleUserData paddleUserData = (isScoreSensorA) ? (PaddleUserData)(((Body)userDataA).getUserData()) : (PaddleUserData)(((Body)userDataA).getUserData());

			final int opponentID = paddleUserData.getOpponentID();
			final Score opponentPaddleScore = this.mPaddleScores.get(opponentID);
			opponentPaddleScore.increase();

			final UpdateScoreServerMessage updateScoreServerMessage = (UpdateScoreServerMessage)this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_UPDATE_SCORE);
			updateScoreServerMessage.set(opponentID, opponentPaddleScore.getScore());

			final SmartList<SocketConnectionClientConnector> clientConnectors = this.mClientConnectors;
			for(int i = 0; i < clientConnectors.size(); i++) {
				try {
					final ClientConnector<SocketConnection> clientConnector = clientConnectors.get(i);
					clientConnector.sendServerMessage(updateScoreServerMessage);
				} catch (final IOException e) {
					Debug.e(e);
				}
			}
			this.mMessagePool.recycleMessage(updateScoreServerMessage);
		}
	}

	@Override
	public void endContact(final Contact pContact) {

	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		if(this.mResetBall) {
			this.mResetBall = false;
			final Vector2 vector2 = Vector2Pool.obtain(0, 0);
			this.mBallBody.setTransform(vector2, 0);

			vector2.set(MathUtils.randomSign() * MathUtils.random(3, 4), MathUtils.randomSign() * MathUtils.random(3, 4));
			this.mBallBody.setLinearVelocity(vector2);
			Vector2Pool.recycle(vector2);
		}
		this.mPhysicsWorld.onUpdate(pSecondsElapsed);

		/* Prepare UpdateBallServerMessage. */
		final Vector2 ballPosition = this.mBallBody.getPosition();
		final float ballX = ballPosition.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - BALL_RADIUS;
		final float ballY = ballPosition.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - BALL_RADIUS;

		final UpdateBallServerMessage updateBallServerMessage = (UpdateBallServerMessage)this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_UPDATE_BALL);
		updateBallServerMessage.set(ballX, ballY);

		final ArrayList<UpdatePaddleServerMessage> updatePaddleServerMessages = this.mUpdatePaddleServerMessages;

		/* Prepare UpdatePaddleServerMessages. */
		final SparseArray<Body> paddleBodies = this.mPaddleBodies;
		for(int j = 0; j < paddleBodies.size(); j++) {
			final int paddleID = paddleBodies.keyAt(j);
			final Body paddleBody = paddleBodies.get(paddleID);
			final Vector2 paddlePosition = paddleBody.getPosition();

			final float paddleX = paddlePosition.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - PADDLE_WIDTH_HALF;
			final float paddleY = paddlePosition.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT - PADDLE_HEIGHT_HALF;

			final UpdatePaddleServerMessage updatePaddleServerMessage = (UpdatePaddleServerMessage)this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_UPDATE_PADDLE);
			updatePaddleServerMessage.set(paddleID, paddleX, paddleY);

			updatePaddleServerMessages.add(updatePaddleServerMessage);
		}

		try {
			/* Update Ball. */
			this.sendBroadcastServerMessage(updateBallServerMessage);

			/* Update Paddles. */
			for(int j = 0; j < updatePaddleServerMessages.size(); j++) {
				this.sendBroadcastServerMessage(updatePaddleServerMessages.get(j));
			}
			this.sendBroadcastServerMessage(updateBallServerMessage);
		} catch (final IOException e) {
			Debug.e(e);
		}

		/* Recycle messages. */
		this.mMessagePool.recycleMessage(updateBallServerMessage);
		this.mMessagePool.recycleMessages(updatePaddleServerMessages);
		updatePaddleServerMessages.clear();
	}

	@Override
	public void reset() {
		/* Nothing. */
	}

	@Override
	protected SocketConnectionClientConnector newClientConnector(final SocketConnection pSocketConnection) throws IOException {
		final SocketConnectionClientConnector clientConnector = new SocketConnectionClientConnector(pSocketConnection);

		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_MOVE_PADDLE, MovePaddleClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				final MovePaddleClientMessage movePaddleClientMessage = (MovePaddleClientMessage)pClientMessage;
				final Body paddleBody = PongServer.this.mPaddleBodies.get(movePaddleClientMessage.mPaddleID);
				final Vector2 paddlePosition = paddleBody.getTransform().getPosition();
				final float paddleY = MathUtils.bringToBounds(-GAME_HEIGHT_HALF + PADDLE_HEIGHT_HALF, GAME_HEIGHT_HALF - PADDLE_HEIGHT_HALF, movePaddleClientMessage.mY);
				paddlePosition.set(paddlePosition.x, paddleY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				paddleBody.setTransform(paddlePosition, 0);
			}
		});

		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_CONNECTION_CLOSE, ConnectionCloseClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				pClientConnector.terminate();
			}
		});

		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH, ConnectionEstablishClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				final ConnectionEstablishClientMessage connectionEstablishClientMessage = (ConnectionEstablishClientMessage) pClientMessage;
				if(connectionEstablishClientMessage.getProtocolVersion() == MessageConstants.PROTOCOL_VERSION) {
					final ConnectionEstablishedServerMessage connectionEstablishedServerMessage = (ConnectionEstablishedServerMessage) PongServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_CONNECTION_ESTABLISHED);
					try {
						pClientConnector.sendServerMessage(connectionEstablishedServerMessage);
					} catch (IOException e) {
						Debug.e(e);
					}
					PongServer.this.mMessagePool.recycleMessage(connectionEstablishedServerMessage);
				} else {
					final ConnectionRejectedProtocolMissmatchServerMessage connectionRejectedProtocolMissmatchServerMessage = (ConnectionRejectedProtocolMissmatchServerMessage) PongServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_CONNECTION_REJECTED_PROTOCOL_MISSMATCH);
					connectionRejectedProtocolMissmatchServerMessage.setProtocolVersion(MessageConstants.PROTOCOL_VERSION);
					try {
						pClientConnector.sendServerMessage(connectionRejectedProtocolMissmatchServerMessage);
					} catch (IOException e) {
						Debug.e(e);
					}
					PongServer.this.mMessagePool.recycleMessage(connectionRejectedProtocolMissmatchServerMessage);
				}
			}
		});

		clientConnector.registerClientMessage(FLAG_MESSAGE_CLIENT_CONNECTION_PING, ConnectionPingClientMessage.class, new IClientMessageHandler<SocketConnection>() {
			@Override
			public void onHandleMessage(final ClientConnector<SocketConnection> pClientConnector, final IClientMessage pClientMessage) throws IOException {
				final ConnectionPongServerMessage connectionPongServerMessage = (ConnectionPongServerMessage) PongServer.this.mMessagePool.obtainMessage(FLAG_MESSAGE_SERVER_CONNECTION_PONG);
				try {
					pClientConnector.sendServerMessage(connectionPongServerMessage);
				} catch (IOException e) {
					Debug.e(e);
				}
				PongServer.this.mMessagePool.recycleMessage(connectionPongServerMessage);
			}
		});

		clientConnector.sendServerMessage(new SetPaddleIDServerMessage(this.mClientConnectors.size())); // TODO should not be size(), as it only works properly for first two connections!
		return clientConnector;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
