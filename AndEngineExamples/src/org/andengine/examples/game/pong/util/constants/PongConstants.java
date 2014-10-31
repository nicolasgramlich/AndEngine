package org.andengine.examples.game.pong.util.constants;

import org.andengine.examples.game.pong.adt.PaddleUserData;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:49:20 - 28.02.2011
 */
public interface PongConstants {
	// ===========================================================
	// Final Fields
	// ===========================================================

	public static final int FPS = 30;

	public static final int GAME_WIDTH = 720;
	public static final int GAME_WIDTH_HALF = GAME_WIDTH / 2;
	public static final int GAME_HEIGHT = 480;
	public static final int GAME_HEIGHT_HALF = GAME_HEIGHT / 2;

	public static final int PADDLE_WIDTH = 20;
	public static final int PADDLE_WIDTH_HALF = PADDLE_WIDTH / 2;
	public static final int PADDLE_HEIGHT = 80;
	public static final int PADDLE_HEIGHT_HALF = PADDLE_HEIGHT / 2;

	public static final int BALL_WIDTH = 10;
	public static final int BALL_HEIGHT = 10;
	public static final int BALL_RADIUS = BALL_WIDTH / 2;

	public static final int SCORE_PADDING = 5;

	public static final PaddleUserData PADDLE_LEFT = new PaddleUserData(0, 1);
	public static final PaddleUserData PADDLE_RIGHT = new PaddleUserData(1, 0);

	
	public static final int SERVER_PORT = 4444;

	/* Server --> Client */
	public static final short FLAG_MESSAGE_SERVER_SET_PADDLEID = 1;
	public static final short FLAG_MESSAGE_SERVER_UPDATE_SCORE = FLAG_MESSAGE_SERVER_SET_PADDLEID + 1;
	public static final short FLAG_MESSAGE_SERVER_UPDATE_BALL = FLAG_MESSAGE_SERVER_UPDATE_SCORE + 1;
	public static final short FLAG_MESSAGE_SERVER_UPDATE_PADDLE = FLAG_MESSAGE_SERVER_UPDATE_BALL + 1;

	/* Client --> Server */
	public static final short FLAG_MESSAGE_CLIENT_MOVE_PADDLE = 1;

	// ===========================================================
	// Methods
	// ===========================================================
}
