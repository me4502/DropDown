package com.me4502.DropDown.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me4502.DropDown.DropDownGame;
import com.me4502.DropDown.Position;
import com.me4502.DropDown.TextureStorage;
import com.me4502.DropDown.Levels.Level;

public class CrossbowBolt extends InsentientEntity {

	Vector2 velocity;

	public CrossbowBolt(Level level, Position position, Vector2 velocity, float rotation) {
		super(level);

		setPosition(position);
		setSprite(new Sprite(TextureStorage.getTexture("crossbowBolt")));

		this.velocity = velocity.nor();

		sprite.setOrigin(0f,2f);
		sprite.setPosition((float)getPosition().getX(), (float)getPosition().getY());
		sprite.setRotation(rotation);

		fadeTimer = 150;
	}

	int fadeTimer;

	@Override
	public void update() {

		fadeTimer--;
		if(fadeTimer < 0) remove = true;

		float x = velocity.x*5;
		//velocity.x *= 0.999;

		float y = velocity.y*5;
		//velocity.y *= 1.03;
		tryMove(new Position(getPosition().getX() + x, getPosition().getY() + y));

		sprite.setPosition((float)getPosition().getX(), (float)getPosition().getY());

		for(Entity ent : level.entities) {
			if(!(ent instanceof Enemy)) continue;
			if(sprite.getBoundingRectangle().overlaps(new Rectangle((float)(ent.getPosition().getX() + ent.getMinimum().getX()), (float)(ent.getPosition().getY() + ent.getMinimum().getY()), (float)ent.getMaximum().getX(), (float)ent.getMaximum().getY()))) {
				Enemy en = (Enemy) ent;
				en.hit(4);
				remove = true;
				break;
			}
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		sprite.setScale(DropDownGame.lev.getBaseYScale());
		sprite.draw(batch);
	}
}