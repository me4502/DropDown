package com.me4502.DropDown;

import java.util.Collection;
import java.util.HashMap;

import com.badlogic.gdx.audio.Sound;

public class SoundStorage {

	private static HashMap<String, Sound> sounds = new HashMap<String, Sound>();

	public static void addSound(String name, Sound sound) {
		sounds.put(name, sound);
	}

	public static Sound getSound(String name) {
		return sounds.get(name);
	}

	public static Collection<Sound> getSounds() {

		return sounds.values();
	}
}