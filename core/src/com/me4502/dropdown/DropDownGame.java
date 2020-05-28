package com.me4502.dropdown;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.me4502.dropdown.Items.Item;
import com.me4502.dropdown.Items.ItemType;
import com.me4502.dropdown.Levels.Level;
import com.me4502.dropdown.Levels.LevelDecoder;
import com.me4502.dropdown.Levels.Materials;
import com.me4502.dropdown.Levels.Tile;

public class DropDownGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;

	private GlyphLayout glyphLayout = new GlyphLayout();
	BitmapFont mainFont, smallFont, largeFont;

	public static Level lev;

	public static final float ambientIntensity = 0.5f;
	public static final Vector3 ambientColor = new Vector3(0.001f, 0.0f, 0.001f);

	public float zAngle;
	public static final float zSpeed = 0.7f;
	public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;

	public static ShaderProgram defaultShader;
	public static ShaderProgram ambientShader;
	public static ShaderProgram lightShader;
	public static ShaderProgram finalShader;

	private FrameBuffer fbo;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();

		TextureStorage.addTexture("playerRightStill", new Texture(Gdx.files.internal("data/entity/player-right-still.png")));
		TextureStorage.addTexture("playerRightCrouch", new Texture(Gdx.files.internal("data/entity/player-right crouch.png")));
		TextureStorage.addTexture("playerRightCrouchStill", new Texture(Gdx.files.internal("data/entity/crouch-still-right.png")));
		TextureStorage.addTexture("playerRight", new Texture(Gdx.files.internal("data/entity/player-right.png")));

		TextureStorage.addTexture("playerLeftStill", new Texture(Gdx.files.internal("data/entity/player-left-still.png")));
		TextureStorage.addTexture("playerLeftCrouch", new Texture(Gdx.files.internal("data/entity/player-left-crouch.png")));
		TextureStorage.addTexture("playerLeftCrouchStill", new Texture(Gdx.files.internal("data/entity/crouch-still-left.png")));
		TextureStorage.addTexture("playerLeft", new Texture(Gdx.files.internal("data/entity/player-left.png")));

		TextureStorage.addTexture("ashleyLeft", new Texture(Gdx.files.internal("data/entity/ashley-left.png")));
		TextureStorage.addTexture("ashleyRight", new Texture(Gdx.files.internal("data/entity/ashley-right.png")));
		TextureStorage.addTexture("ashleyHeadLeft", new Texture(Gdx.files.internal("data/entity/dialog-left.png")));
		TextureStorage.addTexture("ashleyHeadRight", new Texture(Gdx.files.internal("data/entity/dialog-right.png")));

		TextureStorage.addTexture("enemyLeft", new Texture(Gdx.files.internal("data/entity/enemy-left.png")));
		TextureStorage.addTexture("enemyRight", new Texture(Gdx.files.internal("data/entity/enemy-right.png")));

		TextureStorage.addTexture("enemyLeftStill", new Texture(Gdx.files.internal("data/entity/enemy-left-still.png")));
		TextureStorage.addTexture("enemyRightStill", new Texture(Gdx.files.internal("data/entity/enemy-right-still.png")));

		TextureStorage.addTexture("redBrick", new Texture(Gdx.files.internal("data/terrain/dungeon/redbrick.png")));
		TextureStorage.addTexture("redWall", new Texture(Gdx.files.internal("data/terrain/dungeon/redwall.png")));
		TextureStorage.addTexture("purpleBrick", new Texture(Gdx.files.internal("data/terrain/dungeon/purpbrick.png")));
		TextureStorage.addTexture("purpleWall", new Texture(Gdx.files.internal("data/terrain/dungeon/purpwall.png")));
		TextureStorage.addTexture("blueBrick", new Texture(Gdx.files.internal("data/terrain/dungeon/bluebrick.png")));
		TextureStorage.addTexture("blueWall", new Texture(Gdx.files.internal("data/terrain/dungeon/bluewall.png")));
		TextureStorage.addTexture("greenBrick", new Texture(Gdx.files.internal("data/terrain/dungeon/greenbrick.png")));
		TextureStorage.addTexture("greenWall", new Texture(Gdx.files.internal("data/terrain/dungeon/greenwall.png")));
		TextureStorage.addTexture("yellowBrick", new Texture(Gdx.files.internal("data/terrain/dungeon/yellobrick.png")));
		TextureStorage.addTexture("yellowWall", new Texture(Gdx.files.internal("data/terrain/dungeon/yellowall.png")));
		TextureStorage.addTexture("orangeBrick", new Texture(Gdx.files.internal("data/terrain/dungeon/orangbrick.png")));
		TextureStorage.addTexture("orangeWall", new Texture(Gdx.files.internal("data/terrain/dungeon/orangwall.png")));
		TextureStorage.addTexture("barricade", new Texture(Gdx.files.internal("data/terrain/dungeon/barricade.png")));

		TextureStorage.addTexture("embers", new Texture(Gdx.files.internal("data/particles/embers.png")));
		TextureStorage.addTexture("torch", new Texture(Gdx.files.internal("data/terrain/torch.png")));
		TextureStorage.addTexture("light", new Texture(Gdx.files.internal("data/misc/lightmap.png")));
		TextureStorage.addTexture("angledLight", new Texture(Gdx.files.internal("data/misc/angled-lightmap.png")));

		TextureStorage.addTexture("ironBars", new Texture(Gdx.files.internal("data/terrain/bars.png")));
		TextureStorage.addTexture("crack", new Texture(Gdx.files.internal("data/terrain/crack.png")));

		TextureStorage.addTexture("hotbar", new Texture(Gdx.files.internal("data/misc/hotbar.png")));
		TextureStorage.addTexture("health", new Texture(Gdx.files.internal("data/misc/green.png")));

		TextureStorage.addTexture("speechBack", new Texture(Gdx.files.internal("data/misc/speech-back.png")));

		TextureStorage.addTexture("swordArmStabRight", new Texture(Gdx.files.internal("data/entity/weapons/sword-right-stab.png")));
		TextureStorage.addTexture("swordArmStabLeft", new Texture(Gdx.files.internal("data/entity/weapons/sword-left-stab.png")));
		TextureStorage.addTexture("swordArmRight", new Texture(Gdx.files.internal("data/entity/weapons/sword-right.png")));
		TextureStorage.addTexture("swordArmLeft", new Texture(Gdx.files.internal("data/entity/weapons/sword-left.png")));
		TextureStorage.addTexture("swordArmIcon", new Texture(Gdx.files.internal("data/entity/weapons/sword-icon.png")));
		TextureStorage.addTexture("crossbowArmLeft", new Texture(Gdx.files.internal("data/entity/weapons/crossbow-left.png")));
		TextureStorage.addTexture("crossbowArmRight", new Texture(Gdx.files.internal("data/entity/weapons/crossbow-right.png")));
		TextureStorage.addTexture("crossbowArmIcon", new Texture(Gdx.files.internal("data/entity/weapons/crossbow-icon.png")));
		TextureStorage.addTexture("crossbowBolt", new Texture(Gdx.files.internal("data/entity/weapons/crossbow-bolt.png")));
		TextureStorage.addTexture("lampArmLeft", new Texture(Gdx.files.internal("data/entity/weapons/lamp-left.png")));
		TextureStorage.addTexture("lampArmRight", new Texture(Gdx.files.internal("data/entity/weapons/lamp-right.png")));
		TextureStorage.addTexture("lampArmIcon", new Texture(Gdx.files.internal("data/entity/weapons/lamp-icon.png")));
		TextureStorage.addTexture("armArmLeft", new Texture(Gdx.files.internal("data/entity/weapons/arm-left.png")));
		TextureStorage.addTexture("armArmRight", new Texture(Gdx.files.internal("data/entity/weapons/arm-right.png")));

		TextureStorage.addTexture("kissSceneLeft", new Texture(Gdx.files.internal("data/entity/ashley-jeremy-kiss-left.png")));
		TextureStorage.addTexture("kissSceneRight", new Texture(Gdx.files.internal("data/entity/ashley-jeremy-kiss-right.png")));


		SoundStorage.addSound("background", Gdx.audio.newSound(Gdx.files.internal("data/audio/rescue.mp3")));

		SoundStorage.addSound("boarDeath", Gdx.audio.newSound(Gdx.files.internal("data/audio/boar_die.ogg")));
		SoundStorage.addSound("crossbowShot", Gdx.audio.newSound(Gdx.files.internal("data/audio/crossbow_shot.ogg")));
		SoundStorage.addSound("swordStab", Gdx.audio.newSound(Gdx.files.internal("data/audio/sword_stab.ogg")));


		try {
			LevelStorage.addLevelBase("orangeFive", LevelDecoder.decodeLevel(Gdx.files.internal("data/levels/orangeFive.map")));
			LevelStorage.addLevelBase("yellowFour", LevelDecoder.decodeLevel(Gdx.files.internal("data/levels/yellowFour.map")));
			LevelStorage.addLevelBase("greenThree", LevelDecoder.decodeLevel(Gdx.files.internal("data/levels/greenThree.map")));
			LevelStorage.addLevelBase("blueTwo", LevelDecoder.decodeLevel(Gdx.files.internal("data/levels/blueTwo.map")));
			LevelStorage.addLevelBase("purpleOne", LevelDecoder.decodeLevel(Gdx.files.internal("data/levels/purpleOne.map")));
			LevelStorage.addLevelBase("redIntro", LevelDecoder.decodeLevel(Gdx.files.internal("data/levels/redIntro.map")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		smallFont = new BitmapFont(Gdx.files.internal("data/font/small.fnt"), Gdx.files.internal("data/font/small.png"), false);
		mainFont = new BitmapFont(Gdx.files.internal("data/font/main.fnt"), Gdx.files.internal("data/font/main.png"), false);
		largeFont = new BitmapFont(Gdx.files.internal("data/font/large.fnt"), Gdx.files.internal("data/font/large.png"), false);

		lev = new Level(LevelStorage.getLevelBase("redIntro"));

		defaultShader = new ShaderProgram(Gdx.files.internal("data/shaders/vertexShader.glsl"), Gdx.files.internal("data/shaders/defaultPixelShader.glsl"));
		ambientShader = new ShaderProgram(Gdx.files.internal("data/shaders/vertexShader.glsl"), Gdx.files.internal("data/shaders/ambientPixelShader.glsl"));
		lightShader = new ShaderProgram(Gdx.files.internal("data/shaders/vertexShader.glsl"), Gdx.files.internal("data/shaders/lightPixelShader.glsl"));
		finalShader = new ShaderProgram(Gdx.files.internal("data/shaders/vertexShader.glsl"), Gdx.files.internal("data/shaders/pixelShader.glsl"));

		if(ambientShader.isCompiled()) {
			ambientShader.begin();
			ambientShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y, ambientColor.z, ambientIntensity);
			ambientShader.end();
		}

		if(lightShader.isCompiled()) {
			lightShader.begin();
			lightShader.setUniformi("u_lightmap", 1);
			lightShader.end();
		}

		if(finalShader.isCompiled()) {
			finalShader.begin();
			finalShader.setUniformi("u_lightmap", 1);
			finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y, ambientColor.z, ambientIntensity);
			finalShader.end();
		}

		Texture swordSheetRight = TextureStorage.getTexture("ashleyHeadRight");
		TextureRegion[][] tmp = TextureRegion.split(swordSheetRight, swordSheetRight.getWidth() / 2, swordSheetRight.getHeight());
		TextureRegion[] ashleyFramesRight = new TextureRegion[2];
		int index = 0;
		for (int j = 0; j < 2; j++) {
			ashleyFramesRight[index++] = tmp[0][j];
		}
		ashleyDialogAnimation = new Animation(1f, ashleyFramesRight);

		InputListener inputProcessor = new InputListener();
		Gdx.input.setInputProcessor(inputProcessor);

		//Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
	}

	@Override
	public void dispose() {
		batch.dispose();
		defaultShader.dispose();
		ambientShader.dispose();
		lightShader.dispose();
		finalShader.dispose();
		fbo.dispose();
		for(Texture tex : TextureStorage.getTextures())
			tex.dispose();
		for(Sound sound : SoundStorage.getSounds())
			sound.dispose();
	}

	float randomFlicker = 0;
	int flickerTimer = 30;

	@Override
	public void render() {

		if(!isPaused) {
			if((lev.lastCameraMove > 5 || lev.startedReading) && lev.readingLine <= lev.base.getDialogueLines().size()-1) {

			} else
				lev.update();
		}

		zAngle += Gdx.graphics.getDeltaTime() * zSpeed;
		while(zAngle > PI2)
			zAngle -= PI2;

		if(flickerTimer <= 0) {
			randomFlicker = MathUtils.random();
			flickerTimer = 10;
		} else
			flickerTimer --;

		fbo.begin();
		batch.setProjectionMatrix(camera.combined);
		if(defaultShader.isCompiled())
			batch.setShader(defaultShader);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		float lightSize = 700+4.75f + 0.25f * (float)Math.sin(zAngle) + 50f*randomFlicker;
		for(Tile[] tiles : lev.base.getTiles())
			for(Tile tile : tiles) {
				if(tile == null) continue;
				if(tile.getType() != Materials.TORCH) continue;
				batch.draw(TextureStorage.getTexture("light"), tile.getX()*64*lev.getBaseYScale()-Gdx.graphics.getWidth()/2 - lev.cameraXOffset-lightSize/2 + 16, tile.getY()*64*lev.getBaseYScale()-Gdx.graphics.getHeight()/2-lightSize/2 + 16, lightSize, lightSize);
			}
		if(lev.player.getHeldItemType() == ItemType.LAMP && !lev.player.isSneaking() && lev.player.items[lev.player.currentSlot].getDurability() > 0) {
			batch.draw(new TextureRegion(TextureStorage.getTexture("angledLight")), lev.player.getArmSprite().getX()-256, lev.player.getArmSprite().getY()-256 - 85, 300f, 375f, lightSize, lightSize, 1f, 1f, lev.player.getArmDirection());
		}
		batch.end();
		fbo.end();

		//Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.setProjectionMatrix(camera.combined);
		if(finalShader.isCompiled())
			batch.setShader(finalShader);
		batch.begin();
		fbo.getColorBufferTexture().bind(1);
		TextureStorage.getTexture("light").bind(0);
		//sprite.draw(batch);
		lev.render(batch);
		//mainFont.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		//mainFont.draw(batch, "Lolz", 100,100);
		batch.end();

		batch.setProjectionMatrix(camera.combined);
		if(defaultShader.isCompiled())
			batch.setShader(defaultShader);
		batch.begin();
		Sprite sprite = new Sprite(TextureStorage.getTexture("hotbar"));
		sprite.setOrigin(0f, 0f);
		sprite.setAlpha(0.5f);
		sprite.setScale(0.5f, 0.5f);
		int index = 0;
		for(Item item : lev.player.items) {
			if(index == lev.player.currentSlot) sprite.setAlpha(1f);
			else sprite.setAlpha(0.5f);
			sprite.setPosition(-Gdx.graphics.getWidth()/2+32*index, -Gdx.graphics.getHeight()/2+8f);
			sprite.draw(batch);
			Sprite icon = new Sprite(item.getType().getIconTexture());
			icon.setOrigin(0f, 0f);
			if(index != lev.player.currentSlot) icon.setAlpha(0.5f);
			icon.setScale(0.5f, 0.5f);
			icon.setPosition(-Gdx.graphics.getWidth()/2+32*index, -Gdx.graphics.getHeight()/2+8f);
			icon.draw(batch);
			Sprite health = new Sprite(TextureStorage.getTexture("health"));
			if(index == lev.player.currentSlot) health.setAlpha(1f);
			else health.setAlpha(0.5f);
			float healthPerc = (float)item.getDurability()/item.getMaxDurability();
			health.setBounds(-Gdx.graphics.getWidth()/2+32*index++, -Gdx.graphics.getHeight()/2, health.getWidth()*healthPerc, 8f);
			health.setOrigin(0f, 0f);
			health.draw(batch);
		}

		if((lev.lastCameraMove > 5 || lev.startedReading) && lev.readingLine <= lev.base.getDialogueLines().size()-1) {

			stateTime += Gdx.graphics.getDeltaTime();
			lev.startedReading = true;
			Sprite sBack = new Sprite(TextureStorage.getTexture("speechBack"));
			sBack.setOrigin(0f, 0f);
			sBack.setBounds(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/3);
			sBack.draw(batch);
			Sprite ash = new Sprite(ashleyDialogAnimation.getKeyFrame(stateTime, true));
			ash.setOrigin(0f, 0f);
			ash.setBounds(-Gdx.graphics.getWidth() / 2 + 32, -Gdx.graphics.getHeight() / 2 + 32, 128f, 128f);
			ash.draw(batch);
			mainFont.setColor(0f, 0f, 0f, 1f);
			List<String> lines = new LinkedList<>(Arrays.asList(lev.base.getDialogueLines().get(lev.readingLine).split("\\|")));
			int ind = 0;
			for(String string : lines)
				mainFont.draw(batch, string, -Gdx.graphics.getWidth() / 2 + 32+128+32, -164 + (isAndroid() ? 48 : 0) - (mainFont.getBounds(string).height+4)*ind++);

			smallFont.setColor(0f,0f,0f,1f);
			String pressString = isAndroid() ? "Tap " : "Press E ";
			smallFont.draw(batch, "(" + pressString + "to continue)", 0-smallFont.getBounds("(" + pressString + "to continue)").width/2,
					-Gdx.graphics
					.getHeight() / 2 + smallFont.getCapHeight() + 16);
			smallFont.draw(batch, "Ashley", -Gdx.graphics.getWidth() / 2 +30+ash.getWidth()/2-smallFont.getBounds("Ashley").width/2, -Gdx.graphics
					.getHeight() / 2 + smallFont.getCapHeight() + 16);
			if((Gdx.input.isKeyPressed(Keys.E) || isAndroid() && Gdx.input.isTouched()) && clickTimer <= 0) {
				lev.readingLine += 1;
				clickTimer = 20;
			}
			clickTimer--;
		} else
			lev.startedReading = false;

		if(lev.isGameOver) {
			glyphLayout.setText(largeFont, "Game Over");
			largeFont.setColor(1f, 0f, 0f, 1f);
			largeFont.draw(batch, glyphLayout, 0-glyphLayout.width/2, 0-glyphLayout.height/2);

			glyphLayout.setText(largeFont, "Click to Play Again!");
			largeFont.draw(batch, glyphLayout, 0-glyphLayout.width/2, 0-64-glyphLayout.height/2);
		} else if(isPaused) {
			glyphLayout.setText(largeFont, "Paused");
			largeFont.setColor(1f, 1f, 1f, 1f);
			largeFont.draw(batch, glyphLayout, 0-glyphLayout.width/2, 0-glyphLayout.height/2);

			glyphLayout.setText(largeFont, "Press Escape to Resume!");
			largeFont.draw(batch, glyphLayout, 0-glyphLayout.width/2, 0-64-glyphLayout.height/2);
		}

		batch.end();
	}

	public static boolean isAndroid() {

		return Gdx.app.getType() == ApplicationType.Android;
	}

	int clickTimer = 0;
	Animation ashleyDialogAnimation;
	float stateTime;

	@Override
	public void resize(int width, int height) {

		camera = new OrthographicCamera(width, height);

		fbo = new FrameBuffer(Format.RGBA8888, width, height, false);

		lightShader.begin();
		lightShader.setUniformf("resolution", width, height);
		lightShader.end();

		finalShader.begin();
		finalShader.setUniformf("resolution", width, height);
		finalShader.end();

		lev = new Level(lev.base);
	}

	public static boolean isPaused = false;

	@Override
	public void pause() {
		isPaused = true;
	}

	@Override
	public void resume() {
		isPaused = false;
	}

	public static boolean moveLeft, moveRight, moveJump, moveCrouch; //Android movement vars.
	private int lastDownX, lastDownY;

	public class InputListener implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			if(keycode == Keys.ESCAPE)
				if(isPaused) resume(); else pause();

			if(isPaused) {
				if(keycode == Keys.NUMPAD_0)
					lev = new Level(LevelStorage.getLevelBase("redIntro"));
				if(keycode == Keys.NUMPAD_1)
					lev = new Level(LevelStorage.getLevelBase("purpleOne"));
				if(keycode == Keys.NUMPAD_2)
					lev = new Level(LevelStorage.getLevelBase("blueTwo"));
				if(keycode == Keys.NUMPAD_3)
					lev = new Level(LevelStorage.getLevelBase("greenThree"));
				if(keycode == Keys.NUMPAD_4)
					lev = new Level(LevelStorage.getLevelBase("yellowFour"));
				if(keycode == Keys.NUMPAD_5)
					lev = new Level(LevelStorage.getLevelBase("orangeFive"));
			}
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			if(lev.isGameOver && !(lev.readingLine <= lev.base.getDialogueLines().size()-1)) {
				lev = new Level(LevelStorage.getLevelBase("redIntro"));
			} else
				lev.onTouchDown(screenX, screenY, pointer, button);

			if(button == Buttons.LEFT) {
				lastDownX = screenX;
				lastDownY = screenY;
			}
			return true;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			moveLeft = false;
			moveRight = false;
			moveJump = false;
			moveCrouch = false;
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			if(DropDownGame.isAndroid()) {

				//Parse android movement.
				if (lastDownX - screenX > 10 && Math.abs(screenY-lastDownY) < 15) {
					moveRight = false;
					moveLeft = true;
				} else if (lastDownX - screenX < -10 && Math.abs(screenY-lastDownY) < 15) {
					moveLeft = false;
					moveRight = true;
				} else {
					moveLeft = false;
					moveRight = false;
				}

				if (lastDownY - screenY < -10) {
					moveJump = false;
					moveCrouch = true;
				} else if (lastDownY - screenY > 10) {
					moveCrouch = false;
					moveJump = true;
				} else {
					moveJump = false;
					moveCrouch = false;
				}

				return true;
			} else
				return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(int amount) {

			do {
				boolean foundUnbroken = false;
				for(int i = 0; i < lev.player.items.length; i++)
					if(lev.player.items[i].getDurability() > 0)
						foundUnbroken = true;
				if(!foundUnbroken) return true;
				lev.player.currentSlot += amount;
				while(lev.player.currentSlot >= lev.player.items.length)
					lev.player.currentSlot -= lev.player.items.length;
				while(lev.player.currentSlot < 0)
					lev.player.currentSlot += lev.player.items.length;
			} while(lev.player.items[lev.player.currentSlot].getDurability() <= 0);

			return true;
		}
	}
}
