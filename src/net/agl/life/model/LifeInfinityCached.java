package net.agl.life.model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

public class LifeInfinityCached implements Life {

	private byte[] cache = new byte[65536];
	private Set<Point> map;
	private Formula formula;

	private static int getQuadroNeighbors(int src, int index) {
		int n = 0;
		for (int dy = -4; dy <= 4; dy += 4) {
			for (int dx = -1; dx <= 1; dx++) {
				if (dx == 0 && dy == 0)
					continue;
				int i2 = index + dy + dx;
				if (i2 >= 0 && i2 < 16 && (src & (1 << i2)) != 0)
					n++;
			}
		}
		return n;
	}

	private static byte calcQuad(int src, Formula formula) {
		byte b = 0;

		for (int index = 0; index < 4; index++) {
			int qi = index + 5;
			if (index > 1)
				qi += 4;
			int n = getQuadroNeighbors(src, qi);
			if ((src & (1 << qi)) != 0) {
				if (n >= formula.surviveMin && n <= formula.surviveMax)
					b |= (1 << index);
			} else if (n == formula.burn) {
				b |= (1 << index);
			}
		}

		return b;
	}

	public LifeInfinityCached(Formula formula) {
		map = new HashSet<Point>();
		setFormula(formula);
	}

	private LifeInfinityCached(Formula formula, byte[] cache) {
		map = new HashSet<Point>();
		this.formula = formula;
		this.cache = cache;
	}

	@Override
	public Formula getFormula() {
		return formula;
	}

	@Override
	public void setFormula(Formula f) {
		this.formula = formula;
		for (int n = 0; n < cache.length; n++) {
			cache[n] = calcQuad(n, f);
		}
	}

	@Override
	public void setFormula(int burn, int smin, int smax) {
		this.formula = new Formula(burn, smin, smax);
	}

	@Override
	public boolean[][] getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] pack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean unpack(byte[] data, int cols, int rows) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValid(int x, int y) {
		return map.contains(new Point(x, y));
	}

	@Override
	public int getAlives() {
		return map.size();
	}

	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCols() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean test(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void burn(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void kill(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public Life turn() {
		LifeInfinityCached next = new LifeInfinityCached(formula);

		Point lt = new Point(), rb = new Point();
		getCorners(lt, rb);
		Point olt = new Point(lt), orb = new Point(rb);
		lt.x -= 2;
		lt.y -= 2;
		rb.x += 2;
		rb.y += 2;

		for (int y = lt.y; y <= orb.y; y += 2) {
			int q = 0;
			for (int x = lt.x; x <= orb.x; x += 2) {
				q = (q >>> 2) & 0xcccc;
				for (int yy = 0; yy < 4; yy++) {
					for (int xx = 2; xx < 4; xx++) {
						int xxx = x + xx, yyy = y + yy;
						if (xxx >= olt.x && yyy >= olt.y && xxx <= orb.x && yyy <= orb.y && test(xxx, yyy)) {
							q |= 1 << ((y - lt.y) / 8 + ((x - lt.x) % 8));
						}
					}
				}
				byte b = cache[q];
				for (int yy = 0; yy < 2; yy++) {
					for (int xx = 0; xx < 2; xx++) {
						int index = yy * 2 + xx;
						if (((1 << index) & b) != 0) {
							next.map.add(new Point(x + 1 + xx, y + 1 + yy));
						}
					}
				}
			}
		}

		return next;
	}

	public void getCorners(Point lt, Point rb) {
		lt.x = lt.y = rb.x = rb.y = 0;

		for (Point p : map) {
			if (p.x < lt.x)
				lt.x = p.x;
			if (p.y < lt.y)
				lt.y = p.y;
			if (p.x > rb.x)
				rb.x = p.x;
			if (p.y > rb.y)
				rb.y = p.y;
		}
	}

	public Rectangle getRect() {
		if (map.size() == 0)
			return null;

		Point lt = new Point(0, 0);
		Point rb = new Point(0, 0);

		for (Point p : map) {
			if (p.x < lt.x)
				lt.x = p.x;
			if (p.y < lt.y)
				lt.y = p.y;
			if (p.x > rb.x)
				rb.x = p.x;
			if (p.y > rb.y)
				rb.y = p.y;
		}

		return new Rectangle(lt.x, lt.y, rb.x - lt.x + 1, rb.y - lt.y + 1);
	}

	@Override
	public Point getTopLeft() {
		// TODO Auto-generated method stub
		return null;
	}
}
