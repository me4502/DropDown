package com.me4502.DropDown.Levels;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me4502.DropDown.DropDownGame;
import com.me4502.DropDown.LevelStorage;
import com.me4502.DropDown.Position;
import com.me4502.DropDown.Entities.Ashley;
import com.me4502.DropDown.Entities.Enemy;
import com.me4502.DropDown.Entities.Entity;
import com.me4502.DropDown.Entities.LivingEntity;
import com.me4502.DropDown.Entities.Particle;
import com.me4502.DropDown.Entities.Particle.ParticleType;
import com.me4502.DropDown.Entities.Player;
import com.me4502.DropDown.Items.Item;

public class Level {

	public LevelBase base;

	public List<Entity> entities = new LinkedList<Entity>();

	public int cameraXOffset = 0;

	public Player player;
	public Ashley ashley;

	public int readingLine = 0;

	public boolean isGameOver = false;

	public Level(LevelBase base) {
		this.base = base;

		for(Tile[] tiles : base.getTiles()) {
			for(Tile tile : tiles) {
				if(tile == null) continue;
				if(tile.placeAshley)
					entities.add(ashley = new Ashley(this, new Position(tile.getX()*64*getBaseYScale()-Gdx.graphics.getWidth()/2 - cameraXOffset - 8, (tile.getY()+(tile.getType().collides() ? 1 : 0))*64*getBaseYScale()-Gdx.graphics.getHeight()/2+5), tile.isAshleyRight() ? Ashley.Action.STILL_RIGHT : Ashley.Action.STILL_LEFT));
				if(tile.placeEnemy)
					entities.add(new Enemy(this, new Position(tile.getX()*64*getBaseYScale()-Gdx.graphics.getWidth()/2 - cameraXOffset - 8, (tile.getY()+(tile.getType().collides() ? 1 : 0))*64*getBaseYScale()-Gdx.graphics.getHeight()/2+5)));
				if(tile.getType() == Materials.TORCH)
					entities.add(0, new Particle(ParticleType.EMBER, this, new Position(tile.getX()*64*getBaseYScale()-Gdx.graphics.getWidth()/2 - cameraXOffset+8*getBaseYScale(),tile.getY()*64*getBaseYScale()-Gdx.graphics.getHeight()/2+40)));
			}
		}

		entities.add(player = new Player(this));
	}

	public Level(LevelBase base, Item[] items) {
		this(base);
		player.items = items;
		player.switchSlot(0);
	}

	public LevelBase getBase() {

		return base;
	}

	public int lastCameraMove = 0;

	public boolean startedReading = false;

	public void update() {

		if(isGameOver) return;

		lastCameraMove ++;

		int difX = 0;

		if(player.getPosition().getX() > 3 && cameraXOffset < 64*getBaseYScale()*getBase().getTiles().length-Gdx.graphics.getWidth())
			difX = player.isSneaking() ? 1 : 3;
		else if(player.getPosition().getX() < 0 && cameraXOffset > 0)
			difX = -(player.isSneaking() ? 1 : 3);

		if(Gdx.input.isKeyPressed(Keys.F))
			difX *= 10;

		cameraXOffset += difX;

		if(difX != 0) {
			lastCameraMove = 0;
			for(Entity ent : entities)
				ent.getPosition().setX(ent.getPosition().getX() - difX);
		}


		Iterator<Entity> iter = entities.iterator();
		boolean didDelete = false;
		while(iter.hasNext()) {
			Entity ent = iter.next();
			if(ent instanceof LivingEntity && ((LivingEntity)ent).dead == true) {iter.remove(); didDelete = true; continue;}
			if(ent.remove == true) {iter.remove(); continue;}
			ent.update();
		}
		if(didDelete) {
			boolean foundEnemy = false;
			for(Entity ent : entities) {
				if(ent instanceof Enemy) {
					foundEnemy = true;
					break;
				}
			}

			if(!foundEnemy) {
				for(Tile[] tiles : base.getTiles()) {
					for(Tile tile : tiles) {
						if(tile == null) continue;
						if(tile.getType() == Materials.BARRICADE) {
							tile.type = base.getBackground();
							//break;
						}
					}
				}
			}
		}
	}

	public void render(SpriteBatch batch) {

		for(int x = 0; x < getBaseYScale()*getBase().getTiles().length+Gdx.graphics.getWidth()/64; x++) {
			for(int y = 0; y < getBaseYScale()*getBase().getTiles()[0].length+Gdx.graphics.getHeight()/64; y++) {
				Sprite sprite = new Sprite(base.getBackground().getTexture());
				sprite.setOrigin(0f, 0f);

				sprite.setScale(getBaseYScale());
				sprite.setPosition(x*64*sprite.getScaleX()-Gdx.graphics.getWidth()/2 - DropDownGame.lev.cameraXOffset,y*64*sprite.getScaleY()-Gdx.graphics.getHeight()/2);
				sprite.draw(batch);
			}
		}
		for(Tile[] tiles : base.getTiles()) {
			for(Tile tile : tiles) {
				if(tile == null) continue;
				Sprite sprite = new Sprite(tile.type.getTexture());
				sprite.setOrigin(0f, 0f);

				sprite.setScale(getBaseYScale());
				sprite.setPosition(tile.getX()*64*sprite.getScaleX()-Gdx.graphics.getWidth()/2 - cameraXOffset,tile.getY()*64*sprite.getScaleY()-Gdx.graphics.getHeight()/2);
				sprite.draw(batch);
			}
		}
		for(Entity ent : entities)
			ent.render(batch);
	}

	public float getBaseYScale() {
		return Gdx.graphics.getHeight() / 64f / base.getTiles()[0].length;
	}
	public float getBaseXScale() {
		return Gdx.graphics.getWidth() / 64f / base.getTiles().length;
	}

	public void next() {
		if(base.getNext() != null) {
			DropDownGame.lev = new Level(LevelStorage.getLevelBase(base.getNext()), player.items);
		}
	}

	public void onTouchDown(int screenX, int screenY, int pointer, int button) {
		player.useWeapon();
	}
}