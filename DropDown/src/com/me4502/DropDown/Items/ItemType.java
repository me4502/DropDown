package com.me4502.DropDown.Items;

import com.badlogic.gdx.graphics.Texture;
import com.me4502.DropDown.TextureStorage;

public enum ItemType {

	SWORD("swordArm"), CROSSBOW("crossbowArm"), LAMP("lampArm");

	ItemType(String texture) {
		this.texture = texture;
	}

	String texture;

	public String getRawTexture() {
		return texture;
	}

	public Texture getTexture(boolean right) {
		if(right) return TextureStorage.getTexture(texture + "Right");
		else return TextureStorage.getTexture(texture + "Left");
	}

	public Texture getIconTexture() {
		return TextureStorage.getTexture(texture + "Icon");
	}
}