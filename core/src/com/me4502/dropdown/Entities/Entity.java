package com.me4502.dropdown.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me4502.dropdown.Position;
import com.me4502.dropdown.Levels.Level;
import com.me4502.dropdown.Levels.Tile;

public abstract class Entity {

	Level level;

	Sprite sprite;

	Position position;
	Position minimum,maximum;

	protected int timeSinceMove = 0;

	public boolean remove = false;

	public Entity(Level level) {
		this.level = level;
	}

	public Position getMinimum() {
		if(minimum == null) minimum = new Position(0, 0);
		return minimum;
	}

	public Position getMaximum() {
		if(maximum == null) maximum = new Position(64, 64);
		return maximum;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		minimum = new Position(0, 0);
		maximum = new Position(sprite.getWidth(), sprite.getHeight());
	}

	public abstract void remove();

	public abstract void update();

	public abstract void render(SpriteBatch batch);

	public boolean isColliding(Entity ent) {

		double curX = minimum.getX() + position.getX();
		double otherX = ent.minimum.getX() + ent.getPosition().getX();

		double curY = minimum.getY() + position.getY();
		double otherY = ent.minimum.getY() + ent.getPosition().getY();

		return curX <= otherX + ent.maximum.getX() &&
				otherX <= curX + maximum.getX() &&
				curY <= otherY + ent.maximum.getY() &&
				otherY <= curY + maximum.getY();
	}

	public boolean collidesWithTiles(boolean feetOnly, boolean allowLeniency, boolean searchRadius) {

		boolean collides = false;
		for(Tile[] tiles : level.getBase().getTiles()) {
			for(Tile tile : tiles) {
				if(tile == null) continue;
				if(tile.isColliding(this, feetOnly, allowLeniency, searchRadius)) {
					collides = true;
					break;
				}
			}
		}

		return collides;
	}

	public boolean tryMove(Position position) {
		Position old = getPosition();
		setPosition(position);
		int tsm = timeSinceMove;
		timeSinceMove = 0;
		if(collidesWithTiles(false, false, false)) {
			timeSinceMove = tsm;
			setPosition(old);
			return false;
		} else
			return true;
	}
}