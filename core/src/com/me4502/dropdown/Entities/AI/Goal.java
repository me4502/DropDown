package com.me4502.dropdown.Entities.AI;

import com.me4502.dropdown.Entities.Enemy;
import com.me4502.dropdown.Levels.Level;

public abstract class Goal {

	private final Enemy entity;
	private final Level level;

	private boolean finished = false;

	public Goal(Enemy entity, Level level) {
		this.entity = entity;
		this.level = level;
	}

	public Enemy getEntity() {
		return entity;
	}

	public Level getLevel() {
		return level;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public abstract void update();
}