package com.me4502.dropdown;

import java.util.Collection;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;

public class TextureStorage {

	private static final HashMap<String, Texture> textures = new HashMap<>();

	public static void addTexture(String name, Texture texture) {
		textures.put(name, texture);
	}

	public static Texture getTexture(String name) {
		return textures.get(name);
	}

	public static Collection<Texture> getTextures() {
		return textures.values();
	}
}
