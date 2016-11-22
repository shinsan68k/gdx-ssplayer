package com.shingames.ssplayer.sample.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.shingames.ssplayer.sample.SsSampleScene2d;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.width = 800;
                config.height = 600;
                
//		new LwjglApplication(new SsSampleSprite(), config);
		new LwjglApplication(new SsSampleScene2d(), config);
	}
}
