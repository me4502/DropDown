package com.me4502.dropdown.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me4502.dropdown.DropDownGame;
import com.me4502.dropdown.Position;
import com.me4502.dropdown.SoundStorage;
import com.me4502.dropdown.TextureStorage;
import com.me4502.dropdown.Entities.AI.Goal;
import com.me4502.dropdown.Entities.AI.PathfindGoal;
import com.me4502.dropdown.Entities.AI.TargetFindingGoal;
import com.me4502.dropdown.Levels.Level;

public class Enemy extends LivingEntity {

	public Action enemyAction;

	EnemyAnimation animation;

	Entity target;

	Goal currentGoal;

	public int health;
	public int movingCooldown;

	public Enemy(Level level, Position position) {
		super(level);

		health = 20;

		setPosition(position);

		setAction(Action.STILL_LEFT);
		animation = new EnemyAnimation();
		currentGoal = new TargetFindingGoal(this, level);
	}

	public Goal getGoal() {
		return currentGoal;
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	@Override
	public void render(SpriteBatch batch) {
		animation.render(batch);
	}

	public void hit(int amount) {
		jumpTimer = 5;
		movingCooldown = 40;
		health -= amount;
		if(target == null) target = level.player;
		if(health <= 0 && !dead) {
			SoundStorage.getSound("boarDeath").play();
			dead = true;
		}
		if(getPosition().getY() <= -Gdx.graphics.getHeight() / 2) remove = true;
		if(getPosition().getY() >= Gdx.graphics.getHeight() / 2) remove = true;
	}

	@Override
	public void update() {

		super.update();

		if(enemyAction == Action.WALK_LEFT && timeSinceMove > 10)
			setAction(Action.STILL_LEFT);
		if(enemyAction == Action.WALK_RIGHT && timeSinceMove > 10)
			setAction(Action.STILL_RIGHT);

		if(currentGoal != null) {
			if(currentGoal.isFinished()) {
				if(target != null)
					currentGoal = new PathfindGoal(this, level);
				else
					currentGoal = new TargetFindingGoal(this, level);
			}
			currentGoal.update();
		}
	}

	public class EnemyAnimation {

		float stateTime;

		Animation<TextureRegion> walkAnimationLeft;
		TextureRegion[] walkFramesLeft;
		Animation<TextureRegion> walkAnimationRight;
		TextureRegion[] walkFramesRight;

		Animation<TextureRegion> stillAnimationLeft;
		TextureRegion[] stillFramesLeft;
		Animation<TextureRegion> stillAnimationRight;
		TextureRegion[] stillFramesRight;

		TextureRegion currentFrame;

		public EnemyAnimation() {
			Texture walkSheetRight = TextureStorage.getTexture("enemyRight");
			TextureRegion[][] tmp = TextureRegion.split(walkSheetRight, walkSheetRight.getWidth() / 4, walkSheetRight.getHeight());
			walkFramesRight = new TextureRegion[4];
			int index = 0;
			for (int j = 0; j < 4; j++) {
				walkFramesRight[index++] = tmp[0][j];
			}
			walkAnimationRight = new Animation<>(0.15f, walkFramesRight);

			Texture walkSheetLeft = TextureStorage.getTexture("enemyLeft");
			tmp = TextureRegion.split(walkSheetLeft, walkSheetLeft.getWidth() / 4, walkSheetLeft.getHeight());
			walkFramesLeft = new TextureRegion[4];
			index = 0;
			for (int j = 0; j < 4; j++) {
				walkFramesLeft[index++] = tmp[0][j];
			}
			walkAnimationLeft = new Animation<>(0.15f, walkFramesLeft);

			Texture stillSheetLeft = TextureStorage.getTexture("enemyLeftStill");
			tmp = TextureRegion.split(stillSheetLeft, stillSheetLeft.getWidth(), stillSheetLeft.getHeight());
			stillFramesLeft = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				stillFramesLeft[index++] = tmp[0][j];
			}
			stillAnimationLeft = new Animation<>(0.15f, stillFramesLeft);

			Texture stillSheetRight = TextureStorage.getTexture("enemyRightStill");
			tmp = TextureRegion.split(stillSheetRight, stillSheetRight.getWidth(), stillSheetRight.getHeight());
			stillFramesRight = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				stillFramesRight[index++] = tmp[0][j];
			}
			stillAnimationRight = new Animation<>(0.15f, stillFramesRight);
		}

		public void render(SpriteBatch batch) {
			stateTime += Gdx.graphics.getDeltaTime();
			updateSprite();
			sprite.setOrigin(0f, 0f);

			//sprite.setScale(level.getBaseXScale(), level.getBaseYScale()/2);
			sprite.setPosition((float)getPosition().getX(), (float)getPosition().getY());

			sprite.draw(batch);
		}

		public void updateSprite() {
			switch(enemyAction) {
			case WALK_LEFT:
			case MAN_LEFT:
				currentFrame = walkAnimationLeft.getKeyFrame(stateTime, true);
				break;
			case WALK_RIGHT:
			case MAN_RIGHT:
				currentFrame = walkAnimationRight.getKeyFrame(stateTime, true);
				break;
			case STILL_LEFT:
				currentFrame = stillAnimationLeft.getKeyFrame(stateTime, true);
				break;
			case STILL_RIGHT:
				currentFrame = stillAnimationRight.getKeyFrame(stateTime, true);
				break;
			default:
				break;
			}
			if(getSprite() == null || hasChanged) setSprite(new Sprite(currentFrame));
			else sprite.setRegion(currentFrame);
		}
	}

	public void setAction(Action enemyAction) {
		this.enemyAction = enemyAction;
		hasChanged = true;
	}

	boolean hasChanged = false;

	public enum Action {
		WALK_LEFT, WALK_RIGHT, MAN_LEFT, MAN_RIGHT, STILL_LEFT, STILL_RIGHT;
	}
}