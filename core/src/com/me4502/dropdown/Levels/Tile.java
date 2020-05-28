package com.me4502.dropdown.Levels;

import com.badlogic.gdx.Gdx;
import com.me4502.dropdown.DropDownGame;
import com.me4502.dropdown.Entities.Entity;

public class Tile {

	Materials type;

	int x, y;

	boolean placeEnemy, placeAshley;

	boolean ashleyRight;

	public Tile(Materials type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public boolean shouldPlaceAshley() {
		return placeAshley;
	}

	public void setPlaceAshley(boolean placeAshley) {
		this.placeAshley = placeAshley;
	}

	public boolean shouldPlaceEnemy() {
		return placeEnemy;
	}

	public void setPlaceEnemy(boolean placeEnemy) {
		this.placeEnemy = placeEnemy;
	}

	public boolean isAshleyRight() {
		return ashleyRight;
	}

	public void setAshleyRight(boolean ashleyRight) {
		this.ashleyRight = ashleyRight;
	}

	public Materials getType() {

		return type;
	}

	public int getX() {

		return x;
	}

	public int getY() {

		return y;
	}

	public boolean isColliding(Entity ent, boolean feetOnly, boolean allowLeniancy, boolean searchRadius) {
		if(!type.collides()) {
			return false;
		}

		double curX = x*64*DropDownGame.lev.getBaseYScale()-Gdx.graphics.getWidth()/2 - DropDownGame.lev.cameraXOffset;
		double otherX = ent.getMinimum().getX() + ent.getPosition().getX();
		double otherMaxX = otherX + ent.getMaximum().getX();

		double curY = y*64*DropDownGame.lev.getBaseYScale()-Gdx.graphics.getHeight()/2;
		double otherY = ent.getMinimum().getY() + ent.getPosition().getY();
		double otherMaxY = otherY + (feetOnly ? ent.getMaximum().getY()/4 : ent.getMaximum().getY());

		return curX <= otherMaxX - 12 && otherX + 12 <= curX+64*DropDownGame.lev.getBaseYScale() && curY + 20 <= otherMaxY && otherY - (searchRadius ? 1 : 0) + (allowLeniancy ? 5 : 0) <= curY+64*DropDownGame.lev.getBaseYScale();
	}
}