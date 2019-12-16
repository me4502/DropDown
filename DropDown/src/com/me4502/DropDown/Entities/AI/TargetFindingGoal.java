package com.me4502.DropDown.Entities.AI;

import com.me4502.DropDown.Entities.Enemy;
import com.me4502.DropDown.Levels.Level;

public class TargetFindingGoal extends Goal {

	public TargetFindingGoal(Enemy entity, Level level) {
		super(entity, level);
	}

	@Override
	public void update() {
		if(isFinished()) return;
		if(getLevel().player.getPosition().getDistance(getEntity().getPosition()) > (getLevel().player.isSneaking() ? 2 : 7)*64*getLevel().getBaseYScale()) return;
		getEntity().setTarget(getLevel().player);
		setFinished(true);
	}
}