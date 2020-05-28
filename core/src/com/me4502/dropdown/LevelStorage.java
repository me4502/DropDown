package com.me4502.dropdown;

import java.util.HashMap;

import com.me4502.dropdown.Levels.LevelBase;

public class LevelStorage {

	private static final HashMap<String, LevelBase> levels = new HashMap<>();

	public static void addLevelBase(String name, LevelBase level) {
		levels.put(name, level);
	}

	public static LevelBase getLevelBase(String name) {
		return levels.get(name);
	}
}