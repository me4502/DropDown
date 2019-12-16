package com.me4502.DropDown;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Drop Down";
		//cfg.resizable = false;
		cfg.useGL20 = true;
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