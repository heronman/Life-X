package net.agl.life.model;

import java.util.HashSet;
import java.util.Set;

public class LifeInfinity implements Life {

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
		synchronized(map) {
			this.formula = formula;
		}
	}

	@Override
	public void setFormula(int burn, int smin, int smax) {
		synchronized(map) {
			this.formula = new Formula(burn, smin, smax);
		}
	}

	@Override
	public boolean[][] getData() {
		synchronized(map) {
			if(map.size() == 0)
				return null;
			boolean[][] arr = new boolean[getRows()][getCols()];
			for (LifeCell cell : map) {
				arr[cell.y - map.tl.y][cell.x - map.tl.x] = true;
			}
			return arr;
		}
	}

	@Override
	public void setData(boolean[][] data) {
		synchronized(map) {
			clear();

			for(int y = 0; y < data.length; y++) {
				for(int x = 0; x < data[y].length; x++) {
					if(data[y][x])
						map.add(new LifeCell(x, y));
				}
			}
		}
	}

	@Override
	public byte[] pack() {
		synchronized(map) {
			int wd = getCols(), ht = getRows();
			int mapSize = wd * ht;
			int size = mapSize / 8 + ((mapSize % 8) != 0 ? 1 : 0);
			byte[] arr = new byte[size];
			for (LifeCell cell : map) {
				int index = (cell.y - map.tl.y) * wd + (cell.x - map.tl.x);
				arr[index / 8] |= (1 << (index % 8));
			}
			return arr;
		}
	}

	@Override
	public boolean unpack(byte[] data, int cols, int rows) {
		synchronized(map) {
			clear();

			for (int y = 0; y < rows; y++) {
				for (int x = 0; x < cols; x++) {
					int index = y * cols + x;
					byte b = data[index / 8];
					if ((b & (1 << (index % 8))) != 0)
						map.add(new LifeCell(x, y));
				}
			}

			return true;
		}
	}

	@Override
	public int getAlives() {
		synchronized(map) {
			return map.size();
		}
	}

	@Override
	public void clear() {
		synchronized(map) {
			map.clear();
		}
	}

	@Override
	public boolean test(int x, int y) {
		return test(new LifeCell(x, y));
	}

	public boolean test(LifeCell cell) {
		synchronized(map) {
			return map.contains(cell);
		}
	}

	@Override
	public void burn(final int x, final int y) {
		burn(new LifeCell(x, y));
	}

	public void burn(final LifeCell cell) {
		synchronized(map) {
			map.add(cell);
		}
	}

	@Override
	public void kill(final int x, final int y) {
		kill(new LifeCell(x, y));
	}

	public void kill(final LifeCell cell) {
		synchronized(map) {
			map.remove(cell);
		}
	}

	private void processPoint(LifeCell cell, boolean alive, Set<LifeCell> target, Set<LifeCell> ready) {
		int n = 0;

		LifeCell test = new LifeCell();
		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				if ((x | y) == 0)
					continue;
				test.x = cell.x + x;
				test.y = cell.y + y;
				if (map.contains(test)) {
					n++;
				} else if (ready != null && !ready.contains(test)) {
					LifeCell nc = test.clone();
					ready.add(nc);
					processPoint(nc, false, target, null);
				}
			}
		}

		if (alive) {
			if (n >= formula.surviveMin && n <= formula.surviveMax)
				target.add(cell);
		} else {
			if (n == formula.burn)
				target.add(cell);
		}
	}

	@Override
	public Life turn() {
		LifeInfinity next = new LifeInfinity(formula);

		Set<LifeCell> ready = new HashSet<LifeCell>();

		synchronized(map) {
			for (LifeCell cell : map) {
				processPoint(cell, true, next.map, ready);
			}
		}

		return next;
	}

	@Override
	public int getMaxCols() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getMaxRows() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getTop() {
		return map.tl == null ? 0 : map.tl.y;
	}

	@Override
	public int getLeft() {
		return map.tl == null ? 0 : map.tl.x;
	}

	@Override
	public int getBottom() {
		return map.br == null ? -1 : map.br.y;
	}

	@Override
	public int getRight() {
		return map.br == null ? -1 : map.br.x;
	}

	@Override
	public int getCols() {
		if(map.tl == null || map.br == null)
			return 0;
		return map.br.x - map.tl.x + 1;
	}

	@Override
	public int getRows() {
		if(map.tl == null || map.br == null)
			return 0;
		return map.br.y - map.tl.y + 1;
	}

}
