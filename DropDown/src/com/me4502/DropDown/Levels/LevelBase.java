package com.me4502.DropDown.Levels;

import java.util.List;

import com.me4502.DropDown.Position;

public class LevelBase {

	private String name;
	private Tile[][] tiles;
	private Materials background;
	private Position startPoint;

	private String next;

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