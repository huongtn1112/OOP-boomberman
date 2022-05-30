package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		final int WIDTH = 420;
		final int HEIGHT = 700;
		final String TITLE = "Boom";
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WIDTH;
		config.height = HEIGHT;
		config.title = TITLE;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;

		new LwjglApplication(new Main(), config);
	}
}
