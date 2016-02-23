package net.agl.life.model;

import java.util.HashSet;

class LifeSet extends HashSet<LifeCell> {
	private static final long serialVersionUID = 5696409917359364823L;

	protected LifeCell tl, br;

	public LifeSet() {
		super();
	}

	private void recalcCorners() {
		tl = br = null;
		if (isEmpty())
			return;

		for (LifeCell cell : this) {
			adjustCorners(cell);
		}
	}

	private void adjustCorners(LifeCell cell) {
		if (tl == null) {
			tl = new LifeCell(cell);
		} else {
			if (tl.x > cell.x)
				tl.x = cell.x;
			if (tl.y > cell.y)
				tl.y = cell.y;
		}

		if(br == null) {
			br = new LifeCell(cell);
		} else {
			if (br.x < cell.x)
				br.x = cell.x;
			if (br.y < cell.y)
				br.y = cell.y;
		}
	}

	@Override
	public boolean add(LifeCell cell) {
		if (!super.add(cell)) return false;

		adjustCorners(cell);

		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (!super.remove(o))
			return false;
		recalcCorners();
		return true;
	}

	@Override
	public void clear() {
		super.clear();
		tl = br = null;
	}
}
