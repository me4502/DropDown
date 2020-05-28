package com.me4502.dropdown.Entities;

import com.me4502.dropdown.Position;
import com.me4502.dropdown.Levels.Level;

public abstract class LivingEntity extends Entity {

	public int jumpTimer = 0;
	protected int gravity = 0;

	public boolean dead;

	public LivingEntity(Level level) {
		super(level);
	}

	@Override
	public void remove() {

	}

	@Override
	public void update() {

		if(jumpTimer > 0) gravity = 0;
		if(gravity > 6) gravity = 6;
		else gravity ++;
		if(jumpTimer <= 0) {
			if(!tryMove(new Position(getPosition().getX(), getPosition().getY() - gravity)))
				gravity = 0;
		}
		if(jumpTimer > 0)
			tryMove(new Position(getPosition().getX(), getPosition().getY() + jumpTimer--));
		else if (jumpTimer >= -15)
			jumpTimer--;

		timeSinceMove++;
	}
}