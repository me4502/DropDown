package com.me4502.DropDown.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me4502.DropDown.DropDownGame;
import com.me4502.DropDown.Position;
import com.me4502.DropDown.TextureStorage;
import com.me4502.DropDown.Levels.Level;

public class Particle extends InsentientEntity {

	private ParticleType type;
	ParticleAnimation animation;

	public Particle(ParticleType type, Level level, Position position) {
		super(level);

		this.type = type;
		this.position = position;

		animation = new ParticleAnimation();
	}

	public ParticleType getType() {

		return type;
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {
		animation.render(batch);
	}

	public class ParticleAnimation {

		float stateTime;

		Animation emberAnimation;
		TextureRegion[] emberFrames;

		TextureRegion currentFrame;

		public ParticleAnimation() {

			Texture emberSheet = TextureStorage.getTexture("embers");
			TextureRegion[][] tmp = TextureRegion.split(emberSheet, emberSheet.getWidth() / 4, emberSheet.getHeight());
			emberFrames = new TextureRegion[4];
			int index = 0;
			for (int j = 0; j < 4; j++) {
				emberFrames[index++] = tmp[0][j];
			}
			emberAnimation = new Animation(0.15f, emberFrames);

			stateTime = 0f;
		}

		public void render(SpriteBatch batch) {
			stateTime += Gdx.graphics.getDeltaTime();
			updateSprite();
			sprite.setOrigin(0f, 0f);
			sprite.setScale(DropDownGame.lev.getBaseYScale());

			//sprite.setScale(level.getBaseXScale(), level.getBaseYScale()/2);
			sprite.setPosition((float)getPosition().getX(), (float)getPosition().getY());

			sprite.draw(batch);
		}

		public void updateSprite() {
			switch(type) {
			case EMBER:
				currentFrame = emberAnimation.getKeyFrame(stateTime, true);
				break;
			default:
				break;
			}
			if(getSprite() == null) setSprite(new Sprite(currentFrame));
			else sprite.setRegion(currentFrame);
		}
	}

	public static enum ParticleType {

		EMBER("embers");

		ParticleType(String texture) {
			this.texture = texture;
		}

		String texture;
		Texture cachedTexture;

		public Texture getTexture() {

			if(cachedTexture != null) return cachedTexture;
			return cachedTexture = TextureStorage.getTexture(texture);
		}
	}
}