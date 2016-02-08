package net.agl.life.model;

import java.awt.Dimension;

import net.agl.life.config.Config;

/*
 * Life: Bounded Boolean Array model
 */
public class LifeBBA implements Life {
	public static final Formula DEFAULT_FORMULA = new Formula(Config.burn, Config.surviveMin, Config.surviveMax);

	private Formula formula = DEFAULT_FORMULA;
	private boolean[][] cells = null;
	private int alive = 0;

	private LifeBBA() {}

	public LifeBBA(int wd, int ht) {
		setSize(wd, ht);
	}

	public LifeBBA(boolean[][] data) {
		if(data != null && data.length > 0 && data[0].length > 0)
			cells = data;
		for(int y = 0;y < cells.length;y++) {
			for(int x = 0;x < cells[y].length;x++) {
				if(cells[y][x])
					alive++;
			}
		}
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
		boolean[][] copy = new boolean[getRows()][getCols()];
		copy(cells, copy);
		return copy;
	}

	@Override
	public byte[] pack() {
		return packData(cells);
	}

	@Override
	public boolean unpack(byte[] data, int cols, int rows) {
		boolean[][] newcells = unpackData(data, cols, rows);
		if(newcells == null) return false;
		cells = newcells;
		alive = 0;
		for(int y = 0;y < cells.length;y++) {
			for(int x = 0;x < cells[y].length;x++) {
				if(cells[y][x]) alive++;
			}
		}
		return true;
	}

	@Override
	public boolean isValid(int x, int y) {
		return (cells != null
				&& y >= 0 && y < cells.length
				&& x >= 0 && x < cells[0].length
		);
	}

	@Override
	public int getAlives() {
		return alive;
	}

	@Override
	public Dimension getSize() {
		if(cells == null) return null;
		return new Dimension(cells[0].length, cells.length);
	}

	@Override
	public int getCols() {
		if(cells == null || cells[0] == null)
			return 0;
		return cells[0].length;
	}

	@Override
	public int getRows() {
		if(cells == null) return 0;
		return cells.length;
	}

	@Override
	public void setSize(int width, int height) {
		if(width == 0 || height == 0) {
			cells = null;
			alive = 0;
			return;
		}
		if(cells != null && cells.length == height && cells[0].length == width) return;
		boolean[][] newcells = new boolean[height][width];
		if(cells != null) alive = copy(cells, newcells);
		cells = newcells;
	}

	@Override
	public void clear() {
		if(cells == null) return;

		for(int y = 0;y < cells.length;y++)
			for(int x = 0;x < cells[y].length;x++)
				cells[y][x] = false;

		alive = 0;
	}

	@Override
	public boolean test(int x, int y) {
		if(!isValid(x, y))
			return false;
		return cells[y][x];
	}

	@Override
	public void burn(int x, int y) {
		if(isValid(x, y) && !cells[y][x]) {
			cells[y][x] = true;
			alive++;
		}
	}

	@Override
	public void kill(int x, int y) {
		if(isValid(x, y) && cells[y][x]) {
			cells[y][x] = false;
			alive--;
		}
	}

	@Override
	public Life turn() {
		long start = System.currentTimeMillis();
		LifeBBA next = new LifeBBA();
		next.cells = new boolean[getRows()][getCols()];
		next.formula = formula;

		if(cells == null)
			return next;

		for(int y = 0;y < cells.length;y++) {
			for(int x = 0;x < cells[y].length;x++) {
				int n = countNeighbors(x, y);
				if(cells[y][x] && n >= formula.surviveMin && n <= formula.surviveMax
						|| !cells[y][x] && n == formula.burn) {
					next.cells[y][x] = true;
					next.alive++;
				}
			}
		}
		long end = System.currentTimeMillis();

		System.err.println("Life turn calculated in " + (end-start) + " milliseconds");
		return next;
	}

// static utils
	
	public static byte[] packData(boolean[][] data) {
		if(data == null || data.length == 0 || data[0].length == 0)
			return null;
		
		int rows = data.length;
		if(rows == 0) return null;
		int cols = data[0].length;
		if(cols == 0) return null;
		int length = (int)Math.ceil((rows * cols) / 8.0);

		byte[] buf = new byte[length+4];

		for(int y = 0;y < rows;y++) {
			for(int x = 0;x < cols;x++) {
				if(data[y][x]) {
					int idx = (y * cols + x) / 8;
					int bit = (y * cols + x) % 8;
					buf[idx] |= (1 << bit);
				}
			}
		}

		return buf;
	}

	public static boolean[][] unpackData(byte[] data, int cols, int rows) {
		if(data == null)
			throw new IllegalArgumentException("Data array must not be null");
		if(rows <= 0 || cols <= 0)
			throw new IllegalArgumentException("Cols and rows must both be > 0");
		int len = (int)Math.ceil((rows * cols) / 8.0);
		if(len > data.length)
			throw new IllegalArgumentException("The data array is too short for requested field size");

		boolean[][] newcells = new boolean[rows][cols];
		for(int y = 0;y < rows;y++) {
			for(int x = 0;x < cols;x++) {
				int idx = (y * cols + x) / 8;
				int bit = (y * cols + x) % 8;
				newcells[y][x] = ((data[idx] & (1 << bit)) > 0);
			}
		}

		return newcells;
	}

	public static final int copy(final boolean[][] from, final boolean[][] to) {
		int n = 0;
		for(int y = 0;y < from.length && y < to.length;y++) {
			for(int x = 0;x < from[y].length && x < to[y].length;x++) {
				if(to[y][x] = from[y][x]) n++;
			}
		}
		return n;
	}

// non-static utils
	private int countNeighbors(int x, int y) {
		int n = 0;
		for(int _y = y-1;_y - y < 2;_y++) {
			if(_y < 0 || _y >= cells.length) continue;
			for(int _x = x-1;_x - x < 2;_x++) {
				if(_x < 0 || _x >= cells[_y].length
						|| _y == y && _x == x
				) continue;
				if(cells[_y][_x])
					n++;
			}
		}
		return n;
	}

}
