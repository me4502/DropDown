package com.me4502.DropDown;

public class Position {

	double x,y;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getDistance(Position position) {

		return Math.sqrt(getDistanceSquared(position));
	}

	public double getDistanceSquared(Position position) {

		double xOff = Math.pow(x-position.x, 2);
		double yOff = Math.pow(y-position.y, 2);

		return xOff + yOff;
	}
}