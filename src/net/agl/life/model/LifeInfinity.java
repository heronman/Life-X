package net.agl.life.model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class LifeInfinity implements Life {

	@SuppressWarnings("serial")
	private static class LifeSet extends HashSet<Point> {

		protected Point tl = new Point(0, 0), br = new Point(-1, -1);

		public LifeSet() {
			super();
		}

		@Override
		public boolean add(Point p) {
			if (p == null)
				throw new RuntimeException("Can't add null point");
			p = new Point(p);
			boolean empty = isEmpty();
			if (super.add(p)) {
				if (empty) {
					tl.x = br.x = p.x;
					tl.y = br.y = p.y;
				} else {
					if (tl.x > p.x)
						tl.x = p.x;
					if (tl.y > p.y)
						tl.y = p.y;
					if (br.x < p.x)
						br.x = p.x;
					if (br.y < p.y)
						br.y = p.y;
				}
				return true;
			} else {
				return false;
			}
		}

		private void recalcCorners() {
			tl.x = tl.y = 0;
			br.x = br.y = -1;
			if (isEmpty())
				return;

			for (Point p : this) {
				if (tl.x > p.x)
					tl.x = p.x;
				if (tl.y > p.y)
					tl.y = p.y;
				if (br.x < p.x)
					br.x = p.x;
				if (br.y < p.y)
					br.y = p.y;
			}
		}

		@Override
		public boolean remove(Object o) {
			if (!super.remove(o))
				return false;
			recalcCorners();
			return true;
		}
	}

	private LifeSet map;
	private Formula formula;

	public LifeInfinity() {
		map = new LifeSet();
		this.formula = Life.DEFAULT_FORMULA;
	}

	public LifeInfinity(Formula formula) {
		map = new LifeSet();
		this.formula = formula;
	}

	@Override
	public Formula getFormula() {
		return formula;
	}

	@Override
	public void setFormula(Formula formula) {
		this.formula = formula;
	}

	@Override
	public void setFormula(int burn, int smin, int smax) {
		this.formula = new Formula(burn, smin, smax);
	}

	@Override
	public boolean[][] getData() {
		boolean[][] arr = new boolean[getRows()][getCols()];
		for (Point p : map) {
			arr[p.y - map.tl.y][p.x - map.tl.x] = true;
		}
		return arr;
	}

	@Override
	public byte[] pack() {
		int wd = getCols(), ht = getRows();
		int mapSize = wd * ht;
		int size = mapSize / 8 + ((mapSize % 8) != 0 ? 1 : 0);
		byte[] arr = new byte[size];
		for (Point p : map) {
			int index = (p.y - map.tl.y) * wd + (p.x - map.tl.x);
			arr[index / 8] |= (1 << (index % 8));
		}
		return arr;
	}

	@Override
	public boolean unpack(byte[] data, int cols, int rows) {
		clear();

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				int index = y * cols + x;
				byte b = data[index / 8];
				if ((b & (1 << (index % 8))) != 0)
					map.add(new Point(x, y));
			}
		}

		return true;
	}

	@Override
	public boolean isValid(int x, int y) {
		return true;
	}

	@Override
	public int getAlives() {
		return map.size();
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean test(int x, int y) {
		return map.contains(new Point(x, y));
	}

	public boolean test(Point p) {
		return map.contains(p);
	}

	@Override
	public void burn(int x, int y) {
		map.add(new Point(x, y));
	}

	public void burn(Point p) {
		map.add(p);
	}

	@Override
	public void kill(int x, int y) {
		map.remove(new Point(x, y));
	}

	public void kill(Point p) {
		map.remove(p);
	}

	private void processPoint(Point p, boolean alive, Set<Point> target, Set<Point> ready) {
		int n = 0;

		Point test = new Point();
		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				if ((x | y) == 0)
					continue;
				test.x = p.x + x;
				test.y = p.y + y;
				if (map.contains(test)) { // alive: just increase the counter
					n++;
				} else if (ready != null && !ready.contains(test)) { // dead:
																		// process
																		// it if
																		// not
																		// yet
																		// processed
					/*
					 * if the point is dead in the current set and it is a close
					 * neighbor of the target point we have to test it too,
					 * immediately, in the current loop. Also we have to add
					 * that point to the ready-list to avoid double-testing it.
					 * If ready list is not set (null), then it was probably a
					 * subsequent call and we do not need to test neighbors
					 * deeply.
					 */

					ready.add(test); // prevent of multiple processing
					processPoint(test, false, target, null); // for subsequent
																// call, set
																// ready to null
				}
			}
		}

		if (alive) {
			if (n >= formula.surviveMin && n <= formula.surviveMax)
				target.add(p);
		} else {
			if (n == formula.burn)
				target.add(p);
		}
	}

	@Override
	public Life turn() {
		LifeInfinity next = new LifeInfinity(formula);

		Set<Point> ready = new HashSet<Point>();
		for (Point p : map) {
			processPoint(p, true, next.map, ready);
		}

		return next;
	}

	@Override
	public Point getTopLeft() {
		return new Point(map.tl);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(getCols(), getCols());
	}

	@Override
	public int getCols() {
		return map.br.x - map.tl.x + 1;
	}

	@Override
	public int getRows() {
		return map.br.y - map.tl.y + 1;
	}

}
