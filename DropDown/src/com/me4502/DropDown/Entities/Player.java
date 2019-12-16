package com.me4502.DropDown.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me4502.DropDown.DropDownGame;
import com.me4502.DropDown.Position;
import com.me4502.DropDown.SoundStorage;
import com.me4502.DropDown.TextureStorage;
import com.me4502.DropDown.Items.CrossBow;
import com.me4502.DropDown.Items.Item;
import com.me4502.DropDown.Items.ItemType;
import com.me4502.DropDown.Items.Lamp;
import com.me4502.DropDown.Items.Sword;
import com.me4502.DropDown.Levels.Level;

public class Player extends LivingEntity {

	PlayerAnimation animation;
	ArmAnimation armAnimation;
	Action playerAction;

	public Item[] items;

	public int currentSlot = 0;

	Sprite armSprite;

	public Player(Level level) {
		super(level);

		items = new Item[3];

		items[0] = new Sword(ItemType.SWORD, (byte) 10);
		items[1] = new CrossBow(ItemType.CROSSBOW, (byte) 10);
		items[2] = new Lamp(ItemType.LAMP, (byte) 7);

		level.cameraXOffset = 0;
		animation = new PlayerAnimation();
		setPosition(new Position(level.getBase().getSpawnPoint().getX() * 64 * level.getBaseXScale()-Gdx.graphics.getWidth()/2, level.getBase().getSpawnPoint().getY() * 64 * level.getBaseYScale()-Gdx.graphics.getHeight()/2));
		setAction(Action.STILL_RIGHT);

		switchSlot(0);

		armAnimation = new ArmAnimation();
	}

	public Sprite getArmSprite() {

		return armSprite;
	}

	public ItemType getHeldItemType() {

		return items[currentSlot].getType();
	}

	public void switchSlot(int slot) {

		if(items[slot].getDurability() <= 0) return;

		currentSlot = slot;
		armSprite = new Sprite(items[currentSlot].getType().getTexture(isRight()));
		updateArm();
	}

	public Texture getHeldTexture() {

		if(items[currentSlot].getDurability() <= 0) TextureStorage.getTexture("rightArm" + (isRight() ? "Right" : "Left"));

		return items[currentSlot].getType().getTexture(isRight());
	}

	int timeout = 0;
	int damageTimeout = 0;

	public void useWeapon() {

		if(level.ashley != null && level.ashley.isKissing()) return;
		if(isSneaking() || items[currentSlot].getDurability() <= 0) return;
		hasChanged = true;
		switch(getHeldItemType()) {
		case CROSSBOW:
			if(!(timeout > 20)) break;
			timeout = 0;
			Position shoulderPos = new Position(armSprite.getX() + armSprite.getOriginX(), position.getY() + armSprite.getOriginY() + 10);
			double rad = Math.toRadians(getArmDirection()-10);

			double xOff = 4*Math.cos(rad);
			double yOff = 4*Math.sin(rad);

			Vector2 vel = new Vector2((float)xOff, (float)yOff);
			level.entities.add(new CrossbowBolt(level, shoulderPos, vel, getArmDirection()-10));
			SoundStorage.getSound("crossbowShot").play();
			/*Position pos = new Position(sprite.getX() + sprite.getWidth()/2, sprite.getY()+sprite.getHeight()/2);
			float rad = (float) (getArmDirection() * Math.PI / 180f);
			rad += 1.2007963267972;
			Vector2 vel = new Vector2((float)(pos.getX() * Math.sin(rad)), (float)(pos.getY() * Math.cos(rad)));
			level.entities.add(new CrossbowBolt(level, pos, vel, getArmDirection()-10));*/
			break;
		case LAMP:
			break;
		case SWORD:
			SoundStorage.getSound("swordStab").play(0.3f);
			armAnimation.weaponUseState = 15;
			break;
		default:
			break;
		}
	}

	@Override
	public void update() {

		if(level.ashley != null && level.ashley.isKissing()) return;

		super.update();

		timeout++;
		lastHit++;
		damageTimeout++;

		if(getPosition().getY() < -Gdx.graphics.getHeight()) level.next();

		if(playerAction == Action.WALK_LEFT && timeSinceMove > 10)
			setAction(Action.STILL_LEFT);
		if(playerAction == Action.WALK_RIGHT && timeSinceMove > 10)
			setAction(Action.STILL_RIGHT);

		if(playerAction == Action.SNEAK_LEFT && timeSinceMove > 10)
			setAction(Action.SNEAK_STILL_LEFT);
		if(playerAction == Action.SNEAK_RIGHT && timeSinceMove > 10)
			setAction(Action.SNEAK_STILL_RIGHT);

		for(int i = 0; i < items.length; i++) {
			if(Gdx.input.isKeyPressed(Keys.valueOf(String.valueOf(i+1)))) {
				if(items[i].getDurability() > 0)
					switchSlot(i);
				break;
			}
		}

		if(Gdx.input.isKeyPressed(Keys.A) || DropDownGame.moveLeft) {
			if(Gdx.input.isKeyPressed(Keys.S) || DropDownGame.moveCrouch) {
				tryMove(new Position(getPosition().getX()-1, getPosition().getY()));
				setAction(Action.SNEAK_LEFT);
			} else {
				if(isSneaking()) {
					setAction(Action.WALK_LEFT);
					animation.updateSprite();
					if(tryMove(new Position(getPosition().getX()-1, getPosition().getY())))
						setAction(Action.WALK_LEFT);
					else {
						setAction(Action.SNEAK_LEFT);
						animation.updateSprite();
						tryMove(new Position(getPosition().getX()-1, getPosition().getY()));
					}
				} else {
					tryMove(new Position(getPosition().getX()-3, getPosition().getY()));
					setAction(Action.WALK_LEFT);
				}
			}
			timeSinceMove = 0;
		}
		if(Gdx.input.isKeyPressed(Keys.D) || DropDownGame.moveRight) {
			if(Gdx.input.isKeyPressed(Keys.S) || DropDownGame.moveCrouch) {
				tryMove(new Position(getPosition().getX()+1, getPosition().getY()));
				setAction(Action.SNEAK_RIGHT);
			} else {
				if(isSneaking()) {
					setAction(Action.WALK_RIGHT);
					animation.updateSprite();
					if(tryMove(new Position(getPosition().getX()+1, getPosition().getY())))
						setAction(Action.WALK_RIGHT);
					else {
						setAction(Action.SNEAK_RIGHT);
						animation.updateSprite();
						tryMove(new Position(getPosition().getX()+1, getPosition().getY()));
					}
				} else {
					tryMove(new Position(getPosition().getX()+3, getPosition().getY()));
					setAction(Action.WALK_RIGHT);
				}
			}
			timeSinceMove = 0;
		}
		if((Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.SPACE) || DropDownGame.moveJump) && jumpTimer <= -15 && collidesWithTiles(true,false,true))
			jumpTimer = isSneaking() ? 18 : 15;

		if(lastHit > 20 || damageTimeout > 60) {
			for(Entity ent : level.entities) {
				if(!(ent instanceof Enemy)) continue;
				if(lastHit > 20 && getHeldItemType() == ItemType.SWORD && armAnimation.weaponUseState > 0 && armSprite.getBoundingRectangle().overlaps(new Rectangle((float)(ent.getPosition().getX() + ent.getMinimum().getX()), (float)(ent.getPosition().getY() + ent.getMinimum().getY()), (float)ent.getMaximum().getX(), (float)ent.getMaximum().getY()))) {
					Enemy en = (Enemy) ent;
					en.hit(5);
					lastHit = 0;
					break;
				} else if (lastHit > 20 && damageTimeout > 60 && sprite.getBoundingRectangle().overlaps(new Rectangle((float)(ent.getPosition().getX() + ent.getMinimum().getX()), (float)(ent.getPosition().getY() + ent.getMinimum().getY()), (float)ent.getMaximum().getX(), (float)ent.getMaximum().getY()))) {
					items[currentSlot].damage((byte) 1);
					if(items[currentSlot].getDurability() <= 0) {
						do {
							boolean foundUnbroken = false;
							for(int i = 0; i < items.length; i++)
								if(items[i].getDurability() > 0)
									foundUnbroken = true;
							if(!foundUnbroken) {
								level.isGameOver = true;
								level.base.getDialogueLines().add("NO! JEREMY!!!!");
								level.startedReading = true;
								break;
							}
							currentSlot += 1;
							while(currentSlot >= items.length)
								currentSlot -= items.length;
							while(currentSlot < 0)
								currentSlot += items.length;
						} while(items[currentSlot].getDurability() <= 0);
					}
					damageTimeout = 0;
					break;
				}
			}
		}
	}

	int lastHit = 0;

	public boolean isSneaking() {

		return playerAction == Action.SNEAK_LEFT || playerAction == Action.SNEAK_RIGHT || playerAction == Action.SNEAK_STILL_LEFT || playerAction == Action.SNEAK_STILL_RIGHT;
	}

	public boolean isRight() {

		return playerAction == Action.STILL_RIGHT || playerAction == Action.WALK_RIGHT || playerAction == Action.SNEAK_RIGHT || playerAction == Action.SNEAK_STILL_RIGHT;
	}

	public float getArmDirection() {
		float mx = Gdx.input.getX() - Gdx.graphics.getWidth()/2;

		float my = -Gdx.input.getY() + Gdx.graphics.getHeight()/2;

		return (float) (Math.atan2 (mx - armSprite.getX() - (isRight() ? 0 : armSprite.getWidth()), -(my - armSprite.getY()))*180.0d/Math.PI-90.0f);
	}

	@Override
	public void render(SpriteBatch batch) {

		if(level.ashley != null && level.ashley.isKissing()) return;
		if(isRight())
			animation.render(batch);

		if(!isSneaking())
			armAnimation.render(batch);

		if(!isRight())
			animation.render(batch);
	}

	public void updateArm() {
		if(isRight()) {
			armSprite.setOrigin(38f, armSprite.getHeight()-2f);
			armSprite.setPosition((float)getPosition().getX()-15, (float)getPosition().getY()+28);
		} else {
			if(getHeldItemType() == ItemType.SWORD && armAnimation.weaponUseState > 0)
				armSprite.setPosition((float)getPosition().getX()-80, (float)getPosition().getY()+26);
			else
				armSprite.setPosition((float)getPosition().getX()-40, (float)getPosition().getY()+26);
			armSprite.setOrigin(armSprite.getWidth() - 38f, armSprite.getHeight()-2f);
			armSprite.setScale(0.9f, 0.9f);
		}

		armSprite.setRotation(getArmDirection() - (isRight() ? 0 : 180));
	}

	public class ArmAnimation {

		int weaponUseState = 0; //Used for other weapon use animations.

		float stateTime;

		Animation swordAnimationLeft;
		TextureRegion[] swordFramesLeft;
		Animation swordAnimationRight;
		TextureRegion[] swordFramesRight;

		Animation swordAnimationStabLeft;
		TextureRegion[] swordFramesStabLeft;
		Animation swordAnimationStabRight;
		TextureRegion[] swordFramesStabRight;

		Animation crossbowAnimationLeft;
		TextureRegion[] crossbowFramesLeft;
		Animation crossbowAnimationRight;
		TextureRegion[] crossbowFramesRight;

		Animation lampAnimationRight;
		TextureRegion[] lampFramesRight;
		Animation lampAnimationLeft;
		TextureRegion[] lampFramesLeft;

		Animation armAnimationRight;
		TextureRegion[] armFramesRight;
		Animation armAnimationLeft;
		TextureRegion[] armFramesLeft;

		TextureRegion currentFrame;

		public ArmAnimation() {
			Texture swordSheetRight = TextureStorage.getTexture("swordArmRight");
			TextureRegion[][] tmp = TextureRegion.split(swordSheetRight, swordSheetRight.getWidth() / 1, swordSheetRight.getHeight());
			swordFramesRight = new TextureRegion[1];
			int index = 0;
			for (int j = 0; j < 1; j++) {
				swordFramesRight[index++] = tmp[0][j];
			}
			swordAnimationRight = new Animation(1f, swordFramesRight);

			Texture swordSheetLeft = TextureStorage.getTexture("swordArmLeft");
			tmp = TextureRegion.split(swordSheetLeft, swordSheetLeft.getWidth() / 1, swordSheetLeft.getHeight());
			swordFramesLeft = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				swordFramesLeft[index++] = tmp[0][j];
			}
			swordAnimationLeft = new Animation(1f, swordFramesLeft);

			Texture swordSheetStabRight = TextureStorage.getTexture("swordArmStabRight");
			tmp = TextureRegion.split(swordSheetStabRight, swordSheetStabRight.getWidth() / 2, swordSheetStabRight.getHeight());
			swordFramesStabRight = new TextureRegion[2];
			index = 0;
			for (int j = 0; j < 2; j++) {
				swordFramesStabRight[index++] = tmp[0][j];
			}
			swordAnimationStabRight = new Animation(0.15f, swordFramesStabRight);

			Texture swordSheetStabLeft = TextureStorage.getTexture("swordArmStabLeft");
			tmp = TextureRegion.split(swordSheetStabLeft, swordSheetStabLeft.getWidth() / 2, swordSheetStabLeft.getHeight());
			swordFramesStabLeft = new TextureRegion[2];
			index = 0;
			for (int j = 0; j < 2; j++) {
				swordFramesStabLeft[index++] = tmp[0][j];
			}
			swordAnimationStabLeft = new Animation(0.15f, swordFramesStabLeft);

			Texture crossbowSheetRight = TextureStorage.getTexture("crossbowArmRight");
			tmp = TextureRegion.split(crossbowSheetRight, crossbowSheetRight.getWidth() / 1, crossbowSheetRight.getHeight());
			crossbowFramesRight = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				crossbowFramesRight[index++] = tmp[0][j];
			}
			crossbowAnimationRight = new Animation(1f, crossbowFramesRight);

			Texture crossbowSheetLeft = TextureStorage.getTexture("crossbowArmLeft");
			tmp = TextureRegion.split(crossbowSheetLeft, crossbowSheetLeft.getWidth() / 1, crossbowSheetLeft.getHeight());
			crossbowFramesLeft = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				crossbowFramesLeft[index++] = tmp[0][j];
			}
			crossbowAnimationLeft = new Animation(1f, crossbowFramesLeft);

			Texture lampSheetRight = TextureStorage.getTexture("lampArmRight");
			tmp = TextureRegion.split(lampSheetRight, lampSheetRight.getWidth() / 1, lampSheetRight.getHeight());
			lampFramesRight = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				lampFramesRight[index++] = tmp[0][j];
			}
			lampAnimationRight = new Animation(1f, lampFramesRight);

			Texture crouchSheetLeft = TextureStorage.getTexture("lampArmLeft");
			tmp = TextureRegion.split(crouchSheetLeft, crouchSheetLeft.getWidth() / 1, crouchSheetLeft.getHeight());
			lampFramesLeft = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				lampFramesLeft[index++] = tmp[0][j];
			}
			lampAnimationLeft = new Animation(1f, lampFramesLeft);

			Texture armSheetRight = TextureStorage.getTexture("armArmRight");
			tmp = TextureRegion.split(armSheetRight, armSheetRight.getWidth() / 1, armSheetRight.getHeight());
			armFramesRight = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				armFramesRight[index++] = tmp[0][j];
			}
			armAnimationRight = new Animation(1f, armFramesRight);

			Texture armSheetLeft = TextureStorage.getTexture("armArmLeft");
			tmp = TextureRegion.split(armSheetLeft, armSheetLeft.getWidth() / 1, armSheetLeft.getHeight());
			armFramesLeft = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				armFramesLeft[index++] = tmp[0][j];
			}
			armAnimationLeft = new Animation(1f, armFramesLeft);
			stateTime = 0f;
		}

		public void render(SpriteBatch batch) {
			weaponUseState--;
			stateTime += Gdx.graphics.getDeltaTime();
			updateSprite();
			armSprite.draw(batch);
		}

		public void updateSprite() {
			if(items[currentSlot].getDurability() > 0) {
				switch(getHeldItemType()) {
				case CROSSBOW:
					currentFrame = (isRight() ? crossbowAnimationRight : crossbowAnimationLeft).getKeyFrame(stateTime, true);
					break;
				case LAMP:
					currentFrame = (isRight() ? lampAnimationRight : lampAnimationLeft).getKeyFrame(stateTime, true);
					break;
				case SWORD:
					currentFrame = (weaponUseState > 0 ? isRight() ? swordAnimationStabRight : swordAnimationStabLeft : isRight() ? swordAnimationRight : swordAnimationLeft).getKeyFrame(stateTime, true);
					break;
				default:
					break;
				}
			} else
				currentFrame = (isRight() ? armAnimationRight : armAnimationLeft).getKeyFrame(stateTime, true);
			if(armSprite == null || hasChanged) armSprite = new Sprite(currentFrame);
			else armSprite.setRegion(currentFrame);
			updateArm();
		}
	}

	public class PlayerAnimation {

		float stateTime;

		Animation stillAnimationLeft;
		TextureRegion[] stillFramesLeft;
		Animation stillAnimationRight;
		TextureRegion[] stillFramesRight;

		Animation walkAnimationLeft;
		TextureRegion[] walkFramesLeft;
		Animation walkAnimationRight;
		TextureRegion[] walkFramesRight;

		Animation crouchAnimationRight;
		TextureRegion[] crouchFramesRight;
		Animation crouchAnimationLeft;
		TextureRegion[] crouchFramesLeft;

		Animation crouchStillAnimationRight;
		TextureRegion[] crouchStillFramesRight;
		Animation crouchStillAnimationLeft;
		TextureRegion[] crouchStillFramesLeft;

		TextureRegion currentFrame;

		public PlayerAnimation() {
			Texture walkSheetRight = TextureStorage.getTexture("playerRight");
			TextureRegion[][] tmp = TextureRegion.split(walkSheetRight, walkSheetRight.getWidth() / 8, walkSheetRight.getHeight());
			walkFramesRight = new TextureRegion[8];
			int index = 0;
			for (int j = 0; j < 8; j++) {
				walkFramesRight[index++] = tmp[0][j];
			}
			walkAnimationRight = new Animation(0.15f, walkFramesRight);

			Texture stillSheetRight = TextureStorage.getTexture("playerRightStill");
			tmp = TextureRegion.split(stillSheetRight, stillSheetRight.getWidth() / 2, stillSheetRight.getHeight());
			stillFramesRight = new TextureRegion[2];
			index = 0;
			for (int j = 0; j < 2; j++) {
				stillFramesRight[index++] = tmp[0][j];
			}
			stillAnimationRight = new Animation(1f, stillFramesRight);

			Texture walkSheetLeft = TextureStorage.getTexture("playerLeft");
			tmp = TextureRegion.split(walkSheetLeft, walkSheetLeft.getWidth() / 8, walkSheetLeft.getHeight());
			walkFramesLeft = new TextureRegion[8];
			index = 0;
			for (int j = 0; j < 8; j++) {
				walkFramesLeft[index++] = tmp[0][j];
			}
			walkAnimationLeft = new Animation(0.15f, walkFramesLeft);

			Texture stillSheetLeft = TextureStorage.getTexture("playerLeftStill");
			tmp = TextureRegion.split(stillSheetLeft, stillSheetLeft.getWidth() / 2, stillSheetLeft.getHeight());
			stillFramesLeft = new TextureRegion[2];
			index = 0;
			for (int j = 0; j < 2; j++) {
				stillFramesLeft[index++] = tmp[0][j];
			}
			stillAnimationLeft = new Animation(0.5f, stillFramesLeft);

			Texture crouchSheetRight = TextureStorage.getTexture("playerRightCrouch");
			tmp = TextureRegion.split(crouchSheetRight, crouchSheetRight.getWidth() / 8, crouchSheetRight.getHeight());
			crouchFramesRight = new TextureRegion[8];
			index = 0;
			for (int j = 0; j < 8; j++) {
				crouchFramesRight[index++] = tmp[0][j];
			}
			crouchAnimationRight = new Animation(0.25f, crouchFramesRight);

			Texture crouchSheetLeft = TextureStorage.getTexture("playerLeftCrouch");
			tmp = TextureRegion.split(crouchSheetLeft, crouchSheetLeft.getWidth() / 8, crouchSheetLeft.getHeight());
			crouchFramesLeft = new TextureRegion[8];
			index = 0;
			for (int j = 0; j < 8; j++) {
				crouchFramesLeft[index++] = tmp[0][j];
			}
			crouchAnimationLeft = new Animation(0.25f, crouchFramesLeft);

			Texture crouchStillSheetRight = TextureStorage.getTexture("playerRightCrouchStill");
			tmp = TextureRegion.split(crouchStillSheetRight, crouchStillSheetRight.getWidth() / 1, crouchStillSheetRight.getHeight());
			crouchStillFramesRight = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				crouchStillFramesRight[index++] = tmp[0][j];
			}
			crouchStillAnimationRight = new Animation(0.25f, crouchStillFramesRight);

			Texture crouchStillSheetLeft = TextureStorage.getTexture("playerLeftCrouchStill");
			tmp = TextureRegion.split(crouchStillSheetLeft, crouchStillSheetLeft.getWidth() / 1, crouchStillSheetLeft.getHeight());
			crouchStillFramesLeft = new TextureRegion[1];
			index = 0;
			for (int j = 0; j < 1; j++) {
				crouchStillFramesLeft[index++] = tmp[0][j];
			}
			crouchStillAnimationLeft = new Animation(0.25f, crouchStillFramesLeft);
			stateTime = 0f;
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
			switch(playerAction) {
			case STILL_LEFT:
				currentFrame = stillAnimationLeft.getKeyFrame(stateTime, true);
				break;
			case STILL_RIGHT:
				currentFrame = stillAnimationRight.getKeyFrame(stateTime, true);
				break;
			case WALK_LEFT:
				currentFrame = walkAnimationLeft.getKeyFrame(stateTime, true);
				break;
			case WALK_RIGHT:
				currentFrame = walkAnimationRight.getKeyFrame(stateTime, true);
				break;
			case SNEAK_RIGHT:
				currentFrame = crouchAnimationRight.getKeyFrame(stateTime, true);
				break;
			case SNEAK_LEFT:
				currentFrame = crouchAnimationLeft.getKeyFrame(stateTime, true);
				break;
			case SNEAK_STILL_RIGHT:
				currentFrame = crouchStillAnimationRight.getKeyFrame(stateTime, true);
				break;
			case SNEAK_STILL_LEFT:
				currentFrame = crouchStillAnimationLeft.getKeyFrame(stateTime, true);
				break;
			default:
				break;
			}
			if(getSprite() == null || hasChanged) setSprite(new Sprite(currentFrame));
			else sprite.setRegion(currentFrame);
		}
	}

	public void setAction(Action playerAction) {
		this.playerAction = playerAction;
		hasChanged = true;
		armSprite = new Sprite(items[currentSlot].getType().getTexture(isRight()));
		updateArm();
	}

	boolean hasChanged = false;

	private enum Action {
		STILL_LEFT,STILL_RIGHT,WALK_LEFT,WALK_RIGHT,SNEAK_LEFT,SNEAK_RIGHT,SNEAK_STILL_LEFT,SNEAK_STILL_RIGHT;
	}
}