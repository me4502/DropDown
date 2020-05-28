package com.me4502.dropdown.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me4502.dropdown.DropDownGame;
import com.badlogic.gdx.Files.FileType;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Drop Down";
		//cfg.resizable = false;
		cfg.width = 800;
		cfg.height = 600;
		cfg.vSyncEnabled = true;
		//cfg.samples = 16;
		cfg.addIcon("data/icons/ico128.png", FileType.Internal);
		cfg.addIcon("data/icons/ico32.png", FileType.Internal);
		cfg.addIcon("data/icons/ico16.png", FileType.Internal);

		new LwjglApplication(new DropDownGame(), cfg);
	}
}
