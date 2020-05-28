package com.me4502.dropdown.Levels;

import com.badlogic.gdx.graphics.Texture;
import com.me4502.dropdown.TextureStorage;

public enum Materials {

	RED_BRICK("redBrick"), RED_WALL("redWall"), PURPLE_BRICK("purpleBrick"), PURPLE_WALL("purpleWall"), BLUE_BRICK("blueBrick"), BLUE_WALL("blueWall"),
	GREEN_BRICK("greenBrick"), GREEN_WALL("greenWall"), YELLOW_BRICK("yellowBrick"), YELLOW_WALL("yellowWall"), ORANGE_BRICK("orangeBrick"),
	ORANGE_WALL("orangeWall"), TORCH("torch"), CRACK("crack"), BARS("ironBars"), BARRICADE("barricade");

	Materials(String textureName) {
		this.textureName = textureName;
	}

	String textureName;
	Texture cachedTexture;

	public Texture getTexture() {
		if(cachedTexture != null) return cachedTexture;
		return cachedTexture = TextureStorage.getTexture(textureName);
	}

	public static Materials fromString(String string) {
		for(Materials material : values())
			if(string.equalsIgnoreCase(material.textureName))
				return material;
		return valueOf(string);
	}

	public boolean collides() {
		switch(this) {
		case BARS:
		case BLUE_WALL:
		case CRACK:
		case GREEN_WALL:
		case ORANGE_WALL:
		case PURPLE_WALL:
		case RED_WALL:
		case TORCH:
		case YELLOW_WALL:
			return false;
		case YELLOW_BRICK:
		case RED_BRICK:
		case ORANGE_BRICK:
		case PURPLE_BRICK:
		case GREEN_BRICK:
		case BLUE_BRICK:
		case BARRICADE:
			return true;
		default:
			return true;
		}
	}
}