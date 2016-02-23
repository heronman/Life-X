package net.agl.life.model;

import java.awt.Point;

public class LifeCell {
	public volatile int x;
	public volatile int y;

	public LifeCell() {
		set(0, 0);
	}
	
	public LifeCell(int x, int y) {
		set(x, y);
	}
	
	public LifeCell(final LifeCell cell) {
		set(cell);
	}
	
	public LifeCell(final Point p) {
		set(p);
	}

	public void set(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(final LifeCell cell) {
		this.x = cell.x;
		this.y = cell.y;
	}
	
	public void set(final Point cell) {
		this.x = cell.x;
		this.y = cell.y;
	}
	
	@Override
	public int hashCode() {
		return (23 * 31 + x) * 31 + y;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o == this)
			return true;
		if(o instanceof LifeCell) {
			return ((LifeCell)o).x == x && ((LifeCell)o).y == y;
		} else if (o instanceof Point) {
			return ((Point)o).x == x && ((Point)o).y == y;
		}
		return false;
	}

	@Override
	public LifeCell clone() {
		return new LifeCell(this);
	}

	@Override
	public String toString() {
		return (x + ":" + y);
	}
}
