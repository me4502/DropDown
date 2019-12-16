package com.me4502.DropDown.Levels;

import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.me4502.DropDown.Position;

public class LevelDecoder {

	public static LevelBase decodeLevel(FileHandle file) throws Exception {

		BufferedReader br = new BufferedReader(file.reader());
		String line = "";

		LevelBase level = null;
		String name = null, next = null;
		Position startPoint = null;
		int width = 0, height = 0;
		Materials background = null;
		Tile[][] tiles = null;
		List<String> dialogueLines = new LinkedList<String>();

		int index = 0;
		try {
			while((line = br.readLine()) != null) {
				index++;
				if(line.trim().isEmpty() || line.startsWith("#")) continue;
				if(line.startsWith("@")) {
					line = line.substring(1);
					if(line.startsWith("width "))
						width = Integer.parseInt(line.replace("width ", ""));
					else if(line.startsWith("height "))
						height = Integer.parseInt(line.replace("height ", ""));
					else if(line.startsWith("background "))
						background = Materials.fromString(line.replace("background ", ""));
					else if(line.startsWith("name "))
						name = line.replace("name ", "");
					else if(line.startsWith("line "))
						dialogueLines.add(line.replace("line ", ""));
					else if(line.startsWith("next "))
						next = line.replace("next ", "");
					else if(line.startsWith("spawn "))
						startPoint = new Position(Double.parseDouble(line.replace("spawn ", "").split(",")[0]), Double.parseDouble(line.replace("spawn ", "").split(",")[1]));
					else if(line.equalsIgnoreCase("start"))
						tiles = new Tile[width][height];
					else if(line.equalsIgnoreCase("end"))
						level = new LevelBase(name, tiles, background, startPoint, next, dialogueLines);
				} else {
					int x = 0, y = 0;
					String[] bits = line.split(" ");
					x = Integer.parseInt(bits[0].split(",")[0]);
					y = Integer.parseInt(bits[0].split(",")[1]);
					tiles[x][y] = new Tile(Materials.valueOf(bits[1]), x, y);
					if(bits.length > 2)
						for(int i = 2; i < bits.length; i++) {
							if(bits[i].equalsIgnoreCase("enemy"))
								tiles[x][y].setPlaceEnemy(true);
							else if(bits[i].equalsIgnoreCase("ashley"))
								tiles[x][y].setPlaceAshley(true);
							else if(bits[i].equalsIgnoreCase("ashley_right"))
								tiles[x][y].setAshleyRight(true);
							else if(bits[i].equalsIgnoreCase("ashley_left"))
								tiles[x][y].setAshleyRight(false);
						}
				}
			}
		} catch(Exception e) {
			System.out.println("Line " + index);
			throw e;
		}

		br.close();
		return level;
	}
}