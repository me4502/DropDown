package com.me4502.DropDown.Items;


public abstract class Item {

	short durability, maxDurability;
	private ItemType type;

	public Item(ItemType type, short durability) {
		maxDurability = this.durability = durability;
		this.type = type;
	}

	public ItemType getType() {

		return type;
	}

	public short getDurability() {

		return durability;
	}

	public short getMaxDurability() {

		return maxDurability;
	}

	public void setDurability(short durability) {
		this.durability = durability;
	}

	public void damage(short damage) {
		short dur = durability;
		durability -= damage;
		durability = (short) Math.min(durability, maxDurability);
		durability = (short) Math.max(durability, 0);
		if(dur < durability)
			durability = dur;
	}
}