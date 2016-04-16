package test.start.desktop;


import me.pusty.game.main.GameClass;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "LudumDare35";
		config.width = 128*8;
		config.height = 72*8;
		config.resizable = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = 30;
		
		GameClass gameclass = new GameClass();

		new LwjglApplication(gameclass, config);
	}
}
