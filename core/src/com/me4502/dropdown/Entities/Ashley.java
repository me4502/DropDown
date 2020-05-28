package com.me4502.dropdown.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me4502.dropdown.DropDownGame;
import com.me4502.dropdown.Position;
import com.me4502.dropdown.TextureStorage;
import com.me4502.dropdown.Levels.Level;

public class Ashley extends LivingEntity {

	Action ashleyAction, def;

	AshleyAnimations animation;

	boolean interacted = false;

	float kissTimeout = 500f;

	public Ashley(Level level, Position position, Action action) {
		super(level);

		setPosition(position);

		def = action;

		animation = new AshleyAnimations();
		setAction(action);
	}

	@Override
	public void update() {

		super.update();
		if(isKissing()) {
			if(kissTimeout < 0f) setAction(ashleyAction == Action.KISS_LEFT ? Action.STILL_LEFT : Action.STILL_RIGHT);
			if(kissTimeout >= 0)
				kissTimeout -= 1f;
		}

		if(!interacted && getSprite().getBoundingRectangle().overlaps(level.player.getSprite().getBoundingRectangle())) {
			interacted = true;
			level.base.getDialogueLines().add("Thank god you found me, Jeremy!|I love you");
			level.base.getDialogueLines().add("You deserve a reward for your hard efforts ;)");
		}

		if(interacted && level.readingLine > level.base.getDialogueLines().size()-1 && !isKissing() && !level.startedReading && kissTimeout > 0f)
			setAction(ashleyAction == Action.STILL_LEFT ? Action.KISS_LEFT : Action.KISS_RIGHT);
	}

	public boolean isKissing() {

		return ashleyAction == Action.KISS_LEFT || ashleyAction == Action.KISS_RIGHT;
	}

	@Override
	public void render(SpriteBatch batch) {
		animation.render(batch);
	}

	public class AshleyAnimations {

		float stateTime;

		Animation ashleyAnimationRight;
		TextureRegion[] ashleyFramesRight;
		Animation ashleyAnimationLeft;
		TextureRegion[] ashleyFramesLeft;

		Animation ashleyKissAnimationLeft;
		TextureRegion[] ashleyKissFramesLeft;
		Animation ashleyKissAnimationRight;
		TextureRegion[] ashleyKissFramesRight;

		TextureRegion currentFrame;

		public AshleyAnimations() {

			Texture swordSheetRight = TextureStorage.getTexture("ashleyRight");
			TextureRegion[][] tmp = TextureRegion.split(swordSheetRight, swordSheetRight.getWidth() / 2, swordSheetRight.getHeight());
			ashleyFramesRight = new TextureRegion[2];
			int index = 0;
			for (int j = 0; j < 2; j++) {
				ashleyFramesRight[index++] = tmp[0][j];
			}
			ashleyAnimationRight = new Animation(1f, ashleyFramesRight);

			Texture swordSheetLeft = TextureStorage.getTexture("ashleyLeft");
			tmp = TextureRegion.split(swordSheetLeft, swordSheetLeft.getWidth() / 2, swordSheetLeft.getHeight());
			ashleyFramesLeft = new TextureRegion[2];
			index = 0;
			for (int j = 0; j < 2; j++) {
				ashleyFramesLeft[index++] = tmp[0][j];
			}
			ashleyAnimationLeft = new Animation(1f, ashleyFramesLeft);

			Texture kissSheet = TextureStorage.getTexture("kissSceneLeft");
			tmp = TextureRegion.split(kissSheet, kissSheet.getWidth() / 3, kissSheet.getHeight());
			ashleyKissFramesLeft = new TextureRegion[3];
			index = 0;
			for (int j = 0; j < 3; j++) {
				ashleyKissFramesLeft[index++] = tmp[0][j];
			}
			ashleyKissAnimationLeft = new Animation(0.6f, ashleyKissFramesLeft);

			kissSheet = TextureStorage.getTexture("kissSceneRight");
			tmp = TextureRegion.split(kissSheet, kissSheet.getWidth() / 3, kissSheet.getHeight());
			ashleyKissFramesRight = new TextureRegion[3];
			index = 0;
			for (int j = 0; j < 3; j++) {
				ashleyKissFramesRight[index++] = tmp[0][j];
			}
			ashleyKissAnimationRight = new Animation(0.6f, ashleyKissFramesRight);

			stateTime = 0f;
		}

		public void render(SpriteBatch batch) {
			stateTime += Gdx.graphics.getDeltaTime();
			updateSprite();

			sprite.setScale(DropDownGame.lev.getBaseYScale());

			sprite.setPosition((float)getPosition().getX(), (float)getPosition().getY());

			sprite.draw(batch, kissTimeout < 255 ? kissTimeout / 255f : 1.0f);
		}

		public void updateSprite() {
			switch(ashleyAction) {
			case STILL_LEFT:
				currentFrame = ashleyAnimationLeft.getKeyFrame(stateTime, true);
				break;
			case STILL_RIGHT:
				currentFrame = ashleyAnimationRight.getKeyFrame(stateTime, true);
				break;
			case KISS_LEFT:
				currentFrame = ashleyKissAnimationLeft.getKeyFrame(stateTime, false);
				break;
			case KISS_RIGHT:
				currentFrame = ashleyKissAnimationRight.getKeyFrame(stateTime, false);
				break;
			default:
				break;
			}
			if(getSprite() == null || hasChanged) setSprite(new Sprite(currentFrame));
			else sprite.setRegion(currentFrame);
		}
	}

	public void setAction(Action ashleyAction) {
		this.ashleyAction = ashleyAction;
		hasChanged = true;
		animation.stateTime = 0f;
	}

	boolean hasChanged = false;

	public enum Action {
		STILL_LEFT,STILL_RIGHT,KISS_LEFT,KISS_RIGHT;
	}
}