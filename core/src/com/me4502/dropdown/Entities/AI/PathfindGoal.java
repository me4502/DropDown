package com.me4502.dropdown.Entities.AI;

import java.util.Random;

import com.me4502.dropdown.Position;
import com.me4502.dropdown.Entities.Enemy;
import com.me4502.dropdown.Entities.Enemy.Action;
import com.me4502.dropdown.Levels.Level;

public class PathfindGoal extends Goal {

	public PathfindGoal(Enemy entity, Level level) {
		super(entity, level);
	}

	int switchSpeed = 0;

	int jumpTries = 0;
	double jumpTryX = 0;

	@Override
	public void update() {

		if(isFinished()) return;
		if(getEntity().getTarget() == null) return;

		getEntity().movingCooldown --;
		if(getEntity().movingCooldown > 0) return;
		switchSpeed++;

		if(getEntity().getPosition().getX() - getEntity().getTarget().getPosition().getX() < -2 && (getEntity().enemyAction == Action.WALK_RIGHT || switchSpeed > 10)) {
			if(!getEntity().tryMove(new Position(getEntity().getPosition().getX() + 2, getEntity().getPosition().getY())) && getEntity().jumpTimer < -15 && getEntity().collidesWithTiles(true,false,true)) {
				if(getEntity().getPosition().getX() == jumpTryX)
					jumpTries++;
				else
					jumpTries = 1;
				jumpTryX = getEntity().getPosition().getX();
				if(jumpTries > 3) {
					getEntity().setAction(Action.MAN_LEFT);
					switchSpeed = -100;
				} else
					getEntity().jumpTimer = 18;
			}
			if(getEntity().enemyAction != Action.WALK_RIGHT) {
				getEntity().setAction(Action.WALK_RIGHT);
				switchSpeed = 0;
			}
		} else if(getEntity().getPosition().getX() - getEntity().getTarget().getPosition().getX() > 2 && (getEntity().enemyAction == Action.WALK_LEFT || switchSpeed > 10)) {
			if(!getEntity().tryMove(new Position(getEntity().getPosition().getX() - 2, getEntity().getPosition().getY())) && getEntity().jumpTimer < -15 && getEntity().collidesWithTiles(true,false,true)) {
				if(getEntity().getPosition().getX() == jumpTryX)
					jumpTries++;
				else
					jumpTries = 1;
				jumpTryX = getEntity().getPosition().getX();
				if(jumpTries > 3) {
					getEntity().setAction(Action.MAN_RIGHT);
					switchSpeed = -100;
				} else
					getEntity().jumpTimer = 18;
			}
			if(getEntity().enemyAction != Action.WALK_LEFT) {
				getEntity().setAction(Action.WALK_LEFT);
				switchSpeed = 0;
			}
		} else if (getEntity().enemyAction == Action.MAN_LEFT || getEntity().enemyAction == Action.MAN_RIGHT || getEntity().getPosition().getX() - getEntity().getTarget().getPosition().getX() < 2 && getEntity().getPosition().getX() - getEntity().getTarget().getPosition().getX() > -2 && (getEntity().enemyAction == Action.MAN_LEFT || getEntity().enemyAction == Action.MAN_RIGHT || switchSpeed > 10)) {

			Action ac = getEntity().enemyAction;
			if(ac != Action.MAN_LEFT && ac != Action.MAN_RIGHT)
				ac = new Random().nextBoolean() ? Action.MAN_LEFT : Action.MAN_RIGHT;
			if(!getEntity().tryMove(new Position(getEntity().getPosition().getX() + (ac == Action.MAN_RIGHT ? 2 : -2), getEntity().getPosition().getY())) && getEntity().jumpTimer < -15 && getEntity().collidesWithTiles(true,false,true))
				getEntity().jumpTimer = 18;
			if(getEntity().enemyAction != Action.MAN_LEFT && getEntity().enemyAction != Action.MAN_RIGHT) {
				getEntity().setAction(ac);
				switchSpeed = -100;
			}
		}
	}
}