package com.me4502.dropdown.Levels;

import java.util.List;

import com.me4502.dropdown.Position;

public class LevelBase {

	private final String name;
	private final Tile[][] tiles;
	private final Materials background;
	private final Position startPoint;

	private final String next;

	List<String> dialogLines;

	public LevelBase(String name, Tile[][] tiles, Materials background, Position startPoint, String next, List<String> dialogLines) {
		this.name = name;
		this.tiles = tiles;
		this.background = background;
		this.startPoint = startPoint;
		this.next = next;
		this.dialogLines = dialogLines;
	}

	public Tile[][] getTiles() {

		return tiles;
	}

	public Materials getBackground() {

		return background;
	}

	public String getName() {

		return name;
	}

	public Position getSpawnPoint() {
		return startPoint;
	}

	public String getNext() {
		return next;
	}

	public List<String> getDialogueLines() {
		return dialogLines;
	}
}