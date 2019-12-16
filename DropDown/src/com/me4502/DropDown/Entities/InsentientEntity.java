package com.me4502.DropDown.Entities;

import com.me4502.DropDown.Levels.Level;

public abstract class InsentientEntity extends Entity {

	public InsentientEntity(Level level) {
		super(level);
	}

	@Override
	public void remove() {
		remove = true;
	}
}