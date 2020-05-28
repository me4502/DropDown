package com.me4502.dropdown.Entities;

import com.me4502.dropdown.Levels.Level;

public abstract class InsentientEntity extends Entity {

	public InsentientEntity(Level level) {
		super(level);
	}

	@Override
	public void remove() {
		remove = true;
	}
}